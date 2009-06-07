require 'java'

class RubyEcho
  def getDescription
    return "!rubyecho - Ruby echo command"
  end

  def handlePrivateMessage(ircMessage)
    return handleMessage(ircMessage)
  end

  def handleMessage(ircMessage)
    puts "Handling: " + ircMessage.message
    match = ircMessage.message.match(/^!rubyecho (.*)/)
    if (match != nil and match[1].size > 0)
      return ircMessage.createReply("Ruby says #{match[1]}")
    else
      return nil
    end
  end

  def handlePublicMessage(ircMessage)
    return handleMessage(ircMessage)
  end
end
