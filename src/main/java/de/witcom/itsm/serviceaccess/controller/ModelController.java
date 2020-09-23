package de.witcom.itsm.serviceaccess.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.witcom.itsm.serviceaccess.dto.ResourceTypeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessObjectTypeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOfferScopeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessScopeDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessStatusDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.enums.ResourceType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessOfferScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessScope;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessRepository;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;


@RestController
@RequestMapping("/api/v1/models")
public class ModelController {


	@Autowired
	ServiceAccessMapper saMapper;

	@Autowired
	ServiceAccessService saService;
	
	
	@RequestMapping(value="/serviceaccessstatus", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ServiceAccessStatusDTO> getStatus() {
		
		return Arrays.asList(ServiceAccessStatus.values()).stream().map(m -> ServiceAccessStatusDTO.fromEnum(m)).collect(Collectors.toList());

	}

	@RequestMapping(value="/resourcetype", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ResourceTypeDTO> getResourcetypes() {
		return Arrays.asList(ResourceType.values()).stream().map(m -> ResourceTypeDTO.fromEnum(m)).collect(Collectors.toList());
	}

	@RequestMapping(value="/scope", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ServiceAccessScopeDTO> getScopes() {
		return Arrays.asList(ServiceAccessScope.values()).stream().map(m -> ServiceAccessScopeDTO.fromEnum(m)).collect(Collectors.toList());
	}

	@RequestMapping(value="/offerscope", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ServiceAccessOfferScopeDTO> getOfferScopes() {
		return Arrays.asList(ServiceAccessOfferScope.values()).stream().map(m -> ServiceAccessOfferScopeDTO.fromEnum(m)).collect(Collectors.toList());
	}
	
	@RequestMapping(value="/objecttype", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody List<ServiceAccessObjectTypeDTO> getObjecttypes() {
	    return Arrays.asList(ServiceAccessObjectType.values()).stream().map(m -> ServiceAccessObjectTypeDTO.fromEnum(m)).collect(Collectors.toList());
	}
	
	
	
}
