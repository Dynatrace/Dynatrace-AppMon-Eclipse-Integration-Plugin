package com.dynatrace.diagnostics.codelink.core.searchengine;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.internal.ui.search.JavaSearchQuery;
import org.eclipse.jdt.internal.ui.search.JavaSearchResult;
import org.eclipse.jdt.ui.search.PatternQuerySpecification;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;

/** Wrapper for JDT Query Engine
  *
  * @author markus.poechtrager
 */
@SuppressWarnings("restriction")
public class CodeLinkSearchEngine {

	private JavaSearchQuery javaSearchQuery;
	private JavaSearchResult searchResult;

	public IMember[] search(
			String searchText, 
			int searchFor, 
			IJavaElement[] searchableElems, 
			int includeMask, 
			IProgressMonitor monitor) {

		boolean caseSensitive = true;
		
		IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(searchableElems, includeMask);
		
		// create query pattern
		PatternQuerySpecification patternQuery = new PatternQuerySpecification(
				searchText,
				searchFor,
				caseSensitive,
				IJavaSearchConstants.DECLARATIONS,
				searchScope,
				StringResources.searchview_description);
		
		if (Activator.getDefault().isDebug()) {
			LogHelper.logInfo("Searching for Enclosing Class.");
			LogHelper.logInfo("	   Search Text: " + searchText);
			LogHelper.logInfo("    Search For: " + searchFor);
			
			LogHelper.logInfo("    Elements to search in: ");
			for (int i=0; i<searchableElems.length; i++) {
				LogHelper.logInfo("          " + searchableElems[i].getElementName());
			}
			LogHelper.logInfo("    Include Mask: " + includeMask);			
			LogHelper.logInfo("    Case Sensitive: " + caseSensitive);
		}
		
		javaSearchQuery = new JavaSearchQuery(patternQuery);
		
		// run query
		IStatus status = javaSearchQuery.run(monitor);
		
		if (Activator.getDefault().isDebug()) {
			Activator.getDefault().getLog().log(status);
		}
		
		searchResult = (JavaSearchResult)javaSearchQuery.getSearchResult();
		Object[] results = searchResult.getElements();
		List<IMember> lst = new ArrayList<IMember>(); 
		
		// check if we are really dealing with IMember types
		for (int i=0; i<results.length; i++) {
			if (results[i] instanceof IMember) {
				lst.add((IMember)results[i]);
			}
		}
		
		if (Activator.getDefault().isDebug()) {
			LogHelper.logInfo("    Search Results: ");
			for (Iterator<IMember> it = lst.iterator(); it.hasNext();) {
				IMember resItem = (IMember) it.next();
				IJavaProject javaProject = resItem.getJavaProject();
				IType declaringType = resItem.getDeclaringType();
				String elementName = resItem.getElementName();
				IPath path = resItem.getPath();
				LogHelper.logInfo("        Project: " + javaProject.getElementName() + ", Element: " + elementName + ",Path: " + path + ", Declaring Type: " + declaringType);
			}
		}

		return (IMember[])lst.toArray(new IMember[lst.size()]);
	}
	
	
	/** Returns last conducted query
	  * 
	  * @return JavaSearchQuery
	  * @author markus.poechtrager
	 */
	public JavaSearchQuery getLastQuery() {
		return javaSearchQuery;
	}
	
}
