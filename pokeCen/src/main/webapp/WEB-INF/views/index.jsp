<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Index</title>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
<link rel="stylesheet" href="resources/css/index.css">
<!-- 미디어 쿼리 시험 적용 -->
<link rel="stylesheet" media="(max-width: 480px)" href="resources/css/index_mobile.css">
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script src="resources/js/sockjs.min.js"></script>
<script src="resources/js/sockjs.min.js.map" type="application/json"></script> <!-- map 404 제거 -->
<script src="resources/js/stomp.min.js"></script>
<script src="resources/js_script/index_network.js"></script>
<script src="resources/js_script/index_friend.js"></script>
<script type="text/javascript">

const user = "${sessionScope.loginId}";

$(function(){

	// 소켓 통신 시작
	connect();

	// 소유 엔트리 리스트 가져오기 
	ownEntryList();

	// 친구 신청 리스트 가져오기
	followerList();

	// 친구 리스트 
	friendList();

	// 모달 테스트
	$(document).ready(function() {
      $('#entryModal').hide();
      $('#profileModal').hide();
      $('#pokemonModal').hide();
      // $('#entryModal').draggable();
    });

	// 친구 찾기
	$('#friendBtn').on('click', function(){
		
		var searchFriend = $('#searchFriend').val();
		if(searchFriend.trim().length < 1) {
			alert('검색어를 입력해주세요.');
			return;
		}

		$.ajax({
			method : 'POST'
			, url : 'searchFriend'
			, data : {"searchId" :searchFriend}
			, success : function(searchList) {
				console.log(searchList);
				$('.searchList').empty();
				$.each(searchList, function(index, item){
					var online = '오프라인';
					if(item.online == true) {
						online = '온라인';
					}
					// menu += '<div class="roomMember" data-num="'+item.memberId+'" data-auth="'+item.authority+'" data-userName="'+item.userName+'">' + '[공유 완료] ' + item.userName+ '</div>';
					
					$('.searchList')
					.append('<div class="searchedFriend" data-id="' + item.id + '">' + "아이디 : " + item.id + " " + online + " 친구추가" + '</div>');
					
				});
			
			}
		});
	});

	// 클릭 시, 친구추가 
	$(document).on('click', '.searchedFriend', function(){
		var friendId = $(this).attr('data-id');
		var YesOrNo = confirm(friendId + '님에게 친구 신청하시겠습니까?');

		if(YesOrNo) followFriend(friendId);
		else return;
	});		

	// 클릭 시, 해당 포켓몬 데이터 정보  / 모달
	$(document).on('click', '.myPokemon', function(){
		var pokeNo = $(this).attr('data-num');
		var pokeName = $(this).attr('data-name');
		var nickname = $(this).attr('data-nickname');

		alert('정보 로딩 중');
		$('#pokemonModal').fadeIn(); // 모달창 띄우기

		// 이미지 붙이기
		var pokemon = '';
		pokemon += '<img src=resources/img/pokemon/' + pokeNo + '.gif/>';

		$('.pokemonImage').empty().append(pokemon);

		// 프로필 정보 붙이기
		var profile = '';
		profile += '<p>' + '도감넘버 : ' + pokeNo + '</p>';
		profile += '<p>' + '학명 : ' + pokeName + '</p>';
		profile += '<p>' + '닉네임 : ' + nickname + '</p>';
		
		$('.pokemonProfile').empty().append(profile);
	});

	// 클릭 시, 팔로워 정보 / 모달 / 아직은 정보 모달은 X / accept 까지만
	$(document).on('click', '.follower', function(){
		var id = $(this).attr('data-followerId');
		var name = $(this).attr('data-name');
		var country = $(this).attr('data-country');
		var phone = $(this).attr('data-phone');
		var email = $(this).attr('data-email');

		// 원래는 모달 창 표시
		var YesOrNo = confirm('친구 신청을 수락하겠습니까?');
		if(YesOrNo) acceptFollower(id);
		else denyFollower(id);
		return;
	});	

	// 친구 클릭 시, 교환 가능하게 - entry script로 들어가게
	$(document).on('click', '.myFriend', function(){
		var id = $(this).attr('data-id');
		var online = $(this).attr('data-status');

		var YesOrNo = confirm(id + '님과 교환을 하시겠습니까?');
		if(YesOrNo == false) return;
		if(online == '오프라인') {
			alert(id + '님이 오프라인 상태이므로 교환을 할 수 없습니다.');
			return;
		} else {
			alert(id + '에게 교환 신청을 보냅니다.');
			stompClient.send('/suggestExchange', {}, JSON.stringify({"target" : id}));
		}
	});
		

	// 테스트 - 버튼 클릭시, 메세지 보냄
 	$('#sendNoteBtn').on('click', function(){
		test();
	}); 

	// 버튼 클릭시, 통신 종료
 	$('#endBtn').on('click', function(){
		disconnect();
	});
	
});

