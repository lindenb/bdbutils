package com.github.lindenb.bdbutils.sort;



public class LongSorter
	extends AbstractSorterComparable<Long>
	{
	@Override
	protected Long translate(final byte a[],int offset,int len)
		{
		return entryToInput(a,offset,len).readLong();
		}
	}
