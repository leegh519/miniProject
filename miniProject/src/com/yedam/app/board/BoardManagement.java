package com.yedam.app.board;

import java.util.ArrayList;
import java.util.List;

import com.yedam.app.common.Management;
import com.yedam.app.post.Post;
import com.yedam.app.post.PostManagement;

public class BoardManagement extends Management {

	protected List<Post> list = new ArrayList<>();

	public void run(Board board) {
		list = pdao.selectDateOrder(board.getBoardId());

		// 읽기권한 확인
		while (hasReadRole(board)) {
			System.out.println();
			System.out.println(line);
			System.out.println("<" + bdao.selectOne(board.getBoardId()).getBoardName() + ">");
			postListPrint();
			menuPrint();
			int menu = selectMenu();
			if (menu == 1) {
				// 게시물 보기
				viewPost(board);
			} else if (menu == 2) {
				// 글쓰기
				writePost(board);
				printOrderByDate(board);
			} else if (menu == 3) {
				// 날짜순으로 보기
				printOrderByDate(board);
			} else if (menu == 4) {
				// 조회순으로 보기
				printOrderByView(board);
			} else if (menu == 5) {
				// 검색
				searchPost(board);
			} else if (menu == 9) {
				// 뒤로가기
				back();
				break;
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}
		}
	}

	// 검색 - 제목 또는 내용
	private void searchPost(Board board) {
		System.out.print("검색내용> ");
		String search = inputString();
		list = pdao.selectNameContent(search, board.getBoardId());
		if (list.size() == 0) {
			System.out.println("검색결과가 없습니다.");
			list = pdao.selectDateOrder(board.getBoardId());
		}
	}

	// 읽기 권한 여부 확인
	private boolean hasReadRole(Board board) {
		// 게시판의 권한 확인(0: 관리자, 1: 일반회원, 2:비회원)

		int readRole = 2;
		if (loginInfo != null) {
			// 로그인 계정이 있으면 계정에서 읽기 권한값을 가져옴
			readRole = loginInfo.getReadRole();
		}
		if (board.getReadRole() < readRole) {
			System.out.println("게시판 접근 권한이 없습니다.");
			return false;
		}
		return true;
	}

	// 쓰기 권한 여부 확인
	protected boolean hasWriteRole(Board board) {
		// 2(비회원)를 기본값으로 주고
		// 로그인 계정이 있으면 계정에서 쓰기 권한값을 가져옴
		int writeRole = 2;
		if (loginInfo != null) {
			writeRole = loginInfo.getWriteRole();
		}
		if (board.getWriteRole() < writeRole) {
			System.out.println("권한이 없습니다.");
			return false;
		}
		return true;
	}

	// 게시물 작성
	private void writePost(Board board) {
		// 쓰기 권한 여부 확인
		if (hasWriteRole(board)) {
			Post post = new Post();
			// 제목, 내용입력
			System.out.print("제목> ");
			post.setPostName(inputString());
			System.out.println("내용(그만 입력하려면 0입력)> ");
			post.setPostContent(inputContent());

			// 게시판 번호, 작성자 설정
			post.setBoardId(board.getBoardId());

			// 익명게시판이 아니면 로그인계정 ID를 작성자로 설정
			if (board.getWriteRole() < 2) {
				post.setWriterId(loginInfo.getId());
			}

			// DB에 등록
			pdao.insert(post);
		}
	}

	// 게시물 보기
	private void viewPost(Board board) {
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
		post.setPostView(post.getPostView() + 1);
		pdao.updateView(post);

		// postManagement 실행
		new PostManagement(board, post);

	}

	@Override
	protected void menuPrint() {
		System.out.println(line);
		System.out.println("  1.게시물보기 | 2.글쓰기 | 3.최신순 | 4.조회순 | 5.검색 | 9.뒤로가기 ");
		System.out.println(line);
	}

	// 전체 게시물 출력
	protected void postListPrint() {
		for (int i = 0; i < list.size(); i++) {
			System.out.print((i + 1) + ". ");
			System.out.println(list.get(i));
		}
	}

	// 조회수 순으로 보기
	protected void printOrderByView(Board board) {
		list = pdao.selectViewOrder(board.getBoardId());
	}

	// 날짜 순으로 보기
	protected void printOrderByDate(Board board) {
		list = pdao.selectDateOrder(board.getBoardId());
	}

}
