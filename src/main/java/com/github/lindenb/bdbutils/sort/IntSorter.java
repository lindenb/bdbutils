package com.github.lindenb.bdbutils.sort;

import com.sleepycat.bind.tuple.TupleInput;


public class IntSorter
	extends AbstractSorterComparable<Integer>
	{
	@Override
	protected Integer translate(final byte a[])
		{
		return entryToInput(a).readInt();
		}
	}
