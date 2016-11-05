package com.hikingbear.didyoudothat;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddScheduleActivity extends AppCompatActivity {
    private String subject, description; // 일정 제목, 일정 내용
    private int hour, minute, week;
    private TimePicker timepicker;
    private ImageButton ib_timePicker;
    private EditText et_time, et_sub, et_des;
    private ActionBar actionBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        //actionBar = getSupportActionBar();          // 아따 support가 넘나 어려운것..
        //actionBar.setDisplayHomeAsUpEnabled(true);  // 뒤로가기 버튼생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_time = (EditText)findViewById(R.id.et_time);
        et_sub = (EditText)findViewById(R.id.et_subject);
        et_des = (EditText)findViewById(R.id.et_description);

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddScheduleActivity.this, tdpListener, 0, 0, false);   // activity, listener, init hour, init minute, is24hour
                tpd.show();
            }
        });

    }

    // actionbar 뒤로가기 버튼
    @Override
    public boolean onSupportNavigateUp()  {
        Toast.makeText(this, "뒤로가기", Toast.LENGTH_SHORT).show();
        return super.onSupportNavigateUp();
    }
    private TimePickerDialog.OnTimeSetListener tdpListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int _hourOfDay, int _minute) {
            // 시간 정하고 확인버튼 눌렀을 때
            //Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            et_time.setText(_hourOfDay+"시 "+_minute+"분");
            hour = _hourOfDay;
            minute = _minute;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.accept, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        // 확인버튼.(저장버튼)
        if (id == R.id.action_accept) {
            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
            subject = et_sub.getText().toString();
            description = et_des.getText().toString();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
