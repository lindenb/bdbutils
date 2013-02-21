package com.github.lindenb.bdbutils.util;

import java.util.Arrays;
import java.util.Map;


public class DefaultDictionary
	implements Dictionary
	{
	private String tokens[];
	private Map<String,Integer> map;
	public DefaultDictionary(String tokens[])
		{
		this.tokens=tokens;
		this.map= new java.util.HashMap<String,Integer>(tokens.length);
		for(String s:tokens)
			{
			map.put(s.toUpperCase(),map.size());
			}
		}

	public void addAlias(int idx,String alias)
		{
		alias=alias.toUpperCase();
		if(idx<0 || idx>=tokens.length) throw new IndexOutOfBoundsException();
		Integer v=this.map.get(alias);
		if(v!=null)
			{
			if(v==idx) return;
			throw new IllegalArgumentException("defined twice with != id "+alias+"/"+v);
			}
		map.put(alias,idx);
		}
	
	@Override
	public int size()
		{
		return this.map.size();
		}
	
	@Override
	public int getIndex(String key)
		{
		if(key==null) return -1;
		Integer pos=map.get(key.toUpperCase());
		return pos==null?-1:pos.intValue();
		}
	@Override
	public String get(int index)
		{
		return this.tokens[index];
		}
	
	
	@Override
	public int hashCode()
		{
		final int prime = 31;
		int result = 1;
		result = prime * result + Arrays.hashCode(tokens);
		return result;
		}

	@Override
	public boolean equals(Object obj)
		{
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		DefaultDictionary other = (DefaultDictionary) obj;
		if (!Arrays.equals(tokens, other.tokens)) { return false; }
		return true;
		}

	@Override
	public String toString()
		{
		return Arrays.toString(tokens);
		}
	}
