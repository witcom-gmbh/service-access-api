package de.witcom.itsm.serviceaccess.util;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;

@Converter(autoApply = true)
public class ServiceAccessObjectTypeConverter implements AttributeConverter<ServiceAccessObjectType, String> {

	@Override
	public String convertToDatabaseColumn(ServiceAccessObjectType attribute) {
		if (attribute == null) {
            return null;
        }
		
        return attribute.getCode();
	}

	@Override
	public ServiceAccessObjectType convertToEntityAttribute(String code) {
		if (code == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessObjectType.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		
	}

}
