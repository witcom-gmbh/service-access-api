package de.witcom.itsm.serviceaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessSubtype;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;

public interface ServiceAccessRepository extends JpaRepository<ServiceAccessBase, String> {
	
	List<ServiceAccessBase> findBySubType(ServiceAccessSubtype subType);
	
	List<ServiceAccessBase> findBySubTypeAllowedObjectType(ServiceAccessObjectType objectType);


}
