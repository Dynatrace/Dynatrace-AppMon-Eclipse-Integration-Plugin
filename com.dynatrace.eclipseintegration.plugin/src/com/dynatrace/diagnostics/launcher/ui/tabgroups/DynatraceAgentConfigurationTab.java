package com.dynatrace.diagnostics.launcher.ui.tabgroups;

import static org.apache.commons.lang3.StringUtils.isBlank;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Link;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;
import com.dynatrace.diagnostics.launcher.ui.util.GotoUrlAdapter;

class DynatraceAgentConfigurationTab implements ILaunchConfigurationTab {

	private Control control;
	private AgentGroupComponent agentGroupComponent;
	private String errorMessage;
	private ILaunchConfigurationDialog dialog;

	@Override
	public void activated(ILaunchConfigurationWorkingCopy workingCopy) {
	}

	@Override
	public boolean canSave() {
		return isValid(null);
	}

	/**
	 * @wbp.parser.entryPoint
	 */
	@Override
	public void createControl(Composite parent) {
		Composite mainComposite = new Composite(parent, SWT.NONE);
		mainComposite.setLayout(new GridLayout(1, false));
		mainComposite.setLayoutData(new GridData(GridData.FILL_BOTH));
		mainComposite.setFont(parent.getFont());
		control = mainComposite;

		agentGroupComponent = new AgentGroupComponent();
		agentGroupComponent.createComponent(mainComposite);
		createDocumentationLinks(mainComposite);

		ModifyListener listener = new ModifyListener() {
			@Override
			public void modifyText(ModifyEvent e) {
				isValid(null);
				dialog.updateMessage();
				dialog.updateButtons();
			}
		};
		agentGroupComponent.agentName.addModifyListener(listener);
		agentGroupComponent.profileText.addModifyListener(listener);
		agentGroupComponent.agentAdditionalParams.addModifyListener(listener);
		agentGroupComponent.chkEnableRecording.addSelectionListener(new SelectionListener() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				isValid(null);
				dialog.updateMessage();
				dialog.updateButtons();
			}
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				isValid(null);
				dialog.updateMessage();
				dialog.updateButtons();
			}
		});
		agentGroupComponent.comboTestType.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				isValid(null);
				dialog.updateMessage();
				dialog.updateButtons();
			}

			@Override
			public void focusGained(FocusEvent e) {
				isValid(null);
				dialog.updateMessage();
				dialog.updateButtons();
			}
		});
	}


	@Override
	public void deactivated(ILaunchConfigurationWorkingCopy workingCopy) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public Control getControl() {
		return control;
	}

	@Override
	public String getErrorMessage() {
		return errorMessage;
	}

	@Override
	public Image getImage() {
		return Activator.getDefault().getImageRegistry().get(Constants.IMG_COMPANY_ICON);
	}

	@Override
	public String getMessage() {
		return StringResources.launchtab_message;
	}

	@Override
	public String getName() {
		return StringResources.launchtab_name;
	}

	@Override
	public void initializeFrom(ILaunchConfiguration configuration) {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		String defaultName = Constants.getDefaultString(prefStore, Constants.PREF_AGENT_NAME, Constants.DEFAULT_AGENT_NAME);
		String defaultParams = Constants.getDefaultString(prefStore, Constants.PREF_AGENT_PARAMS, "");

		try {
			String serverProfile = configuration.getAttribute(Constants.PREF_SERVER_PROFILE, Constants.DEFAULT_SERVER_PROFILE);
			String agentName = configuration.getAttribute(Constants.PREF_AGENT_NAME, defaultName);
			String params = configuration.getAttribute(Constants.PREF_AGENT_PARAMS, defaultParams);
			boolean sessionRecord = configuration.getAttribute(Constants.PREF_SESSION_RECORD, false);
			String testRunCategory = configuration.getAttribute(Constants.PREF_TEST_CATEGORY_PER_LAUNCH, Constants.DEFAULT_PREF_TEST_CATEGORY);

			agentGroupComponent.init(agentName, sessionRecord, serverProfile, params, testRunCategory);

		} catch (CoreException e) {
			LogHelper.logError("Could not read agent configuration from launch configuration! [ErrorLocation-56]", e);
		}
	}


	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		errorMessage = null;
		if (isBlank(agentGroupComponent.getServerProfile())) {
			errorMessage = "System profile name must not be empty!";
			return false;
		}

		if (isBlank(agentGroupComponent.getName())) {
			errorMessage = "Agent name must not be empty!";
			return false;
		}
		return true;
	}

	@Override
	public void launched(ILaunch launch) {
	}

	@Override
	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		try {
			configuration.setAttribute(Constants.PREF_AGENT_NAME, agentGroupComponent.getName());
			configuration.setAttribute(Constants.PREF_AGENT_PARAMS, agentGroupComponent.getAdditionalParams());
			configuration.setAttribute(Constants.PREF_SESSION_RECORD, agentGroupComponent.isSessionRecord());
			configuration.setAttribute(Constants.PREF_SERVER_PROFILE, agentGroupComponent.getServerProfile());
			configuration.setAttribute(Constants.PREF_TEST_CATEGORY_PER_LAUNCH, agentGroupComponent.getTestCategory());
			configuration.doSave();
		} catch (CoreException e) {
			TransientErrorPopupManager.logAndShowError("Error storing launch configuration [ErrorLocation-57]", e);
		}
	}

	@Override
	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		if (agentGroupComponent == null) {
			return;
		}

		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		String defaultName = Constants.getDefaultString(prefStore, Constants.PREF_AGENT_NAME, Constants.DEFAULT_AGENT_NAME);
		String defaultParams = Constants.getDefaultString(prefStore, Constants.PREF_AGENT_PARAMS, "");
		boolean sessionRecord = prefStore.getBoolean(Constants.PREF_SESSION_RECORD);
		String serverProfile = Constants.getDefaultString(prefStore, Constants.PREF_SERVER_PROFILE, Constants.DEFAULT_AGENT_NAME);
		String testRunCategory = Constants.getDefaultString(prefStore, Constants.PREF_TEST_CATEGORY_PER_LAUNCH, Constants.DEFAULT_PREF_TEST_CATEGORY);

		agentGroupComponent.init(defaultName, sessionRecord, serverProfile, defaultParams, testRunCategory);
	}

	@Override
	public void setLaunchConfigurationDialog(ILaunchConfigurationDialog dialog) {
		this.dialog = dialog;
	}


	private void createDocumentationLinks(Composite mainComposite) {
		Composite linksComposite = new Composite(mainComposite, SWT.NONE);
		linksComposite.setLayout(new FillLayout(SWT.VERTICAL));
		linksComposite.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.END, SWT.TOP).create());
		Link helpAndTroubleshootingLink = new Link(linksComposite, SWT.NONE);
		helpAndTroubleshootingLink.setText(StringResources.preferences_global_link_help);

		Link dynatraceLink = new Link(linksComposite, SWT.NONE);
		dynatraceLink.setText(StringResources.preferences_global_link_dynatrace);

		helpAndTroubleshootingLink.addSelectionListener(new GotoUrlAdapter(StringResources.preferences_global_link_help_url));
		dynatraceLink.addSelectionListener(new GotoUrlAdapter(StringResources.preferences_global_link_dynatrace_url));
	}
}
