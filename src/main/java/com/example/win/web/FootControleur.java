package com.example.win.web;

import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.win.entities.Equipe;
import com.example.win.entities.LeagueStats;
import com.example.win.entities.MatchAbstract;
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
	
	@RequestMapping(value = "/allMstat",method = RequestMethod.GET)
	public List<MatchStat> getAllMstat(){
		return footMetier.getAllMStat();
	}
	
	@RequestMapping(value = "/predict",method = RequestMethod.GET)
	public String getPrediction() throws Exception{
		return footMetier.predict();
	}
	
	@RequestMapping(value = "/predictDeuxEquipeMarque",method = RequestMethod.GET)
	public String getPredictionDeuxEquipeMarque() throws Exception{
		return footMetier.predictDeuxEquipeMarque();
	}
	@RequestMapping(value = "/predictMiTempsUnProlifique",method = RequestMethod.GET)
	public String getPredictionMt1Prolifique() throws Exception{
		return footMetier.predictMiTempsUnProlifique();
	}
	@RequestMapping(value = "/predictMiTempsDeuxProlifique",method = RequestMethod.GET)
	public String getPredictionMt2Prolifique() throws Exception{
		return footMetier.predictMiTempsDeuxProlifique();
	}
	@RequestMapping(value = "/predictMatchNullMiTemps",method = RequestMethod.GET)
	public String getPredictionMatchNullMitemps() throws Exception{
		return footMetier.predictMAtchNullMitemps();
	}
	@RequestMapping(value = "/predictPlusDeuxBut",method = RequestMethod.GET)
	public String getPredictionPlusDeuxBut() throws Exception{
		return footMetier.predictPlusDEuxBut();
	}
	@RequestMapping(value = "/saveData",method = RequestMethod.GET)
	public String savedata() {
		footMetier.saveData();
		return "save done ..";
	}
	@RequestMapping(value = "/getstat/{league}/{number}",method = RequestMethod.GET)
	public LeagueStats getStat(@PathVariable String league,@PathVariable int number) {
		
		return footMetier.getStat(league,number);
	}
	
	@RequestMapping(value = "/Equipe/Statistic/{equipe}/{number}",method = RequestMethod.GET)
	public Equipe getStatEquipe(@PathVariable String equipe,@PathVariable int number) {
		
		return footMetier.getStatByEquipe(equipe,number);
	}
	@CrossOrigin(value = "http://localhost:3000")
	@RequestMapping(value = "/Equipe/{name}/{number}",method = RequestMethod.GET)
	public List<MatchFoot> getEquipe(@PathVariable String name,@PathVariable int number) {
		
		return footMetier.getM(name,number);
	}
	
	@CrossOrigin(value = "http://localhost:3000")
	@RequestMapping(value = "/League/{league}",method = RequestMethod.GET)
	public List<String> getEquipeByLeague(@PathVariable String league) {
		
		return footMetier.getEquipeByLeague(league);
	}
	@CrossOrigin(value = "http://localhost:3000")
	@RequestMapping(value = "/setMatch",method = RequestMethod.GET)
	public List<MatchAbstract> setMatch() throws Exception {
		
		return footMetier.setMatch();
	}
	@CrossOrigin(value = "http://localhost:3000")
	@RequestMapping(value = "/nextMatch",method = RequestMethod.GET)
	public List<MatchAbstract> nextMatch() throws Exception {
		
		return footMetier.getNextMatch();
	}
	
	
	

}
