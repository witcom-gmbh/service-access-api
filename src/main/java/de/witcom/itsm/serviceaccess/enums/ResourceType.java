package de.witcom.itsm.serviceaccess.enums;

import java.util.Arrays;
import java.util.Optional;

public enum ResourceType {
	RMDB_ZONE("RMDB_ZONE","RMDB Zone"),
	RMDB_OBJECT("RMDB_OBJECT","RMDB Objekt"),
	RMDB_CONTRACT("RMDB_CONTRACT","RMDB-Vertrag"),
	CRM_CONTACT("CRM_CONTACT","Firma");
	
	private String name;
	private String code;
	 
    private ResourceType(String code,String name) {
    	this.code = code;
        this.name = name;
    }
 
    public String getName() {
        return name;
    }
    
    public String getCode() {
    	return code;
    }
    
    public static Optional<ResourceType> valueOfCode(String code) {

		return Arrays.stream(ResourceType.values())
				.filter(c -> c.getCode().equals(code))
				.findFirst();

	}
    
}
