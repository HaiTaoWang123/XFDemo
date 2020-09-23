//package com.example.dxc.xfdemo.apt;
//
//import android.database.sqlite.SQLiteDatabase;
//
///**
// * @Class:
// * @Description:
// * @Author: haitaow(haitaow @ hpe.com)
// * @Date: 6/6/2018-4:14 PM.
// * @Version 1.0
// */
//public class BaseDaoFactory {
//    private static final BaseDaoFactory ourInstance = new BaseDaoFactory();
//
//    public static BaseDaoFactory getInstance() {
//        return ourInstance;
//    }
//
//    SQLiteDatabase sqLiteDatabase;
//
//    private BaseDaoFactory() {
//        String path = "/data";
//        sqLiteDatabase = SQLiteDatabase.openOrCreateDatabase(path, null);
//    }
//
//    public <T> IBaseDao<T> getBaseDao(Class<T> entityClass) {
//        BaseDaoImpl<T> baseDao = null;
//        try {
//            baseDao = BaseDaoImpl.class.newInstance();
//            baseDao.init(sqLiteDatabase,entityClass);
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        } catch (InstantiationException e) {
//            e.printStackTrace();
//        }
//        return baseDao;
//    }
//}