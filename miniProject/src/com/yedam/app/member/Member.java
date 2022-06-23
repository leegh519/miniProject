package com.yedam.app.member;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Member {
	private String id;
	private String password;
	// 0:관리자, 1:일반유저, 2:정지당한유저
	private int role;
	private Date regDate;
	private String phone;

	@Override
	public String toString() {
		return "ID: " + id + ", 비밀번호: " + password + ", 전화번호: " + phone + ", 가입날짜: " + regDate;
	}

}
