package com.github.lindenb.bdbutils.binding;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.DatabaseEntry;


public abstract class FixedSizeBinding<T extends FixedSize>
	implements EntryBinding<T>
	{
	/** FixedSizeBinding */
	public FixedSizeBinding()
		{
		}
	
	/** create a new instance of T */
	protected abstract T newInstance();
	
	
	@Override
	public T entryToObject(final DatabaseEntry in)
		{
		T o=newInstance();
		o.readFromBytes(in.getData(),0);
		return o;
		}

	@Override
	public void objectToEntry(final T o, DatabaseEntry out)
		{
		byte array[]=new byte[o.getSizeOf()];
		o.writeToBytes(array,0);
		out.setData(array);
		}
	}
