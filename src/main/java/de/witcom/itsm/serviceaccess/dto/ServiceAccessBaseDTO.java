package de.witcom.itsm.serviceaccess.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import de.witcom.itsm.serviceaccess.entity.ServiceAccessOtherOperatorGroup;
import de.witcom.itsm.serviceaccess.entity.Tag;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;


@Data
@AllArgsConstructor
//@NoArgsConstructor
/*
@JsonTypeInfo(
		use = JsonTypeInfo.Id.NAME,
		include = JsonTypeInfo.As.PROPERTY,
		property = "type"
)

@JsonSubTypes({
		@JsonSubTypes.Type(value = ServiceAccessInfraPassiveDTO.class, name = "ServiceAccessInfraPassiveDTO"),
		@JsonSubTypes.Type(value = ServiceAccessOtherOperatorDTO.class, name = "ServiceAccessOtherOperatorDTO"),
		@JsonSubTypes.Type(value = ServiceAccessOtherOperatorGroupDTO.class, name = "ServiceAccessOtherOperatorGroupDTO")
})
*/
public class ServiceAccessBaseDTO {
	
	private String id;
	private String name;
	@Deprecated
	private String projectId;
	private Set<ResourceReferenceDTO> resources = new HashSet<ResourceReferenceDTO>();
	private ServiceAccessSubtypeDTO subType;
	private ServiceAccessStatusDTO status = ServiceAccessStatusDTO.fromEnum(ServiceAccessStatus.NEW);
	private boolean internal;
	private Set<TagDTO> tags = new HashSet<TagDTO>();

	//private String type;
	
	public ServiceAccessBaseDTO() {
		
		
	}
	
    @Override
    public int hashCode() {
        return 13;
    }
 
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ServiceAccessBaseDTO other = (ServiceAccessBaseDTO) obj;
        return this.getId() != null && this.getId().equals(other.getId());
    }

}
