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
public class MainActivity2 extends AppCompatActivity implements View.OnClickListener{
    Button  BtnArr;
    EditText EdTxtName, EdTxtPrise;
    TextView TxtSumm;
    DBHelper dbHelper;
    SQLiteDatabase database;
    float n = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BtnArr = (Button) findViewById(R.id.BtnArr);
        BtnArr.setOnClickListener(this);
        EdTxtName = (EditText) findViewById(R.id.EdTxtName);
        EdTxtPrise = (EditText) findViewById(R.id.EdTxtPrise);
        TxtSumm = (TextView) findViewById(R.id.TxtSumm);
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

                Button addbt = new Button(this);
                addbt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        View outputDBRoe = (View) view.getParent();
                        ViewGroup outputDB = (ViewGroup) outputDBRoe.getParent();
                        outputDB.removeView(outputDBRoe);
                        outputDB.invalidate();
                        float n1 = Float.parseFloat(outputprise.getText().toString());
                        n = n + n1;
                        TxtSumm.setText("" + n);
                        UpdateTable();
                    }
                });
                params.weight = 3.0f;
                addbt.setLayoutParams(params);
                addbt.setText("Добавить в корзину");
                addbt.setId(cursor.getInt(idIndex));
                tblayrow.addView(addbt);
                tblay.addView(tblayrow);
            } while (cursor.moveToNext());
        } else
            cursor.close();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.BtnArr:
                Toast toast = Toast.makeText(getApplicationContext(),
                        TxtSumm.getText().toString(), Toast.LENGTH_SHORT);
                toast.show();
                TxtSumm.setText(null);
                n=0;
                break;
            default:
                break;
        }
    }
}