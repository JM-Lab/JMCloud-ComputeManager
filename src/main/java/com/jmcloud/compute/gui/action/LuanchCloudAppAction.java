package com.jmcloud.compute.gui.action;

import static com.jmcloud.compute.aws.ec2.manager.EC2Manager.RUNNING_STATUS;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.KEYPAIR_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.PUBLIC_IP_INDEX;
import static com.jmcloud.compute.gui.model.TableViewPanelModel.STATUS_INDEX;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.aws.ec2.util.EC2Util;
import com.jmcloud.compute.sys.SystemCloudApp;
import com.jmcloud.compute.sys.SystemEnviroment;
import com.jmcloud.compute.util.SysUtils;

@Service("luanchCloudAppAction")
public class LuanchCloudAppAction extends AbstractJMCloudGUIAction {

	private static final String LUANCH_CLOUD_APP_PROCESS = "[Luanch Cloud App Process]\t";

	private static final String LUANCH_PROCESS_FAILURE = LUANCH_CLOUD_APP_PROCESS
			+ "Luanch Process Failure!!!";

	protected int selectionRow;
	protected int[] selectionRows;

	private String keypair;

	private String id;

	private String publicIP;

	private String cloudAppRootDir = "cloudapp/";

	@Override
	protected void initAction(ActionEvent e) {
		super.initAction(e);
		this.selectionRow = computeManagerGUIModel.getSelectionRow();
		this.selectionRows = computeManagerGUIModel.getSelectionRows();
	}

	@Override
	protected String doAbstractAction(ActionEvent e) {
		String result = checkEnv(e);
		if (result.contains(FAILURE_SIGNATURE)){
			return result;
		}

		// id 는 현재 ubuntu 만 지원
		id = "ubuntu";
		publicIP = computeManagerGUIModel.getComputeInfo(selectionRow,
				PUBLIC_IP_INDEX);

		if ("Luanch R Server".equals(e.getActionCommand())) {
			result = luanchRServer();
		} else if ("Luanch Hadoop Psudo".equals(e.getActionCommand())) {
			result = luanchHadoopPsudo();
		} else {
			result = LUANCH_PROCESS_FAILURE;
		}
		return result;
	}


	private String checkEnv(ActionEvent e) {
		if (selectionRows.length != 1) {
			return returnErrorMessage("Must Select Only One Compute On The Table View!!!");
		}
		if (!RUNNING_STATUS.equals(computeManagerGUIModel.getComputeInfo(
				selectionRow, STATUS_INDEX))) {
			return returnErrorMessage("Only Running Status, Can Connet Compute!!!");
		}

		return SUCCESS_SIGNATURE;
	}

	private String luanchHadoopPsudo() {
		// TODO Auto-generated method stub
		return null;
	}

	private String luanchRServer() {
		startProgressSpinner();
		// TODO 방화벽 여는 것 추가 필요
		computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
				+ "Make A Working Directory");
		if (mkdirWorkingDir().contains(FAILURE_SIGNATURE)) {
			return LUANCH_PROCESS_FAILURE;
		}

		computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
				+ "Send A Luanch Pack File");
		String luanchPackName = "RServer";
		if (sendRaunchPack(luanchPackName).contains(FAILURE_SIGNATURE)) {
			return LUANCH_PROCESS_FAILURE;
		}

		computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
				+ "Unpack A Luanch Pack File");
		if (unpackRaunchPack(luanchPackName).contains(FAILURE_SIGNATURE)) {
			return LUANCH_PROCESS_FAILURE;
		}

		computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
				+ "Luanch An App");
		if (luanchApp(luanchPackName).contains(FAILURE_SIGNATURE)) {
			return LUANCH_PROCESS_FAILURE;
		}

		SysUtils.sleep(1);

		computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
				+ "Connect An App");
		if (connectApp().contains(FAILURE_SIGNATURE)) {
			return LUANCH_PROCESS_FAILURE;
		}

		showNextSteps();

		return SUCCESS_SIGNATURE;
	}

	private void showNextSteps() {
		computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
				+ "Next Steps Are As Follows...");
		computeManagerGUIModel
				.showResult("1. create a linux account, ex) sudo passwd ubuntu");
		computeManagerGUIModel
				.showResult("2. login R server with the account, ex) account = ubuntu");
		computeManagerGUIModel
				.showResult("3. run a following example on R Server.");
		computeManagerGUIModel.showResult("x<-rnorm(10)");
		computeManagerGUIModel.showResult("x");
		computeManagerGUIModel.showResult("mean(x)");
		computeManagerGUIModel.showResult("hist(x)");
	}

	private String connectApp() {
		String AppURL = "http://" + publicIP + ":8787";
		logger.info("Connect An App : " + AppURL);
		try {
			Desktop.getDesktop().browse(new URI(AppURL));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			return returnErrorMessage("Can't Connect An App : " + AppURL);
		}
		return returnSuccessMessage(AppURL);
	}

	private String luanchApp(String luanchPackName) {
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " sudo sh " + cloudAppRootDir
				+ luanchPackName + "/" + luanchPackName + ".sh";
		logger.info("Command To Luanch An App : " + printCommand(commonCommand));
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			BufferedReader stdOut = new BufferedReader(new InputStreamReader(
					process.getInputStream()));

			BufferedReader stdErr = new BufferedReader(new InputStreamReader(
					process.getErrorStream()));

			computeManagerGUIModel.showResult(LUANCH_CLOUD_APP_PROCESS
					+ "Progressing Are As Follows...");
			Thread stdOutThread = getStdOutThread(stdOut);
			Thread stdErrThread = getStdErrThread(stdErr);
			stdOutThread.start();
			stdErrThread.start();

			if (process.waitFor() != 0) {
				return returnErrorMessage("Can't Luanch An App!!!");
			}

		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return returnErrorMessage("Can't Luanch An App!!!");
		}

		return returnSuccessMessage(publicIP);
	}

	protected Thread getStdOutThread(final BufferedReader stdOut) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String resultLine;
				try {
					while ((resultLine = stdOut.readLine()) != null) {
						writeSTDOut(resultLine);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	protected Thread getStdErrThread(final BufferedReader stdErr) {
		return new Thread(new Runnable() {
			@Override
			public void run() {
				// TODO Auto-generated method stub
				String resultLine;
				try {
					while ((resultLine = stdErr.readLine()) != null) {
						System.err.println(resultLine);
						writeErr(resultLine);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	protected void stdOutAndErr(BufferedReader stdOut, BufferedReader stdErr)
			throws IOException {
		String resultLine;

		while ((resultLine = stdOut.readLine()) != null) {
			writeSTDOut(resultLine);
		}

		while ((resultLine = stdErr.readLine()) != null) {
			writeErr(resultLine);
		}
	}

	protected void writeSTDOut(String line) {
		computeManagerGUIModel.showResult(line);
		logger.info(line);
	}

	protected void writeErr(String line) {
		computeManagerGUIModel.showResult(line);
		logger.fatal(line);
	}

	private String unpackRaunchPack(String luanchPackName) {
		luanchPackName += ".tar.gz";
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " tar xvzf " + cloudAppRootDir
				+ luanchPackName + " -C " + cloudAppRootDir;
		logger.info("Command To Unpack A Luanch Pack File : "
				+ printCommand(commonCommand));
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			if (process.waitFor() != 0) {
				return returnErrorMessage("Can't Unpack A Luanch Pack File!!!");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return returnErrorMessage("Can't Unpack A Luanch Pack File!!!");
		}

		return returnSuccessMessage(publicIP);
	}

	private String mkdirWorkingDir() {
		keypair = SystemEnviroment.getKeypairDir()
				+ computeManagerGUIModel.getComputeInfo(selectionRow,
						KEYPAIR_INDEX);
		if (!new File(keypair).exists()) {
			return returnErrorMessage("Keypair File Dosen't Exist!!!");
		}
		if (SystemEnviroment.getOS().contains("Windows")) {
			keypair = SysUtils.convertIntoCygwinPath(keypair);
		}
		String commonCommand = "ssh -o StrictHostKeyChecking=no -i " + keypair
				+ " " + id + "@" + publicIP + " mkdir -p " + cloudAppRootDir;
		logger.info("Command To Make Cloud App Directory : "
				+ printCommand(commonCommand));
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			if (process.waitFor() != 0) {
				return returnErrorMessage("Can't Make Cloud App Directory!!!");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return returnErrorMessage("Can't Make Cloud App Directory!!!");
		}

		return returnSuccessMessage(publicIP);
	}

	private String sendRaunchPack(String luanchPackName) {
		// TODO Auto-generated method stub
		// 설치에 필요한 스크립트 파일 및 필요한 것들 먼저 scp 로 보냄

		luanchPackName += ".tar.gz";
		Path luanchPack = SystemCloudApp.getLuanchPackFile(luanchPackName);
		if (luanchPack == null) {
			return returnErrorMessage(luanchPackName + " Doesn't Exist!!!");
		}
		String luanchPackFilePath = luanchPack.toFile().getAbsolutePath();
		if (SystemEnviroment.getOS().contains("Windows")) {
			luanchPackFilePath = SysUtils.convertIntoCygwinPath(luanchPackFilePath);
		}
		String commonCommand = "scp -o StrictHostKeyChecking=no -i " + keypair
				+ " " + luanchPackFilePath + " " + id + "@" + publicIP + ":~/"
				+ cloudAppRootDir;
		logger.info("Command To Send Luanch Pack File : "
				+ printCommand(commonCommand));
		Process process;
		try {
			process = new ProcessBuilder(commonCommand.split(" ")).start();
			if (process.waitFor() != 0) {
				return returnErrorMessage("Can't Send A Luanch Pack File");
			}
		} catch (IOException | InterruptedException e) {
			e.printStackTrace();
			logger.fatal(e);
			return returnErrorMessage("Can't Send A Luanch Pack File");
		}

		return returnSuccessMessage(publicIP);

	}

	private String printCommand(String command) {
		StringBuilder sb = new StringBuilder();
		for (String s : command.split(" ")) {
			sb.append(s);
			sb.append(" ");
		}
		return sb.toString();
	}

}
