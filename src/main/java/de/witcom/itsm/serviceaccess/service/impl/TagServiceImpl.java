package de.witcom.itsm.serviceaccess.service.impl;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import de.witcom.itsm.serviceaccess.dto.CreateUpdateTagDTO;
import de.witcom.itsm.serviceaccess.entity.ServiceAccessBase;
import de.witcom.itsm.serviceaccess.entity.Tag;
import de.witcom.itsm.serviceaccess.exception.BadRequestException;
import de.witcom.itsm.serviceaccess.exception.ErrorCode;
import de.witcom.itsm.serviceaccess.exception.NotFoundException;
import de.witcom.itsm.serviceaccess.exception.PersistenceException;
import de.witcom.itsm.serviceaccess.mapper.TagMapper;
import de.witcom.itsm.serviceaccess.repository.TagRepository;
import de.witcom.itsm.serviceaccess.service.TagService;
import de.witcom.itsm.serviceaccess.util.Translator;

@Service
@Transactional
public class TagServiceImpl implements TagService {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TagRepository tagRepo; 
	
	@Autowired
	TagMapper tagMapper;


	@Override
	public Optional<Tag> getTagById(Long tagId) {
		return tagRepo.findById(tagId);
	}

	@Override
	public List<Tag> getTags() {
		return tagRepo.findAll();
	}

	@Override
	public List<Tag> searchTagByName(String tagName) {
		return tagRepo.findByTagNameContaining(tagName);
	}

	@Override
	public Tag createTag(CreateUpdateTagDTO tag) throws PersistenceException{
		
	
		Tag entity = Tag.builder().build();
		tagMapper.fromCreateUpdateTagDTO(tag, entity);
		return this.createOrUpdateTag(entity);
		
	}

	@Override
	public Tag updateTag(Long id,CreateUpdateTagDTO tag) throws NotFoundException,PersistenceException{

		if (id == null) {
			throw new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {Tag.class.getSimpleName(),"NULL"}));
		}
		Tag entity = tagRepo.findById(id).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {Tag.class.getSimpleName(),id}))
		);
		
		tagMapper.fromCreateUpdateTagDTO(tag, entity);
		return this.createOrUpdateTag(entity);
	}

	@Override
	public void deleteTag(Long tagId) {
		
		if(tagId==null) {
			throw new NotFoundException(Translator.toLocale(
					"error.persistence.notfound"
					,new Object[] {Tag.class.getSimpleName(),"NULL"}));
		}
		
		Tag tagToDelete = tagRepo.findById(tagId).orElseThrow(
				() -> new NotFoundException(Translator.toLocale(
						"error.persistence.notfound"
						,new Object[] {Tag.class.getSimpleName(),tagId}))
		);
		
		//do we want that ? or restrict it to admins ?
		//or just allow deletion unused tags ?

		//update m2m
		for (ServiceAccessBase sa : tagToDelete.getTagged()) {
			tagToDelete.removeTagged(sa);
		}
		
		tagRepo.delete(tagToDelete);

	}
	
	@Transactional
	private Tag createOrUpdateTag(Tag tag) throws PersistenceException{
		
		if (StringUtils.isBlank(tag.getTagName())){
			log.error("Error when persisting entity-type {} - tagName cannot be empty ",ServiceAccessBase.class.getSimpleName());
			Object[] args = {Tag.class.getSimpleName()};
			throw new PersistenceException(Translator.toLocale("error.persistence.store",args),ErrorCode.SERVER_ERROR);
		}
		
		try {
			tag = tagRepo.save(tag);
		} catch (Exception e) {
			log.error("Error when persisting entity-type {} - {} ",Tag.class.getSimpleName(), e.getMessage());
			Object[] args = {Tag.class.getSimpleName()};
			throw new PersistenceException(Translator.toLocale("error.persistence.store",args),ErrorCode.SERVER_ERROR);
		}
		
		return tag;
	}

}
