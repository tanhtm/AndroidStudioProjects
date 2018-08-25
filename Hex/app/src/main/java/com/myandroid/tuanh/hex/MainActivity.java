package com.myandroid.tuanh.hex;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText edtNum;
    EditText edtFrom;
    EditText edtTo;
    Button btnResult;
    TextView txtvResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast t = Toast.makeText(
                MainActivity.this,
                "By Tu Anh Nguyen",
                Toast.LENGTH_LONG
        );
        t.show();
        edtNum=(EditText)findViewById(R.id.editTextNum);
        edtFrom=(EditText)findViewById(R.id.editTextFrom);
        edtTo=(EditText)findViewById(R.id.editTextTo);
        btnResult=(Button)findViewById(R.id.buttonResult);
        txtvResult=(TextView)findViewById(R.id.textViewResult);
        btnResult.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                Basis a = new Basis(edtNum.getText().toString(),edtFrom.getText().toString(),edtTo.getText().toString());
                a.Run();
                txtvResult.setText(a.strOut);
            }
        });
    }
}
