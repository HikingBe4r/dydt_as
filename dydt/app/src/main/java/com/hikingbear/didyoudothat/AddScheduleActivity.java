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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.hikingbear.database.DBManager;

import java.util.Calendar;
import java.util.Date;

public class AddScheduleActivity extends AppCompatActivity {

    private String subject, description; // 일정 제목, 일정 내용
    private ImageButton ib_applist;      // 앱선택 버튼
    private EditText et_time, et_sub, et_des; // edittext 시간, 제목, 내용
    private TextView tv_appname;         // 선택한 앱 이름

    DBManager dbManager;    // _id, subject(제목), description(내용), weekday(요일), hour(시), minute(분), deleteable(임시저장 여부) 0(false)or 1(true), packagename(com.example.~~) 랑 appname(어플밑에 이름)
    int id_To_Update = 0;
    static Bundle extras;

    // 현재시간
    Calendar c = Calendar.getInstance();
    long cTimeWeekday = c.get(Calendar.DAY_OF_WEEK);
    long cTimeHour = c.get(Calendar.HOUR);
    long cTimeMinute = c.get(Calendar.MINUTE);

    // 선택한시간. 저장된시간.
    long sTimeWeekday;
    long sTimeHour;
    long sTimeMinute;

    boolean timeSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addschedule);

        dbManager = new DBManager(getApplicationContext());

        //actionBar = getSupportActionBar();          // 아따 support가 넘나 어려운것..
        //actionBar.setDisplayHomeAsUpEnabled(true);  // 뒤로가기 버튼생성
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_sub = (EditText)findViewById(R.id.et_subject);
        et_time = (EditText)findViewById(R.id.et_time);
        et_des = (EditText)findViewById(R.id.et_description);
        tv_appname = (TextView) findViewById(R.id.tv_appname);

        // 이렇게 하면 안됨. if extras!= null 일때도 이걸 쓸데가 있기때문에 이렇게 하면 안됨.
        //if(extras != null) {
        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog tpd = new TimePickerDialog(AddScheduleActivity.this, tdpListener, 0, 0, false);   // activity, listener, init hour, init minute, is24hour
                tpd.show();
                }
            });
        //}


        /**
         *  앱선택버튼
         *  앱선택버튼을 내 스마트폰에 설치된 어플 목록이 출력되는 ViewApplicationList activity가 열린다.
         *  그곳에서 앱을 선택하면 Bundle에 저장해 가져오는게 목표.
         *
         *  bundle이 있으면 initDB를 하지말고 _id를 applistview로 가져가고,
         *  bundle이 없으면 initDB를 해서 _id를 만들어준다.
         */

        ib_applist = (ImageButton) findViewById(R.id.ib_applist);
        ib_applist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(AddScheduleActivity.this, "앱선택을 클릭하셨네요", Toast.LENGTH_SHORT).show();
                Bundle tempBundle = new Bundle();
                if(extras != null) {
                    tempBundle.putInt("_id", extras.getInt("_id"));
                } else if (extras == null) {
                    dbManager.initDB();
                    tempBundle.putInt("_id", dbManager.lastRecord());
                }
                Intent intent = new Intent (getApplicationContext(), ViewApplicationList.class);
                intent.putExtras(tempBundle);
                startActivity(intent);

            }
        });

        cTimeWeekday = c.get(Calendar.DAY_OF_WEEK);
        cTimeHour = c.get(Calendar.HOUR);
        cTimeMinute = c.get(Calendar.MINUTE);

        et_time.setText(getWeekday((int)cTimeWeekday)+"요일 "+cTimeHour+"시 "+cTimeMinute+"분");

        /**
         * main에서 일정을 클릭했을때 화면에 띄워줌.
         * 일정이 클릭되면 (extras != null) 일정내용을 불러온다.
         * extras == null 이면 평소의 addschedule 창이 뜬다.
         *
         * 일정을 보여줄땐 actionbar의 아이콘이 delete로 출력되고
         * 평소에는 menu_addschedule 메뉴가 출력된다.
         */
        extras = getIntent().getExtras();

        try {
            System.out.println("============================================");
            System.out.println("extras 요소");
            System.out.println("extras.getInt(_id): "+extras.getInt("_id"));
            System.out.println("extras.getString(subject): "+extras.getString("subject"));
            System.out.println("extras.getString(app): "+extras.getString("appname"));
            System.out.println("============================================");
            int Value = extras.getInt("_id");
            if (Value >= 0) {

                Cursor rs = dbManager.getData(Value);
                id_To_Update = Value;
                rs.moveToFirst();

                String _subject = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_SUBJECT));
                String _description = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_DESCRIPTION));
                long _weekday = rs.getLong(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_WEEKDAY));
                long _hour = rs.getLong(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_HOUR));
                long _minute = rs.getLong(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_MINUTE));
                String _packagename = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_PACKAGENAME));   // 사실 여기선 쓸일이..
                String _appname = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_APPNAME));

                sTimeWeekday = _weekday;
                sTimeHour = _hour;
                sTimeMinute = _minute;

                if (!rs.isClosed()) {
                    rs.close();
                }

                et_sub.setText(_subject);
                et_des.setText(_description);
                et_time.setText(getWeekday((int)sTimeWeekday)+"요일 "+sTimeHour+"시 "+sTimeMinute+"분");
                if(extras.getString("appname") == "") {
                    tv_appname.setText(_appname);
                } else if (extras.getString("appname") != "") {
                    tv_appname.setText(extras.getString("appname"));
                }

                //  }
            }
        } catch (Exception e) {
        }
    }
    // 백그라운드에서 작동 하는일 없게..?
    @Override
    public void onPause() {
        super.onPause();
        finish();
    }

    /**
     * 액션바 뒤로가기버튼입니다.
     * getSupportActionBar().setDisplayHomeAsUpEnabled(true); 에서 만들어진 것.
     * 여기서 뒤로가기하면 initDB로 생성된 DB를 삭제할것입니다.
     * 즉 마지막에 생성된 db를 지우면되는거죠.
     */
    @Override
    public boolean onSupportNavigateUp()  {
        finish();
        Toast.makeText(this, "뒤로가기", Toast.LENGTH_SHORT).show();
        // bundle != null  &&  Deleteable(lastrecord, "boolean deleteable") == true 이면 삭제.
        //if(extras != null)
        //    dbManager.deleteSchedule(dbManager.lastRecord());

        return super.onSupportNavigateUp();
    }

    private TimePickerDialog.OnTimeSetListener tdpListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int _hourOfDay, int _minute) {
            sTimeHour = _hourOfDay;
            sTimeMinute = _minute;

            //Toast.makeText(getApplicationContext(), hourOfDay + "시 " + minute + "분", Toast.LENGTH_SHORT).show();
            et_time.setText(getWeekday((int)cTimeWeekday)+"요일 "+sTimeHour+"시 "+sTimeMinute+"분");   //요일도 해야하는데.
            timeSelect = true;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(extras != null)
            getMenuInflater().inflate(R.menu.menu_modifyschedule, menu);
        else
            getMenuInflater().inflate(R.menu.menu_addschedule, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_accept) {
            // 제목입력창에 아무것도 안적으면 빠꾸멕이기.
            if (et_sub.getText().toString().getBytes().length <= 0) {
                Toast.makeText(this, "다시 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();
                subject = et_sub.getText().toString();
                description = et_des.getText().toString();

                // _id, subject(제목), description(내용), weekday(요일), hour(시), minute(분), deleteable(임시저장 여부) 0(false)or 1(true), packagename(com.example.~~) 랑 appname(어플밑에 이름)
                if(timeSelect == false) {
                    sTimeWeekday = cTimeWeekday;
                    sTimeHour = cTimeHour;
                    sTimeMinute = cTimeMinute;
                }
                dbManager.insertSchedule(subject, description, cTimeWeekday, sTimeHour, sTimeMinute, null, null); // cTimeWeekday를 sTimeWeekday 로 바꿀예정. 어플선택하면 여기 올리가 없으니까 null값 넣음.

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                return true;
            }
        }
        /**
         * 수정버튼. Bundle이 존재하면 액션바가 바뀌면서 등장.
         *
         */
        if(id == R.id.action_modify) {
            Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
            subject = et_sub.getText().toString();
            description = et_des.getText().toString();

            if(extras.getString("packagename") == null) {
                dbManager.updateSchedule(extras.getInt("_id"), subject, description, cTimeWeekday, sTimeHour, sTimeMinute, null, null);
            } else if (extras.getString("packagename") != null) {
                dbManager.updateSchedule(extras.getInt("_id"), subject, description, cTimeWeekday, sTimeHour, sTimeMinute, extras.getString("packagename"), extras.getString("appname"));
            }

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }
        if (id == R.id.action_delete_mod) {
            Toast.makeText(this, "삭제되었습니다.", Toast.LENGTH_SHORT).show();
            dbManager.deleteSchedule(extras.getInt("_id"));
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * long cTimeWeekday를 한글로 바꿔줌
     */
    public String getWeekday(int i) {
        String weekday = "";
        switch (i) {
            case 1:
                weekday = "일";
                break;
            case 2:
                weekday = "월";
                break;
            case 3:
                weekday = "화";
                break;
            case 4:
                weekday = "수";
                break;
            case 5:
                weekday = "목";
                break;
            case 6:
                weekday = "금";
                break;
            case 7:
                weekday = "토";
                break;
        }

        return weekday;
    }
}
