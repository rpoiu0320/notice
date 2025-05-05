package com.notice.service;

import com.notice.entity.User;
import com.notice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpSession;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder; // 추가

	public boolean register(User user) {
		if (userRepository.findByUsername(user.getUsername()) != null) {
			return false;
		}

		// 비밀번호 암호화
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		userRepository.save(user);
		return true;
	}

	public boolean login(String username, String password, HttpSession session) {
		User user = userRepository.findByUsername(username);

		if (user != null && passwordEncoder.matches(password, user.getPassword())) {
			session.setAttribute("loggedInUser", username);
			return true;
		}

		return false;
	}

	public void logout(HttpSession session) {
		session.removeAttribute("loggedInUser");
	}

	public User getCurrentUser(HttpSession session) {
		String username = (String) session.getAttribute("loggedInUser");
		if (username != null) {
			return userRepository.findByUsername(username);
		}
		return null;
	}
	
	public boolean existsByUsername(String username) {
	    return userRepository.findByUsername(username) != null;
	}

	public boolean existsByName(String name) {
	    return userRepository.findByName(name) != null;
	}

}