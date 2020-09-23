package de.witcom.itsm.serviceaccess.mapper;

import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import de.witcom.itsm.serviceaccess.dto.CreateUpdateTagDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.entity.Tag;

@Mapper(componentModel = "spring",collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface TagMapper {
	
	TagDTO toTagDTO(Tag entity);
	Tag toTag(TagDTO dto); 
	Tag fromCreateUpdateTagDTO(CreateUpdateTagDTO dto,@MappingTarget Tag entity);


}
