package com.example.dxc.xfdemo.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.example.dxc.xfdemo.R;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 4/1/2018-7:26 PM.
 * @Version 1.0
 */

public class LoadingDialog extends Dialog{
    private String loadingText = "";

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
    }

    private LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loadingdialog);
        TextView tv = (TextView)this.findViewById(R.id.textView);
        tv.setText(loadingText);
//        LinearLayout linearLayout = (LinearLayout)this.findViewById(R.id.LinearLayout);
//        linearLayout.getBackground().setAlpha(210);
    }

    public void setLoadingText(String loadingText) {
        this.loadingText = loadingText;
    }
}
