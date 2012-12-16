
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import org.eclipse.swt.graphics.Device;
import org.eclipse.swt.graphics.Image;


public class AvatarCache
{
	static Device device;
	public static void getAvatarForUser(final User user)
	{
	    File storagePath = new File("avatars/");
	    if(!storagePath.isDirectory())
	    	storagePath.mkdirs();
	    final File path=new File(storagePath,user.username);
	    if(path.isFile())
	    {
	    	user.avatar=new Image(device,path.toString());
	    	return; 
	    }
	    else
	    {
			user.avatar=new Image(device,(new File("defaultpic.png").toString()));
			if(user.avatarUrl==null)
				return;
			new Thread(new Runnable() {
    			@Override 
				public void run()
				{
    				URL url = user.avatarUrl;
    				InputStream input;
					try
					{
						input = url.openStream();
					}
					catch (IOException e1)
					{
    					return;
					}
    				try {
    				    OutputStream output = new FileOutputStream (path);
    				    try {
    				        byte[] buffer = new byte[4096];
    				        int bytesRead = 0;
    				        while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
    				            output.write(buffer, 0, bytesRead);
    				        }
    				    } finally {
    				        output.close();
    				    }
    				}
    				catch (IOException e)
    				{
    				} finally {
    					
    				    try
						{
							input.close();
						}
						catch (IOException e)
						{
							return;
						}
    				    user.avatar=new Image(device,path.toString());
    				}
				}
    		}).start();		
	    }
	}
}
