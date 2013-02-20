package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.db.DatabaseWrapper;
import com.github.lindenb.bdbutils.db.SecondaryDatabaseWrapper;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.Transaction;

public class AbstractBedDatabaseWrapper<K, V> extends
		SecondaryDatabaseWrapper<TidBinPos, K, V>
	{
	/** set keyBinding to TidBinPosBinding */
	protected AbstractBedDatabaseWrapper()
		{
		setKeyBinding(TidBinPosBinding.getInstance());
		}
	
	@Override
	/** set setBtreeComparator to TidBinPosSorter and sortedDuplicate=true */
	public SecondaryDatabaseWrapper<TidBinPos, K, V> open(Transaction txn,
			String databaseName, DatabaseWrapper<K, V> primary,
			SecondaryConfig dbConfig)
		{
		dbConfig.setBtreeComparator(TidBinPosSorter.class);
		dbConfig.setSortedDuplicates(true);
		super.open(txn, databaseName, primary, dbConfig);
		return this;
		}
	
	@Override
	protected void keyToEntry(TidBinPos key, DatabaseEntry e) {
		key.writeToEntry(e);
		}
	
	@Override
	protected TidBinPos entryToKey(DatabaseEntry e) {
		TidBinPos t=new TidBinPos();
		t.readFromEntry(e);
		return t;
		}
	
	}
