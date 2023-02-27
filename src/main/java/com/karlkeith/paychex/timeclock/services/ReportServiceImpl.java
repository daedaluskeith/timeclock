package com.karlkeith.paychex.timeclock.services;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.karlkeith.paychex.timeclock.model.Breaks;
import com.karlkeith.paychex.timeclock.model.Shift;

@Service
public class ReportServiceImpl implements ReportService {

	Map<Long, LocalTime> shiftHours = new ConcurrentHashMap<>();

	@Override
	public Map<Long, LocalTime> calculateBreaksByShift(Shift shift) {
		Map<Long, LocalTime> durations = new HashMap<>();
		if (shift.getBreaks().size() != 0) {
			List<Breaks> breaks = shift.getBreaks();
			for (Breaks b : breaks) {
				if(b.getEndtime() != null) {
					Duration breakDuration = Duration.between(b.getStarttime(), b.getEndtime());
					LocalTime t = LocalTime.ofSecondOfDay(breakDuration.getSeconds());
					durations.put(b.getId(), t);
				}
			}
		}
		return durations;
	}

	@Override
	public Map<Long, LocalTime> calculateHoursByShift(List<Shift> shifts) {
		Map<Long, LocalTime> hours = new HashMap<>();
		for (Shift shift : shifts) {
			if (shiftHours.containsKey(shift.getId())) {
				hours.put(shift.getId(), shiftHours.get(shift.getId()));
			} else {
				LocalTime time = calculateShiftLength(shift);
				if (time != null) {
					hours.put(shift.getId(), time);
					shiftHours.put(shift.getId(), time);
				}
			}
		}
		return hours;
	}

	@Override
	public Map<String, LocalTime> calculateHoursByUser(Set<Shift> shifts) {
		Map<String, LocalTime> hours = new HashMap<>();
		for (Shift shift : shifts) {
			LocalTime time;
			if (shiftHours.containsKey(shift.getId())) {
				time = shiftHours.get(shift.getId());
			} else {
				time = calculateShiftLength(shift);
				if (time != null) {
					shiftHours.put(shift.getId(), time);
				}
			}
			if (time != null) {
				LocalTime stored = hours.getOrDefault(shift.getUserid().getUsername(), LocalTime.of(0, 0));
				time = time.plusSeconds(stored.getSecond()).plusMinutes(stored.getMinute()).plusHours(stored.getHour());
				hours.put(shift.getUserid().getUsername(), time);
			}
		}
		return hours;
	}

	public LocalTime calculateShiftLength(Shift shift) {
		LocalDateTime start = shift.getStarttime();
		LocalDateTime end = shift.getEndtime();
		LocalTime t = null;
		if (end != null) {
			Duration totalDuration = Duration.between(start, end);
			totalDuration = totalDuration.abs();

			if (shift.getBreaks().size() != 0) {
				List<Breaks> breaks = shift.getBreaks();
				for (Breaks b : breaks) {
					if(b.getEndtime() != null) {
						Duration breakDuration = Duration.between(b.getStarttime(), b.getEndtime());
						totalDuration = totalDuration.minus(breakDuration);
					}
				}
			}

			t = LocalTime.ofSecondOfDay(totalDuration.getSeconds());
		}
		return t;
	}

}
