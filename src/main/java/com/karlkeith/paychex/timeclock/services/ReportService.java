package com.karlkeith.paychex.timeclock.services;

import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.karlkeith.paychex.timeclock.model.Shift;

public interface ReportService {
	public Map<Long, LocalTime> calculateBreaksByShift(Shift shift);
	public Map<Long, LocalTime> calculateHoursByShift(List<Shift> shifts);
	public Map<String, LocalTime> calculateHoursByUser(Set<Shift> shifts);
	public LocalTime calculateShiftLength(Shift shift);
}
