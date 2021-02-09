package com.pokemon.demo.dao;

import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.pokemon.demo.vo.Box;
import com.pokemon.demo.vo.Entry;
import com.pokemon.demo.vo.Pokemon;

@Repository
public class EntryDAO {

	@Autowired
	SqlSession session;
	
	public List<Pokemon> pokemonList() {
		EntryMapper mapper = session.getMapper(EntryMapper.class);
		return mapper.pokemonList();
	}
	
	public List<Entry> myEntryList(Entry entry) {
		EntryMapper mapper = session.getMapper(EntryMapper.class);
		return mapper.myEntryList(entry);
	}
	
	public int insertEntry(Entry entry) {
		EntryMapper mapper = session.getMapper(EntryMapper.class);
		return mapper.insertEntry(entry);
	}
	
	public Box getInsertableBox(String trainer_id) {
		EntryMapper mapper = session.getMapper(EntryMapper.class);
		return mapper.getInsertableBox(trainer_id);
	}
	
	public int insertNewBox(Box box) {
		EntryMapper mapper = session.getMapper(EntryMapper.class);
		return mapper.insertNewBox(box);
	}
	
	public int updateBoxAmount(Box box) {
		EntryMapper mapper = session.getMapper(EntryMapper.class);
		return mapper.updateBoxAmount(box);
	}
	
}
