package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.binding.FixedSizeBinding;


public class BedSegmentBinding
	extends FixedSizeBinding<BedSegment>
	{
	private final static BedSegmentBinding INSTANCE=new BedSegmentBinding();
	
	private BedSegmentBinding()
		{
		}
	
	public static BedSegmentBinding getInstance()
		{
		return INSTANCE;
		}	

	@Override
	protected BedSegment newInstance() {
		return new BedSegment();
		}
	}

