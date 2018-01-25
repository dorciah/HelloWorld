package com.simon.runner;

import java.io.IOException;

//import static org.junit.Assert.*;

import org.junit.Test;

public class Test_Runner_Init {

	@Test(expected = NumberFormatException.class)
	public void testInit_nopropsShouldThrowNumberException() throws NumberFormatException, NullPointerException, IOException {
		Runner runner = new Runner("properties/noprops.properties");
		runner.init();
	}
	
	@Test(expected = NullPointerException.class)
	public void testInit_noPropFileShouldThrowNullException() throws NumberFormatException, NullPointerException, IOException {
		Runner runner = new Runner("properties/_.properties");
		runner.init();
	}
	
	@Test(expected = NumberFormatException.class)
	public void testInit_textForNumericPropShouldThrowException() throws NumberFormatException, NullPointerException, IOException {
		Runner runner = new Runner("properties/text.properties");
		runner.init();
	}
	
	@Test(expected = NullPointerException.class)
	public void testInit_noLogDirShouldThrowNullException() throws NumberFormatException, NullPointerException, IOException {
		Runner runner = new Runner("properties/no_log.properties");
		runner.init();
	}
	
	@Test(expected = IOException.class)
	public void testInit_badLogDirShouldThrowIOException() throws NumberFormatException, NullPointerException, IOException {
		Runner runner = new Runner("properties/bad_log_dir.properties");
		runner.init();
	}


}
