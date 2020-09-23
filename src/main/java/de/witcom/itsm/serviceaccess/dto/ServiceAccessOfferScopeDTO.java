package de.witcom.itsm.serviceaccess.dto;


import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessOfferScopeDTO {

	String name;
	String code;
	
	public static ServiceAccessOfferScopeDTO fromEnum(ServiceAccessOfferScope myenum) {
		
		return new ServiceAccessOfferScopeDTO(myenum.getName(),myenum.getCode());
	}

}
