package de.witcom.itsm.serviceaccess;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.empty;
//import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import de.witcom.itsm.serviceaccess.dto.*;
import de.witcom.itsm.serviceaccess.entity.*;
import de.witcom.itsm.serviceaccess.enums.*;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.exception.PersistenceException;
import de.witcom.itsm.serviceaccess.mapper.EnumMapper;
import de.witcom.itsm.serviceaccess.mapper.ServiceAccessMapper;
import de.witcom.itsm.serviceaccess.mapper.TagMapper;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessRepository;
import de.witcom.itsm.serviceaccess.repository.ServiceAccessSubtypeRepository;
import de.witcom.itsm.serviceaccess.service.ServiceAccessService;
import de.witcom.itsm.serviceaccess.service.TagService;
import de.witcom.itsm.serviceaccess.service.impl.ServiceAccessServiceImpl;

@SpringBootTest
//@Transactional
class TagTests { 
	
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	TagMapper tagMapper;
	
	@Autowired
	TagService tagService;

	
	@Autowired
	ServiceAccessMapper saMapper;
	@Autowired
	EnumMapper enumMapper;
	
	@Autowired
	ServiceAccessService saService;
	
	@Autowired
	ServiceAccessRepository saRepo;

	@Test
	public void tagCRUD() {
		
		CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
		dto.setTagName("my-first-tag");
		Tag persistedTag = tagService.createTag(dto);
		
		log.debug(persistedTag.toString());
		
		assertThat(persistedTag.getId(), is(notNullValue()));
		assertThat(persistedTag.getTagName(), is(dto.getTagName()));
		
		dto = new CreateUpdateTagDTO();
		dto.setTagName("my-first-tag-changed");
		
		Tag updatedTag = tagService.updateTag(persistedTag.getId(), dto);
		assertThat(updatedTag.getTagName(), is(dto.getTagName()));
		
		tagService.deleteTag(persistedTag.getId());
		
	}

	@Test
	public void updateNXTag() {
		assertThrows(NotFoundException.class, () -> {
			CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
			dto.setTagName("my-fake-tag");
			tagService.updateTag(99L, dto);
		});
		
		assertThrows(NotFoundException.class, () -> {
			CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
			dto.setTagName("my-fake-tag");
			tagService.updateTag(null, dto);
		});
	}
	
	@Test
	public void tagLookup() {
		
		CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
		dto.setTagName("my-first-tag");
		Tag persistedTag = tagService.createTag(dto);
		dto = new CreateUpdateTagDTO();
		dto.setTagName("my-second-tag");
		tagService.createTag(dto);
		dto = new CreateUpdateTagDTO();
		dto.setTagName("random-tag");
		tagService.createTag(dto);
		
		Tag tag = tagService.getTagById(persistedTag.getId()).orElseThrow();
		assertThat(persistedTag.getTagName(), is("my-first-tag"));

		List<Tag> res = tagService.searchTagByName("my");
		assertThat(res,hasSize(2));		
	}
	
	@Test
	public void createInvalidTag() {
		assertThrows(PersistenceException.class, () -> {
			CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
			tagService.createTag(dto);
		});
		
	}

	@Test
	public void createDuplicateTag() {
		
		CreateUpdateTagDTO dto = new CreateUpdateTagDTO();
		dto.setTagName("duplicate-tag");
		tagService.createTag(dto);
		
		assertThrows(PersistenceException.class, () -> {
			CreateUpdateTagDTO dto2 = new CreateUpdateTagDTO();
			dto2.setTagName("duplicate-tag");
			tagService.createTag(dto2);
		});
		
	}

	
}
