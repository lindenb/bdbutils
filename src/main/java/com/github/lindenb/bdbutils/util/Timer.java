package com.github.lindenb.bdbutils.util;

import java.io.File;

public class Timer
	{
	private long start=System.currentTimeMillis();
	private long ninserted=0L;
	private long nExpect=-1L;
	private File bdbHome=null;
	private long startDiskUsage=-1L;

	private static long du(File f)
		{
		return BerkeleyDbUtils.diskUsage(f);
		}
	
	private static String duStr(long diskusage)
		{
		double unitSize = diskusage;
		String unit = "b";

        if ( unitSize > 1024 ) {
        	unitSize = unitSize / 1024; // kb
            unit = "Kb";

            if ( unitSize > 1024 ) {
            	unitSize /= 1024;
                unit = "Mb";

                if ( unitSize > 1024 ) {
                	unitSize /= 1024; 
                    unit = "Gb";

                    if ( unitSize > 1024 ) {
                    	unitSize /= 1024; 
                        unit = "Tb";
                    }
                }
            }
	        }
        return String.format("%3.1f %s", unitSize, unit);
		}
	
	public void setExpectedSize(long nExpect)
		{
		this.nExpect = nExpect;
		}
	
	public void insert()
		{
		ninserted++;
		}
	
	public void setBdbHome(File bdbHome)
		{
		this.bdbHome = bdbHome;
		if(bdbHome!=null) this.startDiskUsage=du(bdbHome);
		}
	
	@Override
	public String toString()
		{
		double secondPerRecord = ((System.currentTimeMillis() - this.start)/1000.0)/ninserted;
		StringBuilder b=new StringBuilder();
		b.append(ninserted);
		if(this.nExpect!=-1L)
			{
			b.append(" / ");
			b.append(this.nExpect);
			b.append(" (");
			b.append(String.format("%4.1f",(ninserted/(double)nExpect)*100.0));
			b.append(" %). ");
			}
		b.append(String.format(" %6.1f records/seconds. ",1.0/secondPerRecord));
		
		if(this.nExpect!=-1L)
			{
			long recRemains=this.nExpect-this.ninserted;
			double timeInSeconds=recRemains*secondPerRecord;
			double unitTime = timeInSeconds;
			String unit = "s";

	        if ( timeInSeconds > 120 ) {
	            unitTime = timeInSeconds / 60; // minutes
	            unit = "m";

	            if ( unitTime > 120 ) {
	                unitTime /= 60; // hours
	                unit = "h";

	                if ( unitTime > 100 ) {
	                    unitTime /= 24; // days
	                    unit = "d";

	                    if ( unitTime > 20 ) {
	                        unitTime /= 7; // days
	                        unit = "w";
	                    }
	                }
	            }
		        }
	        b.append( String.format(" Time remaining:%3.1f %s", unitTime, unit));
			}
		if(this.bdbHome!=null)
			{	
			long u=du(this.bdbHome);
	        b.append(" Disk Usage: ");
	        b.append(duStr(u));
	        b.append(".");
	        if(this.nExpect!=-1L && this.ninserted>0)
	        	{
	        	double bytesPerRec=(u-this.startDiskUsage)/this.ninserted;
	        	double expectUsage=bytesPerRec*this.nExpect;
		        b.append(" Expect Disk Usage: ");
		        b.append(duStr(this.startDiskUsage+(long)expectUsage));
	        	}
			}
		b.append(" Free Memory : ");
		b.append(duStr(Runtime.getRuntime().freeMemory()));
		 b.append(".");
		return b.toString();
		}
	}
