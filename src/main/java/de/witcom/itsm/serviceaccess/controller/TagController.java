package de.witcom.itsm.serviceaccess.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import de.witcom.itsm.serviceaccess.dto.CreateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.CreateUpdateTagDTO;
import de.witcom.itsm.serviceaccess.dto.ErrorMessageDTO;
import de.witcom.itsm.serviceaccess.dto.ServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.dto.TagDTO;
import de.witcom.itsm.serviceaccess.dto.UpdateServiceAccessSubtypeDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.enums.ServiceAccessStatus;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.mapper.TagMapper;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessRepository;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;
import de.witcom.itsm.serviceaccess.service.TagService;
import de.witcom.itsm.serviceaccess.util.Translator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "tag")
@RestController
@RequestMapping("/api/v1/tag")
public class TagController {


	@Autowired
	ServiceAccessMapper saMapper;
	
	@Autowired
	ServiceAccessService saService;
	
	@Autowired
	TagService tagService;
	
	@Autowired
	TagMapper tagMapper;
	
	
	@PostMapping(value="", produces=MediaType.APPLICATION_JSON_VALUE)
	@Operation(description = "Create Tag", responses = {
				@ApiResponse(content = @Content(schema = @Schema(implementation = TagDTO.class)), responseCode = "200"),
				@ApiResponse(responseCode = "500", description = "Backend error"),
				@ApiResponse(responseCode = "400", description = "Malformed request")
				})
	public @ResponseBody ResponseEntity<TagDTO> createServiceAccessSubtype(@RequestBody CreateUpdateTagDTO dto) {
		return new ResponseEntity<TagDTO>(tagMapper.toTagDTO(tagService.createTag(dto)),HttpStatus.OK);
	}

	@Operation(description = "Update Tag", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = TagDTO.class)), responseCode = "200"),
			@ApiResponse(responseCode = "500", description = "Backend error"),
			@ApiResponse(responseCode = "400", description = "Malformed request")
			})
	@PutMapping(value="/{id}", produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody ResponseEntity<TagDTO> updateServiceAccessSubtypes(@PathVariable long id,@RequestBody CreateUpdateTagDTO tag) {
		return new ResponseEntity<TagDTO>(tagMapper.toTagDTO(tagService.updateTag(id, tag)),HttpStatus.OK);
	}
	
	@RequestMapping(value="/search", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Find tag by name. Query-String has to be 2 characters long", responses = {
            @ApiResponse(content = @Content(array = @ArraySchema(schema = @Schema(implementation = ServiceAccessSubtypeDTO.class))), responseCode = "200"),
            @ApiResponse(responseCode = "500", description = "Backend error")
            })
	public @ResponseBody ResponseEntity<List<TagDTO>> findTagbyName(@RequestParam String query) {
		//min 2 character
		if (StringUtils.isBlank(query)) {
			return ResponseEntity.badRequest().build();
		}
		if (query.length() < 2) {
			return ResponseEntity.badRequest().build();
		}
		
		return new ResponseEntity<List<TagDTO>>(
				tagService.searchTagByName(query).stream().map(t -> tagMapper.toTagDTO(t)).collect(Collectors.toList())
				, HttpStatus.OK);
		
	}

	@RequestMapping(value="/{id}", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
    @Operation(description = "Get Tag by id", responses = {
			@ApiResponse(content = @Content(schema = @Schema(implementation = TagDTO.class)), responseCode = "200"),
			@ApiResponse(responseCode = "404", description = "TagDTO doesn't exist"),
			@ApiResponse(responseCode = "500", description = "Backend error")
			}   
    		)
	public @ResponseBody ResponseEntity<TagDTO> getTagById(@PathVariable long id) {
		
		return new ResponseEntity<TagDTO>(
				tagMapper.toTagDTO(tagService.getTagById(id)
						.orElseThrow(() -> new NotFoundException(Translator.toLocale(
								"error.persistence.notfound"
								,new Object[] {Tag.class.getSimpleName(),id}))
						)), HttpStatus.OK);
	}
	
	
	@DeleteMapping("/{id}")
	@Operation(description = "Delete Tag", responses = {
			@ApiResponse(responseCode = "200", description = "Entity deleted"),
			@ApiResponse(responseCode = "500", description = "Backend error"),
			@ApiResponse(responseCode = "400", description = "Malformed request")
			})
    //@Secured(AuthoritiesConstants.ADMIN)
	public ResponseEntity<Void> deleteTagById(@PathVariable long id) {
		tagService.deleteTag(id);
		return ResponseEntity.ok().build();
	}


	
	
}
