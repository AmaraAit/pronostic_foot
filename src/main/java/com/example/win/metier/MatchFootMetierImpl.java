package com.example.win.metier;


import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.datavec.api.records.reader.RecordReader;
import org.datavec.api.records.reader.impl.csv.CSVRecordReader;
import org.datavec.api.split.FileSplit;
import org.deeplearning4j.datasets.datavec.RecordReaderDataSetIterator;
import org.deeplearning4j.nn.api.OptimizationAlgorithm;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.DenseLayer;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.joda.time.DateTime;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.nd4j.evaluation.classification.Evaluation;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.dataset.api.iterator.DataSetIterator;
import org.nd4j.linalg.io.ClassPathResource;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction;
import org.springframework.stereotype.Service;

import com.example.win.entities.DataMatchs;
import com.example.win.entities.Equipe;
import com.example.win.entities.LeagueStats;
import com.example.win.entities.MatchAbstract;
import com.example.win.entities.MatchFoot;
import com.example.win.entities.MatchStat;
import com.example.win.repository.DataMatchRepository;
import com.example.win.repository.MatchAbstractRepository;
import com.example.win.repository.MatchFootRepository;
import com.example.win.repository.MatchStatRepository;

@Service
public class MatchFootMetierImpl implements MatchFootMetier{
	
	MatchFootRepository footRepository;
	
	MatchStatRepository statRepository;
	
	DataMatchRepository dataMatchRepository;
	
	MatchAbstractRepository abstractRepository;
	
	
	public MatchFootMetierImpl(MatchFootRepository footRepository, MatchStatRepository statRepository,
			DataMatchRepository dataMatchRepository, MatchAbstractRepository abstractRepository) {
		
		this.footRepository = footRepository;
		this.statRepository = statRepository;
		this.dataMatchRepository = dataMatchRepository;
		this.abstractRepository = abstractRepository;
	}
	
