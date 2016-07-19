package com.dynatrace.diagnostics.codelink.core.client;



import org.eclipse.jface.preference.IPreferenceStore;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.launcher.rest.RESTService;

/** This class provides live-cycle methods for the CodeLink client
  *
  * @author markus.poechtrager
  * @author michael.kumar
 */
public class CodeLinkClientLauncher {

	private Thread pollingThread;
	private CodeLinkClientProcess threadContent = null;
	private final Object startStopMonitor = new Object();

	public CodeLinkClientLauncher(RESTService restService) {
		threadContent = new CodeLinkClientProcess(restService);
	}


	public void start() {
		IPreferenceStore preferenceStore = Activator.getDefault().getPreferenceStore();
		boolean codeLinkEnabled = Constants.getDefaultBoolean(
				preferenceStore, Constants.PREF_ENABLE_CODELINK, Constants.DEFAULT_ENABLE_CODE_LINK);
		if (!codeLinkEnabled) {
			LogHelper.logInfo("CodeLink disabled, skipping initialization.");
			return;
		}

		synchronized (startStopMonitor) {
			if (pollingThread != null) {
				return;
			}

			threadContent.setKeepPolling(true);
			pollingThread = new Thread(threadContent, "DynaTrace CodeLink polling thread");
			pollingThread.setDaemon(true);
			pollingThread.start();
		}
	}

	public void stop() {
		synchronized (startStopMonitor) {
			if (pollingThread == null) {
				return;
			}

			try {
				threadContent.setKeepPolling(false);
				pollingThread.interrupt();
				pollingThread.join(1000);
				pollingThread = null;
			} catch (InterruptedException ignored) {
				LogHelper.logOK("Shutting downCodeLincClientLauncher interrupted " + ignored + "[ErrorLocation-80]");
			}
		}
	}
}
