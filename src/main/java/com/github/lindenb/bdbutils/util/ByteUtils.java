package com.github.lindenb.bdbutils.util;


public class ByteUtils
{
	public ByteUtils()
	{
	}

	public static int writeByte(int val, byte array[],int offset)
	{
		return writeUnsignedByte((val ^ 0x80),array,offset);
	}

	public static int writeUnsignedByte(int val, byte array[],int offset)
	{
		array[offset++] = (byte) val;
		return offset;
	}      

	public static int write(final byte arrayFrom[],byte arrayTo[],int offsetTo)
	{
		return write(arrayFrom,0,arrayTo,offsetTo,arrayFrom.length);
	}

	public static int write(final byte arrayFrom[],int offsetFrom, byte arrayTo[],int offsetTo,int len)
		{
		System.arraycopy(arrayFrom,offsetFrom,arrayTo,offsetTo,len);
		return offsetTo+len;
		}	


	public static int writeBoolean(boolean val,byte arrayTo[],int offsetTo)
	{
		arrayTo[offsetTo++] =(val ? (byte)1 : (byte)0);
		return offsetTo;
	}

	public static int writeShort(int val,byte arrayTo[],int offsetTo)
	{
		return writeUnsignedShort(val ^ 0x8000,arrayTo,offsetTo);
	}	

	public static int writeUnsignedShort(int val,byte arrayTo[],int offsetTo)
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

/** read Section */

	public static byte readByte(final byte array[],int offset)
		{
		return (byte) (readUnsignedByte(array,offset) ^ 0x80);
		}

	public static int readUnsignedByte(final byte array[],int offset)
		{
		return array[offset];
		}      



	public static boolean readBoolean(final byte array[],int offset)
	{
		return readByte(array,offset)!=(byte)0;
	}

	public static short readShort(final byte array[],int offset)
	{
        return (short) (readUnsignedShort(array,offset) ^ 0x8000);
	}	

	public static int readUnsignedShort(final byte array[],int offset)
	{
		return ((array[offset] << 8) | array[offset+1]);
	}
	
	public static int readInt(final byte array[],int offset)
	{
		return (int) (readUnsignedInt(array,offset) ^ 0x80000000);
	}

	public static long readUnsignedInt(final byte array[],int offset)
	{
		return ((array[offset+0] << 24) | (array[offset+1] << 16) | (array[offset+2] << 8) | array[offset+3]);
	}
	public static long readLong(final byte array[],int offset)
		{
		 return readUnsignedLong(array,offset) ^ 0x8000000000000000L;
		}

	public static int readUnsignedLong(final byte array[],int offset)
		{
		 return ((array[offset+0] << 56) | (array[offset+1] << 48) | (array[offset+2] << 40) | (array[offset+3] << 32) |
	                (array[offset+4] << 24) | (array[offset+5] << 16) | (array[offset+6] << 8)  | array[offset+7]);
		}
	
	public static float readFloat(final byte array[],int offset)
	{
		  return Float.intBitsToFloat((int) readUnsignedInt( array,offset));
	}

	public static double readDouble(final byte array[],int offset)
		{
		return Double.longBitsToDouble(readUnsignedLong(array,offset));
		}
}
