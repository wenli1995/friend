package com.biyuntian.myapplication;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class MyTravel extends BmobObject{
	
	private BmobFile avatar;
	private String when;
	private String where;   
	private String what;
	private String describe; //帖子表述
	private String phone;	//联系电话
	
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}

	public String getWhen() {
		return when;
	}
	public void setWhen(String when) {
		this.when = when;
	}
	public String getWhere() {
		return where;
	}
	public void setWhere(String where) {
		this.where = where;
	}
	public String getWhat() {
		return what;
	}
	public void setWhat(String what) {
		this.what = what;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		this.describe = describe;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
}
