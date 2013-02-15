package com.github.lindenb.bdbutils.db;

import com.sleepycat.je.Cursor;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;

public enum CursorMove {
NEXT,PREV,FIRST,LAST,NEXT_DUP,PREV_DUP,NEXT_NO_DUP,PREV_NO_DUP,SEARCH_KEY,SEARCH_KEY_RANGE,BOTH,BOTH_RANGE;

public OperationStatus move(Cursor c,DatabaseEntry key,DatabaseEntry data,LockMode lockMode)
	{
	switch(this)
		{
		case NEXT: return c.getNext(key, data, lockMode);
		case PREV: return c.getPrev(key, data, lockMode);
		case FIRST: return c.getFirst(key, data, lockMode);
		case LAST: return c.getLast(key, data, lockMode);
		case NEXT_DUP: return c.getNextDup(key, data, lockMode);
		case PREV_DUP: return c.getPrevDup(key, data, lockMode);
		case NEXT_NO_DUP: return c.getNextNoDup(key, data, lockMode);
		case PREV_NO_DUP: return c.getPrevNoDup(key, data, lockMode);
		case SEARCH_KEY: return c.getSearchKey(key, data, lockMode);
		case SEARCH_KEY_RANGE: return c.getSearchKeyRange(key, data, lockMode);
		case BOTH: return c.getSearchBoth(key, data, lockMode);
		case BOTH_RANGE: return c.getSearchBothRange(key, data, lockMode);
		default: throw new IllegalStateException(this.name());
		}
	}
}
