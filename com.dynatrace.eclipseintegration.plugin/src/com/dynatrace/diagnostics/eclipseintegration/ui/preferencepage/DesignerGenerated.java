package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.functionality.TestRunCategoryCombo;
import com.dynatrace.sdk.server.testautomation.models.TestCategory;

import swing2swt.layout.BorderLayout;

class DesignerGenerated extends Composite {

	final Label companyLogoImg;
	final Text serverText;
	final Text collectorAgentConnectionPortText;
	final Text serverRESTPortText;
	final Text loginText;
	final Text passwordText;
	final Text testRetrievalTimeoutText;
	final Combo testCategoryCombo;

	final Text agentLibraryText;
	final Text collectorHost;
	final Text clientPortText;

	final Button enableCodeLinkCheck;
	final Button switchToJavaBrowsingCheck;
	final Link helpAndTroubleshootingLink;
	final Link dynatraceLink;
	final Text clientHostText;
	final Button serverRESTViaSSLCheck;
	final Button clientViaSSLCheck;
	final Button agentLibraryBrowseBtn;

	DesignerGenerated(Composite parent) {
		super(parent, SWT.NONE);
		setLayout(new BorderLayout(0, 0));

		Composite logoHeaderComposite = new Composite(this, SWT.NONE);
		logoHeaderComposite.setLayoutData(BorderLayout.NORTH);
				GridLayout gl_logoHeaderComposite = new GridLayout(1, false);
				gl_logoHeaderComposite.marginBottom = 5;
				gl_logoHeaderComposite.verticalSpacing = 2;
				logoHeaderComposite.setLayout(gl_logoHeaderComposite);

				Composite whiteBackgroundExpandComposite = new Composite(logoHeaderComposite, SWT.NONE);
				whiteBackgroundExpandComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
				whiteBackgroundExpandComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
				GridLayout gl_whiteBackgroundExpandComposite = new GridLayout(1, false);
				gl_whiteBackgroundExpandComposite.horizontalSpacing = 0;
				gl_whiteBackgroundExpandComposite.verticalSpacing = 0;
				gl_whiteBackgroundExpandComposite.marginWidth = 0;
				gl_whiteBackgroundExpandComposite.marginHeight = 0;
				whiteBackgroundExpandComposite.setLayout(gl_whiteBackgroundExpandComposite);

		companyLogoImg = new Label(whiteBackgroundExpandComposite, SWT.NONE);
		companyLogoImg.setLayoutData(new GridData(SWT.LEFT, SWT.FILL, false, false, 1, 1));
		companyLogoImg.setAlignment(SWT.CENTER);
		companyLogoImg.setBackground(SWTResourceManager.getColor(SWT.COLOR_WHITE));
		companyLogoImg.setImage(Activator.getDefault().getImageRegistry().get(Constants.IMG_COMPANY_LOGO));

		Label sloganLabel = new Label(logoHeaderComposite, SWT.NONE);
		sloganLabel.setText(StringResources.preferences_global_slogan_build_optimize_test);

		Composite bottomLinksRow = new Composite(this, SWT.NONE);
		bottomLinksRow.setLayoutData(BorderLayout.SOUTH);
		bottomLinksRow.setLayout(new FormLayout());

		Composite pushRightComposite = new Composite(bottomLinksRow, SWT.NONE);
		pushRightComposite.setLayout(new GridLayout(1, false));
		FormData fd_pushRightComposite = new FormData();
		fd_pushRightComposite.bottom = new FormAttachment(100);
		fd_pushRightComposite.right = new FormAttachment(100);
		pushRightComposite.setLayoutData(fd_pushRightComposite);

		helpAndTroubleshootingLink =  new Link(pushRightComposite, SWT.NONE);
		helpAndTroubleshootingLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		helpAndTroubleshootingLink.setBounds(0, 0, 49, 15);
		helpAndTroubleshootingLink.setText(StringResources.preferences_global_link_help);

		dynatraceLink = new Link(pushRightComposite, SWT.NONE);
		dynatraceLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		dynatraceLink.setBounds(0, 0, 49, 15);
		dynatraceLink.setText(StringResources.preferences_global_link_dynatrace);

		Composite centralComposite = new Composite(this, SWT.NONE);
		centralComposite.setLayoutData(BorderLayout.CENTER);
		centralComposite.setLayout(new GridLayout(1, false));

			Group grpServer = new Group(centralComposite, SWT.NONE);
			grpServer.setText(StringResources.preferences_global_serverGroup);
			GridLayout gl_grpServer = new GridLayout(2, false);
			gl_grpServer.marginBottom = 5;
			gl_grpServer.marginHeight = 10;
			grpServer.setLayout(gl_grpServer);
			grpServer.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Label lblServer = new Label(grpServer, SWT.RIGHT);
				GridData gd_lblServer = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblServer.setLayoutData(gd_lblServer);
				lblServer.setText(StringResources.preferences_global_server_label);

				serverText = new Text(grpServer, SWT.BORDER);
				serverText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Label lblServerRESTPort = new Label(grpServer, SWT.RIGHT);
				GridData gd_lblServerRESTPort = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblServerRESTPort.setLayoutData(gd_lblServerRESTPort);
				lblServerRESTPort.setText(StringResources.preferences_global_server_REST_port_label);
				serverRESTPortText = new Text(grpServer, SWT.BORDER);
				serverRESTPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Label lblServerViaSSH = new Label(grpServer, SWT.NONE);
				lblServerViaSSH.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblServerViaSSH.setText(StringResources.preferences_global_server_via_ssl_label);
				serverRESTViaSSLCheck = new Button(grpServer, SWT.CHECK);
				serverRESTViaSSLCheck.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});

				Label lblLogin = new Label(grpServer, SWT.RIGHT);
				GridData gd_lblLogin = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblLogin.setLayoutData(gd_lblLogin);
				lblLogin.setText(StringResources.preferences_global_login_label);

				loginText = new Text(grpServer, SWT.BORDER);
				loginText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Label lblPassword = new Label(grpServer, SWT.RIGHT);
				GridData gd_lblPassword = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblPassword.minimumWidth = 148;
				lblPassword.setLayoutData(gd_lblPassword);
				lblPassword.setText(StringResources.preferences_global_password_label);

				passwordText = new Text(grpServer, SWT.BORDER | SWT.PASSWORD);
				passwordText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Label lblRetrievalTimeout = new Label(grpServer, SWT.RIGHT);
				GridData gd_lblRetrievalTimeout = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblRetrievalTimeout.minimumWidth = 148;
				lblRetrievalTimeout.setLayoutData(gd_lblRetrievalTimeout);
				lblRetrievalTimeout.setText(StringResources.preferences_global_test_timeout);

				testRetrievalTimeoutText = new Text(grpServer, SWT.BORDER);
				testRetrievalTimeoutText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));

				Label lblTestCategory = new Label(grpServer, SWT.RIGHT);
				GridData gd_lblTestCategory = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblTestCategory.minimumWidth = 148;
				lblTestCategory.setLayoutData(gd_lblTestCategory);
				lblTestCategory.setText(StringResources.preferences_global_test_category);

				testCategoryCombo = new Combo(grpServer, SWT.READ_ONLY | SWT.BORDER);
				testCategoryCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				testCategoryCombo.add(TestRunCategoryCombo.asComboLabel(TestCategory.UNIT));
				testCategoryCombo.add(TestRunCategoryCombo.asComboLabel(TestCategory.PERFORMANCE));

			Group grpClient = new Group(centralComposite, SWT.NONE);
			GridLayout gl_grpClient = new GridLayout(3, false);
			gl_grpClient.marginBottom = 5;
			grpClient.setLayout(gl_grpClient);
			grpClient.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			grpClient.setText(StringResources.preferences_global_agentGroup);

				Label lblNewLabel = new Label(grpClient, SWT.RIGHT);
				GridData gd_lblNewLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblNewLabel.minimumWidth = 148;
				lblNewLabel.setLayoutData(gd_lblNewLabel);
				lblNewLabel.setBounds(0, 0, 55, 15);
				lblNewLabel.setText(StringResources.preferences_global_agentLibraryLabel);

				agentLibraryText = new Text(grpClient, SWT.BORDER);
				agentLibraryText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
				agentLibraryBrowseBtn = new Button(grpClient, SWT.NONE);
				agentLibraryBrowseBtn.setText(StringResources.preferences_global_agentLibraryBrowseLabel);

				Label lblCollectorServerHost = new Label(grpClient, SWT.RIGHT);
				GridData gd_lblCollectorServerHost = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblCollectorServerHost.setLayoutData(gd_lblCollectorServerHost);
				lblCollectorServerHost.setText(StringResources.preferences_global_collector_host_label);
				collectorHost = new Text(grpClient, SWT.BORDER);
				collectorHost.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

				Label lblCollectorServerPort = new Label(grpClient, SWT.RIGHT);
				GridData gd_lblCollectorServerPort = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblCollectorServerPort.setLayoutData(gd_lblCollectorServerPort);
				lblCollectorServerPort.setText(StringResources.preferences_global_collector_agent_port_label);
				collectorAgentConnectionPortText = new Text(grpClient, SWT.BORDER);
				collectorAgentConnectionPortText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 2, 1));

			Group grpCodelink = new Group(centralComposite, SWT.NONE);
			GridLayout gl_grpCodelink = new GridLayout(2, false);
			gl_grpCodelink.marginTop = 5;
			gl_grpCodelink.marginBottom = 5;
			grpCodelink.setLayout(gl_grpCodelink);
			grpCodelink.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
			grpCodelink.setText(StringResources.preferences_global_CodeLinkGroup);

				Label lblEnableCodeLink = new Label(grpCodelink, SWT.NONE);
				lblEnableCodeLink.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblEnableCodeLink.setText(StringResources.preferences_global_enable_code_link);

				enableCodeLinkCheck = new Button(grpCodelink, SWT.CHECK);
				enableCodeLinkCheck.addSelectionListener(new SelectionAdapter() {
					@Override
					public void widgetSelected(SelectionEvent e) {
					}
				});

				Label lblClientHost = new Label(grpCodelink, SWT.NONE);
				GridData gd_lblClientHost = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				gd_lblClientHost.verticalIndent = 0;
				lblClientHost.setLayoutData(gd_lblClientHost);
				lblClientHost.setText(StringResources.preferences_global_client_host);

				clientHostText = new Text(grpCodelink, SWT.BORDER);
				GridData gd_clientHostText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd_clientHostText.verticalIndent = 0;
				clientHostText.setLayoutData(gd_clientHostText);

				Label lblClientRestPort = new Label(grpCodelink, SWT.RIGHT);
				GridData gd_lblClientRestPort = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblClientRestPort.setLayoutData(gd_lblClientRestPort);
				lblClientRestPort.setText(StringResources.preferences_global_client_port);

				clientPortText = new Text(grpCodelink, SWT.BORDER);
				clientPortText.setText("9998");
				GridData gd_clientRESTPortText = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
				gd_clientRESTPortText.widthHint = 56;
				clientPortText.setLayoutData(gd_clientRESTPortText);

				Label lblClientConnectSSL = new Label(grpCodelink, SWT.NONE);
				lblClientConnectSSL.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
				lblClientConnectSSL.setAlignment(SWT.RIGHT);
				lblClientConnectSSL.setText(StringResources.preferences_global_client_via_ssl_label);

				clientViaSSLCheck = new Button(grpCodelink, SWT.CHECK);

				Label lblUseJavaBrowsing = new Label(grpCodelink, SWT.WRAP);
				lblUseJavaBrowsing.setAlignment(SWT.RIGHT);
				GridData layoutData = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
				lblUseJavaBrowsing.setLayoutData(layoutData);
				lblUseJavaBrowsing.setText(StringResources.preferences_global_switch_to_Java_Browsing_Perspective);
				switchToJavaBrowsingCheck = new Button(grpCodelink, SWT.CHECK);

				GC gc = new GC(lblRetrievalTimeout);
				int longestLabelWidth = gc.textExtent(StringResources.preferences_global_test_timeout).x;

				gd_lblServer.widthHint = longestLabelWidth;
				gd_lblServerRESTPort.widthHint = longestLabelWidth;
				gd_lblLogin.widthHint = longestLabelWidth;
				gd_lblPassword.widthHint = longestLabelWidth;
				gd_lblRetrievalTimeout.widthHint = longestLabelWidth;
				gd_lblTestCategory.widthHint = longestLabelWidth;
				gd_lblNewLabel.widthHint = longestLabelWidth;
				gd_lblCollectorServerHost.widthHint = longestLabelWidth;
				gd_lblCollectorServerPort.widthHint = longestLabelWidth;
				gd_lblClientRestPort.widthHint = longestLabelWidth;
				layoutData.widthHint = longestLabelWidth;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
