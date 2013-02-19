package com.github.lindenb.bdbutils.binding;

/**
 * Interface for objects having a fixed size when serialized to
 * an array of bytes and that are able to write/read themselves.
 * @author lindenb
 *
 */
public interface FixedSize
	{
	/** reset this object with the input */
	public int readFromBytes(final byte array[],int offset);
	/** write the content of this object to out */
	public int writeToBytes(byte array[],int offset);
	/** sizeo of structure */
	public int getSizeOf();
	}
