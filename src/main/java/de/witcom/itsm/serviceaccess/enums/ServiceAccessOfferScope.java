package de.witcom.itsm.serviceaccess.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ServiceAccessOfferScope{
	
	UNSCOPED("U","Unbegrenzt"),
	SALE("SALE","Verkauf"),
	PROJECT("PROJECT","Projekt"),
	SERVICE("S","Service");
	
	private String name;
	private String code;
	 
    private ServiceAccessOfferScope(String code,String name) {
    	this.code = code;
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
    
    public String getCode() {
    	return code;
    }
    
    public static Optional<ServiceAccessOfferScope> valueOfCode(String code) {

		return Arrays.stream(ServiceAccessOfferScope.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst();

	}

}
