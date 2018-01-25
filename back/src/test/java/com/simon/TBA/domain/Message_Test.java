package com.simon.TBA.domain;

import org.junit.Assert;
import org.junit.Test;


public class Message_Test {

	@Test
	public void testMessageLongString() {
		
		Message toTest = new Message(1L,"Dummy");
		
		Assert.assertEquals("Dummy",toTest.getContent());
		Assert.assertEquals(1L,toTest.getId(),0);
		
	}

}
