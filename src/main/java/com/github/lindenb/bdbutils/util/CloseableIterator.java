package com.github.lindenb.bdbutils.util;

import java.util.Iterator;

public interface CloseableIterator<E> extends Iterator<E>
	{
	public void close();
	}
