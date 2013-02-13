package com.github.lindenb.bdbutils.binding;

import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import com.sleepycat.bind.tuple.IntegerBinding;
import com.sleepycat.bind.tuple.StringBinding;
import com.sleepycat.bind.tuple.TupleInput;
import com.sleepycat.bind.tuple.TupleOutput;

public class DOMBinding extends AbstractTupleBinding<Document>
	{
	private MapBinding<Integer, String> integer2strBinding=new  MapBinding<Integer, String>(
			new IntegerBinding(),
			new StringBinding()
			);
	
	
	
	@Override
	public Document entryToObject(TupleInput in)
		{
		try
			{
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			factory.setNamespaceAware(true);
			DocumentBuilder builder=factory.newDocumentBuilder();
			Document dom=builder.newDocument();
			Map<Integer,String> id2name=this.integer2strBinding.entryToObject(in);
			entryToObject(dom,dom,in,id2name);
			return dom;
			}
		catch(Exception err)
			{
			throw new RuntimeException(err);
			}
		}
	
	private void entryToObject(Document dom,Node root,TupleInput in,Map<Integer,String> id2name)
		{
		short nodeType=in.readShort();
		switch(nodeType)
			{
			case Node.ELEMENT_NODE:
				{
				int nsid=in.readInt();
				String ns=(nsid==-1?null:id2name.get(nsid));
				String qName=id2name.get(in.readInt());
				Element E=(ns==null?dom.createElement(qName):dom.createElementNS(ns, qName));
				root.appendChild(E);
				int n_atts=in.readInt();
				for(int i=0;i< n_atts;++i)
					{
					entryToObject(dom,E,in,id2name);
					}
				int n_child=in.readInt();
				for(int i=0;i< n_child;++i)
					{
					entryToObject(dom,E,in,id2name);
					}
				break;
				}
			case Node.ATTRIBUTE_NODE:
				{
				int nsid=in.readInt();
				String ns=(nsid==-1?null:id2name.get(nsid));
				String qName=id2name.get(in.readInt());
				Attr att=(ns==null?dom.createAttribute(qName):dom.createAttributeNS(ns, qName));
				att.setValue(in.readString());
				Element.class.cast(root).setAttributeNode(att);
				break;
				}
			case Node.TEXT_NODE:
				{
				root.appendChild(dom.createTextNode(in.readString()));
				break;
				}
			default:
				{
				throw new IllegalStateException();
				}
			}
		}
	
	private void addName(Map<String,Integer> name2id,String name)
		{
		if(!name2id.containsKey(name))
			{
			name2id.put(name, name2id.size()+1);
			}
		}
	
	private Map<String,Integer> collectNames(Node root,Map<String,Integer> name2id)
		{
		String ns=root.getNamespaceURI();
		if(ns!=null) addName(name2id,ns);
		switch(root.getNodeType())
			{
			case Node.ELEMENT_NODE:
				{
				addName(name2id,Element.class.cast(root).getTagName());
				if(root.hasAttributes())
					{
					NamedNodeMap atts=root.getAttributes();
					for(int i=0;i< atts.getLength();++i)
						{
						collectNames(atts.item(i), name2id);
						}
					}
				break;
				}
			case Node.ATTRIBUTE_NODE:
				{
				addName(name2id,Attr.class.cast(root).getName());
				break;
				}
			}
		for(Node c=root.getFirstChild();c!=null;c=c.getNextSibling())
			{
			collectNames(c,name2id);
			}
		return name2id;
		}

	@Override
	public void objectToEntry(Document o, TupleOutput out)
		{
		try
			{
			Map<String,Integer> name2id=collectNames(o,new HashMap<String,Integer>());
			Map<Integer,String> id2name=new HashMap<Integer, String>(name2id.size());
			for(String k:name2id.keySet())
				{
				id2name.put(name2id.get(k), k);
				}
			this.integer2strBinding.objectToEntry(id2name,out);
			objectToEntry(o.getDocumentElement(),out,name2id);
			}
		catch(Exception err)
			{
			throw new RuntimeException(err);
			}
		}
	private void objectToEntry(Node root, TupleOutput out,final Map<String,Integer> name2id)
		{
		out.writeShort(root.getNodeType());
		switch(root.getNodeType())
			{
			case Node.ELEMENT_NODE:
				{
				String ns=root.getNamespaceURI();
				out.writeInt(ns==null?-1:name2id.get(ns));
				out.writeInt(name2id.get(Element.class.cast(root).getTagName()));
				if(root.hasAttributes())
					{
					NamedNodeMap atts=root.getAttributes();
					out.writeInt(atts.getLength());
					for(int i=0;i< atts.getLength();++i)
						{
						objectToEntry(atts.item(i),out, name2id);
						}
					}
				else
					{
					out.writeInt(0);
					}
				if(root.hasChildNodes())
					{
					int n_child=0;
					for(Node c=root.getFirstChild();c!=null;c=c.getNextSibling())
						{
						n_child++;
						}
					out.writeInt(n_child);
					for(Node c=root.getFirstChild();c!=null;c=c.getNextSibling())
						{
						objectToEntry(c,out,name2id);
						}
					}
				else
					{
					out.writeInt(0);
					}
				break;
				}
			case Node.ATTRIBUTE_NODE:
				{
				String ns=root.getNamespaceURI();
				out.writeInt(ns==null?-1:name2id.get(ns));
				out.writeInt(name2id.get(Attr.class.cast(root).getName()));
				out.writeString(Attr.class.cast(root).getValue());
				break;
				}
			case Node.TEXT_NODE:
				{
				out.writeString(Text.class.cast(root).getData());
				break;
				}
			default:
				{
				throw new IllegalArgumentException("node.type="+root.getNodeType());
				}
			}
		}
	}	
