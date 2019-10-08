package com.example.main;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JoinActivity extends AppCompatActivity {
    ImageButton btnDate;
    Calendar cal = Calendar.getInstance();
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH);
    int day = cal.get(Calendar.DATE);
    EditText edtName,edtID,edtPW,edtPWCheck,edtNickName,edtEmail,edtDomain;
    Button btnCheck;
    TextView txtDate;
    String name,id,pw,nickname,date,email,gender;
    boolean login=false;
    RadioGroup rgGender;
    RadioButton rgMale, rgFemale;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_layout);

        btnDate = findViewById(R.id.btnDate);
        edtName=findViewById(R.id.edtName);
        edtID=findViewById(R.id.edtID);
        edtPW=findViewById(R.id.edtPW);
        edtPWCheck=findViewById(R.id.edtPWCheck);
        edtNickName=findViewById(R.id.edtNickName);
        edtEmail=findViewById(R.id.edtEmail);
        edtDomain=findViewById(R.id.edtDomain);
        btnCheck=findViewById(R.id.btnCheck);
        txtDate=findViewById(R.id.txtDate);
        rgGender=findViewById(R.id.rgGender);
        rgMale=findViewById(R.id.rgMale);
        rgFemale=findViewById(R.id.rgFemale);
        name=edtName.getText().toString();
        id=edtID.getText().toString();
        pw=edtPW.getText().toString();

        //TODO 비밀번호와 비밀번호 재입력 확인
        if(pw==edtPWCheck.getText().toString()){
            login=true;
        }else{
            edtPWCheck.setHint("비밀번호 불일치");
            edtPWCheck.setHintTextColor(Color.rgb(204,61,61));
        }

        //TODO 성별선택
        rgGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                if(rgMale.isChecked()){
                    gender="남";
                }else if(rgFemale.isChecked()) gender="여";

            }
        });


        //TODO 아이디 중복체크
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<ResponseRepeat> res = Net.getInstance().getApi().getRepeat(id);
                res.enqueue(new Callback<ResponseRepeat>() {
                    @Override
                    public void onResponse(Call<ResponseRepeat> call, Response<ResponseRepeat> response) {
                        if(response.isSuccessful()){
                            ResponseRepeat responseGet = response.body();
                            if(responseGet.getRepeat()==false){
                                Toast.makeText(JoinActivity.this,"중복된 아이디입니다.",Toast.LENGTH_LONG).show();
                            }else{
                                login=true;
                            }
                        }else Toast.makeText(JoinActivity.this,"통신1 에러",Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailure(Call<ResponseRepeat> call, Throwable t) {
                        Toast.makeText(JoinActivity.this,"통신3 에러",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        //TODO 날짜 선택
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog dateDialog = new DatePickerDialog(JoinActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Toast.makeText(JoinActivity.this, year+"/"+(month+1)+"/"+dayOfMonth, Toast.LENGTH_SHORT).show();
                        cal.set(year, month, dayOfMonth);
                        txtDate.setText(year+"/"+(month+1)+"/"+dayOfMonth);
                    }
                }, year, month, day);
                dateDialog.show();
            }
        });
    }
}