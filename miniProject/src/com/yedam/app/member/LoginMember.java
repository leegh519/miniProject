package com.yedam.app.member;

import java.util.List;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardManagement;
import com.yedam.app.common.Management;
import com.yedam.app.post.Post;
import com.yedam.app.post.PostManagement;

public class LoginMember extends Management {

	// 접속한 게시판 정보 저장
	private Board board;
	private List<Board> list;

	public void run() {

		while (true) {
			menuPrint();
			int menu = selectMenu();

			if (menu == 0) {
				// 로그아웃
				logout();
				break;
			} else if (menu == 1) {
				// 마이페이지
				myPage();
			} else if (menu == 2) {
				// 일일 top10
				top10();
			} else if (2 < menu && menu < list.size() + 3) {
				// 게시판 선택
				selectBoard(menu);
			} else if (menu == list.size() + 3 && loginInfo.getWriteRole() == 0) {
				// 게시판 추가
				insertBoard();
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}

		}
	}

	// 일일 top10
	private void top10() {
		List<Post> list = pdao.selectTOP10();
		int range = 10;
		if (list.size() < 10) {
			range = list.size();
		}
		for (int i = 0; i < range; i++) {
			System.out.print((i + 1) + ". ");
			System.out.println(list.get(i));
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

	// 게시판 추가
	private void insertBoard() {
		Board newBoard = new Board();
		System.out.print("게시판이름> ");
		newBoard.setBoardName(notNullCheck());
		System.out.print("읽기권한설정(0:관리자, 1:회원, 2:비회원)> ");
		newBoard.setReadRole(inputNumber());
		System.out.print("쓰기권한설정(0:관리자, 1:회원, 2:비회원)> ");
		newBoard.setWriteRole(inputNumber());
		bdao.insert(newBoard);
	}

	// 게시판 선택
	private void selectBoard(int menu) {
		board = list.get(menu - 3);
		new BoardManagement().run(board);
	}

	// 마이페이지
	private void myPage() {
		if (loginInfo.getWriteRole() == 0) {
			new AdminManagement().run(loginInfo);
		} else {
			new MemberManagement().run(loginInfo);
		}
	}

	// 로그아웃
	private void logout() {
		System.out.println("로그아웃합니다.");
		loginInfo = null;
	}

	@Override
	protected void menuPrint() {
		list = bdao.selectAllBoard();
		String boardList = " 0.로그아웃 | 1.마이페이지 | 2.일일조회수 TOP10 ";
		System.out.println(line);
		int cnt = 1;
		for (int i = 0; i < list.size(); i++) {
			boardList = boardList + "| " + (i + 3) + "." + list.get(i).getBoardName() + " ";
			if (boardList.length() > 50 * cnt) {
				boardList += "\n ";
				cnt++;
			}
		}
		if (loginInfo.getWriteRole() == 0) {
			boardList += "| " + (list.size() + 3) + ".게시판추가";
		}
		System.out.println(boardList);
		System.out.println(line);
	}

}
