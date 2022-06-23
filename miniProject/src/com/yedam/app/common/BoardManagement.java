package com.yedam.app.common;

import java.util.ArrayList;
import java.util.List;

import com.yedam.app.post.Post;

public class BoardManagement extends Management {

	protected List<Post> list = new ArrayList<>();
	private int boardId;

	public void run(int boardId) {
		list = pdao.selectDateOrder(boardId);
		this.boardId = boardId;
		while (true) {
			System.out.println();
			System.out.println("----------------------------------------------------------------");
			System.out.println("<" + bdao.selectOne(boardId).getBoardName() + ">");
			postListPrint();
			menuPrint();
			int menu = selectMenu();
			if (menu == 1) {
				viewPost();
			} else if (menu == 2) {
				writePost();
			} else if (menu == 3) {
				printOrderByDate();
			} else if (menu == 4) {
				printOrderByView();
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

	private void writePost() {
		// 쓰기 권한 여부 확인

		// 제목, 내용입력

		// 완료

	}

	private void viewPost() {
		// 게시물 번호 선택
		System.out.print("게시물번호 선택> ");
		int postId = inputNumber();

		// 인덱스 범위에 해당하는지 확인
		if (postId < 1 || postId > list.size()) {
			System.out.println("해당 게시물이 존재하지 않습니다.");
			return;
		}

		// 조회수 +1
		Post post = list.get(postId - 1);
		System.out.println("조회수 " + (post.getPostId() + 1));
		post.setPostView(post.getPostView() + 1);
		pdao.updateView(post);

		// postManagement 실행, post를 매개변수로 전달
		// 게시물 출력
		// 댓글 출력
		// 메뉴 : 게시물 수정, 댓글달기, 댓글수정, 뒤로가기
		new postManagement(post);

	}

	@Override
	protected void menuPrint() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("  1.게시물보기 | 2.글쓰기 | 3.최신순 | 4.조회순 | 9.뒤로가기");
		System.out.println("----------------------------------------------------------------");
	}

	// 전체 게시물 출력
	protected void postListPrint() {

		for (int i = 0; i < list.size(); i++) {
			System.out.print((i + 1) + ". ");
			System.out.println(list.get(i));
		}
	}
	// 게시물 작성

	// 조회수 순으로 보기
	protected void printOrderByView() {
		list = pdao.selectViewOrder(boardId);
	}

	// 날짜 순으로 보기
	protected void printOrderByDate() {
		list = pdao.selectDateOrder(boardId);
	}

}
