package com.github.lindenb.bdbutils.db;


import com.sleepycat.bind.EntryBinding;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryCursor;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Transaction;

public class SecondaryDatabaseWrapper<K,PKEY,V>
	extends AbstractDatabaseWrapper<K,V,SecondaryDatabase,SecondaryCursor,SecondaryConfig>
	{
	/** the secondary db */
	private SecondaryDatabase database=null;
	/** link to the primary database */
	private DatabaseWrapper<PKEY,V> primaryDb=null;
	
	
	@Override
	/** creates a default config for this database */
	public SecondaryConfig createDefaultConfig()
		{
		SecondaryConfig cfg=new SecondaryConfig();
		return cfg;
		}
	
	/** link to the primary database, it is set during the 'open' operation */
	public DatabaseWrapper<PKEY,V> getOwner()
		{
		return primaryDb;
		}
	
	/** get the binding for the primary key of the secondary database */
	public EntryBinding<PKEY> getPrimaryKeyDataBinding()
		{
		return getOwner().getKeyBinding();
		}
	
	@Override
	/** get the data binding, it is the dataBinding of the <b>Primary</b> database */
	public EntryBinding<V> getDataBinding()
		{
		if(getOwner()==null) throw new NullPointerException();
		if(getOwner().getDataBinding()==null) throw new NullPointerException();
		return getOwner().getDataBinding();
		}
	
	@Override
	/** returns the primary database */
	public SecondaryDatabase getDatabase()
		{
		return this.database;
		}
	
	
	
	@Override
	/** open a secondary cursor for this database */
	public SecondaryCursor openCursor(Transaction txn)
		{
		return getDatabase().openCursor(txn, createCursorConfig());
		}

	
	public SecondaryDatabaseWrapper<K,PKEY,V> open(
		Transaction txn,
		DatabaseWrapper<PKEY,V> primary,
		SecondaryConfig dbConfig
		)
		{
		
		if(this.database==null)
			{
			this.primaryDb=primary;
			if(dbConfig==null) dbConfig=createDefaultConfig();
			this.database=primary.getDatabase().getEnvironment().openSecondaryDatabase(
				txn,
				getName(),
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
