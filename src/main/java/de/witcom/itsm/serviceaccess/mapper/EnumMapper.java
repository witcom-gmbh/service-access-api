package de.witcom.itsm.serviceaccess.mapper;

import java.util.Arrays;

import org.mapstruct.Mapper;

import de.witcom.itsm.serviceaccess.dto.ResourceTypeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessObjectTypeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOfferScopeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessScopeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessStatusDTO;
import de.witcom.itsm.serviceaccess.enums.ResourceType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;

@Mapper(componentModel = "spring")
public interface EnumMapper {
	
	default ServiceAccessOfferScopeDTO enumToDto(ServiceAccessOfferScope myenum) {
	    return new ServiceAccessOfferScopeDTO(myenum.getName(), myenum.getCode());
	}

	default ServiceAccessOfferScope dtoToEnum(ServiceAccessOfferScopeDTO dto) {
		if (dto.getCode() == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessOfferScope.values())
				.filter(c -> c.getCode().equals(dto.getCode()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
	
	default ServiceAccessScopeDTO enumToDto(ServiceAccessScope myenum) {
	    return new ServiceAccessScopeDTO(myenum.getName(), myenum.getCode());
	}

	default ServiceAccessScope dtoToEnum(ServiceAccessScopeDTO dto) {
		if (dto.getCode() == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessScope.values())
				.filter(c -> c.getCode().equals(dto.getCode()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	default ServiceAccessStatusDTO enumToDto(ServiceAccessStatus myenum) {
	    return new ServiceAccessStatusDTO(myenum.getName(), myenum.getCode());
	}

	default ServiceAccessStatus dtoToEnum(ServiceAccessStatusDTO dto) {
		if (dto.getCode() == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessStatus.values())
				.filter(c -> c.getCode().equals(dto.getCode()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	default ResourceTypeDTO enumToDto(ResourceType myenum) {
	    return new ResourceTypeDTO(myenum.getName(), myenum.getCode());

	}

	default ResourceType dtoToEnum(ResourceTypeDTO dto) {
		if (dto.getCode() == null) {
            return null;
        }
		return Arrays.stream(ResourceType.values())
				.filter(c -> c.getCode().equals(dto.getCode()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}

	default ServiceAccessObjectTypeDTO enumToDto(ServiceAccessObjectType myenum) {
	    return new ServiceAccessObjectTypeDTO(myenum.getName(), myenum.getCode());

	}

	default ServiceAccessObjectType dtoToEnum(ServiceAccessObjectTypeDTO dto) {
		if (dto.getCode() == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessObjectType.values())
				.filter(c -> c.getCode().equals(dto.getCode()))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
	}
	
	
}
