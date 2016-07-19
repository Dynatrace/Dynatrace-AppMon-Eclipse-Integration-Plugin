package com.dynatrace.diagnostics.codelink.core.client;

import java.util.NoSuchElementException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.preference.IPreferenceStore;

import com.dynatrace.diagnostics.codelink.core.utils.Jumper;
import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.codelink.ui.utils.UIUtils;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.launcher.rest.RESTService;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientConnectionFailedException;
import com.dynatrace.diagnostics.launcher.rest.exceptions.ClientHostUnknownException;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;
import com.dynatrace.diagnostics.server.service.data.CodeLinkLookupResponse;

/**
 * handles the connection to the HTTP push REST service
 * @author michael.kumar
 * @author Michal.Weyer
 */
@SuppressWarnings("serial")
class CodeLinkClientProcess implements Runnable {

	private final RESTService restService;
	private volatile boolean keepPolling = true;
	private final Jumper jumper = new Jumper();

	CodeLinkClientProcess(RESTService restService) {
		this.restService = restService;
	}

	void setKeepPolling(boolean keepPolling) {
		this.keepPolling = keepPolling;
	}



	@Override
	public void run() {

		long sessionId = -1;
		boolean prevConnectSuccessful = false;
		boolean suppressEvery10SecondsPopup = false;

		while( keepPolling) {
			try {
				CodeLinkLookupResponse codeLinkLookupResponse = singlePollingAttempt(sessionId);
				verifyClientVersionSupported(codeLinkLookupResponse);
				sessionId = codeLinkLookupResponse.sessionId;
				suppressEvery10SecondsPopup = false;
				if (!prevConnectSuccessful) {
					prevConnectSuccessful = true;
					LogHelper.logInfo("Dynatrace CodeLink connection successful, polling for jump request.");
				}

				if (codeLinkLookupResponse.timedOut) {
					continue;
				}

				try {
					boolean jumpSuccessful = jumpToCode(codeLinkLookupResponse);
					restService.sendCodeLinkResponse(sessionId, jumpSuccessful ? Protocol.RESPONSE_FOUND : Protocol.RESPONSE_NOT_FOUND);
				} catch (Exception e) {
					TransientErrorPopupManager.logAndShowError("Exception thrown while sending codeLink jump response to server [ErrorLocation-75]", e);
				}

			} catch (DelayReconnectException e) {
				try {
					if (!suppressEvery10SecondsPopup) {
						TransientErrorPopupManager.logAndShowError(
								"Dynatrace CodeLink connection failed, will retry every 10 seconds [ErrorLocation-76]", e);
						suppressEvery10SecondsPopup = true;
					}
					prevConnectSuccessful = false;
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e1) {
					LogHelper.logInfo("CodeLink lookup thread interrupted, stopping.");
					keepPolling = false;
					return;
				}
			} catch (RequiresReconfigurationException e) {
				// prevConnectSuccessful = false; -- not needed, since stopping thread
				keepPolling = false;
				IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
				prefStore.setValue(Constants.PREF_ENABLE_CODELINK, false);
				return;
			} catch (RuntimeException e) {
				try {
					prevConnectSuccessful = false;
					LogHelper.logError("Unexpected exception in CodeLink polling thread, will retry in 10 seconds. [ErrorLocation-77]", e);
					Thread.sleep(10 * 1000);
				} catch (InterruptedException e1) {
					LogHelper.logInfo("CodeLink lookup thread interrupted, stopping.");
					keepPolling = false;
					return;
				}
			}
		}
	}

	private boolean jumpToCode(CodeLinkLookupResponse pollForLookupRequestsResponse) {
		try {
			CallDescriptor callDescriptor = new CallDescriptor();
			callDescriptor.setClassName(pollForLookupRequestsResponse.className);
			callDescriptor.setMethodName(pollForLookupRequestsResponse.methodName);
			callDescriptor.setMethodArguments(pollForLookupRequestsResponse.arguments);
			if (Activator.getDefault().isDebug()) {
				LogHelper.logInfo("jumping to: class:" + callDescriptor.getClassName() + " method:" + callDescriptor.getMethodName());
			}
			jumper.jumpTo(callDescriptor);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}


	private CodeLinkLookupResponse singlePollingAttempt(long sessionId) throws DelayReconnectException, RequiresReconfigurationException {
		try {
			CodeLinkLookupResponse codeLinkLookupResponse = requestImpl(sessionId);
			if (codeLinkLookupResponse == null) {
				throw new DelayReconnectException("CodeLink poll request returned null [ErrorLocation-26]");
			}
			return codeLinkLookupResponse;

		} catch (ClientHostUnknownException e) {
			TransientErrorPopupManager.logAndShowError(
					"Dynatrace AppMon client host provided in codeLink configuration is unknown -- please reconfigure."
							+ " CodeLink client stopped until new configuration is provided. [ErrorLocation-78]", e);
			throw new RequiresReconfigurationException("[ErrorLocation-27]", e);
		} catch (ClientConnectionFailedException e) {
			LogHelper.logError("Failed connecting to Dynatrace AppMon Client to poll for CodeLink jump requests. [ErrorLocation-79]", e);
			throw new DelayReconnectException("[ErrorLocation-28]", e);
		}
	}

	private void verifyClientVersionSupported(CodeLinkLookupResponse codeLinkLookupResponse)
			throws RequiresReconfigurationException {
		if (!codeLinkLookupResponse.versionMatched) {
			TransientErrorPopupManager.logAndShowError(
					"Dynatrace Eclipse Plugin is too old to communicate with the found Dynatrace Client, CodeLink disabled.");
			throw new RequiresReconfigurationException("[ErrorLocation-29]");
		}
	}

	private CodeLinkLookupResponse requestImpl(long sessionId) throws ClientHostUnknownException, ClientConnectionFailedException {

		String ideVersionString = System.getProperty("osgi.framework.version");
		if(ideVersionString == null){
			ideVersionString = (String) Platform.getBundle("org.eclipse.platform").getHeaders().get("Bundle-Version"); // eclipse tweak
		}
		String project = "";
		String path = "";
		IProject activeProject = UIUtils.getActiveProject();
		if (activeProject != null) {
			project = activeProject.getName();
			path = activeProject.getProject().getLocation().toOSString();
		}
		if (Activator.getDefault().isDebug()) {
			LogHelper.logInfo("CodeLink client connecting to Dynatrace AppMon client... ");
		}
		return restService.codeLinkConnect(sessionId, ideVersionString, Protocol.PLUGIN_VERSION_31, project, path);
	}



	private final class RequiresReconfigurationException extends Exception {
		private RequiresReconfigurationException(String message) {
			super(message);
		}
		private RequiresReconfigurationException(String message, Throwable cause) {
			super(message, cause);
		}

	}

	private final class DelayReconnectException extends Exception {
		private DelayReconnectException(String message, Throwable cause) {
			super(cause);
		}
		private DelayReconnectException(String s) {
			super(s);
		}
	}
}
