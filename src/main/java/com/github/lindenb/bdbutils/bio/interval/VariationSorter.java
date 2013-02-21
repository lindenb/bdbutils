package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.sort.AbstractSorterComparable;

public class VariationSorter extends AbstractSorterComparable<Variation>
	{
	
	@Override
	protected Variation translate(byte[] a, int offset, int len)
		{
		Variation v=new Variation();
		v.readFromBytes(a, offset, len);
		return v;
		}
	
	}
