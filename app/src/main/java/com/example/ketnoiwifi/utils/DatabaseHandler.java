package com.example.ketnoiwifi.utils;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.ketnoiwifi.model.Password;

import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "PasswordManager";
    private static final int DATABASE_VERSION = 1;
    private static final String KEY_ID = "id";
    private static final String KEY_PASSWORD = "password";
    private static final String TABLE_NAME = "password";
    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_passwords_table = String.format("CREATE TABLE %s( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT)", TABLE_NAME, KEY_ID, KEY_PASSWORD);
        db.execSQL(create_passwords_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_students_table = String.format("DROP TABLE IF EXISTS %s", TABLE_NAME);
        db.execSQL(drop_students_table);

        onCreate(db);
    }

    public void addPassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
//        values.put(KEY_ID, password.getId());
        values.put(KEY_PASSWORD, password.getPassword());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public Password getPasswordWithID(int passwordID) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, null, KEY_ID + " = ?", new String[] { String.valueOf(passwordID) },null, null, null);
        if(cursor != null)
            cursor.moveToFirst();
        Password password = new Password(cursor.getInt(0), cursor.getString(1));
        cursor.close();
        return password;
    }

    public ArrayList<Password> getAllPassword() {
        ArrayList<Password>  passwords = new ArrayList<Password>();

        String query = "SELECT * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();

        while(cursor.isAfterLast() == false) {
            Password password = new Password(cursor.getInt(0), cursor.getString(1));
            passwords.add(password);
            cursor.moveToNext();
        }
        cursor.close();
        return passwords;
    }

    public void updatePassword(Password password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_PASSWORD, password.getPassword());
        db.update(TABLE_NAME, values, KEY_ID + " = ?", new String[] { String.valueOf(password.getId()) });
        db.close();
    }

    public void deleteStudent(int passwordId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, KEY_ID + " = ?", new String[] { String.valueOf(passwordId) });
        db.close();
    }
}
