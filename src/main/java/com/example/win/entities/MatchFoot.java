package com.example.win.entities;

import javax.annotation.Generated;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
public class MatchFoot {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private int jour;
	private int code;
	private String nameUn;
	private String nameDeux;
	private String league;
	private int butEqUnMTUn;
	private int butEqDeuxMTUn;
	private int butEqUnMTDeux;
	private int butEqDeuxMTDeux;
	

}
