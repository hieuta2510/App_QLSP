//package edu.ptit.saleapp.database;
//
//import android.content.ContentValues;
//import android.content.Context;
//import android.database.Cursor;
//import android.database.sqlite.SQLiteDatabase;
//import android.database.sqlite.SQLiteOpenHelper;
//
//import androidx.annotation.Nullable;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import edu.ptit.saleapp.model.Product;
//
//public class SQLiteHelper extends SQLiteOpenHelper {
//    private static final String DATABASE_NAME = "saleapp.db";
//    private static int DATABASE_VERSION = 1;
//
//    public SQLiteHelper(@Nullable Context context) {
//        super(context, DATABASE_NAME, null, DATABASE_VERSION);
//    }
//
//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        String sql = "CREATE TABLE items(" +
//                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
//                "tenSach TEXT, tacGia TEXT, phamVi TEXT, doiTuong TEXT, danhGia REAL)";
//        db.execSQL(sql);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }
//
//    @Override
//    public void onOpen(SQLiteDatabase db) {
//        super.onOpen(db);
//    }
//
//    //getAll order by date desc
//    public List<Item> getAll() {
//        List<Item> list = new ArrayList<>();
//        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
//        String order = "danhGia DESC";
//        Cursor rs = sqLiteDatabase.query("items",
//                null, null, null,
//                null, null, order);
//        while (rs != null && rs.moveToNext()) {
//            int id = rs.getInt(0);
//            String tenSach = rs.getString(1);
//            String tacGia = rs.getString(2);
//            String phamVi = rs.getString(3);
//            String doiTuong = rs.getString(4);
//            Float danhGia = rs.getFloat(5);
//            list.add(new Item(id, tenSach, tacGia, phamVi, doiTuong, danhGia));
//        }
//        return list;
//    }
//
//    public long addItem(Item i) {
//        ContentValues values = new ContentValues();
//        values.put("tenSach", i.getTenSach());
//        values.put("tacGia", i.getTacGia());
//        values.put("phamVi", i.getPhamVi());
//        values.put("doiTuong", i.getDoiTuong());
//        values.put("danhGia", i.getDanhGia());
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        String whereClause = " id = ?";
//        String[] whereArgs = {Integer.toString(i.getId())};
//        return sqLiteDatabase.insert("items", null, values);
//    }
//
////    public List<Item> getByDate(String date) {
////        List<Item> list = new ArrayList<>();
////        String whereClause = "date like ?";
////        String[] whereArgs = {date};
////        SQLiteDatabase st = getReadableDatabase();
////        Cursor rs = st.query("items",
////                null, whereClause, whereArgs,
////                null, null, null);
////        while (rs != null && rs.moveToNext()) {
////            int id= rs.getInt(0);
////            String title = rs.getString(1);
////            String c = rs.getString(2);
////            String p = rs.getString(3);
////            list.add(new Item(id,title,c,p,date));
////        }
////        return list;
////    }
//
//    public int update(Item i) {
//        ContentValues values = new ContentValues();
//        values.put("tenSach", i.getTenSach());
//        values.put("tacGia", i.getTacGia());
//        values.put("phamVi", i.getPhamVi());
//        values.put("doiTuong", i.getDoiTuong());
//        values.put("danhGia", i.getDanhGia());
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        String whereClause = " id = ?";
//        String[] whereArgs = {Integer.toString(i.getId())};
//        return sqLiteDatabase.update("items", values, whereClause, whereArgs);
//    }
//
//    public int delete(Item i) {
//        String whereClause = " id = ?";
//        String[] whereArgs = {Integer.toString(i.getId())};
//        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
//        return sqLiteDatabase.delete("items", whereClause, whereArgs);
//    }
//
//    public List<Item> searchByTacGia(String key) {
//        List<Item> list = new ArrayList<>();
//        String whereClause = "tacGia like ? ORDER BY danhGia DESC ";
//        String[] whereArgs = {"%" + key + "%"};
//        SQLiteDatabase st = getReadableDatabase();
//        Cursor rs = st.query("items",
//                null, whereClause, whereArgs,
//                null, null, null);
//        while (rs != null && rs.moveToNext()) {
//            int id = rs.getInt(0);
//            String tenSach = rs.getString(1);
//            String tacGia = rs.getString(2);
//            String phamVi = rs.getString(3);
//            String doiTuong = rs.getString(4);
//            Float danhGia = rs.getFloat(5);
//            list.add(new Item(id, tenSach, tacGia, phamVi, doiTuong, danhGia));
//        }
//        return list;
//    }
//
//    public List<Item> searchByTacGiaOrTenSach(String key) {
//        List<Item> list = new ArrayList<>();
//        String whereClause = "tacGia like ? OR tenSach like ? ORDER BY danhGia DESC ";
//        String[] whereArgs = {"%" + key + "%", "%" + key + "%"};
//        SQLiteDatabase st = getReadableDatabase();
//        Cursor rs = st.query("items",
//                null, whereClause, whereArgs,
//                null, null, null);
//        while (rs != null && rs.moveToNext()) {
//            int id = rs.getInt(0);
//            String tenSach = rs.getString(1);
//            String tacGia = rs.getString(2);
//            String phamVi = rs.getString(3);
//            String doiTuong = rs.getString(4);
//            Float danhGia = rs.getFloat(5);
//            list.add(new Item(id, tenSach, tacGia, phamVi, doiTuong, danhGia));
//        }
//        return list;
//    }
//}