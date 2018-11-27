package com.android.auto.p160scan.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.android.auto.p160scan.R;
import com.android.auto.p160scan.fragment.BarSettingFragment;
import com.android.auto.p160scan.fragment.MyViewPagerAdapter;
import com.android.auto.p160scan.fragment.ScanFragment;
import com.android.auto.p160scan.fragment.SettingFragment;

public class MainViewActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private MyViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.pager);

        if (viewPager != null) {
            setupViewPager(viewPager);
        }
    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new MyViewPagerAdapter(getFragmentManager(), this);
        adapter.addFragment(new ScanFragment(), "扫描");
        adapter.addFragment(new BarSettingFragment(), "常用设置");
        adapter.addFragment(new SettingFragment(), "应用设置");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);
    }

}
