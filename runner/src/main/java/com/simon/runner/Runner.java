package com.simon.runner;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;
import java.util.Stack;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.log4j.Logger;

import com.simon.runner.actions.Action;
import com.simon.runner.actions.ActionPropSet;
import com.simon.runner.actions.ActionRun;
import com.simon.runner.actions.ActionStart;
import com.simon.runner.actions.ActionMetadata;
import com.simon.runner.enums.ActionOutcome;
import com.simon.runner.enums.ActionType;

/*
 * <h1>Runner</h1>
 * Command line tool to run a variety of actions
 * <ul>
 * <li>Command line action run in same window</li>
 * <li>Command line run independently</li>
 * <li>Set properties in a file</li>
 * <li>Run a script</li>
 * </ul>
 */

public class Runner {

	public static Logger logger = Logger.getLogger(Runner.class);
	private Properties props;
	private List<ActionMetadata> history;
	private Stack<Action> stack;
	private ExecutorService es;
	private String logDir;
	private String propLoc;
	
	public Runner(String propLoc) {
		this.propLoc = propLoc;
	}
	
	/* 
	 * Main method which invokes the command line listener
	 * @param args unused
	 * @return Nothing
	 * @exception None
	 */
	public static void main(String[] args) {
		
		// Initialise and confirm key properties
		Runner runner = new Runner("properties/main.properties");
		try {
			runner.init();
		} catch (IOException e) {
			logger.error("Could not load main.properties from the classpath",e);;
			System.exit(1);
		} catch (NumberFormatException e) {
			logger.error("Couldn't parse the thread count",e);
		} catch (NullPointerException e) {
			logger.error("Could not set all essentual properties");
			System.exit(1);
		}
		
		// start the command listening loop
		Scanner scanner = new Scanner(System.in);
		runner.welcomeMessage();
		ConsoleListener input = null;
		Future<Action> returnedInput = null;
		while (true) {
			try {
				if (returnedInput==null || returnedInput.isDone()) {
					if (returnedInput!=null && returnedInput.isDone()) {
						try {
							runner.handleInput(returnedInput.get());
						} catch (NullPointerException e) {
							e.printStackTrace();
							logger.warn("Not executing command as could not find a matching action");
						} catch (Exception e) {
							logger.error("Error whilsr running actions " + e.getMessage());
						}
					}
					input = new ConsoleListener(scanner,runner.props);
					returnedInput = runner.es.submit(input);
				}
				Thread.sleep(100);
			} catch (InterruptedException e) {
				logger.error("Error waiting for command line input",e);
				return;
			}
		}
	}
	
