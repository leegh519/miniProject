package com.yedam.app.post;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
	private int postId;
	private String postName;
	private String postContent;
	private String writerId = "anony";
	private String insertDate;
	private String updateDate;
	private int postView;
	private int boardId;

	@Override
	public String toString() {
		return "제목: " + postName + ", 작성자: " + writerId + ", 등록일자: " + insertDate + ", 조회수: " + postView;
	}

}
