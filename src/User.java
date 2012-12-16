
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.swt.graphics.Image;


public class User
{
	public long   id;
	public String username, fullname;
	public Image avatar;
	public URL avatarUrl=null;
	
	public User(twitter4j.User user)
	{
		id = user.getId();
		username = user.getScreenName();
		fullname = user.getName();
		t4j = user;
		try
		{
			avatarUrl=new URL(user.getProfileImageURL());
		}
		catch (MalformedURLException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		AvatarCache.getAvatarForUser(this);
	}

	public User(String name)
	{
		username=fullname=name;
		id=(long)(Math.random()*10000);
		AvatarCache.getAvatarForUser(this);
	}

	String getName()
	{
		return username;// later will change based on preferences
	}

	twitter4j.User	t4j;
}
