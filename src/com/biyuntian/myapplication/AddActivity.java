package com.biyuntian.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

public class AddActivity extends Activity{

	private EditText whenEt;
	private EditText whereEt;
	private EditText whatEt;
	private EditText describeEt;
	private Button back;
	private Button sure;
	private Button btn;
	String when=null;
	String where=null;
	String what=null;
	String describe=null;
	MyUser currentUser;
	List<Integer> dis=new ArrayList<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		currentUser=MyUser.getCurrentUser(AddActivity.this,MyUser.class);
		whenEt=(EditText)findViewById(R.id.edit_when);
		whereEt=(EditText)findViewById(R.id.edit_where);
		whatEt=(EditText)findViewById(R.id.edit_what);
		describeEt=(EditText)findViewById(R.id.edit_describe);
		back=(Button)findViewById(R.id.back);
		sure=(Button)findViewById(R.id.sure);
		btn=(Button)findViewById(R.id.bt);
		initAction();
		
	}

	private void initAction() {
		// TODO Auto-generated method stub
		back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
	btn.setOnClickListener(new OnClickListener(){

		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			when=whenEt.getText().toString();
			where=whereEt.getText().toString();
			what=whatEt.getText().toString();
			describe=describeEt.getText().toString();
			if(TextUtils.isEmpty(when)){
				Toast.makeText(AddActivity.this, "ʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(where)){
				Toast.makeText(AddActivity.this, "�ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(what)){
				Toast.makeText(AddActivity.this, "�����Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if(TextUtils.isEmpty(describe)){
				Toast.makeText(AddActivity.this, "��������Ϊ��", Toast.LENGTH_SHORT).show();
				return;
			}
			if(currentUser==null){
				Toast.makeText(AddActivity.this, "���ȵ�¼", Toast.LENGTH_SHORT).show();
			}
			BmobQuery<sort> query=new BmobQuery<sort>();
			query.addQueryKeys("travel");
			query.findObjects(AddActivity.this,new FindListener<sort>(){

				@Override
				public void onError(int code, String msg) {
					// TODO Auto-generated method stub
					Toast.makeText(AddActivity.this, "��ѯʧ��"+msg, Toast.LENGTH_LONG).show();
				}

				@Override
				public void onSuccess(List<sort> keys) {
					
					// TODO Auto-generated method stub
					Toast.makeText(AddActivity.this, "�Ժ�������Ŀ��Ŀ"+keys.size(), Toast.LENGTH_LONG).show();
					for(int i=0;i<keys.size();i++){
						String keyx=keys.get(i).getTravel();
						//Toast.makeText(AddActivity2.this, "����Ŀ"+keyx, Toast.LENGTH_SHORT).show();	
						String input=what;//�û�������˶���Ŀ
						Integer dps=distance(keyx,input);
						//Toast.makeText(AddActivity2.this, "�༭����"+dps, Toast.LENGTH_SHORT).show();
						dis.add(dps);
					
						
					}	
				}

				private int distance(String keyx, String input) {
					// TODO Auto-generated method stub
					char[] keyxChar=keyx.toCharArray();
					char[] inputChar=input.toCharArray();//�������ַ����ָ��ɵ��������ַ�
					int len_keyx=keyxChar.length;
					int len_input=inputChar.length;
					int dp[][]=new int[len_keyx+1][len_input+1];
					for(int i=0;i<=len_keyx;i++){
						for(int j=0;j<=len_input;j++){
							dp[i][j]=Integer.MAX_VALUE;
						}
					}
					for(int k=0;k<=len_input;k++)
				        dp[0][k]=k;
				    for(int j=0;j<=len_keyx;j++)  
				        dp[j][0]=j;
				    for(int n=1;n<=len_keyx;n++)
				    {
				        for(int m=1;m<=len_input;m++)
				        {
				        	
				            if(keyxChar[n-1]==inputChar[m-1]){ ///���ʱ
				                dp[n][m]=dp[n-1][m-1];
				            }
				            else{
				            	/*
				            	 * �޸ĵĵط�
				            	 * ��̬�滯�Ĺ�ʽ:
				            	 * 			f(i-1,j-1)         						a[i]=b[j]
				            	 * f(i,j)=
				            	 * 			1+min(f(i,j-1),f(i-1,j),f(i-1,j-1))		a[i]!=b[j]
				            	 */
				                dp[n][m]=1+min(dp[n-1][m],dp[n][m-1],dp[n-1][m-1]); ///�����ʱ��ȡ���ֿ��ܲ�������С��ֵ+1
				            }
				            
				        	
				        }
				    }
				   
				    return dp[len_keyx][len_input];
				}

				private int min(int i, int j, int k) {
					int min;
				    min=i<j?i:j;
				    min=min<k?min:k;
				    return min;
				}

				
			});
		}
		
	});
		sure.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				if(dis.size()==0){
					Toast.makeText(AddActivity.this, "���ȵ��ȷ����ť", Toast.LENGTH_SHORT).show();
				}else {
				  if(dis.contains(0)||dis.contains(1)){
					MyTravel travel=new MyTravel();
					travel.setAvatar(currentUser.getAvatar());
					travel.setWhen(when);
					travel.setWhere(where);
					travel.setWhat(what);
					travel.setDescribe(describe);
					travel.setPhone(currentUser.getUsername());
					travel.save(AddActivity.this, new SaveListener(){

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(AddActivity.this, "���ʧ��", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							setResult(RESULT_OK);
							Toast.makeText(AddActivity.this, "�༭������Ŀ"+dis.size(), Toast.LENGTH_SHORT).show();
							dis.clear();	
							finish();
						}
						
					});
				  }else{
					  Toast.makeText(AddActivity.this, "���������ݲ����ڳԺ����ַ���", Toast.LENGTH_SHORT).show();
					  dis.clear();
				  }
				  
				}
				
			}
			
		});
	}
	
}
