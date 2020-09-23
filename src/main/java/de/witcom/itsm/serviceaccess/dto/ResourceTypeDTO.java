package de.witcom.itsm.serviceaccess.dto;


import de.witcom.itsm.serviceaccess.enums.ResourceType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceTypeDTO {

	String name;
	String code;
	
	public static ResourceTypeDTO fromEnum(ResourceType myenum) {
		
		return new ResourceTypeDTO(myenum.getName(),myenum.getCode());
	}

}
