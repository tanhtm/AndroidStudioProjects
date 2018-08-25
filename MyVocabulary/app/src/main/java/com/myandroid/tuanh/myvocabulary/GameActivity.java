package com.myandroid.tuanh.myvocabulary;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.applib.MyFolder;
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro;
import com.myandroid.tuanh.myvocabulary.database.FolderDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;
import com.myandroid.tuanh.myvocabulary.database.WordProDB;

import java.io.IOException;
import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private int numOfWord=0;
    private WordProDB wordDB;
    private Button buttonPlay;
    private FloatingActionButton floatingActionButtonSel,floatingActionButtonInfo,floatingActionButtonHS;
    private Spinner spinnerF;
    ArrayList<MyFolder> listFolder = new ArrayList<>();
    private int idF =0;
    private Boolean showSel = false;
    private ArrayList<MyWordPro> arrayList;
    private Animation anim;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        create();
        arrayList = wordDB.getList();
        FolderDB folderDB = new FolderDB(GameActivity.this);
        listFolder= folderDB.getList();
        final ArrayAdapter<MyFolder> arrayAdapter = new ArrayAdapter<>(GameActivity.this,android.R.layout.simple_spinner_item, listFolder);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerF.setAdapter(arrayAdapter);
        spinnerF.setSelection(0);
        spinnerF.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                idF = listFolder.get(i).getId();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numOfWord=0;
                if(idF==0)
                    numOfWord= wordDB.getCount();
                else {
                    //Toast.makeText(GameActivity.this,""+arrayList.size(),Toast.LENGTH_LONG).show();
                    for (int i = 0; i < arrayList.size(); i++) {
                        if (idF == arrayList.get(i).getIdFolder())
                            numOfWord++;
                    }
                }
                if(numOfWord>4) {
                    final Dialog dialog = new Dialog(GameActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.menu_game);
                    dialog.show();
                    Button buttonGW= dialog.findViewById(R.id.buttonGW);
                    buttonGW.startAnimation(anim);
                    Button buttonCC= dialog.findViewById(R.id.buttonCC);
                    buttonCC.startAnimation(anim);
                    Button buttonPG= dialog.findViewById(R.id.buttonPG);
                    buttonPG.startAnimation(anim);
                    buttonCC.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(GameActivity.this,GameCCActivity.class);
                            intent.putExtra("idfolder",idF);
                            dialog.dismiss();
                            startActivity(intent);
                        }
                    });
                    buttonGW.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(GameActivity.this,GameGWActivity.class);
                            intent.putExtra("idfolder",idF);
                            dialog.dismiss();
                            startActivity(intent);
                        }
                    });
                    buttonPG.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(GameActivity.this,PerfectGameActivity.class);
                            intent.putExtra("idfolder",idF);
                            dialog.dismiss();
                            startActivity(intent);
                        }
                    });
                }else
                    Toast.makeText(GameActivity.this,"Số lượng từ vựng của bạn phải lớn hơn 4 :"+numOfWord,Toast.LENGTH_LONG).show();
            }

        });
        floatingActionButtonSel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(showSel){
                    floatingActionButtonInfo.hide();
                    floatingActionButtonHS.hide();
                    showSel= false;
                }
                else {

                    floatingActionButtonInfo.show();
                    floatingActionButtonHS.show();
                    showSel= true;
                }

            }
        });
        floatingActionButtonHS.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
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
        floatingActionButtonInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
    }
    private void create(){
        buttonPlay= findViewById(R.id.buttonPlay);
        spinnerF= findViewById(R.id.spinnerFolder);
        floatingActionButtonSel = findViewById(R.id.floatingActionButtonSel);
        floatingActionButtonInfo = findViewById(R.id.floatingActionButtonInfo);
        floatingActionButtonHS = findViewById(R.id.floatingActionButtonHS);
        floatingActionButtonInfo.hide();
        floatingActionButtonHS.hide();
        createDB();
        wordDB= new WordProDB(GameActivity.this);
        numOfWord= wordDB.getCount();
        ////
        anim = AnimationUtils.loadAnimation(GameActivity.this,R.anim.sacle);
        buttonPlay.setAnimation(anim);
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
