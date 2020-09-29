package de.witcom.itsm.serviceaccess.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ServiceAccessObjectType {
	OtherOperatorGroup("OtherOperatorGroup","OtherOperatorGroup","ServiceAccessOtherOperatorGroup"),
	InfraPassive("InfraPassive","InfraPassive","ServiceAccessInfraPassive"),
	OtherOperator("OtherOperator","OtherOperator","ServiceAccessOtherOperator"),
	InfraBSA("InfraBSA","InfraBSA","ServiceAccessInfraBSA"),
	InfraNK("InfraNK","InfraNK","ServiceAccessInfraNK"),
	InfraActive("InfraActive","InfraActive","ServiceAccessInfraActive");

	private String name;
	private String code;
	private String className;
	
	private ServiceAccessObjectType(String code,String name,String className) {
    	this.code = code;
        this.name = name;
        this.className = className;
    }


	public String getName() {
		return name;
	}

	public String getCode() {
		return code;
	}

	public String getClassname() {
		return className;
	}
	
	public static Optional<ServiceAccessObjectType> valueOfCode(String code) {

		return Arrays.stream(ServiceAccessObjectType.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst();
	}


}
