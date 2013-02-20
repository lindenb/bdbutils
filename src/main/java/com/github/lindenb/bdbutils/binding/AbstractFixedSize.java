package com.github.lindenb.bdbutils.binding;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.DatabaseEntry;

public abstract class AbstractFixedSize implements FixedSize,TupleSerializable
	{
	public AbstractFixedSize()
		{
		}
	
	@Override
	public void readFromTupleInput(TupleInput in)
		{
		byte array[]=new byte[getSizeOf()];
		if(in.readFast(array)!=array.length) throw new RuntimeException();
		readFromBytes(array,0);
		}

	@Override
	public void writeToTupleOutpout(TupleOutput out)
		{
		byte array[]=new byte[getSizeOf()];
		writeToBytes(array, 0);
		out.writeFast(array);
		}

	public void readFromEntry(final DatabaseEntry entry)
		{
		readFromBytes(entry.getData(),0);
		}

	public void writeToEntry(DatabaseEntry entry)
		{
		byte array[]=new byte[getSizeOf()];
		writeToBytes(array,0);
		entry.setData(array);
		}
	
	public DatabaseEntry toDatabaseEntry()
		{
		DatabaseEntry E=new DatabaseEntry();
		writeToEntry(E);
		return E;
		}
	

	@Override
	public abstract int readFromBytes(byte[] array, int offset);
	@Override
	public abstract int writeToBytes(byte[] array, int offset);
	}
