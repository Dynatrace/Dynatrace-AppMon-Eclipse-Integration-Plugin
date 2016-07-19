package com.dynatrace.diagnostics.launcher.ui.actions;

import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.internal.ui.DebugUIPlugin;
import org.eclipse.debug.internal.ui.launchConfigurations.OrganizeFavoritesAction;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.actions.AbstractLaunchHistoryAction;
import org.eclipse.debug.ui.actions.LaunchShortcutsAction;
import org.eclipse.debug.ui.actions.OpenLaunchDialogAction;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;

import com.dynatrace.diagnostics.eclipseintegration.Constants;

/**
 * This is the dropdown toolbar button which also contains the run history.
 * @see IWorkbenchWindowActionDelegate
 */
@SuppressWarnings("restriction")
public class DynatraceLaunchToolbarAction extends AbstractLaunchHistoryAction {
	/**
	 * The constructor.
	 */
	public DynatraceLaunchToolbarAction() {
		super(Constants.DT_LAUNCH_GROUP);
	}

	protected void fillMenu(Menu menu) {
		super.fillMenu(menu);
		// Separator between history and common actions
		if (menu.getItemCount() > 0) {
			addSeparator(menu);
		}
		addToMenu(menu, new LaunchShortcutsAction(getLaunchGroupIdentifier()), -1);
		addToMenu(menu, getOpenDialogAction(), -1);
		addToMenu(menu, new OrganizeFavoritesAction(getLaunchGroupIdentifier()), -1);
	}

	private IAction getOpenDialogAction() {
		return new OpenLaunchDialogAction(getLaunchGroupIdentifier());
	}


	/**
	 * Launch the last launch, or open the launch config dialog if none.
	 * 
	 * @see org.eclipse.ui.IActionDelegate#run(org.eclipse.jface.action.IAction)
	 */
	public void run(IAction action) {
		ILaunchConfiguration configuration = getLastLaunch();
		if (configuration == null) {
			DebugUITools.openLaunchConfigurationDialogOnGroup(DebugUIPlugin.getShell(), new StructuredSelection(), getLaunchGroupIdentifier());
		} else {
			DebugUITools.launch(configuration, getMode());
		}
	}	
}