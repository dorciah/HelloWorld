package com.simon.runner.actions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.simon.runner.enums.ActionOutcome;
import com.simon.tools.ReplaceTools;

public class ActionPropSet extends Action {

	private Properties props;
	
	public ActionPropSet(ActionMetadata meta) {
		super(meta);
	}

	@Override
	public boolean validate() {
		List<String> names = new ArrayList<String>();
		names.add("dir");
		names.add("file");
		if (this.getMeta().getRaw().contains(" ")) {
			this.getMeta().setParsedInput(ReplaceTools.parseQuotedInputs(this.getMeta().getRaw(), names));
			if (this.getMeta().getParsedInput() != null && this.getMeta().getParsedInput().size() == 2) {
				if (!new File(getMeta().getParsedInput().get("dir")).exists()) {
					logger.error("The directory to run the command does not exist - "
							+ this.getMeta().getParsedInput().get("dir"));
					return false;
				}
				if (!new File(getMeta().getParsedInput().get("dir") + "/" + getMeta().getParsedInput().get("file") +".tmp").exists()) {
					logger.error("The file to replace properties does not exist - "
							+ this.getMeta().getParsedInput().get("dir"));
					return false;
				}
			} else {
				logger.error("could not parse directory and file from input " + this.getMeta().getRaw());
				return false;
			}

		} else {
			logger.warn("Expecting command to contain a space - " + this.getMeta().getRaw());
			return false;
		}
		return true;
	}

	@SuppressWarnings("resource")
	@Override
	public Object runAction() {
		Stream<String> lines = null;
		String fullPath = null;
		try {
			fullPath = this.getMeta().getParsedInput().get("dir") + "/" + this.getMeta().getParsedInput().get("file") ;
			lines = Files.lines(Paths.get(fullPath + ".tmp"));
		} catch (IOException e) {
			logger.error("Couldn't read template file: "+  fullPath+ ".tmp");
			getMeta().setErrorText("IO Exception reading file from " + fullPath+ ".tmp");
			getMeta().setOutcome(ActionOutcome.ERROR);
			return null;
		}
		if (lines==null) {
			logger.error("Null contents of file: "+  fullPath+ ".tmp");
			getMeta().setErrorText("Null contents reading file from " + fullPath+ ".tmp");
			getMeta().setOutcome(ActionOutcome.ERROR);
			return null;
		}
		List<String> out = lines.map(str -> ReplaceTools.resolve(str, this.props)).collect(Collectors.toList());
		try {
			Files.write(Paths.get(fullPath), out, Charset.forName("UTF-8"));
		} catch (IOException e) {
			logger.error("IO Exception trying to write to new props file " + fullPath);
		}
		
		return null;
	}
	
	public void setProps(Properties props) {
		this.props = props;
	}

}
