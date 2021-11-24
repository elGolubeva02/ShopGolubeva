package com.example.shop;
import androidx.appcompat.app.AppCompatActivity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button BtnAdd, BtnClear;
    EditText EdTxtName, EdTxtPrise;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;
    float n = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BtnAdd = (Button) findViewById(R.id.BtnAdd);
        BtnAdd.setOnClickListener(this);
        BtnClear = (Button) findViewById(R.id.BtnClear);
        BtnClear.setOnClickListener(this);
        EdTxtName = (EditText) findViewById(R.id.EdTxtName);
        EdTxtPrise = (EditText) findViewById(R.id.EdTxtPrise);
        dbHelper = new DBHelper(this);
        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }
    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priseIndex = cursor.getColumnIndex(DBHelper.KEY_PRISE);
            TableLayout tblay = findViewById(R.id.tblay);
            tblay.removeAllViews();
            do {
                TableRow tblayrow = new TableRow(this);
                tblayrow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                TextView outputid = new TextView(this);
                params.weight = 1.0f;
                outputid.setLayoutParams(params);
                outputid.setText(cursor.getString(idIndex));
                tblayrow.addView(outputid);
                TextView outputname = new TextView(this);
                params.weight = 3.0f;
                outputname.setLayoutParams(params);
                outputname.setText(cursor.getString(nameIndex));
                tblayrow.addView(outputname);
                TextView outputprise= new TextView(this);
                params.weight = 3.0f;
                outputprise.setLayoutParams(params);
                outputprise.setText(cursor.getString(priseIndex));
                tblayrow.addView(outputprise);

                Button deletbt = new Button(this);
                deletbt.setOnClickListener(this);
                params.weight = 3.0f;
                deletbt.setLayoutParams(params);
                deletbt.setText("Удалить запись");
                deletbt.setId(cursor.getInt(idIndex));
                tblayrow.addView(deletbt);

                tblay.addView(tblayrow);
            } while (cursor.moveToNext());
        } else
            cursor.close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnClear:
                database.delete(DBHelper.TABLE_GOODS, null, null);
                TableLayout dbOutput = findViewById(R.id.tblay);
                dbOutput.removeAllViews();
                EdTxtName.setText(null);
                EdTxtPrise.setText(null);
                UpdateTable();
                break;
            case R.id.BtnAdd:
                String name = EdTxtName.getText().toString();
                String prise = EdTxtPrise.getText().toString();
                contentValues = new ContentValues();
                contentValues.put(DBHelper.KEY_NAME, name);
                contentValues.put(DBHelper.KEY_PRISE, prise);
                database.insert(DBHelper.TABLE_GOODS, null, contentValues);
                UpdateTable();
                EdTxtName.setText(null);
                EdTxtPrise.setText(null);
                break;
            default:
                View outputDBRoe = (View) v.getParent();
                ViewGroup outputDB = (ViewGroup) outputDBRoe.getParent();
                outputDB.removeView(outputDBRoe);
                outputDB.invalidate();
                database.delete(DBHelper.TABLE_GOODS,DBHelper.KEY_ID + " = ?", new String[] {String.valueOf(v.getId())});
                contentValues = new ContentValues();
                Cursor cursorUpdater = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);
                if (cursorUpdater.moveToFirst()) {
                    int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                    int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                    int priseIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRISE);
                    int realId = 1;
                    do {
                        if (cursorUpdater.getInt(idIndex) > realId) {
                            contentValues.put(DBHelper.KEY_ID, realId);
                            contentValues.put(DBHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                            contentValues.put(DBHelper.KEY_PRISE, cursorUpdater.getString(priseIndex));
                            database.replace(DBHelper.TABLE_GOODS, null, contentValues);
                        }
                        realId++;
                    } while (cursorUpdater.moveToNext());
                    if (cursorUpdater.moveToLast() && v.getId()!=realId)
                    {
                        database.delete(DBHelper.TABLE_GOODS, DBHelper.KEY_ID+ " = ?", new String[] {cursorUpdater.getString(idIndex)});
                    }

                }
                UpdateTable();
                break;
        }
    }
}
