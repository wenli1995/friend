package com.biyuntian.myapplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore.Images.Media;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

public class TabSettingFragment extends Fragment{

	private static int GALLERY_REQUE_CODE=1;
	private static int CROP_REQUEST_CODE=2;
	private ListView mlistView1;
	private List<String> words1;
	private List<Integer> imageIds1;
	private ImageButton imgBtn;
	private TextView state;
	private File img;
	MyUser currentUser;
	Bitmap bm;
	Bitmap newbm;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.tab3, container, false);
		currentUser=MyUser.getCurrentUser(getActivity(),MyUser.class);
		imgBtn=(ImageButton)view.findViewById(R.id.headImg);
		state=(TextView)view.findViewById(R.id.state);
		mlistView1=(ListView)view.findViewById(R.id.tab3_listView);
		mlistView1.setAdapter(new List1Adapter(getActivity()));
		mlistView1.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					Intent intent=new Intent(getActivity(),LoginActivity.class);
					startActivity(intent);
					break;
				case 1:
					Intent intent1=new Intent(getActivity(),RegisterActivity.class);
					startActivity(intent1);
					break;
				case 2:
					MyUser.logOut(getActivity());   
					//点击“退出”，清除缓存用户对象
					state.setText("未登录");
					imgBtn.setImageResource(R.drawable.head);
					Toast.makeText(getActivity(), "已退出账号", Toast.LENGTH_LONG).show();
					break;
				}
			}
			
		});
		imgBtn.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if(currentUser==null){
					Intent intent=new Intent(getActivity(),LoginActivity.class);
					startActivity(intent);
				}else{
				Intent intent=new Intent(Intent.ACTION_GET_CONTENT);
				intent.setType("image/*");
				startActivityForResult(intent,GALLERY_REQUE_CODE);
				}
			}
			
		});
		if(currentUser!=null){
			state.setText(currentUser.getUsername());
			imgBtn.setImageResource(R.drawable.head);
			if(currentUser.getAvatar()!=null){
				downloadAvatar(currentUser.getAvatar());
			}
		}else{
			state.setText("未登录");
			imgBtn.setImageResource(R.drawable.head);
		}
		fillWords1();
		fillImages1();
		return view;
	}
	
	private void fillImages1() {
		// TODO Auto-generated method stub
		imageIds1=new ArrayList<Integer>();
		imageIds1.add(R.drawable.login);
		imageIds1.add(R.drawable.register);
		imageIds1.add(R.drawable.logout);
	}
	private void fillWords1() {
		// TODO Auto-generated method stub
		words1=new ArrayList<String>();
		words1.add("登录");
		words1.add("注册");
		words1.add("退出");
			
	}
	class List1Adapter extends BaseAdapter{

		private LayoutInflater mInflater1;
		
		private Context mContext1;
		//定义MyAdapter的构造函数
		public List1Adapter(Context context) {
			mContext1 = context;
			mInflater1 = LayoutInflater.from(context);
		}
		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return 3;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			
			final ImageView image1;
			final TextView text1;
			if(convertView==null){
				convertView = mInflater1.inflate(R.layout.tab3_list_item, null);
			}
			image1=(ImageView)convertView.findViewById(R.id.eggImg);
			text1=(TextView)convertView.findViewById(R.id.eggtext);
			text1.setText(words1.get(position).toString());
			image1.setBackgroundResource(imageIds1.get(position));
			return convertView;
		}
		
	}
	//将Bitmap图片保存到SD卡，然后返回的是文件类型的URI
	private Uri savaBitmap(Bitmap bm){
		
		File tmpDir=new File(Environment.getExternalStorageDirectory()+"/com.biyuntian.myapplication");
		if(!tmpDir.exists()){
			tmpDir.mkdir();
		}
		img=new File(tmpDir.getAbsolutePath()+"avatar.png"); //img赋值
		try {
			FileOutputStream fos=new FileOutputStream(img);
			bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
			fos.flush();
			fos.close();
			return Uri.fromFile(img);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}
	
	private Uri convertUri(Uri uri){
		
		InputStream is;
			try {
				is=getActivity().getContentResolver().openInputStream(uri);
				Bitmap bitmap=BitmapFactory.decodeStream(is);
				is.close();
				return savaBitmap(bitmap);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}//获取Uri的输入流
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
		
	}//Uri类型的转换成文件类型的URI
	private void startImageZoom(Uri uri){
		Intent intent=new Intent("com.android.camera.action.CROP");
		intent.setDataAndType(uri, "image/*");
		intent.putExtra("crop", "true");
		intent.putExtra("aspectX", 1);
		intent.putExtra("aspectY", 1);
		intent.putExtra("outputX",100);
		intent.putExtra("outputY",100);
		intent.putExtra("return-data", true);
		startActivityForResult(intent,CROP_REQUEST_CODE);//剪裁图片用的
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if(requestCode==GALLERY_REQUE_CODE){
			if(data==null){
				return;
			}
			Uri uri=data.getData();
			Uri fileUri=convertUri(uri);//返回一个文件类型的Uri
			startImageZoom(uri); //启动剪裁页面
		}else if(requestCode==CROP_REQUEST_CODE){
			if(data==null){
				return;
			}
			Bundle extra=data.getExtras();
			bm=extra.getParcelable("data"); //bm第一次赋值，剪裁后的照片
			imgBtn.setImageBitmap(bm);
			savaBitmap(bm);
			upLoadAvatar();
		}
	}
	private  void downloadAvatar(BmobFile avatarFile) {
		// TODO Auto-generated method stub
		File saveFile = new File(Environment.getExternalStorageDirectory(), avatarFile.getFilename());
		avatarFile.download(getActivity(), saveFile, new DownloadFileListener(){

			@Override
			public void onFailure(int arg0, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "下载头像失败"+msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(String saveFile) {
				// TODO Auto-generated method stub
				bm=BitmapFactory.decodeFile(saveFile);//bm第二次赋值，下载后保存的照片
				Bitmap newbm=bm.createScaledBitmap(bm,100,100,true);
				imgBtn.setImageBitmap(newbm);
				Toast.makeText(getActivity(), "下载成功"+saveFile, Toast.LENGTH_LONG).show();
			}
			
		});
		
	}
	private void upLoadAvatar() {
		// TODO Auto-generated method stub
		final BmobFile avatarFile=new BmobFile(img);  //这里又用到img
		avatarFile.uploadblock(getActivity(), new UploadFileListener(){

			@Override
			public void onFailure(int code, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				ModifyUserInfo();
			
			}

			private void ModifyUserInfo() {
				// TODO Auto-generated method stub
				MyUser newuser=new MyUser();
				newuser.setAvatar(avatarFile);
				MyUser currentUser=BmobUser.getCurrentUser(getActivity(),MyUser.class);
				newuser.update(getActivity(), currentUser.getObjectId(), new UpdateListener(){

					@Override
					public void onFailure(int arg0, String arg1) {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "头像上传失败"+arg1, Toast.LENGTH_LONG).show();
					}

					@Override
					public void onSuccess() {
						// TODO Auto-generated method stub
						Toast.makeText(getActivity(), "头像上传成功", Toast.LENGTH_LONG).show();
						imgBtn.setImageBitmap(bm);
					}
					
				});
			}
			
		});
	}


}
