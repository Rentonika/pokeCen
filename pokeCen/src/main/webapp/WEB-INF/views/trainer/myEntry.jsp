<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript">

var myEntryList; 
$(function(){

	// 처음에는 이상해씨 기본
	$('#selectedImage')
		.append("<img src=resources/img/pokemon/1.gif/>");

	// 선택할 때마다 이미지가 뜨기
	$('#inputEntry').on('change', function(){
		var pokeno = $('#inputEntry').val();
		$('#selectedImage')
		.empty()
		.append("<img src=resources/img/pokemon/" + pokeno + ".gif/>");
		
	});
	
	pokemonSelectList();
	ownEntryList();

	// 엔트리 추가
	$('#inputBtn').on('click', function(){
		
		var selectedPokemonNo = $('#inputEntry').val();	
		var selectedPokemonName = $('#inputEntry option:checked').text();
		var nicknameConfirm = confirm('닉네임을 지으시겠습니까?');
		var nickname;

		if(nicknameConfirm) {
			nickname = prompt('닉네임 입력');
		} else {
			nickname = null;
		}

		if(myEntryList.length >= 6) {
			var YesOrNo = confirm('현재 엔트리가 6마리입니다. 등록할 포켓몬을 박스에 보내겠습니까?');
			if(YesOrNo) inputEntryInBox(selectedPokemonNo, selectedPokemonName, nickname);
			return;
		}
		
		$.ajax({
			method : 'POST'
			, url : 'inputEntry'
			, data : {"pokeNo" : selectedPokemonNo, "poke_name" : selectedPokemonName, "nickname" : nickname}
			, success : function(result) {
				if(result == 'success') {
					alert('엔트리 등록 성공!');
					// 다시 내 엔트리를 가져오기
					ownEntryList();
					
				} else {
					alert('등록 실패하였습니다. 다시 등록해주세요.');
				}
			}
		});
			
	});
	
});

function pokemonSelectList () {

	// select 리스트 DB 리스트 가져오기
	$.ajax({
		method : 'POST'
		, url : 'pokemonList'
		, success : function(list) {
			// console.log(list);
			$.each(list, function(index, item){
				$('#inputEntry')
				.append("<option value="+ item.pokeno + ">" + item.name + "</option>");
			});
			
		}
	});
	
}




// 내 소유 엔트리 가져오기
function ownEntryList() {
	// 내 엔트리 리스트 가져오기
	$.ajax({
		method : 'POST'
		, url : 'myEntryList'
		, success : function(list) {
			myEntryList = list;
			console.log(myEntryList);
		}


	});
}

// 박스 등록
function inputEntryInBox(selectedPokemonNo, selectedPokemonName, nickname) {

	$.ajax({
		method : 'POST'
		, url : 'inputEntryInBox'
		, data : {"pokeNo" : selectedPokemonNo, "poke_name" : selectedPokemonName, "nickname" : nickname}
		, success : function(result) {
			if(result == 'success') {
				alert('박스에 등록했습니다.');
				// 포켓몬 리스트
				ownEntryList();
				// 박스 리스트 갱신
				
			}
		} 
	});
		
	
}

</script>
</head>
<body>
<div class="entryWrapper">

<!-- header -->
<div class="header">
</div>

<!-- content -->
<div class="content">

<select id="inputEntry"> </select>

<input type="button" id="inputBtn" value="등록">

<div id="selectedImage"></div>


<!-- 내 엔트리 리스트 -->
<div id="myEntryList"></div>

</div>


<!-- footer -->
<div class="footer"></div>

</div>
</body>
</html>