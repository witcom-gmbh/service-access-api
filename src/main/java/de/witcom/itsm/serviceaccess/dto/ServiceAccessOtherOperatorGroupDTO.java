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
	
    @Override
    public int hashCode() {
        return 16;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceAccessOtherOperatorGroupDTO other = (ServiceAccessOtherOperatorGroupDTO) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }
	

}
