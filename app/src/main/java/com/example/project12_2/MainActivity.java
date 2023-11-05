package com.example.project12_2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    EditText name, num, result1, result2;
    Button mButton1, mButton2, mButton3, mButton4, mButton5;
    myDB mMyDB;
    SQLiteDatabase mSQLiteDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.firefox);
        setTitle("직접 풀어보기 12-2");

        name = (EditText) findViewById(R.id.edtName);
        num = (EditText) findViewById(R.id.edtNumber);
        result1 = (EditText) findViewById(R.id.edtNameResult);
        result2 = (EditText) findViewById(R.id.edtNumberResult);
        mButton1 = (Button) findViewById(R.id.btnInit);
        mButton2 = (Button) findViewById(R.id.btnInsert);
        mButton3 = (Button) findViewById(R.id.btnUpdate);
        mButton4 = (Button) findViewById(R.id.btnDelete);
        mButton5 = (Button) findViewById(R.id.btnSelect);
        mMyDB = new myDB(this);

        mButton1.setOnClickListener(new View.OnClickListener() { //초기화 버튼
            @Override
            public void onClick(View view) {
                mSQLiteDatabase = mMyDB.getWritableDatabase();
                mMyDB.onUpgrade(mSQLiteDatabase, 1, 2);
                mSQLiteDatabase.close();
                mButton5.callOnClick();
                name.setText("");
                num.setText("");
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() { //입력 버튼
            @Override
            public void onClick(View view) {
                mSQLiteDatabase = mMyDB.getWritableDatabase();
                mSQLiteDatabase.execSQL("insert into groupTBL values ( '"
                                        + name.getText().toString() + "' , "
                                        + num.getText().toString() + ");");

                mSQLiteDatabase.close();
                Toast.makeText(getApplicationContext(), "입력됨", Toast.LENGTH_SHORT).show();
                mButton5.callOnClick();
                name.setText("");
                num.setText("");
            }
        });
        mButton3.setOnClickListener(new View.OnClickListener() { //수정 버튼
            @Override
            public void onClick(View view) {
                mSQLiteDatabase = mMyDB.getWritableDatabase();
                if (name.getText().toString() != "")
                    mSQLiteDatabase.execSQL("update groupTBL set gNum =" + num.getText() + " where gName = '" + name.getText().toString() + "';");
                mSQLiteDatabase.close();
                Toast.makeText(getApplicationContext(), "수정됨", Toast.LENGTH_SHORT).show();
                mButton5.callOnClick();
                name.setText("");
                num.setText("");
            }
        });
        mButton4.setOnClickListener(new View.OnClickListener() { //삭제 버튼
            @Override
            public void onClick(View view) {
                mSQLiteDatabase = mMyDB.getWritableDatabase();
                if(name.getText().toString() != "")
                    mSQLiteDatabase.execSQL("delete from groupTBL where gName = '"+ name.getText().toString() + "';");
                mSQLiteDatabase.close();
                Toast.makeText(getApplicationContext(), "삭제됨", Toast.LENGTH_SHORT).show();
                mButton5.callOnClick();
                name.setText("");
                num.setText("");
            }
        });
        mButton5.setOnClickListener(new View.OnClickListener() { //조회 버튼
            @Override
            public void onClick(View view) {
                mSQLiteDatabase = mMyDB.getReadableDatabase();
                Cursor cursor;
                cursor = mSQLiteDatabase.rawQuery("select * from groupTBL;", null);

                String strNames = "그룹이름" + "\r\n" + "--------" + "\r\n";
                String strNumbers = "인원" + "\r\n" + "--------" + "\r\n";
                while (cursor.moveToNext()) {
                    strNames += cursor.getString(0) + "\r\n";
                    strNumbers += cursor.getString(1) + "\r\n";
                }

                result1.setText(strNames);
                result2.setText(strNumbers);
                cursor.close();
                mSQLiteDatabase.close();
                name.setText("");
                num.setText("");
            }
        });
    }

    public class myDB extends SQLiteOpenHelper{
        public myDB(@Nullable Context context) {
            super(context, "groupDB", null, 1);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            sqLiteDatabase.execSQL("create table groupTBL (gName char(20) primary key, gNum integer);");
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("drop table if exists groupTBL");
            onCreate(sqLiteDatabase);
        }
    }
}