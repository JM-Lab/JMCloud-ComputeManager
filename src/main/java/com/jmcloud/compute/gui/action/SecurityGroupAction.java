package com.jmcloud.compute.gui.action;

import java.awt.event.ActionEvent;

import org.springframework.stereotype.Service;

import com.jmcloud.compute.gui.component.DialogsUtil;

@Service("securityGroupAction")
public class SecurityGroupAction extends AbstractJMCloudGUIAction {

	private String securityGroup;

	@Override
	protected String doAbstractAction(ActionEvent e) {
		if (!isSelectedOnlyGroup()) {
			return returnErrorMessage("Must Select Only One Group On The Tree View!!!");
		}
		securityGroup = computeManagerGUIModel.getSecurityGroup(
				regionOfselectionGroup, selectionGroup);
		switch (e.getActionCommand()) {
		case "Show Rules":
			return showRulesAction();
		case "Add Rule":
			return addRuleAction();
		case "Remove Rule":
			return removeRuleAction();
		default:
			return returnErrorMessage(FAILURE_SIGNATURE);
		}
	}

	private String showRulesAction() {
		startProgressSpinner();
		computeManagerGUIModel
				.showProgressResult("[Show Security Group Rules] "
						+ securityGroup);
		String securityGroupInfo = computeManagerGUIModel.getSecurityGroupInfo(
				regionOfselectionGroup, selectionGroup);
		if (!doProgressMethod(securityGroupInfo != null
				&& !"".equals(securityGroupInfo))) {
			return returnErrorMessage("There Aren't Any Security Rules!!!");
		}
		computeManagerGUIModel.showLineOnInfoView(securityGroupInfo);
		return securityGroup;
	}

	private String tcpOrUdpOrIcmp;
	private String portRange;
	private String cidrRange;

	private boolean showSecurityRuleInput() {
		String[] userInputs = DialogsUtil.showSecurityRuleInputDialog(
				mainFrame, actionCommand, regionOfselectionGroup,
				selectionGroup);
		if (userInputs.length == 0) {
			return false;
		}
		tcpOrUdpOrIcmp = userInputs[0];
		portRange = userInputs[1];
		cidrRange = userInputs[2];
		return true;
	}

	private String addRuleAction() {
		if (!showSecurityRuleInput()) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		startProgressSpinner();
		if (!computeManagerGUIModel.addSecurityRule(regionOfselectionGroup,
				selectionGroup, tcpOrUdpOrIcmp, portRange, cidrRange)) {
			return returnErrorMessage("Can't Create A Security Rule !!!");
		}
		return returnSuccessMessage(securityGroup);
	}

	private String removeRuleAction() {
		if (!showSecurityRuleInput()) {
			return returnErrorMessage(CANCEL_SIGNATURE);
		}
		startProgressSpinner();
		if (!computeManagerGUIModel.removeSecurityRule(regionOfselectionGroup,
				selectionGroup, tcpOrUdpOrIcmp, portRange, cidrRange)) {
			return returnErrorMessage("Can't Remove A Security Rule !!!");
		}
		return returnSuccessMessage(securityGroup);
	}

}
