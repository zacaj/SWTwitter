import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
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
	public Queue<Runnable> tasks;
	public static void main(String[] args)
	{
		SWTwitter twitter=new SWTwitter();
	}
	public SWTwitter()
	{
		tasks=new ConcurrentLinkedQueue<Runnable>();
		final Display display=new Display();
		AvatarCache.device=display;
		Shell shell=new Shell(display);
		shell.setSize(400,800);
		shell.setLocation(1000,100);
		shell.setText("ZATCAP SWT");
		FormLayout layout=new FormLayout();
		shell.setLayout(layout);
		
		ScrolledComposite scroll=new ScrolledComposite(shell,SWT.V_SCROLL);
		scroll.setBackground(display.getSystemColor(SWT.COLOR_GRAY));
		FormData data=new FormData();
		data.height=600;
		data.left=new FormAttachment(0,0);
		data.right=new FormAttachment(100,0);
		scroll.setLayoutData(data);
		scroll.setLayout(new FillLayout());
		
		
		shell.open();
		String path = "";
		File directory = new File(path);
		directory.mkdirs();
		handler=new AccountHandler();
		
		{
			Column column;
			handler.columns.add(column = new EveryColumn());
			{
				SWTColumnObserver observer = new SWTColumnObserver(column, scroll,this);
			}
		}
		try
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
		}
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
