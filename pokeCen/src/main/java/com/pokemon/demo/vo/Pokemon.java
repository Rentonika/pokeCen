package com.pokemon.demo.vo;

import java.util.List;

import lombok.Data;

@Data
public class Pokemon {

	private int pokeno;
	private String name;
	private List<String> types;
	private int height;
	private int weight;
	private List<String> habitats;
	private String sound;
	private String description;
	
	// 매핑 하기 위한 열
	private String type;
	private String habitat;
	
}
