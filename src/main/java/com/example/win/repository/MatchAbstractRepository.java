package com.example.win.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.win.entities.MatchAbstract;


public interface MatchAbstractRepository extends JpaRepository<MatchAbstract, Long>{
	
	@Query("SELECT m FROM MatchAbstract m WHERE m.date> ?1 ORDER BY date ASC")
	List<MatchAbstract> findAllByDate(Date date);

}