	@Override
	public List<MatchFoot> getMatchs() throws Exception{
		int code=1;
		String [] championnat= {"portugal","belgique","premier-league","liga","serie-a",
				"bundesliga","pays-bas","turquie",
				"suisse","ligue-1"};
		
		String [] championnat1= {"portugal","belgique","liga","serie-a","turquie","ligue-1"};
		String[] ann={"2021-2022.htm","2022-2023.htm"};
		int[] jo= {299,275,343,343,315,343};
		String[] urls1= {
				"https://www.maxifoot.fr/calendrier-portugal-",
				"https://www.maxifoot.fr/calendrier-belgique-",
				"https://www.maxifoot.fr/calendrier-liga-espagne-",
				"https://www.maxifoot.fr/calendrier-serie-a-italie-",
				"https://www.maxifoot.fr/calendrier-turquie-",
				"https://www.maxifoot.fr/calendrier-ligue-1-france-"};
		
		List<String> listlien=new ArrayList<>();
		String[] annee= {"2012-2013.htm","2013-2014.htm","2014-2015.htm","2015-2016.htm","2016-2017.htm","2017-2018.htm","2018-2019.htm","2019-2020.htm","2020-2021.htm"};
		
		
		
		
		String []urls= {
				"https://www.maxifoot.fr/calendrier-portugal-",
				"https://www.maxifoot.fr/calendrier-belgique-",
				"https://www.maxifoot.fr/calendrier-premier-league-angleterre-",
				"https://www.maxifoot.fr/calendrier-liga-espagne-",
				"https://www.maxifoot.fr/calendrier-serie-a-italie-",
				"https://www.maxifoot.fr/calendrier-bundesliga-allemagne-",
				"https://www.maxifoot.fr/calendrier-pays-bas-",
				"https://www.maxifoot.fr/calendrier-turquie-",
				"https://www.maxifoot.fr/calendrier-suisse-",
				"https://www.maxifoot.fr/calendrier-ligue-1-france-"};
		List<MatchFoot> listMatchsFoot=new ArrayList<>();
		int j=0;
		String league="";
		for (int i = 0; i < urls.length; i++) {
			league=championnat1[i];
			j=jo[i];
			//for (String string : ann) {
				
				
				Document doc = Jsoup.connect(urls1[i]+"2013-2014.htm").get();
				String equipe1=null;
				Date dateMatch;
				int butEquipe1MT1 = 0;
				int butEquipe1MT2;
				String equipe2;
				int butEquipe2MT1;
				int butEquipe2MT2;
				Elements newsHeadlines = doc.select("div.cald1");
				
				for (Element headline : newsHeadlines.select("table.cd1")) {
						for (Element headlines : headline.select("tr")) {
							List<Element> children = headlines.children();
							
							if(children.size()>1) {
								MatchFoot mf=new MatchFoot();
								equipe1=children.get(0).text();
								equipe2=children.get(1).text();
								if(!(children.get(2).text().contains("rep") )&&(!(children.get(2).text().equals("-")))&&(children.get(2).text().contains("-") )) {
									Element e=children.get(2);
									String urll=e.select("a").attr("abs:href");
									String[] butMt1=null;
									String[] butMt2=null;
									if(urll!="") {
										Document scoremt = Jsoup.connect(urll).get();
										Elements sco = scoremt.select("center");
										String res=sco.get(1).text();
										if(!res.contains("(mi-tps:")) {
											res="(mi-tps: 0-0)";
										}
										res = res.replace("(mi-tps: ","");
										res = res.replace(")","");
										butMt1=res.split("-");
										butMt2=children.get(2).text().split("-");
										butEquipe1MT1=Integer.parseInt(butMt1[0]);
										butEquipe2MT1=Integer.parseInt(butMt1[1]);
										butEquipe1MT2=Integer.parseInt(butMt2[0])-butEquipe1MT1;
										butEquipe2MT2=Integer.parseInt(butMt2[1])-butEquipe2MT1;
									}else {
										urll=e.select("th").text();
										butMt1=urll.split("-");
										butEquipe1MT1=-1;
										butEquipe2MT1=-1;
										butEquipe1MT2=Integer.parseInt(butMt1[0]);
										butEquipe2MT2=Integer.parseInt(butMt1[1]);
									}
							}else {
								butEquipe1MT1=-1;
								butEquipe2MT1=-1;
								butEquipe1MT2=-1;
								butEquipe2MT2=-1;
							}
							mf.setNameUn(equipe1);
							mf.setNameDeux(equipe2);
							mf.setJour(j);
							mf.setCode(code);
							mf.setLeague(league);
							mf.setButEqUnMTUn(butEquipe1MT1);
							mf.setButEqUnMTDeux(butEquipe1MT2);
							mf.setButEqDeuxMTUn(butEquipe2MT1);
							mf.setButEqDeuxMTDeux(butEquipe2MT2);
							footRepository.save(mf);
							listMatchsFoot.add(mf);
							System.out.println(mf);
							
							code+=1;
						}
					}
					j+=1;
				}
			//}
		}
		
		return listMatchsFoot;
		
	}
	@Override
	public List<MatchAbstract> setMatch() throws Exception{
	int code=1;
	String [] championnat= {"portugal","belgique","premier-league","liga","serie-a",
			"bundesliga","pays-bas",//"turquie",
			"suisse","ligue-1"};
	
	
	
	
	
	List<String> listlien=new ArrayList<>();
	String[] annee= {"2012-2013.htm","2013-2014.htm","2014-2015.htm","2015-2016.htm","2016-2017.htm","2017-2018.htm","2018-2019.htm","2019-2020.htm","2020-2021.htm"};
	
	
	
	
	String []urls= {
			"https://www.maxifoot.fr/calendrier-portugal-",
			"https://www.maxifoot.fr/calendrier-belgique-",
			"https://www.maxifoot.fr/calendrier-premier-league-angleterre-",
			"https://www.maxifoot.fr/calendrier-liga-espagne-",
			"https://www.maxifoot.fr/calendrier-serie-a-italie-",
			"https://www.maxifoot.fr/calendrier-bundesliga-allemagne-",
			"https://www.maxifoot.fr/calendrier-pays-bas-",
			//"https://www.maxifoot.fr/calendrier-turquie-",
			"https://www.maxifoot.fr/calendrier-suisse-",
			"https://www.maxifoot.fr/calendrier-ligue-1-france-"};
	List<MatchAbstract> listMatchsFoot=new ArrayList<>();
	int j=0;
	String league="";
	for (int i = 0; i < urls.length; i++) {
		league=championnat[i];
			Document doc = Jsoup.connect(urls[i]+"2023-2024.htm").get();
			String equipe1=null;
			Date dateMatch=null;
			int butEquipe1MT1 = 0;
			int butEquipe1MT2;
			String equipe2;
			int butEquipe2MT1;
			int butEquipe2MT2;
			Elements newsHeadlines = doc.select("div.cald1");
			
			for (Element headline : newsHeadlines.select("table.cd1")) {
					String date="";
					
					for (Element headlines : headline.select("tr")) {
						date=headline.select("tr.ch3").text();
						List<Element> children = headlines.children();
						
						if(children.size()>1) {
							MatchFoot mf=new MatchFoot();
							MatchAbstract m=new MatchAbstract();
							equipe1=children.get(0).text();
							equipe2=children.get(1).text();
							Element e=children.get(2);
							
							if(e.text().contains("h")) {
								date=e.text()+date;
								String[] ind=date.split(" ");
								date=ind[4];
							}else if(e.text().contains("-")) {
								String[] ind=date.split(" ");
								date=ind[4];
							}else if(e.text().contains("/")) {
								date=e.text()+"/2023";
							}else {
								String[] ind=date.split(" ");
								date=ind[4];
							}
							
							if(!(children.get(2).text().contains("rep") )&&(!(children.get(2).text().equals("-")))&&(children.get(2).text().contains("-") )) {
								//System.out.println(e.text()+"///////////");
								String urll=e.select("a").attr("abs:href");
								String[] butMt1=null;
								String[] butMt2=null;
								if(urll!="") {
									Document scoremt = Jsoup.connect(urll).get();
									Elements sco = scoremt.select("center");
									String res=sco.get(1).text();
									if(!res.contains("(mi-tps:")) {
										res="(mi-tps: 0-0)";
									}
									res = res.replace("(mi-tps: ","");
									res = res.replace(")","");
									butMt1=res.split("-");
									butMt2=children.get(2).text().split("-");
									butEquipe1MT1=Integer.parseInt(butMt1[0]);
									butEquipe2MT1=Integer.parseInt(butMt1[1]);
									butEquipe1MT2=Integer.parseInt(butMt2[0])-butEquipe1MT1;
									butEquipe2MT2=Integer.parseInt(butMt2[1])-butEquipe2MT1;
								}else {
									urll=e.select("th").text();
									butMt1=urll.split("-");
									butEquipe1MT1=-1;
									butEquipe2MT1=-1;
									butEquipe1MT2=Integer.parseInt(butMt1[0]);
									butEquipe2MT2=Integer.parseInt(butMt1[1]);
								}
								mf.setNameUn(equipe1);
								mf.setNameDeux(equipe2);
								mf.setJour(j);
								mf.setCode(code);
								mf.setLeague(league);
								mf.setButEqUnMTUn(butEquipe1MT1);
								mf.setButEqUnMTDeux(butEquipe1MT2);
								mf.setButEqDeuxMTUn(butEquipe2MT1);
								mf.setButEqDeuxMTDeux(butEquipe2MT2);
								footRepository.save(mf);
								
								
						}else {
							 e=children.get(2);
							
							
							butEquipe1MT1=-1;
							butEquipe2MT1=-1;
							butEquipe1MT2=-1;
							butEquipe2MT2=-1;
							m.setNameUn(equipe1);
							m.setNameDeux(equipe2);
							m.setJour(j);
							m.setCode(code);
							m.setLeague(league);
							m.setDate(new SimpleDateFormat("dd/MM/yyyy").parse(date));
							abstractRepository.save(m);
							
						}
						
						
						
						code+=1;
					}
				}
				j+=1;
			}
		//}
	}
	listMatchsFoot=getNextMatch();
	return listMatchsFoot;
	
}
	public List<MatchFoot> getAll(){
		List<MatchFoot> l=footRepository.findAll();
		List<MatchFoot> lin1=new ArrayList<>();
		List<MatchFoot> lout1=new ArrayList<>();
		List<MatchFoot> lin2=new ArrayList<>();
		List<MatchFoot> lout2=new ArrayList<>();
		MatchStat m=new MatchStat();
		
		for (MatchFoot matchFoot : l) {
			
			if(matchFoot.getJour()>10) {
				lin1=footRepository.findAllLastMatchByNameUn(matchFoot.getNameUn(), matchFoot.getId());
				m=getinf1(lin1, m);
				lout1=footRepository.findAllLastMatchByNameDeux(matchFoot.getNameUn(), matchFoot.getId());
				m=getinf2(lout1, m);
				lin2=footRepository.findAllLastMatchByNameUn(matchFoot.getNameDeux(), matchFoot.getId());
				m=getinf1(lin2, m);
				lout2=footRepository.findAllLastMatchByNameDeux(matchFoot.getNameDeux(), matchFoot.getId());
				m=getinf2(lout2, m);
				System.out.println(lin1.size());
				
				break;
			}
		
			
		}
		return footRepository.findAll();
		
	}
	
