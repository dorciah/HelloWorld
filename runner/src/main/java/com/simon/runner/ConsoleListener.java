package com.simon.runner;

import java.util.Properties;
import java.util.Scanner;
import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

import com.simon.runner.actions.Action;
import com.simon.tools.ReplaceTools;

public class ConsoleListener implements Callable<Action> {

	public static Logger logger = Logger.getLogger(ConsoleListener.class); 
	private Scanner scanner;
	private Properties props;
	
	public ConsoleListener(Scanner scanner, Properties props) {
		this.scanner = scanner;
		this.props = props;
	}
	
	@Override
	public Action call() throws Exception {
		return ReplaceTools.parseLine(scanner.nextLine(),this.props);
	}
	
	

}
