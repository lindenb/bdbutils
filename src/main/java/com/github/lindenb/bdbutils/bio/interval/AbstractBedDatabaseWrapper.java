package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.db.SecondaryDatabaseWrapper;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryConfig;

public class AbstractBedDatabaseWrapper<K, V> extends
		SecondaryDatabaseWrapper<TidBinPos, K, V>
	{
	/** set keyBinding to TidBinPosBinding */
	protected AbstractBedDatabaseWrapper()
		{
		setKeyBinding(TidBinPosBinding.getInstance());
		}
	
	
	@Override
	public SecondaryConfig createDefaultConfig()
		{
		SecondaryConfig cfg= super.createDefaultConfig();
		cfg.setBtreeComparator(TidBinPosSorter.class);
		cfg.setSortedDuplicates(true);
		return cfg;	
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
