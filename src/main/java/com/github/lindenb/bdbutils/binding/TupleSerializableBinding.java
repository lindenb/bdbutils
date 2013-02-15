package com.github.lindenb.bdbutils.binding;

import java.lang.reflect.Constructor;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class TupleSerializableBinding<T extends TupleSerializable> extends AbstractTupleBinding<T> {
	private Constructor<T> ctor;
	
	public TupleSerializableBinding(Class<T> clazz)
		{
		try {
			this.ctor=clazz.getDeclaredConstructor();
			}
		catch (Exception e) {
			throw new RuntimeException(e);
			}
		}
	
	@Override
	public T entryToObject(TupleInput in)
		{
		try {
			T o=this.ctor.newInstance();
			o.readFromTupleInput(in);
			return o;
			}
		catch (Exception e) {
			throw new RuntimeException(e);
			}
		}

	@Override
	public void objectToEntry(T o, TupleOutput out)
		{
		o.writeToTupleOutpout(out);
		}

}
