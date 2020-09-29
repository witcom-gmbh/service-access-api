package de.witcom.itsm.serviceaccess.mapper;

import java.util.Arrays;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.CollectionMappingStrategy;

import de.witcom.itsm.serviceaccess.dto.*;
import de.witcom.itsm.serviceaccess.entity.*;
import de.witcom.itsm.serviceaccess.enums.ResourceType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;

@Mapper(componentModel = "spring",uses = {EnumMapper.class,TagMapper.class},collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface ServiceAccessMapper {
	
	default ServiceAccessBaseDTO toDto(ServiceAccessBase entity) {
		if(entity instanceof ServiceAccessInfraPassive)
			return toServiceAccessInfraPassiveDTO((ServiceAccessInfraPassive) entity);
		else if (entity instanceof ServiceAccessOtherOperator)
			return toServiceAccessOtherOperatorDTO((ServiceAccessOtherOperator) entity);
		else if (entity instanceof ServiceAccessOtherOperatorGroup)
			return toServiceAccessOtherOperatorGroupDTO((ServiceAccessOtherOperatorGroup) entity);
		
		return toServiceAccessBaseDTO(entity);
	}
	default ServiceAccessBase toEntity(ServiceAccessBaseDTO dto) {
		if(dto instanceof ServiceAccessInfraPassiveDTO)
			return toServiceAccessInfraPassive((ServiceAccessInfraPassiveDTO) dto);
		else if (dto instanceof ServiceAccessOtherOperatorDTO)
			return toServiceAccessOtherOperator((ServiceAccessOtherOperatorDTO) dto);
		else if (dto instanceof ServiceAccessOtherOperatorGroupDTO)
			return toServiceAccessOtherOperatorGroup((ServiceAccessOtherOperatorGroupDTO) dto);

		return toServiceAccessBase(dto);
	}
	
	default ServiceAccessBase createUpdateFromDTO(CreateUpdateServiceAccessBaseDTO dto,@MappingTarget ServiceAccessBase entity) {
		if(dto instanceof CreateUpdateServiceAccessInfraPassiveDTO) {
			return createUpdateServiceAccessInfraPassiveFromDTO((CreateUpdateServiceAccessInfraPassiveDTO)dto,(ServiceAccessInfraPassive) entity);
		}
		else if (dto instanceof CreateUpdateServiceAccessOtherOperatorDTO) {
			return createUpdateServiceAccessOtherOperatorFromDTO((CreateUpdateServiceAccessOtherOperatorDTO)dto,(ServiceAccessOtherOperator) entity);
		}
		else if (dto instanceof CreateUpdateServiceAccessOtherOperatorGroupDTO) {
			return createUpdateServiceAccessOtherOperatorGroupFromDTO((CreateUpdateServiceAccessOtherOperatorGroupDTO)dto,(ServiceAccessOtherOperatorGroup) entity);
		}
	
		//no direct updates of base-entity
		return entity;
		
	}
	
	//Updates from DTO
	ServiceAccessInfraPassive createUpdateServiceAccessInfraPassiveFromDTO(
			CreateUpdateServiceAccessInfraPassiveDTO dto,@MappingTarget ServiceAccessInfraPassive entity);

	ServiceAccessOtherOperator createUpdateServiceAccessOtherOperatorFromDTO(
			CreateUpdateServiceAccessOtherOperatorDTO dto,@MappingTarget ServiceAccessOtherOperator entity);

	ServiceAccessOtherOperatorGroup createUpdateServiceAccessOtherOperatorGroupFromDTO(
			CreateUpdateServiceAccessOtherOperatorGroupDTO dto,@MappingTarget ServiceAccessOtherOperatorGroup entity);

	ServiceAccessBaseDTO toServiceAccessBaseDTO(ServiceAccessBase entity);
	ServiceAccessBase toServiceAccessBase(ServiceAccessBaseDTO dto);
	
	ServiceAccessInfraPassiveDTO toServiceAccessInfraPassiveDTO(ServiceAccessInfraPassive entity);
	ServiceAccessInfraPassive toServiceAccessInfraPassive(ServiceAccessInfraPassiveDTO dto);
	
	ServiceAccessOtherOperatorDTO toServiceAccessOtherOperatorDTO(ServiceAccessOtherOperator entity);
	ServiceAccessOtherOperator toServiceAccessOtherOperator(ServiceAccessOtherOperatorDTO dto);

	ServiceAccessOtherOperatorGroupDTO toServiceAccessOtherOperatorGroupDTO(ServiceAccessOtherOperatorGroup entity);
	ServiceAccessOtherOperatorGroup toServiceAccessOtherOperatorGroup(ServiceAccessOtherOperatorGroupDTO dto);
	
	ServiceAccessSubtypeDTO toServiceAccessSubtypeDTO(ServiceAccessSubtype entity);
	ServiceAccessSubtype toServiceAccessSubtype(ServiceAccessSubtypeDTO dto);

	ResourceReferenceDTO toResourceReferenceDTO(ResourceReference entity);
	ResourceReference toResourceReference(ResourceReferenceDTO dto);
	
	
	ServiceAccessSubtype updateServiceAccessSubtypeFromDTO(
			UpdateServiceAccessSubtypeDTO dto,
			@MappingTarget ServiceAccessSubtype entity);
	
	ServiceAccessSubtype createServiceAccessSubtypeFromDTO(
			CreateServiceAccessSubtypeDTO dto,
			@MappingTarget ServiceAccessSubtype entity);

	/*
	TagDTO toTagDTO(Tag entity);
	Tag toTag(TagDTO dto); 
	Tag fromCreateUpdateTagDTO(TagDTO dto,@MappingTarget Tag entity);
	*/
	
}
