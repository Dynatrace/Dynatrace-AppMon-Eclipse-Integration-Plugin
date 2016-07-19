package com.dynatrace.diagnostics.codelink.core.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Constants;

final class SearchUtils {

	private SearchUtils() {}

	/**
	 * @return IJavaProject[] the plugin is enabled for
	 * @author markus.poechtrager
	 */
	public static IJavaProject[] getParticipatingProjects() {
		IWorkspace ws = ResourcesPlugin.getWorkspace();
		List<IJavaProject> javaProjectList = new ArrayList<IJavaProject>();
		IProject[] projects = ws.getRoot().getProjects();

		for (int i = 0; i < projects.length; i++) {
			IProject project = projects[i];
			if (project.isOpen()) {
				try {
					String value = project.getPersistentProperty(Constants.PROP_PROJECT_MARKER);

					// property not set yet, set by default to true
					if (value == null) {
						project.setPersistentProperty(Constants.PROP_PROJECT_MARKER, Boolean.TRUE.toString());
					}
					else if (value.equals(Boolean.TRUE.toString())) {
						IJavaProject javaProject = JavaCore.create(project);
						javaProjectList.add(javaProject);
					}
				}
				catch (CoreException e) {
					LogHelper.logError("Error getting participating Projects [ErrorLocation-83]", e);
				}
			}
		}

		return (IJavaProject[])javaProjectList.toArray(new IJavaProject[javaProjectList.size()]);
	}


	/**
	  * Constructs argument string someMethod"(...)"
	  * @param args
	  * @return
	  * @author markus.poechtrager
	 */
	public static String constructArgString(String[] args) {
		String argString = "";
		if (args == null || args.length == 0)
			return argString;

		argString += "(";

		argString += preProcessParam(args[0]);

		for (int i=1; i<args.length; i++) {
			argString += ", " + preProcessParam(args[i]);
		}

		argString += ")";
		return argString;
	}



	private static String preProcessParam(String param) {
		if (param.equals("array")) { //$NON-NLS-1$
			return "*";
		}
		return param;
	}


	/** Get search target (source file, app libs, jdk libs)
	  *
	  * @return searchFor Flag (composition of IJavaSearchConstant constants)
	  * @author markus.poechtrager
	 */
	public static int getSearchTargets() {
		int searchFor = 0;

		searchFor = IJavaSearchScope.SOURCES
						| IJavaSearchScope.SYSTEM_LIBRARIES
						| IJavaSearchScope.APPLICATION_LIBRARIES;

//		// check if Source Folders need to be included in search
//		boolean includeSRC = Activator.getDefault().getPreferenceStore().getBoolean(Constants.PREF_INCLUDE_SOURCES);
//		if (includeSRC) {
//			searchFor |= IJavaSearchScope.SOURCES;
//		}
//
//		// check if JDK Libs needs to be included in search
//		boolean includeJDK = Activator.getDefault().getPreferenceStore().getBoolean(Constants.PREF_INCLUDE_JDKLIBS);
//		if (includeJDK) {
//			searchFor |= IJavaSearchScope.SYSTEM_LIBRARIES;
//		}
//
//		// check if App Libs needs to be included in search
//		boolean includeAppLib = Activator.getDefault().getPreferenceStore().getBoolean(Constants.PREF_INCLUDE_APPLIBS);
//		if (includeAppLib) {
//			searchFor |= IJavaSearchScope.APPLICATION_LIBRARIES;
//		}

		return searchFor;
	}

}
