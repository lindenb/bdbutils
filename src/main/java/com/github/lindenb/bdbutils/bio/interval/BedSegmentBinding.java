package com.github.lindenb.bdbutils.bio.interval;


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

	@override
	protected T newInstance();
		{
		return new BedSegment();
		}
	}

