package com.PD.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSource;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.TextTransfer;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.wb.swt.SWTResourceManager;

import com.FP.frame.Config;
import com.FP.frame.Dialog.RTEDesignDialog;
import com.sun.org.glassfish.gmbal.Description;

public class RTEModel extends Composite {

	private Button rteBut;
	private Composite parent;
	private Shell shellMain;
	private List<SWCModel> swcModelList; // ����swc
	private List<ModuleModel> moduleModels; 

	public RTEModel(Shell shellMain, Composite parent, int style) {
		super(parent, style);
		this.shellMain = shellMain;
		this.parent = parent;
		this.setSize(Config.RTCWidth, Config.RTCHeight);
		swcModelList = new ArrayList<>();
		moduleModels = new ArrayList<>();
	}

	public void open() {
		final RTEModel rteModel = this;
		rteBut = new Button(this, SWT.NONE);
		rteBut.setBounds(0, 0, Config.RTCWidth, Config.RTCHeight);
		rteBut.setText("RTE");
		rteBut.setImage(new Image(shellMain.getDisplay(), Config.RTEImagePath));
		rteBut.setBackground(SWTResourceManager.getColor(50, 205, 50));
		rteBut.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseDoubleClick(MouseEvent e) {
				openDesignDialog();
			}
		});
		DragSource dSource = new DragSource(rteBut, DND.DROP_MOVE);
		dSource.setTransfer(new Transfer[] { TextTransfer.getInstance() });
		dSource.addDragListener(new DragSourceAdapter() {

			@Override
			public void dragStart(DragSourceEvent event) {
				parent.redraw();
			}

			@Override
			public void dragSetData(DragSourceEvent event) {
				int x = Display.getCurrent().getCursorLocation().x - shellMain.getBounds().x
						- shellMain.getBorderWidth() * 2 - 20;
				int y = Display.getCurrent().getCursorLocation().y - shellMain.getBounds().y
						- (shellMain.getBounds().height - parent.getSize().y) - shellMain.getBorderWidth() * 2;
				rteModel.setLocation(x - Config.RTCWidth / 2, y);
				event.data = Config.MOVE + ":" + Config.RTE + ":" + rteBut.getText();
			}

			@Override
			public void dragFinished(DragSourceEvent event) {
				parent.redraw();
				drawLineALL();

			}
		});

		Menu menu = new Menu(rteBut);
		rteBut.setMenu(menu);

		MenuItem mntmNewItem = new MenuItem(menu, SWT.NONE);
		mntmNewItem.setText("\u7F16\u8F91(E)");
		mntmNewItem.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				openDesignDialog();
			}
		});

	}

	@Description("����������ӵ�RTE��Module")
	public List<ModuleModel> getModuleModels() {
		return moduleModels;
	}

	@Description("������ô��ڵ�SWC�������������swc������ʱ��Ӧ����ӵ�rte")
	public void addSwcModel(SWCModel swcModel) {
		if (swcModelList != null) {
			swcModelList.add(swcModel);
		}
	}

	@Description("������ô��ڵ�SWC�����������롣swc���ٵ�ʱ��Ӧ�ô�rte�Ƴ�")
	public void removeSwcModel(SWCModel swcModel) {
		if (swcModelList != null) {
			swcModelList.remove(swcModel);
		}
	}

	public List<SWCModel> getSwcModelList() {
		return swcModelList;
	}

	public void drawLineALL() { // �ػ�
		GC gc = new GC(parent);
		for (int i = 1; i < parent.getChildren().length; i++) {
			SWCModel swcModel = (SWCModel) parent.getChildren()[i];
			if (swcModel.isLineToRTE()) {
				gc.drawLine(swcModel.getLocation().x + Config.SWCWidth / 2,
						swcModel.getLocation().y + Config.SWCHeight / 2, this.getLocation().x + Config.RTCWidth / 2,
						this.getLocation().y + Config.RTCHeight / 2);
			}
		}
		parent.layout();
		gc.dispose();
	}

	private void openDesignDialog() {
		RTEDesignDialog rteDesignDialog = new RTEDesignDialog(shellMain, moduleModels, swcModelList);
		rteDesignDialog.open();
		refreshSWC();
	}

	@Description("����:1.���¼���BSW�ļ� ʱ ;2.SWCɾ���ź�ʱ;3.ȡ������RTEʱ��4.ɾ��SWCʱ")
	public void refreshSWC() {
		if (swcModelList != null) {
			for (int i = 0; i < swcModelList.size(); i++) {
				SWCModel swcModel = swcModelList.get(i);
				if (swcModel != null) {
					checkParent(swcModel, moduleModels);
				}
			}
		}
	}

	private void checkParent(SWCModel swcModel, List<ModuleModel> moduleModels) {
		if (swcModel != null && moduleModels != null) {
			ModuleModel moduleModel = swcModel.getSwcData();
			if (moduleModel != null && moduleModel.getDataModels() != null) {
				for (int j = 0; j < moduleModel.getDataModels().size(); j++) {
					DataModel dataModel = moduleModel.getDataModels().get(j);
					if (dataModel != null) {
						switch (dataModel.getFalg()) {
						case Config.ADD:
							if (!hasParent(moduleModels, dataModel)) {
								dataModel.setFalg(Config.LOST);
							}
							break;
						case Config.LOST:
							if (hasParent(moduleModels, dataModel)) {
								dataModel.setFalg(Config.ADD);
							}
							break;
						default:
							break;
						}
					}
				}
			}
		}
	}

	private boolean hasParent(List<ModuleModel> moduleModels, DataModel otherdata) {
		if (moduleModels != null && otherdata != null) {
			for (int i = 0; i < moduleModels.size(); i++) {
				ModuleModel moduleModel = moduleModels.get(i);
				if (moduleModel != null && moduleModel.getDataModels() != null) {
					for (int j = 0; j < moduleModel.getDataModels().size(); j++) {
						DataModel dataModel = moduleModel.getDataModels().get(j);
						if (dataModel != null) {
							if (isADDTO(otherdata, dataModel)) { // copy
								return true;
							}
						}
					}
				}
			}
		} else {
			return false;
		}
		return false;
	}

	private boolean isADDTO(DataModel otherDataModel, DataModel dataModel) {
		if (otherDataModel != null && dataModel != null) {
			if (otherDataModel.getType().equals(dataModel.getType())) {
				for (int i = 0; i < otherDataModel.getArributeNum(); i++) {
					String othervaule = otherDataModel.getAttributeValue(otherDataModel.getAttributeNames()[i]) + "";
					String vaule = dataModel.getAttributeValue(dataModel.getAttributeNames()[i]) + "";
					switch (i) {
					case 1:
						if (othervaule.equals(vaule) || !isIsomerism(othervaule, vaule)) {
							return false;
						}
						break;
					case 2:
						if (othervaule.equals(vaule) || !isIsomerism(othervaule, vaule)) {
							return false;
						}
						break;
					default:
						if (!othervaule.equals(vaule)) {
							return false;
						}
						break;
					}
				}
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	private boolean isIsomerism(String a, String b) {
		if (a != null && b != null) {
			if (a.equals("P-Port") && b.equals("R-Port") || a.equals("R-Port") && b.equals("P-Port")) {
				return true;
			} else if (a.equals("Service") && b.equals("Client") || a.equals("Client") && b.equals("Service")) {
				return true;
			} else if (a.equals("Send") && b.equals("Receive") || a.equals("Receive") && b.equals("Send")) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}

}