	public MatchStat getinf1(List<MatchFoot> l,MatchStat m) {
		int matchMarque=0;
		int matchEncaisse=0;
		int matchMarqueMT=0;
		int matchEncaisseMT=0;
		for (MatchFoot matchFoot : l) {
			if(matchFoot.getButEqDeuxMTDeux()+matchFoot.getButEqDeuxMTUn()>0) {
				matchEncaisse+=1;
			}
			if(matchFoot.getButEqUnMTDeux()+matchFoot.getButEqUnMTUn()>0) {
				matchMarque+=1;
			}
			if(matchFoot.getButEqDeuxMTUn()>0) {
				matchEncaisseMT+=1;
			}
			if(matchFoot.getButEqUnMTUn()>0) {
				matchMarqueMT+=1;
			}
				
		}
		/*
		 * m.setNbrMatchMarqueByEquipeUn(matchMarque);
		 * m.setNbrMatchEncaisseByEquipeUn(matchEncaisse);
		 * m.setNbrMatchMarqueMTByEquipeUn(matchMarqueMT);
		 * m.setNbrMatchEncaisseMTByEquipeUn(matchEncaisseMT);
		 */
		return m;
	}
	public MatchStat getinf2(List<MatchFoot> l,MatchStat m) {
		int matchMarque=0;
		int matchEncaisse=0;
		int matchMarqueMT=0;
		int matchEncaisseMT=0;
		for (MatchFoot matchFoot : l) {
				if(matchFoot.getButEqDeuxMTDeux()+matchFoot.getButEqDeuxMTUn()>0) {
					matchMarque+=1;
				}
				if(matchFoot.getButEqUnMTDeux()+matchFoot.getButEqUnMTUn()>0) {
					matchEncaisse+=1;
				}
				if(matchFoot.getButEqDeuxMTUn()>0) {
					matchMarqueMT+=1;
				}
				if(matchFoot.getButEqUnMTUn()>0) {
					matchEncaisseMT+=1;
				}
				
		}
		/*
		 * m.setNbrMatchMarqueByEquipeUn(matchMarque+m.getNbrMatchMarqueByEquipeUn());
		 * m.setNbrMatchEncaisseByEquipeUn(matchEncaisse+m.getNbrMatchEncaisseByEquipeUn
		 * ());
		 * m.setNbrMatchMarqueMTByEquipeUn(matchMarqueMT+m.getNbrMatchMarqueMTByEquipeUn
		 * ()); m.setNbrMatchEncaisseMTByEquipeUn(matchEncaisseMT+m.
		 * getNbrMatchEncaisseMTByEquipeUn());
		 */
		return m;
	}

