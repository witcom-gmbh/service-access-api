package de.witcom.itsm.serviceaccess.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.witcom.itsm.serviceaccess.dto.CreateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessInfraPassiveDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessInfraPassiveDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessInfraOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorGroupDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessStatusDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessInfraPassive;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessObjectType;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.mapper.EnumMapper;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;
import de.witcom.itsm.serviceaccess.util.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "infra-passive")
@RestController
@RequestMapping("/api/v1/serviceaccess/infrapassive")
public class ServiceAccessInfraPassiveController {
	
	@Autowired
	ServiceAccessMapper saMapper;
	
	@Autowired
	ServiceAccessService saService;

	@Autowired
	EnumMapper enumMapper;
	
	
	@PostMapping(value="", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create ServiceAccess for passive infrastructure", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessInfraPassiveDTO> createServiceAccessInfraPassive(@RequestBody CreateUpdateServiceAccessInfraPassiveDTO dto) {
		return new ResponseEntity<ServiceAccessInfraPassiveDTO>((ServiceAccessInfraPassiveDTO) saService.createServiceAccess(dto),HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates ServiceAccess for passive infrastructure", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessInfraPassiveDTO> updateServiceAccessInfraPassive(@PathVariable String id,@RequestBody CreateUpdateServiceAccessInfraPassiveDTO dto) {
		return new ResponseEntity<ServiceAccessInfraPassiveDTO>((ServiceAccessInfraPassiveDTO) saService.updateServiceAccess(id, dto),HttpStatus.OK);
	}

	@PutMapping(value="/{id}/tags", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates Tags for passive infrastructure ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessInfraPassiveDTO> updateServiceAccessInfraPassiveTags(@PathVariable String id,@RequestBody List<TagDTO> tags) {
		return new ResponseEntity<ServiceAccessInfraPassiveDTO>((ServiceAccessInfraPassiveDTO) saService.updateTags(id, tags),HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}/status", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Sets status  for passive infrastructure ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessInfraPassiveDTO> updateServiceAccessInfraPassiveStatus(@PathVariable String id,@RequestBody ServiceAccessStatusDTO status) {
		return new ResponseEntity<ServiceAccessInfraPassiveDTO>((ServiceAccessInfraPassiveDTO) saService.updateServiceAccessStatus(id, enumMapper.dtoToEnum(status)),HttpStatus.OK);
	}
	

/*	
	@PutMapping(value="/{id}/otherOperators", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates Tags for ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessOtherOperatorGroupDTO> updateServiceAccessInfraGroupMembers(@PathVariable String id,@RequestBody List<ServiceAccessInfraOtherOperatorDTO> otherOperators) {
		return new ResponseEntity<ServiceAccessOtherOperatorGroupDTO>(saService.updateOtherOperators(id, otherOperators),HttpStatus.OK);
	}
*/
	
	
	@RequestMapping(value="", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all ServiceAccesses for passive infrastructure", responses = {
            @ApiResponse( responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Backend error")
            })
	public @ResponseBody ResponseEntity<List<ServiceAccessInfraPassiveDTO>> getAllServiceAccessInfraPassive() {
		
		return new ResponseEntity<List<ServiceAccessInfraPassiveDTO>>(
				saService.getServiceAccess(ServiceAccessObjectType.InfraPassive)
				.stream()
				.map(e -> saMapper.toServiceAccessInfraPassiveDTO((ServiceAccessInfraPassive) e))
				.collect(Collectors.toList())
				, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get ServiceAccessInfraPassiveDTO by id", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "ServiceAccessInfraPassiveDTO doesn't exist"),
			@ApiResponse(responseCode = "500", description = "Backend error")
			}   
    		)
	public @ResponseBody ResponseEntity<ServiceAccessInfraPassiveDTO> getServiceAccessInfraPassiveById(@PathVariable String id) {
		
		return new ResponseEntity<ServiceAccessInfraPassiveDTO>(
				(ServiceAccessInfraPassiveDTO) saService.getServiceAccessById(id)
						.orElseThrow(() -> new NotFoundException(Translator.toLocale(
								"error.persistence.notfound"
								,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
						), HttpStatus.OK);
	}
	

	

	


}
