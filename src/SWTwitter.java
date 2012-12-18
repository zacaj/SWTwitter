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
import org.eclipse.swt.custom.StackLayout;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.SelectionAdapter;
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
	public Composite scrollHolder;
	StackLayout columnStack;
	long replyId=-1;
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
		FormData data;
		
		
		
		scrollHolder=new Composite(shell,SWT.NONE);
		data=new FormData();
		data.height=600;
		data.left=new FormAttachment(0,0);
		data.right=new FormAttachment(100,0);
		scrollHolder.setLayoutData(data);
		columnStack=new StackLayout();
		scrollHolder.setLayout(columnStack);
		
		String path = "";
		File directory = new File(path);
		directory.mkdirs();
		handler=new AccountHandler();
		
			Column column;
			handler.columns.add(column = new EveryColumn());
			SWTColumnObserver every = new SWTColumnObserver(column,this);
			handler.columns.add(column = new MentionColumn("zacaj_"));
			SWTColumnObserver mention = new SWTColumnObserver(column,this);
			columnStack.topControl=every.scroll;
			every.scroll.setFocus();
			
			Button homeColumn=new Button(shell,SWT.PUSH);
			homeColumn.setText("Home");
			data=new FormData();
			data.left=new FormAttachment(0,0);
			data.top=new FormAttachment(scrollHolder,0);
			homeColumn.setLayoutData(data);
			homeColumn.addSelectionListener(new SWTColumnButtonListener(every,this));
			
			Button mentionColumn=new Button(shell,SWT.PUSH);
			mentionColumn.setText("Mentions");
			data=new FormData();
			data.left=new FormAttachment(homeColumn,0);
			data.top=new FormAttachment(scrollHolder,0);
			mentionColumn.setLayoutData(data);
			mentionColumn.addSelectionListener(new SWTColumnButtonListener(mention,this));
			
			Button sendTweet=new Button(shell,SWT.PUSH);
			sendTweet.setText("Tweet");
			data=new FormData();
			data.right=new FormAttachment(100,0);
			data.top=new FormAttachment(homeColumn,0);
			sendTweet.setLayoutData(data);
			
			final Text tweetBox=new Text(shell,SWT.WRAP);
			data=new FormData();
			data.top=new FormAttachment(homeColumn,0);
			data.left=new FormAttachment(0,0);
			data.right=new FormAttachment(sendTweet,0);
			data.bottom=new FormAttachment(100,0);
			tweetBox.setLayoutData(data);
				
			sendTweet.addSelectionListener(new SelectionListener() {
				@Override public void widgetSelected(SelectionEvent e)
				{
					if(replyId==-1)
						handler.sendTweet(tweetBox.getText());
					else
						handler.sendTweet(tweetBox.getText(),replyId);
					tweetBox.setText("");
				}
				@Override public void widgetDefaultSelected(SelectionEvent e)
				{
					// TODO Auto-generated method stub
				}
			});/**/
		shell.open();
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(path
					+ "user.txt"));
			String accessToken = in.readLine();
			String accessTokenSecret = in.readLine();
			String restUrl = in.readLine();
			in.close();
			
			if(accessToken.length()>16)
			{
				handler.accessToken = accessToken;
				handler.accessTokenSecret = accessTokenSecret;
			}
			if (restUrl != null) handler.restUrl = restUrl;
			handler.start();
		}
		catch (Exception ex) 
		{
			//Log.i("ZATCAP","Exception: " + ex.getLocalizedMessage());
			//for (StackTraceElement ste : ex.getStackTrace())
				//Log.i("ZATCAP",ste.toString());
		}/**/
		/*{
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
