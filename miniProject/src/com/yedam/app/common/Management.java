package com.yedam.app.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

import com.yedam.app.board.Board;
import com.yedam.app.board.BoardDAO;
import com.yedam.app.board.BoardManagement;
import com.yedam.app.member.LoginMember;
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
	protected StringBuilder sb = new StringBuilder();
	protected static Member loginInfo = null;
	protected String line = "----------------------------------------------------------------";

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
		Board board = bdao.selectOne(2);
		new BoardManagement().run(board);
	}

	// 공지사항
	private void notice() {
		Board board = bdao.selectOne(1);
		new BoardManagement().run(board);
	}

	private void signUp() {
		// ID 입력
		System.out.print("ID> ");
		String id = notNullCheck();

		// 가입여부 확인
		Member member = mdao.selectOne(id);
		if (member != null) {
			System.out.println("이미 가입된 ID입니다.");
			return;
		}

		// 비밀번호 입력
		System.out.print("비밀번호> ");
		String password = notNullCheck();

		// 전화번호 입력
		System.out.print("전화번호> ");
		String phone = notNullCheck();

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
			return;
		}

		// 로그인 정보 저장
		loginInfo = member;
		System.out.println("로그인성공");

		
			new LoginMember().run();
		

	}

	// 입력 not null
	protected String notNullCheck() {
		String str;
		while (true) {
			str = inputString();
			StringTokenizer st = new StringTokenizer(str);
			if (!st.hasMoreTokens()) {
				System.out.print("내용을 입력하세요> ");
			} else {
				break;
			}
		}
		return str;
	}

	// 문자열 한줄 입력
	protected String inputString() {
		String str = "";
		try {
			str = br.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return str;
	}

	// 문자열 여러줄 입력
	protected String inputContent() {
		String str = "";
		try {
			while (true) {
				str = br.readLine();
				if (str.equals("저장")) {
					break;
				}
				sb.append(str).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sb.toString();
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
	}

	protected void menuPrint() {
		System.out.println(line);
		System.out.println("      1.로그인 | 2.회원가입 | 3.익명게시판 | 4.공지사항 | 9.종료");
		System.out.println(line);
	}
}
