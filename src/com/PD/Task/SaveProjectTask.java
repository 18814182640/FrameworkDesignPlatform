package com.PD.Task;

import java.io.File;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import com.FD.model.DataModel.DataType;
import com.FP.frame.Config;
import com.FP.frame.Dialog.CommonDialog;
import com.PD.Thread.CellBack;
import com.PD.Thread.QueueRunnable;
import com.PD.Thread.Result;
import com.PD.Tool.FileTool;
import com.PD.Tool.CreateXML;
import com.PD.Tool.ExceptionTool;
import com.PD.Tool.LoadXMLFile;
import com.PD.Tool.ProjectTool;
import com.PD.Tool.XMLConfig;
import com.PD.cache.CacheData;
import com.PD.model.RTEModel;
import com.PD.model.SWCModel;

public class SaveProjectTask extends QueueRunnable {
	private RTEModel rteModel;
	private String path;

	public SaveProjectTask(RTEModel rteModel, String path, CellBack cellBack) {
		this.rteModel = rteModel;
		this.path = path;
		this.setName("���湤��");
		this.setStep(new String[] { "��ʼ����.pro", "�����ļ�·��","��ʼϵ�л�ģ������", "ϵ�л����", "���湤�����", });
		this.setCellBack(cellBack);
		this.setResult(new Result());
	}

	@Override
	public void doTask() {
		try {
			this.setNowStep(1);
			ProjectTool.sendPro(rteModel, path);
			this.setNowStep(2);
			String swcpath = path.substring(0, path.lastIndexOf("\\"))+File.separator+"swc";
			FileTool.isExistPath(swcpath);
			this.setNowStep(3);
			ProjectTool.sendProject(rteModel, swcpath);
			this.getResult().setSuccessful(true);
			this.getResult().setMessage(getStep()[getStep().length-1]);
		} catch (Exception e) {
			this.getResult().setReason(ExceptionTool.getExceptionMessage(e));
		}finally {
			this.setNowStep(4);
		}
		
	}

}
