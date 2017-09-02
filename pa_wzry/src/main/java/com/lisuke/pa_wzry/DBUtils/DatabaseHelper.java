/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lisuke.pa_wzry.DBUtils;


import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.lisuke.pa_wzry.Model.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author lisuke
 */

public class DatabaseHelper {
    
    private final static String DATABASE_PATH = "/home/lisuke/Desktop/pa_wzry.db";
    private final static String DATABASE_URL = "jdbc:sqlite:" + DATABASE_PATH;
    private static boolean isCreate = false;
    private static ConnectionSource connectionSource = null; 
    private static final Map<Class,Dao> mDaoMap= new HashMap<>();
    
    public static Dao getDao(Class clazz) throws SQLException, IOException
    {
        Dao dao = mDaoMap.get(clazz);
        if(dao == null){
            dao = DaoManager.createDao(getConnectionSource(), clazz);
            mDaoMap.put(clazz, dao);
        }
        return dao;
    }

    public static ConnectionSource getConnectionSource() throws SQLException{
        if(connectionSource==null)
            connectionSource = new JdbcConnectionSource(DATABASE_URL);
        onCreate();
        return connectionSource ;
    }

    private static void onCreate() throws SQLException {
        TableUtils.createTableIfNotExists(connectionSource, HeroCate.class);
        TableUtils.createTableIfNotExists(connectionSource, EquipCate.class);
        TableUtils.createTableIfNotExists(connectionSource, Hero.class);
        TableUtils.createTableIfNotExists(connectionSource, Equip.class);
        TableUtils.createTableIfNotExists(connectionSource, HeroSkill.class);
        TableUtils.createTableIfNotExists(connectionSource, HeroEquipSuggest.class);
    }
    
}
