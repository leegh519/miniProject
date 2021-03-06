package com.yedam.app.post;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;

public class CommentDAO extends DAO {

	// 싱글톤
	private static CommentDAO dao;

	private CommentDAO() {
	}

	public static CommentDAO getInstance() {
		if (dao == null) {
			dao = new CommentDAO();
		}
		return dao;
	}

	// 등록 - 댓글
	public void insert(Comment comment) {
		try {
			connect();
			String sql = "INSERT INTO comments (comment_id, comment_content, writer_id, post_id, comment_pwd) VALUES (comm_id_seq.nextval, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getCommentContent());
			pstmt.setString(2, comment.getWriterId());
			pstmt.setInt(3, comment.getPostId());
			pstmt.setString(4, comment.getCommentPwd());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("댓글이 등록되었습니다.");
			} else {
				System.out.println("등록실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 등록 - 대댓글
	public void insertRe(Comment comment) {
		try {
			connect();
			String sql = "INSERT INTO comments (comment_id, comment_content, writer_id, post_id, comment_pwd, comment_parent) VALUES (comm_id_seq.nextval, ?, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getCommentContent());
			pstmt.setString(2, comment.getWriterId());
			pstmt.setInt(3, comment.getPostId());
			pstmt.setString(4, comment.getCommentPwd());
			pstmt.setInt(5, comment.getCommentParent());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("댓글이 등록되었습니다.");
			} else {
				System.out.println("등록실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 수정 - 내용만
	public void update(Comment comment) {
		try {
			connect();
			String sql = "UPDATE comments SET comment_content = ? WHERE comment_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comment.getCommentContent());
			pstmt.setInt(2, comment.getCommentId());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("댓글이 수정되었습니다.");
			} else {
				System.out.println("수정실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 삭제
	public void delete(int commentId) {
		try {
			connect();
			String sql = "DELETE FROM comments WHERE comment_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, commentId);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("댓글이 삭제되었습니다.");
			} else {
				System.out.println("삭제실패");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 단건조회
	public Comment selectOne(int commentId) {
		Comment comment = null;
		try {
			connect();
			String sql = "SELECT * FROM comments WHERE comment_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, commentId);

			rs = pstmt.executeQuery();
			if (rs.next()) {
				comment = new Comment();
				comment.setCommentId(rs.getInt("comment_id"));
				comment.setWriterId(rs.getString("writer_id"));
				comment.setCommentContent(rs.getString("comment_content"));
				comment.setCommentPwd(rs.getString("comment_pwd"));
				comment.setCommentParent(rs.getInt("comment_parent"));
				comment.setInsertDate(rs.getString("insert_date"));
				comment.setPostId(rs.getInt("post_id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return comment;
	}

	// 전체조회 - 게시글 1개에 댓글 전체
	public List<Comment> selectCommentAll(int postId) {
		List<Comment> list = new ArrayList<Comment>();
		try {
			connect();
			String sql = "SELECT LPAD('  ', 2*(level-1))||'└ '||writer_id as writer_id, comment_content, comment_id, comment_pwd, comment_parent, insert_date, post_id FROM comments "
					+ " WHERE post_id = ? START WITH comment_parent IS NULL "
					+ "CONNECT BY PRIOR comment_id = comment_parent";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentId(rs.getInt("comment_id"));
				comment.setWriterId(rs.getString("writer_id").substring(2));
				comment.setCommentContent(rs.getString("comment_content"));
				comment.setCommentPwd(rs.getString("comment_pwd"));
				comment.setCommentParent(rs.getInt("comment_parent"));
				comment.setInsertDate(rs.getString("insert_date"));
				comment.setPostId(rs.getInt("post_id"));
				list.add(comment);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

	// 전체조회 - 게시글 1개에 댓글 전체
	public List<Comment> selectCommentWriterAll(String writerId) {
		List<Comment> list = new ArrayList<Comment>();
		try {
			connect();
			String sql = "SELECT * FROM comments WHERE writer_id = ? ORDER BY insert_date";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writerId);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentId(rs.getInt("comment_id"));
				comment.setWriterId(rs.getString("writer_id"));
				comment.setCommentContent(rs.getString("comment_content"));
				comment.setCommentPwd(rs.getString("comment_pwd"));
				comment.setCommentParent(rs.getInt("comment_parent"));
				comment.setInsertDate(rs.getString("insert_date"));
				comment.setPostId(rs.getInt("post_id"));
				list.add(comment);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

	public List<Comment> selectAll() {
		List<Comment> list = new ArrayList<Comment>();
		try {
			connect();
			String sql = "SELECT * FROM comments ORDER BY insert_date";
			pstmt = conn.prepareStatement(sql);

			rs = pstmt.executeQuery();
			while (rs.next()) {
				Comment comment = new Comment();
				comment.setCommentId(rs.getInt("comment_id"));
				comment.setWriterId(rs.getString("writer_id"));
				comment.setCommentContent(rs.getString("comment_content"));
				comment.setCommentPwd(rs.getString("comment_pwd"));
				comment.setCommentParent(rs.getInt("comment_parent"));
				comment.setInsertDate(rs.getString("insert_date"));
				comment.setPostId(rs.getInt("post_id"));
				list.add(comment);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

}
