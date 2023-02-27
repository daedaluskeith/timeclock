package com.karlkeith.paychex.timeclock.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.karlkeith.paychex.timeclock.model.Breaks;

@Repository
public interface BreaksRepository extends JpaRepository<Breaks, Long>{

}
