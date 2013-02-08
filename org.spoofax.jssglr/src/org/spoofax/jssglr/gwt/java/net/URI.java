package java.net;

import java.net.URL;

public class URI extends URL
{
	public URI() { }
	public URL toURL() { return new URL(); }
	public String getScheme() { return ""; }
	public String getPath() { return ""; }
	public boolean isAbsolute() { return false; }
}