function friendList() {

	// 친구 리스트 가져오기
	$.ajax({
		method : 'POST'
		, url : 'myFriendList'
		, success : function(myFriendList) {
			console.log(myFriendList);

			$('#friendList').empty();
			$.each(myFriendList, function(index, item){

				var online = '오프라인';
				var friend = "";
				if(item.online == true) {
					online = '온라인';
	
					friend += '<div class="onlineFriend">';
					friend += '<div class="onlineIcon"></div>';
					friend += '<div class="myFriend" data-id="' + item.id;
					friend += '" data-status="' + online + '">';
					friend +=  item.name + ' / ' + online + '</div></div>';
					
					$('#friendList').append(friend);
				} else {

					friend += '<div class="offlineFriend">';
					friend += '<div class="offlineIcon"></div>';
					friend += '<div class="myFriend" data-id="' + item.id; 
					friend += '" data-status="' + online + '">';
					friend +=  item.name + ' / ' + online + '</div></div>';
					
					$('#friendList').append(friend);
				}
			});
		}
	});	
}

// 친구신청 리스트 보여주기
function followerList() {

	$.ajax({
		method : 'POST'
		, url : 'myFollowerList'
		, success : function(followerList) {
			console.log(followerList);	

			$('.followerList').empty();
			
			$.each(followerList, function(index, item){
				var follower = '';		
				follower += '<div class="follower"';
				follower += 'data-followerId="' + item.id;
				follower += '" data-name="' + item.name;
				follower += '" data-country="' + item.country;
				follower += '" data-email="' + item.email;
				follower += '" data-phone="' + item.phone;
				follower += '">'
				follower += '아이디 : ' + item.id;
				follower += ' 이름 : ' + item.name;
				follower += '</div>';

				$('.followerList').append(follower);
			});
		}
	});
}


//내 소유 엔트리 가져오기
function ownEntryList() {
	// 내 엔트리 리스트 가져오기
	$.ajax({
		method : 'POST'
		, url : 'myEntryList'
		, success : function(list) {
			myEntryList = list;
			console.log(myEntryList);
			
			$('.myEntryList').empty();
			$.each(list, function(index, item){
				var entry = "";
				entry += '<div class="myPokemon"';
				entry += 'data-num="'+ item.pokeNo;
				entry += '" data-name="' + item.poke_name
				entry += '" data-nickname="' + item.nickname
				entry += '">';
				entry += '<img src=resources/img/pokemon/' + item.pokeNo + '.gif/>'
				entry += '</div>';
				
				$('.myEntryList').append(entry);
			});	
		}
	});
}

// 친구 추가
function followFriend(friendId) {
	
	$.ajax({
		method : 'POST'
		, url : 'followFriend'
		, data : {"friend_id" : friendId}
		, success : function(result) {
			if(result == 'success') {
				alert(friendId + '님에게 친구 신청을 보냈습니다.');	
				$('.searchList').empty(); // 검색창 초기화
				friendList(); // 친구 리스트 초기화
			} else {
				alert('오류가 발생했습니다. 한 번 더 시도해주세요.');
			}
		} 
	})
}

function acceptFollower(id) {

	alert(id + '의 친구신청을 수락합니다.');
	$.ajax({
		method : 'POST'
		, url : 'acceptFollower'
		, data : {"friend_id" : id}
		, success : function(result) {
			if(result == 'success') {
				// 다시 친구 리스트 업데이트
				friendList();
				// 팔로워 리스트 업데이트
				followerList(); 
			}
		}
	})
	
}

function denyFollower(id) {

	alert(id + '의 친구신청을 거절합니다.');
	$.ajax({
		method : 'POST'
		, url : 'denyFollower'
		, data : {"friend_id" : id}
		, success : function(result) {
			if(result == 'success') {
				// 팔로워 리스트 업데이트
				followerList(); 
			}
		}
	});
}

// 엔트리 모달창 닫기
function close_entryModal() {
    $('#entryModal').fadeOut();
};

// 프로필 모달창 닫기
function close_profileModal() {
	$('#profileModal').fadeOut();
}

function close_pokemonModal() {
	$('#pokemonModal').fadeOut();
}

</script>
</head>
<body>
<div id="indexWrapper">
<!-- header 영역 -->
<div class="header">
	<div class="pokeball"><img alt="pokeball" src="resources/img/pokeball.png"></div>
	<h1>GTS Shop</h1>
	<p>즐거운 교환소</p>
