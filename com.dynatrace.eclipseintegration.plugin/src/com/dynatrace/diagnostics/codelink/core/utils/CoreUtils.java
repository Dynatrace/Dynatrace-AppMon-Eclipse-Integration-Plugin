package com.dynatrace.diagnostics.codelink.core.utils;


import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.internal.core.JarPackageFragmentRoot;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.preference.IPreferenceStore;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;


@SuppressWarnings("restriction")
final class CoreUtils {
	private CoreUtils() {}
	
	/** Synchronizes files in Workspace of Projects in
	  * which Plugin is enabled
	  * 
	  * @param monitor
	  * @param depth
	  * @author markus.poechtrager
	 */
	public static void syncWorkspace(IProgressMonitor monitor, int depth, int includeMask) {
		monitor = monitor == null ? monitor = new NullProgressMonitor() : monitor;
		IJavaProject[] projects = SearchUtils.getParticipatingProjects();
		for (int i=0; i<projects.length; i++) {
			IJavaProject project = projects[i];
			if (!project.getProject().isSynchronized(depth)) {
				try {
					// get src folder and jar root nodes
					IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
					monitor.beginTask("Refreshing Workspace Resources", roots.length);
					for (int j=0; j<roots.length; j++) {
						IResource resource = roots[j].getUnderlyingResource();
						if (resource != null) {
							//TODO: get real instance types
							if (resource instanceof JarPackageFragmentRoot 
									&& (includeMask & IJavaSearchScope.APPLICATION_LIBRARIES) != 0) {
								continue;
							}
							else if (resource instanceof PackageFragmentRoot 
									&& (includeMask & IJavaSearchScope.SOURCES) != 0) {
								continue;
							}
//							else if (resource instanceof JarPackageFragmentRoot 
//									&& (includeMask & IJavaSearchScope.SYSTEM_LIBRARIES) != 0) {
//								continue;
//							}
							resource.refreshLocal(depth, monitor);
						}
					}
				}
				catch (CoreException e) {
					LogHelper.logError("Error synchronizing Workspace [ErrorLocation-92]", e);
				}
			}
		}
	}
	
	public static boolean isWorkspaceInSync(IProgressMonitor monitor, int depth) {
		monitor = monitor == null ? monitor = new NullProgressMonitor() : monitor;
		IJavaProject[] projects = SearchUtils.getParticipatingProjects();
		for (int i=0; i<projects.length; i++) {
			IJavaProject project = projects[i];
			if (!project.getProject().isSynchronized(depth)) {
				try {
					IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
					monitor.beginTask("Checking synchronization state of Workspace Resources", roots.length);
					for (int j=0; j<roots.length; j++) {
						IResource resource = roots[j].getUnderlyingResource();
						if (resource != null && resource.isSynchronized(depth)) {
							return false;
						}
					}
				}
				catch (CoreException e) {
					LogHelper.logError("Error checking synchronization state of Workspace [ErrorLocation-88]", e);
				}
			}
		}
		return true;
	}
	

	public static boolean isSyncWorkspaceSet() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(Constants.PREF_REFRESH_WORKSPACE);
	}
	

	/** Checks if open in Java Browsing View is set in preferences 
	  * 
	  * @return
	  * @author markus.poechtrager
	 */
	public static boolean isOpenJavaBrowsingSet() {
		IPreferenceStore store = Activator.getDefault().getPreferenceStore();
		return store.getBoolean(Constants.PREF_SWITCH_TO_JAVA_BROWSING);
	}
	
	public String getEclipseVersion() {
		return null;
	}
}
