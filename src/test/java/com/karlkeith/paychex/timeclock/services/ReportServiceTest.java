package com.karlkeith.paychex.timeclock.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.Duration;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.karlkeith.paychex.timeclock.model.BreakType;
import com.karlkeith.paychex.timeclock.model.Breaks;
import com.karlkeith.paychex.timeclock.model.Shift;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;

@ExtendWith(MockitoExtension.class)
class ReportServiceTest {

	@InjectMocks
	ReportServiceImpl reportService;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@Test
	void test() {
		Shift s1 = new Shift(Mockito.mock(TimeClockUser.class));
		s1.setEndtime(s1.getStarttime().plusMinutes(10));
		s1.setId(1L);
		Shift s2 = new Shift(Mockito.mock(TimeClockUser.class));
		s2.setEndtime(s2.getStarttime().plusHours(3).plusMinutes(10).plusSeconds(14));
		s2.setId(2L);
		Shift s3 = new Shift(Mockito.mock(TimeClockUser.class));
		s3.setEndtime(s3.getStarttime().plusHours(4).plusSeconds(14));
		s3.setId(3L);
		Breaks b1 = new Breaks(s3, BreakType.BREAK);
		b1.setEndtime(b1.getStarttime().plusMinutes(10));
		List<Breaks> bList = new ArrayList<>();
		bList.add(b1);
		s3.setBreaks(bList);
		
		List<Shift> shifts = new ArrayList<>();
		shifts.add(s1);
		shifts.add(s2);
		shifts.add(s3);
		Map<Long, LocalTime> hours = reportService.calculateHoursByShift(shifts);
		
		LocalTime t1 = LocalTime.ofSecondOfDay(Duration.ofMinutes(10).getSeconds());
		LocalTime t2 = LocalTime.ofSecondOfDay(Duration.ofHours(3).plusMinutes(10).plusSeconds(14).getSeconds());
		assertEquals(t1,hours.get(s1.getId()));
		assertEquals(t2,hours.get(s2.getId()));
	}

}
