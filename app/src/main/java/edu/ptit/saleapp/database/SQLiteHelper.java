package edu.ptit.saleapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import edu.ptit.saleapp.model.Act;
import edu.ptit.saleapp.model.User;

public class SQLiteHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME ="saleapp.db";
    private static int DATABASE_VERSION = 1;

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE users(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "email TEXT, password TEXT, fullname TEXT, dob TEXT)";
        String sql2 = "CREATE TABLE history(" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "username TEXT, fullname TEXT, act TEXT)";
        db.execSQL(sql);
        db.execSQL(sql2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
    }

    public long addUser(User i) {
        ContentValues values = new ContentValues();
        values.put("email", i.getEmail());
        values.put("password", i.getPassword());
        values.put("fullname", i.getFullname());
        values.put("dob", i.getDob());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("users", null, values);
    }

    public int updateUser(User i) {
        ContentValues values = new ContentValues();
        values.put("email", i.getEmail());
        values.put("password", i.getPassword());
        values.put("fullname", i.getFullname());
        values.put("dob", i.getDob());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String whereClause = " id = ?";
        String[] whereArgs = {Integer.toString(i.getId())};
        System.out.println( );
        return sqLiteDatabase.update("users", values, whereClause, whereArgs);
    }

    public User getUserByEmail(String key) {
        User us = new User();
        String whereClause = "email = ? ";
        String[] whereArgs = {key};
        SQLiteDatabase st = getReadableDatabase();
        Cursor rs = st.query("users",
                null, whereClause, whereArgs,
                null, null, null);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String email = rs.getString(1);
            String password = rs.getString(2);
            String fullname = rs.getString(3);
            String dob = rs.getString(4);
            us.setId(id);
            us.setEmail(email);
            us.setPassword(password);
            us.setFullname(fullname);
            us.setDob(dob);
        }
        return us;
    }

    public long insertDataHistory(Act act)
    {
        ContentValues values = new ContentValues();
        values.put("username", act.getUsername());
        values.put("fullname", act.getFullname());
        values.put("act", act.getAct());
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.insert("history", null, values);
    }

    public List<Act> getAllHistory()
    {
        List<Act> list = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = getReadableDatabase();
        String order = "username ASC";
        Cursor rs = sqLiteDatabase.query("history",
                null, null, null,
                null, null, order);
        while (rs != null && rs.moveToNext()) {
            int id = rs.getInt(0);
            String username = rs.getString(1);
            String fullname = rs.getString(2);
            String act = rs.getString(3);
            list.add(new Act(id, username, fullname, act));
        }
        return list;
    }

    public int deleteHistory(Act act)
    {
        int id = act.getId();
        System.out.println("Here ne");
        System.out.println(id);
        System.out.println("Here ne");
        String whereClause = " id = ?";
        String[] whereArgs = {Integer.toString(id)};
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        return sqLiteDatabase.delete("history", whereClause, whereArgs);
    }
}