package com.github.lindenb.bdbutils.db;

import com.sleepycat.je.Database;
import com.sleepycat.je.Environment;
import com.sleepycat.je.SecondaryDatabase;
import com.sleepycat.je.Transaction;

public interface SecondaryDatabaseFactory
	{
	public SecondaryDatabase openSecondaryDatabase(Transaction txn,Environment env,Database primaryDatabase);
	}
