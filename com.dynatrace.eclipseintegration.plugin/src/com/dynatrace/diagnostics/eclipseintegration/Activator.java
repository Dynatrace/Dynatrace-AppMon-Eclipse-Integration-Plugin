package com.dynatrace.diagnostics.eclipseintegration;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.dynatrace.codelink.CodeLinkClient;
import com.dynatrace.diagnostics.codelink.EclipseCodeLinkSettings;
import com.dynatrace.diagnostics.codelink.EclipseDescriptor;
import com.dynatrace.diagnostics.codelink.EclipseProjectDescriptor;
import com.dynatrace.diagnostics.launcher.LauncherCallbacks;
import com.dynatrace.sdk.server.DynatraceClient;

/**
 * Responsible for:
 * <ul>
 * <li>plugin lifecycle</li>
 * <li>gluing functionality to Eclipse callbacks</li>
 * <li>gluing functionality between submodules</li>
 * </ul>
 *
 * @author Michal.Weyer (refactoring after original author)
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.dynatrace.eclipseintegration.plugin"; //$NON-NLS-1$
	private static Activator singleton;

	private CodeLinkClient codeLinkClient;
	private LauncherCallbacks launcherCallbacks;

	private boolean debug = false;

	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		DynatraceClient client = new DynatraceClient(new EclipseServerConfiguration());
		codeLinkClient = new CodeLinkClient(new EclipseCodeLinkSettings(), new EclipseDescriptor(),
				new EclipseProjectDescriptor());
		launcherCallbacks = new LauncherCallbacks(client);

		initPrefStore();
		launcherCallbacks.attach();
		singleton = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (codeLinkClient != null) {
			stopCodeLink();
		}

		launcherCallbacks.detach();
		singleton = null;

		codeLinkClient = null;
		launcherCallbacks = null;

		super.stop(context);
	}

	/**
	 * Returns the shared instance.
	 */
	public static Activator getDefault() {
		return singleton;
	}

	public void startCodeLink() {
		codeLinkClient.startPolling(CodeLinkClient.DEFAULT_INTERVAL, CodeLinkClient.DEFAULT_UNIT);
	}

	public void stopCodeLink() {
		codeLinkClient.stopPolling();
	}

	public LauncherCallbacks getLauncherCallbacks() {
		return launcherCallbacks;
	}

	public void setDebug(boolean debug) {
		this.debug = debug;
	}

	public boolean isDebug() {
		return this.debug;
	}

	@Override
	protected void initializeImageRegistry(ImageRegistry reg) {
		reg.put(Constants.IMG_AGENT, getImageDescriptor("/img/agenticon.ico")); //$NON-NLS-1$
		reg.put(Constants.IMG_COMPANY_LOGO, getImageDescriptor("/img/companylogo.png")); //$NON-NLS-1$
		reg.put(Constants.IMG_COMPANY_ICON, getImageDescriptor("/icons/dynatrace_run.png")); //$NON-NLS-1$
		reg.put(Constants.IMG_TEST_FAILED, getImageDescriptor("/img/failing_ico.png")); //$NON-NLS-1$
		reg.put(Constants.IMG_TEST_OK, getImageDescriptor("/img/success_ico.png")); //$NON-NLS-1$
		reg.put(Constants.IMG_TEST_VOLATILE, getImageDescriptor("/img/volatile_ico.png")); //$NON-NLS-1$
		reg.put(Constants.IMG_POPUP_ERROR, getImageDescriptor("/icons/crossed_logo_light.png"));
		reg.put(Constants.IMG_POPUP_CLOSE, getImageDescriptor("/icons/small-cross.png"));
		reg.put(Constants.IMG_WHERE_ICON, getImageDescriptor("/img/run_with_appmon_configuration_point.png"));
	}

	private void initPrefStore() {
		IPreferenceStore prefStore = getPreferenceStore();
		prefStore.setDefault(Constants.PROP_PROJECT_DEFAULT_MARKER_VALUE, true);

		boolean isInit = prefStore.getBoolean(Constants.IS_PREFSTORE_INITIALIZED);

		if (!isInit) {
			prefStore.setValue(Constants.PREF_SWITCH_TO_JAVA_BROWSING, false);
			prefStore.setValue(Constants.PREF_ENABLE_CODELINK, true);
			prefStore.setValue(Constants.IS_PREFSTORE_INITIALIZED, true);
		}
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path
	 */
	private static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
