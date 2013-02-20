package com.github.lindenb.bdbutils.bio.interval;

import java.util.ArrayList;
import java.util.List;

import com.github.lindenb.bdbutils.binding.AbstractFixedSize;
import com.github.lindenb.bdbutils.binding.FixedSize;
import com.github.lindenb.bdbutils.binding.TupleSerializable;
import com.github.lindenb.bdbutils.util.ByteUtils;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class TidBinPos
	extends AbstractFixedSize
	implements FixedSize,Cloneable,TupleSerializable
	{
	public static final int SIZEOF=ByteUtils.BYTE_SIZE+ByteUtils.INT_SIZE*2;
	private static final int MAX_BIN = 37450;
	static private final int binOffsets[] = {512+64+8+1, 64+8+1, 8+1, 1, 0};

	private byte tid;
	private int bin;
	private int pos;
	
	public TidBinPos(byte tid,int bin,int pos)
		{
		this.tid=tid;
		this.bin=bin;
		this.pos=pos;
		}
	
	public TidBinPos()
		{
		this((byte)-1,-1,-1);
		}
	@Override
	public TidBinPos clone()
		{
		return new TidBinPos(this.tid,this.bin,this.pos);
		}
	
	
	public byte getTid()
		{
		return this.tid;
		}
	
	public int getBin()
		{
		return this.bin;
		}
	
	public int getPos()
		{
		return this.pos;
		}
	
	static public int bin(int start,int end)
		{
		final int _binFirstShift=17;
		final int _binNextShift=3;
		
		int startBin = start, endBin = end-1, i;
		startBin >>= _binFirstShift;
		endBin >>= _binFirstShift;
		for (i=0; i< binOffsets.length; ++i)
		    {
		    if (startBin == endBin)
		        return binOffsets[i] + startBin;
		    startBin >>= _binNextShift;
		    endBin >>= _binNextShift;
		    }
		throw new IllegalArgumentException("out of range in findBin (max is 512M):"+ start+"-"+end);
		}

	
	public static List<Integer> reg2bin(final int beg, final int _end)
		{
		List<Integer> list=new ArrayList<Integer>(MAX_BIN);
		int  k, end = _end;
		if (beg >= end) throw new IllegalArgumentException("");
		if (end >= 1<<29) end = 1<<29;
		--end;
		list.add(0);
		for (k =    1 + (beg>>26); k <=    1 + (end>>26); ++k) list.add(k);
		for (k =    9 + (beg>>23); k <=    9 + (end>>23); ++k) list.add(k);
		for (k =   73 + (beg>>20); k <=   73 + (end>>20); ++k) list.add(k);
		for (k =  585 + (beg>>17); k <=  585 + (end>>17); ++k) list.add(k);
		for (k = 4681 + (beg>>14); k <= 4681 + (end>>14); ++k) list.add(k);
		return list;
		}
		
	/** reset this object with the input */
	@Override
	public int readFromBytes(final byte array[],int offset)
		{
		this.tid=ByteUtils.readByte(array,offset);
		offset+=ByteUtils.BYTE_SIZE;
		this.bin=ByteUtils.readInt(array,offset);
		offset+=ByteUtils.INT_SIZE;
		this.pos=ByteUtils.readInt(array,offset);
		offset+=ByteUtils.INT_SIZE;
		return offset;
		}
	
	/** write the content of this object to out */
	@Override
	public int writeToBytes(byte array[],int offset)
		{
		ByteUtils.writeByte(this.tid,array,offset);
		offset+=ByteUtils.BYTE_SIZE;
		ByteUtils.writeInt(this.bin,array,offset);
		offset+=ByteUtils.INT_SIZE;
		ByteUtils.writeInt(this.pos,array,offset);
		offset+=ByteUtils.INT_SIZE;
		return offset;
		}
	
	@Override
	public void readFromTupleInput(TupleInput in)
		{
		byte array[]=new byte[TidBinPos.SIZEOF];
		in.read(array);
		readFromBytes(array,0);
		}
	
	@Override
	public void writeToTupleOutpout(TupleOutput out)
		{
		byte array[]=new byte[TidBinPos.SIZEOF];
		writeToBytes(array,0);
		out.write(array);
		}
	
	
	/** sizeo of structure */
	public int getSizeOf()
		{
		return SIZEOF;
		}
		
	public int compare(final TidBinPos o)
		{
		int i=tid - o.tid;
		if(i!=0) return i;
		i=bin - o.bin;
		if(i!=0) return i;
		return pos - o .pos;
		}
		
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + bin;
		result = prime * result + pos;
		result = prime * result + tid;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TidBinPos o = (TidBinPos) obj;
		return  this.tid==o.tid &&
				this.bin==o.bin &&
				this.pos==o.pos;
	}

	
	@Override
	public String toString()
		{
		return "tid:"+ tid +" bin:"+bin+" pos:"+pos;
		}
	}

