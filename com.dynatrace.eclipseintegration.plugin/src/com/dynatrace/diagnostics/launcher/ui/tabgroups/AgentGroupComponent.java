package com.dynatrace.diagnostics.launcher.ui.tabgroups;

import org.eclipse.jface.layout.GridDataFactory;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.diagnostics.launcher.functionality.TestRunCategoryCombo;
import com.dynatrace.sdk.server.testautomation.models.TestCategory;

class AgentGroupComponent {

	Text agentName;
	Text profileText;
	Text agentAdditionalParams;
	Button chkEnableRecording;
	Combo comboTestType;

	void init(String name, boolean sessionRecord, String serverProfile, String params, String testRunCategory) {

		if (agentName != null) {
			agentName.setText(name);
		}
		if (agentAdditionalParams != null) {
			agentAdditionalParams.setText(params);
		}

		if (profileText != null) {
			profileText.setText(serverProfile);
		}
		if (chkEnableRecording != null) {
			chkEnableRecording.setSelection(sessionRecord);
		}

		comboTestType.setText(testRunCategory != null
				? testRunCategory
				: TestRunCategoryCombo.asComboLabel(TestRunCategoryCombo.getTestCategoryGlobalConfig()));
	}

	String getName() {
		return agentName.getText();
	}

	String getServerProfile() {
		return profileText.getText();
	}

	boolean isSessionRecord() {
		return chkEnableRecording.getSelection();
	}

	String getAdditionalParams() {
		return agentAdditionalParams.getText();
	}

	String getTestCategory() {
		return comboTestType.getText();
	}

	void createComponent(Composite parent) {
		Group dynatraceGroup = new Group(parent, SWT.NONE);
		dynatraceGroup.setText("Dynatrace");
		dynatraceGroup.setLayout(new GridLayout(2, false));
		dynatraceGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		GC gc = new GC(parent);
		int MAX_LABEL_WIDTH = gc.textExtent(StringResources.preferences_page2_session_record_label).x;
		gc.dispose();

		Label profileLabel = new Label(dynatraceGroup, SWT.NONE);
		profileLabel.setText(StringResources.preferences_page2_server_profile);
		profileLabel.setLayoutData(GridDataFactory.swtDefaults().hint(MAX_LABEL_WIDTH, -1).create());
		profileText = new Text(dynatraceGroup, SWT.BORDER);
		GridData gd_profileText = GridDataFactory.swtDefaults().grab(true, false).create();
		gd_profileText.horizontalAlignment = SWT.FILL;
		profileText.setLayoutData(gd_profileText);

		Label nameLabel = new Label(dynatraceGroup, SWT.NONE);
		nameLabel.setText(StringResources.preferences_page2_agent_name);
		agentName = new Text(dynatraceGroup, SWT.BORDER);
		agentName.setLayoutData(
				GridDataFactory.swtDefaults()
						.align(SWT.FILL, SWT.CENTER)
						.grab(true, false)
						.hint(MAX_LABEL_WIDTH, -1)
						.create());

		Label additionalParamsLabel = new Label(dynatraceGroup, SWT.NONE);
		additionalParamsLabel.setLayoutData(GridDataFactory.swtDefaults().hint(MAX_LABEL_WIDTH, -1).create());
		additionalParamsLabel.setText(StringResources.preferences_page2_agent_additional_params);
		agentAdditionalParams = new Text(dynatraceGroup, SWT.BORDER);
		agentAdditionalParams.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).create());

		Label enableRecordingLabel = new Label(dynatraceGroup, SWT.NONE);
		GridData gd_enableRecordingLabel = GridDataFactory.swtDefaults().hint(MAX_LABEL_WIDTH, -1).create();
		enableRecordingLabel.setLayoutData(gd_enableRecordingLabel);
		enableRecordingLabel.setText(StringResources.preferences_page2_session_record_label);
		chkEnableRecording = new Button(dynatraceGroup, SWT.CHECK);
		GridData gd_chkEnableRecording = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		chkEnableRecording.setLayoutData(gd_chkEnableRecording);

		Label testTypeLabel = new Label(dynatraceGroup, SWT.NONE);
		GridData gd_testTypeLabel = GridDataFactory.swtDefaults().hint(MAX_LABEL_WIDTH, -1).create();
		testTypeLabel.setLayoutData(gd_testTypeLabel);
		testTypeLabel.setText(StringResources.preferences_test_type_label);
		comboTestType = new Combo(dynatraceGroup, SWT.READ_ONLY | SWT.BORDER);
		comboTestType.setLayoutData(GridDataFactory.swtDefaults().grab(true, false).align(SWT.FILL, SWT.CENTER).create());
		comboTestType.setItems(new String[] {
				TestRunCategoryCombo.asComboLabel(TestCategory.UNIT),
				TestRunCategoryCombo.asComboLabel(TestCategory.PERFORMANCE)
		});
	}


	/**
	 * @deprecated Eclipse SWT designer entry point.
	 * @wbp.parser.entryPoint
	 */
	@Deprecated @SuppressWarnings("unused")
	private void mockParsingRoot() {
		init("name", true, "serverProfile", "noParams", TestRunCategoryCombo.asComboLabel(TestCategory.UNIT));
		Display display = new Display ();
		Shell frame = new Shell(display);
		frame.setLayout(new GridLayout(1, false));
		createComponent(frame);
	}
}