</div>

<!-- nav 영역 -->
<div class="nav">
	<a href="myEntry">엔트리</a>
	<a href="myPage">마이페이지</a>
	<a href="exchangeBoard">교환게시판</a>
	<a href="logout">로그아웃</a>
	<c:if test="${sessionScope.authority == '[ROLE_ADMIN]'}">
	<a href="trainerManagement">유저 관리</a>
	<a href="settings">환경설정</a>
	</c:if>
</div>

<!-- main 영역  -->
<div class="row">

<div class="left">
	<div class="card">
		<h2>[GTS Info] My Entry List</h2>
		<h5>내 멤버들</h5>
		<div class="fakeimg" style="height:200px;">
			<div class="myEntryList"></div>
		</div>

	</div>
	<div class="card">
		<h2>[GTS 트랜드] 2021 트렌드</h2>
		<h5>GTS 트랜드</h5>
		<div class="fakeimg" style="height:200px;">Image</div>
		<h5>1. admin / admin</h5>
		<p>타이틀 - 이번주의 로그인 1위</p>
		<p>내용</p>
	</div>
</div>

<!-- sidebar content -->
<div class="right">
	<div class="card">
	<h3>접속 중인 친구</h3>
	<div class="fakeimg" style="height:100px">
		<div id="friendList"></div>
	</div>
	<h3>친구 신청</h3>
	<div class="fakeimg" style="height:100px">
		<div class="followerList"></div>
	</div>
	<h3>친구 추가</h3>
	<div class="fakeimg" style="height:100px;">
		<input type="text" id="searchFriend"> <input type="button" id="friendBtn" value="Search">
		<div class="searchList"></div>
	</div>
	
	</div>
	<div class="card">
		<h2>아아</h2>
		<div class="fakeimg" style="height:100px">Image</div>
		<h2>아아아</h2>
		<div class="fakeimg" style="height:100px;">
		
		
		</div>
	</div>
</div>

</div>

<!-- footer 영역 -->
<div class="footer" role="containerinfo">
	<h3>link: http://www.naver.com</h3>
</div>

<!-- 엔트리 모달 -->
<div id="entryModal" class="modalWrapper">
<!-- Modal-->
<div class="modal">    
      <!-- Modal header -->
 	<div class="modal-header">
 		<div class="modal-Xbtn" onClick="close_entryModal();">
            <span class="pop_bt" style="font-size: 13pt;">X</span>
        </div>
 	</div>
	<!-- Modal content -->
 	<div class="modal-content">
                <p style="text-align: center;"><span style="font-size: 14pt;"><b><span style="font-size: 24pt;">공지</span></b></span></p>
                <p style="text-align: center; line-height: 1.5;"><br />여기에 내용</p>
                <p><br /></p>
    </div>                     
    <!-- Modal footer -->
    <div class="modal-footer"></div>
            
</div>  
</div>

<!-- 친구 프로필 모달 -->
<div id="profileModal" class="modalWrapper">
<!-- Modal-->
<div class="modal">    
      <!-- Modal header -->
 	<div class="modal-header">
 		<div class="modal-Xbtn" onClick="close_profileModal();">
            <span class="pop_bt" style="font-size: 13pt;">X</span>
        </div>
 	</div>
	<!-- Modal content -->
 	<div class="modal-content">
                <p style="text-align: center;"><span style="font-size: 14pt;"><b><span style="font-size: 24pt;">공지</span></b></span></p>
                <p style="text-align: center; line-height: 1.5;"><br />여기에 내용</p>
                <p><br /></p>
    </div>                     
    <!-- Modal footer -->
    <div class="modal-footer"></div>
            
</div>  
</div>

<!-- 내 엔트리 포켓몬 모달 -->
<div id="pokemonModal" class="modalWrapper">
<!-- Modal-->
<div class="modal">    
      <!-- Modal header -->
 	<div class="modal-header">
 		<div class="modal-Xbtn" onClick="close_pokemonModal();">
            <span class="pop_bt" style="font-size: 13pt;">X</span>
        </div>
 	</div>
	<!-- Modal content -->
 	<div class="modal-content">
 		<div class="pokemonImage"></div>
 		<div class="pokemonProfile"></div>
 		<div class="pokemonDescription"></div>
 		<!-- 선택 창 (무작위 교환, 박스에 보내기, 사요나라 하기)-->
    </div>                     
    <!-- Modal footer -->
    <div class="modal-footer"></div>
            
</div>  

</div>

</div>
</body>
</html>