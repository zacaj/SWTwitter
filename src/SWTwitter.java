import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import net.miginfocom.swt.MigLayout;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class SWTwitter
{
	public AccountHandler handler;
	private Queue<Runnable> tasks;
	public Shell shell;
	public Display display;
	public static void main(String[] args)
	{
		SWTwitter twitter=new SWTwitter();
	}
	public synchronized void addTask(Runnable runnable)
	{
		tasks.add(runnable);
	}
	public SWTwitter()
	{
		tasks=new ConcurrentLinkedQueue<Runnable>();
		display=new Display();
		AvatarCache.device=display;
		shell=new Shell(display);
		shell.setSize(400,800);
		shell.setLocation(1000,100);
		shell.setText("ZATCAP SWT");
		FormLayout layout=new FormLayout();
		shell.setLayout(layout);
		
		
		
		
		String path = "";
		File directory = new File(path);
		directory.mkdirs();
		handler=new AccountHandler();
		
			Column column;
			handler.columns.add(column = new EveryColumn());
			{
				SWTColumnObserver observer = new SWTColumnObserver(column,this);
			}
			handler.columns.add(column = new MentionColumn("zacaj_"));
			{
				//SWTColumnObserver observer = new SWTColumnObserver(column,this);
				
			}
		/*try
		{
			BufferedReader in = new BufferedReader(new FileReader(path
					+ "user.txt"));
			String accessToken = in.readLine();
			String accessTokenSecret = in.readLine();
			String restUrl = in.readLine();
			in.close();
		
			handler.accessToken = accessToken;
			handler.accessTokenSecret = accessTokenSecret;
			if (restUrl != null) handler.restUrl = restUrl;
			handler.start();
		}
		catch (Exception ex) 
		{
			//Log.i("ZATCAP","Exception: " + ex.getLocalizedMessage());
			//for (StackTraceElement ste : ex.getStackTrace())
				//Log.i("ZATCAP",ste.toString());
		}/**/
		{
			BufferedReader in;
			try
			{
				in = new BufferedReader(new FileReader("tweets.txt"));
				long n=new Long(in.readLine());
				for(int i=0;i<n;i++)
				{
					String name=in.readLine();
					String text=in.readLine();
					Tweet tweet=new Tweet(name,text,handler);
					handler.handleItem(tweet);
				}
				in.close();
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (NumberFormatException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (IOException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}/**/
		shell.open();
		int i=0;
		while(!shell.isDisposed())
		{
			while(!tasks.isEmpty())
				tasks.poll().run();
			if(!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
