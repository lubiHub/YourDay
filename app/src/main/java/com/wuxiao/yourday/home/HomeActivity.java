package com.wuxiao.yourday.home;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wuxiao.yourday.R;
import com.wuxiao.yourday.calendar.CalendarFragment;
import com.wuxiao.yourday.common.ThemeManager;
import com.wuxiao.yourday.diary.DiaryFragment;
import com.wuxiao.yourday.note.NoteFragment;
import com.wuxiao.yourday.tab.HomeTabLayout;
import com.wuxiao.yourday.tab.OnTabSelectListener;
import com.wuxiao.yourday.viewpager.FragmentViewPager;
import com.wuxiao.yourday.viewpager.adapters.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {


    private String[] mTitles = {"Entries", "Calender", "Diary"};
    private HomeTabLayout home_tl;
    private FragmentViewPager home_vp;
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        home_tl = (HomeTabLayout) findViewById(R.id.home_tl);
        home_vp = (FragmentViewPager) findViewById(R.id.home_vp);

        mFragments.add(NoteFragment.newInstance());
        mFragments.add(CalendarFragment.newInstance());
        mFragments.add(DiaryFragment.newInstance());
        load_home();
    }

    private void load_home() {

        home_vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        home_tl.setIndicatorColor(ThemeManager.getInstance().getThemeColor(this));
        home_tl.setTextUnselectColor(ThemeManager.getInstance().getThemeColor(this));
        home_tl.setDividerColor(ThemeManager.getInstance().getThemeColor(this));

        home_tl.setTabData(mTitles);
        home_tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                home_vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
            }
        });

        home_vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                home_tl.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        home_vp.setCurrentItem(0);
    }

    private class MyPagerAdapter extends FragmentStatePagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment instantiateFragment(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }


    }


    public void setHomeCurrentItem(int position){
        home_vp.setCurrentItem(position);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        home_vp.notifyPagerVisible();
    }

    // *********************************************************************************************
    @Override
    public void onPause() {
        super.onPause();
        home_vp.notifyPagerInvisible();
    }

}
