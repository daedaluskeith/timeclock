package com.karlkeith.paychex.timeclock.services;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;
import com.karlkeith.paychex.timeclock.repositories.TimeClockUserRepository;

@Service
public class TimeClockUserServiceImpl implements TimeClockUserService{

	private final TimeClockUserRepository userRepository;
	
	public TimeClockUserServiceImpl(TimeClockUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void delete(TimeClockUser object) {
		userRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		userRepository.deleteById(id);
	}

	@Override
	public Set<TimeClockUser> findAll() {
		Set<TimeClockUser> users = new HashSet<>();
		userRepository.findAll().forEach(users::add);
		return users;
	}

	@Override
	public TimeClockUser findById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	@Override
	public TimeClockUser save(TimeClockUser object) {
		return userRepository.save(object);
	}

	@Override
	public TimeClockUser findByUsername(String username) {
		return userRepository.findByUsername(username);
	}

	@Override
	public TimeClockUser createUser(String name, UserType type) {
		String uniqueID = UUID.randomUUID().toString();
		return TimeClockUser.builder().name(name).userType(type).username(uniqueID).build();
	}
}
