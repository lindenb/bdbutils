package com.github.lindenb.bdbutils.xml;

import java.io.IOException;
import java.util.Iterator;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import com.github.lindenb.bdbutils.util.Dictionary;
import com.sleepycat.bind.tuple.TupleOutput;

public class FastXMLEventWriter implements XMLEventWriter
	{
	private TupleOutput out;
	private Dictionary dict;
	public FastXMLEventWriter(TupleOutput out,Dictionary dict)
		{
		this.out=out;
		this.dict=dict;
		}
	
	protected void writeIndexedString(String v)
		{
		if(this.dict!=null)
			{
			if(this.dict.size()<Byte.MAX_VALUE)
				{
				int b=this.dict.getIndex(v);
				if(b!=-1)
					{
					this.out.writeByte((byte)b);
					}
				else
					{
					this.out.writeByte((byte)-1);
					out.writeString(v);
					}
				}
			else if(this.dict.size()<Short.MAX_VALUE)
				{
				int b=this.dict.getIndex(v);
				if(b!=-1)
					{
					this.out.writeShort((short)b);
					}
				else
					{
					this.out.writeShort((short)-1);
					out.writeString(v);
					}
				}
			else
				{
				int b=this.dict.getIndex(v);
				if(b!=-1)
					{
					this.out.writeInt((int)b);
					}
				else
					{
					this.out.writeInt((int)-1);
					out.writeString(v);
					}
				}
			return;
			}
		//no dict
		out.writeString(v);
		}
	
	protected void writeQName(QName qname) throws XMLStreamException
		{
		writeIndexedString(qname.getNamespaceURI());
		writeIndexedString(qname.getPrefix());
		writeIndexedString(qname.getLocalPart());
		}
	
	protected void writeNamespace(Namespace ns) throws XMLStreamException
		{
		writeIndexedString(ns.getPrefix());
		writeIndexedString(ns.getNamespaceURI());
		}
	
	protected void writeAttribute(Attribute att) throws XMLStreamException
		{
		writeQName(att.getName());
		this.out.writeString(att.getValue());
		}
	
	@Override
	public void add(XMLEvent evt) throws XMLStreamException
		{
		out.writeByte((byte)evt.getEventType());
		switch(evt.getEventType())
			{
			case XMLEvent.START_DOCUMENT:
			case XMLEvent.END_DOCUMENT:
			case XMLEvent.END_ELEMENT:
				{
				break;
				}
			case XMLEvent.ATTRIBUTE:
				{
				writeAttribute(Attribute.class.cast(evt));
				break;
				}
			case XMLEvent.START_ELEMENT:
				{
				StartElement elt=evt.asStartElement();
				writeQName(elt.getName());
				for(Iterator<?> iter=elt.getAttributes();iter.hasNext();)
					{
					this.out.writeBoolean(true);//there is an attribute
					writeAttribute(Attribute.class.cast(iter.next()));
					}
				this.out.writeBoolean(false);//end of attributes
				
				for(Iterator<?> iter=elt.getNamespaces();iter.hasNext();)
					{
					this.out.writeBoolean(true);//there is a ns
					writeNamespace(Namespace.class.cast(iter.next()));
					}
				this.out.writeBoolean(false);//end of ns
				
				break;
				}
			case XMLEvent.PROCESSING_INSTRUCTION:
			
			case XMLEvent.CDATA:
			case XMLEvent.CHARACTERS:
				{
				this.out.writeString(evt.asCharacters().getData());
				break;
				}
			case XMLEvent.COMMENT:
				{
				this.out.writeString(javax.xml.stream.events.Comment.class.cast(evt).getText());
				break;
				}
			default: throw new XMLStreamException("event type:"+evt);
			}
		}

	@Override
	public void add(XMLEventReader r) throws XMLStreamException
		{
		while(r.hasNext())
			{
			XMLEvent evt=r.nextEvent();
			this.add(evt);
			}
		}

	@Override
	public void close() throws XMLStreamException {

	}

	@Override
	public void flush() throws XMLStreamException {
		}

	@Override
	public NamespaceContext getNamespaceContext()
		{
		throw new UnsupportedOperationException();
		}

	@Override
	public String getPrefix(String arg0) throws XMLStreamException {
		throw new UnsupportedOperationException();
		}

	@Override
	public void setDefaultNamespace(String arg0) throws XMLStreamException {
		throw new UnsupportedOperationException();
		}

	@Override
	public void setNamespaceContext(NamespaceContext arg0)
			throws XMLStreamException {
		throw new UnsupportedOperationException();
		}

	@Override
	public void setPrefix(String arg0, String arg1) throws XMLStreamException
		{
		throw new UnsupportedOperationException();
		}
	
	
	public static void main(String[] args) throws XMLStreamException,IOException
		{
		TupleOutput t=new TupleOutput();
		XMLEventWriter out=new FastXMLEventWriter(t);
		XMLInputFactory xmlfactory= XMLInputFactory.newInstance();
		XMLEventReader r=xmlfactory.createXMLEventReader(System.in);
		out.add(r);
		System.out.write(t.toByteArray());
		}

	
}
