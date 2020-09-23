package de.witcom.itsm.serviceaccess.service.impl;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityNotFoundException;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import de.witcom.itsm.serviceaccess.dto.CreateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessBaseDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessInfraOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessInfraPassiveDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessOtherOperatorGroupDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessBaseDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessInfraOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorGroupDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.dto.UpdateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.entity.ResourceReference;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessInfraOtherOperator;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessInfraPassive;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessOtherOperatorGroup;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessSubtype;
import de.witcom.itsm.serviceaccess.entity.Tag;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.exception.ErrorCode;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.exception.PersistenceException;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessRepository;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessSubtypeRepository;
import de.witcom.itsm.serviceaccess.repository.TagRepository;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;
import de.witcom.itsm.serviceaccess.util.Translator;

@Service
@Transactional
public class ServiceAccessServiceImpl implements ServiceAccessService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ServiceAccessRepository saRepo;
	@Autowired
	ServiceAccessSubtypeRepository subtypeRepo;
	
	@Autowired
	TagRepository tagRepo;

	@Autowired
	ServiceAccessMapper saMapper;	

	public Optional<ServiceAccessBaseDTO> getServiceAccessById(String id) {
		
		return saRepo.findById(id).stream().map(s -> saMapper.toDto(s)).findAny();
		
		//return saRepo.findById(id);
	}
	
	public List<ServiceAccessBase> getServiceAccess(){
		return saRepo.findAll();
	}


	public ServiceAccessBaseDTO updateServiceAccessStatus(String id,ServiceAccessStatus status)
			throws NotFoundException,BadRequestException,PersistenceException{
		
		ServiceAccessBase entity = saRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
		);
		
		entity.setStatus(status);
		return saMapper.toDto(this.createOrUpdateServiceAccess(entity));
		
	}
	
	/**
	 * 
	 * @param id
	 * @param otherOperators
	 * @return
	 */
	public ServiceAccessOtherOperatorGroupDTO updateOtherOperators(String id,
			List<ServiceAccessInfraOtherOperatorDTO> otherOperators) {
		
		ServiceAccessBase entity = saRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
		);
		if (!(entity instanceof ServiceAccessOtherOperatorGroup)) {
			throw new BadRequestException(
					Translator.toLocale(
					"error.serviceaccess.generic.badrequest"
					,new Object[] {entity.getClass().getSimpleName()}
					)
					);
		}
		ServiceAccessOtherOperatorGroup groupEntity = (ServiceAccessOtherOperatorGroup) entity;
		
		
		Set<ServiceAccessInfraOtherOperator> updatedChilds = otherOperators.stream().map(oo -> {
			
			if (oo.getId()==null) {
				throw new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {Tag.class.getSimpleName(),"NULL"}));
			}
			ServiceAccessBase childentity = saRepo.findById(oo.getId()).orElseThrow(
					() -> new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {Tag.class.getSimpleName(),id}))
			);
			
			if (!(childentity instanceof ServiceAccessInfraOtherOperator)) {
				log.error("Trying to add ServiceAccess {} on Non-ServiceAccessInfraOtherOperator to InfraOtherGroup",childentity.toString());
				throw new BadRequestException(
						Translator.toLocale(
						"error.serviceaccess.generic.badrequest"
						,new Object[] {entity.getClass().getSimpleName()}
						)
						);
			}
			
			return (ServiceAccessInfraOtherOperator)childentity;
		}).collect(Collectors.toSet());

		//update membership
		groupEntity.updateOperators(updatedChilds);
		//persist
		//this.crea
		
		return (ServiceAccessOtherOperatorGroupDTO) saMapper.toDto(this.createOrUpdateServiceAccess(groupEntity));
		
	}
	
	public ServiceAccessBaseDTO updateTags(String id,List<TagDTO> tags) {
		
		ServiceAccessBase entity = saRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
		);

		//check if tags are valid
		Set<Tag> updatedTags = tags.stream().map(tag -> {
			
			if (tag.getId()==null) {
				throw new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {Tag.class.getSimpleName(),"NULL"}));
			}
			Tag tagentity = tagRepo.findById(tag.getId()).orElseThrow(
					() -> new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {Tag.class.getSimpleName(),id}))
			);
			
			return tagentity;
		}).collect(Collectors.toSet());
		
		//update list of tags
		entity.updateTags(updatedTags);
		//persist
		return saMapper.toDto(this.createOrUpdateServiceAccess(entity));
		
		
	}
	
	/**
	 * 
	 * @param CreateUpdateServiceAccessBaseDTO dto
	 * @return ServiceAccessBaseDTO - created serviceaccess 
	 * @throws BadRequestException
	 * @throws PersistenceException
	 */
	public ServiceAccessBaseDTO createServiceAccess(CreateUpdateServiceAccessBaseDTO dto)
			throws BadRequestException,PersistenceException{
		
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
		//Sanity checks
		if ((dto.getSubType()==null)||(dto.getSubType().getSubtypeId()==null)) {
			throw new BadRequestException(
					Translator.toLocale(
					"error.serviceaccess.create.stnotfound"
					,new Object[] {dto.getClass().getSimpleName()}
					)
					);	
		}
		//does subtype exist ?
		ServiceAccessSubtype st = this.getSubTypeEntityById(dto.getSubType().getSubtypeId()).orElseThrow(() -> new BadRequestException(
				Translator.toLocale(
						"error.serviceaccess.create.stnotfound"
						,new Object[] {dto.getClass().getSimpleName()}
						)
				)
				);
		
		//is subtype valid for requested ServiceAccess ?
		
		if (!entity.getClass().getSimpleName().equals(st.getAllowedObjectType().getClassname())) {
			log.error("Object {} cannot be created with subtype {}, only {} allowed"
					,entity.getClass().getSimpleName()
					,st.getName()
					,st.getAllowedObjectType()
					);
			throw new BadRequestException(
					Translator.toLocale(
					"error.serviceaccess.create.stinvalid"
					,new Object[] {dto.getClass().getSimpleName()}
					)
					);
		}
		
		//take that subtype
		dto.setSubType(saMapper.toServiceAccessSubtypeDTO(st));
		//todo subtype valid for objecttype ?
		
		saMapper.createUpdateFromDTO(dto, entity);
		//entity.get
		log.debug(entity.toString());
		//Set<ResourceReference> updatedSet = entity.getResources().stream().map(r -> {r.setServiceAccess(entity); return r;}).collect(Collectors.toSet());
		//entity.setResources(updatedSet);
	
		return saMapper.toDto(this.createOrUpdateServiceAccess(entity));
		
	}
	
	/**
	 * 
	 * @param String id
	 * @param CreateUpdateServiceAccessBaseDTO dto
	 * @return ServiceAccessBaseDTO Updated DTO
	 * @throws BadRequestException
	 * @throws PersistenceException
	 */
	public ServiceAccessBaseDTO updateServiceAccess(String id,CreateUpdateServiceAccessBaseDTO dto)
			throws BadRequestException,PersistenceException{
		ServiceAccessBase entity;
		
		if (id == null) {
			throw new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {ServiceAccessBase.class.getSimpleName(),"NULL"}));
		}
		
		entity = saRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
		);
		//keep old subtype
		dto.setSubType(saMapper.toServiceAccessSubtypeDTO(entity.getSubType()));
		
		saMapper.createUpdateFromDTO(dto, entity);
		
		entity=this.createOrUpdateServiceAccess(entity);
		
		ServiceAccessBaseDTO test = saMapper.toDto(entity);
		return saMapper.toDto(entity);
		
		
	}

	@Transactional()
	public ServiceAccessBase createOrUpdateServiceAccess(ServiceAccessBase entity) throws PersistenceException{
		//ToDo errorhandling / sanity checks

		//Resources - Sanity
		
		if (entity.getResources().stream()
				
				.filter(s -> StringUtils.isBlank(s.getReferenceId()))
				.count() > 0) {
			
			log.error("Error when persisting entity-type {} ",ServiceAccessBase.class.getSimpleName());
			Object[] args = {ServiceAccessBase.class.getSimpleName()};
			throw new BadRequestException(Translator.toLocale("error.persistence.store",args));
		}
		
		//log.debug("Before Store {}",entity.getResources().iterator().next().toString());
		//status-handling
		try {
			entity = saRepo.save(entity);
		} catch (Exception e) {
			log.error("Error when persisting entity-type {} - {} ",ServiceAccessBase.class.getSimpleName(), e.getMessage());
			Object[] args = {ServiceAccessBase.class.getSimpleName()};
			throw new PersistenceException(Translator.toLocale("error.persistence.store",args),ErrorCode.SERVER_ERROR);
		}
		//log.debug("After Store {}",entity.toString());
		//log.debug("After Store {}",entity.getResources().iterator().next().toString());
		
		return entity;
		
	}

	
	/**
	 * 
	 * @param id
	 * @return
	 * @throws NotFoundException
	 */
	public Optional<ServiceAccessSubtypeDTO> getSubtypeById(Long id) throws NotFoundException {
		
		return subtypeRepo.findById(id).stream().map(s -> saMapper.toServiceAccessSubtypeDTO(s)).findAny();
		/*
		return subtypeRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessSubtype.class.getSimpleName(),id}))
		);
		*/
	}
	
	private Optional<ServiceAccessSubtype> getSubTypeEntityById(Long id) {
		
		return subtypeRepo.findById(id);
		
	}

	/**
	 * 
	 * @param name
	 * @return
	 * @throws NotFoundException
	 */
	public Optional<ServiceAccessSubtypeDTO> getSubtypeByName(String name) throws NotFoundException {
		
		return subtypeRepo.findOneByName(name).stream().map(s -> saMapper.toServiceAccessSubtypeDTO(s)).findAny();
				
		/*
		return subtypeRepo.findOneByName(name).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessSubtype.class.getSimpleName(),name}))
		);
		*/
	}
	
	/**
	 * 
	 * @return List of all ServiceAccessSubtypes
	 */
	public List<ServiceAccessSubtypeDTO> getSubtypes() {
		return subtypeRepo.findAll()
				.stream()
				.map(s -> saMapper.toServiceAccessSubtypeDTO(s))
				.collect(Collectors.toList());
	}
	
	/**
	 * Creates ServiceAccessSubtype from DTO
	 *  
	 * @param CreateServiceAccessSubtypeDTO dto
	 * @return created ServiceAccessSubtypeDTO
	 * @throws PersistenceException
	 */
	//@Transactional()
	public ServiceAccessSubtypeDTO createSubtype(CreateServiceAccessSubtypeDTO dto) throws PersistenceException{
		ServiceAccessSubtype entity = new ServiceAccessSubtype();
		//sanity check
		//valid scope ?
		if (ServiceAccessScope.valueOfCode(dto.getScope().getCode()).isEmpty()) {
			throw new BadRequestException(
					Translator.toLocale(
							"error.serviceaccess.create.scopenotfound"
							,new Object[] {dto.getScope().getCode()}
							)
			);
		}
		//valid offerscope ?
		if (ServiceAccessOfferScope.valueOfCode(dto.getOfferScope().getCode()).isEmpty()) {
			throw new BadRequestException(
					Translator.toLocale(
							"error.serviceaccess.create.offerscopenotfound"
							,new Object[] {dto.getOfferScope().getCode()}
							)
			);
		}
		//valid object type ?
		/*
		if (ServiceAccessObjectType.valueOf(dto.getAllowedObjectType().name()) == null) {
			throw new BadRequestException(
					Translator.toLocale(
							"error.serviceaccess.create.objecttypenotfound"
							,new Object[] {dto.getAllowedObjectType().name()}
							)
			);
		}
		*/
		
		//endpoints valid ?
		saMapper.createServiceAccessSubtypeFromDTO(dto, entity);
		return saMapper.toServiceAccessSubtypeDTO(createOrUpdateSubtype(entity));
	}
	
	/**
	 * 
	 * @param Long subtypeId
	 * @param UpdateServiceAccessSubtypeDTO dto
	 * @return
	 * @throws PersistenceException
	 */
	@Transactional()
	public ServiceAccessSubtypeDTO updateSubtype(Long subtypeId, UpdateServiceAccessSubtypeDTO dto) throws NotFoundException,PersistenceException{
		
		ServiceAccessSubtype entity;
		if (subtypeId == null) {
			throw new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {ServiceAccessSubtype.class.getSimpleName(),"NULL"}));
		}

		//load subtype 
		entity = subtypeRepo.findById(subtypeId).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessSubtype.class.getSimpleName(),subtypeId}))
		);
		saMapper.updateServiceAccessSubtypeFromDTO(dto, entity);
		return saMapper.toServiceAccessSubtypeDTO(createOrUpdateSubtype(entity));
	
	}

	
	/**
	 * 
	 * @param entity
	 * @return
	 * @throws PersistenceException
	 */
	@Transactional()
	public ServiceAccessSubtype createOrUpdateSubtype(ServiceAccessSubtype entity) throws PersistenceException{
		//ToDo errorhandling / sanity checks
		
		try {
			entity = subtypeRepo.save(entity);
		} catch (Exception e) {
			log.error("Error when persisting entity-type {} : {} ",ServiceAccessSubtype.class.getSimpleName(), e.getMessage());
			Object[] args = {ServiceAccessSubtype.class.getSimpleName()};
			throw new PersistenceException(Translator.toLocale("error.persistence.store",args),ErrorCode.SERVER_ERROR);
		}
		return entity;
		
	}

	/**
	 * Deletes Service-Access-Subtype, but only if no ServiceAccess is using it
	 * 
	 * @param entity
	 * @throws PersistenceException
	 */
	@Transactional
	public void deleteSubtype(ServiceAccessSubtype entity) throws PersistenceException{
		if (saRepo.findBySubType(entity).size() > 0) {
			log.error("Cannot delete {} {} - in use",ServiceAccessSubtype.class.getSimpleName(), entity.getName());
			Object[] args = {ServiceAccessSubtype.class.getSimpleName(),entity.getName()};
			throw new PersistenceException(Translator.toLocale("error.persistence.delete",args),ErrorCode.SERVER_ERROR);
		}
		subtypeRepo.delete(entity);
	}

	/**
	 * Deletes Service-Access-Subtype by id, but only if no ServiceAccess is using it
	 * 
	 * @param entity
	 * @throws PersistenceException
	 */
	public void deleteSubtype(Long id) throws PersistenceException{
		
		if(id==null) {
			throw new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {ServiceAccessSubtype.class.getSimpleName(),"NULL"}));
		}
			
		ServiceAccessSubtype entity = subtypeRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {ServiceAccessSubtype.class.getSimpleName(),id}))
		);
		this.deleteSubtype(entity);
		
	}

}
