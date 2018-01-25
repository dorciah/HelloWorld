package com.simon.runner.actions;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import com.simon.runner.enums.ActionOutcome;
import com.simon.runner.enums.ActionType;
import com.simon.tools.JSONTools;

public class ActionMetadata {

	public static AtomicInteger baseId = new AtomicInteger();
	private ActionType type;
	private ActionOutcome outcome;
	private String raw;
	private long start;
	private long end;
	private long id;
	private String errorText;
	private Map<String,String> parsedInput;
	
	public ActionMetadata(ActionType type, String raw) {
		this.type = type;
		this.raw = raw;
		init();
	}
	
	public void init() {
		this.id = baseId.incrementAndGet();
		this.outcome = ActionOutcome.QUEUED;
		this.setParsedInput(new HashMap<>());
	}

	public String toString() {
		return JSONTools.toJSONStringSimple(this);
	}
	
	public String toJSONPretty() {
		return JSONTools.toJSONString(this);
	}
	
	public void addToParsedInput(String key, String value) {
		this.parsedInput.put(key, value);
	}
	
	/* getters and setters */
	public ActionType getType() {
		return type;
	}
	public void setType(ActionType type) {
		this.type = type;
	}
	public ActionOutcome getOutcome() {
		return outcome;
	}
	public void setOutcome(ActionOutcome outcome) {
		this.outcome = outcome;
	}
	public String getRaw() {
		return raw;
	}
	public void setRaw(String raw) {
		this.raw = raw;
	}
	public long getStart() {
		return start;
	}
	public void setStart(long start) {
		this.start = start;
	}
	public long getEnd() {
		return end;
	}
	public void setEnd(long end) {
		this.end = end;
	}
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getErrorText() {
		return errorText;
	}
	public void setErrorText(String errorText) {
		this.errorText = errorText;
	}
	public Map<String,String> getParsedInput() {
		return parsedInput;
	}
	public void setParsedInput(Map<String,String> parsedInput) {
		this.parsedInput = parsedInput;
	}
}
