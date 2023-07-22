package com.example.win.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.example.win.entities.LeagueStats;
import com.example.win.entities.MatchFoot;
@RepositoryRestResource
public interface MatchFootRepository extends JpaRepository<MatchFoot, Long>{
	
	@Query(value="SELECT m FROM MatchFoot m WHERE m.nameUn LIKE ?1 or m.nameDeux LIKE ?1  ORDER BY code DESC ")
	List<MatchFoot> findAllMatchByNameUn(String name);
	
	@Query(value="SELECT m FROM MatchFoot m WHERE m.id < ?2 and  m.nameUn LIKE ?1   ORDER BY code DESC ")
	List<MatchFoot> findAllLastMatchByNameUn(String name , long code);
	
	@Query(value="SELECT m FROM MatchFoot m WHERE m.id < ?2 and   m.nameDeux LIKE ?1   ORDER BY code DESC ")
	List<MatchFoot> findAllLastMatchByNameDeux(String name , long code);
	
	@Query("SELECT m FROM MatchFoot m WHERE m.nameUn LIKE ?1 and m.butEqUnMTUn+butEqUnMTDeux > 0 ORDER BY code DESC")
	List<MatchFoot> findAllMatchMarqueDomByEquipe(String name);
	
	@Query("SELECT m FROM MatchFoot m WHERE m.nameDeux LIKE ?1 and m.butEqDeuxMTUn+butEqDeuxMTDeux > 0 ORDER BY code DESC")
	List<MatchFoot> findAllMatchMarqueExtByEquipe(String name);
	
	@Query("SELECT m FROM MatchFoot m WHERE m.nameUn LIKE ?1 and m.butEqDeuxMTUn+butEqDeuxMTDeux > 0 ORDER BY code DESC")
	List<MatchFoot> findAllMatchEncaisseDomByEquipe(String name);
	
	@Query("SELECT m FROM MatchFoot m WHERE m.nameDeux LIKE ?1 and m.butEqUnMTUn+butEqUnMTDeux > 0 ORDER BY code DESC")
	List<MatchFoot> findAllMatchEncaisseExtByEquipe(String name);
	
	@Query("SELECT m FROM MatchFoot m WHERE m.league LIKE ?1  ORDER BY id DESC")
	List<MatchFoot> getStatByLeague(String name);
	
	@Query("SELECT m FROM MatchFoot m WHERE m.nameUn LIKE ?1 or m.nameDeux LIKE ?1  ORDER BY id DESC")
	List<MatchFoot> getStatByEquipe(String name);
}
