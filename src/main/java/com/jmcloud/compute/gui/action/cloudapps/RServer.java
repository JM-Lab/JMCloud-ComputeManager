package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JToolBar;

import org.springframework.stereotype.Service;

@Service("rServer")
public class RServer extends AbstractCloudApp {
	private final String portRange = "8787";
	private final String title = "R Server";
	private final String luanchPackName = "RServer";

	@Override
	public String getPortRange() {
		return portRange;
	}

	@Override
	protected String getTitle() {
		return title;
	}

	@Override
	protected void addCloudAppActions(JToolBar cloudAppActiontoolBar) {
		Action connetRServerAction = new AbstractAction("Connect R Server") {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAppWithBrowser("http://" + publicIP + ":8787");
			}
		};
		cloudAppActiontoolBar.add(connetRServerAction);
		Action createAccountOnRServerAction = new AbstractAction(
				"Create Account") {
			@Override
			public void actionPerformed(ActionEvent e) {
				createAccount();
			}
		};
		cloudAppActiontoolBar.add(createAccountOnRServerAction);

	}

	@Override
	protected String getLuanchPackName() {
		return luanchPackName;
	}

	@Override
	protected void showNextSteps() {
		writeOutLog("Next Steps Are As Follows...");
		writeOut("1. confirm security rules of the Compute Group (Port 8787 from any IP)");
		writeOut("2. create a linux account, ex) sudo passwd ubuntu");
		writeOut("3. login R server with the account, ex) account = ubuntu");
		writeOut("4. run the following example on R Server.");
		writeOut("x<-rnorm(10)");
		writeOut("x");
		writeOut("mean(x)");
		writeOut("hist(x)");
	}

}