	public void handleInput(Action toRun) throws Exception {
		// EXIT, HISTORY and PRINT need to be handled from here
		if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.EXIT)) {
			close();
			System.exit(0);
		}
		else if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.HISTORY)) {
			logger.info("Printing history");
			this.printHistory();
		}
		else if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.PRINT)) {
			System.out.println(toRun.getMeta().getRaw().trim());
		}
		else if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.COMMENT)) {
			logger.info("Ignoring comment");
		}
		else {
			if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.PSET))
				((ActionPropSet)(toRun)).setProps(this.props);
			if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.RUN))
				((ActionRun)(toRun)).setProps(this.props);
			if (toRun.getMeta()!=null && toRun.getMeta().getType().equals(ActionType.START))
				((ActionStart)(toRun)).setProps(this.props);
			this.addToHistory(toRun.getMeta());
			this.startJob(toRun);
		}
	}
	
	private void welcomeMessage() {
		StringBuilder sb = new StringBuilder("\n");
		sb.append(" ______     __   __   ____    __   ____    __   _____   ______ \n");
		sb.append("/   _  \\   |  | |  | |    \\  |  | |    \\  |  | |   __| /   _  \\ \n");
		sb.append("|  |_|  |  |  | |  | |  |  \\ |  | |  |  \\ |  | |  |__  |  |_|  |\n");
		sb.append("|   __  \\  |  | |  | |  |\\  \\|  | |  |\\  \\|  | |   __| |   __  \\ \n");
		sb.append("|  |  \\  | |  |_|  | |  | \\  |  | |  | \\  |  | |  |__  |  |  \\  | \n");
		sb.append("|__|  |__| \\_______/ |__|  \\____| |__|  \\____| |_____| |__|  |__|\n");
		sb.append("\nWelcome to Runner!\n");
		sb.append("Type exit to leave\n");
		sb.append("Logging to " + this.logDir + "\n");
		sb.append("-----------------------\n");
		System.out.print(sb.toString());
		logger.info("Logger started");
	}

	private void startJob(Action a) throws Exception{
		if (a.validate()) {
			a.prepare(this.logDir);
			
			// if runAction then we need to see if there are queued actions
			if (a instanceof ActionRun) {
				@SuppressWarnings("unchecked")
				List<Action> toAdd = (List<Action>) a.runAction();
				for (Action child : toAdd) {
					addToStack(child);
				}
				while (!this.stack.isEmpty()) {
					try{
						handleInput(this.stack.pop());
					} catch (Exception e) {
						logger.error("Error occured when running a script. Stopping execution");
						logger.error(this.stack.size() + " jobs remaining");
						while (!this.stack.isEmpty()) {
							logger.warn("Not running " + this.stack.pop().getMeta().getRaw());
						}
					}
				}
				a.checkOutput();
			}
			else if (a instanceof ActionStart) {
				this.es.submit(((ActionStart)a));
			}
			else {
				a.runAction();
				a.checkOutput();
			}
			
		} else {
			a.getMeta().setErrorText("Action did not validate successfully");
			a.getMeta().setOutcome(ActionOutcome.ERROR);
		}
		
	}

	/* 
	 * print a message before exiting
	 * @param nothing
	 * @return nothing
	 * @exception none
	 */
	private static void close() {
		logger.info("Closing Runner");
	}

	/*
	 * Method to initialise lists and properties
	 * @param nothing
	 * @return void
	 * @exception IOException if can't read properties
	 */
	public void init() throws IOException, NumberFormatException, NullPointerException {
		this.history = new ArrayList<ActionMetadata>();	
		this.stack = new Stack<Action>();
		this.props = new Properties();
		InputStream input = this.getClass().getClassLoader()
                .getResourceAsStream(this.propLoc);
		try{
			this.props.load(input);
		} catch (NullPointerException e) {
			logger.error("Could not load properties");
			throw new NullPointerException();
		}
		Integer nThreads = null;
		try{
			nThreads = Integer.parseInt((String) this.props.get("thread.count"));
		} catch (NumberFormatException e) {
			logger.error("Could not parse " + this.props.get("thread.count") + " - expecting number");
			throw new NumberFormatException();
		}
		this.es = Executors.newFixedThreadPool(nThreads);
		this.logDir = (String) this.props.get("log.dir");
		if (this.logDir == null) {
			logger.error("Log directory is not specified in properties");
			throw new NullPointerException();
		}
		else if (!new File(this.logDir).exists()) {
			logger.error("Log directory does not exist - " + this.logDir);
			throw new IOException();
		}
		else {
			this.logDir = this.logDir + "/" + System.currentTimeMillis();
			new File(this.logDir).mkdir();
		}
	}
	
	/*
	 * Convenience method to add to history
	 * @param ActionMetadata to add to history
	 * @return boolean - true if the item was added
	 * @exception none
	 */
	public boolean addToHistory(ActionMetadata toAdd) {
		return this.history.add(toAdd);
	}
	
	/*
	 * Print the job history
	 */
	public void printHistory() {
		for (ActionMetadata m : this.history) {
			System.out.print(m.toString()+"\n");
		}
	}
	
	/*
	 * Add actions to the queue (from beginning)
	 */
	
	public void addToStack(Action a) {
		this.stack.push(a);
	}
	
	/* getters and setters */
	public Properties getProps() {
		return props;
	}
	public void setProps(Properties props) {
		this.props = props;
	}
	public List<ActionMetadata> getHistory() {
		return history;
	}
	public void setHistory(List<ActionMetadata> history) {
		this.history = history;
	}
	public Stack<Action> getStack() {
		return stack;
	}
	public void setStack(Stack<Action> stack) {
		this.stack = stack;
	}

	public String getPropLoc() {
		return propLoc;
	}

	public void setPropLoc(String propLoc) {
		this.propLoc = propLoc;
	}
}
