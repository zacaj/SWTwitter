

import java.io.File;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.*;


public class SWTColumnObserver implements ColumnObserver
{
	public Column column;
	Composite tweetComposite;
	SWTwitter twitter;
	ScrolledComposite scroll;
	
	public SWTColumnObserver(Column _column,SWTwitter _twitter)
	{
		column=_column;
		column.addObserver(this);
		twitter=_twitter;
		scroll=new ScrolledComposite(twitter.scrollHolder,SWT.V_SCROLL|SWT.BORDER);
		scroll.setBackground(twitter.display.getSystemColor(SWT.COLOR_GRAY));
		
		scroll.setLayout(new FormLayout());
		scroll.getVerticalBar().setIncrement(10);
		scroll.setExpandHorizontal(true);
		scroll.setExpandVertical(false);
		scroll.setMinWidth(100);
		scroll.setFocus();
		FormData data=new FormData();
		data.left=new FormAttachment(0,0);
		data.top=new FormAttachment(0,0);
		data.right=new FormAttachment(100,0);
		data.bottom=new FormAttachment(100,0);
		scroll.setLayoutData(data);
		
		scroll.addMouseListener(new MouseListener()
		{
			@Override public void mouseDoubleClick(MouseEvent e)
			{			}
			@Override public void mouseDown(MouseEvent e)
			{
				scroll.setFocus();
			}
			@Override public void mouseUp(MouseEvent e)
			{			}
		});
		scroll.addControlListener(new ControlListener(){
			@Override public void controlMoved(ControlEvent e)
			{			}
			@Override public void controlResized(ControlEvent e)
			{
				//scroll.setSize(scroll.computeSize(twitter.shell.getSize().x, SWT.DEFAULT));
				scroll.layout();
			}	
		});

		GridLayout lay=new GridLayout();
		tweetComposite=new Composite(scroll,SWT.NONE);
		scroll.setContent(tweetComposite);
		lay.numColumns=1;
		tweetComposite.setLayout(lay);
		
	/*	data=new FormData();
		data.left=new FormAttachment(0,0);
		data.top=new FormAttachment(0,0);
		data.right=new FormAttachment(100,0);
		data.bottom=new FormAttachment(100,0);
		tweetComposite.setLayoutData(data);*/
		//tweetComposite.setSize(tweetComposite.computeSize(scroll.getSize().x,SWT.DEFAULT));
	}
	@Override public void onItemAdded(int index, final Item item)
	{
		final Tweet tweet=(Tweet)item;
		Runnable runnable=new Runnable()
		{
			@Override public void run()
			{
				Composite c=new Composite(tweetComposite,SWT.NONE);
				c.setData(item);
				c.setLayout(new FormLayout());
				{
					SWTweetButtonMouseListener listener;
					Label name=new Label(c,SWT.NONE);
					name.setText(tweet.user.username);
					name.setFont(new Font(tweetComposite.getDisplay(),"Arial",14, SWT.NORMAL));
					FormData data=new FormData();
					data.left=new FormAttachment(0,0);
					data.top=new FormAttachment(0,0);
					name.setLayoutData(data);
					
					Link client=new Link(c,SWT.NONE);
					client.setText(" at "+tweet.dateString+" via "+tweet.client);
					data=new FormData();
					data.left=new FormAttachment(name,3);
					data.top=new FormAttachment(0,6);
					client.setLayoutData(data);
					
					Label image=new Label(c,SWT.NONE);
					image.setImage(tweet.user.avatar);
					data=new FormData();
					data.left=new FormAttachment(0,0);
					data.top=new FormAttachment(name,0);
					image.setLayoutData(data);			

					Label favorite=new Label(c,SWT.NONE);
					favorite.setImage(new Image(twitter.display,"resources/favorite.png"));
					data=new FormData();
					data.top=new FormAttachment(0,0);
					data.right=new FormAttachment(100,0);
					favorite.setLayoutData(data);
					favorite.addMouseTrackListener(listener=new SWTweetButtonMouseListener(favorite,"favorite",tweet,twitter)
					{
						@Override public void mouseUp(MouseEvent e)
						{
							if(tweet.isFavorited)
							{
								tweet.unfavorite();
								def="";
							}
							else
							{
								tweet.favorite();
								def="_on";
							}
						}			
					});
					favorite.addMouseListener(listener);
					
					Label retweet=new Label(c,SWT.NONE);
					retweet.setImage(new Image(twitter.display,"resources/retweet.png"));
					data=new FormData();
					data.top=new FormAttachment(favorite,0);
					data.right=new FormAttachment(100,0);
					retweet.setLayoutData(data);
					retweet.addMouseTrackListener(listener=new SWTweetButtonMouseListener(retweet,"retweet",tweet,twitter)
					{
						@Override public void mouseUp(MouseEvent e)
						{
							tweet.retweet();
							def="_on";
						}			
					});
					retweet.addMouseListener(listener);
					
					Label text=new Label(c,SWT.WRAP);
					text.setText(tweet.text);
					data=new FormData();
					data.left=new FormAttachment(image,0);
					data.top=new FormAttachment(name,0);
					data.right=new FormAttachment(favorite,0);
					data.bottom=new FormAttachment(100,0);
					text.setLayoutData(data);
					
					/*StyledText text2=new StyledText(c,SWT.WRAP);
					text2.setText()
					StyleRange style1 = new StyleRange();
				    style1.start = 0;
				    style1.length = 10;
				    style1.fontStyle = SWT.BOLD;
				    text2.setStyleRange(style1);*/
				}
				c.setSize(c.computeSize(SWT.DEFAULT,SWT.DEFAULT));
				
				GridData dat=new GridData();
				dat.grabExcessHorizontalSpace=true;
				dat.horizontalAlignment=GridData.HORIZONTAL_ALIGN_FILL;
				
				c.setLayoutData(dat);
				
				Control[] tweets=tweetComposite.getChildren();
				int i=0;
				for(i=0;i<tweets.length;i++)
				{
					Item _item=(Item) tweets[i].getData();
					if(_item.time<item.time)
						break;
				}
				if(i<tweets.length)
					c.moveAbove(tweets[i]);
				
				tweetComposite.setSize(tweetComposite.computeSize(tweetComposite.getSize().x,SWT.DEFAULT));
				tweetComposite.layout();
			}
		};
		twitter.addTask(runnable);
	}
	@Override public void onItemRemoved(int index, Item item)
	{
		
	}
}
