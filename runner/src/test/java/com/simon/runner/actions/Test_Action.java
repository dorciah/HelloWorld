package com.simon.runner.actions;

import static org.junit.Assert.*;

import org.junit.Test;

import com.simon.runner.enums.ActionOutcome;
import com.simon.runner.enums.ActionType;

public class Test_Action {

	public Action action;
	
	@Test
	public void testCheckOutput_completedSuccessfully() throws Exception {
		action = new ActionCommand(new ActionMetadata(ActionType.CMD, "cmd \"dir\" \"\" \"\""));
		action.checkOutput();
		assertNotNull(action.getMeta().getEnd());
		assertEquals(action.getMeta().getOutcome(),ActionOutcome.COMPLETE);
	}
	
	@Test(expected = Exception.class)
	public void testCheckOutput_failure() throws Exception {
		action = new ActionCommand(new ActionMetadata(ActionType.CMD, "cmd \"dir\" \"\" \"\""));
		action.getMeta().setErrorText("This has failed");
		action.checkOutput();
		assertNotNull(action.getMeta().getEnd());
		assertEquals(action.getMeta().getOutcome(),ActionOutcome.ERROR);
	}

	@Test
	public void testPrepare() {
		action = new ActionCommand(new ActionMetadata(ActionType.CMD, "cmd \"dir\" \"\" \"\""));
		action.prepare("C:/fake-dir");
		assertNotNull(action.getMeta().getStart());
		assertNotNull(action.getLogFile());
		assertEquals(action.getMeta().getOutcome(),ActionOutcome.ACTIVE);
	}

}
