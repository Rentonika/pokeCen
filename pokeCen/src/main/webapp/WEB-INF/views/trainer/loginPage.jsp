<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login Page</title>
<link href="resources/css/loginPage.css" rel="stylesheet">
<!-- 네이버 로그인 스크립트 -->
<script type="text/javascript" src="https://static.nid.naver.com/js/naverLogin_implicit-1.0.3.js" charset="utf-8"></script>
<script src="resources/js/jquery-3.4.1.min.js"></script>
<script src="resources/js/jquery-ui.min.js"></script>
<script type="text/javascript">

$(function(){
	


})

function loginCheck() {

	var id = $('#id').val();
	var pw = $('#pw').val();

	if(id.trim().length < 1) {
		alert('아이디를 입력해주세요.');
		return;
	}

	if(pw.trim().length < 1) {
		alert('비밀번호를 입력해주세요.');
		return;
	}	

	$('#loginForm').submit();
	
}

</script>
<c:if test="${not empty emailMessage}">
<script type="text/javascript">	
	$(function(){
		alert("${emailMessage}");
	});		
</script>
</c:if>
<link rel="icon" href="data:;base64,="> <!-- favicon io 제거 -->
</head>
<body>

<div class="loginWrapper">
<!-- header -->
<div class="header">
	<h1>Login Page</h1>
</div>

<!-- main -->
<div class="content">

	<div class="loginArea">
	<form action="login" id="loginForm" method="post">
		<table>
			<tr>
			<th>아이디</th>
			<td><input type="text" id="id" name="id"></td>
			</tr>
			<tr>
			<th>비밀번호</th>
			<td><input type="password" id="pw" name="pw"></td>
			</tr>
		</table>
			
		<a href="http://localhost:8888/oauth2/authorization/naver">		
		<img class="naver_icon" alt="naver_icon" src="resources/img/Naver_icon.png">
		</a>
		<a href="http://localhost:8888/oauth2/authorization/kakao">
		<img class="kakao_icon" src="resources/img/kakao_login.png">
		</a>
		<a href="http://localhost:8888/oauth2/authorization/google">
		<img class="google_icon" src="resources/img/google_icon.png">
		</a>
		
			<input type="submit" value="로그인" onclick="loginCheck()">
	</form>
	</div>
		
		<div class="content_msg">
		<h5>아이디가 없으신가요? <a href="signup">회원가입</a></h5>
		<c:if test="${not empty requestScope.failureMessage}">
		<h5>${requestScope.failureMessage}</h5>
		</c:if>
		</div>

</div>

<!-- footer -->
<div class="footer">
</div>

</div>
</body>
</html>