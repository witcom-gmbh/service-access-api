package de.witcom.itsm.serviceaccess.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.witcom.itsm.serviceaccess.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "resource_reference")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ResourceReference {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

    @ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name="sa_id",referencedColumnName = "id", nullable=false,insertable = false, updatable = false)
    @JoinColumn(name="sa_id",referencedColumnName = "id")
    private ServiceAccessBase serviceAccess;
	
    
    //@Column(name = "sa_id",length = 100)
    //private String serviceAccessId;
    
    
    @Enumerated(EnumType.STRING)
	private ResourceType type;
	
    @Column(name = "reference_id", length = 100)
	private String referenceId;
    
    @Column(length = 500)
    private String description;
    
    public String toString() {
    	
    	String ret = "ResourceReference(id="+getId()+",referenceId=" + getReferenceId() +
				", description=" + getDescription() +
				",serviceAccess="+getServiceAccess().getId() + ")]";
    	return ret;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
             
        if (!(o instanceof ResourceReference))
            return false;
             
        return
            id != null &&
           id.equals(((ResourceReference) o).getId());
    }
    @Override
    public int hashCode() {
        return 31;
    }
}
