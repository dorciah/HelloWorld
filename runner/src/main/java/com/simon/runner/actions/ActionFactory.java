package com.simon.runner.actions;

import org.apache.log4j.Logger;

import com.simon.runner.enums.ActionType;

public class ActionFactory {

	public static Logger logger = Logger.getLogger(ActionFactory.class);
	
	public static Action CreateAction(ActionType type, String input) {
		Action a = null;
		ActionMetadata meta = new ActionMetadata(type, input);
		if (type.equals(ActionType.CMD)) {
			a =  new ActionCommand(meta);
		} else if (type.equals(ActionType.EXIT)) {
			a = new ActionExit(meta);
		} else if (type.equals(ActionType.HISTORY)) {
			a = new ActionHistory(meta);
		} else if (type.equals(ActionType.PRINT)) {
			a = new ActionPrint(meta);
		} else if (type.equals(ActionType.PSET)) {
			a = new ActionPropSet(meta);
		} else if (type.equals(ActionType.COMMENT)) {
			a = new ActionPropSet(meta);
		} else if (type.equals(ActionType.RUN)) {
			a = new ActionRun(meta);
		}else if (type.equals(ActionType.START)) {
			a = new ActionStart(meta);
		}
		else logger.warn(type + " not implemented yet");
		return a;
	}
}
