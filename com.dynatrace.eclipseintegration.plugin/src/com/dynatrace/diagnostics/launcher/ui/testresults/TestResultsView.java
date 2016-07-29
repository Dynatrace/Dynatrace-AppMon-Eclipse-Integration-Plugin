package com.dynatrace.diagnostics.launcher.ui.testresults;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.browser.IWebBrowser;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.wb.swt.SWTResourceManager;

import com.dynatrace.diagnostics.codelink.logging.LogHelper;
import com.dynatrace.diagnostics.eclipseintegration.Activator;
import com.dynatrace.diagnostics.eclipseintegration.Constants;
import com.dynatrace.diagnostics.eclipseintegration.StringResources;
import com.dynatrace.sdk.server.testautomation.models.TestMeasure;
import com.dynatrace.sdk.server.testautomation.models.TestResult;
import com.dynatrace.sdk.server.testautomation.models.TestRun;
import com.dynatrace.sdk.server.testautomation.models.TestStatus;

public class TestResultsView extends ViewPart {
	public TestResultsView() {
	}

	static final String VIEW_ID = "com.dynatrace.diagnostics.testruns.view";

	private Composite contentComposite;
	private ScrolledComposite sc;

	@Override
	public void createPartControl(final Composite parent) {
		sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL);
		this.contentComposite = new Composite(sc, SWT.BORDER);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		contentComposite.setLayout(gridLayout);
		contentComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		sc.setContent(contentComposite);
		sc.setExpandHorizontal(true);
		sc.setExpandVertical(true);
		sc.addControlListener(new ControlAdapter() {
			@Override
			public void controlResized(ControlEvent e) {
				sc.setMinSize(contentComposite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});

		Label useButtonLabel = new Label(contentComposite, SWT.NONE);
		useButtonLabel.setText(StringResources.testResultsView_noResults);

		Link linkNoResultsDoc = new Link(contentComposite, SWT.NONE);
		linkNoResultsDoc.setForeground(SWTResourceManager.getColor(36));
		linkNoResultsDoc.setText(StringResources.testResultsView_moreInformation);
		linkNoResultsDoc.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDown(MouseEvent e) {
				IWebBrowser browser;
				try {
					browser = PlatformUI.getWorkbench().getBrowserSupport().getExternalBrowser();
					browser.openURL(new URL(StringResources.link_url_no_testruns));
				} catch (Exception ex) {
					LogHelper.logError("Error while navigating to no testruns documentation [ErrorLocation-96]", ex);
				}
			}
		});
	}

	private TableViewer createTestResultTable() {
		TableViewer dataTable = new TableViewer(this.contentComposite,
				SWT.FULL_SELECTION | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER_SOLID);
		dataTable.getTable().addListener(SWT.Resize, new Listener() {

			public void handleEvent(Event event) {

				Table table = (Table) event.widget;
				int columnCount = table.getColumnCount();
				if (columnCount == 0)
					return;
				Rectangle area = table.getClientArea();
				int totalAreaWidth = area.width;
				int lineWidth = table.getGridLineWidth();
				int totalGridLineWidth = (columnCount - 1) * lineWidth;
				int totalColumnWidth = 0;
				for (TableColumn column : table.getColumns()) {
					totalColumnWidth = totalColumnWidth + column.getWidth();
				}
				int diff = totalAreaWidth - (totalColumnWidth + totalGridLineWidth);

				TableColumn lastCol = table.getColumns()[columnCount - 1];

				// check diff is valid or not. setting negative width doesnt
				// make sense.
				lastCol.setWidth(diff + lastCol.getWidth());

			}
		});
		dataTable.setContentProvider(ArrayContentProvider.getInstance());

		dataTable.getTable().setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false));

		TableViewerColumn col = createColumn(StringResources.testResultsView_metricGroup, dataTable);
		col.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				TestRunResultMeasureRow m = (TestRunResultMeasureRow) element;
				return m.metricGroup;
			}
		});
		col = createColumn(StringResources.testResultsView_metricName, dataTable);
		col.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				TestRunResultMeasureRow m = (TestRunResultMeasureRow) element;
				return m.metricName;
			}
		});
		col = createColumn(StringResources.testResultsView_metricValue, dataTable);
		col.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				TestRunResultMeasureRow m = (TestRunResultMeasureRow) element;
				return m.value;
			}
		});
		col = createColumn(StringResources.testResultsView_metricUnit, dataTable);
		col.setLabelProvider(new ColumnLabelProvider() {

			@Override
			public String getText(Object element) {
				TestRunResultMeasureRow m = (TestRunResultMeasureRow) element;
				return m.unit;
			}
		});

		dataTable.getTable().setHeaderVisible(true);
		return dataTable;
	}

	private TableViewerColumn createColumn(String header, TableViewer dataTable) {
		TableViewerColumn col = new TableViewerColumn(dataTable, SWT.NONE);
		col.getColumn().setWidth(100);
		col.getColumn().setText(header);
		return col;
	}

	@Override
	public void setFocus() {

	}

	void refresh(List<TestRun> finishedTestRuns) {
		for (Control kid : contentComposite.getChildren()) {
			kid.dispose();
		}
		for (TestRun testRun : finishedTestRuns) {
			for (TestResult result : testRun.getTestResults()) {
				CLabel testNameLabel = new CLabel(contentComposite, SWT.NONE);
				testNameLabel.setFont(JFaceResources.getFontRegistry().getBold(JFaceResources.DEFAULT_FONT));
				Image image;
				if (result.getStatus() == TestStatus.PASSED) {
					image = Activator.getDefault().getImageRegistry().get(Constants.IMG_TEST_OK);
				} else if (result.getStatus() == TestStatus.FAILED) {
					image = Activator.getDefault().getImageRegistry().get(Constants.IMG_TEST_FAILED);
				} else {
					image = Activator.getDefault().getImageRegistry().get(Constants.IMG_TEST_VOLATILE);
				}
				testNameLabel.setImage(image);
				final ToolTip tip = new ToolTip(testNameLabel.getShell(), SWT.ON_TOP | SWT.TOOL);
				tip.setMessage(StringResources.testResultsView_testStatus + ": " + result.getStatus());
				testNameLabel.setText(testNameLabelOf(result));
				testNameLabel.addListener(SWT.MouseHover, new Listener() {

					@Override
					public void handleEvent(Event event) {
						if (!tip.isDisposed()) {
							CLabel actionWidget = (CLabel) event.widget;
							Point eventLocation = actionWidget.toDisplay(event.x, event.y);
							tip.setLocation(eventLocation.x + 16, eventLocation.y);
							tip.setVisible(true);
						}
					}
				});
				testNameLabel.addListener(SWT.MouseExit, new Listener() {

					@Override
					public void handleEvent(Event event) {
						if (!tip.isDisposed()) {
							tip.setVisible(false);
						}
					}
				});
				TableViewer dataTable = createTestResultTable();
				List<TestRunResultMeasureRow> rows = new ArrayList<TestRunResultMeasureRow>();
				for (TestMeasure measure : result.getMeasures()) {
					TestRunResultMeasureRow row = new TestRunResultMeasureRow();
					row.metricGroup = measure.getMetricGroup();
					row.metricName = measure.getName();
					row.unit = measure.getUnit();
					row.value = String.valueOf(measure.getValue());
					rows.add(row);
				}
				dataTable.setInput(rows);
				dataTable.refresh();
				for (TableColumn tableColumn : dataTable.getTable().getColumns()) {
					tableColumn.pack();
				}

			}
		}
		sc.notifyListeners(SWT.Resize, new Event());
		contentComposite.layout(true);
		sc.layout(true);
	}

	private String testNameLabelOf(TestResult result) {
		if (result.getPackageName() == null) {
			return result.getName();
		}
		return result.getPackageName() + "." + result.getName();
	}
}
