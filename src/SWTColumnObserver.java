

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.*;


public class SWTColumnObserver implements ColumnObserver
{
	public Column column;
	Composite tweetComposite;
	SWTwitter twitter;
	
	public SWTColumnObserver(Column _column, ScrolledComposite scroll,SWTwitter _twitter)
	{
		column=_column;
		column.addObserver(this);
		twitter=_twitter;

		RowLayout lay=new RowLayout();
		tweetComposite=new Composite(scroll,SWT.NONE);
		scroll.setContent(tweetComposite);
		lay.type=SWT.HORIZONTAL;
		lay.wrap=true;
		lay.fill=true;
		tweetComposite.setLayout(lay);
		
		tweetComposite.setSize(tweetComposite.computeSize(300,SWT.DEFAULT));
		
		/*ListAdapter adapter=new ListAdapter() {			
			@Override public int getCount()
			{
				return column.contents.size();
			}

			@Override public Object getItem(int arg0)
			{
				return column.contents.get(arg0);
			}

			@Override public long getItemId(int arg0)
			{
				return column.contents.get(arg0).time;
			}

			@Override public int getItemViewType(int arg0)
			{			
				return column.contents.get(arg0).getType();
			}

			@Override public View getView(int index, View view, ViewGroup arg2)
			{
				if(view==null || !(view instanceof RelativeLayout))
				{
					view=new RelativeLayout(activity);
				}
				RelativeLayout layout=(RelativeLayout)view;
				{
					View v=view.findViewById(6);
					if(v!=null)
						layout.removeView(v);
				}
				Item item=column.contents.get(index);
				int type=item.getType();
				switch(type)
				{
				case Retweet.type:
				{
					Retweet retweet=(Retweet)item;
					{
						{//rt
							TextView text=(TextView)layout.findViewById(5);
							if(text==null)
							{
								text=new TextView(activity);
								text.setBackgroundColor(Color.LTGRAY);
								text.setTextSize(10);
								text.setTextColor(Color.DKGRAY);
								text.setId(5);
								RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
								lp.addRule(RelativeLayout.RIGHT_OF,2);
								//lp.addRule(RelativeLayout.RIGHT_OF,4);
					        	lp.addRule(RelativeLayout.LEFT_OF,3);
					        	lp.addRule(RelativeLayout.ABOVE,1);
					        	text.setGravity(Gravity.BOTTOM);
					        	layout.addView(text,lp);
							}				
							String str="RT by "+retweet.retweetedBy.getName();
							if(retweet.nRetweet>1)
								str+=" (x"+new Long(retweet.nRetweet).toString()+")";
							text.setText(str);
						}
					}
				}
				case Tweet.type:
				{
					Tweet tweet=(Tweet)item;
					{
			        	ImageView image=(ImageView)layout.findViewById(4);
			        	if(image==null)
			        	{
			        		image=new ImageView(activity);
							image.setId(4);
							RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				        	lp.addRule(RelativeLayout.BELOW,2);
				        	lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				        	layout.addView(image,lp);
			        	}
			        	image.setImageBitmap(tweet.user.avatar);
					}
					{//username
						TextView text=(TextView)layout.findViewById(2);
						if(text==null)
						{
							text=new TextView(activity);
							text.setBackgroundColor(Color.LTGRAY);
							text.setTextSize(12);
							text.setTextColor(Color.DKGRAY);
							text.setId(2);
							RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				        	lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
				        	lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				        	layout.addView(text,lp);
						}				
						text.setText(tweet.user.getName());
					}
					{//time
						TextView text=(TextView)layout.findViewById(3);
						if(text==null)
						{
							text=new TextView(activity);
							text.setBackgroundColor(Color.LTGRAY);
							text.setTextSize(10);
							text.setTextColor(Color.DKGRAY);
							text.setId(3);
							RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				        	lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
				        	lp.addRule(RelativeLayout.ALIGN_PARENT_TOP);
				        	layout.addView(text,lp);
						}				
						text.setText(tweet.dateString);
					}
					{//text
						TextView text=(TextView)layout.findViewById(1);
						if(text==null)
						{
							text=new TextView(activity);
							text.setBackgroundColor(Color.LTGRAY);
							text.setTextSize(14);
							text.setTextColor(Color.BLACK);
							text.setId(1);
							RelativeLayout.LayoutParams lp=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);	        	
							lp.addRule(RelativeLayout.BELOW,2);
				        	lp.addRule(RelativeLayout.BELOW,3);
				        	lp.addRule(RelativeLayout.RIGHT_OF,4);
				        	layout.addView(text,lp);
						}			
						text.setText(tweet.text);
					}
					view.setOnClickListener(new TweetSelectListener(tweet,activity.app));
					break;
				}
				}
				return view;
			}

			@Override public int getViewTypeCount()
			{
				return 3;
			}

			@Override public boolean hasStableIds()
			{
				return true;
			}

			@Override public boolean isEmpty()
			{
				return column.contents.isEmpty();
			}
			
			@Override public void registerDataSetObserver(
					DataSetObserver observer)
			{
				observers.add(observer);
			}

			@Override public void unregisterDataSetObserver(
					DataSetObserver observer)
			{
				observers.remove(observer);
			}

			@Override public boolean areAllItemsEnabled()
			{
				// TODO What does this MEAN
				return false;
			}

			@Override public boolean isEnabled(int position)
			{
				// TODO What does this MEAN
				return false;
			}
			
		};
		
		
		listView.setAdapter(adapter);*/
	}
	@Override public void onItemAdded(int index, final Item item)
	{
		final Tweet tweet=(Tweet)item;
		Runnable runnable=new Runnable()
		{
			@Override public void run()
			{
				Composite c=new Composite(tweetComposite,SWT.NONE);
				c.setLayout(new FormLayout());
				{
					Label name=new Label(c,SWT.NONE);
					name.setText(tweet.user.username);
					name.setFont(new Font(tweetComposite.getDisplay(),"Arial",14, SWT.NORMAL));
					FormData data=new FormData();
					data.left=new FormAttachment(0,0);
					data.top=new FormAttachment(0,0);
					name.setLayoutData(data);
					
					Label image=new Label(c,SWT.NONE);
					image.setImage(tweet.user.avatar);
					data=new FormData();
					data.left=new FormAttachment(0,0);
					data.top=new FormAttachment(name,0);
					image.setLayoutData(data);
					
					Label text=new Label(c,SWT.WRAP);
					text.setText(tweet.text);
					data=new FormData();
					data.left=new FormAttachment(image,0);
					data.top=new FormAttachment(name,0);
					data.right=new FormAttachment(100,0);
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
				c.setSize(c.computeSize(300,SWT.DEFAULT));
				RowData dat=new RowData();
				dat.width=300;
				c.setLayoutData(dat);
				tweetComposite.setSize(tweetComposite.computeSize(300,SWT.DEFAULT));
			}
		};
		twitter.tasks.add(runnable);
	}
	@Override public void onItemRemoved(int index, Item item)
	{
		
	}
}
