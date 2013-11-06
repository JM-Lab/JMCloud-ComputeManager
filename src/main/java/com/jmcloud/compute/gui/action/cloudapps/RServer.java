package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppViewJDialog;
import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppViewPanel;

@Service("rServer")
public class RServer extends AbstractCloudApp {

	private String luanchPackName = "RServer";
	private String portRange = "8787";

	@Override
	public void showCloudAppManagerView() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				viewPanel = new CloudAppViewPanel();
				new CloudAppViewJDialog(mainFrame,
						viewPanel, "R Server : " + publicIP);
			}
		}).start();		
	}

	@Override
	protected void showNextSteps() {
		writeOutInfo("Next Steps Are As Follows...");
		writeOut("1. confirm security rules of the Compute Group (Port 8787 from any IP)");
		writeOut("2. create a linux account, ex) sudo passwd ubuntu");
		writeOut("3. login R server with the account, ex) account = ubuntu");
		writeOut("4. run the following example on R Server.");
		writeOut("x<-rnorm(10)");
		writeOut("x");
		writeOut("mean(x)");
		writeOut("hist(x)");
	}

	@Override
	protected void addAppActions() {
		Action connetRServerAction = new AbstractAction("Connect R Server") {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAppWithBrowser("http://" + publicIP + ":8787");
			}
		};
		appManagementButtonToolBar.add(connetRServerAction);
	}

	@Override
	protected String getLuanchPackName() {
		return luanchPackName;
	}

	@Override
	protected boolean setSecurityRules() {
	return false;
	}

	@Override
	public String getPortRange() {
		return portRange;
	}

}
