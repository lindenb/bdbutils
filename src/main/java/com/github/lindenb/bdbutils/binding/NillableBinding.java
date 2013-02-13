package com.github.lindenb.bdbutils.binding;

import java.util.ArrayList;
import java.util.List;

import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class NillableBinding<T>
	extends AbstractTupleBinding<T>
	{
	private TupleBinding<T> delegate=null;
	
	public NillableBinding( TupleBinding<T> keyBinding )
		{
		this.delegate=delegate;
		}
	
	
	@Override
	public T entryToObject(TupleInput in)
		{
		boolean is_nil=in.readBoolean();
		if(is_nil) return null;
		return this.delegate.entryToObject(in);
		}

	@Override
	public void objectToEntry(final T o, TupleOutput out)
		{
		if(o==null)
			{
			out.writeBoolean(true);
			}
		else
			{
			out.writeBoolean(false);
			this.delegate.objectToEntry(o,out);
			}
		}
	}
