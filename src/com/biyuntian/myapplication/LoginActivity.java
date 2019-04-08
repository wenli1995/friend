package com.biyuntian.myapplication;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.listener.SaveListener;


public class LoginActivity extends Activity{

	private EditText phoneNumber;
	private EditText password;
	private Button login;
	String phoneNum=null;
	String passwordNum=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		Bmob.initialize(this,"7970100018965bf4d03e474e3a1dfa23");
		phoneNumber=(EditText)findViewById(R.id.mobilePhone);
		password=(EditText)findViewById(R.id.password);
		login=(Button)findViewById(R.id.loginBt);
		//用于监听EditText是否有输入，有则设置按钮可点击
		phoneNumber.addTextChangedListener(new MyTextWatcher());
		password.addTextChangedListener(new MyTextWatcher());
	}
	class MyTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			//当用户名和密码都不为空是，登录按钮可接受单击
			if(phoneNumber.length()!=0&&password.length()!=0){
				login.setClickable(true);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	};
	//登录按钮直接绑定到标签的事件监听方法
	public void goLogin(View view){
		phoneNum=phoneNumber.getText().toString();
		passwordNum=password.getText().toString();
		final ProgressDialog pd=new ProgressDialog(LoginActivity.this);
		pd.setMessage("登录中");
		pd.setCanceledOnTouchOutside(false);
		pd.show();
		//参考BMob文档的登录过程
		final MyUser muser = new MyUser();
		muser.setUsername(phoneNum);
		muser.setPassword(passwordNum);
		muser.login(LoginActivity.this, new SaveListener(){
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				pd.dismiss();
				Toast.makeText(LoginActivity.this, "请核对手机号密码并检查网络设置"+msg, Toast.LENGTH_SHORT).show();
				
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				pd.dismiss();
				Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
				Intent intent=new Intent(LoginActivity.this,MainActivity.class);
				startActivity(intent);
				finish();
			}
			
		});
}
}
