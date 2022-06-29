package com.yedam.app.member;

import java.util.List;

import com.yedam.app.board.Board;
import com.yedam.app.common.Management;
import com.yedam.app.post.Comment;
import com.yedam.app.post.Post;
import com.yedam.app.post.PostManagement;

public class AdminManagement extends Management {

	public void run(Member loginInfo)   {

		while (true) {
			clear();
			menuPrint();
			int menu = selectMenu();
			if (menu == 1) {
				// 회원관리
				infoManagement();
			} else if (menu == 2) {
				// 게시물관리
				PostManagement();
			} else if (menu == 3) {
				// 댓글관리
				CommentManagement();
			} else if (menu == 9) {
				back();
				break;
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}

		}

	}

	private void CommentManagement()   {
		System.out.println("<전체 댓글>");
		List<Comment> list = cdao.selectAll();
		if (list.size() == 0) {
			System.out.print("댓글이 없습니다.");
			stop1s();
			return;
		}

		// 댓글 출력
		for (int i = 0; i < list.size(); i++) {
			System.out.print((i + 1) + ". ");
			System.out.println(list.get(i));
		}

		// 댓글 선택시 해당 댓글로 이동
		// 0 선택시 뒤로가기
		System.out.println(line);
		System.out.print("선택(0: 뒤로가기)> ");
		int commentNo = inputNumber();
		if (commentNo == 0) {
			System.out.print("이전화면으로 돌아갑니다.");
			stop1s();
			return;
		}

		// 인덱스 범위에 해당하는지 확인
		if (commentNo < 1 || commentNo > list.size()) {
			System.out.print("해당 댓글이 존재하지 않습니다.");
			stop1s();
			return;
		}
		Comment comment = list.get(commentNo - 1);
		Post post = pdao.selectOne(comment.getPostId());
		Board board = bdao.selectOne(post.getBoardId());
		new PostManagement(board, post);

	}

	private void PostManagement()   {
		System.out.println("<전체 게시물>");
		List<Post> list = pdao.selectAll();
		if (list.size() == 0) {
			System.out.print("게시물이 없습니다.");
			stop1s();
			return;
		}

		// 게시물 출력
		for (int i = 0; i < list.size(); i++) {
			Post post = list.get(i);
			System.out.print((i + 1) + ". " + bdao.selectOne(post.getBoardId()).getBoardName() + ") ");
			System.out.println(post);
		}

		// 게시물 선택시 해당 게시물로 이동
		// 0 선택시 뒤로가기
		System.out.println(line);
		System.out.print("선택(0: 뒤로가기)> ");
		int postNo = inputNumber();
		if (postNo == 0) {
			System.out.print("이전화면으로 돌아갑니다.");
			stop1s();
			return;
		}

		// 인덱스 범위에 해당하는지 확인
		if (postNo < 1 || postNo > list.size()) {
			System.out.print("해당 게시물이 존재하지 않습니다.");
			stop1s();
			return;
		}
		Post post = list.get(postNo - 1);
		Board board = bdao.selectOne(post.getBoardId());
		new PostManagement(board, post);

	}

	private void infoManagement()   {
		// 전체회원 출력
		List<Member> list = mdao.selectAllMember();
		for (int i = 0; i < list.size(); i++) {
			System.out.print((i + 1) + ". ");
			System.out.println(list.get(i));
		}

		// 게시물 선택시 해당 게시물로 이동
		// 0 선택시 뒤로가기
		System.out.println(line);
		System.out.print("선택(0: 뒤로가기)> ");
		int memberNo = inputNumber();
		if (memberNo == 0) {
			System.out.print("이전화면으로 돌아갑니다.");
			stop1s();
			return;
		}

		if (memberNo < 1 || memberNo > list.size()) {
			System.out.print("해당 회원이 존재하지 않습니다.");
			stop1s();
			return;
		}
		Member member = list.get(memberNo - 1);

		System.out.print("변경할 비밀번호> ");
		member.setPassword(notNullCheck());
		System.out.print("변경할 전화번호> ");
		member.setPhone(notNullCheck());
		System.out.print("변경할 읽기권한> ");
		member.setReadRole(inputNumber());
		System.out.print("변경할 쓰기권한> ");
		member.setWriteRole(inputNumber());
		mdao.updatePwd(member);
	}

	@Override
	protected void menuPrint() {
		System.out.println(line);
		System.out.println(" 1.회원관리 | 2.게시물관리 | 3.댓글관리 | 9.뒤로가기");
		System.out.println(line);
	}
}
