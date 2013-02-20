package com.github.lindenb.bdbutils.bio.interval;

import java.util.List;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

import com.github.lindenb.bdbutils.util.CursorIterator;

public class TidBinIterator<CTYPE extends Cursor> extends CursorIterator<CTYPE>
	{
	private byte tid;
	private int chromStart;
	private int chromEnd;
	private List<Integer> bins;
	private int binIndex=-1;
	private boolean first_call=true;
	
	public TidBinIterator(
		CTYPE cursor,
		byte tid,
		int chromStart,
		int chromEnd,
		 LockMode lockMode
		)
		{
		super(cursor,new DatabaseEntry(),new DatabaseEntry(),lockMode);
		this.tid=tid;
		this.chromStart=chromStart;
		this.chromEnd=chromEnd;
		this.bins=TidBinPos.reg2bin(this.chromStart, this.chromEnd);
		}
	
	protected boolean moveCursor()
		{
		for(;;)
			{
			OperationStatus status;
			if(first_call)
				{
				binIndex++;
				if(binIndex>=this.bins.size()) return false;
				TidBinPos ref=new TidBinPos(this.tid,this.bins.get(binIndex),0);
				TidBinPosBinding.getInstance().objectToEntry(ref, super.key);
				status=super.cursor.getSearchKey(super.key, super.data, lockMode);
				first_call=false;
				}
			else
				{
				status=super.cursor.getNext(key, data, lockMode);
				}
			
			if(status!=OperationStatus.SUCCESS)
				{
				first_call=true;
				continue;
				}
			
			TidBinPos tbp=TidBinPosBinding.getInstance().entryToObject(super.key);
			if(tbp.getTid()< this.tid) continue;
			if(tbp.getTid()> this.tid)
				{
				return false;
				}
			if(tbp.getBin()< this.bins.get(binIndex)) continue;
			if(tbp.getBin()> this.bins.get(binIndex))
				{
				first_call=true;
				continue;
				}
			if(this.chromStart > tbp.getPos()) continue;
			if(this.chromEnd <= tbp.getPos()) continue;
			
			return true;
			}
	
		}
	
	}
