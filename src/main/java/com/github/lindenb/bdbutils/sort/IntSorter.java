package com.github.lindenb.bdbutils.sort;



public class IntSorter
	extends AbstractSorterComparable<Integer>
	{
	@Override
	protected Integer translate(final byte a[],int offset,int len)
		{
		return entryToInput(a,offset,len).readInt();
		}
	}
