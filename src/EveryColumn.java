
public class EveryColumn extends Column
{

	public EveryColumn()
	{
		super();
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean newItem(Item item)
	{
		if (item instanceof Tweet)
		{
			Tweet tweet = (Tweet) item;
			//Log.d("Twitter test", tweet.user.getName() + ": " + tweet.text);
		}
		addItem(item);

		return true;
	}
}
