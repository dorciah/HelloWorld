package com.simon.runner.actions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import com.simon.runner.enums.ActionOutcome;
import com.simon.tools.ReplaceTools;

public class ActionStart extends Action implements Callable<String> {

	private Properties props;

	public ActionStart(ActionMetadata meta) {
		super(meta);
	}

	@Override
	public boolean validate() {
		List<String> names = new ArrayList<String>();
		names.add("cmd");
		names.add("exit");
		if (this.getMeta().getRaw().contains(" ")) {
			this.getMeta().setParsedInput(ReplaceTools.parseQuotedInputs(this.getMeta().getRaw(), names));
			if (this.getMeta().getParsedInput() != null && this.getMeta().getParsedInput().size() == 2) {
				return true;
			} else {
				logger.error("could not parse directory and command from input " + this.getMeta().getRaw());
				return false;
			}

		} else {
			logger.warn("Expecting command to contain a space - " + this.getMeta().getRaw());
			return false;
		}
	}

	@Override
	public Object runAction() {

		// create the process
		Process process = createProcess();
		
		// watch the initial wrapper (wrapper which just prints done once the real job is launched)
		if(watchWrapper(process)) {
			watchLog();
		}
		
		try {
			checkOutput();
		} catch (Exception e) {
			logger.error("An error occured when running action " + e.getMessage());
			e.printStackTrace();
		}
		return null;
	}

	private boolean watchWrapper(Process process) {
		BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
		String m = "";
		try {
			while ((m = br.readLine()) != null) {
				if (!m.equalsIgnoreCase("done")) {
					logger.info(m);
				} else {
					break;
				}
			}
		} catch (IOException e) {
			String error = "Error executing command - " + this.getMeta().getParsedInput().get("cmd") + " in "
					+ this.getProps().get("base.dir");
			logger.error(error);
			getMeta().setErrorText(error);
			getMeta().setOutcome(ActionOutcome.ERROR);
			return false;
		} finally {
			if (process.isAlive())
				process.destroyForcibly();
		}
		return true;
	}

	private Process createProcess() {
		String[] cmd = new String[] { "cmd.exe", "/C", "run.cmd", getMeta().getParsedInput().get("cmd"), getLogFile() };
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(cmd, null, new File((String) this.getProps().get("base.dir")));
		} catch (IOException e) {
			String error = "Error building command - " + this.getMeta().getParsedInput().get("cmd") + " in "
					+ this.getProps().get("base.dir");
			logger.error(error);
			getMeta().setErrorText(error);
			getMeta().setOutcome(ActionOutcome.ERROR);
			return null;
		}
		return process;
	}

	private void watchLog() {
		logger.info("Watching log file...");
		String error = null;
		try {
			while (!new File(getLogFile()).exists()) {
				Thread.sleep(1000); // break to wai for log file to be created
			}
			@SuppressWarnings("resource")
			BufferedInputStream reader = new BufferedInputStream(new FileInputStream(getLogFile()));
			StringBuilder sb = new StringBuilder();
			while (true) {
				
				if (reader.available() > 0) {
					char c = (char) reader.read();
					if (String.valueOf(c).matches(".")) {
						sb.append(c);
					} else {
						if (sb.toString().contains(getMeta().getParsedInput().get("exit"))) {
							logger.info(sb.toString() + " exiting...");
							break;
						}
						sb.setLength(0);
					}
				} else {
					try {
						Thread.sleep(500);
					} catch (InterruptedException ex) {
						break;
					}
				}
			}
		} catch (FileNotFoundException e) {
			error = "Could not find log file. " + e.getMessage();
		} catch (IOException e) {
			error="IO exception " + e.getMessage();
		} catch (InterruptedException e) {
			error = e.getMessage();
		}
		if (error!=null) {
			logger.error(error);
			getMeta().setErrorText(error);
			getMeta().setOutcome(ActionOutcome.ERROR);
		}
	}

	@Override
	public String call() throws Exception {
		return (String) runAction();
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
