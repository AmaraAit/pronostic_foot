package com.example.win.web;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.win.entities.MatchFoot;
import com.example.win.entities.MatchStat;
import com.example.win.metier.MatchFootMetier;

@RestController
public class FootControleur {
	MatchFootMetier footMetier;

	public FootControleur(MatchFootMetier footMetier) {
		
		this.footMetier = footMetier;
	}
	
	@RequestMapping(value = "/InsertAllMatchFootByAuthentification",method = RequestMethod.GET)
	public List<MatchFoot> getMatchs() throws Exception{
		return footMetier.getMatchs();
		
	}
	
	@RequestMapping(value = "/saveAllMatchDONOT",method = RequestMethod.GET)
	public void saveAllMatchs() throws Exception {
	 footMetier.saveAllMatchFoot();
		
	}
	@RequestMapping(value = "/",method = RequestMethod.GET)
	public List<MatchFoot> getAllMatchs(){
		return footMetier.getAll();
	}
	
	@RequestMapping(value = "/{name}",method = RequestMethod.GET)
	public List<MatchFoot> getAllMatchs(@PathVariable String name){
		return null;//footMetier.getMatchsByName(name);
	}
	
	@RequestMapping(value = "/{name}/total",method = RequestMethod.GET)
	public int getAllMatchsTotal(@PathVariable String name){
		return footMetier.getMatchsByName(name).size();
	}
	
	@RequestMapping(value = "/Marque/{name}",method = RequestMethod.GET)
	public List<MatchFoot> getAllMatchsMarque(@PathVariable String name){
		return footMetier.getMatchMarqueByName(name);
	}
	
	@RequestMapping(value = "/Encaisse/{name}",method = RequestMethod.GET)
	public List<MatchFoot> getAllMatchsEncaisse(@PathVariable String name){
		return footMetier.getMatchEncaisseByName(name);
	}
	
	@RequestMapping(value = "/Marque/{name}/number",method = RequestMethod.GET)
	public int getAllMatchsMarqueNumber(@PathVariable String name){
		return footMetier.getMatchMarqueByName(name).size();
	}
	
	@RequestMapping(value = "/Encaisse/{name}/number",method = RequestMethod.GET)
	public int getAllMatchsEncaisseNumber(@PathVariable String name){
		return footMetier.getMatchEncaisseByName(name).size();
	}
	
	@RequestMapping(value = "/allMstatAyyyy?iuiuiu",method = RequestMethod.GET)
	public List<MatchStat> getAllMstat(){
		return footMetier.getAllMStat();
	}
	
	@RequestMapping(value = "/predict",method = RequestMethod.GET)
	public String getPrediction() throws Exception{
		return footMetier.predict();
	}
	
	@RequestMapping(value = "/predict1",method = RequestMethod.GET)
	public String getPrediction1() throws Exception{
		return footMetier.predict1();
	}
	

}
