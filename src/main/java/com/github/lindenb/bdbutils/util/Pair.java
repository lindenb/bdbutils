package com.github.lindenb.bdbutils.util;

public class Pair<K,V>
	{
	private K key;
	private V data;
	public Pair(K key,V data)
		{
		this.key=key;
		this.data=data;
		}
	public K getKey()
		{
		return this.key;
		}
	public V getValue()
		{
		return this.data;
		}
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((data == null) ? 0 : data.hashCode());
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		@SuppressWarnings("rawtypes")
		Pair other = (Pair) obj;
		if (data == null) {
			if (other.data != null)
				return false;
		} else if (!data.equals(other.data))
			return false;
		if (key == null) {
			if (other.key != null)
				return false;
		} else if (!key.equals(other.key))
			return false;
		return true;
	}
	@Override
	public String toString()
		{
		return "("+key+"/"+data+")";
		}
	}
