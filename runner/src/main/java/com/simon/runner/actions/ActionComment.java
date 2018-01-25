package com.simon.runner.actions;

public class ActionComment extends Action {

	public ActionComment(ActionMetadata meta) {
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
