package com.karlkeith.paychex.timeclock.services;

import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;

public interface TimeClockUserService extends CrudService<TimeClockUser, Long>{

	public TimeClockUser findByUsername(String username);
	public TimeClockUser createUser(String name, UserType type);
}
