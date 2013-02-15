package com.github.lindenb.bdbutils.binding;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLEventFactory;
import javax.xml.stream.XMLEventWriter;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.Attribute;
import javax.xml.stream.events.Comment;
import javax.xml.stream.events.Namespace;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.github.lindenb.bdbutils.util.Dictionary;
import com.github.lindenb.bdbutils.xml.FastXMLEventReader;
import com.github.lindenb.bdbutils.xml.FastXMLEventWriter;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class DOMBinding extends AbstractTupleBinding<Document>
	{
	private Dictionary dict;
	public DOMBinding(Dictionary dict)
		{
		this.dict=dict;
		}
	
	
	@Override
	public Document entryToObject(TupleInput in)
		{
		try
			{
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document dom=builder.newDocument();
			Node curr=dom;
			FastXMLEventReader r=new FastXMLEventReader(in, this.dict);
			while(r.hasNext())
				{
				XMLEvent evt=r.nextEvent();
				switch(evt.getEventType())
					{
					case XMLEvent.START_ELEMENT:
						{
						StartElement E=evt.asStartElement();
						QName qName=E.getName();
						Element newE;
						newE=dom.createElementNS(qName.getNamespaceURI(),qName.getPrefix()+":"+qName.getLocalPart());
						
						for(Iterator<?> iter=E.getAttributes();iter.hasNext();)
							{
							Attribute att=(Attribute)iter.next();
							newE.setAttributeNS("", "",att.getValue());
							}

						
						curr.appendChild(newE);
						curr=newE;
						break;
						}
					case XMLEvent.END_ELEMENT:
						{
						curr=curr.getParentNode();
						break;
						}
					case XMLEvent.CDATA:
					case XMLEvent.CHARACTERS:
						{
						curr.appendChild(dom.createTextNode(evt.asCharacters().getData()));
						break;
						}
					case XMLEvent.COMMENT:
						{
						curr.appendChild(dom.createComment(Comment.class.cast(evt).getText()));
						break;
						}
					}
				}
			
			
			
			return dom;
			}
		catch(Exception err)
			{
			throw new RuntimeException(err);
			}
		}
	
	

	@Override
	public void objectToEntry(Document o, TupleOutput out)
		{
		try
			{
			XMLEventFactory evtFactory=XMLEventFactory.newFactory();
			FastXMLEventWriter w=new FastXMLEventWriter(out, this.dict);
			objectToEntry(o, w,evtFactory);
			}
		catch(Exception err)
			{
			throw new RuntimeException(err);
			}
		}
	
	private  void objectToEntry(Node root, XMLEventWriter out,XMLEventFactory evtFactory)
		throws XMLStreamException
			{
			switch(root.getNodeType())
				{
				case Node.COMMENT_NODE:
					{
					out.add(evtFactory.createComment(((org.w3c.dom.Comment)root).getData()));
					break;
					}
				case Node.TEXT_NODE:
					{
					out.add(evtFactory.createCharacters(((org.w3c.dom.Text)root).getData()));
					break;
					}
				case Node.CDATA_SECTION_NODE:
					{
					out.add(evtFactory.createCData(((org.w3c.dom.CDATASection)root).getData()));
					break;
					}

				case Node.ELEMENT_NODE:
					{
					List<Attribute> atts=new ArrayList<Attribute>();
					List<Namespace> ns=new ArrayList<Namespace>();
					if(root.hasAttributes())
						{
						NamedNodeMap nnm=root.getAttributes();
						for(int i=0;i< nnm.getLength();++i)
							{
							Attr att=(Attr)nnm.item(i);
							//atts.add(evtFactory.createAttribute(null, att.getValue()));
							}
						}
					
					//evtFactory.createStartElement(prefix, namespaceUri, localName, atts.iterator(), ns.iterator(),context);
					for(Node c=root.getFirstChild();c!=null;c=c.getNextSibling())
						{
						objectToEntry(c,out,evtFactory);
						}
					//evtFactory.createEndElement(prefix, namespaceUri, localName, namespaces);
					break;
					}
				}
			}

	}	
