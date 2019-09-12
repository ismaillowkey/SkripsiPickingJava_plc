/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dao;
import org.sql2o.*;

/**
 *
 * @author lupa-nama
 */
public class conf
{
    public static Sql2o sql2o;

    static {
        sql2o = new Sql2o("jdbc:mysql://localhost:3306/skripsi_picking", "root", "root");
    }
}
