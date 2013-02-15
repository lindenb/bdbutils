package com.github.lindenb.bdbutils.binding;

import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

/**
 * Interface for objects that are able to write/read themselves to/from a DatabaseEntry
 * @author lindenb
 *
 */
public interface TupleSerializable
	{
	/** reset this object with the input */
	public void readFromTupleInput(TupleInput in);
	/** write the content of this object to out */
	public void writeToTupleOutpout(TupleOutput out);
	}
