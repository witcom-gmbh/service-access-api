package de.witcom.itsm.serviceaccess.util;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.witcom.itsm.serviceaccess.enums.ResourceType;


@Converter(autoApply = true)
public class ResourceTypeConverter implements AttributeConverter<ResourceType, String> {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Override
	public String convertToDatabaseColumn(ResourceType attribute) {
		log.debug("In Converter -> Attribute {}",attribute);
		if (attribute == null) {
            return null;
        }
		
        return attribute.getCode();
	}

	@Override
	public ResourceType convertToEntityAttribute(String code) {
		
		log.debug("convert");

		
		if (code == null) {
            return null;
        }
		return Arrays.stream(ResourceType.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		
	}

}
