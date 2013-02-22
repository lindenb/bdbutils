package com.github.lindenb.bdbutils.util;

import java.io.File;
import java.io.IOException;


public class BerkeleyDbUtils 
	{
	public static long diskUsage(File f)
		{
		long S=0L;
		if(f.isDirectory())
			{
			for(File sub:f.listFiles())
				{
				S+=diskUsage(sub);
				}
			}
		else
			{
			S=f.length();
			}
		return S;
		}
	
	public static void createDbHomeIfNotExist(File dbHome) throws IOException
		{
		if(dbHome==null) throw new IllegalArgumentException("null argument.");
		if(dbHome.exists() )
			{
			if(!dbHome.isDirectory()) throw new IOException("Not a directory:"+dbHome);
			}
		else
			{
			if(!dbHome.mkdirs()) throw new IOException("Cannot create directory :" +dbHome);
			}
		}
	}
