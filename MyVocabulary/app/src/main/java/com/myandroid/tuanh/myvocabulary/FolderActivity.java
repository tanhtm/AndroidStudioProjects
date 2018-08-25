package com.myandroid.tuanh.myvocabulary;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.myandroid.tuanh.myvocabulary.adapter.AdapterFolder;
import com.myandroid.tuanh.myvocabulary.applib.MyFolder;
import com.myandroid.tuanh.myvocabulary.database.FolderDB;
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController;

import java.io.IOException;
import java.util.ArrayList;

public class FolderActivity extends AppCompatActivity {
    private FloatingActionButton floatingActionButtonAdd;
    private ListView listViewFolder;
    private FolderDB folderDB;
    private ArrayList<MyFolder> myFolderArrayList = new ArrayList<>();

    private void create()
    {
        floatingActionButtonAdd= findViewById(R.id.floatingActionButtonAdd);
        listViewFolder= findViewById(R.id.listViewFolder);
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

    private void getDataFolderFromDB(){
        createDB();
        ArrayList<MyFolder> arrayList;
        folderDB = new FolderDB(FolderActivity.this);
        arrayList= folderDB.getList();
        myFolderArrayList= arrayList;
    }

    private void setListViewFolder(){
        AdapterFolder adapterFolder = new AdapterFolder(FolderActivity.this,myFolderArrayList);
        listViewFolder.setAdapter(adapterFolder);
    }
    private void onClick(){
        final Dialog dialog = new Dialog(FolderActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_addfolder);
        floatingActionButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });
        Button buttonAdd = dialog.findViewById(R.id.buttonAdd);
        Button buttonQuit = dialog.findViewById(R.id.buttonQuit);
        final EditText editText = dialog.findViewById(R.id.editTextName);
        buttonQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.hide();
            }
        });
        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MyFolder myFolder = new MyFolder(editText.getText().toString());
                createDB();
                folderDB = new FolderDB(FolderActivity.this);
                myFolder.setId(folderDB.newId());
                if(folderDB.isExisted(myFolder))
                    Toast.makeText(FolderActivity.this,"Tên đã tồn tại !",Toast.LENGTH_SHORT).show();
                else {
                    if (folderDB.insertFolder(myFolder)) {
                        Toast.makeText(FolderActivity.this, "Thêm thành công !", Toast.LENGTH_SHORT).show();
                        myFolderArrayList.add(myFolder);
                        setListViewFolder();
                    }
                    else
                        Toast.makeText(FolderActivity.this, "Lỗi !", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder);

        create();
        getDataFolderFromDB();
        setListViewFolder();

        onClick();
    }
}
