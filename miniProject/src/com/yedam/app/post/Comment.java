package com.yedam.app.post;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
	private int commentId;
	private String WriterId;
	private String commentContent;
	private String commentPwd;
	private int commentParent;
	private Date insertDate;
	private int postId;

	@Override
	public String toString() {
		return "작성자: " + WriterId + ", 내용: " + commentContent + ", 작성일: " + insertDate;
	}

}
