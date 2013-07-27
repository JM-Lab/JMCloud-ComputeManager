package com.jmcloud.compute.aws.ec2.manager.provisioner;

import java.io.File;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.sys.EC2EnviromentVO;
import com.jmcloud.compute.commons.RunCLI;
import com.jmcloud.compute.sys.SystemEnviroment;

@Service("runEC2CLI")
public class RunEC2CLI implements RunCLI {

	@Resource(name = "eC2EnviromentVO")
	private EC2EnviromentVO eC2EnvVO;

	@Resource(name = "runCLISimple")
	private RunCLI runCLI;

	private boolean isWindows = SystemEnviroment.getOS().contains(
			"Windows");

	private String getFinalCammand(String command) {
		if (isWindows) {
			command = forWindowsCommand(command);
		}
		StringBuffer sb = new StringBuffer(eC2EnvVO.getEC2CLIHome());
		sb.append(File.separator);
		sb.append("bin");
		sb.append(File.separator);
		sb.append(command);
		sb.append(" -O ");
		sb.append(eC2EnvVO.getAccessKey());
		sb.append(" -W ");
		sb.append(eC2EnvVO.getSecretKey());
		return sb.toString();
	}

	private String forWindowsCommand(String command) {
		if (command.contains(" ")) {
			String[] tempString = command.split(" ", 2);
			tempString[0] = tempString[0] + ".cmd";
			return tempString[0] + " " + tempString[1];
		}
		return command + ".cmd";
	}

	@Override
	public boolean run(String command) {
		try {
			return runCLI.run(getFinalCammand(command));
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String getResultOut() {
		return runCLI.getResultOut();
	}

	@Override
	public boolean getCheckError() {
		return runCLI.getCheckError();
	}

}
