package com.jmcloud.compute.gui.action.cloudapps;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppViewJDialog;

@Service("rServer")
public class RServer extends AbstractCloudApp {
	
	@Override
	protected void showCloudAppView() {
		CloudAppViewJDialog appDialog = new CloudAppViewJDialog(mainFrame,
				viewPanel, "R Server : " + publicIP);
		appDialog.setVisible(true);
	}

	@Override
	protected void showNextSteps() {
		writeOut(LUANCH_PROGRESS_INFO + "Next Steps Are As Follows...");
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
	protected void addManagementButten() {
		Action connetRServerAction = new AbstractAction("Connect R Server") {
			@Override
			public void actionPerformed(ActionEvent e) {
				connectAppWithBrowser("http://" + publicIP + ":8787");
			}
		};
		appManagementButtonToolBar.add(new JButton(connetRServerAction));
	}
}
