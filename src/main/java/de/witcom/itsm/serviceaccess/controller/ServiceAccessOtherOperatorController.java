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
import de.witcom.itsm.serviceaccess.dto.CreateUpdateServiceAccessOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessOtherOperatorGroupDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessStatusDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessOtherOperator;
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

@Tag(name = "other-operator")
@RestController
@RequestMapping("/api/v1/serviceaccess/otheroperator")
public class ServiceAccessOtherOperatorController {
	
	@Autowired
	ServiceAccessMapper saMapper;
	
	@Autowired
	ServiceAccessService saService;

	@Autowired
	EnumMapper enumMapper;
	
	
	@PostMapping(value="", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create ServiceAccess for other-operators", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessOtherOperatorDTO> createServiceAccessOtherOperator(@RequestBody CreateUpdateServiceAccessOtherOperatorDTO dto) {
		return new ResponseEntity<ServiceAccessOtherOperatorDTO>((ServiceAccessOtherOperatorDTO) saService.createServiceAccess(dto),HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates ServiceAccess for other-operators", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessOtherOperatorDTO> updateServiceAccessOtherOperator(@PathVariable String id,@RequestBody CreateUpdateServiceAccessOtherOperatorDTO dto) {
		return new ResponseEntity<ServiceAccessOtherOperatorDTO>((ServiceAccessOtherOperatorDTO) saService.updateServiceAccess(id, dto),HttpStatus.OK);
	}

	@PutMapping(value="/{id}/tags", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Updates Tags for other-operators ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessOtherOperatorDTO> updateServiceAccessOtherOperatorTags(@PathVariable String id,@RequestBody List<TagDTO> tags) {
		return new ResponseEntity<ServiceAccessOtherOperatorDTO>((ServiceAccessOtherOperatorDTO) saService.updateTags(id, tags),HttpStatus.OK);
	}
	
	@PutMapping(value="/{id}/status", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Sets status for other-operators ServiceAccess", responses = {
				@ApiResponse(responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessOtherOperatorDTO> updateServiceAccessOtherOperatorStatus(@PathVariable String id,@RequestBody ServiceAccessStatusDTO status) {
		return new ResponseEntity<ServiceAccessOtherOperatorDTO>((ServiceAccessOtherOperatorDTO) saService.updateServiceAccessStatus(id, enumMapper.dtoToEnum(status)),HttpStatus.OK);
	}
	
	
	@RequestMapping(value="", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all ServiceAccesses for other-operators", responses = {
            @ApiResponse( responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Backend error")
            })
	public @ResponseBody ResponseEntity<List<ServiceAccessOtherOperatorDTO>> getAllServiceAccessOtherOperator() {
		
		return new ResponseEntity<List<ServiceAccessOtherOperatorDTO>>(
				saService.getServiceAccess(ServiceAccessObjectType.OtherOperator)
				.stream()
				.map(e -> saMapper.toServiceAccessOtherOperatorDTO((ServiceAccessOtherOperator) e))
				.collect(Collectors.toList())
				, HttpStatus.OK);
		
	}
	
	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get ServiceAccessOtherOperatorDTO by id", responses = {
			@ApiResponse(responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "ServiceAccessOtherOperatorDTO doesn't exist"),
			@ApiResponse(responseCode = "500", description = "Backend error")
			}   
    		)
	public @ResponseBody ResponseEntity<ServiceAccessOtherOperatorDTO> getServiceAccessOtherOperatorById(@PathVariable String id) {
		
		return new ResponseEntity<ServiceAccessOtherOperatorDTO>(
				(ServiceAccessOtherOperatorDTO) saService.getServiceAccessById(id)
						.orElseThrow(() -> new NotFoundException(Translator.toLocale(
								"error.persistence.notfound"
								,new Object[] {ServiceAccessBase.class.getSimpleName(),id}))
						), HttpStatus.OK);
	}
	

	

	


}
