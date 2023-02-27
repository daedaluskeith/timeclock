package com.karlkeith.paychex.timeclock.services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.karlkeith.paychex.timeclock.model.BreakType;
import com.karlkeith.paychex.timeclock.model.Shift;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;
import com.karlkeith.paychex.timeclock.repositories.ShiftRepository;

@Service
public class ShiftServiceImpl implements ShiftService {

	//stores a cache of active shifts
	private Map<String, Shift> activeShifts = new HashMap<>();
	
	//optimization to not over check the DB for active shifts
	private Set<String> checkedStatus = new HashSet<>();
	private final ShiftRepository shiftRepository;

	public ShiftServiceImpl(ShiftRepository shiftRepository) {
		this.shiftRepository = shiftRepository;
	}

	@Override
	public void delete(Shift object) {
		shiftRepository.delete(object);
	}

	@Override
	public void deleteById(Long id) {
		shiftRepository.deleteById(id);
	}

	public boolean endBreak(TimeClockUser user) {
		if (isOnBreak(user)) {
			Shift shift = activeShifts.get(user.getUsername());
			shift.endBreak();
			shift = save(shift);
			activeShifts.put(user.getUsername(), shift);
			return true;
		}
		return false;
	}

	public Shift endShift(TimeClockUser user) {
		if (hasActiveShift(user)) {
			Shift shift = activeShifts.get(user.getUsername());
			if (UserType.ADMIN.equals(user.getUserType()) || !isOnBreak(user)) {
				shift.endShift();
				save(shift);
				activeShifts.remove(user.getUsername());
				return shift;
			}
		}
		return null;
	}

	@Override
	public Set<Shift> findAll() {
		Set<Shift> shifts = new HashSet<>();
		shiftRepository.findAll().forEach(shifts::add);
		return shifts;
	}

	@Override
	public Shift findById(Long id) {
		return shiftRepository.findById(id).orElse(null);
	}

	@Override
	public BreakType getCurrentBreakType(TimeClockUser user) {
		if (hasActiveShift(user)) {
			return activeShifts.get(user.getUsername()).getCurrentBreakType();
		}
		return null;
	}

	public boolean hasActiveShift(TimeClockUser user) {
		if (activeShifts.containsKey(user.getUsername())) {
			return true;
		} else {
			if (!checkedStatus.contains(user.getUsername())) {
				checkedStatus.add(user.getUsername());
				Shift shift = findLatestActiveShift(user.getId());
				if (shift != null) {
					if(shift.getEndtime() == null) {
					activeShifts.put(user.getUsername(), shift);
					return true;
					}
				}
			}
		}
		return false;
	}

	public boolean isOnBreak(TimeClockUser user) {
		if (hasActiveShift(user)) {
			return activeShifts.get(user.getUsername()).isOnBreak();
		}
		return false;
	}

	@Override
	public Shift save(Shift object) {
		return shiftRepository.save(object);
	}

	public Shift startShift(TimeClockUser user) {
		if (UserType.ADMIN.equals(user.getUserType())
				|| UserType.USER.equals(user.getUserType()) && !hasActiveShift(user)) {
			Shift newShift = new Shift(user);
			newShift = save(newShift);
			activeShifts.put(user.getUsername(), newShift);
			return newShift;
		}
		return null;
	}

	public boolean takeBreak(TimeClockUser user, BreakType type) {
		if (UserType.ADMIN.equals(user.getUserType()) || !isOnBreak(user) && hasActiveShift(user)) {
			Shift shift = activeShifts.get(user.getUsername());
			if (shift != null) {
				shift.takeBreak(type);
				shift = save(shift);
				activeShifts.put(user.getUsername(), shift);
				return true;
			}
		}
		return false;
	}

	@Override
	public Shift findLatestActiveShift(Long userid) {
		return shiftRepository.findLatestShift(userid).orElse(null);
	}

	@Override
	public List<Shift> findByUserid(TimeClockUser userid) {
		return shiftRepository.findByUserid(userid);
	}

}
