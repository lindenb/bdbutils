package com.github.lindenb.bdbutils.sort;


public class StringSorter
	extends AbstractSorterComparable<String>
	{
	@Override
	protected String translate(final byte a[],int offset,int len)
		{
		return entryToInput(a,offset,len).readString();
		}
	}
