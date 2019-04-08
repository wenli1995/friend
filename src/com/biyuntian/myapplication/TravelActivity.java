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

public class TravelActivity extends Activity{

	public static final int REQUESTCODE_ADD = 1;
	private LinearLayout layout_no;
	private ListView travelView;
	private Button addBtn;
	private Button backBtn;
	private ImageView imghead;
	private QuickAdapter<MyTravel> TravelAdapter;
	private Bitmap bm;
	MyUser currentUser;
	SwipeRefreshLayout sw_refresh;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_travel);
		Bmob.initialize(this, "7970100018965bf4d03e474e3a1dfa23");
		currentUser=MyUser.getCurrentUser(TravelActivity.this,MyUser.class);
		sw_refresh=(SwipeRefreshLayout)findViewById(R.id.sw_refresh1);
		layout_no=(LinearLayout)findViewById(R.id.layout_no);
		travelView=(ListView)findViewById(R.id.mylist1);
		addBtn=(Button)findViewById(R.id.add1);
		backBtn=(Button)findViewById(R.id.back1);
		imghead=(ImageView)findViewById(R.id.tv_headImg);
		sw_refresh.setEnabled(true);
		sw_refresh.setRefreshing(true);
		sw_refresh.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				queryTravel();
			}
			
		});
		addBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(TravelActivity.this,AddActivity.class);
				startActivityForResult(intent,REQUESTCODE_ADD);
			}
			
		});
		backBtn.setOnClickListener(new OnClickListener(){

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
				user.findObjects(TravelActivity.this,new FindListener<MyUser>(){

					@Override
					public void onError(int arg0, String msg) {
						// TODO Auto-generated method stub
						Toast.makeText(TravelActivity.this, "查询失败"+msg, Toast.LENGTH_LONG).show();

					}

					@Override
					public void onSuccess(List<MyUser> users) {
						// TODO Auto-generated method stub
						if(users==null||users.size()==0){
							Toast.makeText(TravelActivity.this, "没有数据", Toast.LENGTH_LONG).show();
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
							avatarFile.download(TravelActivity.this, saveFile, new DownloadFileListener(){

								@Override
								public void onFailure(int arg0, String msg) {
									// TODO Auto-generated method stub
									Toast.makeText(TravelActivity.this, "下载头像失败"+msg, Toast.LENGTH_LONG).show();
								}

								@Override
								public void onSuccess(String saveFile) {
									// TODO Auto-generated method stub
									Toast.makeText(TravelActivity.this, "下载成功"+saveFile, Toast.LENGTH_LONG).show();
								}
								
							});
							
						
					}
					
				});
			}
			
		}.start();
		
		
	}
	
	
	
	private void initData() {
		// TODO Auto-generated method stub
	
		if (TravelAdapter == null) {
			TravelAdapter = new QuickAdapter<MyTravel>(this, R.layout.item_list) {
				@Override
				protected void convert(BaseAdapterHelper helper, MyTravel item) {
					String underpath=item.getPhone();
					if(item.getAvatar()!=null){
						bm=BitmapFactory.decodeFile(Environment.getExternalStorageDirectory()+"/"+underpath+".png");
					}else{
						bm=BitmapFactory.decodeResource(getResources(), R.drawable.head1);
					}
					
					// TODO Auto-generated method stub
				
					helper.setText(tv_title, item.getWhen()+item.getWhere()+item.getWhat())
						.setText(tv_describe, item.getDescribe())
						.setText(tv_time, item.getCreatedAt())
						.setText(tv_phone, item.getPhone())
						.setImageBitmap(tv_headImg, bm);
						
				}	
			};
		}
		travelView.setAdapter(TravelAdapter);
		queryTravel();
		
	}
	
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode != RESULT_OK) {
			return;
		}
		if(requestCode==REQUESTCODE_ADD){
			queryTravel();
		}
	}
	private void queryTravel() {
		// TODO Auto-generated method stub
		BmobQuery<MyTravel> query = new BmobQuery<MyTravel>();
		query.order("-createdAt");// 按照时间降序
		query.findObjects(this, new FindListener<MyTravel>() {

			@Override
			public void onError(int code, String msg) {
				// TODO Auto-generated method stub
				travelView.setVisibility(View.GONE);
				layout_no.setVisibility(View.VISIBLE);
				Toast.makeText(TravelActivity.this, "查询失败"+msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<MyTravel> travels) {
				// TODO Auto-generated method stub
				TravelAdapter.clear();
				if(travels==null||travels.size()==0){
					travelView.setVisibility(View.GONE);
					layout_no.setVisibility(View.VISIBLE);
					TravelAdapter.notifyDataSetChanged();
					return;
				}
				TravelAdapter.addAll(travels);
				travelView.setAdapter(TravelAdapter);
				sw_refresh.setRefreshing(false);
			}


			
		});
	}

}
