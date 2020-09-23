package de.witcom.itsm.serviceaccess.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true, includeFieldNames=true)
public class ServiceAccessInfraPassiveDTO extends ServiceAccessBaseDTO{

	public ServiceAccessInfraPassiveDTO() {
		
		
	}
	
}
