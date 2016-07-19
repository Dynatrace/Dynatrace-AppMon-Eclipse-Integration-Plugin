package com.dynatrace.diagnostics.codelink.core.utils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.ui.PlatformUI;

import com.dynatrace.diagnostics.codelink.core.client.CallDescriptorInterface;
import com.dynatrace.diagnostics.codelink.core.searchengine.CodeLinkSearchEngine;
import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.codelink.ui.utils.UIUtils;
import com.dynatrace.diagnostics.eclipseintegration.Activator;

public class Jumper {

	/** Uses multi query approach when searched for method:
	  *     - in first step matching classes will be queried
	  *     - in second step matching methods from resultset of step one will be queried
	  *
	  * @param descr
	  * @return true if successful, otherwise false
	  * @author markus.poechtrager
	 */
	public void jumpTo(final CallDescriptorInterface descr) throws NoSuchElementException {
		if (descr == null || descr.getClassName() == null || descr.getClassName().length() == 0) {
			return;
		}

		// just a holder object which contains the return type
		final List<Exception> exceptionHolder = new ArrayList<Exception>(1);

			// run stuff in gui thread
			PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
				public void run() {
					try {
						// run with progressbar
						PlatformUI.getWorkbench().getProgressService().run(true, true, new IRunnableWithProgress() {
							public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
								try {
									internalOpenInEclipse(descr, monitor);
								}
								catch (NoSuchElementException e) {
									exceptionHolder.add(e);
								}
								catch (CoreException e) {
									LogHelper.logError("Search error [ErrorLocation-81]", e);
								}
							}
						});
					}
					catch (Exception e) {
						LogHelper.logError("Search error [ErrorLocation-82]", e);
					}
				}
			});


		if (exceptionHolder.size() == 1) {
			throw (NoSuchElementException)exceptionHolder.get(0);
		}
	}


	/** Performs actual search and opens results in Package Explorer and Editor
	  * or in Search View respectively
	  *
	  * @param descr
	  * @param monitor
	  * @throws CoreException
	  * @throws NoSuchElementException
	  * @throws InvocationTargetException
	  * @throws InterruptedException
	  * @author markus.poechtrager
	 */
	private static void internalOpenInEclipse(final CallDescriptorInterface descr,	IProgressMonitor monitor)
	throws CoreException, NoSuchElementException, InvocationTargetException, InterruptedException {

		// search string
		StringBuilder searchStringBuilder = new StringBuilder(descr.getClassName());

		// search target
		// TYPE = [enum, interface, class, annotation]
		int searchFor = IJavaSearchConstants.TYPE;

		String className = descr.getClassName();
		String methodName = descr.getMethodName();

		// get the include mask from preferences
		// combination of Source, App Libs, JDK Libs
		// specifies the search scope
		int includeMask = SearchUtils.getSearchTargets();

		// jdt search wrapper
		CodeLinkSearchEngine searchEngine = new CodeLinkSearchEngine();
		//OldSearchEngine searchEngine = new OldSearchEngine();

		// participating projects
		IJavaProject[] projects = SearchUtils.getParticipatingProjects();

		// sync workspace prior search, if necessary
		if (CoreUtils.isSyncWorkspaceSet()) {
			if (!CoreUtils.isWorkspaceInSync(null, IResource.DEPTH_INFINITE)) {
				CoreUtils.syncWorkspace(monitor, IResource.DEPTH_INFINITE, includeMask);
			}
		}

		String searchText = searchStringBuilder.toString();
		int suffixStart = searchText.indexOf('$');
		if (suffixStart > 0) {
			if (Activator.getDefault().isDebug()) {
				LogHelper.logInfo("Anonymous inner class found, truncating inner class index.");
			}
			searchText = searchText.substring(0, suffixStart);

			// replace '$' with '.'
			searchStringBuilder.replace(suffixStart, suffixStart+1, ".");
		}

		// the results (matching classes)
		IMember[] results = searchEngine.search(searchText, searchFor, projects, includeMask, monitor);

		if (results == null || results.length == 0) {
			throw new NoSuchElementException("[ErrorLocation-31]");
		}

		// check search for method/constructor within class
		if (methodName != null && methodName.length() > 0) {
			// ensure we found matching classes

			String ctor = className;
			int idx = className.lastIndexOf(".");
			if (idx != -1) {
				ctor = className.substring(idx+1);
			}

			// check if constructor
			if (ctor.equals(methodName) || methodName.equals("<init>")) {
				// is constructor
				// change search flag to constructor
				searchFor = IJavaSearchConstants.CONSTRUCTOR;
			}
			else {
				// is method
				searchStringBuilder.append(".");
				searchStringBuilder.append(methodName);
				// change target search flag to method
				searchFor = IJavaSearchConstants.METHOD;
			}

			String[] args = descr.getMethodParameters();

			if (args != null && args.length > 0) {
				// add method/constructor parameters
				searchStringBuilder.append(SearchUtils.constructArgString(args));
			}
			else {
				// no parameters
				searchStringBuilder.append("()");
			}

			// search within matching classes
			results = searchEngine.search(searchStringBuilder.toString(), searchFor, results, includeMask, monitor);

			if (results == null || results.length == 0) {
				throw new NoSuchElementException("[ErrorLocation-32]");
			}
		} // if method/constructor

		// display results
		if (results.length == 1) {
			IMember result = results[0];
			if (CoreUtils.isOpenJavaBrowsingSet()) {
				// switch to Java Browsing Perspective
				UIUtils.switchToJavaBrowsingPerspective();
			}
			// open in java editor
			UIUtils.openInJavaEditor(result);
			// reveal in package explorer
			UIUtils.revealInPackageExplorer(result);
		}
		else {
			// if we have >1 results show in search view,
			// from which the user can choose from
			UIUtils.runJavaQuery(searchEngine.getLastQuery());
		}

		UIUtils.bringEclipseToFront();
	}
}
