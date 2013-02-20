package com.github.lindenb.bdbutils.db;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.github.lindenb.bdbutils.util.CloseableIterator;
import com.github.lindenb.bdbutils.util.CursorIterator;
import com.github.lindenb.bdbutils.util.EqualRangeCursorIterator;
import com.github.lindenb.bdbutils.util.Function;
import com.github.lindenb.bdbutils.util.Pair;
import com.github.lindenb.bdbutils.util.TransformIterator;
import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

public abstract class AbstractDatabaseWrapper<K,V,DBTYPE extends Database,CURSORTYPE extends Cursor>
	implements Iterable<Pair<K, V>>
	{
	private EntryBinding<K> keyBinding;

	
	protected AbstractDatabaseWrapper()
		{

		}
	

	
	protected abstract DBTYPE getDatabase();
	
	public boolean isOpen()
		{
		return getDatabase()!=null;
		}
	
	public abstract void close();
	
	public EntryBinding<K> getKeyBinding()
		{
		return this.keyBinding;
		}
	
	public void setKeyBinding(EntryBinding<K> keyBinding)
		{
		this.keyBinding=keyBinding;
		}
	
	public abstract EntryBinding<V> getDataBinding();
	
	
	private <X> DatabaseEntry createEntry(EntryBinding<X> bind,X x)
		{
		DatabaseEntry e=new DatabaseEntry();
		bind.objectToEntry(x, e);
		return e;
		}
	
	public boolean containsKey(Transaction txn,K k,LockMode lck)
		{
		return containsKeyEntry(txn, createKeyEntry(k),lck);
		}
	
	public boolean containsKeyEntry(Transaction txn,DatabaseEntry k,LockMode lck)
		{
		return getEntry(txn,k,lck)!=null;
		}

	
	public V get(Transaction txn,K k,LockMode lck)
		{
		DatabaseEntry e=getEntry(txn, createKeyEntry(k), lck);
		return e==null?null:getDataBinding().entryToObject(e);
		}
	
	public List<V> getAll(Transaction txn,K k,LockMode lockMode)
		{
		List<V> L=new ArrayList<V>();
		CURSORTYPE c=null;
		DatabaseEntry key=new DatabaseEntry();
		DatabaseEntry value=new DatabaseEntry();
		try
			{
			c=openCursor(txn);
			keyToEntry(k, key);
			CursorMove move=CursorMove.SEARCH_KEY;
			while(move.move(c, key, value, lockMode)==OperationStatus.SUCCESS)
				{
				L.add(entryToData(value));
				move=CursorMove.NEXT_DUP;
				}
			}
		finally
			{
			if(c!=null) c.close();
			}
		return L;
		}
	
	
	public DatabaseEntry getEntry(Transaction txn,DatabaseEntry k,LockMode lck)
		{
		DatabaseEntry e= new DatabaseEntry();
		if( getDatabase().get(txn, k,e,lck)==OperationStatus.SUCCESS) return e;
		return null;
		}
	
	public DatabaseEntry createKeyEntry(K k)
		{
		return createEntry(getKeyBinding(),k);
		}
	
	public DatabaseEntry createDataEntry(V v)
		{
		return createEntry(getDataBinding(),v);
		}

	protected void keyToEntry(final K key,DatabaseEntry e)
		{
		getKeyBinding().objectToEntry(key, e);
		}
	
	protected void dataToEntry(final V v,DatabaseEntry e)
		{
		getDataBinding().objectToEntry(v, e);
		}

	protected K entryToKey(final DatabaseEntry e)
		{
		return getKeyBinding().entryToObject(e);
		}
	
	protected V entryToData(final DatabaseEntry e)
		{
		return getDataBinding().entryToObject(e);
		}

	
	protected Function<Pair<DatabaseEntry,DatabaseEntry>,Pair<K,V>> createFunction()
		{
		return new Function<Pair<DatabaseEntry,DatabaseEntry>, Pair<K,V>>()
				{
				@Override
				public Pair<K, V> apply(
						Pair<DatabaseEntry, DatabaseEntry> from
						)
					{
					return new Pair<K,V>(
							getKeyBinding().entryToObject(from.getKey()),
							getDataBinding().entryToObject(from.getValue())
							);
					}
				};
		}
	
	
	public abstract CURSORTYPE openCursor(Transaction txn);
	
	public Iterator<Pair<DatabaseEntry,DatabaseEntry>> entriesIterator(Transaction txn,LockMode lockMode)
		{
		CURSORTYPE c=openCursor(txn);
		DatabaseEntry data=new DatabaseEntry();
		DatabaseEntry key=new DatabaseEntry();
		return new CursorIterator<CURSORTYPE>(c, key, data, lockMode);
		}
	
	public CloseableIterator<Pair<K, V>> iterator(Transaction txn,LockMode lockMode)
		{
		return new TransformIterator<Pair<DatabaseEntry,DatabaseEntry>,Pair<K,V> >(
				entriesIterator(txn, lockMode), 
				createFunction()
				);
		}
	
	
	
	public  CloseableIterator<Pair<K,V>> between(
			Transaction txn,
			K min,
			K max,
			boolean includeLast,
			LockMode lockMode
			)
		{
		return new TransformIterator<Pair<DatabaseEntry,DatabaseEntry>,Pair<K,V> >( betweenEntries(txn,
				createKeyEntry(min),
				createKeyEntry(max),
				includeLast,
				lockMode), 
				createFunction()
				);
		}

	
	
	public  Iterator<Pair<DatabaseEntry, DatabaseEntry>> betweenEntries(
			Transaction txn,
			DatabaseEntry min,
			DatabaseEntry max,
			boolean includeLast,
			LockMode lockMode
			)
		{
		CURSORTYPE c=openCursor(txn);
		DatabaseEntry data=new DatabaseEntry();
		return new EqualRangeCursorIterator<CURSORTYPE>(c,min,max,includeLast,data,lockMode);
		}
	
	
	@Override
	public CloseableIterator<Pair<K, V>> iterator()
		{
		return iterator(null,null);
		}
	}
