package com.karlkeith.paychex.timeclock.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.karlkeith.paychex.timeclock.model.BreakType;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;
import com.karlkeith.paychex.timeclock.model.UserType;
import com.karlkeith.paychex.timeclock.services.ShiftService;
import com.karlkeith.paychex.timeclock.services.TimeClockUserService;

@Controller
public class indexController {

	@Autowired
	TimeClockUserService timeClockUserService;
	@Autowired
	ShiftService shiftService;
	
	@RequestMapping({"", "/", "/index", "/index.html"})
	public String getLandingPage(Model model, Authentication authentication) {
		TimeClockUser user = getCurrentUser(authentication.getName());
		model.addAttribute("name", user.getName());
		if(UserType.ADMIN.equals(user.getUserType())) {
			model.addAttribute("admin", "admin");
		}
		if(shiftService.hasActiveShift(user)) {
			if(shiftService.isOnBreak(user)) {
				switch(shiftService.getCurrentBreakType(user)) {
				case BREAK:
					model.addAttribute("onbreak", "onbreak");
					break;
				case LUNCH:
					model.addAttribute("atlunch", "atlunch");
					break;
				default:
					//something went wrong here
						break;
				}
			}
			else {
				model.addAttribute("onShift", "onShift");
			}
		} else {
			model.addAttribute("start", "start");
		}
		return "index";
	}
	
	@RequestMapping("/clockin")
	public ModelAndView clockIn(Model model, Authentication authentication) {
		shiftService.startShift(getCurrentUser(authentication.getName()));
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/clockout")
	public ModelAndView clockOut(Model model, Authentication authentication) {
		shiftService.endShift(getCurrentUser(authentication.getName()));
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/takebreak")
	public ModelAndView takeBreak(Model model, Authentication authentication) {
		shiftService.takeBreak(getCurrentUser(authentication.getName()), BreakType.BREAK);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/takelunch")
	public ModelAndView takeLunch(Model model, Authentication authentication) {
		shiftService.takeBreak(getCurrentUser(authentication.getName()), BreakType.LUNCH);
		return new ModelAndView("redirect:/");
	}
	
	@RequestMapping("/endbreak")
	public ModelAndView endBreak(Model model, Authentication authentication) {
		shiftService.endBreak(getCurrentUser(authentication.getName()));
		return new ModelAndView("redirect:/");
	}
	
	
	private TimeClockUser getCurrentUser(String name) {
		return timeClockUserService.findByUsername(name);
	}
}
