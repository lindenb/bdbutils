package com.github.lindenb.bdbutils.binding;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public abstract  class AbstractTupleBinding<T>
	extends TupleBinding<T>
	{
	@Override
	public abstract T entryToObject(TupleInput in);
	@Override
	public abstract void objectToEntry(T o, TupleOutput out);
	}
