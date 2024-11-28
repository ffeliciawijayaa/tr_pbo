
package cgroup.controller;

import java.sql.ResultSet;
import java.sql.Statement;
import cgroup.model.LoginAdmin;

public class ControllerAdmin {
    
    public Statement stm;
    public ResultSet res;
    public String sql;
    
    //Konstruktor
    public ControllerAdmin() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }
    
    public boolean cekLogin(String a, String b) {
        LoginAdmin la = new LoginAdmin();
        la.setUsername(a);
        la.setPassword(b);
        boolean status = false;
        
        try {
            //query
            this.sql = "SELECT * FROM tb_admin WHERE username='"+la.getUsername()+"'AND password='"+la.getPassword()+"'";
            //menjalankan query nya
            //khusus query select menggunakan executeQuery
            res = stm.executeQuery(sql);
            
            if (res.next()) status = true;
            else status = false;
        } catch (Exception e) {
            System.out.println("Gagal Query"+e);
        }
        return status;
    } 
    
}
