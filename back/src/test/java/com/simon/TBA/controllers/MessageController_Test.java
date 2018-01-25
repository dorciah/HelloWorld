package com.simon.TBA.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.mockito.BDDMockito.*;

import com.simon.TBA.domain.Message;
import com.simon.TBA.services.MessageRepository;

@RunWith(SpringRunner.class)
@WebMvcTest(MessageController.class)
public class MessageController_Test {

	@Autowired
    private MockMvc mvc;
 
    @MockBean
    private MessageRepository repository;
	
	@Test
	public void testGetMessages_shouldReturnEmptyList() throws Exception {
		
		mvc.perform(get("/api/messages")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$").isEmpty())
				;		
	}

	@Test
	public void testGetMessages_shouldReturnTwoMessages() throws Exception {
		
		Message first = new Message(1L,"first");
		Message second = new Message(2L,"second");
		
		List<Message> toTest = Arrays.asList(first,second);
		given(repository.findAll()).willReturn(toTest);
		
		mvc.perform(get("/api/messages")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$[0].id",is(1)))
				.andExpect(jsonPath("$[0].content",is("first")))
				.andExpect(jsonPath("$[1].id",is(2)))
				.andExpect(jsonPath("$[1].content",is("second")))
				.andExpect(jsonPath("$.length()",is(2)))
				;		
	}
	
	@Test
	public void testGetMessage_shouldReturnNull() throws Exception {
		
		mvc.perform(get("/api/messages/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist())
				;		
	}
	
	@Test
	public void testGetMessage_shouldReturnMessage() throws Exception {
		
		Message toTest = new Message(1L,"Dummy");
		given(repository.findOne(1L)).willReturn(toTest);
		
		mvc.perform(get("/api/messages/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").exists())
				.andExpect(jsonPath("$.id",(is(1))))
				;		
	}


}
