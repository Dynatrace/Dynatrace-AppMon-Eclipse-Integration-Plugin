package com.dynatrace.diagnostics.launcher.ui.errorpopup;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.launcher.ui.util.GetWorkbench;

/**
 * @author Michal.Weyer
 * @since 2016-04-15
 */
class TransientErrorPopup extends Shell {

	private final Composite linksComposite;
	private final Composite compositeContent;
	private final Composite compositeVCentered;
	private final Label lblTitle;
	private final Link linkOnlineDocumentation;

	TransientErrorPopup(String errorMsg) {
		super(GetWorkbench.getWorkbenchWindow().getShell(), SWT.NONE);
		setSize(440, 147);

		setLayout(new GridLayout(2, false));

		Composite imagePaddingComposite = new Composite(this, SWT.NONE);
		imagePaddingComposite.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, true, 1, 1));
		RowLayout rl_imagePaddingComposite = new RowLayout(SWT.HORIZONTAL);
		rl_imagePaddingComposite.marginBottom = 15;
		rl_imagePaddingComposite.marginTop = 15;
		rl_imagePaddingComposite.marginLeft = 10;
		imagePaddingComposite.setLayout(rl_imagePaddingComposite);

		Label lblCrossIcon = new Label(imagePaddingComposite, SWT.NONE);
		lblCrossIcon.setImage(Activator.getDefault().getImageRegistry().get(Constants.IMG_POPUP_ERROR));

		compositeContent = new Composite(this, SWT.NONE);
		compositeContent.setLayout(new FillLayout(SWT.VERTICAL));
		GridData gd_compositeContent = new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1);
		gd_compositeContent.widthHint = 305;
		compositeContent.setLayoutData(gd_compositeContent);

		compositeVCentered = new Composite(compositeContent, SWT.NONE);
		GridLayout gl_compositeVCentered = new GridLayout(1, false);
		gl_compositeVCentered.marginRight = 5;
		compositeVCentered.setLayout(gl_compositeVCentered);

		lblTitle = new Label(compositeVCentered, SWT.WRAP);
		lblTitle.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 2, 1));
		lblTitle.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblTitle.setText(errorMsg);

		linksComposite = new Composite(compositeVCentered, SWT.NONE);
		linksComposite.setLayout(new FillLayout(SWT.VERTICAL));
		linksComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		linkOnlineDocumentation = new Link(linksComposite, SWT.NONE);
			linkOnlineDocumentation.setText("<a>Online Documentation</a>");

		Link linkErrorLog = new Link(linksComposite, SWT.NONE);
			linkErrorLog.setText("<a>Error log</a>");
			linkErrorLog.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseDown(MouseEvent e) {
					ShowErrorLog.showErrorLog();
					TransientErrorPopup.this.close();
				}
			});

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TransientErrorPopup.this.close();
			}
		});
		imagePaddingComposite.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TransientErrorPopup.this.close();
			}
		});
		compositeContent.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TransientErrorPopup.this.close();
			}
		});
		compositeVCentered.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TransientErrorPopup.this.close();
			}
		});

		drawCloseCross();
	}

	private void drawCloseCross() {
		final int CROSS_MARGIN_RIGHT = 4;
		final int CROSS_MARGIN_TOP = 4;
		final Image cross = Activator.getDefault().getImageRegistry().get(Constants.IMG_POPUP_CLOSE);
		final int cross_width = cross.getImageData().width;

		Point parentSize = TransientErrorPopup.this.getSize();
		final Point parentLocation = new Point(parentSize.x - cross_width - CROSS_MARGIN_RIGHT, CROSS_MARGIN_TOP);

		List<Control> drawCrossOver = Arrays.asList(compositeContent, compositeVCentered, lblTitle, this);
		for (final Control control : drawCrossOver) {
			control.addPaintListener(new PaintListener() {
				@Override
				public void paintControl(PaintEvent e) {
					Point childLocation = translateToChildOffset(control, parentLocation);
					e.gc.drawImage(cross, childLocation.x, childLocation.y);
				}
			});
		}
	}

	private Point translateToChildOffset(Control child, Point parentLocation) {
		if (child instanceof Shell) {
			return parentLocation;
		}

		Point inChildOffset = new Point(0, 0);
		while ( !(child.getParent() instanceof Shell)) {
			inChildOffset.x += child.getLocation().x;
			inChildOffset.y += child.getLocation().y;
			child = child.getParent();
		}

		inChildOffset.x += child.getLocation().x;
		inChildOffset.y += child.getLocation().y;

		return new Point(parentLocation.x - inChildOffset.x, parentLocation.y - inChildOffset.y);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	void showGlobalConfig() {
		Link dynatraceGlobalConfig = new Link(linksComposite, SWT.NONE);
		dynatraceGlobalConfig.setText("<a>Dynatrace config</a>");

		dynatraceGlobalConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TransientErrorPopup.this.close();
				ShowErrorLog.showDynatraceGlobalConfigurationWindows();
			}
		});
	}

	void showRunConfig() {
		Link dynatraceLaunchConfig = new Link(linksComposite, SWT.NONE);
		dynatraceLaunchConfig.setText("<a>Launch Config</a>");
		dynatraceLaunchConfig.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				TransientErrorPopup.this.close();
				ShowErrorLog.showDynatraceLaunchConfigWindow();
			}
		});
	}

	void setOnlineDocumentationLinkDestination(final String url) {
		linkOnlineDocumentation.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				try {
					IWebBrowser browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
					browser.openURL(new URL(url));
				} catch (Exception ex) {
					LogHelper.logError("Error navigating to documentation page from error popup [ErrorLocation-93]");
				}
			}
		});
	}
}
