package com.simon.runner.actions;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import com.simon.runner.enums.ActionOutcome;
import com.simon.runner.enums.ActionType;

public class Test_ActionMetadata {

	public static ActionMetadata meta = null;
	
	@BeforeClass
	public static void setup() {
		meta = new ActionMetadata(ActionType.COMMENT, "# comment");
	}
	

	@Test
	public void testInit_statusQueued() {	
		assertEquals(meta.getOutcome(),ActionOutcome.QUEUED);
	}
	
	@Test
	public void testInit_rawValue() {
		assertEquals(meta.getRaw(),"# comment");
	}
	
	@Test
	public void testInit_actionType() {
		assertEquals(meta.getType(),ActionType.COMMENT);
	}
	
	@Test
	public void testInit_idIncrement() {
		assertEquals(meta.getId(), 1);
	}
	
	@Test
	public void testInit_idIncrement2() {
		ActionMetadata meta2 = new ActionMetadata(ActionType.COMMENT, "# comment 2");
		assertEquals(meta2.getId(), 2);
	}
		
		
}
