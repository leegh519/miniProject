package com.yedam.app.common;

import java.util.List;

import com.yedam.app.post.Post;

public class postManagement extends BoardManagement {

	protected Post post;

	public postManagement(Post post) {
		this.post = post;
		while (true) {
			System.out.println();
			postPrint();
			commentPrint();
			int menu = selectMenu();
			if (menu == 1) {

			} else if (menu == 2) {

			} else if (menu == 3) {

			} else if (menu == 4) {

			} else if (menu == 9) {
				back();
				break;
			} else if (menu == -1) {
				continue;
			} else {
				inputErrMsg();
			}

		}
	}

	private void commentPrint() {
		List<String> comment = cdao.selectCommentAll(post.getPostId());
		System.out.println("----------------------------------------------------------------");
		System.out.println("댓글수 " + comment.size());
		for (String s : comment) {
			System.out.println(s);
		}
	}

	private void postPrint() {
		System.out.println("----------------------------------------------------------------");
		System.out.println("제목 : " + post.getPostName());
		System.out.println();
		System.out.println(post.getPostContent());
	}
}
