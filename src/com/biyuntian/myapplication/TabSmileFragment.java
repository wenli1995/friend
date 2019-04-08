package com.biyuntian.myapplication;


import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class TabSmileFragment extends Fragment{

	private ListView mlistView;
	private List<String> words;
	private List<Integer> imageIds;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view=inflater.inflate(R.layout.tab2, container, false);
		mlistView=(ListView)view.findViewById(R.id.tab2_listView);
		mlistView.setAdapter(new ListAdapter(getActivity()));
		mlistView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				// TODO Auto-generated method stub
				switch(position){
				case 0:
					Intent intent=new Intent(getActivity(),TravelActivity.class);
					startActivity(intent);
					break;
				case 1:
					Intent intent1=new Intent(getActivity(),SportActivity.class);
					startActivity(intent1);
					break;
				case 2:
					Intent intent2=new Intent(getActivity(),StudyActivity.class);
					startActivity(intent2);
					break;
				}
			}
			
		});
		fillWords();
		fillImages();
		return view;
	}
	private void fillImages() {
		// TODO Auto-generated method stub
		imageIds=new ArrayList<Integer>();
		imageIds.add(R.drawable.travel);
		imageIds.add(R.drawable.sport);
		imageIds.add(R.drawable.study);
	}
	private void fillWords() {
		// TODO Auto-generated method stub
		words=new ArrayList<String>();
		words.add("#吃喝玩乐#");
		words.add("#运动健身#");
		words.add("#天天向上#");
	}
	class ListAdapter extends BaseAdapter{

		private LayoutInflater mInflater;
		
		private Context mContext;
		//定义MyAdapter的构造函数
		public ListAdapter(Context context) {
			mContext = context;
			mInflater = LayoutInflater.from(context);
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
			final ImageView image;
			final TextView text;
			if(convertView==null){
				convertView = mInflater.inflate(R.layout.tab2_list_item, null);
			}
			image=(ImageView)convertView.findViewById(R.id.image);
			text=(TextView)convertView.findViewById(R.id.text);
			text.setText(words.get(position).toString());
			image.setBackgroundResource(imageIds.get(position));
			return convertView;
		}
		
	}

}
