package de.witcom.itsm.serviceaccess;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import de.witcom.itsm.serviceaccess.dto.*;
import de.witcom.itsm.serviceaccess.entity.*;
import de.witcom.itsm.serviceaccess.enums.*;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.mapper.EnumMapper;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.util.Translator;

@SpringBootTest
@Transactional
class MapperTests { 
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ServiceAccessMapper saMapper;
	
	@Autowired
	EnumMapper enumMapper;
	
	ServiceAccessSubtype stHA;
	ServiceAccessSubtype stEW;

	@BeforeEach
	public void init() {
		
		stHA = ServiceAccessSubtype.builder()
				.name("HA-LWL")
				.description("LWL Hausanschluss")
				.allowedObjectType(ServiceAccessObjectType.InfraPassive)
				.numberOfEndpoints(1)
				.scope(ServiceAccessScope.UNSCOPED)
				.offerScope(ServiceAccessOfferScope.UNSCOPED)
				.build();
		
		 stEW = ServiceAccessSubtype.builder()
				.name("ETHERNET-WHOLEBUY")
				.description("Ethernet Fremdoperator")
				.allowedObjectType(ServiceAccessObjectType.InfraOtherOperator)
				.numberOfEndpoints(2)
				.scope(ServiceAccessScope.SERVICE)
				.offerScope(ServiceAccessOfferScope.SERVICE)
				.build();
		
	}
	
	public CreateUpdateServiceAccessBaseDTO getCreateUpdatePassiveDTO() {
		
		HashSet<ResourceReferenceDTO> resRef = new HashSet<ResourceReferenceDTO>();
		ResourceReferenceDTO res = new ResourceReferenceDTO();
		res.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		res.setDescription(RandomStringUtils.random(10, true, true));
		resRef.add(res );
		
		CreateUpdateServiceAccessInfraPassiveDTO dto = new CreateUpdateServiceAccessInfraPassiveDTO();
		dto.setName(RandomStringUtils.random(10, true, false));
		dto.setProjectId(RandomStringUtils.random(5, false, true));
		dto.setResources(resRef);
		dto.setSubType(saMapper.toServiceAccessSubtypeDTO(stHA));
		
		return dto;

	}

