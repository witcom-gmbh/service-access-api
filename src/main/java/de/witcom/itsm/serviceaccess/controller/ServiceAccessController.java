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
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessBaseDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessBaseDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorGroupDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessStatusDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
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

@Tag(name = "serviceaccess")
@RestController
@RequestMapping("/api/v1/serviceaccess")
public class ServiceAccessController {
	
	@Autowired
	ServiceAccessMapper saMapper;
	
	@Autowired
	ServiceAccessService saService;

	@Autowired
	EnumMapper enumMapper;
	
	/*
	@PostMapping(value="", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessBaseDTO> createServiceAccess(@RequestBody CreateUpdateServiceAccessBaseDTO dto) {
		return new ResponseEntity<ServiceAccessBaseDTO>(saService.createServiceAccess(dto),HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessBaseDTO> updateServiceAccess(@PathVariable String id,@RequestBody CreateUpdateServiceAccessBaseDTO dto) {
		return new ResponseEntity<ServiceAccessBaseDTO>(saService.updateServiceAccess(id, dto),HttpStatus.OK);
	}

	@PutMapping(value="/{id}/tags", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates Tags for ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessBaseDTO> updateServiceAccessTags(@PathVariable String id,@RequestBody List<TagDTO> tags) {
		return new ResponseEntity<ServiceAccessBaseDTO>(saService.updateTags(id, tags),HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}/status", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Sets status for ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessBaseDTO> updateServiceAccessStatus(@PathVariable String id,@RequestBody ServiceAccessStatusDTO status) {
		return new ResponseEntity<ServiceAccessBaseDTO>(saService.updateServiceAccessStatus(id, enumMapper.dtoToEnum(status)),HttpStatus.OK);
	}
	

	
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
    @Operation(description = "List all ServiceAccesses", responses = {
            @ApiResponse( responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Backend error")
            })
	public @ResponseBody ResponseEntity<List<ServiceAccessBaseDTO>> getAllServiceAccess() {
		
		return new ResponseEntity<List<ServiceAccessBaseDTO>>(
				saService.getServiceAccess()
				.stream()
				.map(e -> saMapper.toDto(e))
				.collect(Collectors.toList())
				, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get ServiceAccessBaseDTO by id", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "ServiceAccessBaseDTO doesn't exist"),
			@ApiResponse(responseCode = "500", description = "Backend error")
			}   
    		)
	public @ResponseBody ResponseEntity<ServiceAccessBaseDTO> getServiceAccessSubtypesById(@PathVariable String id) {
		
		return new ResponseEntity<ServiceAccessBaseDTO>(
				saService.getServiceAccessById(id)
						.orElseThrow(() -> new NotFoundException(Translator.toLocale(
								"error.persistence.notfound"
								,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
						), HttpStatus.OK);
	}
	

	

	


}
