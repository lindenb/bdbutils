package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.binding.AbstractTupleSerializable;
import com.github.lindenb.bdbutils.util.ByteUtils;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class Variation
	extends AbstractTupleSerializable
	implements Comparable<Variation>
	{
	private byte tid;
	private int pos1;
	private String ref;
	private String alt;
	
	public Variation()
		{
		this.tid=(byte)-1;
		this.pos1=-1;
		this.ref="";
		this.alt="";
		}
	
	public Variation(byte tid,int pos1,String ref,String alt)
		{
		this.tid=tid;
		this.pos1=pos1;
		this.ref=ref;
		this.alt=alt;
		}
	
	
	public byte getTid()
		{
		return tid;
		}

	public int getPos1()
		{
		return pos1;
		}

	public String getRef()
		{
		return ref;
		}

	public String getAlt()
		{
		return alt;
		}

	@Override
	public void readFromTupleInput(TupleInput in)
		{
		this.tid=in.readByte();
		this.pos1=in.readInt();
		int c;
		StringBuilder b=new StringBuilder(10);
		while((c=in.read())!=-1 && c!='\0')
			{
			b.append((char)c);
			}
		ref=b.toString();
		b=new StringBuilder(10);
		while((c=in.read())!=-1 && c!='\0')
			{
			b.append((char)c);
			}
		alt=b.toString();
		}
	
	@Override
	public void writeToTupleOutpout(TupleOutput out)
		{
		out.makeSpace(ByteUtils.INT_SIZE+(ByteUtils.BYTE_SIZE*(3+this.alt.length()+this.ref.length())));
		out.writeByte(this.tid);
		out.writeInt(this.pos1);
		for(int i=0;i< this.alt.length();++i)
			{
			out.write((byte)this.alt.charAt(i));
			}
		out.write('\0');
		
		for(int i=0;i< this.ref.length();++i)
			{
			out.write((byte)this.ref.charAt(i));
			}
		out.write('\0');
		}
	
	public BedSegment toBedSegment()
		{
		return new BedSegment(this.tid,this.pos1-1,this.pos1);
		}
	
	@Override
	public int compareTo(Variation o)
		{
		int i=this.tid - o.tid;
		if(i!=0) return i;
		 i=this.pos1 - o.pos1;
		if(i!=0) return i;
		i=this.ref.compareTo(o.ref);
		if(i!=0) return i;
		return this.alt.compareTo(o.alt);
		}
	
	@Override
	public int hashCode()
		{
		final int prime = 31;
		int result = 1;
		result = prime * result + alt.hashCode();
		result = prime * result + pos1;
		result = prime * result + ref.hashCode();
		result = prime * result + tid;
		return result;
		}

	@Override
	public boolean equals(Object obj)
		{
		if (this == obj) { return true; }
		if (obj == null) { return false; }
		if (getClass() != obj.getClass()) { return false; }
		Variation other = (Variation) obj;
		if (tid != other.tid) { return false; }
		if (pos1 != other.pos1) { return false; }
		if (!alt.equals(other.alt)) { return false; }
		if (!ref.equals(other.ref)) { return false; }
		return true;
		}

	@Override
	public String toString()
		{
		return String.valueOf(this.tid)+":"+this.pos1+"["+this.ref+"/"+this.alt+"]";
		}
	
	}
