package de.witcom.itsm.serviceaccess.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
//@EqualsAndHashCode(callSuper=false)
@ToString(callSuper=true, includeFieldNames=true)
public class ServiceAccessInfraPassiveDTO extends ServiceAccessBaseDTO{

	public ServiceAccessInfraPassiveDTO() {
		
		
	}
	
    @Override
    public int hashCode() {
        return 14;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceAccessInfraPassiveDTO other = (ServiceAccessInfraPassiveDTO) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }

	
}
