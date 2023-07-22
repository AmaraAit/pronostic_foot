package com.example.win.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeagueStats {
	
	private String name;
	private int numberMatchNullMiTemps;
	private int numberMatchDeuxEquipeMarque;
	private int numberMatchMiTempsDeuxPlusProlifique;
	private int numberMatchMiTempsUnPlusProlifique;
	private int numberMatchPlusDeuxBut;
	private int numberMatchPlusDeuxButMiTemps;
	private int numberMatch;
}
