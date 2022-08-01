package com.ezen.board.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.ezen.board.dto.MemberDto;
import com.ezen.board.service.MemberService;

@Controller
public class MemberController {

	@Autowired
	MemberService ms;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String firstRequest( HttpServletRequest request , Model model ) {
		
		HttpSession session =request.getSession();
		
		String url = "";
		if( session.getAttribute("loginUser") != null )
			url = "redirect:/boardList";   // redirect - jsp 페이지로의 이동이 아닌 다른 리퀘스트 이동일때 사용
		else 
			url = "member/loginForm";
		
		return url;
	}
	
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String login(  HttpServletRequest request, Model model ) {
		
		String url = "member/loginForm";
		
		String id = request.getParameter("id");
		String pwd = request.getParameter("pwd");
		
		MemberDto mdto = ms.getMember(id);
		
		if( mdto == null ) {
			model.addAttribute("message" , "아이디가 없습니다");
		} else if( mdto.getPwd() == null ) {
			model.addAttribute("message" , "DB 오류, 관리자에게 문의하세요");
		} else if( !mdto.getPwd().contentEquals(pwd)  ) {
			model.addAttribute("message" , "비밀번호가 틀렸습니다");
		} else if( mdto.getPwd().contentEquals(pwd)  ) {
			url = "redirect:/boardList";
			HttpSession session = request.getSession();
			session.setAttribute("loginUser", mdto);
		}
		
		return url;
		
	}
	
	
	@RequestMapping("logout")
	public String logout( Model model, HttpServletRequest request ) {
		
		HttpSession session = request.getSession();
		
		session.invalidate();
		//session.removeAttribute("loginUser");
	
		// return "member/loginForm";
		return "redirect:/";
	}
	
	
	@RequestMapping("/memberJoinForm")
	public String memberJoinForm(  ) {
		return "member/memberJoinForm";
	}
	
	
	@RequestMapping("/idcheck")
	public String idcheck( HttpServletRequest request , Model model ) {
		String id = request.getParameter("id");
		
		MemberDto mdto = ms.getMember(id);
		if( mdto == null ) {
			model.addAttribute("result" , -1);
		}else {
			model.addAttribute("result" , 1);
		}
		model.addAttribute("id", id);
		return "member/idcheck";
	}
	
	
	// RequestMapping   부터 리턴 까지 회원 가입 메서드를  작성하세요
	// MemberService 의 메서드이름은  insertMember 입니다
	@RequestMapping(value="/memberJoin", method=RequestMethod.POST)
	public String memberJoin( HttpServletRequest request, Model model) {
		MemberDto mdto = new MemberDto();
		mdto.setUserid( request.getParameter("id") );
		mdto.setPwd( request.getParameter("pwd") );
		mdto.setName( request.getParameter("name") );
		mdto.setPhone( request.getParameter("phone") );
		mdto.setEmail( request.getParameter("email") );
		
		int result = ms.insertMember( mdto );
		
		if( result == 1) model.addAttribute("message","회원가입 성공. 로그인하세요");
		else model.addAttribute("message","회원가입 실패. 다음에 다시 시도하세요");
		
		return "member/loginForm";
	}
	
	
	@RequestMapping("/memberEditForm")
	public String memberEditForm(Model model, HttpServletRequest request) {
		
		HttpSession session = request.getSession();
		if( session.getAttribute("loginUser") == null) 
			return "loginform";
		
		return "member/memberEditForm";
	}
	
	// RequestMapping 부터 리턴까지 회원정보수정메서드를 작성하세요
	// MemberService의 메서드 이름은 updateMember입니다.
	@RequestMapping(value="/memberEdit", method=RequestMethod.POST)
	public String memberEdit(Model model, HttpServletRequest request) {
		
		MemberDto mdto = new MemberDto();
		mdto.setUserid(request.getParameter("id"));
		mdto.setPwd(request.getParameter("pwd"));
		mdto.setName(request.getParameter("name"));
		mdto.setPhone(request.getParameter("phone"));
		mdto.setEmail(request.getParameter("email"));
		
		int result = ms.updateMember(mdto);
		
		HttpSession session = request.getSession();
		if(result==1) session.setAttribute("loginUser", mdto);
		
		return "redirect:/boardList";
	}
}











