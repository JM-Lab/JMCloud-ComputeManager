package com.jmcloud.compute.commons;

import org.apache.log4j.Logger;

public interface RunCLI {

	public static Logger logger = Logger.getLogger(RunCLI.class);

	public abstract boolean run(String command);

	public abstract String getResultOut();

	public abstract boolean getCheckError();

}