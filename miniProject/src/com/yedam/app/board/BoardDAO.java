package com.yedam.app.board;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.yedam.app.common.DAO;

public class BoardDAO extends DAO {

	// 싱글톤
	private static BoardDAO dao;

	private BoardDAO() {
	}

	public static BoardDAO getInstance() {
		if (dao == null) {
			dao = new BoardDAO();
		}
		return dao;
	}

	// insert 새로운 게시판 생성
	public void insert(Board board) {
		try {
			connect();
			String sql = "INSERT INTO boards (board_id, board_name, board_readrole, board_writerole) VALUES (board_id_seq.nextval, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardName());
			pstmt.setInt(2, board.getReadRole());
			pstmt.setInt(3, board.getWriteRole());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println(board.getBoardName() + "이 생성되었습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// update 게시판 이름 변경
	public void updateName(Board board) {
		try {
			connect();
			String sql = "UPDATE boards SET board_name = ? WHERE board_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, board.getBoardName());
			pstmt.setInt(2, board.getBoardId());

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println(board.getBoardName() + "이 변경되었습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// update 게시판 접근권한 변경
	public void updateRole(Board board) {
		try {
			connect();
			String sql = "UPDATE boards SET board_readrole = ?, board_writerole = ? WHERE board_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, board.getReadRole());
			pstmt.setInt(2, board.getWriteRole());
			pstmt.setInt(3, board.getBoardId());

			int result = pstmt.executeUpdate();
			if (result > 0) {
				System.out.println(board.getBoardName() + "이 변경되었습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// delete 게시판 삭제
	public void delete(int boardId) {
		try {
			connect();
			String sql = "DELETE FROM boards WHERE board_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("게시판이 삭제되었습니다.");
			} else {
				System.out.println("삭제 실패하였습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	// selectOne 단건조회
	public Board selectOne(int boardId) {
		Board board = null;
		try {
			connect();
			String sql = "SELECT * FROM boards WHERE board_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				board = new Board();
				board.setBoardId(rs.getInt("board_id"));
				board.setBoardName(rs.getString("board_name"));
				board.setReadRole(rs.getInt("board_readrole"));
				board.setWriteRole(rs.getInt("board_writerole"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return board;
	}

	// selectAll 전체조회
	public List<Board> selectAllBoard() {
		List<Board> list = new ArrayList<Board>();
		try {
			connect();
			String sql = "SELECT * FROM boards";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Board board = new Board();
				board.setBoardId(rs.getInt("board_id"));
				board.setBoardName(rs.getString("board_name"));
				board.setReadRole(rs.getInt("board_readrole"));
				board.setWriteRole(rs.getInt("board_writerole"));
				list.add(board);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

}
