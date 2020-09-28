package de.witcom.itsm.serviceaccess.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("InfraPassive")
//@EqualsAndHashCode(callSuper=false)
//@Data
@Getter
@Setter
@SuperBuilder

@ToString(callSuper=true, includeFieldNames=true)
public class ServiceAccessInfraPassive extends ServiceAccessBase{
	
	public ServiceAccessInfraPassive() {
		
		
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
        ServiceAccessInfraPassive other = (ServiceAccessInfraPassive) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }	
	
	
}
