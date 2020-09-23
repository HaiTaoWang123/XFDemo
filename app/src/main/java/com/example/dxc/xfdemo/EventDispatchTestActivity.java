package com.example.dxc.xfdemo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dxc.xfdemo.adapter.ALeftAdapter;
import com.example.dxc.xfdemo.adapter.ARightAdapter;
import com.example.dxc.xfdemo.common.BaseActivity;
import com.example.dxc.xfdemo.model.BaseStockMDL;
import com.example.dxc.xfdemo.model.Stock;
import com.example.dxc.xfdemo.model.StockMDL;
import com.example.dxc.xfdemo.network.base.NetWorkListener;
import com.example.dxc.xfdemo.network.service.StockService;
import com.example.dxc.xfdemo.util.ScreenUtil;
import com.example.dxc.xfdemo.widget.MyHorizontalScrolloView;
import com.github.haitaowang123.baseutils.algorithm.SortAlgorithm;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * @Class: EventDispatchTest
 * @Description: 事件分发机制测试
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 3/29/2018-5:29 PM.
 * @Version 1.0
 */

public class EventDispatchTestActivity extends BaseActivity implements View.OnClickListener {

    private static final String Default_Flag = "-1";//默认列表，不排序
    private static final String Accending_Flag = "0";//升序排列
    private static final String Descending_Flag = "1";//降序排列

    //左侧固定一列数据适配
    private ListView left_container_listview;

    //右侧数据适配
    private ListView right_container_listview;
    private HashMap<Integer,Object> stockMDLList;

    private MyHorizontalScrolloView title_horsv;
    private MyHorizontalScrolloView content_horsv;
    private boolean isReverse = false;

