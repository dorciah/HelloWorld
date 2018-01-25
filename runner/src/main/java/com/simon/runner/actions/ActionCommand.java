package com.simon.runner.actions;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import com.simon.runner.enums.ActionOutcome;
import com.simon.tools.ReplaceTools;

public class ActionCommand extends Action {

	private static PrintWriter printWriter = null;
	
	public ActionCommand(ActionMetadata meta) {
		super(meta);
	}

	@Override
	public boolean validate() {
		List<String> names = new ArrayList<String>();
		names.add("dir");
		names.add("cmd");
		names.add("success");
		names.add("failure");
		if (this.getMeta().getRaw().contains(" ")) {
			this.getMeta().setParsedInput(ReplaceTools.parseQuotedInputs(this.getMeta().getRaw(), names));
			if (this.getMeta().getParsedInput() != null && this.getMeta().getParsedInput().size() == 4) {
				if (new File(getMeta().getParsedInput().get("dir")).exists()) {
						return true;
				} else {
					logger.error("The directory to run the command does not exist - "
							+ this.getMeta().getParsedInput().get("dir"));
					return false;
				}
			} else {
				logger.error("could not parse directory and command from input " + this.getMeta().getRaw());
				return false;
			}

		} else {
			logger.warn("Expecting command to contain 3 spaces - " + this.getMeta().getRaw());
			return false;
		}
	}

	@Override
	public Object runAction() {
		
		try {
			printWriter = new PrintWriter(new File(this.getLogFile()));
		} catch (FileNotFoundException e1) {
			logger.error("Can't create log file " + this.getLogFile());
			this.getMeta().setErrorText("Could not create log file, Not running action");
			return null;
		}
		ProcessBuilder pb = new ProcessBuilder("cmd.exe", "/C", this.getMeta().getParsedInput().get("cmd"));
		pb.directory(new File(this.getMeta().getParsedInput().get("dir")));
		try {
			Process process = pb.start();
			BufferedReader out = new BufferedReader(new InputStreamReader(process.getInputStream()));
			BufferedReader err = new BufferedReader(new InputStreamReader(process.getErrorStream()));
			out.lines().forEach(s -> ActionCommand.printAndCheck(s, getMeta().getParsedInput().get("success"),getMeta().getParsedInput().get("failure")));
			if (process.exitValue() == 1) {
				logger.warn("Command did not terminate successfully");
				getMeta().setErrorText("Error during execution of command");
				err.lines().peek(System.err::println).forEach(printWriter::println);
			}
		} catch (IllegalArgumentException e) {
			// exit
		} 
		catch (Exception e) {
			getMeta().setErrorText("Exception during action execution");
			getMeta().setOutcome(ActionOutcome.ERROR);
		} 
		finally {
			printWriter.close();
		}
		return "";
	}
	
	public static void printAndCheck(String toPrint, String success, String failure) throws IllegalArgumentException, NullPointerException{
		System.out.println(toPrint);
		printWriter.println(toPrint);
		if (toPrint.contains(success) && success.length()>0) {
			throw new IllegalArgumentException("output matches the exit condition!");
		}
		else if (toPrint.contains(failure) && failure.length()>0) {
			throw new NullPointerException("Failure condition was met: " + toPrint);
		}
			
	}

}
