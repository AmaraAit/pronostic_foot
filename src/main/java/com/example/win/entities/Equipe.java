package com.example.win.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Equipe {
	
	private String name;
	private int numberMatchNull;
	private int numberMatchNullMiTemps;
	private int numberMatchDeuxEquipeMarque;
	private int numberMatchMTDeuxProlifique;
	private int numberMatchMTUnProlifique;
	private int numberMatchPlusDeuxBut;
	private int numberMatchPlusDeuxButMiTemps;
	
	private int numberMatch;
	

}