	@Override
	public void saveAllMatchFoot() throws Exception {
		
		List<MatchFoot> listMEqUNDOM=new ArrayList<>();
		List<MatchFoot> listMEqUNEXT=new ArrayList<>();
		List<MatchFoot> listMEqDEUXDOM=new ArrayList<>();
		List<MatchFoot> listMEqDEUXEXT=	new ArrayList<>();
		
		
		for (MatchFoot m : footRepository.findAll()) {
			MatchStat ms=new MatchStat();
			ms.setEquipeDomicile(m.getNameUn());
			ms.setEquipeExterieur(m.getNameDeux());
			ms.setLeague(m.getLeague());
			ms.setId(m.getId());
			
			if(m.getJour()>10) {
				if(m.getButEqUnMTUn()==m.getButEqDeuxMTUn()) {
					ms.setIsMatchNullMiTemps(1);
				}else {
					ms.setIsMatchNullMiTemps(0);
				}
				if((m.getButEqUnMTUn()+m.getButEqUnMTDeux()+m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>2)) {
					ms.setIsPlusDeuxBut(1);
				}else {
					ms.setIsPlusDeuxBut(0);
				}
				if(m.getButEqUnMTUn()+m.getButEqUnMTDeux()>0 && m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>0) {
					ms.setIsDeuxEquipeMarque(1);
				}else {
					ms.setIsDeuxEquipeMarque(0);
				}
					
				if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()>m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()) {
					ms.setIsMiTempsUnProlifique(1);
				}else {
					ms.setIsMiTempsUnProlifique(0);
				}
				if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()<m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()) {
					ms.setIsMiTempsDeuxProlifique(1);
				}else {
					ms.setIsMiTempsDeuxProlifique(0);
				}
				listMEqUNDOM=footRepository.findAllLastMatchByNameUn(m.getNameUn(), m.getId());
				listMEqUNEXT=footRepository.findAllLastMatchByNameDeux(m.getNameUn(), m.getId());
				listMEqDEUXDOM=footRepository.findAllLastMatchByNameUn(m.getNameDeux(), m.getId());
				listMEqDEUXEXT=footRepository.findAllLastMatchByNameDeux(m.getNameDeux(), m.getId());
				
				ms.setNbrMatchMarqueByEquipeUnADOM(matchmarqeAdom(listMEqUNDOM));
				ms.setNbrMatchMarqueByEquipeUnADOMMT(matchmarqeAdomMT(listMEqUNDOM));
				ms.setNbrMatchMarqueByEquipeUnAEXT(matchmarqeAext(listMEqUNEXT));
				ms.setNbrMatchMarqueByEquipeUnAEXTMT(matchmarqeAextMT(listMEqUNEXT));
				ms.setNbrMatchMarqueByEquipeDeuxADOM(matchmarqeAdom(listMEqDEUXDOM));
				ms.setNbrMatchMarqueByEquipeDeuxADOMMT(matchmarqeAdomMT(listMEqDEUXDOM));
				ms.setNbrMatchMarqueByEquipeDeuxAEXT(matchmarqeAext(listMEqDEUXEXT));
				ms.setNbrMatchMarqueByEquipeDeuxAEXTMT(matchmarqeAextMT(listMEqDEUXEXT));
				ms.setNbrButMarqueByEquipeUn(butmarqeAdom(listMEqUNDOM));
				ms.setNbrButMarqueByEquipeDeux(butmarqeAext(listMEqUNEXT));
				ms.setNbrButEncaisseByEquipeUn(butEncaisseAdom(listMEqUNDOM));
				ms.setNbrButEncaisseByEquipeDeux(butEncaisseAext(listMEqUNEXT));
				ms.setNbrButMarqueByEquipeUn(butmarqeAdom(listMEqDEUXDOM));
				ms.setNbrButMarqueByEquipeDeux(butmarqeAext(listMEqDEUXEXT));
				ms.setNbrButEncaisseByEquipeUn(butEncaisseAdom(listMEqDEUXDOM));
				ms.setNbrButEncaisseByEquipeDeux(butEncaisseAext(listMEqDEUXEXT));
				
				ms.setNbrMatchEncaisseByEquipeUnADOM(matchencaisseAdom(listMEqUNDOM));
				ms.setNbrMatchEncaisseByEquipeUnADOMMT(matchencaisseAdomMT(listMEqUNDOM));
				ms.setNbrMatchEncaisseByEquipeUnAEXT(matchencaisseAext(listMEqUNEXT));
				ms.setNbrMatchEncaisseByEquipeUnAEXTMT(matchencaisseAextMT(listMEqUNEXT));
				ms.setNbrMatchEncaisseByEquipeDeuxADOM(matchencaisseAdom(listMEqDEUXDOM));
				ms.setNbrMatchEncaisseByEquipeDeuxADOMMT(matchencaisseAdomMT(listMEqDEUXDOM));
				ms.setNbrMatchEncaisseByEquipeDeuxAEXT(matchencaisseAext(listMEqDEUXEXT));
				ms.setNbrMatchEncaisseByEquipeDeuxAEXTMT(matchencaisseAextMT(listMEqDEUXEXT));
				
				
				statRepository.save(ms);
			}
		}
	}

	@Override
	public void saveMatchFoot(MatchFoot matchFoot) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateMatchFoot() {
		// TODO Auto-generated method stub
		
	}
	
	public List<MatchFoot> getMatchMarqueByName(String name){
		List<MatchFoot> l=footRepository.findAllMatchMarqueDomByEquipe(name);
		l.addAll(footRepository.findAllMatchMarqueExtByEquipe(name));
		return l;
	}
	
	@Override
	public List<MatchFoot> getListMatchFootByEquipeUn(String nameUn) {
		// TODO Auto-generated method stub
		return footRepository.findAllMatchByNameUn(nameUn);
	}

	@Override
	public List<MatchFoot> getListMatchFootByEquipeDeux(String nameDeux) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<MatchFoot> getListMatchByLeague(String name, String league) {
		
		return null;//(List<MatchFoot>) footRepository.findByNameunORNamedeuxANDLeague(name, league);
	}

	@Override
	public List<MatchFoot> getMatchsByName(String name) {
		List<MatchFoot> liste=footRepository.findAllMatchByNameUn(name);
		List<MatchFoot> l=new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			l.add(liste.get(i));
		}
		return l;
	}
	
	@Override
	public List<MatchFoot> getMatchsLastByName(String name, int code) {
		List<MatchFoot> listein=footRepository.findAllLastMatchByNameUn(name, code);
		List<MatchFoot> listeout=footRepository.findAllLastMatchByNameDeux(name, code);
		List<MatchFoot> l=new ArrayList<>();
		for (int i = 0; i < 7; i++) {
			l.add(listein.get(i));
			l.add(listeout.get(i));
		}
		return l;
	}
	
	
	
	

	@Override
	public List<MatchFoot> getMatchEncaisseByName(String name) {
		List<MatchFoot> lin=footRepository.findAllMatchEncaisseDomByEquipe(name);
		List<MatchFoot> lout=footRepository.findAllMatchEncaisseExtByEquipe(name);
		List<MatchFoot> l = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			l.add(lin.get(i));
			l.add(lout.get(i));
			
		}
		
		
		return l;
	}
	
	public double matchmarqeAdom(List<MatchFoot> l) {
		int matchMarqADom=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqUnMTUn()+m.getButEqUnMTDeux()>0) {
				matchMarqADom+=1;
			}
			count+=1;
		}
		return matchMarqADom;
	}
	
	public double butmarqeAdom(List<MatchFoot> l) {
		int but=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			
				but+=m.getButEqUnMTUn()+m.getButEqUnMTDeux();
			
			count+=1;
		}
		return but;
	}
	
	public double butmarqeAext(List<MatchFoot> l) {
		int but=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			
				but+=m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux();
			
			count+=1;
		}
		return but;
	}
	
	public double butEncaisseAdom(List<MatchFoot> l) {
		int but=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>0) {
				but+=m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux();
			}
			count+=1;
		}
		return but;
	}
	
	public double butEncaisseAext(List<MatchFoot> l) {
		int but=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			but+=m.getButEqUnMTUn()+m.getButEqUnMTDeux();
			
			count+=1;
		}
		return but;
	}

	@Override
	public double matchmarqeAext(List<MatchFoot> l) {
		int matchMarqAEXT=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>0) {
				matchMarqAEXT+=1;
			}
			count+=1;
		}
		return matchMarqAEXT;
	}

	@Override
	public double matchencaisseAdom(List<MatchFoot> l) {
		int matchEncaisseADom=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>0) {
				matchEncaisseADom+=1;
			}
			count+=1;
		}
		return matchEncaisseADom;
		
	}

	@Override
	public double matchencaisseAext(List<MatchFoot> l) {
		int matchEncaisseAEXT=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqUnMTUn()+m.getButEqUnMTDeux()>0) {
				matchEncaisseAEXT+=1;
			}
			count+=1;
		}
		return matchEncaisseAEXT;
	}

	@Override
	public double matchmarqeAdomMT(List<MatchFoot> l) {
		int matchmarqueADOMMT=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqUnMTUn()>0) {
				matchmarqueADOMMT+=1;
			}
			count+=1;
		}
		return matchmarqueADOMMT;
	}

	@Override
	public double matchmarqeAextMT(List<MatchFoot> l) {
		int matchmarqueAEXTMT=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqDeuxMTUn()>0) {
				matchmarqueAEXTMT+=1;
			}
			count+=1;
		}
		return matchmarqueAEXTMT;
	}

	@Override
	public double matchencaisseAdomMT(List<MatchFoot> l) {
		int matchEncaisseADOMMT=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqDeuxMTUn()>0) {
				matchEncaisseADOMMT+=1;
			}
			count+=1;
		}
		return matchEncaisseADOMMT;
	}

	@Override
	public double matchencaisseAextMT(List<MatchFoot> l) {
		int matchEncaisseAEXTMT=0;
		int count=0;
		for (MatchFoot m : l) {
			if(count>5) {
				break;
			}
			if(m.getButEqUnMTUn()>0) {
				matchEncaisseAEXTMT+=1;
			}
			count+=1;
		}
		return matchEncaisseAEXTMT;
	}
	@Override
	public List<MatchStat> getAllMStat() {
		List<MatchStat> l = statRepository.findAll();
		for (MatchStat s : l) {
			DataMatchs m=new DataMatchs();
			m.setId(s.getId());
			
			
			m.setNbrMatchEncaisseByEquipeDeuxADOM(s.getNbrMatchEncaisseByEquipeDeuxADOM());
			m.setNbrMatchEncaisseByEquipeDeuxADOMMT(s.getNbrMatchEncaisseByEquipeDeuxADOMMT());
			m.setNbrMatchEncaisseByEquipeDeuxAEXT(s.getNbrMatchEncaisseByEquipeDeuxAEXT());
			m.setNbrMatchEncaisseByEquipeDeuxAEXTMT(s.getNbrMatchEncaisseByEquipeDeuxAEXTMT());
			m.setNbrMatchEncaisseByEquipeUnADOM(s.getNbrMatchEncaisseByEquipeUnADOM());
			m.setNbrMatchEncaisseByEquipeUnADOMMT(s.getNbrMatchEncaisseByEquipeUnADOMMT());
			m.setNbrMatchEncaisseByEquipeUnAEXT(s.getNbrMatchEncaisseByEquipeUnAEXT());
			m.setNbrMatchEncaisseByEquipeUnAEXTMT(s.getNbrMatchEncaisseByEquipeUnAEXTMT());
			
			m.setNbrMatchMarqueByEquipeDeuxADOM(s.getNbrMatchMarqueByEquipeDeuxADOM());
			m.setNbrMatchMarqueByEquipeDeuxADOMMT(s.getNbrMatchMarqueByEquipeDeuxADOMMT());
			m.setNbrMatchMarqueByEquipeDeuxAEXT(s.getNbrMatchMarqueByEquipeDeuxAEXT());
			m.setNbrMatchMarqueByEquipeDeuxAEXTMT(s.getNbrMatchMarqueByEquipeDeuxAEXTMT());
			m.setNbrMatchMarqueByEquipeUnADOM(s.getNbrMatchMarqueByEquipeUnADOM());
			m.setNbrMatchMarqueByEquipeUnADOMMT(s.getNbrMatchMarqueByEquipeUnADOMMT());
			m.setNbrMatchMarqueByEquipeUnAEXT(s.getNbrMatchMarqueByEquipeUnAEXT());
			m.setNbrMatchMarqueByEquipeUnAEXTMT(s.getNbrMatchMarqueByEquipeUnAEXTMT());
			
			m.setIsDeuxEquipeMarque(s.getIsDeuxEquipeMarque());
			
			dataMatchRepository.save(m);
		}
		return l;
	}
	
	
	@Override
	public String predict() throws Exception{
		//saveData();
		double learningRate=0.001;
		String a = null;
		MultiLayerConfiguration configuration= new NeuralNetConfiguration.Builder()
				.optimizationAlgo(OptimizationAlgorithm.CONJUGATE_GRADIENT)
				.weightInit(WeightInit.SIGMOID_UNIFORM)
				.updater(new Adam(learningRate))
				.list()
				.layer(0,new DenseLayer.Builder()
						  .nIn(20).nOut(2).activation(Activation.SIGMOID).build()
						  )
				.layer(1,new org.deeplearning4j.nn.conf.layers.OutputLayer.Builder()
						  .nIn(2)
						  .nOut(2)
						  .activation(Activation.SOFTMAX)
						  .lossFunction(LossFunction.POISSON)
						  .build()
						  )
						  
 				.build();
		MultiLayerNetwork model=new MultiLayerNetwork(configuration);
		model.init();
		
		
		File fileTrain = null;
		try {
			fileTrain = new ClassPathResource("trainData1.csv").getFile();
			
			RecordReader recordReaderTrain =new CSVRecordReader(0);
			recordReaderTrain.initialize(new FileSplit(fileTrain));
			
			int batchSize=1;
			DataSetIterator dataSetIteratorTrain=new RecordReaderDataSetIterator(recordReaderTrain, batchSize,20,2);
			int nEpocks=1;
			for (int j=0;j<nEpocks;j++) {
				model.fit(dataSetIteratorTrain);
				System.out.println("------------------------");
				
			}
			File fileTrainTest=new ClassPathResource("testData1.csv").getFile();
			RecordReader recordReaderTest =new CSVRecordReader(0);
			recordReaderTest.initialize(new FileSplit(fileTrainTest));
			int batchSizeT=1;
			DataSetIterator dataSetIteratorTest=new RecordReaderDataSetIterator(recordReaderTest, batchSizeT,20,2);
			Evaluation evaluation=new Evaluation();
			while(dataSetIteratorTest.hasNext()) {
				//DataSet dataset=dataSetIteratorTest.next();
				INDArray feature=dataSetIteratorTest.next().getFeatures();
				INDArray labels=dataSetIteratorTest.next().getLabels();
				INDArray predicted=model.output(feature);
				evaluation.eval(labels, predicted); 
			}
			System.out.println(evaluation.stats());
			a=evaluation.stats();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*DataSetIterator iterator = new RecordReaderDataSetIterator(
				recordReaderTrain, 1, 9, 9);*/
		/*
		 * DataSet allData = dataSetIteratorTrain.next(); DataNormalization normalizer =
		 * new NormalizerStandardize(); normalizer.fit(allData);
		 * normalizer.transform(allData); SplitTestAndTrain testAndTrain =
		 * allData.splitTestAndTrain(0.65); DataSet trainingData =
		 * testAndTrain.getTrain(); DataSet testData = testAndTrain.getTest();
		 */
		/*
		 * UIServer uiServer=UIServer.getInstance(); InMemoryStatsStorage
		 * inMemoryStatsStorage=new InMemoryStatsStorage();
		 * uiServer.attach(inMemoryStatsStorage);
		 * 
		 * model.setListeners(new StatsListener(inMemoryStatsStorage));
		 */
		
		
		
		
		
		return a;

	}
	public void saveData() {
		FileWriter file = null;
		FileWriter fileTest = null;
		FileWriter file1 = null;
		FileWriter fileTest1 = null;
		FileWriter file2 = null;
		FileWriter fileTest2 = null;
		FileWriter file3 = null;
		FileWriter fileTest3 = null;
		FileWriter file4 = null;
		FileWriter fileTest4 = null;
	       String sep="\n";
	       String delemetre=",";
	       List<MatchStat> l=statRepository.findAll();
	       try
	      {
	        file = new FileWriter("src/main/resources/trainData.csv");
	        fileTest = new FileWriter("src/main/resources/testData.csv");
	        file1 = new FileWriter("src/main/resources/trainData1.csv");
	        fileTest1 = new FileWriter("src/main/resources/testData1.csv");
	        file2 = new FileWriter("src/main/resources/trainData2.csv");
	        fileTest2 = new FileWriter("src/main/resources/testData2.csv");
	        file3 = new FileWriter("src/main/resources/trainData3.csv");
	        fileTest3 = new FileWriter("src/main/resources/testData3.csv");
	        file4 = new FileWriter("src/main/resources/trainData4.csv");
	        fileTest4 = new FileWriter("src/main/resources/testData4.csv");
	        int j=0;
	        for (MatchStat d : l) {
	        	if(j>28000) {
	        		  fileTest.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
			          fileTest.append(delemetre);
			          fileTest.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
			          fileTest.append(delemetre);
			          
			          fileTest.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
			          fileTest.append(delemetre);
			          
			          fileTest.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
			          fileTest.append(delemetre);
			          fileTest.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
			          fileTest.append(delemetre);
			          fileTest.append((String.valueOf(d.getIsPlusDeuxBut())));
			          fileTest.append(sep);
			          
			          //*********************************
			          fileTest1.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
			          fileTest1.append(delemetre);
			          fileTest1.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
			          fileTest1.append(delemetre);
			          
			          fileTest1.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
			          fileTest1.append(delemetre);
			          
			          fileTest1.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
			          fileTest1.append(delemetre);
			          fileTest1.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
			          fileTest1.append(delemetre);
			          fileTest1.append((String.valueOf(d.getIsMatchNullMiTemps())));
			          fileTest1.append(sep);
			          //****************************************************
			          fileTest2.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
			          fileTest2.append(delemetre);
			          fileTest2.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
			          fileTest2.append(delemetre);
			          
			          fileTest2.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
			          fileTest2.append(delemetre);
			          
			          fileTest2.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
			          fileTest2.append(delemetre);
			          fileTest2.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
			          fileTest2.append(delemetre);
			          fileTest2.append((String.valueOf(d.getIsMiTempsDeuxProlifique())));
			          fileTest2.append(sep);
			          //*******************************************************
						/*
						 * fileTest3.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
						 * fileTest3.append(delemetre);
						 * fileTest3.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
						 * fileTest3.append(delemetre);
						 */
			          fileTest3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
			          fileTest3.append(delemetre);
						/*
						 * fileTest3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
						 * fileTest3.append(delemetre);
						 * fileTest3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
						 * fileTest3.append(delemetre);
						 */
			          fileTest3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
			          fileTest3.append(delemetre);
			          
			          fileTest3.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
			          fileTest3.append(delemetre);
			          
						/*
						 * fileTest3.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
						 * fileTest3.append(delemetre);
						 * fileTest3.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
						 * fileTest3.append(delemetre);
						 */
			          fileTest3.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
			          fileTest3.append(delemetre);
						/*
						 * fileTest3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
						 * fileTest3.append(delemetre);
						 * fileTest3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
						 * fileTest3.append(delemetre);
						 */
			          fileTest3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
			          fileTest3.append(delemetre);
			          fileTest3.append((String.valueOf(d.getIsPlusDeuxBut())));
			          fileTest3.append(sep);
			          
			          //***********************************************
			          fileTest4.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
			          fileTest4.append(delemetre);
			          fileTest4.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
			          fileTest4.append(delemetre);
			          
			          fileTest4.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
			          fileTest4.append(delemetre);
			          
			          fileTest4.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
			          fileTest4.append(delemetre);
			          fileTest4.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
			          fileTest4.append(delemetre);
			          fileTest4.append((String.valueOf(d.getIsMiTempsUnProlifique())));
			          fileTest4.append(sep);

	        	}else {
	        		  file.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
	        		  file.append(delemetre);
	        		  file.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
	        		  file.append(delemetre);
	        		  
	        		  file.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
	        		  file.append(delemetre);
			          
	        		  file.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
	        		  file.append(delemetre);
	        		  file.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
	        		  file.append(delemetre);
	        		  file.append((String.valueOf(d.getIsPlusDeuxBut())));
			          file.append(sep);
			          
			          //********************************************************
			          
			          file1.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
	        		  file1.append(delemetre);
	        		  file1.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
	        		  file1.append(delemetre);
	        		  
	        		  file1.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
	        		  file1.append(delemetre);
			          
	        		  file1.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
	        		  file1.append(delemetre);
	        		  file1.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
	        		  file1.append(delemetre);
	        		  file1.append((String.valueOf(d.getIsMiTempsUnProlifique())));
			          file1.append(sep);
			          //********************************************************
			          file2.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
	        		  file2.append(delemetre);
	        		  file2.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
	        		  file2.append(delemetre);
	        		  
	        		  file2.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
	        		  file2.append(delemetre);
			          
	        		  file2.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
	        		  file2.append(delemetre);
	        		  file2.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
	        		  file2.append(delemetre);
	        		  file2.append((String.valueOf(d.getIsMiTempsDeuxProlifique())));
			          file2.append(sep);
			          //***************************************************************
						/*
						 * file3.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
						 * file3.append(delemetre);
						 * file3.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
						 * file3.append(delemetre);
						 */
	        		  file3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
	        		  file3.append(delemetre);
						/*
						 * file3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
						 * file3.append(delemetre);
						 * file3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
						 * file3.append(delemetre);
						 */
	        		  file3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
	        		  file3.append(delemetre);
	        		  
	        		  file3.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
	        		  file3.append(delemetre);
			          
						/*
						 * file3.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
						 * file3.append(delemetre);
						 * file3.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
						 * file3.append(delemetre);
						 */
	        		  file3.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
	        		  file3.append(delemetre);
						/*
						 * file3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
						 * file3.append(delemetre);
						 * file3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
						 * file3.append(delemetre);
						 */
	        		  file3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
	        		  file3.append(delemetre);
	        		  file3.append((String.valueOf(d.getIsPlusDeuxBut())));
			          file3.append(sep);
			          
			          //***************************************************
			          
			          file4.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOM()));
	        		  file4.append(delemetre);
	        		  file4.append(String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxADOMMT()));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeDeuxAEXTMT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOM())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnADOMMT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchEncaisseByEquipeUnAEXTMT())));
	        		  file4.append(delemetre);
	        		  
	        		  file4.append((String.valueOf(d.getNbrButMarqueByEquipeUn())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrButMarqueByEquipeDeux())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrButEncaisseByEquipeUn())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrButEncaisseByEquipeDeux())));
	        		  file4.append(delemetre);
			          
	        		  file4.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOM()));
	        		  file4.append(delemetre);
	        		  file4.append(String.valueOf(d.getNbrMatchMarqueByEquipeDeuxADOMMT()));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchMarqueByEquipeDeuxAEXTMT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOM())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnADOMMT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getNbrMatchMarqueByEquipeUnAEXTMT())));
	        		  file4.append(delemetre);
	        		  file4.append((String.valueOf(d.getIsMiTempsUnProlifique())));
			          file4.append(sep);
	        	}
	        	j+=1;
	        	  
	        }
	      
	        file.close();
	        fileTest.close();
	     }
	      catch(Exception e)
	      {
	        e.printStackTrace();
	      }
	}
	@Override
	public Equipe getStatByEquipe(String equipe,int numberMatch) {
		Equipe e=new Equipe();
		List<MatchFoot> l=footRepository.getStatByEquipe(equipe);
		String name;
		int numberMatchNull=0;
		int numberMatchNullMiTemps=0;
		int numberMatchDeuxEquipeMarque=0;
		int numberMatchMTDeuxProlifique=0;
		int numberMatchMTUnProlifique=0;
		int numberMatchPlusDeuxBut=0;
		int numberMatchPlusDeuxButMiTemps=0;
		int jour=1;
		
		for (MatchFoot m : l) {
			e.setName(equipe);
			if(jour>numberMatch) {
				break;
			}
			if(m.getButEqUnMTUn()==m.getButEqDeuxMTUn()) {
				numberMatchNullMiTemps+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()<m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()) {
				numberMatchMTDeuxProlifique+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()>m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()) {
				numberMatchMTUnProlifique+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()+m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()>2) {
				numberMatchPlusDeuxBut+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()>2) {
				numberMatchPlusDeuxButMiTemps+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqUnMTDeux()>0 && m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>0) {
				numberMatchDeuxEquipeMarque+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqUnMTDeux()==m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()) {
				numberMatchNull+=1;
			}
			jour+=1;
		}
		e.setNumberMatchNull(numberMatchNull);
		e.setNumberMatchDeuxEquipeMarque(numberMatchDeuxEquipeMarque);
		e.setNumberMatchMTDeuxProlifique(numberMatchMTDeuxProlifique);
		e.setNumberMatchMTUnProlifique(numberMatchMTUnProlifique);
		e.setNumberMatchNullMiTemps(numberMatchNullMiTemps);
		e.setNumberMatchPlusDeuxBut(numberMatchPlusDeuxBut);
		e.setNumberMatchPlusDeuxButMiTemps(numberMatchPlusDeuxButMiTemps);
		e.setNumberMatch(numberMatch);
		return e;
	}
	
	@Override
	public LeagueStats getStat(String league,int numberMatch) {
		LeagueStats championnat=new LeagueStats();
		List<MatchFoot> l=footRepository.getStatByLeague(league);
		 int numberMatchNullMiTemps=0;
		 int numberMatchDeuxEquipeMarque=0;
		 int numberMatchMiTempsDeuxPlusProlifique=0;
		 int numberMatchMiTempsUnPlusProlifique=0;
		 int numberMatchPlusDeuxBut=0;
		 int numberMatchPlusDeuxButMiTemps=0;
		 int jour=1;
		
		for (MatchFoot m : l) {
			championnat.setName(m.getLeague());
			if(jour>numberMatch) {
				break;
			}
			if(m.getButEqUnMTUn()==m.getButEqDeuxMTUn()) {
				numberMatchNullMiTemps+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()<m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()) {
				numberMatchMiTempsDeuxPlusProlifique+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()>m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()) {
				numberMatchMiTempsUnPlusProlifique+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()+m.getButEqUnMTDeux()+m.getButEqDeuxMTDeux()>2) {
				numberMatchPlusDeuxBut+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqDeuxMTUn()>2) {
				numberMatchPlusDeuxButMiTemps+=1;
			}
			if(m.getButEqUnMTUn()+m.getButEqUnMTDeux()>0 && m.getButEqDeuxMTUn()+m.getButEqDeuxMTDeux()>0) {
				numberMatchDeuxEquipeMarque+=1;
			}
			jour+=1;
		}
		championnat.setNumberMatchDeuxEquipeMarque(numberMatchDeuxEquipeMarque);
		championnat.setNumberMatchMiTempsDeuxPlusProlifique(numberMatchMiTempsDeuxPlusProlifique);
		championnat.setNumberMatchMiTempsUnPlusProlifique(numberMatchMiTempsUnPlusProlifique);
		championnat.setNumberMatchNullMiTemps(numberMatchNullMiTemps);
		championnat.setNumberMatchPlusDeuxBut(numberMatchPlusDeuxBut);
		championnat.setNumberMatchPlusDeuxButMiTemps(numberMatchPlusDeuxButMiTemps);
		championnat.setNumberMatch(jour);
		return championnat;
	}
	@Override
	public List<MatchFoot> getM(String name, int num) {
		List<MatchFoot> l = footRepository.getStatByEquipe(name);
		List<MatchFoot> lis=new ArrayList<>();
		int number=1;
		for (MatchFoot m : l) {
			if(number>num) {
				break;
			}
			number+=1;
			lis.add(m);
			
			
		}
		return lis;
	}
	@Override
	public String predictDeuxEquipeMarque() throws Exception{
		//saveData();
		double learningRate=0.001;
		String a = null;
		MultiLayerConfiguration configuration= new NeuralNetConfiguration.Builder()
				.optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
				.weightInit(WeightInit.UNIFORM )
				.updater(new Adam(learningRate))
				.list()
				.layer(0,new DenseLayer.Builder()
						  .nIn(20).nOut(2).activation(Activation.RELU).build()
						  )
				.layer(1,new org.deeplearning4j.nn.conf.layers.OutputLayer.Builder()
						  .nIn(2)
						  .nOut(2)
						  .activation(Activation.SOFTPLUS)
						  .lossFunction(LossFunction.POISSON)
						  .build()
						  )
						  
 				.build();
		MultiLayerNetwork model=new MultiLayerNetwork(configuration);
		model.init();
		
		
		File fileTrain = null;
		try {
			fileTrain = new ClassPathResource("trainData.csv").getFile();
			
			RecordReader recordReaderTrain =new CSVRecordReader(0);
			recordReaderTrain.initialize(new FileSplit(fileTrain));
			
			int batchSize=1;
			DataSetIterator dataSetIteratorTrain=new RecordReaderDataSetIterator(recordReaderTrain, batchSize,20,2);
			int nEpocks=3;
			for (int j=0;j<nEpocks;j++) {
				model.fit(dataSetIteratorTrain);
				System.out.println("------------------------");
				
			}
			File fileTrainTest=new ClassPathResource("testData.csv").getFile();
			RecordReader recordReaderTest =new CSVRecordReader(0);
			recordReaderTest.initialize(new FileSplit(fileTrainTest));
			int batchSizeT=1;
			DataSetIterator dataSetIteratorTest=new RecordReaderDataSetIterator(recordReaderTest, batchSizeT,20,2);
			Evaluation evaluation=new Evaluation();
			while(dataSetIteratorTest.hasNext()) {
				DataSet dataset=dataSetIteratorTest.next();
				INDArray feature=dataset.getFeatures();
				INDArray labels=dataset.getLabels();
				INDArray predicted=model.output(feature);
				evaluation.eval(labels, predicted); 
				
			}
			System.out.println(evaluation.stats());
			a=evaluation.stats();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		/*DataSetIterator iterator = new RecordReaderDataSetIterator(
				recordReaderTrain, 1, 9, 9);*/
		/*
		 * DataSet allData = dataSetIteratorTrain.next(); DataNormalization normalizer =
		 * new NormalizerStandardize(); normalizer.fit(allData);
		 * normalizer.transform(allData); SplitTestAndTrain testAndTrain =
		 * allData.splitTestAndTrain(0.65); DataSet trainingData =
		 * testAndTrain.getTrain(); DataSet testData = testAndTrain.getTest();
		 */
		/*
		 * UIServer uiServer=UIServer.getInstance(); InMemoryStatsStorage
		 * inMemoryStatsStorage=new InMemoryStatsStorage();
		 * uiServer.attach(inMemoryStatsStorage);
		 * 
		 * model.setListeners(new StatsListener(inMemoryStatsStorage));
		 */
		return a;

	}
	
	
	@Override
	public List<String> getEquipeByLeague(String name) {
		// TODO Auto-generated method stub
		return footRepository.getEquipeByLeague(name);
	}

	@Override
	public List<MatchAbstract> getNextMatch() throws ParseException{
		List<MatchAbstract> nextMatch=new ArrayList<>();
		int count =1;
		Date today=new Date();
		String n="10/07/2023";
		
		Date d = new SimpleDateFormat("dd/MM/yyyy").parse(n);
		LocalDateTime ldt = LocalDateTime.ofInstant(today.toInstant(), ZoneId.systemDefault());
		ldt=ldt.minusDays(5);
		LocalDateTime ldt1=ldt.plusDays(10);
		Date out = Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant());
		Date out1 = Date.from(ldt1.atZone(ZoneId.systemDefault()).toInstant());
		System.out.println(out);
			for (MatchAbstract matchAbstract : abstractRepository.findAllByDate(out)) {
				matchAbstract.setComment("");
				if(matchAbstract.getDate().before(out1)) {
					Equipe e1=getStatByEquipe(matchAbstract.getNameUn(), 10);
					Equipe e2=getStatByEquipe(matchAbstract.getNameDeux(), 10);
					
					if(e1.getNumberMatchDeuxEquipeMarque()>6 && e2.getNumberMatchDeuxEquipeMarque()>6) {
						matchAbstract.setComment(matchAbstract.getComment()+"BUT POUR LES 2");
						boolean exist=false;
						for (MatchAbstract m : nextMatch) {
							if(m.getNameUn().contains(matchAbstract.getNameUn()) && m.getNameDeux().contains(matchAbstract.getNameDeux())&& m.getJour()==matchAbstract.getJour()) {
								exist=true;
							}
						}
						if(exist==false) {
							nextMatch.add(matchAbstract);
						}
					}
					if(e1.getNumberMatchPlusDeuxBut()>7 && e2.getNumberMatchPlusDeuxBut()>7) {
						matchAbstract.setComment(matchAbstract.getComment()+"+ 2.5");
						boolean exist=false;
						for (MatchAbstract m : nextMatch) {
							if(m.getNameUn().contains(matchAbstract.getNameUn()) && m.getNameDeux().contains(matchAbstract.getNameDeux())&& m.getJour()==matchAbstract.getJour()) {
								exist=true;
							}
						}
						if(exist==false) {
							nextMatch.add(matchAbstract);
						}
					}
					
					if(e1.getNumberMatchPlusDeuxBut()>7 && e2.getNumberMatchPlusDeuxBut()>7) {
						matchAbstract.setComment(matchAbstract.getComment()+"+ 2.5");
						boolean exist=false;
						for (MatchAbstract m : nextMatch) {
							if(m.getNameUn().contains(matchAbstract.getNameUn()) && m.getNameDeux().contains(matchAbstract.getNameDeux())&& m.getJour()==matchAbstract.getJour()) {
								exist=true;
							}
						}
						if(exist==false) {
							nextMatch.add(matchAbstract);
						}
					}
					 
					
					
					count=count+1;
				}
				
				
				
			}
		
		return nextMatch;
	}
}
