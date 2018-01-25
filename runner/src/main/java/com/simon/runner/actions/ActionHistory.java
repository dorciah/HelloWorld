package com.simon.runner.actions;

public class ActionHistory extends Action {

	public ActionHistory(ActionMetadata meta) {
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
