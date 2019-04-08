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

public class StudyActivity extends Activity{

	public static final int REQUESTCODE_ADD3 = 3;
	private LinearLayout layout_no;
	private ListView studyView;
	private Button addBtn3;
	private Button backBtn3;
	private QuickAdapter<MyStudy> studyAdapter;
	private Bitmap bm3;
	MyUser currentUser3;
	SwipeRefreshLayout sw_refresh3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_study);
		Bmob.initialize(this, "7970100018965bf4d03e474e3a1dfa23");
		currentUser3=MyUser.getCurrentUser(StudyActivity.this,MyUser.class);
		sw_refresh3=(SwipeRefreshLayout)findViewById(R.id.sw_refresh3);
		layout_no=(LinearLayout)findViewById(R.id.layout_no);
		studyView=(ListView)findViewById(R.id.mylist3);
		addBtn3=(Button)findViewById(R.id.add3);
		backBtn3=(Button)findViewById(R.id.back3);
		sw_refresh3.setEnabled(true);
		sw_refresh3.setRefreshing(true);
		sw_refresh3.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				queryStudy();
			}
			
		});
		addBtn3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(StudyActivity.this,AddActivity3.class);
				startActivityForResult(intent,REQUESTCODE_ADD3);
			}
			
		});
		backBtn3.setOnClickListener(new OnClickListener(){

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
				user.findObjects(StudyActivity.this,new FindListener<MyUser>(){

					@Override
					public void onError(int arg0, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(StudyActivity.this, "查询失败"+msg, Toast.LENGTH_LONG).show();

					}

					@Override
					public void onSuccess(List<MyUser> users) {
						// TODO Auto-generated method stub
						if(users==null||users.size()==0){
							Toast.makeText(StudyActivity.this, "没有数据", Toast.LENGTH_LONG).show();
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
							avatarFile.download(StudyActivity.this, saveFile, new DownloadFileListener(){

								@Override
								public void onFailure(int arg0, String msg) {
									// TODO Auto-generated method stub
									Toast.makeText(StudyActivity.this, "下载头像失败"+msg, Toast.LENGTH_LONG).show();
								}

								@Override
								public void onSuccess(String saveFile) {
									// TODO Auto-generated method stub
									Toast.makeText(StudyActivity.this, "下载成功"+saveFile, Toast.LENGTH_LONG).show();
								}
								
							});
							
						
					}
					
				});
			}
			
		}.start();
		
		
	}
	
	
	
	private void initData() {
		// TODO Auto-generated method stub
	
		if (studyAdapter == null) {
			studyAdapter = new QuickAdapter<MyStudy>(this, R.layout.item_list) {
				@Override
				protected void convert(BaseAdapterHelper helper, MyStudy item) {
					String underpath=item.getPhone();
					if(item.getAvatar()!=null){
						bm3=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/"+underpath+".png");
					}else{
						bm3=BitmapFactory.decodeResource(getResources(), R.drawable.head1);
					}
					
					// TODO Auto-generated method stub
				
					helper.setText(tv_title, item.getWhen()+item.getWhere()+item.getWhat())
						.setText(tv_describe, item.getDescribe())
						.setText(tv_time, item.getCreatedAt())
						.setText(tv_phone, item.getPhone())
						.setImageBitmap(tv_headImg, bm3);
						
				}	
			};
		}
		studyView.setAdapter(studyAdapter);
		queryStudy();
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if(requestCode==REQUESTCODE_ADD3){
			queryStudy();
		
		}
	}
	private void queryStudy() {
		// TODO Auto-generated method stub
		BmobQuery<MyStudy> query = new BmobQuery<MyStudy>();
		query.order("-createdAt");// 按照时间降序
		query.findObjects(this, new FindListener<MyStudy>() {

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				studyView.setVisibility(View.GONE);
				layout_no.setVisibility(View.VISIBLE);
				Toast.makeText(StudyActivity.this, "查询失败"+msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<MyStudy> travels) {
				// TODO Auto-generated method stub
				studyAdapter.clear();
				if(travels==null||travels.size()==0){
					studyView.setVisibility(View.GONE);
					layout_no.setVisibility(View.VISIBLE);
					studyAdapter.notifyDataSetChanged();
					return;
				}
				studyAdapter.addAll(travels);
				studyView.setAdapter(studyAdapter);
				sw_refresh3.setRefreshing(false);
			}


			
		});
	}

}
