package com.dynatrace.diagnostics.codelink.ui.propertypage;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.dialogs.PropertyPage;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;

/**
  * 
  * @author markus.poechtrager
 */
public class DTDPropertyPage extends PropertyPage {

	private Button check;


	public DTDPropertyPage() {
		super();
	}

	/**
	 * @see PreferencePage#createContents(Composite)
	 */
	protected Control createContents(Composite parent) {
		
		Composite main = new Composite(parent, SWT.NONE);
		GridLayout gl = new GridLayout(2, false);
		main.setLayout(gl);

		check = new Button(main, SWT.CHECK);
		check.setText(StringResources.javaproject_properties_page1_enableChk);
		
		// init with stored data
		// should be placed in a hook method that gets called when page is about to get displayed
		init();

		return main;
	}
	

	private void init() {
		try {
			IAdaptable elem = getElement();
			IProject p = (IProject) elem.getAdapter(IProject.class);
			// get value from persistent property stored per project in workspace
			String marker = p.getPersistentProperty(Constants.PROP_PROJECT_MARKER);
			
			if (marker != null) {
				boolean isEnabled = Boolean.valueOf(marker).booleanValue();
				check.setSelection(isEnabled);
			}
			else {
				performDefaults();
			}
		}
		catch (Exception e) {
			LogHelper.logError("Error initializing DTDPropertyPage, performingDefaults() [ErrorLocation-84]");
			performDefaults();
		}
	}
	

	protected void performDefaults() {
		check.setSelection(Activator.getDefault().getPreferenceStore()
				.getDefaultBoolean(Constants.PROP_PROJECT_DEFAULT_MARKER_VALUE));
	}
	

	public boolean performOk() {

		try {
			IAdaptable elem = getElement();
			IProject project = (IProject) elem.getAdapter(IProject.class);

			// store setting per project
			project.setPersistentProperty(Constants.PROP_PROJECT_MARKER, Boolean
					.toString(check.getSelection()));
		}
		catch (Exception e) {
			LogHelper.logError("Error on DTDPropertyPage.performOK [ErrorLocation-85]", e);
			return false;
		}
		return true;
	}

}