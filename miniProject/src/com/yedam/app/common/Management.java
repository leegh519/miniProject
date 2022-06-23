package com.yedam.app.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import com.yedam.app.board.BoardDAO;
import com.yedam.app.member.Member;
import com.yedam.app.member.MembersDAO;
import com.yedam.app.post.CommentDAO;
import com.yedam.app.post.PostDAO;

public class Management {
	protected MembersDAO mdao = MembersDAO.getInstance();
	protected BoardDAO bdao = BoardDAO.getInstance();
	protected CommentDAO cdao = CommentDAO.getInstance();
	protected PostDAO pdao = PostDAO.getInstance();
	protected BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	protected static Member loginInfo = null;

	public void run() {

		while (true) {
			menuPrint();
			int menu = selectMenu();

			if (menu == 1) {
				// 로그인
				login();
			} else if (menu == 2) {
				// 회원가입
				signUp();
			} else if (menu == 3) {
				// 익명게시판
				anonyBoard();
			} else if (menu == 4) {
				// 공지사항
				notice();
			} else if (menu == 9) {
				// 종료
				exit();
				break;
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}

		}

	}

	// 익명게시판
	private void anonyBoard() {
		new BoardManagement().run(2);
	}

	// 공지사항
	private void notice() {
		new BoardManagement().run(1);
	}

	private void signUp() {
		// ID 입력
		System.out.print("ID> ");
		String id = inputString();

		// 가입여부 확인
		Member member = mdao.selectOne(id);
		if (member != null) {
			System.out.println("이미 가입된 ID입니다.");
			return;
		}

		// 비밀번호 입력
		System.out.print("비밀번호> ");
		String password = inputString();

		// 전화번호 입력
		System.out.print("전화번호> ");
		String phone = inputString();

		member = new Member();
		member.setId(id);
		member.setPassword(password);
		member.setPhone(phone);

		// 가입완료
		mdao.insert(member);

	}

	protected void inputErrMsg() {
		System.out.println("메뉴에 없는 기능입니다.");
	}

	protected void login() {
		// ID 입력
		System.out.print("ID> ");
		String id = inputString();

		// 가입여부 확인
		Member member = mdao.selectOne(id);
		if (member == null) {
			System.out.println("가입되지않은 회원입니다.");
			return;
		}

		// 비밀번호 입력
		System.out.print("비밀번호> ");
		String password = inputString();

		// 비밀번호 일치확인
		if (!member.getPassword().equals(password)) {
			System.out.println("비밀번호가 일치하지 않습니다.");
		}

		// 로그인 정보 저장
		loginInfo = member;
		System.out.println("로그인성공");

		// 관리자로 로그인시 실행
		if (loginInfo.getRole() == 0) {
			new Admin();
		}
		// 일반회원 로그인시 실행
		else if (loginInfo.getRole() == 1) {
			new LoginMember();
		}

	}

	protected String inputString() {
		String str = "";
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	protected int selectMenu() {
		System.out.print("선택> ");
		return inputNumber();
	}

	protected int inputNumber() {
		int n = -1;
		try {
			n = Integer.parseInt(br.readLine());
		} catch (NumberFormatException e) {
			System.out.println("숫자로 입력하세요");
		} catch (IOException e) {
			e.printStackTrace();
		}
		return n;
	}

	protected void exit() {
		System.out.println("프로그램을 종료합니다.");
	}

	protected void back() {
		System.out.println("이전화면으로 돌아갑니다.");
		System.out.println();
	}

	protected void menuPrint() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("  1.로그인 | 2.회원가입 | 3.익명게시판 | 4.공지사항 | 9.종료");
		System.out.println("----------------------------------------------------------------");
	}
}
