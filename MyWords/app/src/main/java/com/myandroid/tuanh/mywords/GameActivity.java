package com.myandroid.tuanh.mywords;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;

import database.SQLiteDataController;
import database.WordDB;

public class GameActivity extends AppCompatActivity {

    private int numOfWord;
    private WordDB wordDB;
    private Button buttonPlay,buttonHS,buttonInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        create();
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(numOfWord>4) {
                    Dialog dialog = new Dialog(GameActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.menu_game);
                    dialog.show();
                    Button buttonGW= dialog.findViewById(R.id.buttonGW);
                    Button buttonCC= dialog.findViewById(R.id.buttonCC);
                    buttonCC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(GameActivity.this,GameCCActivity.class));
                        }
                    });
                    buttonGW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                                startActivity(new Intent(GameActivity.this,GameGWActivity.class));
                        }
                    });
                }else
                    Toast.makeText(GameActivity.this,"Số lượng từ vựng của bạn phải lớn hơn 4",Toast.LENGTH_LONG).show();
            }

        });
        buttonHS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(GameActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_high_score);
                dialog.show();
                Button buttonNum= dialog.findViewById(R.id.buttonNumW);
                //Button buttonHSGW= dialog.findViewById(R.id.buttonHSGW);
                Button buttonHSCC= dialog.findViewById(R.id.buttonHSCC);
                buttonNum.setText("Số lượng từ vựng: "+ numOfWord );
                /*
                 xử lí điểm cao
                */
                SharedPreferences sharedPreferences= getSharedPreferences("pointgame",MODE_PRIVATE);
                buttonHSCC.setText("Đuổi từ- Bắt chữ: "+sharedPreferences.getInt("point",0));
            }
        });
        final Dialog dialog= new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_game_info);
        buttonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
    private void create(){
        buttonPlay= (Button) findViewById(R.id.buttonPlay);
        buttonHS= (Button) findViewById(R.id.buttonHS);
        buttonInfo= (Button)findViewById(R.id.buttonInfo);
        createDB();
        wordDB= new WordDB(GameActivity.this);
        numOfWord= wordDB.getCount();
    }
    private void createDB() {
// khởi tạo database
        SQLiteDataController sql = new SQLiteDataController(this);
        try {
            sql.isCreatedDatabase();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
