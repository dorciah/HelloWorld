package com.simon.TBA.services;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.simon.TBA.domain.Message;

/**
 * 
 * Dummy interface for messages.
 * 
 * @author Simon
 *
 */

public interface MessageRepository extends JpaRepository<Message, Long> {
	
	@Query("select m from Message m where m.content = :content")
	Message getMessageByContent(@Param("content") String content);
	
}
