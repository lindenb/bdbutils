package com.github.lindenb.bdbutils.util;

import java.util.Comparator;

import com.github.lindenb.bdbutils.db.CursorMove;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public class EqualRangeCursorIterator<T extends Cursor> extends CursorIterator<T>
	{
	private CursorMove cursorMove=CursorMove.SEARCH_KEY_RANGE;
	private DatabaseEntry max;
	private boolean includeLast;
	private Comparator<byte[]> comparator;
	public EqualRangeCursorIterator(
			T cursor,
			DatabaseEntry min,
			DatabaseEntry max,
			boolean includeLast,
			DatabaseEntry data,
			LockMode lockMode
			)
		{
		super(cursor, min, data, lockMode);
		this.max=max;
		this.includeLast=includeLast;
		this.comparator=cursor.getDatabase().getConfig().getBtreeComparator();
		}
	
	@Override
	protected boolean moveCursor()
		{
		OperationStatus status=this.cursorMove.move(super.cursor, super.key, super.data, super.lockMode);
		this.cursorMove=CursorMove.NEXT;
		if(status!=OperationStatus.SUCCESS) return false;
		int i=this.comparator.compare(this.key.getData(),this.max.getData());
		if(i>0  || (i==0 && !includeLast)) return false;
		return true;
		}
	}
