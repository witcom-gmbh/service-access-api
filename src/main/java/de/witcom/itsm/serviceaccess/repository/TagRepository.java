package de.witcom.itsm.serviceaccess.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import de.witcom.itsm.serviceaccess.entity.Tag;

public interface TagRepository extends JpaRepository<Tag, Long> {
	
	List<Tag >findByTagNameContaining(String title);

}
