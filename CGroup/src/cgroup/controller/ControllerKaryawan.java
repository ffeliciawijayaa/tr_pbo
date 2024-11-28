package cgroup.controller;

import cgroup.model.Karyawan;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class ControllerKaryawan {

    public Statement stm;
    public ResultSet res;
    public String sql;

    //untuk mendesign tabel dengan kolom kolom
    DefaultTableModel dtm = new DefaultTableModel();

    //Konstruktor
    public ControllerKaryawan() {
        Koneksi db = new Koneksi();
        db.config();
        this.stm = db.stm;
    }

    //method untuk membuat model atau design tabel
    public DefaultTableModel buatTabel() {
        dtm.addColumn("id karyawan");
        dtm.addColumn("nama");
        dtm.addColumn("alamat");
        dtm.addColumn("tanggal masuk");
        dtm.addColumn("jabatan");
        dtm.addColumn("no rekening");
        dtm.addColumn("email");
        dtm.addColumn("no hp");
        return dtm;
    }

    //mrthod untuk mengambil data nya dari database alias 
    //SELECT
    public void tampilkanData() {
        try {
            //persiapan tabel - clear area dtn
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();

            //Query
            this.sql = "SELECT * FROM tb_karyawan";

            //jalankan query
            res = stm.executeQuery(sql);

            //unboxing data ke dalam objek/array
            while (res.next()) {
                Object[] obj = new Object[8];
                //namanya harus sama dengan yang di data base
                obj[0] = res.getString("id_karyawan");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("alamat");
                obj[3] = res.getString("tanggal_masuk");
                obj[4] = res.getString("jabatan");
                obj[5] = res.getString("no_rek");
                obj[6] = res.getString("email");
                obj[7] = res.getString("no_hp");

                //masukkan objek ke dtm
                dtm.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("Gagal Query" + e);
        }
    }

    public boolean tambahData(String nama, String alamat, String tanggal_masuk,
            String jabatan, String no_rek, String email, String no_hp) {
        try {
            // SQL query untuk memasukkan data ke tabel tb_karyawan
            sql = "INSERT INTO tb_karyawan (nama, alamat, tanggal_masuk, jabatan, no_rek, email, no_hp) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";

            // Menggunakan PreparedStatement untuk menghindari SQL Injection
            PreparedStatement pst = stm.getConnection().prepareStatement(sql);
            pst.setString(1, nama);
            pst.setString(2, alamat);
            pst.setString(3, tanggal_masuk);
            pst.setString(4, jabatan);
            pst.setString(5, no_rek); // Gunakan setInt untuk kolom no_rek yang bertipe INT
            pst.setString(6, email);
            pst.setString(7, no_hp);

            // Eksekusi query
            int rowsAffected = pst.executeUpdate();

            // Jika rowsAffected > 0, berarti data berhasil dimasukkan
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Error saat insert data: " + e.getMessage());
            return false;
        }
    }

    //menu laporan gaji
    public DefaultTableModel buatTabelLaporanBulan() {
        DefaultTableModel dtmBulan = new DefaultTableModel();
        dtmBulan.addColumn("id karyawan");
        dtmBulan.addColumn("nama karyawan");
        dtmBulan.addColumn("no rekening");
        dtmBulan.addColumn("gaji perjam");
        dtmBulan.addColumn("jam kerja");
        dtmBulan.addColumn("tunjangan");
        dtmBulan.addColumn("total gaji");
        dtmBulan.addColumn("status");
        return dtmBulan;
    }

    public DefaultTableModel buatTabelLaporanTahun() {
        DefaultTableModel dtmTahun = new DefaultTableModel();
        dtmTahun.addColumn("id karyawan");
        dtmTahun.addColumn("nama karyawan");
        dtmTahun.addColumn("no rekening");
        dtmTahun.addColumn("gaji perjam");
        dtmTahun.addColumn("jam kerja");
        dtmTahun.addColumn("tunjangan");
        dtmTahun.addColumn("total gaji");
        return dtmTahun;
    }

    //mrthod untuk mengambil data nya dari database alias 
    //SELECT
    public void tampilkanLaporanBulan(DefaultTableModel dtmBulan) {
        try {
            // Clear table
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();

            // Query
            this.sql = "SELECT k.id_karyawan, k.nama, k.no_rek, g.gaji_perjam, g.jam_kerja, g.tunjangan, ((g.gaji_perjam * g.jam_kerja)) AS total_gaji, g.status FROM tb_karyawan k NATURAL JOIN tb_gaji g;";

            // Execute query
            res = stm.executeQuery(sql);

            // Populate data
            while (res.next()) {
                Object[] obj = new Object[8];
                obj[0] = res.getString("id_karyawan");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("no_rek");
                obj[3] = res.getString("gaji_perjam");
                obj[4] = res.getString("jam_kerja");
                obj[5] = res.getString("tunjangan");
                obj[6] = res.getString("total_gaji");
                obj[7] = res.getString("status");
                dtmBulan.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("Gagal Query: " + e);
        }
    }

    public void tampilkanLaporanTahun(DefaultTableModel dtmTahun) {
        try {
            // Clear table
            dtm.getDataVector().removeAllElements();
            dtm.fireTableDataChanged();

            // Query
            this.sql = "SELECT k.id_karyawan, k.nama, g.gaji_perjam, g.jam_kerja FROM tb_karyawan k LEFT JOIN tb_gaji g ON k.id_karyawan = g.id_karyawan WHERE k.id_karyawan = ?;";
            // Execute query
            res = stm.executeQuery(sql);

            // Populate data
            while (res.next()) {
                Object[] obj = new Object[4];
                obj[0] = res.getString("id_karyawan");
                obj[1] = res.getString("nama");
                obj[2] = res.getString("gaji_perjam");
                obj[3] = res.getString("jam_kerja");
              
              
             
                dtmTahun.addRow(obj);
            }
        } catch (Exception e) {
            System.out.println("Gagal Query: " + e);
        }
    }

    //coba buat hapus data 
    
}
