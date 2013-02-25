package com.github.lindenb.bdbutils.db;


import com.sleepycat.bind.EntryBinding;
import com.sleepycat.bind.tuple.ByteBinding;
import com.sleepycat.je.DatabaseConfig;
import com.sleepycat.je.Environment;
import com.sleepycat.je.Transaction;

public class TemporaryKeySet<K> extends DatabaseWrapper<K,Byte>
	{
	private static long ID_GENERATOR=0L;
	private TemporaryKeySet()
		{
		setDataBinding(new ByteBinding());
		}
	@Override
	public DatabaseConfig createDefaultConfig()
		{
		DatabaseConfig cfg= super.createDefaultConfig();
		cfg.setTemporary(true);
		cfg.setAllowCreate(true);
		cfg.setExclusiveCreate(true);
		return cfg;
		}
	
	public boolean put(Transaction txn,K key)
		{
		return put(txn, key, (byte)1);
		}
	
	public static <T> TemporaryKeySet<T> createTemporaryKeySet(
			Environment env,
			Transaction txn,
			EntryBinding<T> binding
			)
		{
		String name="tmpDB_"+(++ID_GENERATOR)+"_"+System.currentTimeMillis()+"_"+(int)(Math.random()*1000.0);
		TemporaryKeySet<T> set=new TemporaryKeySet<T>();
		set.setName(name);
		set.setKeyBinding(binding);
		set.open(env, txn, null);
		return set;
		}
	
	}
