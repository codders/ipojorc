
class RubyEcho
  def getDescription
    return "!rubyecho - Ruby echo command"
  end

  def handlePrivateMessage(channel, nick, message)
    return handleMessage(channel, nick, message)
  end

  def handleMessage(channel, nick, message)
    match = message.match(/^!rubyecho (.*)/)
    if (match != nil and match[1].size > 0)
      return "Ruby says #{match[1]}"
    else
      return nil
    end
  end

  def handlePublicMessage(channel, nick, message)
    return handleMessage(channel, nick, message)
  end
end

puts "Loaded ruby echo command"
