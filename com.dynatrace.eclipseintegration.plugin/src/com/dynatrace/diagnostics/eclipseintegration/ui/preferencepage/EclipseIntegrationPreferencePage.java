package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_CLIENT_PORT_NO_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_CLIENT_PORT_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_REST_PORT_NO_SSL;
import static com.dynatrace.diagnostics.eclipseintegration.Constants.DEFAULT_SERVER_REST_PORT_SSL;
import static java.util.Arrays.asList;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;

import com.dynatrace.diagnostics.eclipseintegration.Activator;


/**
 * @author michael.kumar
 * @author Michal.Weyer
 */
public class EclipseIntegrationPreferencePage extends PreferencePage implements IWorkbenchPreferencePage {

	private DesignerGenerated view;
	private Configuration configuration;
	private Validation validation;

	@Override
	public void init(IWorkbench workbench) {
		// does nothing, data loaded in createContents
	}

	@Override
	public void createControl(Composite parent) {
		super.createControl(parent);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.minimumWidth = 600;
		gridData.minimumHeight = 520;
		view.setLayoutData(gridData);
	}

	protected Control createContents(Composite parent) {
		this.view = new DesignerGenerated(parent);
		this.configuration = new Configuration(view);
		this.validation = new Validation(this, view);
		validation.attachListeners();

		new Links(view).initLinks();
		configuration.loadConfigurationIntoFields();

		new DebugOnLogoDblClick(view).debugOnLogoDblClick();
		new TimeoutDigitsOnly(view.testRetrievalTimeoutText).attachHandlers();
		new PortDigitsOnly(view.serverRESTPortText).attachHandlers();
		new PortSSLOrNotDefaultsOnCheckbox(view.serverRESTViaSSLCheck, view.serverRESTPortText,
				DEFAULT_SERVER_REST_PORT_SSL, DEFAULT_SERVER_REST_PORT_NO_SSL)
				.attachHandlers();

		new AgentLibraryBrowse(view.agentLibraryText, view.agentLibraryBrowseBtn).attachHandlers();
		new PortDigitsOnly(view.collectorAgentConnectionPortText).attachHandlers();

		new EnableDisableSection(view.enableCodeLinkCheck, asList(view.clientHostText, view.clientPortText, view.clientViaSSLCheck, view.switchToJavaBrowsingCheck))
				.attachHandlers()
				.updateVisibility();
		new PortDigitsOnly(view.clientPortText).attachHandlers();
		new PortSSLOrNotDefaultsOnCheckbox(view.clientViaSSLCheck, view.clientPortText,
				DEFAULT_CLIENT_PORT_SSL, DEFAULT_CLIENT_PORT_NO_SSL)
				.attachHandlers();

		return view;
	}



	@Override
	public boolean isValid() {
		return validation.isValid();
	}

	@Override
	protected void performDefaults() {
		configuration.performDefaults();
		super.performDefaults();
	}

	@Override
	protected void performApply() {
		if (!validation.applyPressed()) {
			return;
		}
		super.performApply();
	}

	@Override
	public boolean performOk() {
		if (!validation.okPressed()) {
			return false;
		}
		configuration.saveConfiguration();

//		try {
//			Activator activator = Activator.getDefault();
//			activator.stopCodeLink();
//			activator.startCodeLink();
//		} catch (Exception e) {
//			throw new RuntimeException("Exception when restarting CodeLink background thread [ErrorLocation-30]", e);
//		}

		return super.performOk();
	}
}
