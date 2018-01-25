package com.simon.tools;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

import com.simon.runner.actions.Action;
import com.simon.runner.actions.ActionFactory;
import com.simon.runner.enums.ActionType;

public class ReplaceTools {

	public final static String REGEX_DOLLAR_BRACES = "\\$\\{(.*?)\\}";
	public final static String REGEX_QUOTED = "\"([^\"]*)\"|(\\S+)";
	public static final Logger logger = Logger.getLogger(ReplaceTools.class);
	
	public static Action parseLine(String raw, Properties props) {
		// tidy and resolve placeholders
		String trimmed = ReplaceTools.resolve(raw, props);
		
		// decide what to do. Some commands have no parameters
		if (trimmed.startsWith("#")) return ActionFactory.CreateAction(ActionType.COMMENT,"");
		else if (trimmed.equalsIgnoreCase(ActionType.EXIT.toString())) return ActionFactory.CreateAction(ActionType.EXIT,"");
		else if (trimmed.equalsIgnoreCase(ActionType.HISTORY.toString())) return ActionFactory.CreateAction(ActionType.HISTORY,"");
		else if (trimmed.contains(" ")) {
			String type = trimmed.split(" ")[0].toUpperCase();
			for (ActionType AT : ActionType.values()) {
				if (AT.toString().equalsIgnoreCase(type))
					return ActionFactory.CreateAction(AT, trimmed.substring(trimmed.indexOf(type)+type.length()+1));
			}
			logger.warn("Could not identify type of action " + type);
			return null;
		}
		else {
			logger.warn("Could not find command type in line: " + trimmed);
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public static Map<String,String> parseQuotedInputs(String input,List<String> names){
		Map<String,String> out = new HashMap<String,String>();
		Matcher m = Pattern.compile(REGEX_QUOTED).matcher(input);
		List<String> parsed = new ArrayList<String>();
		while (m.find()) {
			if (m.group(1) != null) {
				parsed.add(m.group(1));
			} else {
				// ignore
			}
		}
		if (parsed.size() == names.size()) {
			int index = 0;
			for (String s : parsed) {
				out.put(names.get(index), s);
				index++;
			}
		} else {
			logger.warn("Expecting input to contain " + names.size() + " quoted strings - " + input);
			return Collections.EMPTY_MAP;
		}
		return out;
	}
	
	public static String resolve(String input, Properties props) {
		
		String out = input;
		Matcher m = Pattern.compile(REGEX_DOLLAR_BRACES).matcher(input);
		Map<String,String> toReplace = new HashMap<>();
		while (m.find()) {
	        if (m.group(1) != null) {
	            if (props.containsKey(m.group(1))) {
	            	toReplace.put(m.group(1),(String) props.get(m.group(1)));
	            }
	            else {
	            	logger.warn("Could not resolve property " + m.group(1));
	            }
	        } else {
	            // ignore as no matches
	        }
		}
		if (toReplace.size()>0) {
			for (String key : toReplace.keySet()) {
				out = out.replace("${"+key+"}", toReplace.get(key));
			}
		}
		return out;
	}
	
}
