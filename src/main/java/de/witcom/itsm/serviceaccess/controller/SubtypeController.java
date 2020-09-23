package de.witcom.itsm.serviceaccess.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.witcom.itsm.serviceaccess.dto.CreateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.ErrorMessageDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.UpdateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessRepository;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;
import de.witcom.itsm.serviceaccess.util.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "subtype")
@RestController
@RequestMapping("/api/v1/subtype")
public class SubtypeController {


	@Autowired
	ServiceAccessMapper saMapper;
	
	@Autowired
	ServiceAccessService saService;
	
	@PostMapping(value="", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create ServiceAccessSubtype", responses = {
				@ApiResponse(content = @Content(schema = @Schema(implementation = ServiceAccessSubtypeDTO.class)), responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<ServiceAccessSubtypeDTO> createServiceAccessSubtype(@RequestBody CreateServiceAccessSubtypeDTO dto) {
		return new ResponseEntity<ServiceAccessSubtypeDTO>(saService.createSubtype(dto),HttpStatus.OK);
	}

	@Operation(description = "Update ServiceAccessSubtype", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = ServiceAccessSubtypeDTO.class)), responseCode = "200"),
			@ApiResponse(responseCode = "500", description = "Backend error"),
			@ApiResponse(responseCode = "400", description = "Malformed request")
			})
	@PutMapping(value="/{subtypeId}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<ServiceAccessSubtypeDTO> updateServiceAccessSubtypes(@PathVariable long subtypeId,@RequestBody UpdateServiceAccessSubtypeDTO dto) {
		return new ResponseEntity<ServiceAccessSubtypeDTO>(saService.updateSubtype(subtypeId,dto),HttpStatus.OK);
	}
	
	@RequestMapping(value="", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "List all ServiceAccessSubtypes", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceAccessSubtypeDTO.class))), responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Backend error")
            })
	public @ResponseBody ResponseEntity<List<ServiceAccessSubtypeDTO>> getAllServiceAccessSubtypes() {
		
		return new ResponseEntity<List<ServiceAccessSubtypeDTO>>(
				saService.getSubtypes()
				, HttpStatus.OK);
		
	}

	@RequestMapping(value="/{subtypeId}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get ServiceAccessSubtypeDTO by id", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = ServiceAccessSubtypeDTO.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "ServiceAccessSubtypeDTO doesn't exist"),
			@ApiResponse(responseCode = "500", description = "Backend error")
			}   
    		)
	public @ResponseBody ResponseEntity<ServiceAccessSubtypeDTO> getServiceAccessSubtypesById(@PathVariable long subtypeId) {
		
		return new ResponseEntity<ServiceAccessSubtypeDTO>(
				saService.getSubtypeById(subtypeId)
						.orElseThrow(() -> new NotFoundException(Translator.toLocale(
								"error.persistence.notfound"
								,new Object[] {ServiceAccessBase.class.getSimpleName(),subtypeId}))
						), HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{subtypeId}")
	@Operation(description = "Delete ServiceAccessSubtype", responses = {
			@ApiResponse(responseCode = "200", description = "Entity deleted"),
			@ApiResponse(responseCode = "500", description = "Backend error"),
			@ApiResponse(responseCode = "400", description = "Malformed request")
			})
    //@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteServiceAccessSubtypesById(@PathVariable long subtypeId) {
		
		saService.deleteSubtype(subtypeId);
		return ResponseEntity.ok().build();
		
	}


	
	
}
