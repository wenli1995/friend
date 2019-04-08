package com.biyuntian.myapplication;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;

public class MainActivity extends FragmentActivity implements OnClickListener{

	private ViewPager viewPager;
	private FragmentPagerAdapter mAdapter;
	private List<Fragment> mFragments=new ArrayList<Fragment>();
	private ImageButton tabImg1;
	private ImageButton tabImg2;
	private ImageButton tabImg3;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		InitView();
		InitAction();
	
	}
	@SuppressWarnings("deprecation")
	private void InitAction() {
		// TODO Auto-generated method stub
		tabImg1.setOnClickListener(this);
		tabImg2.setOnClickListener(this);
		tabImg3.setOnClickListener(this);
		viewPager.setOnPageChangeListener(new OnPageChangeListener(){

			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void onPageSelected(int arg0) {
				// TODO Auto-generated method stub
				int currentItem = viewPager.getCurrentItem();
				resetImage();
				switch (currentItem){
				case 0:
					tabImg1.setImageResource(R.drawable.home_dark);
					break;
				case 1:
					tabImg2.setImageResource(R.drawable.smile_dark);
					break;
				case 2:
					tabImg3.setImageResource(R.drawable.people_dark);
					break;
				default:
					break;
				}
				
			}
			
		});
			
		
	}
	private void InitView() {
		// TODO Auto-generated method stub
		viewPager=(ViewPager)findViewById(R.id.viewpager);
		tabImg1=(ImageButton)findViewById(R.id.tab1);
		tabImg2=(ImageButton)findViewById(R.id.tab2);
		tabImg3=(ImageButton)findViewById(R.id.tab3);
		
		Fragment tab1Fragment=new TabHomeFragment();
		Fragment tab2Fragment=new TabSmileFragment();
		Fragment tab3Fragment=new TabSettingFragment();
		mFragments.add(tab1Fragment);
		mFragments.add(tab2Fragment);
		mFragments.add(tab3Fragment);
		
		mAdapter=new FragmentPagerAdapter(getSupportFragmentManager()){

			@Override
			public Fragment getItem(int arg0) {
				// TODO Auto-generated method stub
				return mFragments.get(arg0);
			}

			@Override
			public int getCount() {
				// TODO Auto-generated method stub
				return mFragments.size();
			}


		};
		

		viewPager.setAdapter(mAdapter);
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		resetImage();
		switch(v.getId()){
		case R.id.tab1:
			viewPager.setCurrentItem(0);
			tabImg1.setImageResource(R.drawable.home_dark);
			break;
		case R.id.tab2:
			viewPager.setCurrentItem(1);
			tabImg2.setImageResource(R.drawable.smile_dark);
			break;
		case R.id.tab3:
			viewPager.setCurrentItem(2);
			tabImg3.setImageResource(R.drawable.people_dark);
			break;
		default:
			break;
				
		}
	}
	private void resetImage() {
		// TODO Auto-generated method stub
		tabImg1.setImageResource(R.drawable.home_light);
		tabImg2.setImageResource(R.drawable.smile_light);
		tabImg3.setImageResource(R.drawable.people_light);
	}


}
