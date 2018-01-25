package com.simon.runner.actions;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.simon.tools.ReplaceTools;

public class ActionRun extends Action {

	private Properties props;
	
	public ActionRun(ActionMetadata meta) {
		super(meta);
	}

	@Override
	public boolean validate() {
		try (InputStream input = this.getClass().getClassLoader()
				.getResourceAsStream("scripts/" + this.getMeta().getRaw().trim())) {
			input.read();
		} catch (IOException e) {
			logger.error("Script " + this.getMeta().getRaw() + " cannot be read");
			this.getMeta().setErrorText("Script " + this.getMeta().getRaw() + " cannot be read");
			return false;
		} catch (NullPointerException e) {
			logger.error("Script " + this.getMeta().getRaw() + " cannot be found");
			this.getMeta().setErrorText("Script " + this.getMeta().getRaw() + " does not exist");
			return false;
		}
		this.getMeta().addToParsedInput("script", getMeta().getRaw().trim());
		return true;
	}

	@Override
	public Object runAction() {
		List<Action> out = new ArrayList<>();
		try (BufferedReader br = new BufferedReader(new InputStreamReader(
				this.getClass().getClassLoader().getResourceAsStream("scripts/" + this.getMeta().getRaw().trim())))) {
			out = br.lines().map((s) -> ReplaceTools.parseLine(s, this.props)).collect(Collectors.toList());
			Collections.reverse(out);
		} catch (IOException e) {
			logger.error("Script " + this.getMeta().getRaw() + " does not exist");
			this.getMeta().setErrorText("Script " + this.getMeta().getRaw() + " does not exist");
		}
		for (Action a : out) {
			System.out.println(a.getMeta().toString());
		}
		return out;
	}

	public Properties getProps() {
		return props;
	}

	public void setProps(Properties props) {
		this.props = props;
	}

}
