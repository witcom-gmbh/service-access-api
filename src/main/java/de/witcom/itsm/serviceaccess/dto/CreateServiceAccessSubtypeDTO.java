package de.witcom.itsm.serviceaccess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateServiceAccessSubtypeDTO {
	
	@JsonProperty(required = true)
	private String name;
	private String description;
	@JsonProperty(required = true)
	private ServiceAccessObjectTypeDTO allowedObjectType;
	@JsonProperty(required = true)
	private Integer numberOfEndpoints;
	@JsonProperty(required = true)
	private ServiceAccessOfferScopeDTO offerScope = ServiceAccessOfferScopeDTO.fromEnum(ServiceAccessOfferScope.UNSCOPED);
	@JsonProperty(required = true)
	private ServiceAccessScopeDTO scope = ServiceAccessScopeDTO.fromEnum(ServiceAccessScope.UNSCOPED);;
	

}
