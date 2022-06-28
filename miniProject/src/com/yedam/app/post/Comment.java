package com.yedam.app.post;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Comment {
	private int commentId;
	private String WriterId = "anony";
	private String commentContent;
	private String commentPwd;
	private int commentParent;
	private String insertDate;
	private int postId;

	@Override
	public String toString() {
		return WriterId + ": " + commentContent + ", 작성일: " + insertDate.substring(2);
	}

}
