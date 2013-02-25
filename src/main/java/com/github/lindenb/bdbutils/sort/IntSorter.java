package com.github.lindenb.bdbutils.sort;

import java.util.Comparator;

import com.github.lindenb.bdbutils.util.ByteUtils;



public class IntSorter
	implements Comparator<byte[]>
	{
	@Override
	public int compare(byte[] arg0, byte[] arg1)
		{
		int i0=ByteUtils.readInt(arg0, 0);
		int i1=ByteUtils.readInt(arg1, 0);
		return i0-i1;
		}
	}
