package com.github.lindenb.bdbutils.sort;

import com.sleepycat.bind.tuple.TupleInput;


public class StringSorter
	extends AbstractSorterComparable<String>
	{
	@Override
	protected String translate(final byte a[])
		{
		return entryToInput(a).readString();
		}
	}
