--member--------------------------------------

select * from member;

--board--------------------------------------

drop sequence board_seq;
create sequence board_seq start with 1 increment by 1;
delete BOARD;

select * from board order by num desc;

insert into board(num,userid,email,pass,title,content)
values(board_seq.nextVal, 'one', 'abc@abc.com', '1234', 'test!test!1', 'test!!!!!!입니다.111111');
insert into board(num,userid,email,pass,title,content)
values(board_seq.nextVal, 'two', 'acc@abc.com', '1234', 'test!test!2', 'test!!!!!!입니다.22222');
insert into board(num,userid,email,pass,title,content)
values(board_seq.nextVal, 'scott', 'abc@abc.com', '1234', 'test!test!3', 'test!!!!!!입니다.33333');
insert into board(num,userid,email,pass,title,content)
values(board_seq.nextVal, 'user', 'abc@abc.com', '1234', 'test!test!4', 'test!!!!!!입니다.44444');
insert into board(num,userid,email,pass,title,content)
values(board_seq.nextVal, 'user', 'user@naver.com', '1234', 'test!test!5', 'test!!!!!!입니다.55555');


--reply--------------------------------------

drop table reply;
drop sequence reply_seq;

create table reply(
	num number(7) primary key,
	boardnum number(5),
	userid varchar2(20),
	writedate date default sysdate,
	content varchar2(1000)
);

create sequence reply_seq start with 1 increment by 1;

select * from REPLY;

insert into REPLY(num,boardnum,userid,content)
values(reply_seq.nextVal, 152, 'one', '테스ㅡ트트트ㅡ');
insert into REPLY(num,boardnum,userid,content)
values(reply_seq.nextVal, 152, 'scott', 'test!!');
insert into REPLY(num,boardnum,userid,content)
values(reply_seq.nextVal, 151, 'one', 'test!!');