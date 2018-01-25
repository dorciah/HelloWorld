package com.simon.runner.actions;

public interface Actionable {

	public boolean validate();
	
	public void prepare(String logDir);
	
	public void checkOutput() throws Exception;
	
	public Object runAction();
	
}
