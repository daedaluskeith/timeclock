package com.karlkeith.paychex.timeclock.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

class ShiftTest {

	static TimeClockUser user;
	
	@BeforeAll
	static void setUpBeforeClass(){
		user = Mockito.mock(TimeClockUser.class);
		when(user.getId()).thenReturn(1L);
		when(user.getName()).thenReturn("TestUser");
		when(user.getUsername()).thenReturn("12345");
		when(user.getUserType()).thenReturn(UserType.USER);
	}
	
	@Test
	void simpleShiftTest() {
		Shift shift = new Shift(user);
		assertNotNull(shift.getStarttime());
		assertNull(shift.getEndtime());
		shift.endShift();
		assertNotNull(shift.getEndtime());
	}

	@Test
	void simpleBreakTest() {
		Shift shift = new Shift(user);
		assertEquals(0, shift.getBreaks().size());
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.BREAK);
		assertTrue(shift.isOnBreak());
		assertEquals(1, shift.getBreaks().size());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		assertEquals(1, shift.getBreaks().size());
		shift.endShift();
	}
	
	@Test
	void simpleLunchTest() {
		Shift shift = new Shift(user);
		assertEquals(0, shift.getBreaks().size());
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.LUNCH);
		assertTrue(shift.isOnBreak());
		assertEquals(1, shift.getBreaks().size());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		assertEquals(1, shift.getBreaks().size());
		shift.endShift();
	}
	
	@Test
	void multipleBreaksTest() {
		Shift shift = new Shift(user);
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.BREAK);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.BREAK);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.BREAK);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.endShift();
	}
	
	@Test
	void multipleLunchesTest() {
		Shift shift = new Shift(user);
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.LUNCH);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.LUNCH);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.LUNCH);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.endShift();
	}
	
	@Test
	void takeLunchOnBreakTest() {
		Shift shift = new Shift(user);
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.BREAK);
		assertTrue(shift.isOnBreak());
		shift.takeBreak(BreakType.LUNCH);
		assertTrue(shift.isOnBreak());
		shift.endBreak();
		assertFalse(shift.isOnBreak());
		shift.endShift();
	}
	
	@Test
	void quitingDuringBreakTest() {
		Shift shift = new Shift(user);
		assertFalse(shift.isOnBreak());
		shift.takeBreak(BreakType.BREAK);
		assertTrue(shift.isOnBreak());
		shift.endShift();
		assertFalse(shift.isOnBreak());
	}
	
}
