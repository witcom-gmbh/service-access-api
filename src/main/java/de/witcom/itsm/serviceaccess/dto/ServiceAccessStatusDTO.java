package de.witcom.itsm.serviceaccess.dto;


import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessStatusDTO {

	String name;
	String code;
	
	public static ServiceAccessStatusDTO fromEnum(ServiceAccessStatus myenum) {
		
		return new ServiceAccessStatusDTO(myenum.getName(),myenum.getCode());
	}

}
