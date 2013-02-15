package com.github.lindenb.bdbutils.sort;

import java.lang.reflect.Constructor;

import com.github.lindenb.bdbutils.binding.TupleSerializable;
import com.sleepycat.bind.tuple.TupleInput;


public class SorterTupleSerializable<T extends Comparable<T> & TupleSerializable >
	extends AbstractSorterComparable<T>
		{
		private Constructor<T> ctor;
		public SorterTupleSerializable(Class<T> clazz)
			{
			try {
				this.ctor=clazz.getDeclaredConstructor();
				}
			catch (Exception e) {
				throw new RuntimeException(e);
				}
			}
		@Override
		protected T translate(byte[] a,int offset,int len)
			{
			try {
				T o=this.ctor.newInstance();
				o.readFromTupleInput(new TupleInput(a,offset,len));
				return o;
				}
			catch (Exception e) {
				throw new RuntimeException(e);
				}
			}
		}
