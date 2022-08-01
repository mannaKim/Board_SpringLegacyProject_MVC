package com.ezen.board.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.ezen.board.dto.BoardDto;
import com.ezen.board.dto.ReplyDto;
import com.ezen.board.util.DataBaseManager;
import com.ezen.board.util.Paging;

@Repository
public class BoardDao {

	Connection con = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	
	@Autowired
	DataBaseManager dbm;

	public ArrayList<BoardDto> getBoardMain(Paging paging) {
		ArrayList<BoardDto> list = new ArrayList<BoardDto>();
		String sql = "select * from ("
				+ "select * from ("
				+ "select rownum as rn, b.* from "
				+ "((select * from board order by num desc) b)"
				+ ") where rn>=?"
				+ ") where rn<=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, paging.getStartNum());
			pstmt.setInt(2, paging.getEndNum());
			rs = pstmt.executeQuery();
			while(rs.next()) {
				BoardDto bdto = new BoardDto();
				bdto.setNum(rs.getInt("num"));
				bdto.setPass(rs.getString("pass"));
				bdto.setUserid(rs.getString("userid"));
				bdto.setEmail(rs.getString("email"));
				bdto.setTitle(rs.getString("title"));
				bdto.setContent(rs.getString("content"));
				bdto.setReadcount(rs.getInt("readcount"));
				bdto.setWritedate(rs.getTimestamp("writedate"));
//				bdto.setReplycnt(rs.getInt("replycnt"));
				bdto.setImgfilename(rs.getString("imgfilename"));
				list.add(bdto);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
		return list;
	}

	public int getAllCount() {
		int count = 0;
		String sql = "select count(*) as cnt from board";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if(rs.next()) count = rs.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
		return count;
	}

	public int replyCount(int num) {
		int count = 0;
		String sql = "select count(*) as cnt from reply where boardnum=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,num);
			rs = pstmt.executeQuery();
			if(rs.next()) count = rs.getInt("cnt");
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
		return count;
	}

	public void plusReadCount(int num) {
		String sql = "update board"
				+ " set readcount = readcount+1"
				+ " where num=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
	}

	public BoardDto getBoardOne(int num) {
		BoardDto bdto = new BoardDto();
		String sql = "select * from board where num=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				bdto.setNum(rs.getInt("num"));
				bdto.setPass(rs.getString("pass"));
				bdto.setUserid(rs.getString("userid"));
				bdto.setEmail(rs.getString("email"));
				bdto.setTitle(rs.getString("title"));
				bdto.setContent(rs.getString("content"));
				bdto.setReadcount(rs.getInt("readcount"));
				bdto.setWritedate(rs.getTimestamp("writedate"));
//				bdto.setReplycnt(rs.getInt("replycnt"));
				bdto.setImgfilename(rs.getString("imgfilename"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
		return bdto;
	}

	public ArrayList<ReplyDto> getReply(int num) {
		ArrayList<ReplyDto> list = new ArrayList<ReplyDto>();
		String sql = "select * from reply"
				+ " where boardnum=?"
				+ " order by num desc";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				ReplyDto rvo = new ReplyDto();
				rvo.setNum(rs.getInt("num"));
				rvo.setBoardnum(rs.getInt("boardnum"));
				rvo.setUserid(rs.getString("userid"));
				rvo.setWritedate(rs.getTimestamp("writedate"));
				rvo.setContent(rs.getString("content"));
				list.add(rvo);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
		return list;
	}

	public void addReply(ReplyDto rdto) {
		String sql = "insert into reply(num, boardnum, userid, content)"
				+ " values(reply_seq.nextVal,?,?,?)";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, rdto.getBoardnum());
			pstmt.setString(2, rdto.getUserid());
			pstmt.setString(3, rdto.getContent());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
	}

	public void deleteReply(int replynum) {
		String sql = "delete reply where num=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, replynum);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
	}

	public void insertBoard(BoardDto bdto) {
		String sql = "insert into board(num,userid,pass,email,title,content,imgfilename)"
				+ " values(board_seq.nextVal,?,?,?,?,?,?)";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bdto.getUserid());
			pstmt.setString(2, bdto.getPass());
			pstmt.setString(3, bdto.getEmail());
			pstmt.setString(4, bdto.getTitle());
			pstmt.setString(5, bdto.getContent());
			pstmt.setString(6, bdto.getImgfilename());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
	}

	public void updateBoard(BoardDto bdto) {
		String sql = "update board"
				+ " set pass=?,email=?,title=?,content=?,imgfilename=?"
				+ " where num=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, bdto.getPass());
			pstmt.setString(2, bdto.getEmail());
			pstmt.setString(3, bdto.getTitle());
			pstmt.setString(4, bdto.getContent());
			pstmt.setString(5, bdto.getImgfilename());
			pstmt.setInt(6, bdto.getNum());
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
	}

	public void deleteBoard(int num) {
		String sql = "delete board where num=?";
		con = dbm.getConnection();
		try {
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1,num);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			dbm.close(con, pstmt, rs);
		}
	}
	
}
