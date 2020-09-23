package de.witcom.itsm.serviceaccess.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ServiceAccessStatus {
	NEW("N","Neu"),
	PLANNING("IP","In Planung"),
	PLANNED("P","Geplant"),
	ACTIVE("A","Aktiv"),
	SUSPENDED("S","Gesperrt für neue Services"),
	RETIRED("R","Beendet");
	
	private String name;
	private String code;
	 
    private ServiceAccessStatus(String code,String name) {
    	this.code = code;
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
    
    public String getCode() {
    	return code;
    }
    
    public static Optional<ServiceAccessStatus> valueOfCode(String code) {

		return Arrays.stream(ServiceAccessStatus.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst();

	}

}
