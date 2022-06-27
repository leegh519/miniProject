package com.yedam.app.member;

import java.util.List;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardManagement;
import com.yedam.app.common.Management;

public class LoginMember extends Management {

	// 접속한 게시판 정보 저장
	private Board board;
	private List<Board> list;

	public void run()   {

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
			} else if (1 < menu && menu < list.size() + 2) {
				// 게시판 선택
				selectBoard(menu);
			} else if (menu == list.size() + 2 && loginInfo.getWriteRole() == 0) {
				// 게시판 추가
				insertBoard();
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}

		}
	}

	private void insertBoard()   {
		Board newBoard = new Board();
		System.out.print("게시판이름> ");
		newBoard.setBoardName(inputString());
		System.out.print("읽기권한설정(0:관리자, 1:회원, 2:비회원)> ");
		newBoard.setReadRole(inputNumber());
		System.out.print("쓰기권한설정(0:관리자, 1:회원, 2:비회원)> ");
		newBoard.setWriteRole(inputNumber());
		bdao.insert(newBoard);
	}

	// 게시판 선택
	private void selectBoard(int menu)   {
		board = list.get(menu - 2);
		new BoardManagement().run(board);
	}

	// 마이페이지
	private void myPage()   {
		if (loginInfo.getWriteRole() == 0) {
			new AdminManagement().run(loginInfo);	
		}else {
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
		String boardList = " 0.로그아웃 | 1.마이페이지 ";
		System.out.println(line);
		int cnt = 1;
		for (int i = 0; i < list.size(); i++) {
			boardList = boardList + "| " + (i + 2) + "." + list.get(i).getBoardName() + " ";
			if (boardList.length() > 50 * cnt) {
				boardList += "\n ";
				cnt++;
			}
		}
		if (loginInfo.getWriteRole() == 0) {
			boardList += "| " + (list.size() + 2) + ".게시판추가";
		}
		System.out.println(boardList);
		System.out.println(line);
	}

}
