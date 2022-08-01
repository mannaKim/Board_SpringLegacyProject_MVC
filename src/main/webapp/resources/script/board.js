function loginCheck(){
	if( document.frm.id.value==''){
		alert("아이디를 입력하세요");
		document.frm.id.focus();
		return false;
	}else if( document.frm.pw.value==''){
		alert("패스워드를 입력하세요");
		document.frm.pw.focus();
		return false;
	}else{
		return true;
	}
}


function idCheck(){
	if( document.frm.id.value==""){
		alert('아이디를 입력하여 주십시오.');
		document.frm.id.focus();
		return;
	}
	var id = document.frm.id.value;
	var opt = "toolbar=no, menubar=no, resizable=no, width=450, height=200";
	window.open("idcheck?id=" + id, "중복체크", opt);
	// 리퀘스트에도 ?와 함께 기존처럼 파라미터를 붙여서 링크를 만들수 있습니다.
}


function idok(userid){
	opener.frm.id.value = userid;
	opener.frm.re_id.value = userid;
	self.close();
}


function joinCheck(){
	if (document.frm.id.value==""){
		alert("아이디를 써주세요");		document.frm.id.focus();
		return false;
	}else if( document.frm.name.value.length==0){  
		alert("이름을 써주세요");		document.frm.name.focus();
		return false;
	}else if( document.frm.pwd.value==""){  
		alert("암호는 반드시 입력하여야 합니다");		document.frm.pw.focus();
		return false;
	}else if( document.frm.pwd.value != document.frm.pwd_check.value){
		alert("암호가 일치하지 않습니다");		document.frm.pw_check.focus();
		return false;
	}else if (document.frm.re_id.value != document.frm.id.value) { 
		alert("중복 체크를 하지 않았습니다.");		document.frm.id.focus();
		return false;
	} else if (document.frm.phone.value == "") { 
		alert("전화번호를 입력하세요.");		document.frm.phone.focus();
		return false;
	}else {
		return true;
	}
}


function reply_check(){
	if(document.frm2.reply.value='') {
		alert("내용을 입력하세요.");
		document.frm2.reply.focus();
		return false;
	}else {
		return true;
	}
}


function open_win(url, name){
	window.open(url,name,"toolbar=no,menubar=no,scrollbars=no,resizable=no,width=500,height=230");
}


function boardCheck(){
	if(document.frm.pass.value==""){
		alert('비밀번호는 수정/삭제시 필요합니다.');
		document.frm.pass.focus();
		return false;
	} else if(document.frm.title.value==""){
		alert('제목은 필수사항입니다.');
		document.frm.title.focus();
		return false;
	} else if(document.frm.content.value==""){
		alert('내용을 입력해주세요.');
		document.frm.content.focus();
		return false;
	} else {
		return true;
	}
}