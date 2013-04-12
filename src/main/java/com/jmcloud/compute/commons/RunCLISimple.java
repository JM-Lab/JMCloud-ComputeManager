package com.jmcloud.compute.commons;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.stereotype.Service;

@Service("runCLISimple")
public class RunCLISimple implements RunCLI {

	protected StringBuffer resultOut;

	protected boolean checkError;

	protected Process process;

	private void runInit() {
		resultOut = new StringBuffer();

		checkError = true;
	}

	@Override
	public boolean run(String command) {

		runInit();

		logger.info("Command: " + command);

		try {
			process = new ProcessBuilder(command.split(" ")).start();

			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			BufferedReader stdErr = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			stdOutAndErr(stdOut, stdErr);

		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

		return checkError;
	}

	protected void stdOutAndErr(BufferedReader stdOut, BufferedReader stdErr)
			throws IOException {
		String resultLine;

		while ((resultLine = stdOut.readLine()) != null) {
			writeSTDOut(resultLine);
		}

		while ((resultLine = stdErr.readLine()) != null) {
			writeErr(resultLine);
			checkError = false;
		}
	}

	protected void writeSTDOut(String line) {
		resultOut.append(line);
		resultOut.append("\n");
		logger.info(line);
	}

	protected void writeErr(String line) {
		resultOut.append(line);
		resultOut.append("\n");
		logger.fatal(line);
	}

	@Override
	public String getResultOut() {
		return resultOut.toString();
	}

	@Override
	public boolean getCheckError() {
		return checkError;
	}

}
