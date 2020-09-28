package de.witcom.itsm.serviceaccess;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.beans.HasPropertyWithValue.hasProperty;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
//import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.hamcrest.collection.IsIterableContainingInAnyOrder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import de.witcom.itsm.serviceaccess.dto.*;
import de.witcom.itsm.serviceaccess.entity.*;
import de.witcom.itsm.serviceaccess.enums.*;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.exception.PersistenceException;
import de.witcom.itsm.serviceaccess.mapper.EnumMapper;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.mapper.TagMapper;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessRepository;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessSubtypeRepository;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;
import de.witcom.itsm.serviceaccess.service.TagService;
import de.witcom.itsm.serviceaccess.service.impl.ServiceAccessServiceImpl;

@SpringBootTest
//@Transactional
class ServiceTests { 
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ServiceAccessMapper saMapper;
	@Autowired
	EnumMapper enumMapper;
	
	@Autowired
	TagMapper tagMapper;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	ServiceAccessService saService;
	
	@Autowired
	ServiceAccessRepository saRepo;

	@BeforeAll
	public static void init(@Autowired ServiceAccessServiceImpl saService) {

		ServiceAccessSubtype st = ServiceAccessSubtype.builder()
				.name("HA-LWL")
				.description("LWL Hausanschluss")
				.numberOfEndpoints(1)
				.scope(ServiceAccessScope.UNSCOPED)
				.offerScope(ServiceAccessOfferScope.UNSCOPED)
				.allowedObjectType(ServiceAccessObjectType.InfraPassive)
				.build();
		//st = subtypeRepo.save(st);
		
		saService.createOrUpdateSubtype(st);

		st =  ServiceAccessSubtype.builder()
				.name("ETHERNET-WHOLEBUY")
				.description("Ethernet Fremdoperator")
				.numberOfEndpoints(2)
				.allowedObjectType(ServiceAccessObjectType.InfraOtherOperator)
				.scope(ServiceAccessScope.SERVICE)
				.offerScope(ServiceAccessOfferScope.SERVICE)
				.build();

		saService.createOrUpdateSubtype(st);

	}

	public CreateUpdateServiceAccessBaseDTO getCreateUpdatePassiveDTO() {
		
		
		ServiceAccessSubtypeDTO st = saService.getSubtypeByName("HA-LWL").orElseThrow();
		
		HashSet<ResourceReferenceDTO> resRef = new HashSet<ResourceReferenceDTO>();
		ResourceReferenceDTO res = new ResourceReferenceDTO();
		res.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		res.setReferenceId(RandomStringUtils.random(10, true, true));
		resRef.add(res );
		
		CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
		dto.setName(RandomStringUtils.random(10, true, false));
		dto.setProjectId(RandomStringUtils.random(5, false, true));
		dto.setResources(resRef);
		dto.setSubType(st);
		
		/*
		TagDTO tag = new TagDTO();
		tag.setTagName(RandomStringUtils.random(10, true, true));
		dto.getTags().add(tag);
		tag = new TagDTO();
		tag.setTagName(RandomStringUtils.random(10, true, true));
		dto.getTags().add(tag);
		*/
		
		return dto;

	}

	public CreateUpdateServiceAccessBaseDTO getCreateUpdateOODTO() {
		
		ServiceAccessSubtypeDTO st = saService.getSubtypeByName("ETHERNET-WHOLEBUY").orElseThrow();

		HashSet<ResourceReferenceDTO> resRef = new HashSet<ResourceReferenceDTO>();
		ResourceReferenceDTO res = new ResourceReferenceDTO();
		res.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		res.setDescription(RandomStringUtils.random(10, true, true));
		resRef.add(res );
		res = new ResourceReferenceDTO();
		res.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		res.setReferenceId(RandomStringUtils.random(10, true, true));
		resRef.add(res );
		
		CreateUpdateServiceAccessInfraOtherOperatorDTO dto = new CreateUpdateServiceAccessInfraOtherOperatorDTO();
		dto.setName(RandomStringUtils.random(10, true, false));
		dto.setProjectId(RandomStringUtils.random(5, false, true));
		dto.setResources(resRef);
		dto.setConstraints(RandomStringUtils.random(10, true, false));
		dto.setSubType(st);
		
		/*
		TagDTO tag = new TagDTO();
		tag.setTagName(RandomStringUtils.random(10, true, true));
		dto.getTags().add(tag);
		tag = new TagDTO();
		tag.setTagName(RandomStringUtils.random(10, true, true));
		dto.getTags().add(tag);
		*/
		return dto;

	}

	
	@Test
	void lookuptests() {
		
		assertThat(saService.getSubtypeByName("HA-LWL"),is(not(Optional.empty())));
		assertThat(saService.getSubtypeByName("DUMMY"),is(Optional.empty()));
		assertThat(saService.getSubtypes(),is(not(empty())));
		
		
	}
	