    private ARightAdapter rightAdapter;
    private ALeftAdapter leftAdapter;
    private TextView tvCurrentPrice, tvPriceChange, tvChangePercent, tvPriceLastDay,
            tvPriceToday, tvPriceHigh, tvPriceLow, tvExchangeRate, tvAmount, tvVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setBaseContentLayout(R.layout.acitvity_event_dispatch);
        setTitle("分发机制测试");
        setSettingVisible(true, "刷新");
        setBackVisible(true, "返回");
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            //权限还没有授予，需要在这里写申请权限的代码
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.INTERNET}, 60);
        }
        findView();
        initData();
    }

    private void findView() {
        title_horsv = (MyHorizontalScrolloView) findViewById(R.id.title_horsv);
        content_horsv = (MyHorizontalScrolloView) findViewById(R.id.content_horsv);
        left_container_listview = (ListView) findViewById(R.id.left_container_listview);
        right_container_listview = (ListView) findViewById(R.id.right_container_listview);

        tvCurrentPrice = (TextView) findViewById(R.id.right_tab_textview1);
        tvPriceChange = (TextView) findViewById(R.id.right_tab_textview2);
        tvChangePercent = (TextView) findViewById(R.id.right_tab_textview3);
        tvPriceLastDay = (TextView) findViewById(R.id.right_tab_textview4);
        tvPriceToday = (TextView) findViewById(R.id.right_tab_textview5);
        tvPriceHigh = (TextView) findViewById(R.id.right_tab_textview6);
        tvPriceLow = (TextView) findViewById(R.id.right_tab_textview7);
        tvExchangeRate = (TextView) findViewById(R.id.right_tab_textview8);
        tvAmount = (TextView) findViewById(R.id.right_tab_textview9);
        tvVolume = (TextView) findViewById(R.id.right_tab_textview10);

        tvCurrentPrice.setOnClickListener(this);
        tvPriceChange.setOnClickListener(this);
        tvChangePercent.setOnClickListener(this);
        tvPriceLastDay.setOnClickListener(this);
        tvPriceToday.setOnClickListener(this);
        tvPriceHigh.setOnClickListener(this);
        tvPriceLow.setOnClickListener(this);
        tvExchangeRate.setOnClickListener(this);
        tvAmount.setOnClickListener(this);
        tvVolume.setOnClickListener(this);
        setTvTag();

        // 设置两个水平控件的联动
        title_horsv.setScrollView(content_horsv);
        content_horsv.setScrollView(title_horsv);

        initLoadingDialog("股票数据加载中...", false);
        showLoadingDialog();

        stockMDLList = new HashMap<>();
    }

    private void initView() {
        //添加左侧数据
        leftAdapter = new ALeftAdapter(this, stockMDLList,isReverse);
        left_container_listview.setAdapter(leftAdapter);
        ScreenUtil.setListViewHeightBasedOnChildren(left_container_listview);

        // 添加右边内容数据
        rightAdapter = new ARightAdapter(this, stockMDLList,isReverse);
        right_container_listview.setAdapter(rightAdapter);
        ScreenUtil.setListViewHeightBasedOnChildren(right_container_listview);

        right_container_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StockMDL stock = (StockMDL) parent.getAdapter().getItem(position);
                Toast.makeText(EventDispatchTestActivity.this, stock.getName() + position, Toast.LENGTH_LONG).show();
            }
        });
    }


    //初始化数据源
    private void initData() {
        StockService.getStockList(1, "A", 2, 1, new NetWorkListener<BaseStockMDL>() {
            @Override
            public void onSuccess(BaseStockMDL data) {
                if (data != null && data.getData() != null) {
                    stockMDLList = formatStockData(data.getData());
                    initView();
                    dismissLoadingDialog();
                } else {
                    dismissLoadingDialog();
                    Toast.makeText(EventDispatchTestActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailed(String errMsg) {
                dismissLoadingDialog();
                Toast.makeText(EventDispatchTestActivity.this, "获取数据失败！", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onSettingClick() {
        showLoadingDialog();
        initData();
        setTvDrawableRight(null, Default_Flag);
    }

    @Override
    public void onClick(View v) {
        String var = null;
        if (v.getTag() == Default_Flag) {
            setTvTag();
            v.setTag(Descending_Flag);//默认降序
        } else if (v.getTag() == Accending_Flag) {//此前升序
            v.setTag(Descending_Flag);//变为降序
        } else if (v.getTag() == Descending_Flag) {//此前为降序
            v.setTag(Accending_Flag);//变为升序
        }
        switch (v.getId()) {
            case R.id.right_tab_textview1:
                var = "trade";
                break;
            case R.id.right_tab_textview2:
                var = "pricechange";
                break;
            case R.id.right_tab_textview3:
                var = "changepercent";
                break;
            case R.id.right_tab_textview4:
                var = "settlement";
                break;
            case R.id.right_tab_textview5:
                var = "open";
                break;
            case R.id.right_tab_textview6:
                var = "high";
                break;
            case R.id.right_tab_textview7:
                var = "low";
                break;
            case R.id.right_tab_textview8:
                var = "turnoverratio";
                break;
            case R.id.right_tab_textview9:
                var = "amount";
                break;
            case R.id.right_tab_textview10:
                var = "volume";
                break;
            default:
                break;
        }

        showLoadingDialog();
        if (v.getTag().equals(Descending_Flag)) {
            SortAlgorithm.bubbleSortByAttribute(stockMDLList,var,true);
        } else {
            SortAlgorithm.bubbleSortByAttribute(stockMDLList,var,false);
        }
        setTvDrawableRight(v, (String) v.getTag());

        leftAdapter.notifyDataSetChanged();
        rightAdapter.notifyDataSetChanged();
        dismissLoadingDialog();
    }

    /**
     * 格式化数据
     * @param stocks 待格式化数据
     * @return
     */
    private HashMap<Integer,Object> formatStockData(List<Stock> stocks) {
        HashMap<Integer,Object> stockList = new HashMap<>();
        DecimalFormat df = new DecimalFormat("#.00");
        for (int i = 0; i < stocks.size(); i++) {
            StockMDL stock = new StockMDL();
            stock.setName(stocks.get(i).getName());//名称
            stock.setCode(stocks.get(i).getCode());//代码

//            stock.setTrade(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getTrade()))));//最新价
//            stock.setPricechange(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getPricechange()))));//涨跌额
//            stock.setChangepercent(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getChangepercent()))));//涨跌幅
//            stock.setSettlement(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getSettlement()))));//昨收
//            stock.setOpen(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getOpen()))));//今开
//            stock.setHigh(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getHigh()))));//最高
//            stock.setLow(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getLow()))));//最低
//            stock.setBuy(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getBuy()))));//最低
//            stock.setSell(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getSell()))));//卖价
//            stock.setMktcap(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getMktcap()))));//总市值
//            stock.setPer(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getPer()))));//本益比
//            stock.setPb(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getPb()))));//平均市净率
//            stock.setTurnoverratio(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getTurnoverratio()))));//换手率
//            stock.setAmount(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getAmount()) * 0.00000001)));//成交额
//            stock.setVolume(Double.parseDouble(df.format(Double.parseDouble(stocks.get(i).getVolume()) * 0.0001)));//成交量

            stock.setTrade(Double.parseDouble(stocks.get(i).getTrade()));//最新价
            stock.setPricechange(Double.parseDouble(stocks.get(i).getPricechange()));//涨跌额
            stock.setChangepercent(Double.parseDouble(stocks.get(i).getChangepercent()));//涨跌幅
            stock.setSettlement(Double.parseDouble(stocks.get(i).getSettlement()));//昨收
            stock.setOpen(Double.parseDouble(stocks.get(i).getOpen()));//今开
            stock.setHigh(Double.parseDouble(stocks.get(i).getHigh()));//最高
            stock.setLow(Double.parseDouble(stocks.get(i).getLow()));//最低
            stock.setBuy(Double.parseDouble(stocks.get(i).getBuy()));//最低
            stock.setSell(Double.parseDouble(stocks.get(i).getSell()));//卖价
            stock.setMktcap(Double.parseDouble(stocks.get(i).getMktcap()));//总市值
            stock.setPer(Double.parseDouble(stocks.get(i).getPer()));//本益比
            stock.setPb(Double.parseDouble(stocks.get(i).getPb()));//平均市净率
            stock.setTurnoverratio(Double.parseDouble(stocks.get(i).getTurnoverratio()));//换手率
            stock.setAmount(Double.parseDouble(stocks.get(i).getAmount()) * 0.00000001);//成交额
            stock.setVolume(Double.parseDouble(stocks.get(i).getVolume()) * 0.0001);//成交量
            if (stock.getTrade() == 0){
                stock.setTrade(stock.getSettlement());
            }
            stockList.put(i,stock);
        }
        Log.e("stockList", new Gson().toJson(stockList));
        return stockList;
    }

    /**
     * 设置默认非排序状态下的tag，均设置为-1
     */
    private void setTvTag() {
        tvCurrentPrice.setTag(Default_Flag);
        tvPriceChange.setTag(Default_Flag);
        tvChangePercent.setTag(Default_Flag);
        tvPriceLastDay.setTag(Default_Flag);
        tvPriceToday.setTag(Default_Flag);
        tvPriceHigh.setTag(Default_Flag);
        tvPriceLow.setTag(Default_Flag);
        tvExchangeRate.setTag(Default_Flag);
        tvAmount.setTag(Default_Flag);
        tvVolume.setTag(Default_Flag);
    }

    /**
     * 绘制升序和降序图标
     * @param view 被添加的View
     * @param sortType 排序类型
     */
    private void setTvDrawableRight(View view, String sortType) {
        tvCurrentPrice.setCompoundDrawables(null, null, null, null);
        tvPriceChange.setCompoundDrawables(null, null, null, null);
        tvChangePercent.setCompoundDrawables(null, null, null, null);
        tvPriceLastDay.setCompoundDrawables(null, null, null, null);
        tvPriceToday.setCompoundDrawables(null, null, null, null);
        tvPriceHigh.setCompoundDrawables(null, null, null, null);
        tvPriceLow.setCompoundDrawables(null, null, null, null);
        tvExchangeRate.setCompoundDrawables(null, null, null, null);
        tvAmount.setCompoundDrawables(null, null, null, null);
        tvVolume.setCompoundDrawables(null, null, null, null);

        if (view != null && !sortType.equals(Default_Flag)) {
            Drawable drawable = null;
            if (sortType.equals(Descending_Flag)) {
                drawable = getResources().getDrawable(R.mipmap.icon_descending);
            } else if (sortType.equals(Accending_Flag)) {
                drawable = getResources().getDrawable(R.mipmap.icon_accending);
            }
            drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
            ((TextView) view).setCompoundDrawables(null, null, drawable, null);
        }
    }
}
