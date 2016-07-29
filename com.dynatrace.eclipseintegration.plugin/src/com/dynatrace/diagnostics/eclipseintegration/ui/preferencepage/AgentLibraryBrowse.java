package com.dynatrace.diagnostics.eclipseintegration.ui.preferencepage;

import org.apache.commons.lang3.SystemUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.dynatrace.diagnostics.eclipseintegration.Constants;

/**
 * @author Michal.Weyer
 * @since 2016-04-06
 */
class AgentLibraryBrowse {

	private final Text fileInputText;
	private final Button fileBrowseBtn;

	AgentLibraryBrowse(Text fileInputText, Button fileBrowseBtn) {
		this.fileInputText = fileInputText;
		this.fileBrowseBtn = fileBrowseBtn;
	}

	void attachHandlers() {
		fileBrowseBtn.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browseForLibrary();
			}

			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				browseForLibrary();
			}
		});
	}

	private void browseForLibrary() {
		Shell parentShell = fileBrowseBtn.getShell();
		FileDialog fileDialog = new FileDialog(parentShell, SWT.OPEN);
		if (SystemUtils.IS_OS_WINDOWS) {
			fileDialog.setFilterExtensions(Constants.WINDOWS_EXTENSIONS);
		} else if (SystemUtils.IS_OS_LINUX || SystemUtils.IS_OS_SOLARIS || SystemUtils.IS_OS_AIX
				|| SystemUtils.IS_OS_HP_UX || SystemUtils.IS_OS_UNIX) {

			fileDialog.setFilterExtensions(Constants.LINUX_EXTENSIONS);
		}

		String selectedFilePath = fileDialog.open();
		if (selectedFilePath == null) {
			return;
		}
		fileInputText.setText(selectedFilePath);
	}
}