	void createAndUpdateOtherOperatorGroupFromDTO() {
		
		//First - we need 2 oo's
		
		CreateUpdateServiceAccessBaseDTO ooDto1 = getCreateUpdateOODTO();
		CreateUpdateServiceAccessBaseDTO ooDto2 = getCreateUpdateOODTO();
		
		ServiceAccessBaseDTO persistedDto1 = saService.createServiceAccess(ooDto1);
		ServiceAccessBaseDTO persistedDto2 = saService.createServiceAccess(ooDto2);
		
		//Now we create a group
		ServiceAccessSubtypeDTO st = saService.getSubtypeByName("ETHERNET-WHOLEBUY").orElseThrow();
		CreateUpdateServiceAccessOtherOperatorGroupDTO ooGroupDTO = new CreateUpdateServiceAccessOtherOperatorGroupDTO();
		//ooGroupDTO.getOtherOperators().add((ServiceAccessInfraOtherOperatorDTO) persistedDto1);
		//ooGroupDTO.getOtherOperators().add((ServiceAccessInfraOtherOperatorDTO) persistedDto2);
		ooGroupDTO.setName("my-group");
		ooGroupDTO.setSubType(st);
		
		ServiceAccessOtherOperatorGroupDTO persistedGroup = (ServiceAccessOtherOperatorGroupDTO) saService.createServiceAccess(ooGroupDTO);
		log.debug(persistedGroup.toString());
		
		//lets add one operator to the group
		List<ServiceAccessInfraOtherOperatorDTO> otherOperators = new ArrayList<ServiceAccessInfraOtherOperatorDTO>();
		
		otherOperators.add((ServiceAccessInfraOtherOperatorDTO) persistedDto1);
		saService.updateOtherOperators(persistedGroup.getId(), otherOperators);
		persistedGroup = (ServiceAccessOtherOperatorGroupDTO) saService.getServiceAccessById(persistedGroup.getId()).get();
		
		//load the service service that builds the group and check if has been marked as used in group
		ServiceAccessInfraOtherOperatorDTO loaded = (ServiceAccessInfraOtherOperatorDTO) saService.getServiceAccessById(persistedDto1.getId()).get();
		assertThat(loaded.isUsedInGroup(),is(true));
		
		//remove the group
		otherOperators = new ArrayList<ServiceAccessInfraOtherOperatorDTO>();
		saService.updateOtherOperators(persistedGroup.getId(), otherOperators);
		loaded = (ServiceAccessInfraOtherOperatorDTO) saService.getServiceAccessById(persistedDto1.getId()).get();
		assertThat(loaded.isUsedInGroup(),is(false));
		
		/*
		TagDTO tag = new TagDTO();
		tag.setTagName(RandomStringUtils.random(10, true, true));
		ooGroupDTO.getTags().add(tag);
		tag = new TagDTO();
		tag.setTagName(RandomStringUtils.random(10, true, true));
		ooGroupDTO.getTags().add(tag);
		*/
	}

