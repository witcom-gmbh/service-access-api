package de.witcom.itsm.serviceaccess.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper=true, includeFieldNames=true)
public class CreateUpdateServiceAccessOtherOperatorDTO extends CreateUpdateServiceAccessBaseDTO{
	
	private String constraints;


}
