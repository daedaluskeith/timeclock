package com.karlkeith.paychex.timeclock.services;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.karlkeith.paychex.timeclock.model.BreakType;
import com.karlkeith.paychex.timeclock.model.Shift;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;
import com.karlkeith.paychex.timeclock.repositories.ShiftRepository;

@ExtendWith(MockitoExtension.class)
class ShiftServiceImplTest {

	@Mock
	TimeClockUser user;

	@Mock
	ShiftRepository shiftRepository;
	
	@InjectMocks
	ShiftServiceImpl shiftService;
	
	@BeforeEach
	public void setup() {
		when(shiftRepository.save(any(Shift.class))).then(AdditionalAnswers.returnsFirstArg());
		
		
	}

	@Test
	void simpleShiftServiceTest() {
		when(user.getUserType()).thenReturn(UserType.USER);
		assertFalse(shiftService.hasActiveShift(user));
		assertNotNull(shiftService.startShift(user));
		assertTrue(shiftService.hasActiveShift(user));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}
	
	@Test
	void multipleShiftsTest() {
		when(user.getUserType()).thenReturn(UserType.USER);
		assertFalse(shiftService.hasActiveShift(user));
		assertNotNull(shiftService.startShift(user));
		assertTrue(shiftService.hasActiveShift(user));
		assertNull(shiftService.startShift(user));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}
	
	@Test
	void multipleAdminShiftsTest() {
		when(user.getUserType()).thenReturn(UserType.ADMIN);
		assertFalse(shiftService.hasActiveShift(user));
		assertNotNull(shiftService.startShift(user));
		assertTrue(shiftService.hasActiveShift(user));
		assertNotNull(shiftService.startShift(user));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}
	
	@Test
	void takeBreaksTest() {
		when(user.getUserType()).thenReturn(UserType.USER);
		assertFalse(shiftService.hasActiveShift(user));
		assertFalse(shiftService.endBreak(user));
		assertFalse(shiftService.takeBreak(user, BreakType.BREAK));
		assertNotNull(shiftService.startShift(user));
		assertFalse(shiftService.endBreak(user));
		assertTrue(shiftService.takeBreak(user, BreakType.BREAK));
		assertFalse(shiftService.takeBreak(user, BreakType.BREAK));
		assertTrue(shiftService.endBreak(user));
		assertTrue(shiftService.takeBreak(user, BreakType.LUNCH));
		assertFalse(shiftService.takeBreak(user, BreakType.BREAK));
		assertTrue(shiftService.endBreak(user));
		assertFalse(shiftService.endBreak(user));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}
	
	@Test
	void takeBreaksAdminTest() {
		when(user.getUserType()).thenReturn(UserType.ADMIN);
		assertFalse(shiftService.hasActiveShift(user));
		assertFalse(shiftService.endBreak(user));
		assertFalse(shiftService.takeBreak(user, BreakType.BREAK));
		assertNotNull(shiftService.startShift(user));
		assertFalse(shiftService.endBreak(user));
		assertTrue(shiftService.takeBreak(user, BreakType.BREAK));
		assertTrue(shiftService.takeBreak(user, BreakType.BREAK));
		assertTrue(shiftService.endBreak(user));
		assertTrue(shiftService.takeBreak(user, BreakType.LUNCH));
		assertTrue(shiftService.takeBreak(user, BreakType.BREAK));
		assertTrue(shiftService.endBreak(user));
		assertFalse(shiftService.endBreak(user));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}
	
	@Test
	void endShiftOnBreakTest() {
		when(user.getUserType()).thenReturn(UserType.USER);
		assertNotNull(shiftService.startShift(user));
		assertTrue(shiftService.takeBreak(user, BreakType.BREAK));
		assertNull(shiftService.endShift(user));
		assertTrue(shiftService.hasActiveShift(user));
		assertTrue(shiftService.endBreak(user));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}
	
	@Test
	void endShiftOnBreakAdminTest() {
		when(user.getUserType()).thenReturn(UserType.ADMIN);
		assertNotNull(shiftService.startShift(user));
		assertTrue(shiftService.takeBreak(user, BreakType.BREAK));
		assertNotNull(shiftService.endShift(user));
		assertFalse(shiftService.hasActiveShift(user));
	}

}
