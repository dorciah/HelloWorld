package com.simon.runner.actions;

public class ActionPrint extends Action {

	public ActionPrint(ActionMetadata meta) {
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
