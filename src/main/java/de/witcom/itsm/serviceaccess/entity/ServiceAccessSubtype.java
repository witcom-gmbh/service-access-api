package de.witcom.itsm.serviceaccess.entity;

import javax.persistence.*;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "service_access_subtype")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessSubtype {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subtype_id")
	private Long subtypeId;
    
    @Column(name = "name", unique = true, length = 50)
    private String name;

	@Column(length = 1000)
	private String description;    
	
	@Enumerated(EnumType.STRING)
	@Column(name = "allowed_type")
	private ServiceAccessObjectType allowedObjectType;
	
	@Column(name = "number_of_endpoints")
	private Integer numberOfEndpoints;

	@Column(columnDefinition="VARCHAR(20)",name = "offer_scope")
	@Builder.Default
	private ServiceAccessOfferScope offerScope =  ServiceAccessOfferScope.UNSCOPED;

	@Column(columnDefinition="VARCHAR(20)")
	@Builder.Default
	private ServiceAccessScope scope = ServiceAccessScope.UNSCOPED;

}
