package com.github.lindenb.bdbutils.xml;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class BuildXmlBinding extends DefaultHandler
	{

	
	private Map<String,Integer> name2id=new HashMap<String,Integer>();
	private List<String> id2name=new ArrayList<String>();
	private BuildXmlBinding()
		{
		
		}
	private void addName(String name)
		{
		if(name==null || name2id.containsKey(name)) return;
		int n=id2name.size();
		name2id.put(name, n);
		id2name.add(name);
		}
	
	private void stripPrefix(String qName,String localName)
		{
		if(qName==null || localName.equals(qName) || !qName.endsWith(localName)) return;
		String pfx= qName.substring(0,qName.length()-localName.length());
		if(!pfx.endsWith(":")) return ;
		pfx=pfx.substring(0,pfx.length()-1);
		addName(pfx);
		}
	
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException
		{
		addName(uri);
		addName(localName);
		stripPrefix(qName,localName);
		for(int i=0;i< attributes.getLength();++i)
			{
			addName(attributes.getURI(i));
			addName(attributes.getLocalName(i));
			stripPrefix(attributes.getQName(i),attributes.getLocalName(i));
			}
		}
	
	@Override
	public void startPrefixMapping(String prefix, String uri)
			throws SAXException {
		addName(prefix);
		addName(uri);
		}
	
	private void dump()
		{
		System.out.println("private static final String ID2NAME[]={");
		for(int i=0;i< id2name.size();++i)
			{
			System.out.print("\t\"");
			System.out.print(id2name.get(i));
			System.out.print("\"");
			if(i+1!=id2name.size()) System.out.print(",");
			System.out.println("// "+i);
			}
		System.out.println("\t};");
		System.out.println("private static final java.util.Map<String,Integer> NAME2ID=java.util.Collections.unmodifiableMap(new java.util.HashMap<String,Integer>("+id2name.size()+")");
		System.out.println("\t{{");
		for(int i=0;i< id2name.size();++i)
			{
			System.out.println("\tput(ID2NAME["+i+"],"+i+");");
			}
		System.out.println("\t}});");
		
		
		System.out.println("@Override\nprotected String readIndexedString()\n{");
		if(id2name.size()<Byte.MAX_VALUE)
			{
			System.out.println("byte n=input.readByte();");
			}
		else if(id2name.size()<Short.MAX_VALUE)
			{
			System.out.println("short n=input.readShort();");
			}
		else
			{
			System.out.println("int n=input.readInt();");
			}
		System.out.println("if(n!=-1) return ID2NAME[n];");
		System.out.println("return in.readString();");
		System.out.println("}\n");
		
		
		System.out.println("@Override\nprotected void writeIndexedString(String v)\n{\nInteger idx=NAME2ID.get(v);");
		
		if(id2name.size()<Byte.MAX_VALUE)
			{
			System.out.println("if(idx==null)\n\t{ out.writeByte((byte)-1); out.writeString(v);}\nelse\n\t{  out.writeByte(idx.byteValue());}");
			}
		else if(id2name.size()<Short.MAX_VALUE)
			{
			System.out.println("if(idx==null) { out.writeShort((short)-1); out.writeString(v);} else {  out.writeShort(idx.shortValue());}");
			}
		else
			{
			System.out.println("if(idx==null) { out.writeInt(-1); out.writeString(v);} else {  out.writeInt(idx.intValue());}");
			}
		System.out.println("}\n");
		}
	/**
	 * @param args
	 */
	public static void main(String[] args) throws SAXException,IOException,ParserConfigurationException
		{
		SAXParserFactory saxParserFactory=SAXParserFactory.newInstance();
		saxParserFactory.setNamespaceAware(true);
		saxParserFactory.setValidating(false);
		SAXParser sax=saxParserFactory.newSAXParser();
		BuildXmlBinding handler=new BuildXmlBinding();
		sax.parse(System.in, handler);
		handler.dump();
		}

}
