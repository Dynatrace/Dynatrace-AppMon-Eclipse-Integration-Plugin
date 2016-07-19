package com.dynatrace.diagnostics.codelink.ui.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMember;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.ui.javaeditor.JavaEditor;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jdt.internal.ui.search.JavaSearchQuery;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.search.ui.NewSearchUI;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.dialogs.ListDialog;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;

/** Helper to simplify UI interaction
  *
  * @author markus.poechtrager
 */
@SuppressWarnings("restriction")
public final class UIUtils {

	private UIUtils() {}

	/**
	 * Reveals IMember in Package Explorer
	 *
	 * @param obj The object which should be revealed
	 */
	public static void revealInPackageExplorer(final IMember obj) {
		// ensure we run in gui thread
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				PackageExplorerPart part = PackageExplorerPart.getFromActivePerspective();
				if (part != null) {
					part.selectAndReveal(obj);
				}
			}
		});
	}

	/**
	 * Switch from current Perspective to Java Browsing Perspective
	 */
	public static void switchToJavaBrowsingPerspective() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				IWorkbench wb = PlatformUI.getWorkbench();
				try {
					wb.showPerspective(JavaUI.ID_BROWSING_PERSPECTIVE, wb.getActiveWorkbenchWindow());
				}
				catch (WorkbenchException e) {
					LogHelper.logError("Error switching to Java Browsing Perspective [ErrorLocation-86]");
				}
			}
		});
	}

	/**
	 * Open an IMember in a Java Editor
	 *
	 * @param obj The object which should be openend/revealed
	 * @throws JavaModelException
	 * @throws PartInitException
	 */
	public static void openInJavaEditor(final IMember obj) {
		// ensure we run in gui thread
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				try {
					JavaEditor editor = (JavaEditor)JavaUI.openInEditor(obj, true, true);
					editor.setSelection(obj);
				}
				catch (Exception e) {
					LogHelper.logError("Error opening Java Editor [ErrorLocation-87]", e);
				}
			}
		});
	}


	/**@deprecated
	  *
	  * Result Selection Dialog
	  *
	  * @param parent
	  * @param results
	  * @return selected IMember or null if user cancels
	  * @author markus.poechtrager
	 */
	public static IMember showSelectionDialog(final Composite parent, final IMember[] results) {
		final List<IMember> resultWrapper = new ArrayList<IMember>(1);

		final ILabelProvider labelProv = new ILabelProvider() {
			public Image getImage(Object element) {
				return null;
			}

			public String getText(Object element) {
				if (element instanceof IMethod) {
					IMethod tmp = (IMethod)element;
					String projectName = tmp.getJavaProject().getElementName();
					String packageName = tmp.getDeclaringType().getPackageFragment().getElementName();
					String className = tmp.getParent().getElementName();
					String elemName = tmp.getElementName();

					//TODO if used, add parameter
//					String params = "";
//
//					for (String s : tmp.getParameterTypes()) {
//						params += s
//					}
					return "[" + projectName + "]" + " " + packageName + " " + className + "." + elemName;
				}
				else if (element instanceof IType) {
					IType tmp = (IType)element;
					String projectName = tmp.getJavaProject().getElementName();
					String packageName = tmp.getPackageFragment().getElementName();
					String className = tmp.getElementName();
					return "[" + projectName + "]" + " " + packageName + " " + className;
				}
				return null;
			}

			public void addListener(ILabelProviderListener listener) {}

			public void dispose() {}

			public boolean isLabelProperty(Object element,	String property) {
				return false;
			}

			public void removeListener(ILabelProviderListener listener) {}
		};

		final IStructuredContentProvider contentProv = new IStructuredContentProvider() {
			public Object[] getElements(Object inputElement) {
				if (inputElement instanceof IMember[]) {
					return (Object[])inputElement;
				}
				return null;
			}

			public void dispose() {}

			public void inputChanged(Viewer viewer,	Object oldInput, Object newInput) {}
		};

		ListDialog dialog = new ListDialog(parent.getShell());

		dialog.setTitle("Selection Dialog");
		dialog.setLabelProvider(labelProv);
		dialog.setContentProvider(contentProv);
		dialog.setInitialSelections(new Object[] { results[0] });
		dialog.setWidthInChars(getLongestStringEntry(results, 80));
		dialog.setHeightInChars(results.length + 2);
		dialog.setInput(results);

		if (dialog.open() == Dialog.OK) {
			IMember theChosenOne = (IMember)dialog.getResult()[0];
			resultWrapper.add(theChosenOne);
		}

		return resultWrapper.size() == 1 ? (IMember)resultWrapper.get(0) : null;
	}

	/**
	 * @deprecated
	 *
	 */
	private static int getLongestStringEntry(IMember[] results, int maxLength) {
		int length = 0;

		for (int i = 0; i < results.length; i++) {
			IMember member = results[i];
			int mLength = member.toString().length();
			length = mLength > length ? mLength : length;
		}

		return length > maxLength ? maxLength : length;
	}


	public static void runJavaQuery(final JavaSearchQuery searchQuery) {
		// ensure we run in gui thread
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				NewSearchUI.runQueryInBackground(searchQuery);
			}
		});
	}

	/** Brings eclipse on top of all windows
	  * (currently only blinks in Taskbar)
	  *
	  * @author markus.poechtrager
	 */
	public static void bringEclipseToFront() {
		PlatformUI.getWorkbench().getDisplay().syncExec(new Runnable() {
			public void run() {
				Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
				shell.setVisible(true);
				shell.setMinimized(false);
				shell.forceFocus();
				shell.forceActive();
				shell.open();
			}
		});
	}


	/** Retrieves currently selected project in tree
	  *
	  * @return
	  * @author markus.poechtrager
	 */
	public static IProject getActiveProject() {
		final List<IProject> resultHolder = new ArrayList<IProject>(1);
		Display.getDefault().syncExec(new Runnable() {
			public void run() {
				PackageExplorerPart part = PackageExplorerPart.getFromActivePerspective();
				// get active project from selection in tree
				if (part != null) {
					Object selObject = (((IStructuredSelection)part.getTreeViewer().getSelection()).getFirstElement());
					if (selObject instanceof IResource) {
						IResource resource = (IResource)selObject;
						resultHolder.add(resource.getProject());
					}
					else if (selObject instanceof IJavaElement) {
						IJavaElement element = (IJavaElement)selObject;
						resultHolder.add(element.getJavaProject().getProject());
					}
				}
			}
		});

		if (!resultHolder.isEmpty()) {
			return (IProject) resultHolder.get(0);
		}
		return null;
	}
}
