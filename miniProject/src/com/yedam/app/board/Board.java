package com.yedam.app.board;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Board {
	private int boardId;
	private String boardName;
	// 0: 관리자만, <=1: 회원 2<=: 비회원
	private int readRole;
	// 0: 관리자만, <=1: 회원 2<=: 비회원
	private int writeRole;

	@Override
	public String toString() {
		String rRole = "비회원유저";
		String wRole = "비회원유저";
		if (readRole == 1) {
			rRole = "회원유저";
		} else if (readRole == 0) {
			rRole = "관리자";
		}
		if (readRole == 1) {
			wRole = "회원유저";
		} else if (readRole == 0) {
			wRole = "관리자";
		}
		return "게시판번호: " + boardId + ", 게시판이름:" + boardName + ", 읽기권한:" + rRole + ", 수정권한" + wRole;
	}
}