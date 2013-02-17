package com.github.lindenb.bdbutils.util;

public class ByteUtils
	{
	public ByteUtils()
		{
		}
	
	public static int writeByte(byte val, byte array[],int offset)
		{
        	return writeUnsignedByte(val ^ 0x80,array,offset);
        	}
        	
 	public static int writeUnsignedByte(byte val, byte array[],int offset)
		{
		array[offset++] = (byte) b;
        	return offset;
        	}      
        	
         public static int write(final byte arrayFrom[],byte arrayTo[],int offsetTo)
		{
		return write(arrayFrom,0,arrayTo,offsetTo,arrayFrom.length);
        	}
        
        public static int write(final byte arrayFrom[],int offsetFrom, byte arrayTo[],int offsetTo,int len)
		{
		System.arrayCopy(arrayFrom,offsetFrom,arrayTo,offsetTo,len);
        	return offsetTo+len;
        	}	
        
        
	public static int writeBoolean(boolean val,byte arrayTo[],int offsetTo)
		{
        	array[offset++] =(val ? (byte)1 : (byte)0);
        	return offset;
    		}
        
        public static int writeShort(int val,byte arrayTo[],int offsetTo)
        	{
		return writeUnsignedShort(val ^ 0x8000,arrayTo,offsetTo);
    		}	
        	
        public final int writeUnsignedShort(int val,byte arrayTo[],int offsetTo)
		{
		arrayTo[offsetTo++] = (byte) (val >>> 8);
		arrayTo[offsetTo++] = (byte) val;
		return offsetTo;
	        }
        public static int writeInt(int val,byte arrayTo[],int offsetTo)
        	{
    		return writeUnsignedInt(val ^ 0x80000000,arrayTo,offsetTo);
    		}
    
        public static int writeUnsignedInt(long val,byte arrayTo[],int offsetTo)
        	{
		arrayTo[offsetTo++] =((byte) (val >>> 24));
		arrayTo[offsetTo++] =((byte) (val >>> 16));
		arrayTo[offsetTo++] =((byte) (val >>> 8));
		arrayTo[offsetTo++] =((byte) val);
		return offsetTo;
	    	}
	public static int writeLong(long val,byte arrayTo[],int offsetTo)
		{
        	return writeUnsignedLong(val ^ 0x8000000000000000L,arrayTo,offsetTo);
    		}
    
	public static int writeUnsignedLong(long val,byte arrayTo[],int offsetTo)
	    	{
		arrayTo[offsetTo++] =((byte) (val >>> 56));
		arrayTo[offsetTo++] =((byte) (val >>> 48));
		arrayTo[offsetTo++] =((byte) (val >>> 40));
		arrayTo[offsetTo++] =((byte) (val >>> 32));
		arrayTo[offsetTo++] =((byte) (val >>> 24));
		arrayTo[offsetTo++] =((byte) (val >>> 16));
		arrayTo[offsetTo++] =((byte) (val >>> 8));
		arrayTo[offsetTo++] =((byte) val);
		return offsetTo;
	    	}
	public static int writeFloat(float val,byte arrayTo[],int offsetTo)
		{
        	return writeUnsignedInt(Float.floatToIntBits(val),arrayTo,offsetTo);
    		}

	public static int writeDouble(double val,byte arrayTo[],int offsetTo)
		{
		return writeUnsignedLong(Double.doubleToLongBits(val),arrayTo,offsetTo);
	    	}
    
    
    
    
	}
