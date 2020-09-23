package de.witcom.itsm.serviceaccess.dto;


import java.util.HashSet;
import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper=true, includeFieldNames=true)
public class CreateUpdateServiceAccessOtherOperatorGroupDTO extends CreateUpdateServiceAccessBaseDTO{

	//Set<ServiceAccessInfraOtherOperatorDTO> otherOperators = new HashSet<ServiceAccessInfraOtherOperatorDTO>();

}
