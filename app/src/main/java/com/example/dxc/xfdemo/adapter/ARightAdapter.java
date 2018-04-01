package com.example.dxc.xfdemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dxc.xfdemo.R;
import com.example.dxc.xfdemo.model.Stock;

import java.util.List;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/30/2018-9:02 PM.
 * @Version 1.0
 */

public class ARightAdapter extends BaseAdapter {
    private Context context;
    List<Stock> list;


    public ARightAdapter(Context context, List<Stock> models) {
        super();
        this.context = context;
        this.list = models;
    }

    @Override
    public int getCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null) {
            return list.get(position);
        }
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHold viewHold;
        if (convertView == null) {
            viewHold = new ViewHold();
            convertView = LayoutInflater.from(context).inflate(R.layout.layout_right_item_a, parent,false);
            viewHold.textView1 = (TextView) convertView.findViewById(R.id.right_item_textview0);
            viewHold.textView2 = (TextView) convertView.findViewById(R.id.right_item_textview1);
            viewHold.textView3 = (TextView) convertView.findViewById(R.id.right_item_textview2);
            viewHold.textView4 = (TextView) convertView.findViewById(R.id.right_item_textview3);
            viewHold.textView5 = (TextView) convertView.findViewById(R.id.right_item_textview4);
//            viewHold.textView6 = (TextView) convertView.findViewById(R.id.right_item_textview5);
//            viewHold.textView7 = (TextView) convertView.findViewById(R.id.right_item_textview6);
            convertView.setTag(viewHold);
        } else {
            viewHold = (ViewHold) convertView.getTag();
        }
        Stock stock = (Stock) getItem(position);
        viewHold.textView1.setText(stock.getCode()+"");
        viewHold.textView2.setText(stock.getTrade()+"");
        viewHold.textView3.setText(stock.getPricechange()+"");
        viewHold.textView4.setText(stock.getChangepercent()+"");
//        viewHold.textView5.setText(stock.getSpeed()+"");



        return convertView;
    }

    static class ViewHold {

        TextView  textView1, textView2, textView3, textView4, textView5, textView6,textView7;

    }
}
