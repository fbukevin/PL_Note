import java.net.*;

public class InetAddr
{
	public void reveal()
	{
		InetAddress localIP;

		try 
		{
			localIP = InetAddress.getLocalHost();
			String ip = localIP.getHostAddress();
			System.out.println("ip is " + ip);
			System.out.println("name and ip is "+localIP);
		}
		catch(UnknownHostException er)
		{ }
	
	
	}
	
}