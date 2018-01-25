package com.simon.TBA.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/** 
 * 
 * Message is a dummy domain class to be modified when building applications
 * requiring data to be persisted
 * 
 * @author Simon
 *
 */

@Entity
@Table(name="message")
public class Message {

	@Id
	@Column(name="ID", nullable=false, unique=true, length=11)
	private long id;
	
	@Column(name="CONTENT", length=100, nullable=true)
	private String content;

	public Message() {}
	
	public Message (long id, String content) {
		this.id = id;
		this.content = content;
	}
	
	/* getters and setters */
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
	
}
