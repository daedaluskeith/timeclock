package com.karlkeith.paychex.timeclock.bootstrap;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.stereotype.Component;

import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;
import com.karlkeith.paychex.timeclock.services.TimeClockUserService;

@Component
public class DataLoader implements CommandLineRunner {

	private final TimeClockUserService userService;
	private final JdbcUserDetailsManager jdbcUserDetailsManager;

	public DataLoader(TimeClockUserService userService, JdbcUserDetailsManager jdbcUserDetailsManager) {
		this.userService = userService;
		this.jdbcUserDetailsManager = jdbcUserDetailsManager;
	}

	@Override
	public void run(String... args) throws Exception {
		if (userService.findAll().size() == 0) {
			loadUsers();
		}

	}

	private void loadUsers() {
		TimeClockUser timeClockUser = TimeClockUser.builder().name("TestUser").username("user").userType(UserType.USER)
				.build();
		userService.save(timeClockUser);

		TimeClockUser timeClockAdmin = TimeClockUser.builder().name("TestAdmin").username("admin")
				.userType(UserType.ADMIN).build();
		userService.save(timeClockAdmin);

		UserDetails user = User.builder().username("user").password(new BCryptPasswordEncoder().encode(""))
				.roles("USER").build();
		UserDetails admin = User.builder().username("admin").password(new BCryptPasswordEncoder().encode(""))
				.roles("ADMIN", "USER").build();
		jdbcUserDetailsManager.createUser(user);
		jdbcUserDetailsManager.createUser(admin);
	}

}
