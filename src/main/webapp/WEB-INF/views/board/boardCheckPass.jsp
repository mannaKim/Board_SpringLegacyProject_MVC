<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<script type="text/javascript">
		if (window.name == 'update') {
			window.opener.location.href = "boardUpdateForm?num=${num}";
		} else if (window.name == 'delete') {
			var ans = confirm("삭제하시겠습니까?");
			if (ans)
				window.opener.location.href = "boardDelete?num=${num}";
		}
		self.close(); 
	</script>
</body>
</html>