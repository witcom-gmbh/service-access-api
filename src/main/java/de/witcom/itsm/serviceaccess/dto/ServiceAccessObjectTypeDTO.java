package de.witcom.itsm.serviceaccess.dto;


import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessObjectTypeDTO {

	String name;
	String code;
	
	public static ServiceAccessObjectTypeDTO fromEnum(ServiceAccessObjectType myenum) {
		
		return new ServiceAccessObjectTypeDTO(myenum.getName(),myenum.getCode());
	}

}
