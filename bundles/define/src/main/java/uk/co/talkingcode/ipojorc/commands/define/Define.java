package uk.co.talkingcode.ipojorc.commands.define;

import uk.co.talkingcode.ipojorc.api.IRCCommand;
import uk.co.talkingcode.ipojorc.api.IRCMessage;

public class Define implements IRCCommand
{
  private IRCMessage generateResponse(IRCMessage incoming, String query, String result)
  {
    incoming.setProcessed(true);
    IRCMessage response = new IRCMessage();
    response.setChannel(incoming.getChannel());
    response.setMessage("*" + query + "* " + result);
    return response;
  }
  
	public IRCMessage handleCommand(IRCMessage message)
	{
	  if (!message.isProcessed() && message != null)
	  {
	    if (message.getMessage().startsWith("!define "))
	    {		    
		  String query = message.getMessage().substring(8);
		  UrbanLookup ul = new UrbanLookup(query);
		  return generateResponse(message, query, ul.getDefinition());
		}
		else if (message.getMessage().startsWith("!example "))
		{
		  String query = message.getMessage().substring(9);
	      UrbanLookup ul = new UrbanLookup(query);
	      return generateResponse(message, query, ul.getExample());
		}
	  }
	  return null;
	}
}
