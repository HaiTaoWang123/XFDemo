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

public class ALeftAdapter extends BaseAdapter {

    private Context context;
    private List<Stock> list;

    public ALeftAdapter(Context context, List<Stock> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        if (list!=null) {
            return list.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if (list!=null) {
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
        ViewHold hold;
        if (convertView==null) {
            hold=new ViewHold();
            convertView= LayoutInflater.from(context).inflate(R.layout.layout_left_item, null);
            hold.textView1=(TextView) convertView.findViewById(R.id.left_container_textview0);
            hold.textView2=(TextView) convertView.findViewById(R.id.left_container_textview1);
            convertView.setTag(hold);
        }else {
            hold=(ViewHold) convertView.getTag();
        }

        hold.textView1.setText(list.get(position).getName());
        hold.textView2.setText(list.get(position).getCode());
        return convertView;
    }

    static class ViewHold{
        TextView textView1,textView2;
    }
}
