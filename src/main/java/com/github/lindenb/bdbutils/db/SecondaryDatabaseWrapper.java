package com.github.lindenb.bdbutils.db;

import java.util.Iterator;

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

public class SecondaryDatabaseWrapper<K,PKEY,V>
	extends AbstractDatabaseWrapper<K,V>
	{
	private SecondaryDatabase database=null;
	private DatabaseWrapper<PKEY,V> primaryDb=null;
	
	
	public DatabaseWrapper<PKEY,V> getOwner()
		{
		return primaryDb;
		}
	
	
	public EntryBinding<PKEY> getPrimaryKeyDataBinding()
		{
		return getOwner().getKeyBinding();
		}
	
	@Override
	public EntryBinding<V> getDataBinding()
		{
		return getOwner().getDataBinding();
		}
	
	
	public SecondaryDatabase getDatabase()
		{
		return this.database;
		}
	
	public SecondaryDatabaseWrapper<K,V,PKEY> open(
		Transaction txn,
		String databaseName,
		DatabaseWrapper<PKEY,V> primary,
		SecondaryConfig dbConfig
		)
		{
		if(this.database!=null)
			{
			this.database=openSecondaryDatabase(
				txn,
				String databaseName,
				primary.getDatabase(),
				SecondaryConfig dbConfig
				);
			this.primaryDb=primary;
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
	}
