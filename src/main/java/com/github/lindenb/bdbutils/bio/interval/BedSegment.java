package com.github.lindenb.bdbutils.bio.interval;


public class BedSegment
	implements FixedSize,Cloneable
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
		this.tid=ByteUtils.writeByte(array,offset);
		offset+=Byte.SIZE;
		this.start=ByteUtils.writeInt(array,offset);
		offset+=Integer.SIZE;
		this.end=ByteUtils.writeInt(array,offset);
		offset+=Integer.SIZE;
		return offset;
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
	public int hashCode()
		{
		TODO
		}
	
	@Override
	public boolean equals(Object o)
		{
		if(this==o) return true;
		if(o==null || !(o instanceof BedSegment)) return false;
		BedSegment o=BedSegment.class.cast(o);
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

