package com.biyuntian.myapplication;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;

public class MyUser extends BmobUser{
	
	private String dearName;
	private Boolean sex=true;
	private String hobby;
	private String signup;
	private BmobFile avatar;
	
	public MyUser(){
		
	}
	
	public MyUser(BmobFile avatar){
		this.avatar=avatar;
	}
	public BmobFile getAvatar() {
		return avatar;
	}
	public void setAvatar(BmobFile avatar) {
		this.avatar = avatar;
	}
	public String getDearName() {
		return dearName;
	}
	public void setDearName(String dearName) {
		this.dearName = dearName;
	}

	public Boolean getSex() {
		return sex;
	}
	public void setSex(Boolean sex) {
		this.sex = sex;
	}
	public String getHobby() {
		return hobby;
	}
	public void setHobby(String hobby) {
		this.hobby = hobby;
	}
	public String getSignup() {
		return signup;
	}
	public void setSignup(String signup) {
		this.signup = signup;
	}
	
	
}
