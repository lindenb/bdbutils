package com.github.lindenb.bdbutils.bio.interval;

import com.github.lindenb.bdbutils.binding.FixedSizeBinding;


public class TidBinPosBinding
	extends FixedSizeBinding<TidBinPos>
	{
	private final static TidBinPosBinding INSTANCE=new TidBinPosBinding();
	
	private TidBinPosBinding()
		{
		}
	
	public static TidBinPosBinding getInstance()
		{
		return INSTANCE;
		}	

	@Override
	protected TidBinPos newInstance() {
		return new TidBinPos();
		}
	}

