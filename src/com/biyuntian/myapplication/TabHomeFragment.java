package com.biyuntian.myapplication;


import static com.biyuntian.myapplication.R.id.showhead;
import static com.biyuntian.myapplication.R.id.showtitle;
import static com.biyuntian.myapplication.R.id.showcontent;
import static com.biyuntian.myapplication.R.id.showtime;
import java.util.ArrayList;
import java.util.List;

import com.biyuntian.myapplication.Adapter.BaseAdapterHelper;
import com.biyuntian.myapplication.Adapter.QuickAdapter;
import com.biyuntian.myapplication.TabSmileFragment.ListAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class TabHomeFragment extends Fragment{

	private ListView mlistView1;
	private QuickAdapter<show> showAdapter;
	private TextView imgbt1;
	private TextView imgbt2;
	private TextView imgbt3;
	private List<String> words1;
	private List<String> urls;
	private Integer imgId=R.drawable.c1;
	SwipeRefreshLayout sw_refresh2;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.tab1, container, false);
		Bmob.initialize(getActivity(), "7970100018965bf4d03e474e3a1dfa23");
		mlistView1=(ListView)view.findViewById(R.id.tab1_listView);
		sw_refresh2=(SwipeRefreshLayout)view.findViewById(R.id.sw_refresh2);
		imgbt1=(TextView)view.findViewById(R.id.link1);
		imgbt2=(TextView)view.findViewById(R.id.link2);
		imgbt3=(TextView)view.findViewById(R.id.link3);
		initdata();
		initAction();
		fillUrls();
		return view;
	}
	private void initAction() {
		// TODO Auto-generated method stub
		sw_refresh2.setEnabled(true);
		sw_refresh2.setRefreshing(true);
		sw_refresh2.setOnRefreshListener(new OnRefreshListener(){

			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				queryShow();
			}
			
		});
		imgbt1.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(getActivity(),WebViewActivity.class);
				intent.putExtra("url", urls.get(0));
				startActivity(intent);
			}
			
		});
		imgbt2.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent(getActivity(),WebViewActivity.class);
				intent1.putExtra("url", urls.get(1));
				startActivity(intent1);
			}
			
		});
		imgbt3.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent2=new Intent(getActivity(),WebViewActivity.class);
				intent2.putExtra("url", urls.get(2));
				startActivity(intent2);
			}
			
		});
	}
	private void initdata() {
		// TODO Auto-generated method stub
		if (showAdapter == null) {
			showAdapter = new QuickAdapter<show>(getActivity(), R.layout.tab1_list_item) {
				
				@Override
				protected void convert(BaseAdapterHelper helper, show item) {
					// TODO Auto-generated method stub
					if(item.getType().equals("travel")){
						helper.setImageResource(showhead, R.drawable.c1);
					}else if(item.getType().equals("sport")){
						helper.setImageResource(showhead, R.drawable.c2);
					}else if(item.getType().equals("study")){
						helper.setImageResource(showhead, R.drawable.c3);
					}
					helper.setText(showtitle, item.getTitle())
					.setText(showcontent, item.getDescribe())
					.setText(showtime, item.getCreatedAt());
					
				}
				
			};
		}
		mlistView1.setAdapter(showAdapter);
		queryShow();
			
	}
	private void queryShow() {
		// TODO Auto-generated method stub
		BmobQuery<show> query = new BmobQuery<show>();
		query.order("-createdAt");// 按照时间降序
		query.findObjects(getActivity(), new FindListener<show>(){

			@Override
			public void onError(int arg0, String msg) {
				// TODO Auto-generated method stub
				Toast.makeText(getActivity(), "查询失败"+msg, Toast.LENGTH_LONG).show();
			}

			@Override
			public void onSuccess(List<show> shows) {
				// TODO Auto-generated method stub
				showAdapter.clear();
				if(shows==null||shows.size()==0){
					showAdapter.notifyDataSetChanged();
					return;
				}
				showAdapter.addAll(shows);
				mlistView1.setAdapter(showAdapter);
				sw_refresh2.setRefreshing(false);
			}
			
		});

	}

	private void fillUrls() {
		// TODO Auto-generated method stub
		urls=new ArrayList<String>();
		urls.add("http://chanyouji.com/");
		urls.add("http://www.lvye.cn/");
		urls.add("https://ke.qq.com/");
	}

		
	



}
