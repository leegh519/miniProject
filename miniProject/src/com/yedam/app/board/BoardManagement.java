package com.yedam.app.board;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.yedam.app.common.Management;
import com.yedam.app.post.Post;
import com.yedam.app.post.PostManagement;

public class BoardManagement extends Management {

	protected List<Post> list = new ArrayList<>();
	private String order = "d";
	private int page = 1;
	private int lastPage = 1;

	public void run(Board board) {
		// 읽기권한 확인
		while (hasReadRole(board)) {
			clear();
			System.out.println();
			System.out.println(line);
			System.out.println("<" + bdao.selectOne(board.getBoardId()).getBoardName() + ">");
			listSort(board);
			postListPrint();
			menuPrint();
			int menu = selectMenu();
			if (menu == 1) {
				// 게시물 보기
				viewPost(board);
			} else if (menu == 2) {
				// 글쓰기
				writePost(board);
			} else if (menu == 3) {
				// 날짜순으로 보기
				printOrderByDate(board);
			} else if (menu == 4) {
				// 조회순으로 보기
				printOrderByView(board);
			} else if (menu == 5) {
				// 검색
				searchPost(board);
			} else if (menu == 6) {
				// 이전페이지
				previousPage();
			} else if (menu == 7) {
				// 다음페이지
				nextPage();
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

	private void nextPage() {
		if (page == (list.size() / 10) + 1) {
			System.out.print("마지막페이지입니다.");
			stop1s();
			return;
		}
		page++;
	}

	private void previousPage() {
		if (page == 1) {
			System.out.print("첫페이지입니다.");
			stop1s();
			return;
		}
		page--;
	}

	// list 정렬&갱신
	private void listSort(Board board) {
		if (order.equals("d")) {
			printOrderByDate(board);
		} else if (order.equals("v")) {
			printOrderByView(board);
		}
		lastPage = (list.size() / 10) + 1;
	}

	// 검색 - 제목 또는 내용
	private void searchPost(Board board) {
		System.out.print("검색내용> ");
		String search = notNullCheck();
		list = pdao.selectNameContent(search, board.getBoardId());
		if (list.size() == 0) {
			System.out.print("검색결과가 없습니다.");
			stop1s();
			order = "d";
			return;
		}
		order = "s";
	}

	// 읽기 권한 여부 확인
	protected boolean hasReadRole(Board board) {
		// 게시판의 권한 확인(0: 관리자, 1: 일반회원, 2:비회원)

		int readRole = 2;
		if (loginInfo != null) {
			// 로그인 계정이 있으면 계정에서 읽기 권한값을 가져옴
			readRole = loginInfo.getReadRole();
		}
		if (board.getReadRole() < readRole) {
			System.out.print("게시판 접근 권한이 없습니다.");
			stop1s();
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
			System.out.print("권한이 없습니다.");
			stop1s();
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
			post.setPostName(notNullCheck());
			System.out.println("내용(그만 입력하려면 \"저장\"입력)> ");
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
			System.out.print("해당 게시물이 존재하지 않습니다.");
			stop1s();
			return;
		}

		// 조회수 +1
		Post post = list.get(postId - 1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		Calendar c1 = Calendar.getInstance();
		int today = Integer.parseInt(sdf.format(c1.getTime()));
		if (post.getTodayView() % 1000000 != today) {
			post.setTodayView(today);
		}

		pdao.updateView(post);

		// postManagement 실행
		new PostManagement(board, post);

	}

	@Override
	protected void menuPrint() {
		System.out.println(line);
		System.out.println(
				"       1.게시물보기 | 2.글쓰기 | 3.최신순 | 4.조회순 | 5.검색 \n" + "             6.이전페이지 | 7.다음페이지 | 9.뒤로가기 ");
		System.out.println(line);
	}

	// 전체 게시물 출력
	protected void postListPrint() {
		int start = (page - 1) * 10;
		for (int i = start; i < start + 10; i++) {
			if (i >= list.size()) {
				break;
			}
			System.out.print((i + 1) + ". ");
			System.out.println(list.get(i));
		}
		System.out.println("(현재페이지 "+page+"/"+lastPage+")");
	}

	// 조회수 순으로 보기
	protected void printOrderByView(Board board) {
		list = pdao.selectViewOrder(board.getBoardId());
		order = "v";
	}

	// 날짜 순으로 보기
	protected void printOrderByDate(Board board) {
		list = pdao.selectDateOrder(board.getBoardId());
		order = "d";
	}

}
