package com.hikingbear.didyoudothat;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.ActionMode;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.hikingbear.database.DBManager;
import com.hikingbear.swipe.SwipeDismissListViewTouchListener;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ListView mListView;
    public DBManager dbManager;
    public Bundle dataBundle;
    int id_To_Search;
    Cursor cs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // choice모드 때 status bar color 없어지는거 보정
        Window window = getWindow();
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbManager = new DBManager(getApplicationContext());
        cs = dbManager.fetchAllNames();

        // 일정추가버튼 (+) fab
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddScheduleActivity.class);
                startActivity(intent);
            }
        });

        // navigation drawer - actionbar left button
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // ListView, swipe
        final ArrayList array_list = dbManager.getAllSchedule();
        final ArrayAdapter arrayAdapter=new ArrayAdapter(this,android.R.layout.simple_list_item_activated_1, array_list);

        mListView = (ListView)findViewById(R.id.lv_schedule);
        mListView.setAdapter(arrayAdapter);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        /**
         * longclick을 하면 choice모드가 된다.
         * onItemCheckedStateChanged: check상태가 바뀔때마다 실행
         * onCreateActionMode: 처음 choice모드가 되면 실행
         * onPrepareActionMode: onCreateActionMode 이후에 실행
         * onDestroyActionMode: choice모드에서 나갈때 실행행
         */
        final List<Integer> selectedList = new ArrayList<>();

        mListView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                System.out.println("====onItemCheckedStateChanged====");
                int checkedCount = mListView.getCheckedItemCount();

                if(checkedCount != 0) {
                    mode.setTitle(checkedCount + " Selected");
                    if (checked)
                        selectedList.add(position);
                    else
                        selectedList.remove(position);

                    System.out.println("checkedCount: " + checkedCount);
                    System.out.println("getCheckedItemIds: " + mListView.getCheckedItemPosition());

                    // 삭제시 오류를 막기위한 정렬
                    Collections.sort(selectedList);
                    Collections.reverse(selectedList);
                }
                else if(checkedCount == 0) {
                    System.out.println("0개니까 꺼져야함.");
                    Toast.makeText(MainActivity.this, "0개니까 꺼져야함.", Toast.LENGTH_SHORT).show();
                    mode.finish();
                }

            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                // status bar color change
                System.out.println("====onCreateActionMode====");
                if (getSupportActionBar() != null)
                    getSupportActionBar().hide();
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                System.out.println("====onPrepareActionMode====");
                getMenuInflater().inflate(R.menu.menu_selectschedule, menu);

                return false;
            }

            /**
             * 1. listview 아이템 check
             * 2. 뒤로가기버튼 or delete버튼
             * 3. delete버튼 - 몇개 체크됐는지, 체크된 아이템이 뭔지,
             * 4. 정말 삭제하시겠습니까? 띄우기.
             *
             * 완성.
             */
            //Context context = getApplicationContext();
            @Override
            public boolean onActionItemClicked(final ActionMode mode, MenuItem item) {
                System.out.println("====onActionItemClicked====");
                int id = item.getItemId();

                if (id == R.id.action_delete) {
                    System.out.println("delete gogo");
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(MainActivity.this);

                    // 제목셋팅
                    alertDialogBuilder.setTitle("일정 삭제");
                    alertDialogBuilder
                            .setMessage("정말로 삭제하시겠습니까?")
                            .setCancelable(false)
                            .setPositiveButton("삭제",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            // 삭제한다.
                                            for(int position: selectedList) {
                                                System.out.println(position+"번 아이템");
                                                if(cs != null) {
                                                    if(cs.moveToFirst()) {
                                                        cs.moveToPosition(position);
                                                        dbManager.deleteSchedule(cs.getInt(0));
                                                        arrayAdapter.remove(arrayAdapter.getItem(position));    // 여기서 역순으로 지워줘야 똑바로 잘 지우ㅝ진다.

                                                    }
                                                }
                                                System.out.println("삭제");
                                            }
                                            cs = dbManager.fetchAllNames();
                                            mode.finish();
                                        }

                                    })
                            .setNegativeButton("취소",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog, int id) {
                                            // 다이얼로그를 취소한다
                                            dialog.cancel();

                                        }
                                    });
                    // 다이얼로그 생성
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // 다이얼로그 보여주기
                    alertDialog.show();

                    return true;
                }
                return false;
            }


            @Override
            public void onDestroyActionMode(ActionMode mode) {
                System.out.println("====onDestroyActionMode====");
                if (getSupportActionBar() != null)
                    getSupportActionBar().show();
                selectedList.clear();
            }

        });
        /**
         * 일정선택시 databundle값을 설정해 다른 activity에 넘긴다.
         * 이것도 일단은 해결.
         */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(cs != null) {
                    if(cs.moveToFirst()) {
                        cs.moveToPosition(position);
                        id_To_Search = cs.getInt(cs.getColumnIndex("_id"));
                        dataBundle = new Bundle();
                        dataBundle.putInt("_id", id_To_Search);

                        Intent intent = new Intent(getApplicationContext(), AddScheduleActivity.class);
                        intent.putExtras(dataBundle);
                        startActivity(intent);

                    }
                }
            }
        });

        /**
         * swipe 기능 이건 삭제만 하는 기능이라 여기서 변경을 해야함.
         * 지우고나서 바로 refresh돼야 클릭해서 일정확인이 가능한데 그게 안되네.
         * cs = dbManager.fetchAllNames(); 을 사용한다--> 왜냐? customAdapter를 사용중이니까 내가 직접해야한다.
         * arrayAdapter.notifyDataSetChanged(); 는 써도 당연히 안된다. custom일때는..
         * ------
         * 어쨌든 완성
         */
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
                            if(cs != null) {
                                if(cs.moveToFirst()) {
                                    cs.moveToPosition(position);
                                    dbManager.deleteSchedule(cs.getInt(0));
                                    arrayAdapter.remove(arrayAdapter.getItem(position));
                                    cs = dbManager.fetchAllNames();
                                }
                            }
                        }
                    }
                });

        mListView.setOnTouchListener(touchListener);
        mListView.setOnScrollListener(touchListener.makeScrollListener());

    }

    // 자연스럽게 navi가 들어가게. 스르륵 스르륵
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
            Intent intent = new Intent (MainActivity.this, ViewApplicationList.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_delete) {
            System.out.println("delete gogo");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */


}


// 이건 없어도 되지않을까 생각. 내가 우상단 버튼을 안쓰기 때문.




