package com.github.lindenb.bdbutils.bio.interval;

import java.util.Comparator;

import com.github.lindenb.bdbutils.util.ByteUtils;

public class BedSegmentSorter
	implements Comparator<byte[]>
	{
	@Override
	public int compare(byte[] o1, byte[] o2) {
		int i=(int)ByteUtils.readByte(o1, 0) - (int)ByteUtils.readByte(o2, 0);
		if(i!=0) return i;
		i=(int)ByteUtils.readInt(o1, ByteUtils.BYTE_SIZE) - (int)ByteUtils.readInt(o2,  ByteUtils.BYTE_SIZE);
		if(i!=0) return i;
		i=(int)ByteUtils.readInt(o1, ByteUtils.BYTE_SIZE+ByteUtils.INT_SIZE) - (int)ByteUtils.readInt(o2,  ByteUtils.BYTE_SIZE+ByteUtils.INT_SIZE);
		return i;
		}
	}

