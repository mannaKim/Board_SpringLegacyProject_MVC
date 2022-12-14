<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="resources/css/board.css" />
<script src="resources/script/board.js"></script>
</head>
<body>
	<div id="wrap" align="center">
		<h1>게시글 등록</h1>
		<form name="frm" method="post" action="boardWrite" enctype="multipart/form-data">
			<table>
				<tr>
					<th>작성자</th>
					<td>
						${loginUser.userid}
						<input type="hidden" name="userid" value="${loginUser.userid}">
					</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="pass">*</td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input type="text" name="email" value="${loginUser.email}"></td>
				</tr>
				<tr>
					<th>제목</th>
					<td><input type="text" name="title" size="70">*</td>
				</tr>
				<tr>
					<th>내용</th>
					<td><textarea cols="70" rows="15" name="content"></textarea>*</td>
				</tr>
				<tr>
					<th>이미지</th>
					<td><input type="file" name="imgfilename"></td>
				</tr>
			</table>
			<br>
			<br> 
			<input type="submit" value="등록" onClick="return boardCheck()">
			<input type="reset" value="다시 작성">
			<input type="button" value="목록으로" onClick="location.href='boardList'">
		</form>
	</div>
</body>
</html>