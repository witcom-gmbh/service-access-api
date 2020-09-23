package de.witcom.itsm.serviceaccess.util;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;

@Converter(autoApply = true)
public class ServiceAccessStatusConverter implements AttributeConverter<ServiceAccessStatus, String> {


	@Override
	public String convertToDatabaseColumn(ServiceAccessStatus attribute) {
		if (attribute == null) {
            return null;
        }
		
        return attribute.getCode();
	}

	@Override
	public ServiceAccessStatus convertToEntityAttribute(String code) {

		
		if (code == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessStatus.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		
	}

}
