package com.notice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.notice.util.CustomUserDetailsService;


@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncode()	//パスワードをハッシュ化
	{
		return new BCryptPasswordEncoder();
	}

	@Bean
	public HttpFirewall getHttpFirewall()	//ファイアウォール設定
	{
		return new DefaultHttpFirewall();
	}
	
	@Autowired
	private CustomUserDetailsService customUserDetailsService;
	
	@Bean
	public SecurityFilterChain sfc(HttpSecurity http) throws Exception //会員のログインについて
	{
		http
			.formLogin(formLogin -> formLogin
					.loginPage("/login")				//	使用者がログインをするurl
					.loginProcessingUrl("/user/login")	//	認証処理をするurl
					.usernameParameter("username")		//	使用者のパラメータ名
					.defaultSuccessUrl("/", true)		//	ログイン成功時にredirectするページ
					.failureForwardUrl("/error"))		//	失敗時
			.logout(logout -> logout
					.logoutRequestMatcher(new AntPathRequestMatcher("/user/logout"))	//	ログアウトのurl
					.logoutSuccessUrl("/")												//	ログインと一緒
					.invalidateHttpSession(true))										//	ログアウト時にsession削除可否
			.authorizeHttpRequests(authorize -> authorize
					.requestMatchers("/", "/user/**", "/comment/**", "/error")	// URLアクセス権限付与
					.permitAll()										// 全ての使用者に
					.requestMatchers("/admin/**")
					.hasRole("ADMIN")				// 管理者だけに付与
					.anyRequest().permitAll())
			.csrf(csrf -> csrf
					.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()))
			.userDetailsService(customUserDetailsService);
						//	認証されていないユーザーのリクエスト、例外処理ロジック
		
		return http.build();
	}
}