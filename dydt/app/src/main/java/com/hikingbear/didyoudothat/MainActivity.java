package com.hikingbear.didyoudothat;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hikingbear.database.DBManager;
import com.hikingbear.swipe.SwipeDismissListViewTouchListener;


import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    DBManager dbManager;
    public Bundle dataBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(getApplicationContext());

        // 일정추가버튼 (+)
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        // navigation drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ListView, swipe

        ArrayList array_list = dbManager.getAllContacts();
        final ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_1, array_list);

        mListView = (ListView)findViewById(R.id.lv_schedule);
        mListView.setAdapter(arrayAdapter);

        // 일정선택시 databundle값 설정
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // TODO Auto-generated method stub
                int id_To_Search = arg2 + 1;

                dataBundle = new Bundle();
                dataBundle.putInt("_id", id_To_Search);

                Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);

                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });
        // swipe 기능 이건 삭제만 하는 기능이라 여기서 변경을 해야함.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(mListView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for(int position: reverseSortedPositions) {
                                    //db에서 삭제도 해야함.

                                    arrayAdapter.remove(arrayAdapter.getItem(position));
                                    int id_To_Search = position + 1;
                                    dbManager.deleteTest(id_To_Search);
                                }
                                arrayAdapter.notifyDataSetChanged();
                            }
                        });
        mListView.setOnTouchListener(touchListener);
        mListView.setOnScrollListener(touchListener.makeScrollListener());

    }

    // 자연스럽게 navi가 들어가게.
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // navigation drawer 항목세팅
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_achievement) {
            Toast.makeText(this, "추가예정입니다.", Toast.LENGTH_SHORT).show();
            // Intent intent = new Intent (MainActivity.this, AchievementActivity.class);
            //startActivity(intent);
        } else if (id == R.id.nav_review) {
            Toast.makeText(this, "리뷰써주셔서 감사합니다.", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_send) {
            Toast.makeText(this, "추가예정입니다.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent (MainActivity.this, SendActivity.class);
            //startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "추가예정입니다.", Toast.LENGTH_SHORT).show();
            //Intent intent = new Intent (MainActivity.this, SettingsActivity.class);
            //startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


// 이건 없어도 되지않을까 생각. 내가 우상단 버튼을 안쓰기 때문.
    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */
