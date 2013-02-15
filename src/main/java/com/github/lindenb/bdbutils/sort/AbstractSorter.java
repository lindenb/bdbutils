package com.github.lindenb.bdbutils.sort;

import com.sleepycat.bind.tuple.TupleInput;

public abstract class AbstractSorter<T>
	implements java.util.Comparator<byte[]>
	{
	/** Utility method to create a new tuple input from an array */
	protected TupleInput entryToInput(final byte a[],int offset,int len)
		{
		return new TupleInput(a);
		}
	
	/** create an object T from an array of bytes */
	protected abstract T translate(final byte a[],int offset,int lent);
	
	protected T translate(final byte a[])
		{
		return translate(a,0,a.length);
		}
	
	/** compare two objects T */
	protected abstract int cmp(final T o1, final T o2);
	
	@Override
	public int compare(byte a1[],byte a2[])
		{
		return this.cmp(translate(a1),translate(a2));
		}
	
	@Override
	public String toString()
		{
		return getClass().toString();
		}
	}
