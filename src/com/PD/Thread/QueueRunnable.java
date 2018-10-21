package com.PD.Thread;

public abstract class QueueRunnable implements Runnable {
	private String name;
	private String[] Step;
	private int nowStep;
	private CellBack cellBack; // �ص��ӿ�
	private Result result; // ����ִ�н��
	abstract public void doTask();

	@Override
	public void run() {
		doTask();
		if (Step != null) {
			nowStep = Step.length - 1;
		}
		if (this.cellBack != null) {
			cellBack.cellback(result);
		}
	}

	protected String getName() {
		return name;
	}

	protected void setName(String name) {
		this.name = name;
	}

	public String[] getStep() { // ����
		return Step;
	}

	protected void setStep(String[] step) {
		Step = step;
	}

	public int getNowStep() { // ����
		return nowStep;
	}

	protected void setNowStep(int nowStep) {
		this.nowStep = nowStep;
	}

	protected CellBack getCellBack() {
		return cellBack;
	}

	protected void setCellBack(CellBack cellBack) {
		this.cellBack = cellBack;
	}

	protected Result getResult() {
		return result;
	}

	protected void setResult(Result result) {
		this.result = result;
	}

}
