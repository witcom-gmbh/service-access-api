package de.witcom.itsm.serviceaccess.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("OtherOperatorGroup")
//@EqualsAndHashCode(callSuper=false)
//@Data
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
//@ToString(callSuper=true, includeFieldNames=true)
public class ServiceAccessOtherOperatorGroup extends ServiceAccessBase{
	
	
	@Builder.Default
    @ManyToMany()
    @JoinTable(
      name = "oogroup_oo", 
      joinColumns = @JoinColumn(name = "oogroup_id"), 
      inverseJoinColumns = @JoinColumn(name = "oo_id"))
	Set<ServiceAccessInfraOtherOperator> otherOperators = new HashSet<ServiceAccessInfraOtherOperator>();
	
	public void addOtherOperator(ServiceAccessInfraOtherOperator operator) {
		if (otherOperators.contains(operator)) {
			return;
		}
		operator.getOperatorGroups().add(this);
		operator.setUsedInGroup(true);
		otherOperators.add(operator);
	}

	public void removeOtherOperator(ServiceAccessInfraOtherOperator operator) {
		if (!otherOperators.contains(operator)) {
			return;
		}
		operator.getOperatorGroups().remove(this);
		if (operator.getOperatorGroups().isEmpty()) {
			operator.setUsedInGroup(false);
		}
		otherOperators.remove(operator);
	}
	
    public void updateOperators(Set<ServiceAccessInfraOtherOperator> updatedOperators) {
    	
    	Set<ServiceAccessInfraOtherOperator> safeCopy = new HashSet<ServiceAccessInfraOtherOperator>(this.getOtherOperators());
    	for (ServiceAccessInfraOtherOperator oo:safeCopy) {
			if (!updatedOperators.contains(oo)) {
				this.removeOtherOperator(oo);
			}
		}
		for (ServiceAccessInfraOtherOperator oo : updatedOperators) {
			this.addOtherOperator(oo);
		}
    }
	
	
	public String toString() {
		
		String ret = "ServiceAccessOtherOperatorGroup(id="+getId()+",name=" + getName() +
				", tags=" + getTags().toString() +
				",resources="+getResources().toString() + ",numberOfOtherOperators="+getOtherOperators().size()+ ")]";
		return ret;
		
		
	}
	
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
        ServiceAccessOtherOperatorGroup other = (ServiceAccessOtherOperatorGroup) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }		

	
}
