package de.witcom.itsm.serviceaccess.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import de.witcom.itsm.serviceaccess.enums.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResourceReferenceDTO {

	//private Long id;
	@JsonProperty(required = true)
	private ResourceTypeDTO type;
	@JsonProperty(required = true)
	private String referenceId;
    private String description;

}
