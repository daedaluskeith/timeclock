package com.karlkeith.paychex.timeclock.services;

import java.util.List;

import com.karlkeith.paychex.timeclock.model.BreakType;
import com.karlkeith.paychex.timeclock.model.Shift;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;

public interface ShiftService extends CrudService<Shift, Long>{
	public boolean endBreak(TimeClockUser user);
	public Shift endShift(TimeClockUser user);
	public List<Shift> findByUserid(TimeClockUser userid);
	public Shift findLatestActiveShift(Long userid);
	public BreakType getCurrentBreakType(TimeClockUser user);
	public boolean hasActiveShift(TimeClockUser user);
	public boolean isOnBreak(TimeClockUser user);
	public Shift startShift(TimeClockUser user);
	public boolean takeBreak(TimeClockUser user, BreakType type);
}
