package com.github.lindenb.bdbutils.xml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;

import javax.xml.XMLConstants;
import javax.xml.namespace.QName;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.XMLEvent;

import com.github.lindenb.bdbutils.util.Dictionary;
import com.sleepycat.bind.tuple.TupleInput;

public class FastXMLEventReader implements XMLEventReader
	{
	private TupleInput input;
	private XMLEventFactory xmlEventFactory;
	private boolean hasNextCalled=false;
	private XMLEvent next=null;
	private boolean eof_met=false;
	private Stack<QName> qNameStack=new Stack<QName>();
	private Dictionary dict;
	
	public FastXMLEventReader(TupleInput input,Dictionary dict)
		{
		this.input=input;
		this.xmlEventFactory=XMLEventFactory.newInstance();
		this.dict=dict;
		}
	
	@Override
	public final Object next()
		{
		try {
			return this.nextEvent();
		} catch (XMLStreamException e) {
			throw new RuntimeException(e);
			}
		}

	@Override
	public void remove() {
		throw new UnsupportedOperationException("cannot remove");
		}

	@Override
	public void close() throws XMLStreamException
		{
		eof_met=true;
		}

	@Override
	public String getElementText() throws XMLStreamException {
		return nextEvent().asCharacters().getData();
		}

	@Override
	public Object getProperty(String name) throws IllegalArgumentException
		{
		return null;
		}

	protected String readIndexedString()
		{
		if(this.dict!=null)
			{
			if(this.dict.size()<Byte.MAX_VALUE)
				{
				byte b=this.input.readByte();
				if(b!=-1) return this.dict.get(b);
				}
			else if(this.dict.size()<Short.MAX_VALUE)
				{
				short b=this.input.readShort();
				if(b!=-1) return this.dict.get(b);
				}
			else
				{
				int b=this.input.readInt();
				if(b!=-1) return this.dict.get(b);
				}
			}
		//no string found continue
		return input.readString();
		}
	
	protected QName readQName()
		{
		String ns=readIndexedString();
		String pfx=readIndexedString();
		String local=readIndexedString();
		if(ns==null) ns=XMLConstants.NULL_NS_URI;
		if(pfx==null) return new QName(ns, local);
		return new QName(ns,local,pfx);
		
		}
	protected Attribute readAttribute()
		{
		QName qName=readQName();
		return this.xmlEventFactory.createAttribute(qName,input.readString());
		}
	
	protected Namespace readNamespace()
		{
		String pfx=readIndexedString();
		String ns=readIndexedString();
		return (pfx==null?
				this.xmlEventFactory.createNamespace(ns):
				this.xmlEventFactory.createNamespace(pfx,ns)
				);
		}
	
	@Override
	public boolean hasNext()
		{
		if(!hasNextCalled)
			{
			next=null;
			hasNextCalled=true;
			if(eof_met)
				{
				return false;
				}
			int type=input.readByte();
			switch(type)
				{
				case XMLEvent.END_DOCUMENT:
					{
					
					next=this.xmlEventFactory.createEndDocument();
					break;
					}
				case XMLEvent.START_DOCUMENT:
					{
					next=this.xmlEventFactory.createStartDocument("UTF-8", "1.0");
					break;
					}
				case XMLEvent.CHARACTERS:
					{
					next=this.xmlEventFactory.createCharacters(input.readString());
					break;
					}
				case XMLEvent.COMMENT:
					{
					next=this.xmlEventFactory.createComment(input.readString());
					break;
					}
				case XMLEvent.CDATA:
					{
					next=this.xmlEventFactory.createCData(input.readString());
					break;
					}
				case XMLEvent.ATTRIBUTE:
					{
					next=readAttribute();
					break;
					}
				case XMLEvent.START_ELEMENT:
					{
					QName qName=readQName();
					List<Attribute> atts=new ArrayList<Attribute>();
					List<Namespace> ns=new ArrayList<Namespace>();

					while(input.readBoolean())
						{
						atts.add(readAttribute());
						}
					
					while(input.readBoolean())
						{
						ns.add(readNamespace());
						}
					
					next=this.xmlEventFactory.createStartElement(
							qName,
							atts.iterator(),
							ns.iterator()
							);
					this.qNameStack.push(qName);
					break;
					}
				case XMLEvent.END_ELEMENT:
					{
					QName qName=this.qNameStack.pop();
					List<Namespace> ns=Collections.emptyList();
					next=this.xmlEventFactory.createEndElement(qName, ns.iterator());
					break;
					}
				default: throw new RuntimeException();
				}
			}
		return next!=null;
		}

	@Override
	public XMLEvent nextEvent() throws XMLStreamException
		{
		if(!hasNextCalled) hasNext();
		if(next==null) throw new XMLStreamException("no next event");
		hasNextCalled=false;
		XMLEvent ret=next;
		next=null;
		if(ret.isEndDocument()) eof_met=true;
		return ret;
		}

	@Override
	public XMLEvent nextTag() throws XMLStreamException {
		while(hasNext())
			{
			XMLEvent evt=nextEvent();
			if(evt.isStartElement() || evt.isEndElement()) return evt;
			if(evt.isCharacters() && evt.asCharacters().getData().trim().isEmpty()) continue;
			break;
			}
		throw new XMLStreamException("not tag found");
		}

	@Override
	public XMLEvent peek() throws XMLStreamException
		{
		if(!hasNextCalled) hasNext();
		return next;
		}
	public static void main(String[] args) throws XMLStreamException,IOException
		{
		int c;
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		while((c=System.in.read())!=-1) baos.write((byte)c);
		XMLEventReader in=new FastXMLEventReader(new TupleInput(baos.toByteArray()),null);
		
		XMLOutputFactory xmlfactory= XMLOutputFactory.newInstance();
		XMLEventWriter w=xmlfactory.createXMLEventWriter(System.out);
		while(in.hasNext())
			{
			XMLEvent evt=in.nextEvent();
			w.add(evt);
			}
		w.flush();
		w.close();
		}
	}
