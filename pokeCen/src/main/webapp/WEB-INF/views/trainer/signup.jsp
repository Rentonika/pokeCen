<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Sign up</title>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript">

function idCheck() {

	var id = $('#id').val();
	var pw = $('#pw').val();
	var name = $('#name').val();

	// id 체크
	if(id.trim().length <3 || id.trim().length > 10) {
		alert('아이디를 3~10글자 내외로 입력해주세요.');
		return;
	}

	// pw 체크
	if(pw.trim().length < 3 || pw.trim().length > 7) {
		alert('비밀번호를 3~7글자 내외로 입력해주세요.');
		return;		
	}

	// 이름 체크
	if(name.trim().length < 1) {
		alert('이름을 입력해주세요.');
		return;
	}

	// 아이디 중복확인
	$.ajax({
		method : 'POST'
		, data : {"id" : id}
		, url : 'idCheck'
		, success : function(resp) {
			if(resp == 'fail') {
				alert('이미 가입된 아이디입니다.');
				$('#id').val('');
				return;
			} else if(resp == 'success') {
				// 정보 전달
				$('#signupForm').submit();
			}
		}
		, error : function(err) {
			alert('에러가 발생했습니다. 다시 시도해주세요.');
			return;
		}
	});

}

</script>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
</head>
<body>
<h3>회원가입</h3>
<form action="signup" id="signupForm" method="post">
<table border="1">
<tr>
<th>* 아이디 : </th>
<td><input type="text" id="id" name="id"></td>
</tr>
<tr>
<th>* 비밀번호 : </th>
<td><input type="password" id="pw" name="pw"></td>
</tr>
<tr>
<th>* 이름 : </th>
<td><input type="text" id="name" name="name"></td>
</tr>
<tr>
<th>지역 : </th>
<td><input type="text" id="country" name="country"></td>
</tr>
<tr>
<th>휴대폰 : </th>
<td><input type="text" id="phone" name="phone"></td>
</tr>
<tr>
<th>이메일 : </th>
<td><input type="text" id="email" name="email"></td>
</tr>
</table>
<input type="button" value="회원가입" onclick="idCheck()">
<input type="reset" value="리셋">
</form>
</body>
</html>