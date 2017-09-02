/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lisuke.pa_wzry.DBUtils;

import com.j256.ormlite.dao.Dao;
import com.lisuke.pa_wzry.Model.Equip;
import java.io.IOException;
import java.sql.SQLException;

/**
 *
 * @author lisuke
 */
public class EquipDAO {
    
    Dao dao;

    public EquipDAO() throws SQLException, IOException {
        dao = DatabaseHelper.getDao(Equip.class);
    }
    
}
