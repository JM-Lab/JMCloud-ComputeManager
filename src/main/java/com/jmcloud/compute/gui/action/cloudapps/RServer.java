package com.jmcloud.compute.gui.action.cloudapps;

import javax.swing.SwingUtilities;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.action.cloudapps.views.CloudAppViewJDialog;

@Service("rServer")
public class RServer extends AbstractCloudApp{

	@Override
	protected void showCloudAppView() {
		CloudAppViewJDialog appDialog = new CloudAppViewJDialog(mainFrame, viewPanel, "R Server Installation");
		appDialog.setVisible(true);
	}
	
	@Override
	protected void showNextSteps() {
		writeOut(LUANCH_PROGRESS_INFO + "Next Steps Are As Follows...");
		writeOut("1. create a linux account, ex) sudo passwd ubuntu");
		writeOut("2. login R server with the account, ex) account = ubuntu");
		writeOut("3. run the following example on R Server.");
		writeOut("x<-rnorm(10)");
		writeOut("x");
		writeOut("mean(x)");
		writeOut("hist(x)");
	}
}
