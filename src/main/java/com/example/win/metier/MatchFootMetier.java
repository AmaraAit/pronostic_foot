package com.example.win.metier;

import java.text.ParseException;
import java.util.List;

import com.example.win.entities.Equipe;
import com.example.win.entities.LeagueStats;
import com.example.win.entities.MatchAbstract;
import com.example.win.entities.MatchFoot;
import com.example.win.entities.MatchStat;

public interface MatchFootMetier {
	
	public List<MatchFoot> getMatchs() throws Exception;
	
	
	
	public void saveAllMatchFoot() throws Exception;
	
	public void saveMatchFoot(MatchFoot matchFoot);
	
	public void updateMatchFoot();
	
	public List<MatchFoot> getListMatchFootByEquipeUn(String nameUn);
	
	public List<MatchFoot> getListMatchFootByEquipeDeux(String nameDeux);
	
	public List<MatchFoot> getListMatchByLeague(String name, String league);

	public List<MatchFoot> getAll();
	
	public List<MatchFoot> getMatchsByName(String name);
	
	public List<MatchFoot> getMatchMarqueByName(String name);
	
	public List<MatchAbstract> getNextMatch() throws ParseException ;
	
	public List<MatchAbstract> setMatch() throws Exception;
	
	public List<MatchFoot> getMatchEncaisseByName(String name);

	public List<MatchFoot> getMatchsLastByName(String name, int code);
	
	public double matchmarqeAdom(List<MatchFoot> l);
	
	public double matchmarqeAext(List<MatchFoot> l);
	
	public double matchencaisseAdom(List<MatchFoot> l);
	
	public double matchencaisseAext(List<MatchFoot> l);
	
	public double matchmarqeAdomMT(List<MatchFoot> l);
	
	public double matchmarqeAextMT(List<MatchFoot> l);
	
	public double matchencaisseAdomMT(List<MatchFoot> l);
	
	public double matchencaisseAextMT(List<MatchFoot> l);
	
	public List<MatchStat> getAllMStat();
	
	public String predict() throws Exception;

	public void saveData();
	
	public LeagueStats getStat(String league,int numberMatch);
	
	public Equipe getStatByEquipe(String Equipe,int numberMatch);
	
	public List<MatchFoot> getM(String name, int num);

	public String predictDeuxEquipeMarque() throws Exception;
	
	
	
	public List<String> getEquipeByLeague(String name);

}
