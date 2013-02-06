/*
Fractal is a light-weight group communication system. Copyright (C) 2005, Sprint Project.      This library is free software; you can redistribute it and/or modify it under     the terms of the GNU Lesser General Public License as published by the Free Software 	Foundation; either version 2.1 of the License, or (at your option) any later version.      This library is distributed in the hope that it will be useful, but WITHOUT      ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.      You should have received a copy of the GNU Lesser General Public License along      with this library; if not, write to the      Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
file attached to it to see how you can use it, edit or distributed.
*/


package net.sourceforge.fractal;

/**   
* @author P. Sutra
* 
*/ 


import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.sourceforge.fractal.utils.Pair;

public class UMessage extends Message implements Cloneable, Comparable<UMessage>{

	private static final long serialVersionUID = Messageable.FRACTAL_MID;
	
	private static final String uniqueIdSeparator=":"; // to construct the uniqueness of swid
	private static AtomicInteger rbmCounter; // logical clock of site
	
	protected String swid; // a system wide unique id
	
	static{
		rbmCounter = new AtomicInteger(0);
	}
	
	public Serializable serializable; 	
	
	public UMessage(){}
	
	public UMessage(Serializable s, int idSource){
		serializable = s;
		source = idSource;
		swid = String.valueOf(idSource) + uniqueIdSeparator+ rbmCounter.incrementAndGet();
	}
	
	public String getUniqueId(){
		return swid;
	}
	
	public void setUniqueId(String s){
		swid = s;
	}
		
	public Object clone(){
		UMessage m = (UMessage)super.clone();
		m.serializable = this.serializable;
		m.swid = this.swid;
		m.source = this.source;
		return m;
	}
	
	public void writeExternal(ObjectOutput out) throws IOException {
		 super.writeExternal(out);
		 out.writeObject(serializable);	
		 out.writeObject(swid);
	}

	public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
		super.readExternal(in);
		this.serializable = (Serializable)in.readObject();
		this.swid = (String)in.readObject();
	}
	
	public int hashCode(){
		return swid.hashCode();	
	}
	
	public boolean equals(Object arg0) {
		if(arg0 == null ) return false;
		else if (arg0 instanceof UMessage ){
		return	this.swid.equals(((UMessage)arg0).getUniqueId());
		}
		else return false;
    }
	
	
	public int compareTo(UMessage arg0) {
		if(arg0 == null || !(arg0 instanceof UMessage) )
			throw new ClassCastException();
		Pattern p = Pattern.compile("([^:]*):([^:]*)");
		Matcher m1 = p.matcher(this.swid);
		m1.find();
		Matcher m2 = p.matcher(((UMessage)arg0).swid);
		m2.find();
		if(Integer.valueOf(m1.group(1)) > Integer.valueOf(m2.group(1))){
			return 1;
		}else if(Integer.valueOf(m1.group(1)) < Integer.valueOf(m2.group(1))){
			return -1;
		}else{// equality
			if( Integer.valueOf(m1.group(2)) > Integer.valueOf(m2.group(2)) ){
				return 1;
			}else if ( Integer.valueOf(m1.group(2)) < Integer.valueOf(m2.group(2)) ){
				return -1;
			}else{
				return 0;
			}
		}
	}
	
	public String toString(){
		return '<'+swid+','+source+','+serializable+'>';
	}

	public Pair<Integer,Integer> uidToObject(){
		Pattern p = Pattern.compile("([^:]*):([^:]*)");
		Matcher m1 = p.matcher(this.swid);
		m1.find();
		return new Pair<Integer,Integer>(Integer.valueOf(m1.group(1)),Integer.valueOf(m1.group(2)));
	}

}