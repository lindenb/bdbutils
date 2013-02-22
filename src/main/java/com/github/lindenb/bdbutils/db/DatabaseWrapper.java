package com.github.lindenb.bdbutils.db;

import java.util.Comparator;

import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.Cursor;
import com.sleepycat.je.Database;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.Environment;
import com.sleepycat.je.LockMode;
import com.sleepycat.je.OperationStatus;
import com.sleepycat.je.Transaction;

public class DatabaseWrapper<K,V>
	extends AbstractDatabaseWrapper<K,V,Database,Cursor>
	{
	private Database database=null;
	private EntryBinding<V> dataBinding;
	
	public DatabaseWrapper()
		{

		}
	
	@Override
	public EntryBinding<V> getDataBinding()
		{
		return this.dataBinding;
		}
	
		
	public void setDataBinding(EntryBinding<V> dataBinding)
		{
		this.dataBinding=dataBinding;
		}
	
	@Override
	public Database getDatabase()
		{
		return database;
		}
	
	public DatabaseWrapper<K,V> open(Environment env,Transaction txn,String name,DatabaseConfig dbConfig)
		{
		 if(this.database==null)
		 	{
		 	this.database=env.openDatabase(txn,name,dbConfig) ;
		 	}
		return this;
		}
	
	
	
	@Override
	public void close()
		{
		if(this.database!=null)
			{
			this.database.close();
			this.database=null;
			}
		}
	

	public void clear(Transaction txn,LockMode lockMode)
		{
		Cursor c=null;
		DatabaseEntry k=new DatabaseEntry();
		DatabaseEntry v=new DatabaseEntry();
		try {
			c=openCursor(txn);
			while(c.getNext(k, v, lockMode)==OperationStatus.SUCCESS)
				{
				c.delete();
				}
			}
		finally
			{
			if(c!=null) c.close();
			}
		}

	

	
	public boolean put(Transaction txn,K k,V v)
		{
		return putEntries(txn, createKeyEntry(k), createDataEntry(v));
		}	

	public boolean putEntries(Transaction txn,DatabaseEntry k,DatabaseEntry v)
		{
		return this.getDatabase().put(txn, k, v)==OperationStatus.SUCCESS;
		}	

	
	@Override
	public Cursor openCursor(Transaction txn)
		{
		return getDatabase().openCursor(txn, null);
		}
	
	/** delete records from key(begin,end) */
	public boolean delete(Transaction txn,K key)
		{
		return getDatabase().delete(txn, createKeyEntry(key))==OperationStatus.SUCCESS;
		}
	
	/** delete records from key(begin,end). Returns the number of records deleted */
	public long delete(Transaction txn,K keyBegin,K keyEnd,boolean includeLast)
		{
		long n_removed=0;
		Cursor c=null;
		DatabaseEntry k=createKeyEntry(keyBegin);
		DatabaseEntry kE=createKeyEntry(keyEnd);
		Comparator<byte[]> cmp=getDatabase().getConfig().getBtreeComparator();
		DatabaseEntry v=new DatabaseEntry();
		try {
			c=openCursor(txn);
			CursorMove mover=CursorMove.SEARCH_KEY_RANGE;
			while(mover.move(c,k,v, LockMode.DEFAULT)==OperationStatus.SUCCESS)
				{
				int diff=cmp.compare(
						k.getData(),
						kE.getData()
						);
				if(diff>0 || (diff==0 && !includeLast)) break;
				if(c.delete()==OperationStatus.SUCCESS)
					{
					n_removed++;
					}
				mover=CursorMove.NEXT;
				}
			}
		finally
			{
			if(c!=null) c.close();
			}
		return n_removed;
		}
	}
