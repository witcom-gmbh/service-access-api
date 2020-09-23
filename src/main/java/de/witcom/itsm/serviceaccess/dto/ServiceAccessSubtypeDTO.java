package de.witcom.itsm.serviceaccess.dto;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessSubtypeDTO {
	
	private Long subtypeId;
	private String name;
	private String description;
	private ServiceAccessObjectTypeDTO allowedObjectType;
	private Integer numberOfEndpoints;
	private ServiceAccessOfferScopeDTO offerScope = ServiceAccessOfferScopeDTO.fromEnum(ServiceAccessOfferScope.UNSCOPED);
	private ServiceAccessScopeDTO scope = ServiceAccessScopeDTO.fromEnum(ServiceAccessScope.UNSCOPED);;

}
