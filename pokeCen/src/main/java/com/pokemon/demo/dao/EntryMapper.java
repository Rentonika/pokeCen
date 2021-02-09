package com.pokemon.demo.dao;

import java.util.List;

import com.pokemon.demo.vo.Box;
import com.pokemon.demo.vo.Entry;
import com.pokemon.demo.vo.Pokemon;

public interface EntryMapper {
	
	public List<Pokemon> pokemonList();
	public List<Entry> myEntryList(Entry entry);
	public int insertEntry(Entry entry);
	public Box getInsertableBox(String trainer_id);
	public int insertNewBox(Box box);
	public int updateBoxAmount(Box box);
}
