package com.github.lindenb.bdbutils.db;

import com.sleepycat.je.Database;
import com.sleepycat.je.Environment;
import com.sleepycat.je.SecondaryConfig;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Transaction;

public class DefaultSecondaryDatabaseFactory
	implements SecondaryDatabaseFactory
	{
	private SecondaryConfig cfg=new SecondaryConfig();
	private String name=null;
	public DefaultSecondaryDatabaseFactory()
		{
		cfg.setAllowCreate(true);
		cfg.setReadOnly(false);
		}
	
	public void setConfig(SecondaryConfig cfg)
		{
		this.cfg = cfg;
		}
	
	public SecondaryConfig getConfig()
		{
		return cfg;
		}

	public void setName(String name)
		{
		this.name = name;
		}
	
	public String getName() {
		return name;
		}
	
	
	@Override
	public SecondaryDatabase openSecondaryDatabase(Transaction txn,Environment env,Database primaryDatabase)
		{
		SecondaryDatabase db=env.openSecondaryDatabase(txn, getName(), primaryDatabase, getConfig().clone());
		return db;
		}
	}
