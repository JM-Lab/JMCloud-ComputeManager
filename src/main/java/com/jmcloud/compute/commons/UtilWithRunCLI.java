package com.jmcloud.compute.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.jmcloud.compute.sys.SystemEnviroment;

public class UtilWithRunCLI {
	public static boolean connectServerWithSSH(String keypairPath, String id,
			String publicIP, Logger logger) {
		List<String> command = new ArrayList<>();
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i "
				+ keypairPath + " " + id + "@" + publicIP;
		if (SystemEnviroment.getOS().contains("Windows")) {
			command.add("cmd");
			command.add("/c");
			command.add("start");
			command.add("cmd");
			command.add("/c");
			command.add(commonCommand);
		} else if (SystemEnviroment.getOS().contains("Mac")) {
			command.add("osascript");
			command.add("-e");
			command.add("tell application \"Terminal\" to do script \""
					+ commonCommand + "\"");
		} else {
			logger.error("Not Support On " + SystemEnviroment.getOS());
			return false;
		}
		logger.info("Command To Connect Compute : " + printCommand(command));
		try {
			new ProcessBuilder(command).start();
		} catch (IOException e) {
			e.printStackTrace();
			logger.fatal(e);
			return false;
		}
		return true;
	}

	private static String printCommand(List<String> command) {
		StringBuilder sb = new StringBuilder();
		for (String s : command) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString();
	}

}
