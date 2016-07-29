package com.dynatrace.diagnostics.codelink;

import java.util.logging.Level;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;

import com.dynatrace.codelink.IDEDescriptor;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.launcher.ui.errorpopup.TransientErrorPopupManager;

public class EclipseDescriptor implements IDEDescriptor {
	public static final Version PLUGIN_VERSION_31 = new Version("3", "1", "0");
	private static final String LOG_FORMAT = "[%s](%s) - %s";

	@Override
	public int getId() {
		return IDEDescriptor.ECLIPSE_ID;
	}

	@Override
	public Version getPluginVersion() {
		return PLUGIN_VERSION_31;
	}

	@Override
	public String getVersion() {
		String ideVersionString = System.getProperty("osgi.framework.version");
		if (ideVersionString == null) {
			ideVersionString = (String) Platform.getBundle("org.eclipse.platform").getHeaders().get("Bundle-Version"); // eclipse
																														// tweak
		}
		return ideVersionString;
	}

	@Override
	public void log(Level level, String title, String subtitle, String content, boolean notification) {
		String logMessage = String.format(LOG_FORMAT, title, subtitle, content);
		if (!notification) {
			ILog logger = Activator.getDefault().getLog();
			IStatus status = new Status(levelToStatus(level), Activator.PLUGIN_ID, IStatus.OK, logMessage, null);
			logger.log(status);
		} else {
			TransientErrorPopupManager.logAndShowError(logMessage);
		}
	}

	private static int levelToStatus(Level level) {
		if (level == Level.SEVERE) {
			return IStatus.ERROR;
		}
		if (level == Level.WARNING) {
			return IStatus.WARNING;
		}
		if (level == Level.INFO) {
			return IStatus.INFO;
		}
		return IStatus.OK;
	}
}
