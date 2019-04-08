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

public class AddActivity3 extends Activity{

	private EditText whenEt3;
	private EditText whereEt3;
	private EditText whatEt3;
	private EditText describeEt3;
	private Button back3;
	private Button sure3;
	private Button btn3;
	String when3=null;
	String where3=null;
	String what3=null;
	String describe3=null;
	List<Integer> dis=new ArrayList<Integer>();
	MyUser currentUser;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		currentUser=MyUser.getCurrentUser(AddActivity3.this,MyUser.class);
		whenEt3=(EditText)findViewById(R.id.edit_when);
		whereEt3=(EditText)findViewById(R.id.edit_where);
		whatEt3=(EditText)findViewById(R.id.edit_what);
		describeEt3=(EditText)findViewById(R.id.edit_describe);
		back3=(Button)findViewById(R.id.back);
		sure3=(Button)findViewById(R.id.sure);
		btn3=(Button)findViewById(R.id.bt);
		initAction();
		
	}

	private void initAction() {
		// TODO Auto-generated method stub
		back3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		btn3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				when3=whenEt3.getText().toString();
				where3=whereEt3.getText().toString();
				what3=whatEt3.getText().toString();
				describe3=describeEt3.getText().toString();
				if(TextUtils.isEmpty(when3)){
					Toast.makeText(AddActivity3.this, "ʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(where3)){
					Toast.makeText(AddActivity3.this, "�ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(what3)){
					Toast.makeText(AddActivity3.this, "�����Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(describe3)){
					Toast.makeText(AddActivity3.this, "��������Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(currentUser==null){
					Toast.makeText(AddActivity3.this, "���ȵ�¼", Toast.LENGTH_SHORT).show();
				}
				BmobQuery<sort> query=new BmobQuery<sort>();
				query.addQueryKeys("sduty");
				query.findObjects(AddActivity3.this,new FindListener<sort>(){

					@Override
					public void onError(int code, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(AddActivity3.this, "��ѯʧ��"+msg, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(List<sort> keys) {
						
						// TODO Auto-generated method stub
						Toast.makeText(AddActivity3.this, "ѧϰ��Ŀ��Ŀ"+keys.size(), Toast.LENGTH_LONG).show();
						for(int i=0;i<keys.size();i++){
							String keyx=keys.get(i).getSduty();//��̨�е�һ���˶���Ŀ
							//Toast.makeText(AddActivity2.this, "����Ŀ"+keyx, Toast.LENGTH_SHORT).show();	
							String input=what3;//�û�������˶���Ŀ
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
		sure3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(dis.size()==0){
					Toast.makeText(AddActivity3.this, "���ȵ��ȷ����ť", Toast.LENGTH_SHORT).show();
				}else{
				  if(dis.contains(0)||dis.contains(1)){
					MyStudy study=new MyStudy();
					study.setAvatar(currentUser.getAvatar());
					study.setWhen(when3);
					study.setWhere(where3);
					study.setWhat(what3);
					study.setDescribe(describe3);
					study.setPhone(currentUser.getUsername());
					study.save(AddActivity3.this, new SaveListener(){

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(AddActivity3.this, "���ʧ��", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							setResult(RESULT_OK);
							Toast.makeText(AddActivity3.this, "�༭������Ŀ"+dis.size(), Toast.LENGTH_SHORT).show();
							dis.clear();
							finish();
						}
						
					});
					}else{
						Toast.makeText(AddActivity3.this, "���������ݲ������������Ϸ���", Toast.LENGTH_SHORT).show();
						dis.clear();
					}
				}
				
			}
			
		});
	}
	
}
