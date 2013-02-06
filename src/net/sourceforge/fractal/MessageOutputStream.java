/*
Fractal is a light-weight group communication system. Copyright (C) 2005, Sprint Project.      This library is free software; you can redistribute it and/or modify it under     the terms of the GNU Lesser General Public License as published by the Free Software 	Foundation; either version 2.1 of the License, or (at your option) any later version.      This library is distributed in the hope that it will be useful, but WITHOUT      ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A      PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.      You should have received a copy of the GNU Lesser General Public License along      with this library; if not, write to the      Free Software Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA 
file attached to it to see how you can use it, edit or distributed.
*/


package net.sourceforge.fractal;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;


/**   
* @author L. Camargos
* 
*/ 

public class MessageOutputStream extends ObjectOutputStream{
    
	public MessageOutputStream(OutputStream out) throws IOException{
           super(out);
	}
	
    protected void writeClassDescriptor(ObjectStreamClass desc) throws IOException {
        if(desc.getSerialVersionUID()  != Messageable.FRACTAL_MID){ // does not implement messageable (or if does, it does in the wrong way)
            writeInt(-1);
            super.writeClassDescriptor(desc);
        }else{
            Integer magic_num = MessageStream.messageToId.get(desc.forClass());
            
            if(magic_num != null){ //Class is  registered.
                if(ConstantPool.MESSAGE_STREAM_DEBUG) System.out.println("MessageOutputStream " + desc.getName() + " ==> " + magic_num);
                
                writeInt(magic_num.intValue());
                
            }else{ //Is not registered.
            	throw new RuntimeException("Class is not registered "+desc.getName());
            }
        }
    }
}
