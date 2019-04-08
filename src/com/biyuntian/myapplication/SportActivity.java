package com.biyuntian.myapplication;

import static com.biyuntian.myapplication.R.id.tv_describe;
import static com.biyuntian.myapplication.R.id.tv_phone;
import static com.biyuntian.myapplication.R.id.tv_time;
import static com.biyuntian.myapplication.R.id.tv_title;
import static com.biyuntian.myapplication.R.id.tv_headImg;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import com.biyuntian.myapplication.Adapter.BaseAdapterHelper;
import com.biyuntian.myapplication.Adapter.QuickAdapter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

public class SportActivity extends Activity{

	public static final int REQUESTCODE_ADD2 = 2;
	private LinearLayout layout_no2;
	private ListView sportView;
	private Button addBtn2;
	private Button backBtn2;
	private QuickAdapter<MySport> SportAdapter;
	private Bitmap bm2;
	MyUser currentUser2;
	SwipeRefreshLayout sw_refresh2;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sport);
		Bmob.initialize(this, "7970100018965bf4d03e474e3a1dfa23");
		currentUser2=MyUser.getCurrentUser(SportActivity.this,MyUser.class);
		sw_refresh2=(SwipeRefreshLayout)findViewById(R.id.sw_refresh2);
		layout_no2=(LinearLayout)findViewById(R.id.layout_no);
		sportView=(ListView)findViewById(R.id.mylist2);
		addBtn2=(Button)findViewById(R.id.add2);
		backBtn2=(Button)findViewById(R.id.back2);
		sw_refresh2.setEnabled(true);
		sw_refresh2.setRefreshing(true);
		sw_refresh2.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				querySport();
			}
			
		});
		addBtn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(SportActivity.this,AddActivity2.class);
				startActivityForResult(intent,REQUESTCODE_ADD2);
			}
			
		});
		backBtn2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
			
		});
		
		initData();
		
		new Thread(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				BmobQuery<MyUser> user= new BmobQuery<MyUser>();
				user.findObjects(SportActivity.this,new FindListener<MyUser>(){

					@Override
					public void onError(int arg0, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(SportActivity.this, "查询失败"+msg, Toast.LENGTH_LONG).show();

					}

					@Override
					public void onSuccess(List<MyUser> users) {
						// TODO Auto-generated method stub
						if(users==null||users.size()==0){
							Toast.makeText(SportActivity.this, "没有数据", Toast.LENGTH_LONG).show();
						}else{
							Iterator<MyUser> it=users.iterator();
							while(it.hasNext()){
								MyUser user=it.next();
								if(user.getAvatar()!=null){
									downloadAvatar(user.getAvatar(),user);
								}
								
						}
							
						}
					}

					private void downloadAvatar(BmobFile avatarFile, MyUser user) {
						// TODO Auto-generated method stub
		
							File saveFile = new File(Environment.getExternalStorageDirectory(), user.getUsername()+".png");
							avatarFile.download(SportActivity.this, saveFile, new DownloadFileListener(){

								@Override
								public void onFailure(int arg0, String msg) {
									// TODO Auto-generated method stub
									Toast.makeText(SportActivity.this, "下载头像失败"+msg, Toast.LENGTH_LONG).show();
								}

								@Override
								public void onSuccess(String saveFile) {
									// TODO Auto-generated method stub
									Toast.makeText(SportActivity.this, "下载成功"+saveFile, Toast.LENGTH_LONG).show();
								}
								
							});
							
						
					}
					
				});
			}
			
		}.start();
		
		
	}
	
	
	
	private void initData() {
		// TODO Auto-generated method stub
	
		if (SportAdapter == null) {
			SportAdapter = new QuickAdapter<MySport>(this, R.layout.item_list) {
				@Override
				protected void convert(BaseAdapterHelper helper, MySport item) {
					String underpath=item.getPhone();
					if(item.getAvatar()!=null){
						bm2=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/"+underpath+".png");
					}else{
						bm2=BitmapFactory.decodeResource(getResources(), R.drawable.head1);
					}
					
					// TODO Auto-generated method stub
				
					helper.setText(tv_title, item.getWhen()+item.getWhere()+item.getWhat())
						.setText(tv_describe, item.getDescribe())
						.setText(tv_time, item.getCreatedAt())
						.setText(tv_phone, item.getPhone())
						.setImageBitmap(tv_headImg, bm2);
						
				}	
			};
		}
		sportView.setAdapter(SportAdapter);
		querySport();
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if(requestCode==REQUESTCODE_ADD2){
			querySport();
		}
	}
	private void querySport() {
		// TODO Auto-generated method stub
		BmobQuery<MySport> query = new BmobQuery<MySport>();
		query.order("-createdAt");// 按照时间降序
		query.findObjects(this, new FindListener<MySport>() {

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				sportView.setVisibility(View.GONE);
				layout_no2.setVisibility(View.VISIBLE);
				Toast.makeText(SportActivity.this, "查询失败"+msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<MySport> travels) {
				// TODO Auto-generated method stub
				SportAdapter.clear();
				if(travels==null||travels.size()==0){
					sportView.setVisibility(View.GONE);
					layout_no2.setVisibility(View.VISIBLE);
					SportAdapter.notifyDataSetChanged();
					return;
				}
				SportAdapter.addAll(travels);
				sportView.setAdapter(SportAdapter);
				sw_refresh2.setRefreshing(false);
			}


			
		});
	}

}
