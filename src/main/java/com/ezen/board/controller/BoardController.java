package com.ezen.board.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezen.board.dto.BoardDto;
import com.ezen.board.dto.ReplyDto;
import com.ezen.board.service.BoardService;
import com.ezen.board.util.Paging;
import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

@Controller
public class BoardController {

	@Autowired
	BoardService bs;
	
	@Autowired
	ServletContext context; //파일업로드에서 사용
	
	@RequestMapping("/boardList")
	public String main( HttpServletRequest request , Model model) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser")==null)
			return "member/loginForm";
		else {
			int page = 1;
			if(request.getParameter("page")!=null) {
				page = Integer.parseInt(request.getParameter("page"));
				session.setAttribute("page", page);
			} else if(session.getAttribute("page")!=null) {
				page = (Integer)session.getAttribute("page");
			} else {
				session.removeAttribute("page");
			}
			// page 값을 전달해서, 모든 멤버변수가 계산된 Paging 객체와 그에 맞는 게시물리스트를 리턴 받습니다.
			// 서로 다른 형(type)의 두 개의 객체를 동시에 리턴받기 위해서는 두 개의 객체를 담을 수 있는 HashMap을 이용합니다.
			HashMap <String,Object> result = bs.getBoardMain(page);
			model.addAttribute("paging", (Paging)result.get("paging"));
			model.addAttribute("boardList", (ArrayList<BoardDto>)result.get("boardList"));
		}
		return "board/main";
	}
	
	
	@RequestMapping("/boardView")
	public String boardView(Model model, HttpServletRequest request) {
		// model에 담겨서 이동해야 할 데이터 : 게시물 번호로 조회한 게시물 내용, 그 게시물의 댓글 리스트
		// 해야 할 일 : 조회수++
		
		int num = Integer.parseInt(request.getParameter("num")); // 게시물 번호
		
//		BoardDto bdto= bs.getBoardView(num); // 게시물조회 & 조회수+1
//		model.addAttribute("board", bdto);
//		ArrayList<ReplyDto> list = bs.getReply(num); // 댓글리스트 받기
//		model.addAttribute("replyList", list);
		
		//위 명령을 간단하게(?) 해쉬맵으로 받아오기
		HashMap<String, Object> result = bs.boardView(num);
		model.addAttribute("board", (BoardDto)result.get("board"));
		model.addAttribute("replyList", (ArrayList<ReplyDto>)result.get("replyList"));
		
		return "board/boardView";
	}
	
	@RequestMapping(value="/addReply", method=RequestMethod.POST)
	public String addReply(Model model, HttpServletRequest request) {
		//되돌아갈 게시물 번호를 따로 저장
		String boardnum = request.getParameter("boardnum");
		
		ReplyDto rdto = new ReplyDto();
		rdto.setUserid(request.getParameter("userid"));
		rdto.setContent(request.getParameter("content"));
		rdto.setBoardnum(Integer.parseInt(boardnum));
		
		bs.addReply(rdto);
		
		return "redirect:/boardViewWithoutCount?num="+boardnum;
	}
	
	
	@RequestMapping("/boardViewWithoutCount")
	public String boardViewWithoutCount(Model model, HttpServletRequest request) {
		int num = Integer.parseInt(request.getParameter("num"));
		
		HashMap<String, Object> result = bs.boardViewWithoutCount(num);
		model.addAttribute("board", (BoardDto)result.get("board"));
		model.addAttribute("replyList", (ArrayList<ReplyDto>)result.get("replyList"));
		
		return "board/boardView";
	}
	
	
	@RequestMapping("/deleteReply")
	public String deleteReply(Model model, HttpServletRequest request) {
		//전달된 댓글 번호로 댓글을 삭제하고
		//전달돤 boardnum으로 원래 보던 게시물 페이지로 이동
		int boardnum = Integer.parseInt(request.getParameter("boardnum")); //게시물번호
		
		bs.deleteReply(Integer.parseInt(request.getParameter("replynum"))); //댓글번호 전달
		
		return "redirect:/boardViewWithoutCount?num="+boardnum;
	}
	
	
	@RequestMapping("/boardWriteForm")
	public String boardWriteForm(Model model, HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(session.getAttribute("loginUser")==null)
			return "member/loginForm";
		return "board/boardWriteForm";
	}

	
	@RequestMapping(value="/boardWrite", method = RequestMethod.POST)
	public String boardWrite(Model model, HttpServletRequest request) {
		String path = context.getRealPath("resources/upload");
		try {
			MultipartRequest multi = new MultipartRequest(
					request, path, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			BoardDto bdto = new BoardDto();
			bdto.setUserid(multi.getParameter("userid"));
			bdto.setPass(multi.getParameter("pass"));
			bdto.setEmail(multi.getParameter("email"));
			bdto.setTitle(multi.getParameter("title"));
			bdto.setContent(multi.getParameter("content"));
			bdto.setImgfilename(multi.getFilesystemName("imgfilename"));
			
			bs.insertBoard(bdto);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "redirect:/boardList";
	}
	
	
	@RequestMapping("/boardEditForm")
	public String boardEditForm(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		model.addAttribute("num",num);
		return "board/boardCheckPassForm";
	}
	
	
	@RequestMapping("/boardEdit")
	public String boardEdit(Model model, HttpServletRequest request) {
		// 전달된 num값으로 게시물 조회하고,
		// 입력한 pass와 비교해서 수정페이지로 이동할지 여부를 결정합니다.
		String num = request.getParameter("num");
		String pass = request.getParameter("pass");
		
		BoardDto bdto = bs.getBoardOne(num);
		
		model.addAttribute("num", num);
		
		if(bdto.getPass().equals(pass)) {
			return "board/boardCheckPass";
		}else {
			model.addAttribute("message", "비밀번호가 맞지 않습니다.");
			return "board/boardCheckPassForm";
		}
	}
	
	
	@RequestMapping("/boardUpdateForm")
	public String boardUpdateForm(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");

		BoardDto bdto = bs.getBoardOne(num);
		model.addAttribute("num", num);
		model.addAttribute("board", bdto);
		
		return "board/boardEditForm";
	}
	
	
	@RequestMapping(value="/boardUpdate", method = RequestMethod.POST)
	public String boardUpdate(Model model, HttpServletRequest request) {		
		String url = "";
		//MultipartRequest 선언
		String path = context.getRealPath("resources/upload");
		MultipartRequest multi;
		try {
			multi = new MultipartRequest(
					request, path, 5*1024*1024, "UTF-8", new DefaultFileRenamePolicy()
			);
			
			//각 파라미터들을 dto에 저장
			int num = Integer.parseInt(multi.getParameter("num")); //게시물번호
			BoardDto bdto = new BoardDto();
			bdto.setNum(num);
			bdto.setUserid(multi.getParameter("userid"));
			bdto.setPass(multi.getParameter("pass"));
			bdto.setEmail(multi.getParameter("email"));
			bdto.setTitle(multi.getParameter("title"));
			bdto.setContent(multi.getParameter("content"));
			
			//이미지 선별 저장
			if(multi.getFilesystemName("imgfilename")==null) {
				bdto.setImgfilename(multi.getParameter("oldfilename"));
			} else {
				bdto.setImgfilename(multi.getFilesystemName("imgfilename"));
			}
			
			//게시물 수정
			bs.updateBoard(bdto);
			
			//원래 보던 게시물로 다시 돌아가기
			url = "redirect:/boardViewWithoutCount?num="+num;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return url;
	}
	
	
	@RequestMapping("/boardDeleteForm")
	public String boardDeleteForm(Model model, HttpServletRequest request) {
		String num = request.getParameter("num");
		BoardDto bdto = bs.getBoardOne(num);
		model.addAttribute("num",num);
		model.addAttribute("board", bdto);
		return "board/boardCheckPassForm";
	}
	
	
	@RequestMapping("/boardDelete")
	public String boardDelete(Model model, HttpServletRequest request) {
		//전달된 게시물번호로 게시물을 삭제하세요
		int num = Integer.parseInt(request.getParameter("num"));
		bs.deleteBoard(num);
		return "redirect:/boardList";
	}
	
}
