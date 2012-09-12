package ips.tool;

import ips.algorithm.PositioningAlgorithmType;
import ips.algorithm.PositioningResult;
import ips.algorithm.knn.DistanceResult;
import ips.algorithm.knn.NNResults;
import ips.algorithm.test.batch.BatchPositioning;
import ips.algorithm.test.batch.BatchTest;
import ips.data.entities.Position;
import ips.data.entities.wlan.AccessPoint;
import ips.data.entities.wlan.AccessPointPowerLevels;
import ips.data.entities.wlan.WLANFingerprint;
import ips.data.serialization.Serializer;
import ips.data.upload.DataUploadFilter;
import ips.data.upload.FilterType;
import ips.http.HttpClient;
import ips.server.DataUploadRequest;
import ips.server.PositioningRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.StatusLineManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.text.TextViewer;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.swtchart.Chart;
import org.swtchart.IBarSeries;
import org.swtchart.ISeries.SeriesType;

public class IpsTool extends ApplicationWindow {
	private Text txtSetMeasurementsFolder;
	private Tree measurementsTree;
	private Canvas canvas;

	Display display = Display.getCurrent();
	Shell shell;

	private Tree resultsTree;

	java.util.List<WLANFingerprint> fps;
	java.util.List<DataUploadFilter> filters;

	java.util.List<BatchPositioning> batchPositioningRequests;
	java.util.List<DataUploadRequest> batchDataUploadRequest;

	java.util.List<BatchPositioning> batchRequests;

	private PaintListener mmPaintListener;
	private PaintListener drPaintListener;
	private CTabFolder tabFolder;
	private CTabItem tbtmAlgorihtmTests;
	private Composite composite_4;
	private CTabItem tbtmDatastoreSetup;
	private Composite composite_3;
	private Group group_1;
	private Label output1;
	private Text outputTxt1;
	private Label output2;
	private Text outputTxt2;
	private Label output3;
	private Text outputTxt3;
	private Label output4;
	private Text outputTxt4;
	private Composite composite_5;
	private Text txtFingerprintsFolder;
	private Button btnLoadFingerPrints;
	private Button btnBrowseFingerprints;
	private Group grpFilters;
	private Combo comboFilterType;
	private Text txtFilterValue;
	private Button btnNewFilter;
	private Label lblFilterType;
	private Text txtOutputFilterType;
	private Label lblFilterValue;
	private Text txtOutputFilterValue;
	private Label label;
	private Group group_2;
	private Tree treeFingerprints;
	private Group group_3;
	private Label outputUpload1;
	private Text outputUploadTxt1;
	private Label outputUpload2;
	private Text outputUploadTxt2;
	private Label outputUpload3;
	private Text outputUploadTxt3;
	private Label outputUpload4;
	private Text outputUploadTxt4;
	private Button btnFilterRemove;
	private Button btnUploadData;
	private List listFilters;
	private ListViewer listViewer;
	private Button btnClearResults;
	private Button btnClearMeasurements;
	private Button btnBatchPositioning;
	private Button btnBatchDataUpload;
	private CTabItem tbtmBatchTesting;
	private Composite composite_1;
	private List listBatchPosReqs;
	private ListViewer listViewer_1;
	private List listBatchUploadReq;
	private ListViewer listViewer_2;
	private Label label_1;
	private Label lblNewLabel_2;
	private Label lblNewLabel_3;
	private List listBatchRequests;
	private ListViewer listViewer_3;
	private Composite composite_2;
	private Button btnAddBatch;
	private Button btnClearBatch;
	private Label lblNewLabel_4;
	private Composite composite_6;
	private Button btnCheckClearData;
	private Button btnNewButton;
	private Group grpTestOptions;
	private Button btnCheckButton;
	private Button btnRunBatch;
	private Composite compositeBatchGraphs;
	private StyledText textBatchResults;
	private TextViewer textViewer;
	private CTabItem tbtmDataStoreAnalysis;
	private Composite compositeGraphTab;
	private Composite composite_7;
	private Label lblMap;
	private Combo combo;
	private ComboViewer comboViewer;

	private Button btnLoadData;
	private Label lblNewLabel_1;
	private Button btnNewButton_1;
	private StyledText styledText;

	/**
	 * Create the application window.
	 */
	public IpsTool() {
		super(null);
		createActions();
		addToolBar(SWT.FLAT | SWT.WRAP);
		addMenuBar();
		addStatusLine();

		display = Display.getCurrent();
		shell = new Shell(display);

		shell.setSize(getInitialSize());
		// ctx = new Context(new DataManager());

		filters = new ArrayList<DataUploadFilter>();

		batchDataUploadRequest = new ArrayList<DataUploadRequest>();
		batchPositioningRequests = new ArrayList<BatchPositioning>();

		batchRequests = new ArrayList<BatchPositioning>();
	}

	@SuppressWarnings("unchecked")
	private void outputTreeItem(final Object o) {

		if (Entry.class.isInstance(o)) {
			outputAp((Entry<AccessPoint, AccessPointPowerLevels>) o);
		} else if (DistanceResult.class.isInstance(o)) {
			outputDistanceRes((DistanceResult) o);

			if (drPaintListener != null) {
				canvas.removePaintListener(drPaintListener);
			}

			drPaintListener = new PaintListener() {
				public void paintControl(PaintEvent e) {
					display = Display.getCurrent();
					e.gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));

					DistanceResult dr = (DistanceResult) o;

					Position p = dr.pos;

					e.gc.fillOval(p.getX(), p.getY(), 15, 15);
				}
			};

			canvas.addPaintListener(drPaintListener);

