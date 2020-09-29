package de.witcom.itsm.serviceaccess.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
//@EqualsAndHashCode(callSuper=false)
@AllArgsConstructor
@NoArgsConstructor
@ToString(callSuper=true, includeFieldNames=true)
public class ServiceAccessOtherOperatorDTO extends ServiceAccessBaseDTO{
	
	private boolean usedInGroup;
	private String constraints;
	
	
    @Override
    public int hashCode() {
        return 15;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceAccessOtherOperatorDTO other = (ServiceAccessOtherOperatorDTO) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }



}
