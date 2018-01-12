package com.example.dxc.xfdemo.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.dxc.xfdemo.fragment.EncodeBaseFragment;

import java.util.List;

/**
 * Created by haitaow on 1/11/2018-11:12 AM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class EncodeFragmentPageAdapter extends FragmentPagerAdapter{
    private List<EncodeBaseFragment> fragmentList;


    public EncodeFragmentPageAdapter(FragmentManager fm, List<EncodeBaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
    }

//    @Override
//    public FragmentPagerAdapter getItem(int position) {
//        EncodeBaseFragment fragment = null;
//        if (fragmentList.size()>position){
//            fragment = fragmentList.get(position);
//            if (fragment != null){
//                return fragment
//            }
//        }
//        while (position>= fragmentList.size()){
//            fragmentList.add(null);
//        }
//        fragment
//        return fragmentList.get(position);
//    }


    @Override
    public Fragment getItem(int position) {
//        Fragment fragment = null;
//        if (fragmentList.size() > position) {
//            fragment = fragmentList.get(position);
//            if (fragment != null) {
//                return fragment;
//            }
//        }
//        while (position>=fragmentList.size()) {
//            fragmentList.add(null);
//        }
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }
}
