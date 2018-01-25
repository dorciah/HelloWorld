package com.simon.runner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

import com.simon.runner.actions.ActionCommand;
import com.simon.runner.actions.ActionComment;
import com.simon.runner.actions.ActionHistory;
import com.simon.runner.actions.ActionMetadata;
import com.simon.runner.actions.ActionPrint;
import com.simon.runner.enums.ActionType;
import com.simon.tools.ReplaceTools;

public class Test_Runner_HandleInput {

	public static Runner runner;
	
	@BeforeClass
	public static void setup(){
		runner = new Runner("properties/test_good.properties");
	}
	
	public void init(Runner runner) throws Exception {
		try {
			runner.init();
		} catch (NumberFormatException | NullPointerException | IOException e) {
			e.printStackTrace();
			throw new Exception(e.getMessage());
		}
	}

	@Test
	public void testHandleInput_commentShouldBeNoException() throws Exception {
		ActionComment comment = new ActionComment(new ActionMetadata(ActionType.COMMENT, "# this is a comment"));	
		try {
			init(runner);
			runner.handleInput(comment);
		} catch (Exception e) {
			fail("Exception thrown handling comment");
		}
		assertEquals(runner.getHistory().size(), 0);
	}
	
	@Test(expected = NullPointerException.class)
	public void testHandleInput_nullMetadataThrowException() throws Exception {
		ActionCommand nullMetaAction = new ActionCommand(null);
		init(runner);	
		runner.handleInput(nullMetaAction);
	}
	
	@Test
	public void testHandleInput_historyNoException() throws Exception {
		ActionHistory hist = new ActionHistory(new ActionMetadata(ActionType.HISTORY, ""));
		init(runner);
		runner.handleInput(hist);
		assertEquals(runner.getHistory().size(), 0);
	}
	
	@Test
	public void testHandleInput_printNoException() throws Exception {	
		init(runner);
		ActionPrint print = (ActionPrint) ReplaceTools.parseLine("print ${thread.count}",runner.getProps());
		runner.handleInput(print);
		assertEquals(runner.getHistory().size(), 0);
	}

}
