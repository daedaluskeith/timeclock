package com.karlkeith.paychex.timeclock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.services.ShiftService;
import com.karlkeith.paychex.timeclock.services.TimeClockUserService;

@Controller
@RequestMapping("/reports")
public class ReportsController {

	@Autowired
	TimeClockUserService timeClockUserService;
	
	@Autowired
	ShiftService shiftService;
	
	@RequestMapping({"", "/"})
	public String getReports() {
		return "reports/reports";
	}
	
	@RequestMapping("/users")
	public String getAllUsers(Model model) {
		model.addAttribute("users", timeClockUserService.findAll());
		return "reports/users";
	}
	
	@RequestMapping({"/user/{username}/shifts", "/user/{username}/shifts/"})
	public String getUserShifts(Model model, @PathVariable(name = "username")String username) {
		TimeClockUser user = timeClockUserService.findByUsername(username);
		model.addAttribute("shifts",shiftService.findByUserid(user));
		return "reports/userShifts";
	}
}
