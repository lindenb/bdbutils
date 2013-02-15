package com.github.lindenb.bdbutils.binding;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLStreamException;

import com.github.lindenb.bdbutils.util.Dictionary;
import com.github.lindenb.bdbutils.xml.FastXMLEventReader;
import com.github.lindenb.bdbutils.xml.FastXMLEventWriter;
import com.sleepycat.bind.tuple.TupleInput;

public class JAXBBinding<T> extends AbstractTupleBinding<T>
	{
	private JAXBContext ctx;
	private Dictionary dict;
	private Class<T> clazz;
	public JAXBBinding(Class<T> clazz,Dictionary dict)
		{
		this.clazz=clazz;
		this.dict=dict;
		}

	protected JAXBContext createJaxbContext() throws JAXBException
		{
		return JAXBContext.newInstance(this.clazz);
		}
	
	private JAXBContext jaxbCtx()
		{
		if(ctx==null)
			{
			synchronized(clazz)
				{
				if(ctx==null)
					{
					try {
						ctx=createJaxbContext();
					} catch (JAXBException e) {
						throw new RuntimeException(e);
						}
					}
				}
			}
		return ctx;
		}
	
	@Override
	public T entryToObject(TupleInput in)
		{
		try {
			FastXMLEventReader r=new FastXMLEventReader(in, this.dict);
			Unmarshaller um=jaxbCtx().createUnmarshaller();
			JAXBElement<T> o=um.unmarshal(r,this.clazz);
			r.close();
			return o.getValue();
			}
		catch (JAXBException e)
			{
			throw new RuntimeException(e);
			}
		catch (XMLStreamException e)
			{
			throw new RuntimeException(e);
			}
		}

	public void objectToEntry(T o, com.sleepycat.bind.tuple.TupleOutput out)
		{
		try {
			Marshaller m=jaxbCtx().createMarshaller();
			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, false);
			m.setProperty(Marshaller.JAXB_FRAGMENT, true);
			FastXMLEventWriter w=new FastXMLEventWriter(out,this.dict);
			m.marshal(o, w);
			w.flush();
			w.close();
		} catch (JAXBException e) {
			throw new RuntimeException(e);
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
		}
		}
	}
