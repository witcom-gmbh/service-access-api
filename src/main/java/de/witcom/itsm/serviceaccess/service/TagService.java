package de.witcom.itsm.serviceaccess.service;

import java.util.List;
import java.util.Optional;

import de.witcom.itsm.serviceaccess.dto.CreateUpdateTagDTO;
import de.witcom.itsm.serviceaccess.entity.Tag;

public interface TagService {
	
	public Optional<Tag> getTagById(Long tagId);
	public List<Tag> getTags();
	public List<Tag> searchTagByName(String tagName);
	public Tag createTag(CreateUpdateTagDTO tag);
	public Tag updateTag(Long id, CreateUpdateTagDTO tag);
	public void deleteTag(Long tagId);

}
