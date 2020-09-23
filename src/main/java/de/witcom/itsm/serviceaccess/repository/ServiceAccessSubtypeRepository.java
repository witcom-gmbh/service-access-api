package de.witcom.itsm.serviceaccess.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessSubtype;

public interface ServiceAccessSubtypeRepository extends JpaRepository<ServiceAccessSubtype, Long> {
	
	Optional<ServiceAccessSubtype> findOneByName(String name);

}
