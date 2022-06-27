package com.yedam.app.post;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;

public class PostDAO extends DAO {

	// 싱글톤
	private static PostDAO dao;

	private PostDAO() {
	}

	public static PostDAO getInstance() {
		if (dao == null) {
			dao = new PostDAO();
		}
		return dao;
	}

	// 등록
	public void insert(Post post) {

		try {
			connect();
			String sql = "INSERT INTO posts (post_id, post_name, post_content, writer_id, board_id) VALUES (post_id_seq.nextval, ?, ?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getPostName());
			pstmt.setString(2, post.getPostContent());
			pstmt.setString(3, post.getWriterId());
			pstmt.setInt(4, post.getBoardId());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("게시물이 등록되었습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	// 수정 - 내용
	public void update(Post post) {

		try {
			connect();
			String sql = "UPDATE posts SET post_content = ?, update_date = sysdate WHERE post_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, post.getPostContent());
			pstmt.setInt(2, post.getPostId());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("게시물이 수정되었습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	// 수정 - 게시물 조회수
	public void updateView(Post post) {

		try {
			connect();
			String sql = "UPDATE posts SET post_view = ? WHERE post_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, post.getPostView());
			pstmt.setInt(2, post.getPostId());

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	// 삭제
	public void delete(int postId) {
		try {
			connect();
			String sql = "DELETE FROM posts WHERE post_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postId);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("게시물이 삭제되었습니다.");
			} else {
				System.out.println("삭제 실패하였습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 단건조회
	public Post selectOne(int postId) {
		Post post = null;
		try {
			connect();
			String sql = "SELECT * FROM posts WHERE post_id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, postId);

			rs = pstmt.executeQuery();

			if (rs.next()) {
				post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setPostName(rs.getString("post_name"));
				post.setPostContent(rs.getString("post_content"));
				post.setWriterId(rs.getString("writer_id"));
				post.setInsertDate(rs.getString("insert_date"));
				post.setUpdateDate(rs.getString("update_date"));
				post.setPostView(rs.getInt("post_view"));
				post.setBoardId(rs.getInt("board_id"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return post;
	}

	// 전체조회 - 날짜순
	public List<Post> selectDateOrder(int boardId) {
		List<Post> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM posts WHERE board_id = ? ORDER BY insert_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Post post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setPostName(rs.getString("post_name"));
				post.setPostContent(rs.getString("post_content"));
				post.setWriterId(rs.getString("writer_id"));
				post.setInsertDate(rs.getString("insert_date"));
				post.setUpdateDate(rs.getString("update_date"));
				post.setPostView(rs.getInt("post_view"));
				post.setBoardId(rs.getInt("board_id"));

				list.add(post);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

	// 전체조회 - 날짜순
	public List<Post> selectViewOrder(int boardId) {
		List<Post> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM posts WHERE board_id = ? ORDER BY post_view DESC, insert_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Post post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setPostName(rs.getString("post_name"));
				post.setPostContent(rs.getString("post_content"));
				post.setWriterId(rs.getString("writer_id"));
				post.setInsertDate(rs.getString("insert_date"));
				post.setUpdateDate(rs.getString("update_date"));
				post.setPostView(rs.getInt("post_view"));
				post.setBoardId(rs.getInt("board_id"));

				list.add(post);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

	// 전체조회 - 작성자 ID
	public List<Post> selectWriter(String writerId) {
		List<Post> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM posts WHERE writer_id = ? ORDER BY insert_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, writerId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Post post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setPostName(rs.getString("post_name"));
				post.setPostContent(rs.getString("post_content"));
				post.setWriterId(rs.getString("writer_id"));
				post.setInsertDate(rs.getString("insert_date"));
				post.setUpdateDate(rs.getString("update_date"));
				post.setPostView(rs.getInt("post_view"));
				post.setBoardId(rs.getInt("board_id"));

				list.add(post);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

	// 전체조회 - 제목이나 내용 검색
	public List<Post> selectNameContent(String search, int boardId) {
		List<Post> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM posts WHERE board_id = ? AND (post_name LIKE '%" + search
					+ "%' OR post_content LIKE '%" + search + "%') ORDER BY insert_date DESC";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, boardId);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Post post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setPostName(rs.getString("post_name"));
				post.setPostContent(rs.getString("post_content"));
				post.setWriterId(rs.getString("writer_id"));
				post.setInsertDate(rs.getString("insert_date"));
				post.setUpdateDate(rs.getString("update_date"));
				post.setPostView(rs.getInt("post_view"));
				post.setBoardId(rs.getInt("board_id"));

				list.add(post);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

	// 전체조회
	public List<Post> selectAll() {
		List<Post> list = new ArrayList<>();
		try {
			connect();
			String sql = "SELECT * FROM posts ORDER BY insert_date DESC";
			pstmt = conn.prepareStatement(sql);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Post post = new Post();
				post.setPostId(rs.getInt("post_id"));
				post.setPostName(rs.getString("post_name"));
				post.setPostContent(rs.getString("post_content"));
				post.setWriterId(rs.getString("writer_id"));
				post.setInsertDate(rs.getString("insert_date"));
				post.setUpdateDate(rs.getString("update_date"));
				post.setPostView(rs.getInt("post_view"));
				post.setBoardId(rs.getInt("board_id"));

				list.add(post);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

		return list;
	}

}
