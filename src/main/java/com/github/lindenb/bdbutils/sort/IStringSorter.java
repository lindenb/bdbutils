package com.github.lindenb.bdbutils.sort;



public class IStringSorter
	extends StringSorter
	{
	@Override
	protected int cmp(final String o1, final String o2)
		{
		return o1.compareToIgnoreCase(o2);
		}
	}
