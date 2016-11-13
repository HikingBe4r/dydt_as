// 공지사항
-------------------------------------------------------------------------------
[오후 2:54 2016-11-01] 시작합니다.

<초기컨셉> 
기본적인 일정관리 어플을 토대로 하지만, Daily가 1순위, 전체 일정관리를 2순위 목표로 한다.
매일 할일을 등록해놓고 관리해주는 어플리케이션.
예를 들어 12시에는 점심먹기, 1시에 약먹기, 6시에 운동하기등 하루하루 할일을 등록한다.
기존어플과 차별점은 다른어플을 일정에 등록해 클릭시 해당 어플이 실행되어 게임 등의 출석체크를 놓치지 않게 하는것이 포인트.
기본적인 구성이 완성되면 각 주마다 달성률을 확인하는 기능을 추가하고자 한다.

<Activity 구성>
시작화면 -> MainActivity -> Navigation Drawer -> Settings Activity (세팅)
					      -> Achievement Activity (달성률)
					      -> 도움말, 리뷰쓰기, 의견보내기등
			 -> Add_Schedule Activity


구글스토어 등록까지가 목표이다. 2주내에 버그없이 완성하는것이 목표. 100다운로드하자
---------------------------------------------------------------------------------
[오후 6:22 2016-11-02]

Achievement, AddSchedule, Send, Settings Activity 추가. 기능추가 예정.
				각각 layout.xml도 추가. 기능추가 예정.
AndroidManifest.xml에서 menu actionbar에 있는 icon, title 제거. == claer[오전 2:41 2016-11-03]

market://details?id=패키지명 // 이를 사용하면 마켓으로 연결된다는데. 어떻게 하는걸까?

------------------------------------------------------------
[오전 2:21 2016-11-03] 

ic_drawer 터치가 너무 작음. 확인요망. -- 야매로 그림크기를 키워서 하긴했는데 너무 벽에 붙어있는게 문제인듯. [오전 2:46 2016-11-03]
intent오류. 각 activity 확인요망 == clear [오전 2:41 2016-11-03]

getActionBar().setTitle("");
getActionBar().setIcon(android.R.color.transparent);

이 두가지를

actionbar.setDisplayShowTitleEnabled(false);
actionbar.setDisplayShowHomeEnabled(false);
로 변경하게되면. 액션바에서 아이콘과 title이 없어짐.

MainActivity 구성요소 - DrawerActionBar, Floating Action Button, ListView

[오전 4:26 2016-11-03]  FAB 하려다 실패. library를 넣어야 하는것 같음.

----------------------------------------------------------
[오후 3:29 2016-11-03]
fab 넣으려다가그냥 android studio로 재시작.

addschedule layout부터 시작.

ActionBar가 왜 안보였느냐!!!!! == extends AppCompatActivity   [오후 7:26 2016-11-03]

시계 띄우기 성공 TimePickerDialog를 Activity에서 불러온다. layout이 아니다. -- [오후 9:13 2016-11-03] 

--------------------------------------------------------------
[오후 12:08 2016-11-04]

시계에서 time select하면 edittext에 추가. -- 완료
actionbar에 back버튼 추가 -- 완료 
	하는방법
actionBar = getSupportActionBar();          // 아따 support가 넘나 어려운것..
actionBar.setDisplayHomeAsUpEnabled(true);  // 뒤로가기 버튼생성


<예정>
요일 선택 및 반복버튼 추가
일정 db에 등록 및 mainactivity에 출력

settingsActivity 추가
sendActivity 추가
Main의 Navi 디테일 챙기기


// 아이콘 추가하는법 (안드로이드 제공아이콘) [오후 6:23 2016-11-04]
res - new - image asset - icon Type (ActionBar & Tab icon) 여기까지 보면 할수있을것.

---------------------------------------------------------------------
[오전 11:40 2016-11-08]
//PandingIntent 가 그 용도죠. alarmManager랑 아주 그냥 찰떡처럼 붙어있는 놈이죠

<addschedule Activity>
뒤로가기 툴바버튼 function		-- finish와 intent후 finishactivity중 고민. 일단 finish함.
Accept 메뉴버튼 function - 일정 db에 저장할것.
제목, 시간, 내용 저장 -> MainActivity 표시

<MainActivity>
AddSchedule에서 저장된 일정 표시하기.  -> content_main.xml
일정 선택후 삭제, 수정기능		-> google에 listview swipe 로 search
왼쪽에서 오른쪽 드래그하면 일정완료 체크. - function1
오른쪽에서 왼쪽 드래그하면 일정 삭제.	  - function2

일정 클릭하면 일정내용 다 볼수있음 -> addschedule.activity 로 intent

튜토리얼 화면 - ImageSwitcher, ViewFlipper 등을 참조하세요.
	        화면을 Fragment로 잡으시고, ViewPager로 구현하면 되요..
                최종결과는 화면 스와이프하면 다른 그림 나오고 그림나오고 하는 기능임.


달성률 nav 아이콘수정
리뷰쓰기 nav 아이콘수정

----------------------------------------------------------------------
[오후 3:55 2016-11-13]
있던 일정 클릭했을떄 메뉴바에 있는 accept 아이콘을 휴지통 아이콘으로 바꾸고, 삭제 기능을 하게함.

----------------------------------------------------------------------
https://hikingbearstudio.slack.com/messages/readme/

slack 으로 ReadMe 를 옮기기로 했습니다.