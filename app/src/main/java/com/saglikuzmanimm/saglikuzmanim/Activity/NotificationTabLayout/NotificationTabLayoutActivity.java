package com.saglikuzmanimm.saglikuzmanim.Activity.NotificationTabLayout;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.saglikuzmanimm.saglikuzmanim.Fragment.NotificationFragment.DisplayNotificationAppointmentFragment;
import com.saglikuzmanimm.saglikuzmanim.Fragment.NotificationFragment.DisplayNotificationPaymentFragment;
import com.saglikuzmanimm.saglikuzmanim.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class NotificationTabLayoutActivity extends AppCompatActivity {
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private final String tab1 = "Randevu Bildirimleri";
    private final String tab2 = "Ödeme Bildirimleri";

    private String whichIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_tab_layout);

        tabLayout = findViewById(R.id.tab_layout_notification_activity);
        viewPager = findViewById(R.id.viewPager_notification_activity);

        Intent intent = getIntent();
        whichIntent = intent.getStringExtra("type");

        ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add(tab1);
        arrayList.add(tab2);

        prepareViewPager(viewPager, arrayList);
        tabLayout.setupWithViewPager(viewPager);

    }

    private void prepareViewPager(ViewPager viewPager, ArrayList<String> arrayList) {

        MainAdapter adapter = new MainAdapter(getSupportFragmentManager());
        ArrayList<Fragment> fragments = new ArrayList<>();


        DisplayNotificationAppointmentFragment displayNotificationAppointmentFragment = new DisplayNotificationAppointmentFragment();
        DisplayNotificationPaymentFragment displayNotificationPaymentFragment = new DisplayNotificationPaymentFragment();
        fragments.add(displayNotificationAppointmentFragment);
        fragments.add(displayNotificationPaymentFragment);
        adapter.fragmentList.add(displayNotificationAppointmentFragment);
        adapter.fragmentList.add(displayNotificationPaymentFragment);

        adapter.arrayList.add(tab1);
        adapter.arrayList.add(tab2);


        viewPager.setAdapter(adapter);


    }
    public String getType(){
        return whichIntent;
    }

    private class MainAdapter extends FragmentPagerAdapter {
        ArrayList<String> arrayList = new ArrayList<>();
        List<Fragment> fragmentList = new ArrayList<>();

        public MainAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        public void addFragment(Fragment fragment, String title) {
            arrayList.add(title);
            fragmentList.add(fragment);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return arrayList.get(position);
        }
    }
}