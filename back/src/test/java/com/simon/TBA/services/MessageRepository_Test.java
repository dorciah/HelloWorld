package com.simon.TBA.services;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import com.simon.TBA.domain.Message;


@RunWith(SpringRunner.class)
@DataJpaTest
public class MessageRepository_Test {

	@Autowired
    private TestEntityManager entityManager;
	
	@Autowired
	private MessageRepository messageRepository;
	
	@Test
	public void testGetMessageByContent_shouldReturnMessage() {
		Message toPersist = new Message(2L,"content");
		entityManager.persist(toPersist);
		
		Message toVerify = messageRepository.getMessageByContent("content");
		Assert.assertEquals(toPersist.getContent(), toVerify.getContent());
	}
	
	@Test
	public void testGetMessageByContent_shouldReturnNull() {
		Message toPersist = new Message(2L,"content");
		entityManager.persist(toPersist);
		
		Message toVerify = messageRepository.getMessageByContent("missing");
		Assert.assertNull(toVerify);
	}
	
	@After
	public void after() {
		this.entityManager.clear();
	}

}
