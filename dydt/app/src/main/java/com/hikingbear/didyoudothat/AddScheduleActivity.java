//========================================
// 안드로이드 뒤로가기버튼 눌렀을때 intent처리 해야할듯.
//========================================


package com.hikingbear.didyoudothat;

import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
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

import com.hikingbear.database.DBManager;

public class AddScheduleActivity extends AppCompatActivity {
    private String subject, description; // 일정 제목, 일정 내용
    private int hour, minute, week;
    private TimePicker timepicker;
    private ImageButton ib_timePicker;
    private EditText et_time, et_sub, et_des;
    private ActionBar actionBar;
    DBManager dbManager;    //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
    int id_To_Update = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);

        dbManager = new DBManager(getApplicationContext());

        //actionBar = getSupportActionBar();          // 아따 support가 넘나 어려운것..
        //actionBar.setDisplayHomeAsUpEnabled(true);  // 뒤로가기 버튼생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_sub = (EditText)findViewById(R.id.et_subject);
        et_time = (EditText)findViewById(R.id.et_time);
        et_des = (EditText)findViewById(R.id.et_description);

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddScheduleActivity.this, tdpListener, 0, 0, false);   // activity, listener, init hour, init minute, is24hour
                tpd.show();
            }
        });

        /**
         * main에서 일정을 클릭했을때 화면에 띄워줌.
         * 일정이 클릭되면 (extras != null) 일정내용을 불러온다.
         * extras == null 이면 평소의 addschedule 창이 뜬다.
         *
         * 일정을 보여줄땐 actionbar의 아이콘이 delete로 출력되고
         * 평소에는 accept 버튼이 출력된다.
         */
        Bundle extras = getIntent().getExtras();

        if(extras !=null)
        {
            int Value = extras.getInt("_id");

            if(Value>0){
                //means this is the view part not the add contact part.
                Cursor rs = dbManager.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String _subject = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_SUBJECT));
                String _year = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_YEAR));
                String _month = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_MONTH));
                String _day = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_DAY));
                String _hour = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_HOUR));
                String _minute = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_MINUTE));
                String _description = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_DESCRIPTION));

                if (!rs.isClosed()) {
                    rs.close();
                }
                //Button b = (Button)findViewById(R.id.action_accept);
                //b.setVisibility(View.INVISIBLE);

                et_sub.setText((CharSequence)_subject);
                et_sub.setFocusable(false);
                et_sub.setClickable(false);

                et_time.setText(_year+"년 " +_month+"월 "+_day+"일 "+_hour+"시 "+_minute+"분");
                et_time.setFocusable(false);
                et_time.setClickable(false);

                et_des.setText((CharSequence)_description);
                et_des.setFocusable(false);
                et_des.setClickable(false);
            }
        }

    }
    // 백그라운드에서 작동 하는일 없게..?
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    // actionbar 뒤로가기 버튼
    @Override
    public boolean onSupportNavigateUp()  {
        finish();
        Toast.makeText(this, "뒤로가기", Toast.LENGTH_SHORT).show();
        return super.onSupportNavigateUp();
    }
    private TimePickerDialog.OnTimeSetListener tdpListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int _hourOfDay, int _minute) {
            //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT

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
        int id = item.getItemId();
        Bundle extras = getIntent().getExtras();
        if(extras ==null) {

            // 확인버튼.(저장버튼)
            if (id == R.id.action_accept && et_sub.getText()!=null) {
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                subject = et_sub.getText().toString();
                description = et_des.getText().toString();

                //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
                // 날짜는 필요없고 요일이 필요할것 같다..? Today앱인데 필요할까? -> 안필요하다. 제목, 시, 분, 내용 4가지면 될듯. 나중에 수정바람
                dbManager.insertSchedule(subject, 2016, 11, 10, hour, minute, description);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                return true;
            } else if (id == R.id.action_accept && et_sub.getText() == null) {
                Toast.makeText(this, "다시 입력해주세요..", Toast.LENGTH_SHORT).show();
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
