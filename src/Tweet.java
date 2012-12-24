
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.SortedMap;
import java.util.Vector;

import twitter4j.AsyncTwitter;
import twitter4j.HashtagEntity;
import twitter4j.MediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterAdapter;
import twitter4j.TwitterListener;
import twitter4j.URLEntity;
import twitter4j.UserMentionEntity;


public class Tweet extends Item
{
	public long	  id=-1;
	public String text;
	public Tweet  inReplyTo=null;
	public long replyId=-1;// -1 if not in reply to a tweet
	public User   user;
	public Status t4j;
	public Date   date;
	public String dateString;
	public String client;
	public AccountHandler handler;
	public final static int type=1;
	public boolean isFavorited=false;

	protected Tweet(Status status,AccountHandler _handler)//TODO if have multiple accounts, should tweets keep track of handler?  Separate tweets per account even if both follow, or...?
	{
		t4j = status;
		handler=_handler;
		id = status.getId();
		text = status.getText();
		user = handler.getUser(status.getUser());
		date=status.getCreatedAt();
		time=date.getTime();
		dateString=new Long(date.getHours()).toString()+":"+new Long(date.getMinutes()).toString();//TODO 12 hour time+padding
		time=date.getTime();
		replyId=status.getInReplyToStatusId();
		if(replyId==-1)
			inReplyTo=null;
		else
			inReplyTo=handler.getLoadedTweet(replyId);
		isFavorited=status.isFavorited();
		
		class Entity
		{
			public int start,end;
			public String replacement;
			public Entity(int _start,int _end,String _r)
			{
				start=_start;
				end=_end;
				replacement=_r;
			}
		}
		Vector<Entity> entities=new Vector<Entity>();
		{
			URLEntity[] ents=status.getURLEntities();
			if(entities!=null)
			{
				for(int i=ents.length-1;i>=0;i--)
				{
					entities.add(new Entity(ents[i].getStart(),ents[i].getEnd(),"<a href=\""+ents[i].getExpandedURL()+"\">"+ents[i].getDisplayURL()+"</a>"));
				}
			}
		}
		{
			MediaEntity[] ents=status.getMediaEntities();
			if(entities!=null)
			{
				for(int i=ents.length-1;i>=0;i--)
				{
					entities.add(new Entity(ents[i].getStart(),ents[i].getEnd(),"<a href=\""+ents[i].getMediaURL()+"\">"+ents[i].getDisplayURL()+"</a>"));
				}
			}
		}
		{
			UserMentionEntity[] ents=status.getUserMentionEntities();
			if(entities!=null)
			{
				for(int i=ents.length-1;i>=0;i--)
				{
					entities.add(new Entity(ents[i].getStart(),ents[i].getEnd(),"<a href=\"https://twitter.com/"+ents[i].getScreenName()+"\">@"+ents[i].getScreenName()+"</a>"));
				}
			}
		}
		{
			HashtagEntity[] ents=status.getHashtagEntities();
			if(entities!=null)
			{
				for(int i=ents.length-1;i>=0;i--)
				{
					entities.add(new Entity(ents[i].getStart(),ents[i].getEnd(),"<a href=\"https://twitter.com/search?q=%23"+ents[i].getText()+"\">#"+ents[i].getText()+"</a>"));
				}
			}
		}
		if(entities.size()>=2)
			Collections.sort(entities,new Comparator<Entity>(){
	
				@Override public int compare(Entity a,Entity b)
				{
					return a.start-b.start;
				}
			});
		for(int i=entities.size()-1;i>=0;i--)
		{
			String original=text;
			Entity entity=entities.elementAt(i);
			String begin=text.substring(0,entity.start);
			String end=text.substring(entity.end,text.length());
			text=begin+entity.replacement+end;
		}
		client=status.getSource();
		
	}
	public Tweet(String name, String _text,AccountHandler _handler)
	{
		id=(long)(Math.random()*10000);
		text=_text;
		handler=_handler;
		user=handler.getUser(name);
		date=new Date();
		dateString="00:00";
		time=date.getTime();
		t4j=null;
	}
	public int getType()
	{
		return type;
	}
	public static Tweet createTweet(Status status,AccountHandler handler)
	{
		if(status.isRetweet())
			return new Retweet(status,handler);
		else
			return new Tweet(status,handler);
	}
	public void retweet()
	{
		AsyncTwitter twitter=handler.asyncFactory.getInstance();
		twitter.retweetStatus(id);
	}
	public void delete()
	{
		handler.asyncFactory.getInstance().destroyStatus(id);
	}
	public void favorite()
	{
		AsyncTwitter twitter=handler.asyncFactory.getInstance();//TODO are listeners added per instance?  If not, make one listener with everything implemented
		TwitterListener listener = new TwitterAdapter() {
	       @Override public void createdFavorite(Status status)
	       {
	    	   if(status.getId()==id)
	    		   isFavorited=true;
	    	   else
	    		   handler.getTweet(status).isFavorited=true;
	       }
	    };
		
	    twitter.addListener(listener);
	    twitter.createFavorite(id);
	}
	public void unfavorite()
	{
		AsyncTwitter twitter=handler.asyncFactory.getInstance();//TODO are listeners added per instance?  If not, make one listener with everything implemented
		TwitterListener listener = new TwitterAdapter() {
	       @Override public void destroyedFavorite(Status status)
	       {
	    	   if(status.getId()==id)
	    		   isFavorited=false;
	    	   else
	    		   handler.getTweet(status).isFavorited=false;
	       }
	    };
		
	    twitter.addListener(listener);
	    twitter.destroyFavorite(id);
	}
}
