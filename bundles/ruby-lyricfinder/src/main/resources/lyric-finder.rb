require 'java'
require 'cgi'
require 'uri'
require 'net/http'

class RubyLyricFinder
  def getDescription
    return "!lyric - Ruby lyric finder"
  end

  def handlePrivateMessage(ircMessage)
    return handleMessage(ircMessage)
  end

  def handleMessage(ircMessage)
    puts "Handling: " + ircMessage.message
    match = ircMessage.message.match(/.*\/~ (.*) ~\/.*/)[1].gsub(/[,.\/\\!?'"]/, '')
    if (match != nil and match.size > 0)
      return lyricfind(match)
    else
      return nil
    end
  end

  def fetch_url(query)
    query = query.gsub(/ /, "+");
    url = "http://www.google.co.uk/search?hl=en&as_q=#{query}&num=1&as_sitesearch=lyricsdomain.com"
    r = Net::HTTP.get_response(URI.parse(url)).body
    return r
  end

  def lyricfind(query)
    result = fetch_url(query + " lyric")
    result = result.split("Search Results")[1]
    if result == nil
    then
      return "not found"
    else
      result = result.split("Lyrics")[0]
      result = result.gsub(/<\/?[^>]*>/, "")
      result = CGI.unescapeHTML(result)
      return result
    end
  end

  def handlePublicMessage(ircMessage)
    return handleMessage(ircMessage)
  end
  
end
