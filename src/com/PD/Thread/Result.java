package com.PD.Thread;

public class Result {

	private boolean isSuccessful;
	private String message; // ���ص���Ϣ
	private String reason; // ���ص�ʧ��ԭ��

	public boolean isSuccessful() {
		return isSuccessful;
	}

	public void setSuccessful(boolean isSuccessful) {
		this.isSuccessful = isSuccessful;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

}
