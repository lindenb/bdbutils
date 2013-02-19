package com.github.lindenb.bdbutils.bio.interval;


import com.github.lindenb.bdbutils.util.ExtendedIterator;

public class BinIterator extends ExtendedIterator<Integer>
	{
	private int start;
	private int end;
	private int index=0;
	public BinIterator(int start,int end)
		{
		this.start=start;
		this.end=end;
		}


	
	@Override
	protected boolean getNext()
		{
		_next=null;
		int k;
		int j=0;
		int end = this.end;
		if (this.start >= end)
			{
			_next=null;
			return false;
			}
		if (end >= 1<<29) end = 1<<29;
		--end;
		if(index==0)
			{
			super._next=0;
			this.index=1;
			return true;
			}
		j=0;
		for (k =    1 + (this.start>>26); k <=    1 + (end>>26) && _next==null; ++k,++j) {j++; if(j>=this.index) {_next=k;this.index=j+1;break;}}
		for (k =    9 + (this.start>>23); k <=    9 + (end>>23) && _next==null; ++k,++j) {j++; if(j>=this.index) {_next=k;this.index=j+1;break;}}
		for (k =   73 + (this.start>>20); k <=   73 + (end>>20) && _next==null; ++k,++j) {j++; if(j>=this.index) {_next=k;this.index=j+1;break;}}
		for (k =  585 + (this.start>>17); k <=  585 + (end>>17) && _next==null; ++k,++j) {j++; if(j>=this.index) {_next=k;this.index=j+1;break;}}
		for (k = 4681 + (this.start>>14); k <= 4681 + (end>>14) && _next==null; ++k,++j) {j++; if(j>=this.index) {_next=k;this.index=j+1;break;}}
		return _next!=null;
		}
	
	public static void main(String[] args) {
		BinIterator iter=new BinIterator(1,1<<29 -1);
		while(iter.hasNext())
			{
			System.out.println(iter.next());
			}
		}
	}
