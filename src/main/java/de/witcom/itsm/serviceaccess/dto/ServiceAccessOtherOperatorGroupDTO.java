package de.witcom.itsm.serviceaccess.dto;


import java.util.Set;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@RequiredArgsConstructor
@ToString(callSuper=true, includeFieldNames=true)
public class ServiceAccessOtherOperatorGroupDTO extends ServiceAccessBaseDTO{

	Set<ServiceAccessInfraOtherOperatorDTO> otherOperators;

}
