package com.notice.controller;

import com.notice.entity.User;
import com.notice.service.UserService;

import java.util.Collections;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@GetMapping("/login")
	public String loginForm() {
		// Spring Security가 처리하므로 단순히 로그인 페이지만 반환

		return "login";
	}

	@GetMapping("/register")
	public String registerForm(Model model) {
		model.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String register(@ModelAttribute("user") User user, Model model) {
		if (userService.register(user)) {
			return "redirect:/user/login";
		} else {
			model.addAttribute("error", "이미 사용 중인 아이디입니다.");
			return "register";
		}
	}

	@GetMapping("/check-username")
	@ResponseBody
	public Map<String, Boolean> checkUsername(@RequestParam("username") String username) {
		boolean exists = userService.existsByUsername(username);
		return Collections.singletonMap("exists", exists);
	}

	@GetMapping("/check-name")
	@ResponseBody
	public Map<String, Boolean> checkName(@RequestParam String name) {
		boolean exists = userService.existsByName(name);
		return Collections.singletonMap("exists", exists);
	}

}