package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.binding.FixedSize;
import com.github.lindenb.bdbutils.binding.TupleSerializable;
import com.github.lindenb.bdbutils.util.ByteUtils;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;


public class BedSegment
	implements FixedSize,Cloneable,TupleSerializable
	{
	public static final int SIZEOF=Integer.SIZE*2+Byte.SIZE;
	private byte tid;
	private int start;
	private int end;
	
	public BedSegment(byte tid,int start,int end)
		{
		this.tid=tid;
		this.start=start;
		this.end=end;
		}
	
	public BedSegment()
		{
		this((byte)-1,-1,-1);
		}
	@Override
	public BedSegment clone()
		{
		return new BedSegment(this.tid,this.start,this.end);
		}
	
	
	public byte getTid()
		{
		return this.tid;
		}
	
	public int getStart()
		{
		return this.start;
		}
	
	public int getEnd()
		{
		return this.end;
		}
	
		
	/** reset this object with the input */
	@Override
	public int readFromBytes(final byte array[],int offset)
		{
		this.tid=ByteUtils.readByte(array,offset);
		offset+=Byte.SIZE;
		this.start=ByteUtils.readInt(array,offset);
		offset+=Integer.SIZE;
		this.end=ByteUtils.readInt(array,offset);
		offset+=Integer.SIZE;
		return offset;
		}
	
	/** write the content of this object to out */
	@Override
	public int writeToBytes(byte array[],int offset)
		{
		ByteUtils.writeByte(this.tid,array,offset);
		offset+=Byte.SIZE;
		ByteUtils.writeInt(this.start,array,offset);
		offset+=Integer.SIZE;
		ByteUtils.writeInt(this.end,array,offset);
		offset+=Integer.SIZE;
		return offset;
		}
	
	@Override
	public void readFromTupleInput(TupleInput in)
		{
		byte array[]=new byte[BedSegment.SIZEOF];
		in.read(array);
		readFromBytes(array,0);
		}
	
	@Override
	public void writeToTupleOutpout(TupleOutput out)
		{
		byte array[]=new byte[BedSegment.SIZEOF];
		writeToBytes(array,0);
		out.write(array);
		}
	
	public int getBin()
		{
		return TidBinPos.bin(this.start, this.end);
		}
	
	public TidBinPos toTidBinPos()
		{
		return new TidBinPos(getTid(), getBin(), getStart());
		}
	
	/** sizeo of structure */
	public int getSizeOf()
		{
		return SIZEOF;
		}
		
	public int compare(final BedSegment o)
		{
		int i=tid - o.tid;
		if(i!=0) return i;
		i=start - o.start;
		if(i!=0) return i;
		return end - o .end;
		}
		
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + end;
		result = prime * result + start;
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
		BedSegment o = (BedSegment) obj;
		return  this.tid==o.tid &&
				this.start==o.start &&
				this.end==o.end;
	}

	
	@Override
	public String toString()
		{
		return String.valueOf(tid)+":"+start+"-"+end;
		}
	}

