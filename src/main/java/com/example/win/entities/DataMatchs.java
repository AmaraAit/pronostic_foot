package com.example.win.entities;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
public class DataMatchs {
	
	@Id
	private long id;
	
	private double nbrMatchMarqueByEquipeUnADOM;
	private double nbrMatchMarqueByEquipeUnAEXT;
	private double nbrMatchMarqueByEquipeDeuxADOM;
	private double nbrMatchMarqueByEquipeDeuxAEXT;
	private double nbrMatchMarqueByEquipeUnADOMMT;
	private double nbrMatchMarqueByEquipeUnAEXTMT;
	private double nbrMatchMarqueByEquipeDeuxADOMMT;
	private double nbrMatchMarqueByEquipeDeuxAEXTMT;
	
	
	private double nbrMatchEncaisseByEquipeUnADOM;
	private double nbrMatchEncaisseByEquipeUnAEXT;
	private double nbrMatchEncaisseByEquipeDeuxADOM;
	private double nbrMatchEncaisseByEquipeDeuxAEXT;
	private double nbrMatchEncaisseByEquipeUnADOMMT;
	private double nbrMatchEncaisseByEquipeUnAEXTMT;
	private double nbrMatchEncaisseByEquipeDeuxADOMMT;
	private double nbrMatchEncaisseByEquipeDeuxAEXTMT;
	
	private int isDeuxEquipeMarque;
}
