package com.github.lindenb.bdbutils.util;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class CursorIterator extends ExtendedIterator<Pair<DatabaseEntry,DatabaseEntry>>
	{
	protected Cursor cursor;
	protected DatabaseEntry key;
	protected DatabaseEntry data;
	protected LockMode lockMode;
	
	public CursorIterator(
		Cursor cursor,
		DatabaseEntry key,
		DatabaseEntry data,
		 LockMode lockMode
		)
		{
		this.cursor=null;
		this.key=key;
		this.data=data;
		this.lockMode=lockMode;
		}
	
	protected boolean moveCursor()
		{
		if(this.cursor.getNext(key, data, lockMode)!=OperationStatus.SUCCESS) return false;
		return true;
		}
	
	@Override
	protected boolean getNext() {
		if(this.cursor==null) return false;
		if(!moveCursor()) return false;
		super._next=new Pair<DatabaseEntry, DatabaseEntry>(this.key, this.data);
		return true;
		}
	
	@Override
	public void close()
		{
		if(this.cursor!=null)
			{
			this.cursor.close();
			this.cursor=null;
			this.data=null;
			this.key=null;
			}
		super.close();
		}
	}