	@Test
	@Transactional
	void testServiceAccessAndTags() {
		
		CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
		dto.setTagName("my-first-tag");
		Tag persistedTag = tagService.createTag(dto);

		CreateUpdateServiceAccessBaseDTO createDTO = getCreateUpdatePassiveDTO();

		ServiceAccessBaseDTO persisted = saService.createServiceAccess(createDTO);
		assertThat(persisted.getTags(), hasSize(0));
		
		List<TagDTO> tags = new ArrayList<TagDTO>();
		tags.add(tagMapper.toTagDTO(persistedTag));
		saService.updateTags(persisted.getId(), tags );
		
		ServiceAccessBaseDTO mysa = saService.getServiceAccessById(persisted.getId()).get();
		assertThat(mysa.getTags(), hasSize(1));

		//add another tag to service
		dto = new CreateUpdateTagDTO();
		dto.setTagName("my-second-tag");
		Tag persistedTag2 = tagService.createTag(dto);
		tags.add(tagMapper.toTagDTO(persistedTag2));
		saService.updateTags(persisted.getId(), tags );
		mysa = saService.getServiceAccessById(persisted.getId()).get();
		assertThat(mysa.getTags(), hasSize(2));
		
		//delete one tag from database
		tagService.deleteTag(persistedTag2.getId());
		assertThat(tagService.getTagById(persistedTag2.getId()),is(Optional.empty()));
		mysa = saService.getServiceAccessById(persisted.getId()).get();
		assertThat(mysa.getTags(), hasSize(1));
		
		//remove all tags from service
		saService.updateTags(persisted.getId(), new ArrayList<TagDTO>() );
		mysa = saService.getServiceAccessById(persisted.getId()).get();
		assertThat(mysa.getTags(), hasSize(0));
		
	}
	
