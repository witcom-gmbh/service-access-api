package de.witcom.itsm.serviceaccess.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "tag")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Tag {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(length = 100, unique = true,name="tag_name")
	@NotNull
	private String tagName;
	
	@Builder.Default
	@ManyToMany(mappedBy = "tags")
	private Set<ServiceAccessBase> tagged = new HashSet<ServiceAccessBase>();
	
	public String toString() {
		
		String ret = "Tag(id="+getId()+",name=" + getTagName() + ")]";
		return ret;
	}
	
	public void addTagged(ServiceAccessBase sa) {
		sa.getTags().add(this);
		tagged.add(sa);
		
	}
	
	public void removeTagged(ServiceAccessBase sa) {
		sa.getTags().remove(this);
		tagged.remove(sa);
	}

	

}
