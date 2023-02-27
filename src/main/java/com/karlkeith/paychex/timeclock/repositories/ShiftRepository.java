package com.karlkeith.paychex.timeclock.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.karlkeith.paychex.timeclock.model.Shift;
import com.karlkeith.paychex.timeclock.model.TimeClockUser;

@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>{
	
	@Query(value = "select s.* from shifts s where s.userid = :userid order by s.starttime desc limit 1", nativeQuery = true)
	public Optional<Shift> findLatestShift(Long userid);
	
	public List<Shift> findByUserid(TimeClockUser userid);
}
