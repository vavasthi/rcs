package com.avasthi.apps.android.sandtrucks;

public class SandTrucksManagerBackendApiResponse 
{

	private Boolean status_;
	private Integer code_;
	
	static final int SUCCESS = 0x00;
	static final int USER_NOT_FOUND = 0x01;
	static final int USER_NOT_ASSOCIATED = 0x02;
	
	public Boolean getStatus() {
		return status_;
	}

	public void setStatus(Boolean status) {
		this.status_ = status;
	}

	public int getCode() {
		return code_;
	}

	public void setCode(int code) {
		code_ = code;
	}
}