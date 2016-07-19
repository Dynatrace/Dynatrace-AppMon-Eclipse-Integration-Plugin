package com.dynatrace.diagnostics.eclipseintegration;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import com.dynatrace.diagnostics.codelink.core.client.CodeLinkClientLauncher;
import com.dynatrace.diagnostics.launcher.LauncherCallbacks;
import com.dynatrace.diagnostics.launcher.rest.RESTService;

/**
 * Responsible for: <ul>
 *     <li>plugin lifecycle</li>
 *     <li>gluing functionality to Eclipse callbacks</li>
 *     <li>gluing functionality between submodules</li>
 * </ul>
 *
 * @author Michal.Weyer (refactoring after original author)
 */
public class Activator extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "com.dynatrace.eclipseintegration.plugin"; //$NON-NLS-1$
	private static Activator singleton;

	private CodeLinkClientLauncher codeLinkLauncher;
	private LauncherCallbacks launcherCallbacks;

	private boolean debug = false;


	public Activator() {
	}

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);

		RESTService restService = new RESTService();
		codeLinkLauncher = new CodeLinkClientLauncher(restService);
		launcherCallbacks = new LauncherCallbacks(restService);

		initPrefStore();
		launcherCallbacks.attach();
		singleton = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		if (codeLinkLauncher != null) {
			codeLinkLauncher.stop();
		}

		launcherCallbacks.detach();
		singleton = null;

		codeLinkLauncher = null;
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
		codeLinkLauncher.start();
	}

	public void stopCodeLink() {
		codeLinkLauncher.stop();
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
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path
	 */
	private static ImageDescriptor getImageDescriptor(String path) {
		return imageDescriptorFromPlugin(PLUGIN_ID, path);
	}
}
