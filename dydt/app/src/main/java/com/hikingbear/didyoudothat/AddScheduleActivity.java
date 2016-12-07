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
    private long hour, minute, weekday;   // 시, 분, 요일
    private ImageButton ib_applist;      // 앱선택 버튼
    private EditText et_time, et_sub, et_des; // edittext 시간, 제목, 내용
    private TextView tv_appname;         // 선택한 앱 이름
    private ActionBar actionBar;
    DBManager dbManager;    //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
    int id_To_Update = 0;
    static Bundle extras;
    boolean timecheck = false;

    Calendar c = Calendar.getInstance();
    long cTimeY = c.get(Calendar.YEAR);
    long cTimeMO = c.get(Calendar.MONTH) + 1;   // 1을 더해줘야 제대로된 달이 나오더라.
    long cTimeD = c.get(Calendar.DATE);
    long cTimeWD = c.get(Calendar.DAY_OF_WEEK);
    long cTimeH = c.get(Calendar.HOUR_OF_DAY);
    long cTimeM = c.get(Calendar.MINUTE);

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
         */
        ib_applist = (ImageButton) findViewById(R.id.ib_applist);
        ib_applist.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Toast.makeText(AddScheduleActivity.this, "앱선택을 클릭하셨네욧", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent (getApplicationContext(), ViewApplicationList.class);
                startActivity(intent);

            }
        });

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
            int Value = extras.getInt("_id");
            if (Value > 0) {
            /*
            int Value = extras.getInt("_id");
            String appName = extras.getString("app");

            System.out.println("=-=========================================================");
            System.out.println("Value: " + Value);
            System.out.println("appName: " + appName);
            System.out.println("=-=========================================================");
*/
                //          if(Value>0) {
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
                String _appname = rs.getString(rs.getColumnIndex(DBManager.SCHEDULE_COLUMN_APP));

                System.out.println("=-=========================================================");
                System.out.println("try  _appname: " + _appname);
                System.out.println("=-=========================================================");

                if (!rs.isClosed()) {
                    rs.close();
                }

                et_sub.setText(_subject);
                et_time.setText(_year + "년 " + _month + "월 " + _day + "일 " + _hour + "시 " + _minute + "분");
                et_des.setText(_description);
                tv_appname.setText(_appname);
                //  }
            }
        } catch (Exception e) {
            System.out.println("별일아님.");
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
            timecheck = true;
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

                //subject TEXT, year INT, month INT, day INT, hour INT, minute INT, description TEXT
                // 날짜는 필요없고 요일이 필요할것 같다..? Today앱인데 필요할까? -> 안필요하다. 제목, 시, 분, 내용 4가지면 될듯. 나중에 수정바람
                // 기본 시간입력이 필요함. 시분 요일

                System.out.println("지금시간: "+cTimeY+ "년 "+cTimeMO+ "월 "+cTimeD+ "일 "+cTimeH+ "시 "+cTimeM+ "분 "+cTimeWD+"요일");

                /**
                 * if 시간선택 하면
                 *      dbManager.insertSchedule(subject, Calendar.YEAR, Calendar.MONTH, Calendar.DATE, Calendar.HOUR_OF_DAY, Calendar.MINUTE, description);
                 * else if 시간선택 안하면
                 *      dbManager.insertSchedule(subject, Calendar.YEAR, Calendar.MONTH, Calendar.DATE, hour, minute, description);
                 *
                 * 여기서 시간선택한다는 걸 플래그를 줄지, 있던걸 체크하는방식으로할지 고민이 된다.
                 */

                if(timecheck == true) {
                    dbManager.insertSchedule(subject, cTimeY, cTimeMO, cTimeD, hour, minute, description);
                } else if (timecheck == false) {
                    dbManager.insertSchedule(subject, cTimeY, cTimeMO, cTimeD, cTimeH, cTimeM, description);
                }

                //dbManager.insertSchedule(subject, Calendar.YEAR, 11, 10, hour, minute, description);

                System.out.println("================================================");

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                return true;
            }
        }
        // 수정
        if(id == R.id.action_modify) {
            Toast.makeText(this, "수정되었습니다.", Toast.LENGTH_SHORT).show();
            subject = et_sub.getText().toString();
            description = et_des.getText().toString();

            dbManager.updateSchedule(extras.getInt("_id"), subject, cTimeY, cTimeMO, cTimeD, hour, minute, description);

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
}
