package com.github.lindenb.bdbutils.binding;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import com.sleepycat.bind.tuple.TupleBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;



public class GZipBinding<T>
	extends AbstractTupleBinding<T>
	{
	private TupleBinding<T> delegate=null;
	
	public GZipBinding( TupleBinding<T> keyBinding )
		{
		this.delegate=delegate;
		}
	
	
	@Override
	public T entryToObject(TupleInput in)
		{
		int n=in.readInt();
		byte array[]=new byte[n];
		if(in.readFast(array,0,n)!=n)
			{
			throw new RuntimeException("cannot read array");
			}
		try
			{
			
			ByteArrayInputStream bais= new ByteArrayInputStream(array);
			GZIPInputStream gzin= new GZIPInputStream(bais);
			ByteArrayOutputStream baos=new ByteArrayOutputStream(array.length);
			copyTo(gzin,baos);
			gzin.close();
			bais.close();
			
			return this.delegate.entryToObject(new TupleInput(baos.toByteArray()));
			}
		catch(IOException err)
			{
			throw new RuntimeException(err);
			}

		
		}

	@Override
	public void objectToEntry(final T o, TupleOutput out)
		{
		TupleOutput to=new TupleOutput();
		this.delegate.objectToEntry(o, to);
		byte array[]=to.toByteArray();
		try
			{
			ByteArrayOutputStream baos=new ByteArrayOutputStream(array.length);
			GZIPOutputStream gzout= new GZIPOutputStream(baos);
			ByteArrayInputStream bais= new ByteArrayInputStream(array);
			copyTo(bais, gzout);
			gzout.finish();
			gzout.close();
			bais.close();
	
			byte compressed[]=baos.toByteArray();

			out.writeInt(compressed.length);
			out.writeFast(compressed,0,array.length);
			}
		catch(IOException err)
			{
			throw new RuntimeException(err);
			}
		}
	
	private void copyTo(InputStream in,OutputStream out) throws IOException
		{
		byte buff[]=new byte[256];
		int nread;
		while((nread=in.read(buff,0,buff.length))!=-1)
			{
			out.write(buff,0,nread);
			}
		}
	
	}
