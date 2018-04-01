package com.example.dxc.xfdemo;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.dxc.xfdemo.adapter.ALeftAdapter;
import com.example.dxc.xfdemo.adapter.ARightAdapter;
import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.model.BaseStockMdl;
import com.example.dxc.xfdemo.model.Stock;
import com.example.dxc.xfdemo.network.base.NetWorkListener;
import com.example.dxc.xfdemo.network.service.StockService;
import com.example.dxc.xfdemo.util.ScreenUtil;
import com.example.dxc.xfdemo.widget.MyHorizontalScrolloView;

import java.util.List;

/**
 * @Class: EventDispatchTest
 * @Description: 事件分发机制测试
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/29/2018-5:29 PM.
 * @Version 1.0
 */

public class EventDispatchTest extends BaseActivity {

    //左侧固定一列数据适配
    private ListView left_container_listview;
    private List<String> leftlList;

    //右侧数据适配
    private ListView right_container_listview;
    private List<Stock> stockList;

    private MyHorizontalScrolloView title_horsv;
    private MyHorizontalScrolloView content_horsv;

    private ARightAdapter rightAdapter;
    private ALeftAdapter leftAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.acitvity_event_dispatch);
        setTitle("分发机制测试");
        setSettingVisible(true, "刷新");
        setBackVisible(true, "返回");

        findView();
        initData();
        initView();


    }

    private void findView() {
        title_horsv = (MyHorizontalScrolloView) findViewById(R.id.title_horsv);
        content_horsv = (MyHorizontalScrolloView) findViewById(R.id.content_horsv);
        left_container_listview = (ListView) findViewById(R.id.left_container_listview);
        right_container_listview = (ListView) findViewById(R.id.right_container_listview);

        // 设置两个水平控件的联动
        title_horsv.setScrollView(content_horsv);
        content_horsv.setScrollView(title_horsv);

        initLoadingDialog("股票数据加载中...",false);
        showLoadingDialog();
    }

    private void initView() {
        //添加左侧数据
        leftAdapter = new ALeftAdapter(this, leftlList);
        left_container_listview.setAdapter(leftAdapter);
        ScreenUtil.setListViewHeightBasedOnChildren(left_container_listview);


        // 添加右边内容数据
        rightAdapter = new ARightAdapter(this, stockList);
        right_container_listview.setAdapter(rightAdapter);
        ScreenUtil.setListViewHeightBasedOnChildren(right_container_listview);

        right_container_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Stock stock = (Stock) parent.getAdapter().getItem(position);
                Toast.makeText(EventDispatchTest.this, stock.getName() + position, Toast.LENGTH_LONG).show();
            }
        });
    }


    //初始化数据源
    private void initData() {

//        stockList = new ArrayList<>();
//        for (int i = 0; i < 40; i++) {
//            stockList.add(new Stock(001, "风华基金", 19.92, 0.0999, 0.2));
//        }
//        leftlList = new ArrayList<>();
//        for (int i = 0; i < stockList.size(); i++) {
//            leftlList.add(stockList.get(i).getName());
//        }

        StockService.getStockList("A", 2, 20, new NetWorkListener<BaseStockMdl>() {
            @Override
            public void onSuccess(BaseStockMdl data) {
                if (data != null && data.getData() != null) {
                    stockList = data.getData();
                    for (int i = 0; i < stockList.size(); i++) {
                        leftlList.add(stockList.get(i).getName());
                    }
                    rightAdapter.notifyDataSetChanged();
                    leftAdapter.notifyDataSetChanged();
                    dismissLoadingDialog();
                }
            }

            @Override
            public void onFailed(String errMsg) {
                dismissLoadingDialog();
                Toast.makeText(EventDispatchTest.this,"获取数据失败！",Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSettingClick() {
        initLoadingDialog("更新中......",false);
        initData();
    }
}
