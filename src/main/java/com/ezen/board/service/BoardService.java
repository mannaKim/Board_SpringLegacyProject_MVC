package com.ezen.board.service;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezen.board.dao.BoardDao;
import com.ezen.board.dto.BoardDto;
import com.ezen.board.dto.ReplyDto;
import com.ezen.board.util.Paging;

@Service
public class BoardService {

	@Autowired
	BoardDao bdao;

//	public ArrayList<BoardDto> getBoardMain() {
//		return bdao.getBoardMain();
//	}
	
	//위 메서드에서 페이징 기능 추가
	public HashMap<String, Object> getBoardMain(int page) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		//1. 페이징 객체 처리
		Paging paging = new Paging();
		paging.setPage(page);
		int count = bdao.getAllCount();
		paging.setTotalCount(count);
		
		//2. 계산된 Paging 객체에 의한 게시물 조회
		ArrayList<BoardDto> list = bdao.getBoardMain(paging);
		
		//3. 댓글 개수 조회
		for(BoardDto bdto : list) {
			int cnt = bdao.replyCount(bdto.getNum());
			bdto.setReplycnt(cnt);
		}
		
		//4. 페이징,리스트 등을 해쉬맵에 저장
		result.put("paging", paging);
		result.put("boardList", list);
		
		return result;
	}

	public HashMap<String, Object> boardView(int num) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		//1. 조회수++
		bdao.plusReadCount(num);
		
		//2. 게시글 조회
		BoardDto bdto = bdao.getBoardOne(num);
		
		//3. 댓글 조회
		ArrayList<ReplyDto> list = bdao.getReply(num);
		
		//4. 해쉬맵에 리턴할 내용 저장
		result.put("board", bdto);
		result.put("replyList", list);
		
		return result;
	}

	public void addReply(ReplyDto rdto) {
		bdao.addReply(rdto);
	}

	public HashMap<String, Object> boardViewWithoutCount(int num) {
		HashMap<String, Object> result = new HashMap<String, Object>();
		
		BoardDto bdto = bdao.getBoardOne(num);
		
		ArrayList<ReplyDto> list = bdao.getReply(num);
		
		result.put("board", bdto);
		result.put("replyList", list);
		
		return result;
	}

	public void deleteReply(int replynum) {
		bdao.deleteReply(replynum);
	}

	public void insertBoard(BoardDto bdto) {
		bdao.insertBoard(bdto);
	}

	public BoardDto getBoardOne(String num) {
		return bdao.getBoardOne(Integer.parseInt(num));
	}

	public void updateBoard(BoardDto bdto) {
		bdao.updateBoard(bdto);
	}

	public void deleteBoard(int num) {
		bdao.deleteBoard(num);
	}
}
