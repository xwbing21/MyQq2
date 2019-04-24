package com.example.xue.myqq.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import butterknife.BindView;
import com.example.xue.myqq.R;
import com.example.xue.myqq.anim.MyPagerTransition;
import com.example.xue.myqq.base.BaseFragment;


public class ContactsFragment extends BaseFragment {

    @BindView(R.id.tab_contact)
    public TabLayout mContactTab;   // Contact上的Tab
    private static String[] mTabTitles;
    @BindView(R.id.vp_contact)
    public ViewPager mContactViewPager;

    @Override
    protected int getContentLayoutId() {
        return R.layout.fragment_contacts;
    }

    @Override
    protected void initData() {
        mTabTitles = getResources().getStringArray(R.array.contact_tab_titles);
        // 1 Tablayout 与 ViewPager 关联
        mContactTab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // 被选中的时候回调
                mContactViewPager.setCurrentItem(tab.getPosition(), true);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        // 2 ViewPager滑动关联tabLayout
        mContactViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mContactTab));
        // 3 设置tabLayout的标签来自于PagerAdapter
        mContactTab.setupWithViewPager(mContactViewPager);
        // 4 添加适配器
        MyPagerAdapter pagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        mContactViewPager.setAdapter(pagerAdapter);
        // 5 添加动画
        mContactViewPager.setPageTransformer(false, new MyPagerTransition());
    }

    static class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTabTitles[position];
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new FriendFragment();
                    break;
                case 1:
                    fragment = new PhoneContactFragment();
                    break;
                case 2:
                    fragment = new OfficialFragment();
                    break;
                default:
                    break;
            }
            if (fragment == null) {
                return null;
            }
            Bundle bundle = new Bundle();
            bundle.putString("tabTitle", mTabTitles[position]);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mTabTitles.length;
        }
    }
}
