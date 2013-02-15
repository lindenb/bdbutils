package com.github.lindenb.bdbutils.util;

/**
 * Interface used to encode the frequent words
 * in BDB. For example, the TAGS for XML could be
 * encoded as an integer instead of writing the string
 * each time
 * @author lindenb
 *
 */
public interface Dictionary
	{
	/** maximum number of items in the dictionary can be used to use writeByte or writeShort or writeInt*/
	public int size();
	/** returns the index for the specified key or -1 if not found */
	public int getIndex(String key);
	/** return the key for the specified index of null if not found */
	public String get(int index);
	}
