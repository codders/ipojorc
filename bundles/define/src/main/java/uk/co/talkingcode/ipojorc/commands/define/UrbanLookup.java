package uk.co.talkingcode.ipojorc.commands.define;

import java.net.*;
import java.io.*;
import org.apache.commons.lang.*;

public class UrbanLookup
{
	private String url = "http://www.urbandictionary.com/define.php?term=";
	private URL urltemp;
	@SuppressWarnings("unused")
	private String query;
	InputStream is = null;
	DataInputStream dis;
	String line;
	
	@SuppressWarnings("deprecation")
	public UrbanLookup(String query)
	{
		this.query = query;
		this.url = url + URLEncoder.encode(query);
	}
	
	private String fetchData()
	{
		try
		{
			urltemp = new URL(url);
			is = urltemp.openStream();
		    dis = new DataInputStream(new BufferedInputStream(is));
		    return(dis.readUTF());
		}
		catch (IOException e) {
			return("");
		} 
	}
	
	public String getDefinition()
	{
		String result = "";
		String raw = fetchData();
		//debug("raw = " + raw);
		result = this.parse(raw, "definition");
		if (result.isEmpty())
		{
			return("not found");
		}
		return(result);
	}
	
	public String getExample()
	{
		String result = "";
		String raw = fetchData();
		result = this.parse(raw, "example");
		if (result.isEmpty())
		{
			return("not found");
		}
		return(result);
	}
	
	private static void debug(String output)
	{
		System.out.println(output);
	}
	
	private String parse(String data, String div)
	{
		//debug("parsing for " + div);
		String match = "<div class='" + div + "'>";
		//debug("match = " + match);arg0
		if (data.contains("isn't defined"))
		{
			return("");
		}
		if (data.contains(match))
		{
			String tmp = data.split(match)[1];
			//debug("tmp = " + tmp);
			String tmp2 = tmp.split("</div>")[0];
			//debug("tmp2 = " + tmp2);
			return(StringEscapeUtils.unescapeHtml(tmp2.trim()));
		}
		else
		{
			//debug("didnt match");
			return("");
		}
	}
	
	public void destroy()
	{
		try 
		{
			is.close();
			dis.close();
		}
		catch (IOException e) 
		{
		}
	}
	
	public static void main(String[] args)
	{
		UrbanLookup ul = new UrbanLookup("dry spell");
		debug(ul.url);
		debug(ul.getDefinition());
		debug(ul.getExample());
		ul.destroy();
	}
}