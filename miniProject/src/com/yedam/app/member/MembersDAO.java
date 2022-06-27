package com.yedam.app.member;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.DAO;

public class MembersDAO extends DAO {
	// 싱글톤
	private static MembersDAO dao;

	private MembersDAO() {
	}

	public static MembersDAO getInstance() {
		if (dao == null) {
			dao = new MembersDAO();
		}
		return dao;
	}

	// 등록 - 회원가입
	public void insert(Member member) {

		try {
			connect();
			String sql = "INSERT INTO members (id, password, phone) VALUES (?, ?, ?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPassword());
			pstmt.setString(3, member.getPhone());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("회원가입이 완료되었습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}

	}

	// 수정 - 비밀번호, 전화번호
	public void updatePwd(Member member) {

		try {
			connect();
			String sql = "UPDATE members SET password = ?, phone = ?, readrole = ?, writerole = ? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getPassword());
			pstmt.setString(2, member.getPhone());
			pstmt.setInt(3, member.getReadRole());
			pstmt.setInt(4, member.getWriteRole());
			pstmt.setString(5, member.getId());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("수정되었습니다.");
			} else {
				System.out.println("수정이 실패하였습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 수정 - 회원등급
	public void updateRole(Member member) {

		try {
			connect();
			String sql = "UPDATE members SET readrole = ?, writerole=? WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setInt(1, member.getReadRole());
			pstmt.setInt(2, member.getWriteRole());
			pstmt.setString(3, member.getId());

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("회원등급이 수정되었습니다.");
			} else {
				System.out.println("수정이 실패하였습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 삭제
	public void delete(String id) {
		try {
			connect();
			String sql = "DELETE FROM members WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);

			int result = pstmt.executeUpdate();

			if (result > 0) {
				System.out.println("회원이 삭제되었습니다.");
			} else {
				System.out.println("삭제 실패하였습니다.");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
	}

	// 전체조회
	public List<Member> selectAllMember() {
		List<Member> list = new ArrayList<Member>();
		try {
			connect();
			String sql = "SELECT * FROM members ORDER BY reg_date";
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);

			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPassword(rs.getString("password"));
				m.setReadRole(rs.getInt("readrole"));
				m.setWriteRole(rs.getInt("writerole"));
				m.setRegDate(rs.getString("reg_date"));
				m.setPhone(rs.getString("phone"));
				list.add(m);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return list;
	}

	// 단건조회 - 회원id로 검색
	public Member selectOne(String id) {
		Member member = null;
		try {
			connect();
			String sql = "SELECT * FROM members WHERE id = ?";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				member = new Member();
				member.setId(rs.getString("id"));
				member.setPassword(rs.getString("password"));
				member.setReadRole(rs.getInt("readrole"));
				member.setWriteRole(rs.getInt("writerole"));
				member.setRegDate(rs.getString("reg_date"));
				member.setPhone(rs.getString("phone"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			disconnect();
		}
		return member;

	}

}
