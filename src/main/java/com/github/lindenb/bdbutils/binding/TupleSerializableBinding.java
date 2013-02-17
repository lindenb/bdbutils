package com.github.lindenb.bdbutils.binding;

import java.lang.reflect.Constructor;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public abstract class TupleSerializableBinding<T extends TupleSerializable>
	extends AbstractTupleBinding<T>
	{
	public TupleSerializableBinding()
		{
		}
	
	/** create a new instance of T */
	protected abstract T newInstance();
	
	@Override
	public T entryToObject(TupleInput in)
		{
		T o= newInstance();
		o.readFromTupleInput(in);
		return o;
		}

	@Override
	public void objectToEntry(T o, TupleOutput out)
		{
		o.writeToTupleOutpout(out);
		}
	}
