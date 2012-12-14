
/**
 * Shows retweets of the specified user
 * @author Zack
 *
 */
public class RetweetColumn extends Column
{
	String username;
	
	public RetweetColumn(String _username)
	{
		super();
		username=_username;
	}
	@Override public boolean newItem(Item item)
	{
		if(item instanceof Retweet)
		{
			Retweet retweet=(Retweet)item;
			if(retweet.user.username.equalsIgnoreCase(username))
			{
				addItem(item);
				return true;
			}
		}
		return false;
	}

}
