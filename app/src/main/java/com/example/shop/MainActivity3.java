package com.example.shop;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity3 extends AppCompatActivity implements View.OnClickListener {
    Button BtnReg, BtnAuth;
    EditText EdTxtLogin, EdTxtPassword;
    DBHelper dbHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);

        BtnAuth = (Button)findViewById(R.id.BtnAuth);
        BtnAuth.setOnClickListener(this);
        BtnReg = (Button)findViewById(R.id.BtnReg);
        BtnReg.setOnClickListener(this);
        EdTxtLogin = (EditText) findViewById(R.id.EdTxtLogin);
        EdTxtPassword = (EditText) findViewById(R.id.EdTxtPassword);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBHelper.KEY_NAMEP, "admin");
        contentValues.put(DBHelper.KEY_PASSWORDS, "admin");
        database.insert(DBHelper.TABLE_PEOPLE, null, contentValues);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.BtnAuth:
                Cursor logCursor = database.query(DBHelper.TABLE_PEOPLE, null, null, null, null, null, null);
                boolean logged = false;
                if (logCursor.moveToFirst()) {
                    int usernameIndex = logCursor.getColumnIndex(DBHelper.KEY_NAMEP);
                    int passwordIndex = logCursor.getColumnIndex(DBHelper.KEY_PASSWORDS);
                    do {
                        if (EdTxtLogin.getText().toString().equals(logCursor.getString(usernameIndex)) && EdTxtPassword.getText().toString().equals(logCursor.getString(passwordIndex))) {
                            if (EdTxtLogin.getText().toString().equals("admin") && EdTxtPassword.getText().toString().equals("admin")) {
                                logged = true;
                                Intent intent = new Intent(MainActivity3.this, MainActivity.class);
                                startActivity(intent);
                                break;
                            } else {
                                logged = true;
                                Intent intent = new Intent(MainActivity3.this, MainActivity2.class);
                                startActivity(intent);
                                break;
                            }
                        }
                    } while (logCursor.moveToNext());
                }
                logCursor.close();
                if (!logged)
                    Toast.makeText(this, "Пользователь не найден.", Toast.LENGTH_LONG).show();
                break;
            case R.id.BtnReg:
                Cursor signCursor = database.query(DBHelper.TABLE_PEOPLE, null, null, null, null, null, null);
                boolean finded = false;
                if (signCursor.moveToFirst()) {
                    int usernameIndex = signCursor.getColumnIndex(DBHelper.KEY_NAMEP);
                    do {
                        if (EdTxtLogin.getText().toString().equals(signCursor.getString(usernameIndex))) {
                            Toast.makeText(this, "Введенный логин уже существует в базе.", Toast.LENGTH_LONG).show();
                            finded = true;
                            break;
                        }
                    } while (signCursor.moveToNext());
                }
                if (!finded) {
                    ContentValues contentValues = new ContentValues();
                    contentValues.put(DBHelper.KEY_NAMEP, EdTxtLogin.getText().toString());
                    contentValues.put(DBHelper.KEY_PASSWORDS, EdTxtPassword.getText().toString());
                    database.insert(DBHelper.TABLE_PEOPLE, null, contentValues);
                    Toast.makeText(this, "Вы успешно зарегестрировались.", Toast.LENGTH_LONG).show();
                }
                signCursor.close();
                break;
        }

    }
}