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
		<h1>게시글 수정</h1>
		<form name="frm" method="post" action="boardUpdate" enctype="multipart/form-data">
			<input type="hidden" name="num" value="${board.num}">
			<table>
				<tr>
					<th>작성자</th>
					<td>
						${loginUser.userid}
						<input type="hidden" name="userid" value="${loginUser.userid}" size="12">
					</td>
				</tr>
				<tr>
					<th>비밀번호</th>
					<td><input type="password" name="pass" size="12">*</td>
				</tr>
				<tr>
					<th>이메일</th>
					<td><input type="text" name="email" size="12" value="${board.email}"></td>
				</tr>
				<tr>
					<th>제목</th>
					<td><input type="text" name="title" size="12" value="${board.title}">*</td>
				</tr>
				<tr>
					<th>본문</th>
					<td><textarea cols="70" rows="15" name="content">${board.content}</textarea></td>
				</tr>
				<tr>
					<th>이미지</th>
					<td>
						<c:choose>
							<c:when test="${empty board.imgfilename}">
								<img src="resources/upload/noname.jpg" height="80">
								<br>
							</c:when>
							<c:otherwise>
								<img src="resources/upload/${board.imgfilename}" height="80">
								<br>
							</c:otherwise>
						</c:choose>
						<input type="file" name="imgfilename"><br>파일을 수정하고자 할 때만 선택하세요 
						<input type="hidden" name="oldfilename" value="${board.imgfilename}">
					</td>
				</tr>
			</table>
			<br> 
			<input type="submit" value="수정" onClick="return boardCheck()">
			<input type="button" value="목록"
				onClick="location.href='boardList'">
		</form>
	</div>
</body>
</html>