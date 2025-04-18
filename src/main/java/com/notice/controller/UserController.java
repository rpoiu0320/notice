package com.notice.controller;

import com.notice.entity.User;
import com.notice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

	private final UserService userService;

	@GetMapping("/signup")
	public String signupForm(Model model) {
		model.addAttribute("user", User.builder());
		return "user/signup";
	}

	@PostMapping("/signup")
	public String signupSubmit(@ModelAttribute User user) {
		userService.save(user);
		return "redirect:/user/login";
	}

	@GetMapping("/login")
	public String loginForm() {
		return "user/login";
	}
}
