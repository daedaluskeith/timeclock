package com.karlkeith.paychex.timeclock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.karlkeith.paychex.timeclock.model.Shift;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.services.ReportService;
import com.karlkeith.paychex.timeclock.services.ShiftService;
import com.karlkeith.paychex.timeclock.services.TimeClockUserService;

@Controller
@RequestMapping("/reports")
public class ReportsController {

	@Autowired
	TimeClockUserService timeClockUserService;
	
	@Autowired
	ShiftService shiftService;
	
	@Autowired
	ReportService reportService;
	
	@RequestMapping({"", "/"})
	public String getReports(Model model) {
		model.addAttribute("users", timeClockUserService.findAll());
		model.addAttribute("hours", reportService.calculateHoursByUser(shiftService.findAll()));
		return "reports/reports";
	}
	
	@RequestMapping({"/user/{username}/shifts", "/user/{username}/shifts/"})
	public String getUserShifts(Model model, @PathVariable(name = "username")String username) {
		TimeClockUser user = timeClockUserService.findByUsername(username);
		model.addAttribute("shifts",shiftService.findByUserid(user));
		model.addAttribute("username", username);
		model.addAttribute("hours", reportService.calculateHoursByShift(shiftService.findByUserid(timeClockUserService.findByUsername(username))));
		return "reports/userShifts";
	}
	
	@RequestMapping({"/user/{username}/shift/{id}", "/user/{username}/shift/{id}/"})
	public String getUserShift(Model model, @PathVariable(name = "username")String username,
			@PathVariable(name = "id") Long id) {
		Shift shift = shiftService.findById(id);
		model.addAttribute("shift",shift);
		model.addAttribute("username", username);
		model.addAttribute("totalTime", reportService.calculateShiftLength(shift));
		model.addAttribute("durations", reportService.calculateBreaksByShift(shift));
		return "reports/shiftDetails";
	}
}
