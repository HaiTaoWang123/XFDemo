package com.example.dxc.xfdemo;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dxc.xfdemo.common.BaseActivity;

/**
 * Created by haitaow on 1/22/2018-6:47 PM.
 * Email: haitaow@hpe.com
 * version 1.0
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private EditText etName, etPwd;
    private Button btLogin, btRegist;
    private Toast mToast;
    public static final String userName = "userName";
    public static final String userPwd = "userPwd";
    public static final String isRegist = "isRegist";
    private String name = "";
    private String pwd = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.activity_login);
        setBackVisible(false, "");
        setSettingVisible(false, "");
        setTitle("用户登录");

        etName = (EditText) findViewById(R.id.et_user_name);
        etPwd = (EditText) findViewById(R.id.et_user_pwd);
        btLogin = (Button) findViewById(R.id.bt_ID_login);
        btRegist = (Button) findViewById(R.id.bt_ID_regist);

        btLogin.setOnClickListener(this);
        btRegist.setOnClickListener(this);
        SharedPreferences sp = getSharedPreferences(SplashActivity.sp_Name, Activity.MODE_PRIVATE);
        if (sp.getBoolean(isRegist,false)){
//            btRegist.setClickable(false);
            btRegist.setEnabled(false);
//            btLogin.setClickable(true);
            btLogin.setEnabled(true);
        }else {
//            btRegist.setClickable(true);
            btRegist.setEnabled(true);
//            btLogin.setClickable(false);
            btLogin.setEnabled(false);
        }

        mToast = Toast.makeText(LoginActivity.this, "", Toast.LENGTH_SHORT);
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sp = getSharedPreferences(SplashActivity.sp_Name, Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        name = etName.getText().toString().trim();
        pwd = etPwd.getText().toString().trim();
        switch (v.getId()) {
            case R.id.bt_ID_regist:
                if (!sp.getBoolean(isRegist, false)) {
                    if (checkNamePwd()) {
                        if (sp.getString(userName, "") != null
                                && sp.getString(userName, "").equals(userName)) {
                            showToast("该用户名已注册，请登录或更换用户名！");
                            etName.setText("");
                            etPwd.setText("");
                        } else {
                            editor.putString(userName, name);
                            editor.putString(userPwd, pwd);
                            editor.putBoolean(isRegist, true);
                            editor.commit();
                            showToast("注册成功，请登录");
                            etPwd.setText("");
                            btLogin.setEnabled(true);
                            btRegist.setEnabled(false);
                        }
                    }
                }
                break;
            case R.id.bt_ID_login:
                if (sp.getBoolean(isRegist, false)) {
                    if (checkNamePwd()) {
                        String spName = sp.getString(userName, "");
                        String spPwd = sp.getString(userPwd, "");
                        if (spName.equals(name) && spPwd.equals(pwd)) {
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else if (spName.isEmpty() || spName.equals("")) {
                            showToast("该用户名未注册，请注册！");
                        } else if (!spName.isEmpty() && !spName.equals(name)) {
                            showToast("用户名错误，请重新输入");
                            etName.setText("");
                            etPwd.setText("");
                        } else if (spName.equals(name) && !spPwd.equals(pwd)) {
                            showToast("密码错误，请重新输入");
                            etPwd.setText("");
                        }
                    }
                } else {
                    showToast("您还未注册，请先注册！");
                }
                break;
            default:
                break;
        }
    }

    private void showToast(final String msg) {
        mToast.setText(msg);
        mToast.show();
    }

    private boolean checkNamePwd() {
        if (name == null || name.isEmpty()) {
            showToast("用户名不能为空");
            return false;
        }
        if (pwd == null || pwd.isEmpty()) {
            showToast("密码不能为空");
            return false;
        }
        return true;
    }

    @Override
    public void onSettingClick() {

    }

    @Override
    public void onBackPressed() {
        SharedPreferences sp = getSharedPreferences(SplashActivity.sp_Name, Activity.MODE_PRIVATE);
        if (!sp.getBoolean(isRegist, false)){
            Intent intent = new Intent(this,SelectLoginStyleActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
