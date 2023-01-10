/*
 the txt file template is:
 user_attribute_name, real_attribute_name,min_val,max_val
 .....
 ip, the_ip_address
 port, the_number_of_port  
 rate, the_frequency_in_second
   */

package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Observable;
import java.util.Set;

public class ListOfAttributes{
	HashMap<String,AttributeSettings> attributesConnection;
	int port,rate;
	String ip;
	//the connection between the attribute name to its' setting
	public ListOfAttributes() {
		attributesConnection=new HashMap<String, AttributeSettings>();
	}
	public ListOfAttributes(String txtFile) {
		attributesConnection=new HashMap<>();
		try {
			BufferedReader read=new BufferedReader
					(new FileReader(new File(txtFile)));
			String line=null;
			while((line=read.readLine())!=null) {
				String[] data=line.split(",");
				if(data.length==4)
					attributesConnection.put
					(data[0],new AttributeSettings
							(new String[]{data[1],data[2],data[3]}));
				else {
					if(data.length==2) {
						switch(data[0]) {
						case "ip":
							ip=data[1];
							break;
						case "port":
							port=Integer.parseInt(data[1]);
							break;
						case "rate":
							rate=Integer.parseInt(data[1]);
							break;
						}
					}
				}
				
			}
		} catch (IOException e) {
			//e.printStackTrace();
		}
	}
	public boolean isEmpty(){return attributesConnection.keySet().size()==0;}
	public void addAttribute(String attributeName, AttributeSettings attributeSettings) {attributesConnection.put(attributeName, attributeSettings); }
	public boolean contains(String attributeName) {
		return attributesConnection.containsKey(attributeName);
	}
	public Set<String> getAttributesNames() {
		return attributesConnection.keySet();
	}
	public HashMap<String, AttributeSettings> getList(){
		return this.attributesConnection;
	}
	public int getLength() {
		return attributesConnection.size();
	}
}
