package com.yedam.app.post;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Post {
	private int postId;
	private String postName;
	private String postContent;
	private String writerId;
	private Date insertDate;
	private Date updateDate;
	private int postView;
	private int boardId;

	@Override
	public String toString() {
		return "제목: " + postName + ", 작성자: " + writerId + ", 등록일자: " + insertDate;
	}

}
