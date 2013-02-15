package com.github.lindenb.bdbutils.util;

import java.util.Iterator;

public class TransformIterator<F,T> extends ExtendedIterator<T>
	{
	private Function<F, T> transform;
	private Iterator<F> delegate;
	public TransformIterator(Iterator<F> delegate, Function<F, T> transform)
		{
		this.delegate=delegate;
		this.transform=transform;
		}
	@Override
	protected boolean getNext()
		{
		if(delegate==null || !delegate.hasNext()) return false;
		super._next=this.transform.apply(this.delegate.next());
		return true;
		}
	@Override
	public void close() {
		if(delegate!=null && delegate instanceof CloseableIterator)
			{
			CloseableIterator.class.cast(delegate).close();
			}
		delegate=null;
		super.close();
		}	
	}
