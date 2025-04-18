package com.notice.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserDto {
	@NotBlank(message = "ユーザー名は必須です")
	@Size(min = 2, max = 30, message = "ユーザー名は2文字以上、30文字以内で入力してください")
	private String username;

	@NotBlank(message = "パスワードは必須です")
	@Size(min = 6, message = "パスワードは6文字以上で入力してください")
	private String password;
}
