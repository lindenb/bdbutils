package com.github.lindenb.bdbutils.binding;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;
import com.sleepycat.je.DatabaseEntry;

public abstract class AbstractTupleSerializable implements TupleSerializable
	{
	public AbstractTupleSerializable()
		{
		}
		
	@Override
	public abstract void readFromTupleInput(TupleInput in);

	@Override
	public abstract void writeToTupleOutpout(TupleOutput out);

	public void readFromEntry(final DatabaseEntry entry)
		{
		readFromBytes(entry.getData(), entry.getOffset(), entry.getSize());
		}

	public void writeToEntry(DatabaseEntry entry)
		{
		TupleOutput t=new TupleOutput();
		writeToTupleOutpout(t);
		entry.setData(t.getBufferBytes());
		}
	
	public DatabaseEntry toDatabaseEntry()
		{
		DatabaseEntry E=new DatabaseEntry();
		writeToEntry(E);
		return E;
		}
	
	public void readFromBytes(final byte array[],int offset,int length)
		{
		TupleInput t=new TupleInput(array,offset,length);
		readFromTupleInput(t);
		}
	
	}
