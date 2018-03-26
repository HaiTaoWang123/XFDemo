package com.example.dxc.filedownloadtest.util;

import android.os.Environment;

import com.example.dxc.filedownloadtest.model.DownLoadFileInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @Class:
 * @Description:
 * @Author: haitaow(haitaow@hpe.com)
 * @Date: 2/28/2018-3:52 PM.
 * @Version 1.0
 */

public class DownloadUtils {

    public static String getDownloadPath(){
        String downloadPath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            downloadPath = Environment.getExternalStorageState() + File.separator + "download";
        }
        return downloadPath;
    }

    /**
     * 获取测试数据
     * @return
     */
    public static List<DownLoadFileInfo> getTestData() {
        List<DownLoadFileInfo> list = new ArrayList<DownLoadFileInfo>();
//        DownLoadFileInfo app1 = new DownLoadFileInfo("奇趣营", "http://219.128.78.33/apk.r1.market.hiapk.com/data/upload/apkres/2015/9_25/18/com.lling.qiqu_060245.apk");
//        list.add(app1);
//        DownLoadFileInfo app2 = new DownLoadFileInfo("云老师", "http://183.57.28.39/apk.r1.market.hiapk.com/data/upload/apkres/2015/9_30/9/com.bbk.cloudteacher_095422.apk");
//        list.add(app2);
//        DownLoadFileInfo app3 = new DownLoadFileInfo("开心消消乐", "http://183.62.114.247/m.wdjcdn.com/apk.wdjcdn.com/2/65/61103e72de9d8f95465e68b535559652.apk");
//        list.add(app3);
//        DownLoadFileInfo app4 = new DownLoadFileInfo("欢乐斗地主", "http://119.147.254.64/dd.myapp.com/16891/F5151080C8F69D2D992047D19B24C39D.apk");
//        list.add(app4);
//        DownLoadFileInfo app5 = new DownLoadFileInfo("天天酷跑", "http://183.61.182.28/m.wdjcdn.com/apk.wdjcdn.com/b/eb/295abecdc49760ed4b74fbc0d36c3ebb.apk");
//        list.add(app5);
//        DownLoadFileInfo app6 = new DownLoadFileInfo("中国象棋", "http://183.61.182.28/m.wdjcdn.com/apk.wdjcdn.com/8/0b/d7b904d26558ae7e22d1e6ebdf4830b8.apk");
//        list.add(app6);
//        DownLoadFileInfo app7 = new DownLoadFileInfo("消灭星星", "http://14.18.142.20/m.wdjcdn.com/apk.wdjcdn.com/b/a0/e8c01dc875f0c72f4952ce800f3f6a0b.apk");
//        list.add(app7);
//        DownLoadFileInfo app8 = new DownLoadFileInfo("墨迹天气", "http://125.88.65.248/m.wdjcdn.com/apk.wdjcdn.com/3/8d/19e04e8c921e30eaae2dd5835697e8d3.apk");
//        list.add(app8);
//        DownLoadFileInfo app9 = new DownLoadFileInfo("暗黑黎明", "http://183.61.182.28/m.wdjcdn.com/apk.wdjcdn.com/4/7d/410a086fe71834660e01cecdde55d7d4.apk");
//        list.add(app9);
//        DownLoadFileInfo app10 = new DownLoadFileInfo("有道词典", "http://14.18.142.20/m.wdjcdn.com/apk.wdjcdn.com/b/4a/ab2001cd8ec89687e34dab51119064ab.apk");
//        list.add(app10);
//        DownLoadFileInfo app11 = new DownLoadFileInfo("虎扑跑步", "http://14.18.142.20/m.wdjcdn.com/apk.wdjcdn.com/1/7c/f0276757ab38391ba24489e224ca87c1.apk");
//        list.add(app11);
//        DownLoadFileInfo app12 = new DownLoadFileInfo("大众点评", "http://125.88.65.248/m.wdjcdn.com/apk.wdjcdn.com/6/43/e5f01e24b83295ef6bd04c454377b436.apk");
//        list.add(app12);
//        DownLoadFileInfo app13 = new DownLoadFileInfo("一号店", "http://125.88.65.248/m.wdjcdn.com/apk.wdjcdn.com/6/55/188dd88849b9901e3547d9cb104df556.apk");
//        list.add(app13);
        DownLoadFileInfo app1 = new DownLoadFileInfo("今日头条", "http://a4.res.meizu.com/source/3459/57ed68c1eda34b7280c65f22aa63d3a8?sign=787da0af86700e9908f77256d3c5ceb2&t=5ab3b213&fname=com.ss.android.article.news_665");
        list.add(app1);
        DownLoadFileInfo app2 = new DownLoadFileInfo("追书神器", "http://a3.res.meizu.com/source/3445/37aeb53bc0e0468cb5d823c0c7a9723b?auth_key=1521726090-0-0-e413b3223ca624dd030f7718563bd95f&fname=com.ushaqi.zhuishushenqi_2360");
        list.add(app2);
        DownLoadFileInfo app3 = new DownLoadFileInfo("微卷阅读", "http://a4.res.meizu.com/source/3381/0bd69ca962374aec8f3fc66fe6d94eee?sign=7b191e1b2e1fb5b8967fc001f4e67e5b&t=5ab3b28c&fname=cn.wejuan.reader_1002");
        list.add(app3);
        DownLoadFileInfo app4 = new DownLoadFileInfo("凤凰新闻", "http://a4.res.meizu.com/source/3437/77d6563f09f44afc8b9c9885abb12d88?sign=9e0ea5819ffc8c0773fd4b702c09203d&t=5ab3b28f&fname=com.ifeng.news2_276");
        list.add(app4);
        DownLoadFileInfo app5 = new DownLoadFileInfo("奇热小说", "http://a3.res.meizu.com/source/3428/6009ad2e446743de813d249e0ee19b41?auth_key=1521726096-0-0-d7ef01ea8574150ee23e5fc8ad190a41&fname=com.qixiao.qrxs_309");
        list.add(app5);
        DownLoadFileInfo app6 = new DownLoadFileInfo("爱卡汽车", "http://a3.res.meizu.com/source/3451/df5aec09cc934c978f49e2dea3116733?auth_key=1521726097-0-0-34ea4894157b135e2bfe86a9ac0b3c22&fname=com.xcar.activity_191");
        list.add(app6);
        DownLoadFileInfo app7 = new DownLoadFileInfo("咪咕阅读", "http://14.18.142.20/m.wdjcdn.com/apk.wdjcdn.com/b/a0/e8c01dc875f0c72f4952ce800f3f6a0b.apk");
        list.add(app7);
        DownLoadFileInfo app8 = new DownLoadFileInfo("球经", "http://a4.res.meizu.com/source/2830/630dd51cea2f4d818fb9f7727a159c6b?sign=95032f564b6df4cca3d5282227de050f&t=5ab3b37b&fname=com.information.football_51");
        list.add(app8);
        return list;
    }
}
