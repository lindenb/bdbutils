package com.github.lindenb.bdbutils.db;


import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Transaction;

public class SecondaryDatabaseWrapper<K,PKEY,V>
	extends AbstractDatabaseWrapper<K,V,SecondaryDatabase,SecondaryCursor>
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
		if(getOwner()==null) throw new NullPointerException();
		if(getOwner().getDataBinding()==null) throw new NullPointerException();
		return getOwner().getDataBinding();
		}
	
	@Override
	public SecondaryDatabase getDatabase()
		{
		return this.database;
		}
	
	@Override
	public SecondaryCursor openCursor(Transaction txn)
		{
		return getDatabase().openCursor(txn, null);
		}

	
	public SecondaryDatabaseWrapper<K,PKEY,V> open(
		Transaction txn,
		String databaseName,
		DatabaseWrapper<PKEY,V> primary,
		SecondaryConfig dbConfig
		)
		{
	
		if(this.database==null)
			{
			this.primaryDb=primary;
			this.database=primary.getDatabase().getEnvironment().openSecondaryDatabase(
				txn,
				databaseName,
				primary.getDatabase(),
				dbConfig
				);
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
