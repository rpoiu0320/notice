package com.notice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.notice.util.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;

@Configuration		//	このクラスは設定に利用するクラスだと明示
@EnableWebSecurity	//	WebSecurity活性化	
@RequiredArgsConstructor	//	finalがあるオブジェクトのコンストラクタを自動的に生成
public class SecurityConfig {

	private final CustomUserDetailsService userDetailsService;

	@Bean	//	beanに登録、springで管理されるコンテナーにオブジェクトだと明示
	public PasswordEncoder passwordEncoder() {	//	パスワードにハッシュ関数を適用
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.
			csrf()	//	token設定
				.disable()
			.authorizeHttpRequests()	//	権限付与
				.requestMatchers("/user/login\", \"/css/**\", \"/js/**\", \"/images/**")	//	こんなurlは
				.permitAll()					//	みんな接続できる
				.anyRequest()					//	他のurlは
				.authenticated()				//	認証が必要
			.and()
				.formLogin()			//	ログイン設定
				.loginPage("/user/login")			//	ログインurl設定
				.defaultSuccessUrl("/board/list")	//	ログインが成功した後、移動するurl
			.and()
				.logout()				//	ログアウト設定
				.logoutSuccessUrl("/user/login")	//	ログアウトurl設定
				.invalidateHttpSession(true);		//	ログアウトした後、移動するurl

		return http.build();
	}
}
