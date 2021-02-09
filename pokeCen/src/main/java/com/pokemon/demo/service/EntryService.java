package com.pokemon.demo.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.pokemon.demo.dao.EntryDAO;
import com.pokemon.demo.vo.Box;
import com.pokemon.demo.vo.Entry;
import com.pokemon.demo.vo.Pokemon;

@Service
public class EntryService {

	@Autowired
	EntryDAO dao;
	
	public List<Pokemon> pokemonList() {
		List<Pokemon> pokemonList = dao.pokemonList();
		for(Pokemon poke : pokemonList) {
			poke.setTypes(Arrays.asList(poke.getType().split(",")));
			if(poke.getHabitat() == null) continue;
			poke.setHabitats(Arrays.asList(poke.getHabitat().split(",")));
		}
		return pokemonList;
	}
	
	public List<Entry> myEntryList(Entry entry) {
		return dao.myEntryList(entry);
	}
	
	public int insertEntry(Entry entry) {
		return dao.insertEntry(entry);
	}
	
	public Box getInsertableBox(String trainer_id) {
		return dao.getInsertableBox(trainer_id);
	}
	
	public int insertNewBox(Box box) {
		return dao.insertNewBox(box);
	}
	
	public int updateBoxAmount(Box box) {
		return dao.updateBoxAmount(box);
	}
}
