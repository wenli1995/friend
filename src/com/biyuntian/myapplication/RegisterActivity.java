package com.biyuntian.myapplication;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.RequestSMSCodeListener;
import cn.bmob.v3.listener.SaveListener;
public class RegisterActivity extends Activity{

	private EditText phoneNumber2;
	private EditText verification;
	private EditText password2;
	private Button register;
	private Button get;
	String phoneNum2=null;
	String verifyNum2=null;
	String passwordNum2=null;
	MyCountTimer mTimer;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		Bmob.initialize(this, "7970100018965bf4d03e474e3a1dfa23");
		phoneNumber2=(EditText)findViewById(R.id.mobilePhone2);
		verification=(EditText)findViewById(R.id.vertifyNum);
		password2=(EditText)findViewById(R.id.password2);
		get=(Button)findViewById(R.id.get);
		register=(Button)findViewById(R.id.registerBt);
		//���ڼ���EditText�Ƿ������룬�������ð�ť�ɵ��
		phoneNumber2.addTextChangedListener(new MyTextWatcher());
		verification.addTextChangedListener(new MyTextWatcher());
		password2.addTextChangedListener(new MyTextWatcher());
	}
	class MyTextWatcher implements TextWatcher{

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			// TODO Auto-generated method stub
			if(phoneNumber2.length()!=0){
				get.setClickable(true);
			}
			if(phoneNumber2.length()!=0&&password2.length()!=0&&verification.length()!=0){
				register.setClickable(true);
			}
		}

		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
		
	};
	public void register(View view){
		
		phoneNum2=phoneNumber2.getText().toString();
		verifyNum2=verification.getText().toString();
		passwordNum2=password2.getText().toString();
		final MyUser user=new MyUser();
		user.setUsername(phoneNum2);;
		user.setPassword(passwordNum2);
		user.signUp(RegisterActivity.this, new SaveListener(){
			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(RegisterActivity.this, "ע��ʧ��:"+msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				Toast.makeText(RegisterActivity.this, "ע��ɹ�", Toast.LENGTH_LONG).show();
				Intent intent=new Intent(RegisterActivity.this,MainActivity.class);
				//����ָ��Activity���ȴ����ؽ��
				startActivity(intent);
				finish();
			}
			
		});

		
	}

   //��Ա�ڲ��࣬Android�е���CountDowmTimer
	class MyCountTimer extends CountDownTimer{  
		  
        public MyCountTimer(long millisInFuture, long countDownInterval) {  
            super(millisInFuture, countDownInterval);  
        }  
		@Override  
        public void onTick(long millisUntilFinished) {  
            get.setText((millisUntilFinished / 1000) +"����ط�");  
        }  
        @Override  
        public void onFinish() {  
        	get.setText("���·�����֤��");  
        }  
    }
	//ֱ�Ӱ󶨵�Button��ǩ���¼���������������ȡ��֤��
	public void get(View view){
		
		phoneNum2=phoneNumber2.getText().toString();
		if(!TextUtils.isEmpty(phoneNum2)){			
		    mTimer=new MyCountTimer(60000,1000);
			mTimer.start();   
			BmobSMS.requestSMSCode(RegisterActivity.this, phoneNum2, "��֤��", new RequestSMSCodeListener(){

				@Override
				public void done(Integer smsId, BmobException ex) {
					// TODO Auto-generated method stub
					if(ex==null){
						Toast.makeText(RegisterActivity.this, "��֤�뷢�ͳɹ�", Toast.LENGTH_LONG).show();
					}else{
						mTimer.cancel();
					}
				}

		
				
			});
		 }
	}	
		
}
