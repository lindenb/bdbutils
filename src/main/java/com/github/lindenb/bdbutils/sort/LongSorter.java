package com.github.lindenb.bdbutils.sort;

import com.sleepycat.bind.tuple.TupleInput;


public class LongSorter
	extends AbstractSorterComparable<Long>
	{
	@Override
	protected Long translate(final byte a[])
		{
		return entryToInput(a).readLong();
		}
	}
