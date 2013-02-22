package com.github.lindenb.bdbutils.db;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.github.lindenb.bdbutils.util.BerkeleyDbUtils;
import com.sleepycat.je.Environment;
import com.sleepycat.je.EnvironmentConfig;

public class EnvironmentWrapper
	{
	private Environment environment=null;
	protected List<DatabaseWrapper<?, ?>> databases=new ArrayList<DatabaseWrapper<?,?>>();
	
	public void open(File dir,EnvironmentConfig cfg)
		{
		if(this.environment!=null) return;
		this.environment=new Environment(dir, cfg);
		}
	
	public Environment getEnvironment()
		{
		return environment;
		}
	
	public boolean isOpen()
		{
		return this.environment!=null;
		}
	
	public static long du(File home)
		{
		return BerkeleyDbUtils.diskUsage(home);
		}
	
	public static String DirUsage(File home)
		{
		return String.valueOf(du(home));
		}
	
	protected void closeDatabases()
		{
		for(DatabaseWrapper<?, ?> db:databases)
			{
			db.close();
			}
		}
	
	public void close()
		{
		closeDatabases();
		if(this.environment!=null)
			{
			try {this.environment.close();}catch(Exception err){}
			this.environment=null;
			}
		}
	@Override
	public String toString()
		{
		return "Environment:"+
			(this.environment!=null?
			this.environment.getHome().toString()
			:
			"closed environment"
			);
		}
	}
