package com.dynatrace.diagnostics.codelink.logging;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import com.dynatrace.diagnostics.eclipseintegration.Activator;

/** Helper to simplify logging
  *
  * @author markus.poechtrager
 */
public class LogHelper {
	public static void logError(String errorMsg, Exception e) {
		if (Activator.getDefault() != null) {
			ILog logger = Activator.getDefault().getLog();
			IStatus status = new Status(
					IStatus.ERROR, Activator.PLUGIN_ID,
					IStatus.OK,	errorMsg, e);
			logger.log(status);
		}
	}

	public static void logError(String errorMsg) {
		if (Activator.getDefault() != null) {
			ILog logger = Activator.getDefault().getLog();
			IStatus status = new Status(
					IStatus.ERROR, Activator.PLUGIN_ID,
					IStatus.OK,	errorMsg, null);
			logger.log(status);
		}
	}

	public static void logWarning(String errorMsg) {
		if (Activator.getDefault() == null) {
			return;
		}

		ILog logger = Activator.getDefault().getLog();
		IStatus status = new Status(
				IStatus.WARNING, Activator.PLUGIN_ID,
				IStatus.OK,	errorMsg, null);
		logger.log(status);
	}

	public static void logInfo(String infoMsg) {
		if (Activator.getDefault() == null) {
			return;
		}

		ILog logger = Activator.getDefault().getLog();
		IStatus status = new Status(
				IStatus.INFO, Activator.PLUGIN_ID,
				IStatus.OK,	infoMsg, null);
		logger.log(status);
	}

	public static void logOK(String msg) {
		if (Activator.getDefault() == null) {
			return;
		}

		ILog logger = Activator.getDefault().getLog();
		IStatus status = new Status(
				IStatus.OK, Activator.PLUGIN_ID,
				IStatus.OK,	msg, null);
		logger.log(status);
	}

	public static IStatus createInfoStatus(String infoMsg) {
		IStatus status = new Status(
				IStatus.INFO, Activator.PLUGIN_ID,
				IStatus.OK,	infoMsg, null);
		return status;
	}

	public static IStatus createErrorStatus(String errorMsg, Exception e) {
		IStatus status = new Status(
				IStatus.ERROR, Activator.PLUGIN_ID,
				IStatus.OK,	errorMsg, e);
		return status;
	}
}
