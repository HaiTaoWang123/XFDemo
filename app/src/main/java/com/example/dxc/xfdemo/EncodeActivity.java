package com.example.dxc.xfdemo;

import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.HorizontalScrollView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.dxc.xfdemo.adapter.EncodeFragmentPageAdapter;
import com.example.dxc.xfdemo.common.BaseFragmentActivity;
import com.example.dxc.xfdemo.fragment.DispatchTouchEventFragment;
import com.example.dxc.xfdemo.fragment.EncodeBaseFragment;
import com.example.dxc.xfdemo.fragment.IsbnFragment;
import com.example.dxc.xfdemo.fragment.SmsFragment;
import com.example.dxc.xfdemo.fragment.TelFragment;
import com.example.dxc.xfdemo.fragment.TextFragment;
import com.example.dxc.xfdemo.fragment.UrlFragment;
import com.example.dxc.xfdemo.fragment.VacdFragment;
import com.example.dxc.xfdemo.fragment.WifiFragment;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

/**
 * Created by haitaow on 1/10/2018-5:09 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class EncodeActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private Context context;
    private HorizontalScrollView hScrollView;
    private RadioGroup radioGroup;
    private RadioButton rbText, rbUrl, rbSms, rbTest, rbVacd, rbWifi, rbTel, rbIsbn;
    private ViewPager viewPager;
    private TextFragment textFragment;
    private UrlFragment urlFragment;
    private SmsFragment smsFragment;
    private TelFragment telFragment;
    private DispatchTouchEventFragment testFragment;
    private VacdFragment vacdFragment;
    private WifiFragment wifiFragment;
    private IsbnFragment isbnFragment;
    private List<EncodeBaseFragment> fragmentList;
    private FragmentManager fragmentManager;
    private EncodeFragmentPageAdapter pageAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_encode_layout);
        setSettingVisible(false, "");
        setTitle("二维码生成");
        context = EncodeActivity.this;

        initView();

        intiViewPager();
    }

    private void initView() {
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        radioGroup = (RadioGroup) findViewById(R.id.rg_encoded_type);
        rbText = (RadioButton) findViewById(R.id.rb_text);
        rbSms = (RadioButton) findViewById(R.id.rb_sms);
        rbTest = (RadioButton) findViewById(R.id.rb_test);
        rbUrl = (RadioButton) findViewById(R.id.rb_url);
        rbTel = (RadioButton) findViewById(R.id.rb_tel);
        rbWifi = (RadioButton) findViewById(R.id.rb_wifi);
        rbVacd = (RadioButton) findViewById(R.id.rb_vcd);
        rbIsbn = (RadioButton) findViewById(R.id.rb_isbn);
        hScrollView = (HorizontalScrollView) findViewById(R.id.hScrollow);
    }

    private void intiViewPager() {
        textFragment = new TextFragment(context);
        telFragment = new TelFragment(context);
        testFragment = new DispatchTouchEventFragment(context);
        testFragment.setmParentWidth(viewPager.getWidth());
        smsFragment = new SmsFragment(context);
        vacdFragment = new VacdFragment(context);
        wifiFragment = new WifiFragment(context);
        urlFragment = new UrlFragment(context);
        isbnFragment = new IsbnFragment(context);

        fragmentList = new ArrayList<>();
        fragmentList.add(textFragment);
        fragmentList.add(urlFragment);
        fragmentList.add(isbnFragment);
        fragmentList.add(smsFragment);
        fragmentList.add(testFragment);
        fragmentList.add(telFragment);
        fragmentList.add(vacdFragment);
        fragmentList.add(wifiFragment);

        fragmentManager = getSupportFragmentManager();
        pageAdapter = new EncodeFragmentPageAdapter(fragmentManager, fragmentList);
        viewPager.setAdapter(pageAdapter);
        viewPager.setCurrentItem(0);
        rbText.setChecked(true);
        viewPager.addOnPageChangeListener(this);
        radioGroup.setOnCheckedChangeListener(this);
        getContentResolver();
    }

    @Override
    public void onSettingClick() {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                radioGroup.check(R.id.rb_text);
                rbText.performClick();
                break;
            case 1:
                radioGroup.check(R.id.rb_url);
                rbUrl.performClick();
                break;
            case 2:
                radioGroup.check(R.id.rb_isbn);
                rbIsbn.performClick();
                break;
            case 3:
                radioGroup.check(R.id.rb_sms);
                rbSms.performClick();
                break;
            case 4:
                radioGroup.check(R.id.rb_test);
                rbTest.performClick();
                break;
            case 5:
                radioGroup.check(R.id.rb_tel);
                rbTel.performClick();
                break;
            case 6:
                radioGroup.check(R.id.rb_vcd);
                rbVacd.performClick();
                break;
            case 7:
                radioGroup.check(R.id.rb_wifi);
                rbWifi.performClick();
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
        int index = group.indexOfChild(radioButton);
        hScrollView.smoothScrollTo(radioButton.getLeft() -
                (int) (getWindowManager().getDefaultDisplay().getWidth() / 4.5), 0);
        viewPager.setCurrentItem(index);
        /*switch (checkedId) {
            case R.id.rb_text:
                viewPager.setCurrentItem(0);
                break;
            case R.id.rb_url:
                viewPager.setCurrentItem(1);
                break;
            case R.id.rb_sms:
                viewPager.setCurrentItem(2);
                break;
            case R.id.rb_tel:
                viewPager.setCurrentItem(3);
                break;
            case R.id.rb_vcd:
                viewPager.setCurrentItem(4);
                break;
            case R.id.rb_wifi:
                viewPager.setCurrentItem(5);
                break;
            default:
                break;
        }*/
    }
}
