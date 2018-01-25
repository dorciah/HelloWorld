package com.simon.runner.actions;

import org.apache.log4j.Logger;

import com.simon.runner.enums.ActionOutcome;

public abstract class Action implements Actionable {

	public final Logger logger = Logger.getLogger(Action.class);
	private ActionMetadata meta;
	private String logFile;

	public Action(ActionMetadata meta) {
		this.meta = meta;
	}
	
	public void checkOutput() throws Exception {
		this.getMeta().setEnd(System.currentTimeMillis());
		if (this.getMeta().getErrorText()==null) {
			this.getMeta().setOutcome(ActionOutcome.COMPLETE);
			logger.info("Action " + this.getMeta().getId() + " complete! in " + (this.getMeta().getEnd() - this.getMeta().getStart())/1000 + " seconds");
		}
		else {
			logger.error("An error occured when running action " + this.getMeta().getId());
			this.getMeta().setOutcome(ActionOutcome.ERROR);
			throw new Exception();
		}
	}
	
	public void prepare(String logDir) {
		this.logFile = logDir + "/" + this.getMeta().getId() + "_" + this.getMeta().getType() + ".log";
		this.getMeta().setStart(System.currentTimeMillis());
		this.getMeta().setOutcome(ActionOutcome.ACTIVE);
		logger.info("Starting action " + this.getMeta().getId() + " " + this.getMeta().getType() + " " + this.getMeta().getParsedInput().toString());
	}
	
	/* getters and setters */
	public ActionMetadata getMeta() {
		return meta;
	}
	public void setMeta(ActionMetadata meta) {
		this.meta = meta;
	}

	public String getLogFile() {
		return logFile;
	}

	public void setLogFile(String logFile) {
		this.logFile = logFile;
	}

}
