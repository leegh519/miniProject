package com.yedam.app.common;

import java.util.List;

import com.yedam.app.board.Board;
import com.yedam.app.post.Comment;
import com.yedam.app.post.Post;

public class postManagement extends BoardManagement {

	private Post post;
	private List<Comment> comment;

	public postManagement(Board board, Post post) {
		while (true) {
			this.post = pdao.selectOne(post.getPostId());
			comment = cdao.selectCommentAll(this.post.getPostId());
			postPrint();
			commentPrint(comment);
			menuPrint();
			int menu = selectMenu();
			if (menu == 1) {
				// 댓글 쓰기
				insertComment(board);
			} else if (menu == 2) {
				// 대댓글
				insertReComment(board);
			} else if (menu == 3) {
				// 댓글 수정
				updateComment(comment, board);
			} else if (menu == 4 && post.getWriterId().equals(loginInfo.getId())) {
				// 글 내용수정(삭제)
				updatePost();
				break;
			} else if (menu == 9) {
				// 뒤로 가기
				back();
				break;
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}

		}
	}

	// 댓글 수정
	private void updateComment(List<Comment> comment, Board board) {

		if (loginInfo != null && board.getWriteRole() < loginInfo.getWriteRole()) {
			System.out.println("댓글을 달수 없는 게시판입니다.");
			return;
		}

		// 수정할 댓글 번호 입력
		System.out.print("댓글번호> ");
		int commId = inputNumber();

		// 댓글번호 있는지 확인
		if (commId < 1 || commId > comment.size()) {
			System.out.println("해당 댓글이 존재하지 않습니다.");
			return;
		}

		Comment comm = comment.get(commId - 1);

		// 비밀번호 입력받아서 확인
		System.out.print("비밀번호> ");
		String password = inputString();
		if (!comm.getCommentPwd().equals(password)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
			return;
		}

		// 일치하면 수정할 내용입력 (-1입력하면 삭제)
		System.out.println("수정할내용 입력(-1: 삭제)");
		String content = inputString();
		if (content.equals("-1")) {
			cdao.delete(comm.getCommentId());
		} else {
			comm.setCommentContent(content);
			cdao.update(comm);
		}
	}

	// 대댓글 쓰기
	private void insertReComment(Board board) {

		if (loginInfo != null) {
			if (board.getWriteRole() < loginInfo.getWriteRole()) {
				System.out.println("댓글을 달수 없는 게시판입니다.");
			}
		} else {
			if (board.getWriteRole() < 2) {
				System.out.println("댓글을 달수 없는 게시판입니다.");
			}
		}

		if (loginInfo != null && board.getWriteRole() < loginInfo.getWriteRole()) {
			System.out.println("댓글을 달수 없는 게시판입니다.");
			return;
		}
		// 대댓글 작성할 댓글 번호 입력
		System.out.print("댓글번호> ");
		int commId = inputNumber();

		// 댓글번호 있는지 확인
		if (commId < 1 || commId > comment.size()) {
			System.out.println("해당 댓글이 존재하지 않습니다.");
			return;
		}

		// 내용입력
		Comment comm = new Comment();
		System.out.print("내용> ");
		comm.setCommentContent(inputString());
		comm.setPostId(post.getPostId());
		comm.setCommentParent(comment.get(commId - 1).getCommentId());

		// 익명게시판이 아니면 로그인계정 ID를 작성자로 설정
		// 댓글 비밀번호는 계정 비밀번호와 동일
		if (board.getWriteRole() < 2) {
			comm.setWriterId(loginInfo.getId());
			comm.setCommentPwd(loginInfo.getPassword());
		} else {
			// 비밀번호 입력
			System.out.print("비밀번호> ");
			comm.setCommentPwd(inputString());
		}

		cdao.insertRe(comm);

	}

	// 댓글 쓰기
	private void insertComment(Board board) {
		if (loginInfo != null && board.getWriteRole() < loginInfo.getWriteRole()) {
			System.out.println("댓글을 달수 없는 게시판입니다.");
			return;
		}

		// 내용입력
		Comment comm = new Comment();
		System.out.print("내용> ");
		comm.setCommentContent(inputString());
		comm.setPostId(post.getPostId());

		// 익명게시판이 아니면 로그인계정 ID를 작성자로 설정
		// 댓글 비밀번호는 계정 비밀번호와 동일
		if (board.getWriteRole() < 2) {
			comm.setWriterId(loginInfo.getId());
			comm.setCommentPwd(loginInfo.getPassword());
		} else {
			// 익명게시판이면 비밀번호 입력
			System.out.print("비밀번호> ");
			comm.setCommentPwd(inputString());
		}

		cdao.insert(comm);

	}

	@Override
	protected void menuPrint() {
		String menu = " 1.댓글작성 | 2.대댓글작성 | 3.댓글수정 ";
		// 게시물 작성자가 현재 로그인한 사용자면 글수정기능 추가
		if (loginInfo != null && post.getWriterId().equals(loginInfo.getId())) {
			menu += "| 4.게시물수정 ";
		}
		menu += "| 9.뒤로가기 ";
		System.out.println(line);
		System.out.println(menu);
		System.out.println(line);
	}

	// 글 내용수정(삭제)
	private void updatePost() {

		// 일치하면 수정할 내용입력 (-1입력하면 삭제)
		System.out.println("수정할내용 입력(-1: 삭제, 0: 입력마침)");
		String content = inputContent();
		if (content.equals("-1")) {
			pdao.delete(post.getPostId());
		} else {
			post.setPostContent(content);
			pdao.update(post);
		}
	}

	private void commentPrint(List<Comment> comment) {
		System.out.println(line);
		System.out.println("댓글 " + comment.size());
		for (int i = 0; i < comment.size(); i++) {
			System.out.print((i + 1) + ". ");
			System.out.println(comment.get(i));
		}
	}

	private void postPrint() {
		System.out.println(line);
		System.out.println("제목 : " + post.getPostName());
		System.out.println();
		System.out.println(post.getPostContent());
	}

}
