package uk.co.talkingcode.ipojorc.api;

public class IRCUser {

  private String login;
  private String nick;
  private String hostname;

  public IRCUser(String nick, String login, String hostname)
  {
    this.nick = nick;
    this.login = login;
    this.hostname = hostname;
  }

  public String getLogin() {
    return login;
  }

  public void setLogin(String login) {
    this.login = login;
  }

  public String getNick() {
    return nick;
  }

  public void setNick(String nick) {
    this.nick = nick;
  }

  public String getHostname() {
    return hostname;
  }

  public void setHostname(String hostname) {
    this.hostname = hostname;
  }

  @Override
  public boolean equals(Object obj) {
    if (obj == null)
      return false;
    if (!(obj instanceof IRCUser))
      return false;
    IRCUser other = (IRCUser)obj;
    return (other.hostname.equals(hostname)
        && other.login.equals(login)
        && other.nick.equals(nick));
  }

  @Override
  public int hashCode() {
    return hostname.hashCode() + login.hashCode() + nick.hashCode();
  }
  
  
}
