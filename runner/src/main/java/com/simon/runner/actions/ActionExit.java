package com.simon.runner.actions;

public class ActionExit extends Action {

	public ActionExit(ActionMetadata meta) {
		super(meta);
	}

	@Override
	public boolean validate() {
		return true;
	}

	@Override
	public Object runAction() {
		return null;
	}

}
