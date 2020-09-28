package de.witcom.itsm.serviceaccess.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Entity
@DiscriminatorValue("InfraOtherOperator")
//@EqualsAndHashCode(callSuper=false)
//@Data
@Getter
@Setter
//@RequiredArgsConstructor
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper=true, includeFieldNames=true)
@SuperBuilder
public class ServiceAccessInfraOtherOperator extends ServiceAccessBase{
	
	@Column(length = 5000)
	private String constraints;

	@Builder.Default
	@Column(name = "used_in_group")
	private boolean usedInGroup = false;

	
	@Builder.Default
	@ManyToMany(mappedBy = "otherOperators")
	private Set<ServiceAccessOtherOperatorGroup> operatorGroups=new HashSet<ServiceAccessOtherOperatorGroup>();
	
	public void addOperatorGroup(ServiceAccessOtherOperatorGroup group) {
		//Todo - if we want a many-to one relation, we have to replace the elements
		if (operatorGroups.contains(group))
		   return ;
		group.otherOperators.add(this);
		this.setUsedInGroup(true);
		operatorGroups.add(group);
	}

	public void removeOperatorGroup(ServiceAccessOtherOperatorGroup group) {
		if (!operatorGroups.contains(group)) {
			//System.out.println("No element to remove");
			return ;
		}
		group.otherOperators.remove(this);
		operatorGroups.remove(group);
		if (operatorGroups.isEmpty()) {
			this.setUsedInGroup(false);
		}
	}
	
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
        ServiceAccessInfraOtherOperator other = (ServiceAccessInfraOtherOperator) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }	


	
}
