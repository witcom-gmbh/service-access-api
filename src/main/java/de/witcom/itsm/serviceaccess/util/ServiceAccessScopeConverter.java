package de.witcom.itsm.serviceaccess.util;

import java.util.Arrays;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;


@Converter(autoApply = true)
public class ServiceAccessScopeConverter implements AttributeConverter<ServiceAccessOfferScope, String> {

	@Override
	public String convertToDatabaseColumn(ServiceAccessOfferScope attribute) {
		if (attribute == null) {
            return null;
        }
        return attribute.getCode();
	}

	@Override
	public ServiceAccessOfferScope convertToEntityAttribute(String code) {
		
		if (code == null) {
            return null;
        }
		return Arrays.stream(ServiceAccessOfferScope.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst()
				.orElseThrow(IllegalArgumentException::new);
		
	}

}
