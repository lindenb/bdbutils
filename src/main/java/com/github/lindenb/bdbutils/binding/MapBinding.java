package com.github.lindenb.bdbutils.binding;

import java.util.HashMap;
import java.util.Map;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class MapBinding<K,V> extends AbstractTupleBinding<java.util.Map<K,V>>
	{
	private TupleBinding<K> keyBinding=null;
	private TupleBinding<V> valueBinding=null;
	private int size_capacity=4;
	

	
	public MapBinding(
			TupleBinding<K> keyBinding,
			TupleBinding<V> valueBinding
			)
		{
		this(keyBinding,valueBinding,4);
		}
	
	
	public MapBinding(
			TupleBinding<K> keyBinding,
			TupleBinding<V> valueBinding,
			int size_capacity
			)
		{
		this.keyBinding=keyBinding;
		this.valueBinding=valueBinding;
		this.size_capacity=size_capacity;
		switch(size_capacity)
			{
			case 1: case 2: case 4:break;
			default:throw new IllegalArgumentException("bad size capacity not(1/2/4)");
			}
		}
	
	protected Map<K,V> createMap(int reserve)
		{
		return new HashMap<K,V>(reserve);
		}
	
	@Override
	public Map<K, V> entryToObject(TupleInput in)
		{
		int n_elements;
		switch(this.size_capacity)
			{
			case 1: n_elements=in.readUnsignedByte();break;
			case 2: n_elements=in.readUnsignedShort();break;
			case 4: n_elements=in.readInt();break;
			default: throw new IllegalStateException();
			}
		Map<K,V> m=createMap(n_elements);
		for(int i=0;i< n_elements;++i)
			{
			K k=this.keyBinding.entryToObject(in);
			V v=this.valueBinding.entryToObject(in);
			m.put(k, v);
			}
		return m;
		}

	@Override
	public void objectToEntry(final Map<K, V> map, TupleOutput out)
		{
		int n=map.size();
		switch(this.size_capacity)
			{
			case 1:
				{
				if(n>255) throw new IllegalStateException("256 > n="+n);
				out.writeUnsignedByte((int)n);
				break;
				}
			case 2:
				{
				if(n>65535) throw new IllegalStateException("256 > n="+n);
				out.writeUnsignedShort((int)n);
				break;
				}
			case 4:
				{
				out.writeInt(n);
				break;
				}
			default: throw new IllegalStateException();
			}
		for(K k:map.keySet())
			{
			this.keyBinding.objectToEntry(k, out);
			this.valueBinding.objectToEntry(map.get(k), out);
			}
		}
	}
