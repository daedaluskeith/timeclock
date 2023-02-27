package com.karlkeith.paychex.timeclock.controllers;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;
import com.karlkeith.paychex.timeclock.services.TimeClockUserService;

@Controller
public class SignupController {
	
	private final TimeClockUserService userService;
	
	private final JdbcUserDetailsManager jdbcUserDetailsManager;
	
	public SignupController(TimeClockUserService userService, JdbcUserDetailsManager jdbcUserDetailsManager) {
	       this.userService = userService;
	       this.jdbcUserDetailsManager = jdbcUserDetailsManager;
	    }
	
	@RequestMapping("/signup")
	public String signUp() {
		return "signup";
	}
	
	@RequestMapping( value = "/signup/newUser")
	public String newUser(Model model, @RequestParam("name")String name, @RequestParam("userType")UserType type) {
		TimeClockUser newUser = userService.createUser(name, type);
		UserBuilder user = User.builder()
	 			.username(newUser.getUsername())
	 			.password(new BCryptPasswordEncoder().encode(""));
		if(newUser.getUserType().equals(UserType.ADMIN)) {
			user.roles("USER", "ADMIN");
		} else {
			user.roles("USER");
		}
		jdbcUserDetailsManager.createUser(user.build());
		userService.save(newUser);

		//ModelAndView mv = new ModelAndView("redirect:/signup");
		model.addAttribute("user", newUser);
		return "signup";
	}

}
