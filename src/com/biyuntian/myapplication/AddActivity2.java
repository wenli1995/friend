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

public class AddActivity2 extends Activity{

	private EditText whenEt2;
	private EditText whereEt2;
	private EditText whatEt2;
	private EditText describeEt2;
	private Button back2;
	private Button sure2;
	private Button btn2;
	String when2=null;
	String where2=null;
	String what2=null;
	String describe2=null;
	MyUser currentUser;
	List<Integer> dis=new ArrayList<Integer>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add);
		currentUser=MyUser.getCurrentUser(AddActivity2.this,MyUser.class);
		whenEt2=(EditText)findViewById(R.id.edit_when);
		whereEt2=(EditText)findViewById(R.id.edit_where);
		whatEt2=(EditText)findViewById(R.id.edit_what);
		describeEt2=(EditText)findViewById(R.id.edit_describe);
		back2=(Button)findViewById(R.id.back);
		sure2=(Button)findViewById(R.id.sure);
		btn2=(Button)findViewById(R.id.bt);
		initAction();
		
	}//onCreate 

	private void initAction() {
		// TODO Auto-generated method stub
		back2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		btn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				when2=whenEt2.getText().toString();
				where2=whereEt2.getText().toString();
				what2=whatEt2.getText().toString();
				describe2=describeEt2.getText().toString();
				if(TextUtils.isEmpty(when2)){
					Toast.makeText(AddActivity2.this, "ʱ�䲻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(where2)){
					Toast.makeText(AddActivity2.this, "�ص㲻��Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(what2)){
					Toast.makeText(AddActivity2.this, "�����Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(TextUtils.isEmpty(describe2)){
					Toast.makeText(AddActivity2.this, "��������Ϊ��", Toast.LENGTH_SHORT).show();
					return;
				}
				if(currentUser==null){
					Toast.makeText(AddActivity2.this, "���ȵ�¼", Toast.LENGTH_SHORT).show();
				}
				BmobQuery<sort> query=new BmobQuery<sort>();
				query.addQueryKeys("sport");
				query.findObjects(AddActivity2.this,new FindListener<sort>(){

					@Override
					public void onError(int code, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(AddActivity2.this, "��ѯʧ��"+msg, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(List<sort> keys) {
						
						// TODO Auto-generated method stub
						Toast.makeText(AddActivity2.this, "�˶���Ŀ��Ŀ"+keys.size(), Toast.LENGTH_LONG).show();
						for(int i=0;i<keys.size();i++){
							String keyx=keys.get(i).getSport();//��̨�е�һ���˶���Ŀ
							//Toast.makeText(AddActivity2.this, "����Ŀ"+keyx, Toast.LENGTH_SHORT).show();	
							String input=what2;//�û�������˶���Ŀ
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
		sure2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				if(dis.size()==0){
					Toast.makeText(AddActivity2.this, "���ȵ��ȷ����ť", Toast.LENGTH_SHORT).show();
				}else {
				 if(dis.contains(0)||dis.contains(1)){	
					MySport sport=new MySport();
					sport.setAvatar(currentUser.getAvatar());
					sport.setWhen(when2);
					sport.setWhere(where2);
					sport.setWhat(what2);
					sport.setDescribe(describe2);
					sport.setPhone(currentUser.getUsername());
					//����������ݱ����ں�̨
					sport.save(AddActivity2.this, new SaveListener(){

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(AddActivity2.this, "���ʧ��", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							setResult(RESULT_OK);
							Toast.makeText(AddActivity2.this, "�༭������Ŀ"+dis.size(), Toast.LENGTH_SHORT).show();
							dis.clear();
							finish();
						}
						
					});
				}else{
					Toast.makeText(AddActivity2.this, "���������ݲ������˶��������", Toast.LENGTH_SHORT).show();
					dis.clear();
				}
			  }
				
			}//onClick
			
		});//sure.setOnClickListner����
		
	}//initActin����
	

	
}
