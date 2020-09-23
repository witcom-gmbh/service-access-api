package de.witcom.itsm.serviceaccess.util;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;


@Converter(autoApply = true)
public class ServiceAccessOfferScopeConverter implements AttributeConverter<ServiceAccessScope, String> {


	@Override
	public String convertToDatabaseColumn(ServiceAccessScope attribute) {
		if (attribute == null) {
            return null;
        }
		
        return attribute.getCode();
	}

	@Override
	public ServiceAccessScope convertToEntityAttribute(String code) {

		
		if (code == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessScope.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		
	}

}
