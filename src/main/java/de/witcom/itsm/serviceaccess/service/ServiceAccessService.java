package de.witcom.itsm.serviceaccess.service;

import java.util.List;
import java.util.Optional;

import de.witcom.itsm.serviceaccess.dto.CreateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessBaseDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessBaseDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessInfraOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorGroupDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.dto.UpdateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessSubtype;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.exception.PersistenceException;

public interface ServiceAccessService {
	
	public ServiceAccessOtherOperatorGroupDTO updateOtherOperators(String id,
			List<ServiceAccessInfraOtherOperatorDTO> otherOperators);
	public ServiceAccessBaseDTO updateTags(String id,List<TagDTO> tags);
	public ServiceAccessBaseDTO updateServiceAccessStatus(String id,ServiceAccessStatus status) 
			throws NotFoundException,BadRequestException,PersistenceException;
	public Optional<ServiceAccessBaseDTO> getServiceAccessById(String id);
	public ServiceAccessBaseDTO createServiceAccess(CreateUpdateServiceAccessBaseDTO dto)
			throws BadRequestException,PersistenceException;
	public ServiceAccessBaseDTO updateServiceAccess(String id,CreateUpdateServiceAccessBaseDTO dto)
			throws BadRequestException,PersistenceException;
	public List<ServiceAccessBase> getServiceAccess();
	public List<ServiceAccessBase> getServiceAccess(ServiceAccessObjectType objectType);


	public ServiceAccessSubtypeDTO createSubtype(CreateServiceAccessSubtypeDTO dto) throws PersistenceException;
	public ServiceAccessSubtypeDTO updateSubtype(Long subtypeId, UpdateServiceAccessSubtypeDTO dto) throws NotFoundException,PersistenceException;
	public Optional<ServiceAccessSubtypeDTO> getSubtypeById(Long id);
	public Optional<ServiceAccessSubtypeDTO> getSubtypeByName(String name);
	public List<ServiceAccessSubtypeDTO> getSubtypes();
	public void deleteSubtype(Long id) throws PersistenceException;
	//public void deleteSubtype(ServiceAccessSubtype entity) throws PersistenceException;
	

}