	@Test
	void resourceTests() {
		
		ServiceAccessSubtypeDTO st = saService.getSubtypeByName("HA-LWL").orElseThrow();
		
		CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
		dto.setName("resource-tests");
		dto.setSubType(st);
		
		
		ResourceReferenceDTO resref = new ResourceReferenceDTO();
		resref.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_OBJECT));
		resref.setReferenceId(RandomStringUtils.random(10, true, true));
		dto.getResources().add(resref);
		
		resref = new ResourceReferenceDTO();
		resref.setType(ResourceTypeDTO.fromEnum(ResourceType.CRM_CONTACT));
		resref.setReferenceId(RandomStringUtils.random(10, true, true));
		dto.getResources().add(resref);
		
		resref = new ResourceReferenceDTO();
		resref.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_CONTRACT));
		resref.setReferenceId(RandomStringUtils.random(10, true, true));
		dto.getResources().add(resref);
		
		resref = new ResourceReferenceDTO();
		resref.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_NNI));
		resref.setReferenceId(RandomStringUtils.random(10, true, true));
		dto.getResources().add(resref);
		
		resref = new ResourceReferenceDTO();
		resref.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		resref.setReferenceId(RandomStringUtils.random(10, true, true));
		dto.getResources().add(resref);

		ServiceAccessBaseDTO createdDto = saService.createServiceAccess(dto);
		
		ServiceAccessBaseDTO loaded = saService.getServiceAccessById(createdDto.getId()).get();
		assertThat(loaded.getResources(),hasSize(5));
		
	}
	
	@Test
	//@Transactional
	void createAndUpdateServiceAccessFromDTO() {
		
		ServiceAccessSubtypeDTO st = saService.getSubtypeByName("HA-LWL").orElseThrow();
		
		CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
		dto.setName("new-name");
		dto.setSubType(st);
		dto.setProjectId("12345");
		
		ResourceReferenceDTO resref = new ResourceReferenceDTO();
		resref.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_OBJECT));
		resref.setReferenceId(RandomStringUtils.random(10, true, true));
		dto.getResources().add(resref);
		

		ServiceAccessBaseDTO createdDto = saService.createServiceAccess(dto);
		log.debug(createdDto.toString());
		
		ServiceAccessBaseDTO loaded = saService.getServiceAccessById(createdDto.getId()).get();
		log.debug(loaded.toString());
		assertThat(loaded.getResources(),hasSize(1));
		
		
		dto.setProjectId("0815");
		log.debug("Update DTO {}",dto.toString());
		createdDto = saService.updateServiceAccess(createdDto.getId(), dto);
		assertThat(createdDto.getProjectId(),is("0815"));
		
	}
	
	@Test
	void findServiceAccessByObjectType() {

		ServiceAccessSubtypeDTO st = saService.getSubtypeByName("HA-LWL").orElseThrow();
		
		CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
		dto.setName(RandomStringUtils.random(10, true, true));
		dto.setSubType(st);
		dto.setProjectId("12345");
		
		ServiceAccessBaseDTO created = saService.createServiceAccess(dto);
		
		List<ServiceAccessBase> res = saService.getServiceAccess(ServiceAccessObjectType.InfraPassive);
		assertThat(
				  res,
				  hasItem(allOf(
				    Matchers.<ServiceAccessBase>hasProperty("id", is(created.getId()))
				  ))
				);
		
		saRepo.deleteById(created.getId());
		
	}

	@Test
	void createServiceAccessWithInvalidSubtypeFromDTO() {

		assertThrows(BadRequestException.class, () -> {
			
			ServiceAccessSubtypeDTO st = saService.getSubtypeByName("ETHERNET-WHOLEBUY").orElseThrow();
			CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
			dto.setName("new-name");
			dto.setSubType(st);
			dto.setProjectId("12345");
			saService.createServiceAccess(dto);
		});
		
	}

	
	@Test
	void createServiceAccessWithEmptySubtypeFromDTO() {

		assertThrows(NullPointerException.class, () -> {
			CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
			ServiceAccessSubtypeDTO stFalse = saMapper.toServiceAccessSubtypeDTO(ServiceAccessSubtype.builder().build());
			dto.setName("new-name");
			dto.setSubType(stFalse);
			dto.setProjectId("12345");
			saService.createServiceAccess(dto);
		});
		
	}

	@Test
	void createServiceAccessWithNXSubtypeFromDTO() {

		assertThrows(NullPointerException.class, () -> {
			CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
			ServiceAccessSubtypeDTO stFalse = saMapper.toServiceAccessSubtypeDTO(ServiceAccessSubtype
					.builder()
					.scope(ServiceAccessScope.UNSCOPED)
					.offerScope(ServiceAccessOfferScope.UNSCOPED)
					.subtypeId(12L)
					.name("TESTMICH")
					.build());
			dto.setName("new-name");
			dto.setSubType(stFalse);
			dto.setProjectId("12345");
			saService.createServiceAccess(dto);
		});
		
	}

	
	@Test
	void createServiceAccessWithNullSubtypeFromDTO() {

		assertThrows(BadRequestException.class, () -> {
			
			CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
			dto.setName("new-name");
			dto.setSubType(null);
			dto.setProjectId("12345");
			saService.createServiceAccess(dto);
		});
		
	}

	@Test
	void createUpdateDeleteSubtypeFromDTO() {
	
		//Create
		CreateServiceAccessSubtypeDTO dto = new CreateServiceAccessSubtypeDTO();
		dto.setName("HA-LWL-TEST");
		dto.setNumberOfEndpoints(1);
		dto.setAllowedObjectType(ServiceAccessObjectTypeDTO.fromEnum(ServiceAccessObjectType.InfraPassive));
		dto.setOfferScope(ServiceAccessOfferScopeDTO.fromEnum(ServiceAccessOfferScope.UNSCOPED));
		dto.setScope(ServiceAccessScopeDTO.fromEnum(ServiceAccessScope.UNSCOPED));
		
		ServiceAccessSubtypeDTO persistedDto = saService.createSubtype(dto);
		assertThat(persistedDto.getSubtypeId(),is(notNullValue()));
		
		//Update
		UpdateServiceAccessSubtypeDTO updatedDto = new UpdateServiceAccessSubtypeDTO();
		updatedDto.setDescription("DESCRIPTION");
		persistedDto = saService.updateSubtype(persistedDto.getSubtypeId(), updatedDto);
		assertThat(persistedDto.getDescription(),is("DESCRIPTION"));
		
		//Delete 
		saService.deleteSubtype(persistedDto.getSubtypeId());
		
	}

	@Test
	void updateNXSubtype() {
		
		
		assertThrows(NotFoundException.class, () -> {
			UpdateServiceAccessSubtypeDTO updatedDto = new UpdateServiceAccessSubtypeDTO();
			updatedDto.setDescription("DESCRIPTION");
			saService.updateSubtype(99L, updatedDto);
	    });
		
	}
	
	
	@Test
	void createSubtypeWithDuplicateName() {
		
		assertThrows(PersistenceException.class, () -> {
			
			CreateServiceAccessSubtypeDTO dto = new CreateServiceAccessSubtypeDTO();
			dto.setName("HA-LWL");
			dto.setNumberOfEndpoints(1);
			dto.setAllowedObjectType(ServiceAccessObjectTypeDTO.fromEnum(ServiceAccessObjectType.InfraPassive));
			dto.setOfferScope(ServiceAccessOfferScopeDTO.fromEnum(ServiceAccessOfferScope.UNSCOPED));
			dto.setScope(ServiceAccessScopeDTO.fromEnum(ServiceAccessScope.UNSCOPED));
			
			saService.createSubtype(dto);
			
	    });
		
		
	}


}