			canvas.redraw();
		} else if (PositioningResult.class.isInstance(o)) {

			outputPositioningRes((PositioningResult) o);

			if (drPaintListener != null) {
				canvas.removePaintListener(drPaintListener);
			}

			drPaintListener = new PaintListener() {
				public void paintControl(PaintEvent e) {
					display = Display.getCurrent();
					e.gc.setBackground(display.getSystemColor(SWT.COLOR_GREEN));

					PositioningResult dr = (PositioningResult) o;

					Position p = dr.getPosition();

					e.gc.fillOval(p.getX(), p.getY(), 15, 15);
				}
			};

			canvas.addPaintListener(drPaintListener);

			canvas.redraw();

		} else if (WLANFingerprint.class.isInstance(o)) {

			if (mmPaintListener != null) {
				canvas.removePaintListener(mmPaintListener);
			}

			mmPaintListener = new PaintListener() {
				public void paintControl(PaintEvent e) {
					display = Display.getCurrent();
					e.gc.setBackground(display.getSystemColor(SWT.COLOR_CYAN));

					WLANFingerprint fp = (WLANFingerprint) o;

					Position p = fp.getPosition();

					e.gc.fillOval(p.getX(), p.getY(), 15, 15);
				}
			};

			canvas.addPaintListener(mmPaintListener);

			canvas.redraw();
		}
	}

	@SuppressWarnings("unchecked")
	private void outputTreeItem2(final Object o) {

		if (Entry.class.isInstance(o)) {
			output2Ap((Entry<AccessPoint, AccessPointPowerLevels>) o);
		}
	}

	private void outputDistanceRes(DistanceResult dRes) {

		output1.setText("Distance");
		outputTxt1.setText(Double.toString(dRes.distance));
		output2.setText("Matched aps");
		outputTxt2.setText(Double.toString(dRes.nrOfMatchedBssids));
		output3.setText("X");
		outputTxt3.setText(Double.toString(dRes.pos.getX()));
		output4.setText("Y");
		outputTxt4.setText(Double.toString(dRes.pos.getY()));
	}

	private void outputPositioningRes(PositioningResult dRes) {

		output3.setText("X");
		outputTxt3.setText(Double.toString(dRes.getX()));
		output4.setText("Y");
		outputTxt4.setText(Double.toString(dRes.getY()));
	}

	private void outputAp(Entry<AccessPoint, AccessPointPowerLevels> ap) {

		output1.setText("bssid");
		outputTxt1.setText(ap.getKey().getBSSID());
		output2.setText("ssid");
		outputTxt2.setText(ap.getKey().getSSID());
		output3.setText("frequency");
		outputTxt3.setText(Double.toString(ap.getKey().getFrequency()));
		output4.setText("level");
		outputTxt4.setText(Double.toString(ap.getValue().getAverage()));
	}

	private void clearOutput() {
		output1.setText("Output 1");
		outputTxt1.setText("");
		output2.setText("Output 2");
		outputTxt2.setText("");
		output3.setText("Output 3");
		outputTxt3.setText("");
		output4.setText("Output 4");
		outputTxt4.setText("");
	}

	private void output2Ap(Entry<AccessPoint, AccessPointPowerLevels> ap) {

		outputUpload1.setText("bssid");
		outputUploadTxt1.setText(ap.getKey().getBSSID());
		outputUpload2.setText("ssid");
		outputUploadTxt2.setText(ap.getKey().getSSID());
		outputUpload3.setText("frequency");
		outputUploadTxt3.setText(Integer.toString(ap.getKey().getFrequency()));
		outputUpload4.setText("level");
		outputUploadTxt4.setText(Double.toString(ap.getValue().getAverage()));
	}

	private void outputFilter(DataUploadFilter filter) {

		txtOutputFilterType.setText(filter.getType().getStringType());
		txtOutputFilterValue.setText(Integer.toString(filter.getFilterValue()));
	}

	private void setMap(String mapId) {

		String path = "";

		if (combo.getText().equals("Full map")) {
			path = "/home/wouter/Dropbox/1eMa/Thesis/SVN/Workspace2/ips_android_fingerprinting_offline/res/drawable-hdpi/full.png";
		} else if (combo.getText().equals("Medium map 1")) {
			path = "/home/wouter/Dropbox/1eMa/Thesis/SVN/Workspace2/ips_android_fingerprinting_offline/res/drawable-hdpi/medium1.png";
		} else if (combo.getText().equals("Medium map 2")) {
			path = "/home/wouter/Dropbox/1eMa/Thesis/SVN/Workspace2/ips_android_fingerprinting_offline/res/drawable-hdpi/medium2.png";
		} else if (combo.getText().equals("Small map 1")) {
			path = "/home/wouter/Dropbox/1eMa/Thesis/SVN/Workspace2/ips_android_fingerprinting_offline/res/drawable-hdpi/small1.png";
		} else if (combo.getText().equals("Small map 2")) {
			path = "/home/wouter/Dropbox/1eMa/Thesis/SVN/Workspace2/ips_android_fingerprinting_offline/res/drawable-hdpi/small2.png";
		}

		Image image = new Image(null, path);

		canvas.setBackgroundImage(image);

		GridData gridData = new GridData(SWT.CENTER, SWT.TOP, false, false, 1,
				2);
		gridData.heightHint = 502;
		gridData.widthHint = image.getBounds().width;

		canvas.setLayoutData(gridData);

		canvas.redraw();
	}

	/**
	 * Create contents of the application window.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createContents(Composite parent) {

		Composite container = new Composite(parent, SWT.NONE);
		container.setLayout(new GridLayout(3, false));

		tabFolder = new CTabFolder(container, SWT.BORDER);
		tabFolder.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, true, true, 3,
				1));
		tabFolder.setSelectionBackground(Display.getCurrent().getSystemColor(
				SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT));

		tbtmAlgorihtmTests = new CTabItem(tabFolder, SWT.NONE);
		tbtmAlgorihtmTests.setText("Algorihtm tests");

		composite_4 = new Composite(tabFolder, SWT.NONE);
		tbtmAlgorihtmTests.setControl(composite_4);
		composite_4.setLayout(new GridLayout(3, false));

		composite_7 = new Composite(composite_4, SWT.NONE);
		composite_7.setLayout(new RowLayout(SWT.HORIZONTAL));

		lblMap = new Label(composite_7, SWT.NONE);
		lblMap.setLayoutData(new RowData(128, 29));
		lblMap.setText("Indoor map");

		comboViewer = new ComboViewer(composite_7, SWT.NONE);
		combo = comboViewer.getCombo();
		combo.setItems(new String[] { "Full map", "Medium map 1",
				"Medium map 2", "Small map 1", "Small map 2" });
		combo.setLayoutData(new RowData(327, SWT.DEFAULT));
		combo.select(0);

		combo.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				String text = combo.getText();

				if (text.equals("Full map")) {
					setMap(text);
				} else if (text.equals("Medium map 1")) {
					setMap(text);
				} else if (text.equals("Medium map 2")) {
					setMap(text);
				} else if (text.equals("Small map 1")) {
					setMap(text);
				} else if (text.equals("Small map 2")) {
					setMap(text);
				}
			}

		});

		Composite composite = new Composite(composite_4, SWT.NONE);
		GridLayout gl_composite = new GridLayout(2, false);
		gl_composite.verticalSpacing = 7;
		composite.setLayout(gl_composite);

		txtSetMeasurementsFolder = new Text(composite, SWT.BORDER);
		txtSetMeasurementsFolder.setEditable(false);
		txtSetMeasurementsFolder.setText("Set measurements folder");

		Button btnChoose = new Button(composite, SWT.NONE);

		btnChoose.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (file.isDirectory()) {
						txtSetMeasurementsFolder.setText(file.getPath());
						btnLoadData.setEnabled(true);
					} else {
						txtSetMeasurementsFolder
								.setText("Please choose a folder");
					}
				}
			}
		});
		btnChoose.setText("Browse ....");
		{
			String path = "/home/wouter/Dropbox/1eMa/Thesis/SVN/Workspace/Fingerprinting_offline/res/drawable-hdpi/plan_brusselzuid_3.png";
			Image image = new Image(null, path);

			btnLoadData = new Button(composite_4, SWT.NONE);
			btnLoadData.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {

					measurementsTree.removeAll();

					/*
					 * Load the measurements by deserializing them
					 */
					Serializer sz = Serializer.getInstance();
					File folder = new File(txtSetMeasurementsFolder.getText());

					fps = new ArrayList<WLANFingerprint>();

					for (File file : folder.listFiles()) {
						try {
							WLANFingerprint fp = (WLANFingerprint) sz
									.deserialize(WLANFingerprint.class, file);

							fps.add(fp);
						} catch (Exception e1) {
							System.out.println("Error deserializing file : "
									+ file.getPath());
							e1.printStackTrace();
						}
					}

					measurementsTree.setData(fps);

					TreeItem item = new TreeItem(measurementsTree, SWT.NULL);
					item.setText("Measurements");
					item.setData(fps);
					int i = 0;
					for (WLANFingerprint fp : fps) {

						TreeItem fpItem = new TreeItem(item, SWT.NULL);
						fpItem.setText("Measurement " + i++);
						fpItem.setData(fp);

						fpItem.addListener(SWT.Selection, new Listener() {

							@Override
							public void handleEvent(Event e) {
								TreeItem[] items = measurementsTree
										.getSelection();

								final WLANFingerprint fp = (WLANFingerprint) items[0]
										.getData();

								canvas.addPaintListener(new PaintListener() {
									public void paintControl(PaintEvent e) {
										e.gc.setBackground(display
												.getSystemColor(SWT.COLOR_GREEN));

										Position p = fp.getPosition();

										e.gc.fillOval(p.getX(), p.getY(), 15,
												15);
									}
								});

								canvas.redraw();

							}
						});

						for (Entry<AccessPoint, AccessPointPowerLevels> entry : fp
								.getLevels().entrySet()) {

							TreeItem mmItem = new TreeItem(fpItem, SWT.NULL);
							mmItem.setText(entry.getKey().getSSID());
							mmItem.setData(entry);

							mmItem.addListener(SWT.SELECTED, new Listener() {

								@Override
								public void handleEvent(Event e) {
									TreeItem[] items = measurementsTree
											.getSelection();

									@SuppressWarnings("unchecked")
									final Entry<AccessPoint, AccessPointPowerLevels> ap = (Entry<AccessPoint, AccessPointPowerLevels>) items[0]
											.getData();

									outputAp(ap);

								}
							});
						}
					}

					measurementsTree
							.addSelectionListener(new SelectionListener() {

								@Override
								public void widgetDefaultSelected(
										SelectionEvent e) {
									// TODO Auto-generated method stub

								}

								@Override
								public void widgetSelected(SelectionEvent e) {
									TreeItem ti = (TreeItem) e.item;

									outputTreeItem(ti.getData());
								}
							});
				}
			});
			btnLoadData.setEnabled(false);
			btnLoadData.setText("Load data");

			canvas = new Canvas(composite_4, SWT.NONE);
			canvas.setBackgroundImage(image);

			GridData gridData = new GridData(SWT.CENTER, SWT.TOP, false, false,
					1, 2);
			gridData.heightHint = 502;
			gridData.widthHint = image.getBounds().width;

			canvas.setLayoutData(gridData);

		}

		Group grpDataSetup = new Group(composite_4, SWT.NONE);
		grpDataSetup.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1));
		grpDataSetup.setText("Test Data");
		grpDataSetup.setLayout(new GridLayout(1, false));

		measurementsTree = new Tree(grpDataSetup, SWT.BORDER);
		GridData gd_measurementsTree = new GridData(SWT.FILL, SWT.TOP, true,
				true, 1, 1);
		gd_measurementsTree.widthHint = 200;
		gd_measurementsTree.heightHint = 200;
		measurementsTree.setLayoutData(gd_measurementsTree);

		Group grpAlgorithmSetup = new Group(composite_4, SWT.NONE);
		GridData gd_grpAlgorithmSetup = new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 1);
		gd_grpAlgorithmSetup.heightHint = 217;
		grpAlgorithmSetup.setLayoutData(gd_grpAlgorithmSetup);
		grpAlgorithmSetup.setText("Algorithm Setup");
		grpAlgorithmSetup.setLayout(new GridLayout(1, false));

		Label lblNewLabel = new Label(grpAlgorithmSetup, SWT.NONE);
		lblNewLabel.setText("Algorithm");

		final List listAlgorithms = new List(grpAlgorithmSetup, SWT.BORDER);

		// list_1.setItems(new String[] { "Nearest Neighbors", "Bayes" });
		GridData gd_listAlgorithms = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_listAlgorithms.widthHint = 190;
		gd_listAlgorithms.heightHint = 66;
		listAlgorithms.setLayoutData(gd_listAlgorithms);

		/**
		 * Set the types algorithms
		 */
		java.util.List<String> algorithmTypes = new ArrayList<String>();
		String[] types = new String[PositioningAlgorithmType.values().length];
		for (PositioningAlgorithmType algo : PositioningAlgorithmType.values()) {
			algorithmTypes.add(algo.getAlgorithmName());
		}
		algorithmTypes.toArray(types);
		listAlgorithms.setItems(types);

		Button btnRunAlgo = new Button(grpAlgorithmSetup, SWT.NONE);

		{

		}

		btnRunAlgo.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				// Get the currently selected measurement
				TreeItem[] items = measurementsTree.getSelection();
				WLANFingerprint fp = (WLANFingerprint) items[0].getData();

				/** Get the selected Potioning algortihm type */
				String selection = listAlgorithms.getSelection()[0];
				PositioningAlgorithmType type = PositioningAlgorithmType
						.fromString(selection);

				// Create a new request object for the server
				PositioningRequest request = new PositioningRequest();
				request.fullResult = true;
				request.fp = fp;
				request.algorithm = type;

				// Send the request to the server
				PositioningResult pos = HttpClient.calculatePosition(request);

				TreeItem item = new TreeItem(resultsTree, SWT.NULL);
				item.setText("Results");
				int j = 0;

				switch (type) {
				case NearestNeighbors:
					NNResults res = (NNResults) pos;

					for (DistanceResult dRes : res.getResults()) {
						TreeItem dItem = new TreeItem(item, SWT.NULL);
						dItem.setText("Distance result " + j++);
						dItem.setData(dRes);
					}

					break;
				case BayesPositioning:

					TreeItem dItem = new TreeItem(item, SWT.NULL);
					dItem.setText("Bayes result " + j++);
					dItem.setData(pos);
					break;
				}

				resultsTree.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						final TreeItem ti = (TreeItem) e.item;

						outputTreeItem(ti.getData());
					}
				});
			}
		});

		btnRunAlgo.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnRunAlgo.setText("Run");

		{

		}

		btnBatchPositioning = new Button(grpAlgorithmSetup, SWT.NONE);
		btnBatchPositioning.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				@SuppressWarnings("unchecked")
				java.util.List<WLANFingerprint> fp = (java.util.List<WLANFingerprint>) measurementsTree
						.getData();
				/** Get the selected Positioning algorithm type */
				String selection = listAlgorithms.getSelection()[0];
				PositioningAlgorithmType type = PositioningAlgorithmType
						.fromString(selection);

				BatchPositioning batchPositioning = new BatchPositioning(fp,
						type);

				batchPositioningRequests.add(batchPositioning);

				listBatchPosReqs.add("Batch Pos Req "
						+ batchPositioningRequests.size());

				listAlgorithms.deselectAll();
			}
		});
		btnBatchPositioning.setText("Save for Batch");

		Group group = new Group(composite_4, SWT.NONE);
		group.setLayoutData(new GridData(SWT.LEFT, SWT.TOP, false, false, 1, 1));
		group.setText("Results");
		group.setLayout(new GridLayout(1, false));

		resultsTree = new Tree(group, SWT.BORDER);
		GridData gd_resultsTree = new GridData(SWT.FILL, SWT.FILL, true, true,
				1, 1);
		gd_resultsTree.widthHint = 200;
		gd_resultsTree.heightHint = 200;
		resultsTree.setLayoutData(gd_resultsTree);

		group_1 = new Group(composite_4, SWT.NONE);
		GridData gd_group_1 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				1);
		gd_group_1.heightHint = 193;
		gd_group_1.widthHint = 201;
		group_1.setLayoutData(gd_group_1);
		group_1.setText("Output");
		group_1.setLayout(new GridLayout(2, false));

		output1 = new Label(group_1, SWT.NONE);
		GridData gd_output1 = new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1);
		gd_output1.widthHint = 73;
		output1.setLayoutData(gd_output1);
		output1.setText("Output1");

		outputTxt1 = new Text(group_1, SWT.BORDER);
		GridData gd_outputTxt1 = new GridData(SWT.FILL, SWT.CENTER, false,
				false, 1, 1);
		gd_outputTxt1.widthHint = 128;
		outputTxt1.setLayoutData(gd_outputTxt1);

		output2 = new Label(group_1, SWT.NONE);
		output2.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		output2.setText("Output2");

		outputTxt2 = new Text(group_1, SWT.BORDER);
		outputTxt2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		output3 = new Label(group_1, SWT.NONE);
		output3.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		output3.setText("Output3");

		outputTxt3 = new Text(group_1, SWT.BORDER);
		outputTxt3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		output4 = new Label(group_1, SWT.NONE);
		output4.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false,
				1, 1));
		output4.setText("Output4");

		outputTxt4 = new Text(group_1, SWT.BORDER);
		outputTxt4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));
		new Label(group_1, SWT.NONE);

		btnClearResults = new Button(group_1, SWT.NONE);
		btnClearResults.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearOutput();
				listAlgorithms.deselectAll();
				resultsTree.removeAll();
			}
		});

		GridData gd_btnClearResults = new GridData(SWT.RIGHT, SWT.BOTTOM,
				false, false, 1, 1);
		gd_btnClearResults.widthHint = 146;
		gd_btnClearResults.heightHint = 26;
		btnClearResults.setLayoutData(gd_btnClearResults);
		btnClearResults.setText("Clear Results\n");
		new Label(group_1, SWT.NONE);

		btnClearMeasurements = new Button(group_1, SWT.NONE);
		btnClearMeasurements.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				clearOutput();
				listAlgorithms.deselectAll();
				measurementsTree.removeAll();
			}
		});
		btnClearMeasurements.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER,
				false, false, 1, 1));
		btnClearMeasurements.setText("Clear Measurements");

		tbtmDatastoreSetup = new CTabItem(tabFolder, SWT.NONE);
		tbtmDatastoreSetup.setText("Datastore setup");

		composite_3 = new Composite(tabFolder, SWT.NONE);
		tbtmDatastoreSetup.setControl(composite_3);
		composite_3.setLayout(new GridLayout(3, false));

		composite_5 = new Composite(composite_3, SWT.NONE);
		GridData gd_composite_5 = new GridData(SWT.LEFT, SWT.FILL, false,
				false, 1, 1);
		gd_composite_5.widthHint = 366;
		composite_5.setLayoutData(gd_composite_5);
		composite_5.setLayout(new GridLayout(3, false));

		txtFingerprintsFolder = new Text(composite_5, SWT.BORDER);
		txtFingerprintsFolder.setText("Set fingerprints folder");
		txtFingerprintsFolder.setEditable(false);
		txtFingerprintsFolder.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		btnLoadFingerPrints = new Button(composite_5, SWT.NONE);
		btnLoadFingerPrints.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				/*
				 * Load the measurements by deserializing them
				 */
				Serializer sz = Serializer.getInstance();
				File folder = new File(txtFingerprintsFolder.getText());

				fps = new ArrayList<WLANFingerprint>();

				for (File file : folder.listFiles()) {
					try {
						WLANFingerprint fp = (WLANFingerprint) sz.deserialize(
								WLANFingerprint.class, file);

						fps.add(fp);
					} catch (Exception e1) {
						System.out.println("Error deserializing file : "
								+ file.getPath());
						e1.printStackTrace();
					}
				}

				treeFingerprints.setData(fps);

				TreeItem item = new TreeItem(treeFingerprints, SWT.NULL);
				item.setText("Fingerprints");
				item.setData(fps);
				int i = 0;
				for (WLANFingerprint fp : fps) {

					TreeItem fpItem = new TreeItem(item, SWT.NULL);
					fpItem.setText("Fingerprint" + i++);
					fpItem.setData(fp);

					/*
					 * fpItem.addListener(SWT.Selection, new Listener() {
					 * 
					 * @Override public void handleEvent(Event e) { TreeItem[]
					 * items = measurementsTree.getSelection();
					 * 
					 * final Fingerprint fp = (Fingerprint) items[0] .getData();
					 * 
					 * canvas.addPaintListener(new PaintListener() { public void
					 * paintControl(PaintEvent e) { e.gc.setBackground(display
					 * .getSystemColor(SWT.COLOR_GREEN));
					 * 
					 * Position p = fp.getPosition();
					 * 
					 * e.gc.fillOval(p.getX(), p.getY(), 15, 15); } });
					 * 
					 * canvas.redraw();
					 * 
					 * } });
					 */

					for (Entry<AccessPoint, AccessPointPowerLevels> entry : fp
							.getLevels().entrySet()) {

						TreeItem mmItem = new TreeItem(fpItem, SWT.NULL);
						mmItem.setText(entry.getKey().getSSID());
						mmItem.setData(entry);

						mmItem.addListener(SWT.SELECTED, new Listener() {

							@Override
							public void handleEvent(Event e) {
								TreeItem[] items = treeFingerprints
										.getSelection();

								@SuppressWarnings("unchecked")
								final Entry<AccessPoint, AccessPointPowerLevels> ap = (Entry<AccessPoint, AccessPointPowerLevels>) items[0]
										.getData();

								output2Ap(ap);

							}
						});
					}
				}

				treeFingerprints.addSelectionListener(new SelectionListener() {

					@Override
					public void widgetDefaultSelected(SelectionEvent e) {
						// TODO Auto-generated method stub

					}

					@Override
					public void widgetSelected(SelectionEvent e) {
						TreeItem ti = (TreeItem) e.item;

						outputTreeItem2(ti.getData());
					}
				});
			}
		});
		btnLoadFingerPrints.setText("Load data");
		btnLoadFingerPrints.setEnabled(false);

		btnBrowseFingerprints = new Button(composite_5, SWT.NONE);
		btnBrowseFingerprints.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				DirectoryDialog dialog = new DirectoryDialog(shell, SWT.NULL);
				String path = dialog.open();
				if (path != null) {

					File file = new File(path);
					if (file.isDirectory()) {
						txtFingerprintsFolder.setText(file.getPath());
						btnLoadFingerPrints.setEnabled(true);
					} else {
						txtFingerprintsFolder.setText("Please choose a folder");
					}
				}
			}
		});

		btnBrowseFingerprints.setText("Browse ....");

		group_2 = new Group(composite_3, SWT.NONE);
		GridData gd_group_2 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				3);
		gd_group_2.heightHint = 237;
		group_2.setLayoutData(gd_group_2);
		group_2.setText("Test Data");
		group_2.setLayout(new GridLayout(1, false));

		treeFingerprints = new Tree(group_2, SWT.BORDER);
		GridData gd_treeFingerprints = new GridData(SWT.FILL, SWT.TOP, true,
				true, 1, 1);
		gd_treeFingerprints.widthHint = 200;
		gd_treeFingerprints.heightHint = 186;
		treeFingerprints.setLayoutData(gd_treeFingerprints);

		group_3 = new Group(composite_3, SWT.NONE);
		GridData gd_group_3 = new GridData(SWT.LEFT, SWT.TOP, false, false, 1,
				2);
		gd_group_3.widthHint = 205;
		gd_group_3.heightHint = 201;
		group_3.setLayoutData(gd_group_3);
		group_3.setText("Output");
		group_3.setLayout(new GridLayout(2, false));

		outputUpload1 = new Label(group_3, SWT.NONE);
		outputUpload1.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		outputUpload1.setText("Output1");

		outputUploadTxt1 = new Text(group_3, SWT.BORDER);
		outputUploadTxt1.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				false, false, 1, 1));

		outputUpload2 = new Label(group_3, SWT.NONE);
		outputUpload2.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		outputUpload2.setText("Output2");

		outputUploadTxt2 = new Text(group_3, SWT.BORDER);
		outputUploadTxt2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		outputUpload3 = new Label(group_3, SWT.NONE);
		outputUpload3.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		outputUpload3.setText("Output3");

		outputUploadTxt3 = new Text(group_3, SWT.BORDER);
		outputUploadTxt3.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		outputUpload4 = new Label(group_3, SWT.NONE);
		outputUpload4.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		outputUpload4.setText("Output4");

		outputUploadTxt4 = new Text(group_3, SWT.BORDER);
		outputUploadTxt4.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		grpFilters = new Group(composite_3, SWT.NONE);
		GridData gd_grpFilters = new GridData(SWT.LEFT, SWT.TOP, false, false,
				1, 2);
		gd_grpFilters.heightHint = 197;
		gd_grpFilters.widthHint = 362;
		grpFilters.setLayoutData(gd_grpFilters);
		grpFilters.setText("Filters");
		grpFilters.setLayout(new GridLayout(3, false));

		comboFilterType = new Combo(grpFilters, SWT.NONE);
		comboFilterType
				.setItems(new String[] { ">", ">=", "<", "<=", "=", "!=" });
		GridData gd_comboFilterType = new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1);
		gd_comboFilterType.widthHint = 138;
		comboFilterType.setLayoutData(gd_comboFilterType);

		txtFilterValue = new Text(grpFilters, SWT.BORDER);
		txtFilterValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true,
				false, 1, 1));

		btnNewFilter = new Button(grpFilters, SWT.NONE);
		btnNewFilter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				String filterType = comboFilterType.getText();
				String filterValue = txtFilterValue.getText();

				if ((filterType != null) && (filterValue != null)) {

					FilterType type = FilterType.fromString(filterType);
					DataUploadFilter filter = new DataUploadFilter(type,
							Integer.parseInt(filterValue));

					int filterNr = filters.size() + 1;
					listFilters.add("Filter " + filterNr);
					filters.add(filter);
				}
			}
		});
		btnNewFilter.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true,
				false, 1, 1));
		btnNewFilter.setText("New Filter");

		label = new Label(grpFilters, SWT.SEPARATOR | SWT.HORIZONTAL);
		GridData gd_label = new GridData(SWT.LEFT, SWT.CENTER, false, false, 3,
				1);
		gd_label.widthHint = 352;
		label.setLayoutData(gd_label);

		listViewer = new ListViewer(grpFilters, SWT.BORDER | SWT.V_SCROLL);
		listFilters = listViewer.getList();
		GridData gd_listFilters = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 3);
		gd_listFilters.heightHint = 146;
		gd_listFilters.widthHint = 161;
		listFilters.setLayoutData(gd_listFilters);
		listFilters.addSelectionListener(new SelectionListener() {

			@Override
			public void widgetSelected(SelectionEvent arg0) {
				int index = listFilters.getSelectionIndex();

				DataUploadFilter filter = filters.get(index);
				if (filter != null) {
					outputFilter(filter);
				}

			}

			@Override
			public void widgetDefaultSelected(SelectionEvent arg0) {
				// TODO Auto-generated method stub

			}
		});

		lblFilterType = new Label(grpFilters, SWT.NONE);
		lblFilterType.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblFilterType.setText("Filter type");

		txtOutputFilterType = new Text(grpFilters, SWT.BORDER);
		txtOutputFilterType.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));

		lblFilterValue = new Label(grpFilters, SWT.NONE);
		lblFilterValue.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		lblFilterValue.setText("Filter value\n");

		txtOutputFilterValue = new Text(grpFilters, SWT.BORDER);
		txtOutputFilterValue.setLayoutData(new GridData(SWT.FILL, SWT.CENTER,
				true, false, 1, 1));
		new Label(grpFilters, SWT.NONE);

		btnFilterRemove = new Button(grpFilters, SWT.NONE);
		btnFilterRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int index = listFilters.getSelectionIndex();
				listFilters.remove(index);
				filters.remove(index);

				txtOutputFilterType.setText("");
				txtOutputFilterValue.setText("");
			}
		});
		btnFilterRemove.setLayoutData(new GridData(SWT.RIGHT, SWT.BOTTOM, true,
				false, 1, 1));
		btnFilterRemove.setText("Remove");

		btnUploadData = new Button(composite_3, SWT.NONE);
		btnUploadData.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false,
				false, 1, 1));
		btnUploadData.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {

				java.util.List<WLANFingerprint> fps = (java.util.List<WLANFingerprint>) treeFingerprints
						.getData();

				DataUploadRequest uploadRequest = new DataUploadRequest(fps,
						filters, btnCheckClearData.getEnabled());

				HttpClient.uploadData(uploadRequest);
			}
		});
		btnUploadData.setText("Upload data");

		composite_6 = new Composite(composite_3, SWT.NONE);
		composite_6.setLayout(new GridLayout(1, false));
		GridData gd_composite_6 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_6.widthHint = 365;
		gd_composite_6.heightHint = 33;
		composite_6.setLayoutData(gd_composite_6);

		btnCheckClearData = new Button(composite_6, SWT.CHECK);
		btnCheckClearData.setText("Clear Data");

		btnNewButton = new Button(composite_3, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				treeFingerprints.removeAll();
			}
		});
		btnNewButton.setText("Clear");

		btnBatchDataUpload = new Button(composite_3, SWT.NONE);
		btnBatchDataUpload.addSelectionListener(new SelectionAdapter() {
			@SuppressWarnings("unchecked")
			@Override
			public void widgetSelected(SelectionEvent e) {

				java.util.List<WLANFingerprint> fps = (java.util.List<WLANFingerprint>) treeFingerprints
						.getData();

				DataUploadRequest uploadRequest = new DataUploadRequest(fps,
						filters, btnCheckClearData.getEnabled());

				batchDataUploadRequest.add(uploadRequest);

				listBatchUploadReq.add("DataUploadReq "
						+ batchDataUploadRequest.size());
			}
		});
		btnBatchDataUpload.setText("Save for Batch");

		tbtmBatchTesting = new CTabItem(tabFolder, SWT.NONE);
		tbtmBatchTesting.setText("Batch Testing");

		composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmBatchTesting.setControl(composite_1);
		composite_1.setLayout(new GridLayout(6, false));

		lblNewLabel_2 = new Label(composite_1, SWT.NONE);
		lblNewLabel_2.setText("Positioning Requests");

		lblNewLabel_3 = new Label(composite_1, SWT.NONE);
		lblNewLabel_3.setText("Data Upload Requests");

		label_1 = new Label(composite_1, SWT.SEPARATOR | SWT.VERTICAL);
		GridData gd_label_1 = new GridData(SWT.LEFT, SWT.CENTER, false, false,
				1, 2);
		gd_label_1.heightHint = 193;
		label_1.setLayoutData(gd_label_1);
		new Label(composite_1, SWT.NONE);

		lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		GridData gd_lblNewLabel_4 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_lblNewLabel_4.heightHint = 22;
		lblNewLabel_4.setLayoutData(gd_lblNewLabel_4);
		lblNewLabel_4.setText("Batch Tests\n");

		grpTestOptions = new Group(composite_1, SWT.NONE);
		grpTestOptions.setText("Test Options");
		grpTestOptions.setLayout(new GridLayout(2, false));
		GridData gd_grpTestOptions = new GridData(SWT.LEFT, SWT.TOP, false,
				false, 1, 2);
		gd_grpTestOptions.widthHint = 299;
		gd_grpTestOptions.heightHint = 177;
		grpTestOptions.setLayoutData(gd_grpTestOptions);

		btnCheckButton = new Button(grpTestOptions, SWT.CHECK);
		btnCheckButton.setSelection(true);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnCheckButton.setText("Show accuracy graph");
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);
		new Label(grpTestOptions, SWT.NONE);

		btnRunBatch = new Button(grpTestOptions, SWT.NONE);
		btnRunBatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				java.util.List<Map<WLANFingerprint, PositioningResult>> results = new ArrayList<Map<WLANFingerprint, PositioningResult>>();

				for (BatchPositioning req : batchRequests) {

					if (req.hasUploadRequest()) {

						HttpClient.uploadData(req.getUploadRequest());
					}

					Map<WLANFingerprint, PositioningResult> posRes = new HashMap<WLANFingerprint, PositioningResult>();

					for (WLANFingerprint fp : req.getFps()) {

						PositioningRequest posReq = new PositioningRequest(
								true, fp, req.getAlgo());

						posRes.put(fp, HttpClient.calculatePosition(posReq));
					}

					results.add(posRes);

				}

				displayBatchResults(results);
			}

			private void displayBatchResults(
					java.util.List<Map<WLANFingerprint, PositioningResult>> results) {

				double[] accuracies = new double[results.size()];

				int i = 0;
				for (Map<WLANFingerprint, PositioningResult> res : results) {

					BatchTest test = new BatchTest(res);

					System.out.print(test.getAverageAccuracy() + ", ");

					accuracies[i++] = test.getAverageAccuracy();

					// Output

					textBatchResults.append("Batch Result " + i + " \n");
					textBatchResults.append("\t Nr of samples: " + res.size()
							+ "\n");
					// textBatchResults.append("\t Algorithm used: " +
					// test.getNrOfCorrectPos() + "\n");
					textBatchResults.append("\t Correct positions: "
							+ test.getNrOfCorrectPos() + "\n");
					textBatchResults.append("\t Average accuracy: "
							+ test.getAverageAccuracy() + "\n");
				}
				System.out.println();
				System.out.println("Creating chart");

				Chart chart = new Chart(compositeBatchGraphs, SWT.NONE);

				// set titles
				chart.getTitle().setText("Average Accuracy");
				chart.getAxisSet().getXAxis(0).getTitle().setText("Batch Set");
				chart.getAxisSet().getYAxis(0).getTitle()
						.setText("Average Accuracy");

				// create bar series
				IBarSeries barSeries = (IBarSeries) chart.getSeriesSet()
						.createSeries(SeriesType.BAR, "bar series");
				barSeries.setYSeries(accuracies);

				// adjust the axis range
				chart.getAxisSet().adjustRange();

				compositeGraphTab.redraw();

			}
		});
		btnRunBatch.setText("Run Batch Tests");

		listViewer_1 = new ListViewer(composite_1, SWT.BORDER | SWT.V_SCROLL);
		listBatchPosReqs = listViewer_1.getList();
		GridData gd_listBatchPosReqs = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_listBatchPosReqs.widthHint = 140;
		gd_listBatchPosReqs.heightHint = 166;
		listBatchPosReqs.setLayoutData(gd_listBatchPosReqs);

		listViewer_2 = new ListViewer(composite_1, SWT.BORDER | SWT.V_SCROLL);
		listBatchUploadReq = listViewer_2.getList();
		GridData gd_listBatchUploadReq = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_listBatchUploadReq.widthHint = 140;
		gd_listBatchUploadReq.heightHint = 166;
		listBatchUploadReq.setLayoutData(gd_listBatchUploadReq);

		composite_2 = new Composite(composite_1, SWT.NONE);
		composite_2.setLayout(new GridLayout(1, false));
		GridData gd_composite_2 = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_composite_2.heightHint = 166;
		gd_composite_2.widthHint = 100;
		composite_2.setLayoutData(gd_composite_2);
		new Label(composite_2, SWT.NONE);
		new Label(composite_2, SWT.NONE);

		btnClearBatch = new Button(composite_2, SWT.NONE);
		GridData gd_btnClearBatch = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnClearBatch.heightHint = 25;
		gd_btnClearBatch.widthHint = 90;
		btnClearBatch.setLayoutData(gd_btnClearBatch);
		btnClearBatch.setText("Clear");
		new Label(composite_2, SWT.NONE);

		btnAddBatch = new Button(composite_2, SWT.NONE);
		btnAddBatch.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {

				int i1 = listBatchPosReqs.getSelectionIndex();
				// TODO
				int i2 = listBatchUploadReq.getSelectionIndex();

				BatchPositioning pos = batchPositioningRequests.get(i1);
				// DataUploadRequest req = batchDataUploadRequest.get(i2);

				// pos.setUploadRequest(req);

				batchRequests.add(pos);

				listBatchRequests.add("Batch Test " + batchRequests.size());

			}
		});
		GridData gd_btnAddBatch = new GridData(SWT.LEFT, SWT.CENTER, false,
				false, 1, 1);
		gd_btnAddBatch.heightHint = 25;
		gd_btnAddBatch.widthHint = 90;
		btnAddBatch.setLayoutData(gd_btnAddBatch);
		btnAddBatch.setText("Add\n");

		listViewer_3 = new ListViewer(composite_1, SWT.BORDER | SWT.V_SCROLL);
		listBatchRequests = listViewer_3.getList();
		GridData gd_listBatchRequests = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 1, 1);
		gd_listBatchRequests.widthHint = 140;
		gd_listBatchRequests.heightHint = 166;
		listBatchRequests.setLayoutData(gd_listBatchRequests);

		compositeBatchGraphs = new Composite(composite_1, SWT.NONE);
		compositeBatchGraphs.setLayout(new FillLayout(SWT.HORIZONTAL));
		GridData gd_compositeBatchGraphs = new GridData(SWT.LEFT, SWT.CENTER,
				false, false, 5, 1);
		gd_compositeBatchGraphs.heightHint = 300;
		gd_compositeBatchGraphs.widthHint = 600;
		compositeBatchGraphs.setLayoutData(gd_compositeBatchGraphs);

		textViewer = new TextViewer(composite_1, SWT.BORDER);
		textBatchResults = textViewer.getTextWidget();
		textBatchResults.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true,
				true, 1, 1));
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);
		new Label(composite_1, SWT.NONE);

		tbtmDataStoreAnalysis = new CTabItem(tabFolder, SWT.NONE);
		tbtmDataStoreAnalysis.setText("Data store analysis");

		compositeGraphTab = new Composite(tabFolder, SWT.NONE);
		tbtmDataStoreAnalysis.setControl(compositeGraphTab);

		lblNewLabel_1 = new Label(compositeGraphTab, SWT.NONE);
		lblNewLabel_1.setBounds(10, 10, 127, 27);
		lblNewLabel_1.setText("Analyse data store");

		btnNewButton_1 = new Button(compositeGraphTab, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton_1.setBounds(171, 10, 82, 27);
		btnNewButton_1.setText("Ok");

		styledText = new StyledText(compositeGraphTab, SWT.BORDER);
		styledText
				.setText("Average amount of aps = 6 \nAverage ap level = -80.28951783316492 \nBest level = -41.0\nWorst level = -92.0\n");
		styledText.setBounds(10, 43, 397, 214);

		return container;
	}

	/**
	 * Create the actions.
	 */
	private void createActions() {
		// Create the actions
	}

	/**
	 * Create the menu manager.
	 * 
	 * @return the menu manager
	 */
	@Override
	protected MenuManager createMenuManager() {
		MenuManager menuManager = new MenuManager("menu");
		return menuManager;
	}

	/**
	 * Create the toolbar manager.
	 * 
	 * @return the toolbar manager
	 */
	@Override
	protected ToolBarManager createToolBarManager(int style) {
		ToolBarManager toolBarManager = new ToolBarManager(style);
		return toolBarManager;
	}

	/**
	 * Create the status line manager.
	 * 
	 * @return the status line manager
	 */
	@Override
	protected StatusLineManager createStatusLineManager() {
		StatusLineManager statusLineManager = new StatusLineManager();
		return statusLineManager;
	}

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void main(String args[]) {
		try {
			IpsTool window = new IpsTool();
			window.setBlockOnOpen(true);
			window.open();
			// Display.getCurrent().dispose();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Configure the shell.
	 * 
	 * @param newShell
	 */
	@Override
	protected void configureShell(Shell newShell) {
		newShell.setMinimumSize(new Point(1000, 700));
		super.configureShell(newShell);
		newShell.setText("IPS Tool");
	}

	/**
	 * Return the initial size of the window.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(2000, 1500);
	}
}
