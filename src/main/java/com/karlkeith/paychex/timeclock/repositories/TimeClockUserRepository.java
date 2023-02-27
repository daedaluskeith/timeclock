package com.karlkeith.paychex.timeclock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karlkeith.paychex.timeclock.model.TimeClockUser;

@Repository
public interface TimeClockUserRepository extends JpaRepository<TimeClockUser, Long>{

	public TimeClockUser findByUsername(String username);
}
