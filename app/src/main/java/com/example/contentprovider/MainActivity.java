package com.example.contentprovider;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    TextView tv_word,tv_wordmeaning,tv_wordsample;
    EditText ev_word;
    Button btn_add,btn_update,btn_delete,btn_check;
    private ContentResolver resolver;
    String words;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        resolver = this.getContentResolver();

        bangding();
        Log.d("MyTag",words);

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                words = ev_word.getText().toString();
                if (words.equals("")){
                    tv_word.setText("请输入单词");
                    tv_wordmeaning.setText("");
                    tv_wordsample.setText("");
                } else  {
                    Cursor cursor = resolver.query(Words.Word.CONTENT_URI,
                            new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD,
                                    Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                            null,null,null);
                    if (cursor == null) {
                        Toast.makeText(MainActivity.this,"数据库中没有单词",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (cursor.moveToFirst()) {
                        do {
                            if (cursor.getString(
                                    cursor.getColumnIndex(
                                            Words.Word.COLUMN_NAME_WORD
                                    )).equals(words)) {
                                tv_word.setText(
                                        cursor.getString(
                                                cursor.getColumnIndex(
                                                        Words.Word.COLUMN_NAME_WORD
                                                )
                                        )
                                );
                                tv_wordmeaning.setText(
                                        cursor.getString(
                                                cursor.getColumnIndex(
                                                        Words.Word.COLUMN_NAME_MEANING
                                                )
                                        )
                                );
                                tv_wordsample.setText(
                                        cursor.getString(
                                                cursor.getColumnIndex(
                                                        Words.Word.COLUMN_NAME_SAMPLE
                                                )
                                        )
                                );
                                Toast.makeText(MainActivity.this,"查找成功",Toast.LENGTH_SHORT).show();
                                return;
                            } else {
                                continue;
                            }
                        } while (cursor.moveToNext());
                        Toast.makeText(MainActivity.this,"没有找到单词",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                words = ev_word.getText().toString();
                final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert,null);
                new AlertDialog.Builder(MainActivity.this).setTitle("新增单词")
                        .setView(tableLayout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                                String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                                String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();

                                ContentValues values = new ContentValues();

                                values.put(Words.Word.COLUMN_NAME_WORD,strWord);
                                values.put(Words.Word.COLUMN_NAME_MEANING,strMeaning);
                                values.put(Words.Word.COLUMN_NAME_SAMPLE,strSample);

                                Uri newuri = resolver.insert(Words.Word.CONTENT_URI,values);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                words = ev_word.getText().toString();
                String wordID = "";
                if (tv_word.getText().toString().equals("")) {
                    Toast.makeText(MainActivity.this,"请先查询单词",Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    try {
                        resolver.delete(Words.Word.CONTENT_URI, Words.Word.COLUMN_NAME_WORD + " = " + tv_word.getText().toString(),null);
                        Toast.makeText(MainActivity.this,"删除成功",Toast.LENGTH_SHORT).show();
                        tv_word.setText("");
                        tv_wordsample.setText("");
                        tv_wordmeaning.setText("");
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this,"删除失败",Toast.LENGTH_SHORT).show();
                        tv_word.setText("");
                        tv_wordsample.setText("");
                        tv_wordmeaning.setText("");
                        e.printStackTrace();
                    }
                    /*Cursor cursor = resolver.query(Words.Word.CONTENT_URI,
                            new String[]{Words.Word._ID, Words.Word.COLUMN_NAME_WORD,
                                    Words.Word.COLUMN_NAME_MEANING, Words.Word.COLUMN_NAME_SAMPLE},
                            null,null,null);
                    if (cursor == null) {
                        Toast.makeText(MainActivity.this,"数据库中没有单词",Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (cursor.moveToFirst()) {
                        do {

                            if (cursor.getString(cursor.getColumnIndex(Words.Word.COLUMN_NAME_WORD)).equals(tv_word.getText().toString())) {
                                wordID = cursor.getString(cursor.getColumnIndex(Words.Word._ID));
                                break;
                            }

                        } while (cursor.moveToNext());
                    }
                    if (wordID.equals("")){
                        Toast.makeText(MainActivity.this,"已经没有该单词",Toast.LENGTH_SHORT).show();
                    } else {
                        Uri uri = Uri.parse(Words.Word.CONTENT_URI_STRING + "/" + wordID);
                        int result = resolver.delete(uri,null,null);
                    }*/
                }
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                words = ev_word.getText().toString();
                final TableLayout tableLayout = (TableLayout) getLayoutInflater().inflate(R.layout.insert,null);
                new AlertDialog.Builder(MainActivity.this).setTitle("修改单词")
                        .setView(tableLayout)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strWord = ((EditText) tableLayout.findViewById(R.id.txtWord)).getText().toString();
                                String strMeaning = ((EditText) tableLayout.findViewById(R.id.txtMeaning)).getText().toString();
                                String strSample = ((EditText) tableLayout.findViewById(R.id.txtSample)).getText().toString();

                                ContentValues values = new ContentValues();

                                values.put(Words.Word.COLUMN_NAME_WORD,strWord);
                                values.put(Words.Word.COLUMN_NAME_MEANING,strMeaning);
                                values.put(Words.Word.COLUMN_NAME_SAMPLE,strSample);

                                int result = resolver.update(Words.Word.CONTENT_URI,values, Words.Word.COLUMN_NAME_WORD + " = " + words,null);
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .create().show();
            }
        });
    }

    private void bangding(){
        tv_word = findViewById(R.id.wordShow);
        tv_wordmeaning = findViewById(R.id.wordmeaningShow);
        tv_wordsample = findViewById(R.id.wordsampleShow);
        ev_word = findViewById(R.id.wordEdit);
        btn_add = findViewById(R.id.addWord);
        btn_check = findViewById(R.id.checkWord);
        btn_delete = findViewById(R.id.deleteWord);
        btn_update = findViewById(R.id.updateWord);
    }

}