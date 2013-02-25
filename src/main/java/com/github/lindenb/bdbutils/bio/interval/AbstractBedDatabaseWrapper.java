package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.db.SecondaryDatabaseWrapper;
import com.sleepycat.je.DatabaseEntry;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.SecondaryKeyCreator;

public abstract class AbstractBedDatabaseWrapper<K, V> extends
		SecondaryDatabaseWrapper<TidBinPos, K, V>
	{
	/** set keyBinding to TidBinPosBinding */
	protected AbstractBedDatabaseWrapper()
		{
		setKeyBinding(TidBinPosBinding.getInstance());
		}
	
	/** extract the TidBinPos from the Key/Value */
	protected abstract TidBinPos extractTidBinPos(final K k,final V v);
	/** extract the TidBinPos from the Key/Value */
	protected TidBinPos extractTidBinPos(final DatabaseEntry k,final DatabaseEntry v)
		{
		return extractTidBinPos(
				getOwner().getKeyBinding().entryToObject(k),
				getOwner().getDataBinding().entryToObject(v)
				);
		}
	
	
	@Override
	public SecondaryConfig createDefaultConfig()
		{
		SecondaryConfig cfg= super.createDefaultConfig();
		cfg.setBtreeComparator(TidBinPosSorter.class);
		cfg.setSortedDuplicates(true);
		cfg.setKeyCreator(new SecondaryKeyCreator()
			{
			@Override
			public boolean createSecondaryKey(
					SecondaryDatabase db2,
					DatabaseEntry k,
					DatabaseEntry v,
					DatabaseEntry result
					)
				{
				TidBinPos tid=extractTidBinPos(k, v);
				if(tid!=null)
					{
					tid.writeToEntry(result);
					return true;
					}
				return false;
				}
			});
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
