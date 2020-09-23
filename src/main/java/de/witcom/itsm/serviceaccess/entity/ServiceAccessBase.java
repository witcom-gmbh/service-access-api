package de.witcom.itsm.serviceaccess.entity;

import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import de.witcom.itsm.serviceaccess.util.generator.StringPrefixedSequenceIdGenerator;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "service_access_base")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="object_type", 
  discriminatorType = DiscriminatorType.STRING)
//@Data
//@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceAccessBase {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sa_seq")
    @GenericGenerator(
        name = "sa_seq", 
        strategy = "de.witcom.itsm.serviceaccess.util.generator.StringPrefixedSequenceIdGenerator", 
        parameters = {
            @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.INCREMENT_PARAM, value = "1"),
            @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.VALUE_PREFIX_PARAMETER, value = "SA"),
            @org.hibernate.annotations.Parameter(name = StringPrefixedSequenceIdGenerator.NUMBER_FORMAT_PARAMETER, value = "%09d") })
	@Column(length = 100)
	private String id;
	
	@Column(length = 100)
	@NotNull
	private String name;
	@Column(length = 1000)
	private String description;
	@Column(length = 50,name = "project_id")
	private String projectId;
	
	@Builder.Default
    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
	private java.time.Instant createdAt = Instant.now();
    
    @Builder.Default
    @Column(name = "modified_date")
    @LastModifiedDate
	private java.time.Instant updatedAt = Instant.now();
	
	@Builder.Default
	@OneToMany(mappedBy="serviceAccess",fetch = FetchType.EAGER
	,cascade = CascadeType.ALL
	)
	private Set<ResourceReference> resources = new HashSet<ResourceReference>();

	@Builder.Default
	@Column(columnDefinition="VARCHAR(20)")
	private ServiceAccessStatus status = ServiceAccessStatus.NEW;
	
	@Builder.Default
	private boolean internal = false;
    @ManyToOne()
    @JoinColumn(name = "subtype_id", referencedColumnName = "subtype_id")
	private ServiceAccessSubtype subType;
    
    @Builder.Default
    @ManyToMany()
    @JoinTable(
      name = "sa_tag", 
      joinColumns = @JoinColumn(name = "sa_id"), 
      inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<Tag>();
    
    public void addResourceReference(ResourceReference resource){
    	resource.setServiceAccess(this);
    	resources.add(resource);
    }
    
    public void addTag(Tag tag) {
    	tag.getTagged().add(this);
    	tags.add(tag);
    }
    
    public void removeTag(Tag tag) {
    	this.tags.remove(tag);
    	tag.getTagged().remove(this);
    }
    
    public void updateTags(Set<Tag> updatedTags) {
    	Set<Tag> safeCopy = new HashSet<Tag>(this.getTags());
    	for (Tag tag:safeCopy) {
			if (!updatedTags.contains(tag)) {
				this.removeTag(tag);
			}
		}
		for (Tag tag : updatedTags) {
			this.addTag(tag);
		}
    }
    
}
