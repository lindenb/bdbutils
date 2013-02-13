package com.github.lindenb.bdbutils.sort;


public abstract class AbstractSorterComparable<T extends Comparable<T> >
	extends AbstractSorter<T>
	{
	/** compare two objects T */
	@Override
	protected int cmp(final T o1, final T o2)
		{
		return o1.compareTo(o2);
		}
	}