	public CreateUpdateServiceAccessBaseDTO getCreateUpdateOODTO() {
		
		HashSet<ResourceReferenceDTO> resRef = new HashSet<ResourceReferenceDTO>();
		ResourceReferenceDTO res = new ResourceReferenceDTO();
		res.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		res.setDescription(RandomStringUtils.random(10, true, true));
		resRef.add(res );
		res = new ResourceReferenceDTO();
		res.setType(ResourceTypeDTO.fromEnum(ResourceType.RMDB_ZONE));
		res.setDescription(RandomStringUtils.random(10, true, true));
		resRef.add(res );
		
		CreateUpdateServiceAccessInfraOtherOperatorDTO dto = new CreateUpdateServiceAccessInfraOtherOperatorDTO();
		dto.setName(RandomStringUtils.random(10, true, false));
		dto.setProjectId(RandomStringUtils.random(5, false, true));
		dto.setResources(resRef);
		dto.setConstraints(RandomStringUtils.random(10, true, false));
		dto.setSubType(saMapper.toServiceAccessSubtypeDTO(stEW));
		
		return dto;

	}
	
	
	ServiceAccessInfraOtherOperator createOOEntity() {

		HashSet<ResourceReference> ooRes = new HashSet<ResourceReference>();
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_ZONE)
				.referenceId("command-elid-1")
				.description("A-ENDE")
				.build());
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_ZONE)
				.referenceId("command-elid-2")
				.description("B-ENDE")
				.build());
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.RMDB_CONTRACT)
				.referenceId("command-elid-3")
				.description("Vertrag")
				.build());
		ooRes.add(ResourceReference.builder()
				.type(ResourceType.CRM_CONTACT)
				.referenceId("crm-id")
				.description("Lieferant")
				.build());
		
		ServiceAccessInfraOtherOperator infraOO = ServiceAccessInfraOtherOperator.builder()
				.name("my-name-2")
				.id("my-id")
				.resources(ooRes)
				.subType(stEW)
				.constraints("100 mbits max")
				.build();

		return infraOO;
	}
	
	
	ServiceAccessInfraPassive createInfraPassiveEntity() {
		
		ServiceAccessInfraPassive infraPassive1 = ServiceAccessInfraPassive.builder()
				.name("my-name")
				.subType(stHA)
				.build();
		
		infraPassive1.getResources().add(ResourceReference.builder()
				.type(ResourceType.RMDB_OBJECT)
				.referenceId("command-elid")
				.build()
				);
		
		return infraPassive1;
	}

	@Test
	void listMapping() {
		
		//Object 1
		
		CreateUpdateServiceAccessBaseDTO oo1 = getCreateUpdateOODTO();
		CreateUpdateServiceAccessBaseDTO oo2 = getCreateUpdateOODTO();
		
		ServiceAccessBase entity1;
		
		if (oo1 instanceof CreateUpdateServiceAccessInfraPassiveDTO) {
			entity1 = ServiceAccessInfraPassive.builder().build();
		} else if (oo1 instanceof CreateUpdateServiceAccessInfraOtherOperatorDTO) {
			entity1 = ServiceAccessInfraOtherOperator.builder().build();
		} else if (oo1 instanceof CreateUpdateServiceAccessOtherOperatorGroupDTO) {
			entity1 = ServiceAccessOtherOperatorGroup.builder().build();
		} else {
			throw new BadRequestException(
					Translator.toLocale(
					"error.serviceaccess.create.objecttypenotfound"
					,new Object[] {oo1.getClass().getSimpleName()}
					)
					);
		}
		
		ServiceAccessBase entity2;
		
		if (oo2 instanceof CreateUpdateServiceAccessInfraPassiveDTO) {
			entity2 = ServiceAccessInfraPassive.builder().build();
		} else if (oo2 instanceof CreateUpdateServiceAccessInfraOtherOperatorDTO) {
			entity2 = ServiceAccessInfraOtherOperator.builder().build();
		} else if (oo2 instanceof CreateUpdateServiceAccessOtherOperatorGroupDTO) {
			entity2 = ServiceAccessOtherOperatorGroup.builder().build();
		} else {
			throw new BadRequestException(
					Translator.toLocale(
					"error.serviceaccess.create.objecttypenotfound"
					,new Object[] {oo2.getClass().getSimpleName()}
					)
					);
		}

		saMapper.createUpdateFromDTO(oo1, entity1);
		ServiceAccessBaseDTO dto1 = saMapper.toDto(entity1);
		saMapper.createUpdateFromDTO(oo2, entity2);
		ServiceAccessBaseDTO dto2 = saMapper.toDto(entity2);
		
		//saMapper.createUpdateFromDTO(oo2, entity)
		
		CreateUpdateServiceAccessOtherOperatorGroupDTO ooGroupDTO = new CreateUpdateServiceAccessOtherOperatorGroupDTO();
		ooGroupDTO.setName("my-group");

		log.debug(ooGroupDTO.toString());
		
		ServiceAccessBase groupentity = ServiceAccessOtherOperatorGroup.builder().build();
		
		//Map to entity
		saMapper.createUpdateFromDTO(ooGroupDTO, groupentity);
		log.debug(groupentity.toString());
		ServiceAccessOtherOperatorGroup groupentity2 =  (ServiceAccessOtherOperatorGroup) groupentity;
		assertThat( groupentity2.getName(),is("my-group"));
		
		
	}
	@Test
	void mappingForSACreation() {
		
		CreateUpdateServiceAccessBaseDTO dto = getCreateUpdatePassiveDTO();
		
		ServiceAccessBase entity;
		
		if (dto instanceof CreateUpdateServiceAccessInfraPassiveDTO) {
			entity = ServiceAccessInfraPassive.builder().build();
		} else if (dto instanceof CreateUpdateServiceAccessInfraOtherOperatorDTO) {
			entity = ServiceAccessInfraOtherOperator.builder().build();
		} else if (dto instanceof CreateUpdateServiceAccessOtherOperatorGroupDTO) {
			entity = ServiceAccessOtherOperatorGroup.builder().build();
		} else {
			throw new BadRequestException(
					Translator.toLocale(
					"error.serviceaccess.create.objecttypenotfound"
					,new Object[] {dto.getClass().getSimpleName()}
					)
					);
		}
		//dto.setSubType(saMapper.toServiceAccessSubtypeDTO(st));
		
		saMapper.createUpdateFromDTO(dto, entity);
		assertThat(entity.getResources(),hasSize(1));
		assertThat(entity.getTags(),hasSize(2));
		assertThat(entity.getSubType().getName(),is(stHA.getName()));
		
	}

}
