package de.witcom.itsm.serviceaccess.dto;


import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessScopeDTO {

	String name;
	String code;
	
	public static ServiceAccessScopeDTO fromEnum(ServiceAccessScope myenum) {
		
		return new ServiceAccessScopeDTO(myenum.getName(),myenum.getCode());
	}

}
