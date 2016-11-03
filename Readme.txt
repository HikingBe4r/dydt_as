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