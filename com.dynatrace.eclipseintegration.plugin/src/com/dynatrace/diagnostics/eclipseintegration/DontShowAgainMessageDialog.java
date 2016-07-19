package com.dynatrace.diagnostics.eclipseintegration;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
* Dialog with Buttons "OK" and "Cancel" and a "don't show this message again"-Flag.
*
* @author alexander.scheran
*/

public class DontShowAgainMessageDialog extends MessageDialog {

   private static final String DIALOG_ROOT_ID = "dynatrace_dontshowagaindialog."; //$NON-NLS-1$

	private static final int RETURN_CODE_DONTSHOWAGAIN = -11;

	private String dialogId	= null;

	private Button button = null;

	/**
	 * SN:
	 * Attention! Leave package visibility to dialog creation methods.
	 * Use the {@link DialogCenter} instead to create and show the dialog.
	 *
	 * @param parentShell
	 * @param dialogId
	 * @param dialogTitle
	 * @param dialogTitleImage
	 * @param dialogMessage
	 * @param dialogImageType
	 * @param dialogButtonLabels
	 * @param defaultIndex
	 * @author stefan.nadschlaeger
	 */
	private DontShowAgainMessageDialog(Shell parentShell, String dialogId, String dialogTitle, Image dialogTitleImage,
			String dialogMessage, int dialogImageType, String[] dialogButtonLabels, int defaultIndex) {
		super(parentShell, dialogTitle, dialogTitleImage, dialogMessage, dialogImageType, dialogButtonLabels, defaultIndex);
		this.dialogId = dialogId;
	}


   /**
    * Convenience method to open a standard information dialog.
    *
    * @param parent the parent shell of the dialog, or <code>null</code> if none
    * @param title the dialog's title, or <code>null</code> if none
    * @param message the message
    */
   public static DontShowAgainMessageDialog createInformationDialog(Shell parent, String dialogId, String title, String message) {
   	DontShowAgainMessageDialog dialog = new DontShowAgainMessageDialog(parent, dialogId, title, null, message, INFORMATION, new String[] {IDialogConstants.OK_LABEL }, 0);
   	return dialog;
   }

   /**
    * Convenience method to open a standard YES/NO Confirm dialog.
    *
    * @param parent the parent shell of the dialog, or <code>null</code> if none
    * @param title the dialog's title, or <code>null</code> if none
    * @param question the question
    */
   public static DontShowAgainMessageDialog createConfirmDialog(Shell parent, String dialogId, String title, String question) {
       DontShowAgainMessageDialog dialog = new DontShowAgainMessageDialog(parent, dialogId, title, null, question, QUESTION, new String[] {IDialogConstants.YES_LABEL, IDialogConstants.NO_LABEL }, 0);
       return dialog;
   }

   /**
    * Convenience method to open a Confirm dialog with configurable buttons.
    *
    * @param parent the parent shell of the dialog, or <code>null</code> if none
    * @param title the dialog's title, or <code>null</code> if none
    * @param question the question
    */
   public static DontShowAgainMessageDialog createConfigurableConfirmDialog(Shell parent, String dialogId, String title, String question, String okLabel, String cancelLabel) {
   	DontShowAgainMessageDialog dialog = new DontShowAgainMessageDialog(parent, dialogId, title, null, question, QUESTION, new String[] {okLabel, cancelLabel}, 0);
   	return dialog;
   }

   /**
    * Convenience method to open a standard warning dialog.
    *
    * @param parent the parent shell of the dialog, or <code>null</code> if none
    * @param title the dialog's title, or <code>null</code> if none
    * @param message the message
    * @param withCancel if this dialog should have a cancel button
    */
   public static DontShowAgainMessageDialog createWarningDialog(Shell parent, String dialogId, String title, String message, boolean withCancel) {
   	DontShowAgainMessageDialog dialog = null;
   	if (withCancel) {
   		dialog = new DontShowAgainMessageDialog(parent, dialogId, title, null, message, WARNING, new String[] {IDialogConstants.OK_LABEL, IDialogConstants.CANCEL_LABEL}, 0);
   	}
   	else {
   		dialog = new DontShowAgainMessageDialog(parent, dialogId, title, null, message, WARNING, new String[] {IDialogConstants.OK_LABEL }, 0);
   	}
   	return dialog;
   }


   /**
    * Returns the 'don't show again' state of this dialog with the given id
    * @author alexander.scheran
    * @param dialogId The id of this dialog
    * @return true, if this dialog with the given id should not be shown again
    */
   private static boolean isDontShowAgain(String dialogId) {
   IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
   	return prefStore.getBoolean(DIALOG_ROOT_ID+dialogId);
   }

   /**
    * Sets the dialog with the given id to state 'dont' show again'
    * @author alexander.scheran
    * @param dialogId The dialog id
    */
   private static void setDontShowAgain(String dialogId) {
   	IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
   	prefStore.setValue(DIALOG_ROOT_ID+dialogId, Boolean.TRUE.booleanValue());
   }

	@Override
	public int open() {
		if (dialogId != null && isDontShowAgain(dialogId)) {
			return RETURN_CODE_DONTSHOWAGAIN;
		}
		return super.open();
	}

	@Override
	public boolean close() {
		if (button.getSelection() == true && dialogId != null) {
			setDontShowAgain(dialogId);
		}
		return super.close();
	}

	@Override
	protected Control createDialogArea(Composite parent) {
		Composite composite = (Composite)super.createDialogArea(parent);
		Composite dontShowAgainComposite = createComposite(parent, SWT.NONE, 1, 4, 0, 0, 0, GridData.FILL_HORIZONTAL);
		GridData data = new GridData();
		data.horizontalAlignment = GridData.FILL;
		data.horizontalSpan = 2;
		dontShowAgainComposite.setLayoutData(data);
		button = new Button(dontShowAgainComposite, SWT.CHECK);
		button.setText("Don't show this again");
		return composite;
	}

    private static Composite createComposite(Composite parent, int style, int columns, int marginWidth, int marginHeight,
			int verticalSpacing, int horizontalSpacing, int gridStyle) {
        Composite composite = new Composite(parent, style);
        GridLayout layout = new GridLayout(columns, false);
        layout.marginHeight = marginHeight;
        layout.marginWidth = marginWidth;
        layout.verticalSpacing = verticalSpacing;
        layout.horizontalSpacing = horizontalSpacing;
        composite.setLayout(layout);

        if (parent.getLayout() instanceof GridLayout) {
        	composite.setLayoutData(new GridData(gridStyle));
        }
        return composite;
    }
}
