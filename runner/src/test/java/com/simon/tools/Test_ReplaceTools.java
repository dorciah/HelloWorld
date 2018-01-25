package com.simon.tools;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.simon.runner.Runner;
import com.simon.runner.actions.Action;
import com.simon.runner.enums.ActionType;

public class Test_ReplaceTools {

	public static Runner runner;
	
	@Before
	public void setup() {
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
	public void testParseLine_returnComment() throws Exception {
		init(runner);
		Action comment = ReplaceTools.parseLine("# This is a comment", runner.getProps());
		assertEquals(comment.getMeta().getType(), ActionType.COMMENT);
	}
	@Test
	public void testParseLine_returnPrint() throws Exception {
		init(runner);
		Action print = ReplaceTools.parseLine("print ${thread.count}", runner.getProps());
		assertEquals(print.getMeta().getType(), ActionType.PRINT);
		assertEquals(print.getMeta().getRaw().trim(),"8");
	}
	@Test
	public void testParseLine_returnExit() throws Exception {
		init(runner);
		Action exit = ReplaceTools.parseLine("exit", runner.getProps());
		assertEquals(exit.getMeta().getType(), ActionType.EXIT);
	}
	@Test
	public void testParseLine_returnHistory() throws Exception {
		init(runner);
		Action history = ReplaceTools.parseLine("history", runner.getProps());
		assertEquals(history.getMeta().getType(), ActionType.HISTORY);
	}
	@Test
	public void testParseLine_returnCommand() throws Exception {
		init(runner);
		Action command = ReplaceTools.parseLine("cmd \"C:/\" \"dir\" \"\" \"\"", runner.getProps());
		assertEquals(command.getMeta().getType(), ActionType.CMD);
	}
	@Test
	public void testParseLine_returnRun() throws Exception {
		init(runner);
		Action run = ReplaceTools.parseLine("run build/all", runner.getProps());
		assertEquals(run.getMeta().getType(), ActionType.RUN);
	}
	@Test
	public void testParseLine_returnStart() throws Exception {
		init(runner);
		Action start = ReplaceTools.parseLine("start \"cd front & mvn clean test\" \"BUILD SUCCESS\"", runner.getProps());
		assertEquals(start.getMeta().getType(), ActionType.START);
	}
	@Test
	public void testParseLine_badInputShouldReturnNull() throws Exception {
		init(runner);
		Action not = ReplaceTools.parseLine("not recognised", runner.getProps());
		assertNull(not);
		Action not2 = ReplaceTools.parseLine("not", runner.getProps());
		assertNull(not2);
		Action not3 = ReplaceTools.parseLine("", runner.getProps());
		assertNull(not3);
	}

//	@Test
//	public void testParseQuotedInputs() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void testResolve() {
//		fail("Not yet implemented");
//	}

}
