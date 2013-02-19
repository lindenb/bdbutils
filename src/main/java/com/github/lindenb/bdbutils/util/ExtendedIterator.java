package com.github.lindenb.bdbutils.util;

import java.util.ArrayList;
import java.util.List;

public class ExtendedIterator<T> implements CloseableIterator<T>
	{
	protected boolean _hasNextCalled=false;
	protected boolean _hasNext=false;
	protected T _next=null;
	protected boolean closed_flag=false;
	private Predicate<T> predicate=null;
	
	public void setPredicate(Predicate<T> predicate)
		{
		this.predicate = predicate;
		}
	
	protected boolean accept(T v)
		{
		return predicate==null?true:predicate.apply(v);
		}
	
	protected boolean getNext()
		{	
		_next=null;
		return false;
		}

	
	protected boolean getFilteredNext()
		{
		if(closed_flag) return false;
		while(getNext())
			{
			if(accept(this._next)) return true;
			}
		return false;
		}
	
	@Override
	public boolean hasNext()
		{
		if(_hasNextCalled) return _hasNext;
		_hasNextCalled=true;
		if(closed_flag) return false;
		_next=null;
		_hasNext=getFilteredNext();
		if(!_hasNext)
			{
			close();
			}
		return _hasNext;
		}

	@Override
	public T next()
		{
		if(!hasNext()) throw new IllegalStateException();
		T r=_next;
		_next=null;
		_hasNextCalled=false;
		_hasNext=false;
		return r;
		}
	
	public T peek()
		{
		if(!hasNext()) return null;
		return _next;
		}

	@Override
	public void remove()
		{
		throw new UnsupportedOperationException();
		}

	@Override
	public void close()
		{
		closed_flag=true;
		}
	@Override
	protected void finalize() throws Throwable
		{
		close();
		super.finalize();
		}
	
	public List<T> asList()
		{
		List<T> L=new ArrayList<T>();
		while(this.hasNext())
			{
			L.add(this.next());
			}
		return L;
		}
	
	}
