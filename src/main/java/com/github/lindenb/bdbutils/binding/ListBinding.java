package com.github.lindenb.bdbutils.binding;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class ListBinding<K> extends AbstractTupleBinding<java.util.List<K>>
	{
	private TupleBinding<K> keyBinding=null;
	private int size_capacity=4;
	

	
	public ListBinding(
			TupleBinding<K> keyBinding
			)
		{
		this(keyBinding,4);
		}
	
	
	public ListBinding(
			TupleBinding<K> keyBinding,
			int size_capacity
			)
		{
		this.keyBinding=keyBinding;
		this.size_capacity=size_capacity;
		switch(size_capacity)
			{
			case 1: case 2: case 4:break;
			default:throw new IllegalArgumentException("bad size capacity not(1/2/4)");
			}
		}
	
	protected List<K> createList(int reserve)
		{
		return new ArrayList<K>(reserve);
		}
	
	@Override
	public List<K> entryToObject(TupleInput in)
		{
		int n_elements;
		switch(this.size_capacity)
			{
			case 1: n_elements=in.readUnsignedByte();break;
			case 2: n_elements=in.readUnsignedShort();break;
			case 4: n_elements=in.readInt();break;
			default: throw new IllegalStateException();
			}
		List<K> m=createList(n_elements);
		for(int i=0;i< n_elements;++i)
			{
			K k=this.keyBinding.entryToObject(in);
			m.add(k);
			}
		return m;
		}

	@Override
	public void objectToEntry(final List<K> L, TupleOutput out)
		{
		int n=L.size();
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
		for(K k:L)
			{
			this.keyBinding.objectToEntry(k, out);
			}
		}
	}
