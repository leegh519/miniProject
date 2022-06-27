package com.yedam.app.member;

import java.util.List;

import com.yedam.app.board.Board;
import com.yedam.app.common.Management;
import com.yedam.app.post.Comment;
import com.yedam.app.post.Post;
import com.yedam.app.post.PostManagement;

public class MemberManagement extends Management {

	public void run(Member loginInfo)   {

		while (true) {
			menuPrint();
			int menu = selectMenu();
			if (menu == 1) {
				// 회원정보변경
				updateInfo();
			} else if (menu == 2) {
				// 내가 쓴 글
				myPost();
			} else if (menu == 3) {
				// 내가 쓴 댓글
				myComment();
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

	// 내가 쓴 댓글
	private void myComment()   {
		System.out.println("<내가 쓴 댓글>");
		List<Comment> list = cdao.selectCommentWriterAll(loginInfo.getId());
		if (list.size() == 0) {
			System.out.println("댓글이 없습니다.");
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
			System.out.println("이전화면으로 돌아갑니다.");
			return;
		}

		// 인덱스 범위에 해당하는지 확인
		if (commentNo < 1 || commentNo > list.size()) {
			System.out.println("해당 댓글이 존재하지 않습니다.");
			return;
		}
		Comment comment = list.get(commentNo - 1);
		Post post = pdao.selectOne(comment.getPostId());
		Board board = bdao.selectOne(post.getBoardId());
		new PostManagement(board, post);
	}

	// 내가 쓴 글
	private void myPost()   {
		System.out.println("<내가 쓴 글>");
		List<Post> list = pdao.selectWriter(loginInfo.getId());
		if (list.size() == 0) {
			System.out.println("게시물이 없습니다.");
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
			System.out.println("이전화면으로 돌아갑니다.");
			return;
		}

		// 인덱스 범위에 해당하는지 확인
		if (postNo < 1 || postNo > list.size()) {
			System.out.println("해당 게시물이 존재하지 않습니다.");
			return;
		}
		Post post = list.get(postNo - 1);
		Board board = bdao.selectOne(post.getBoardId());
		new PostManagement(board, post);

	}

	// 회원정보변경
	private void updateInfo() {
		// 기존 정보 출력
		System.out.println(loginInfo);

		// 변경여부 확인
		System.out.print("변경하시겠습니까?(y/n)> ");
		String choice = inputString();
		if (choice.toUpperCase().equals("N")) {
			return;
		}
		// 비밀번호, 전화번호 변경
		else if (choice.toUpperCase().equals("Y")) {
			System.out.print("변경할 비밀번호> ");
			loginInfo.setPassword(inputString());
			System.out.print("변경할 전화번호> ");
			loginInfo.setPhone(inputString());
			mdao.updatePwd(loginInfo);
		}
	}

	@Override
	protected void menuPrint() {
		System.out.println(line);
		System.out.println(" 1.회원정보변경 | 2.내가쓴글 | 3.내가쓴댓글 | 9.뒤로가기 ");
		System.out.println(line);
	}

}
