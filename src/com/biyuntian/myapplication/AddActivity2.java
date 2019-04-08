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
					Toast.makeText(AddActivity2.this, "时间不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(where2)){
					Toast.makeText(AddActivity2.this, "地点不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(TextUtils.isEmpty(what2)){
					Toast.makeText(AddActivity2.this, "活动不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				
				if(TextUtils.isEmpty(describe2)){
					Toast.makeText(AddActivity2.this, "描述不能为空", Toast.LENGTH_SHORT).show();
					return;
				}
				if(currentUser==null){
					Toast.makeText(AddActivity2.this, "请先登录", Toast.LENGTH_SHORT).show();
				}
				BmobQuery<sort> query=new BmobQuery<sort>();
				query.addQueryKeys("sport");
				query.findObjects(AddActivity2.this,new FindListener<sort>(){

					@Override
					public void onError(int code, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(AddActivity2.this, "查询失败"+msg, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess(List<sort> keys) {
						
						// TODO Auto-generated method stub
						Toast.makeText(AddActivity2.this, "运动项目数目"+keys.size(), Toast.LENGTH_LONG).show();
						for(int i=0;i<keys.size();i++){
							String keyx=keys.get(i).getSport();//后台中的一个运动条目
							//Toast.makeText(AddActivity2.this, "本项目"+keyx, Toast.LENGTH_SHORT).show();	
							String input=what2;//用户输入的运动条目
							Integer dps=distance(keyx,input);
							//Toast.makeText(AddActivity2.this, "编辑距离"+dps, Toast.LENGTH_SHORT).show();
							dis.add(dps);
						
							
						}	
					}

					private int distance(String keyx, String input) {
						// TODO Auto-generated method stub
						char[] keyxChar=keyx.toCharArray();
						char[] inputChar=input.toCharArray();//将中文字符串分隔成单个中文字符
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
					        	
					            if(keyxChar[n-1]==inputChar[m-1]){ ///相等时
					                dp[n][m]=dp[n-1][m-1];
					            }
					            else{
					            	/*
					            	 * 修改的地方
					            	 * 动态规化的公式:
					            	 * 			f(i-1,j-1)         						a[i]=b[j]
					            	 * f(i,j)=
					            	 * 			1+min(f(i,j-1),f(i-1,j),f(i-1,j-1))		a[i]!=b[j]
					            	 */
					                dp[n][m]=1+min(dp[n-1][m],dp[n][m-1],dp[n-1][m-1]); ///不相等时，取三种可能操作的最小数值+1
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
					Toast.makeText(AddActivity2.this, "请先点击确定按钮", Toast.LENGTH_SHORT).show();
				}else {
				 if(dis.contains(0)||dis.contains(1)){	
					MySport sport=new MySport();
					sport.setAvatar(currentUser.getAvatar());
					sport.setWhen(when2);
					sport.setWhere(where2);
					sport.setWhat(what2);
					sport.setDescribe(describe2);
					sport.setPhone(currentUser.getUsername());
					//将输入的内容保存在后台
					sport.save(AddActivity2.this, new SaveListener(){

						@Override
						public void onFailure(int arg0, String arg1) {
							// TODO Auto-generated method stub
							Toast.makeText(AddActivity2.this, "添加失败", Toast.LENGTH_SHORT).show();
						}

						@Override
						public void onSuccess() {
							// TODO Auto-generated method stub
							setResult(RESULT_OK);
							Toast.makeText(AddActivity2.this, "编辑距离数目"+dis.size(), Toast.LENGTH_SHORT).show();
							dis.clear();
							finish();
						}
						
					});
				}else{
					Toast.makeText(AddActivity2.this, "发布的内容不属于运动健身分类", Toast.LENGTH_SHORT).show();
					dis.clear();
				}
			  }
				
			}//onClick
			
		});//sure.setOnClickListner结束
		
	}//initActin结束
	

	
}
