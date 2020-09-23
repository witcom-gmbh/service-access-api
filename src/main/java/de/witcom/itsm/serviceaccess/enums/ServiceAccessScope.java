package de.witcom.itsm.serviceaccess.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ServiceAccessScope {
	UNSCOPED("U","Unbegrenzt"),
	SALE("SALE","Verkauf"),
	PROJECT("PROJECT","Projekt"),
	SERVICE("S","Service"),
	ONCE("O","Einmal");
	
	private String name;
	private String code;
	 
    private ServiceAccessScope(String code,String name) {
    	this.code = code;
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
    
    public String getCode() {
    	return code;
    }
    
    public static Optional<ServiceAccessScope> valueOfCode(String code) {

		return Arrays.stream(ServiceAccessScope.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst();
				//.orElseThrow(IllegalArgumentException::new);
	}

}
