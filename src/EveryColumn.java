import java.io.FileNotFoundException;
import java.io.PrintWriter;


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
		/*if(contents.size()==20)
		{
			try
			{
				PrintWriter out=new PrintWriter("tweets.txt");
				out.println(new Long(contents.size()).toString());
				for(Item i:contents)
				{
					Tweet tweet=(Tweet)i;
					out.println(tweet.user.username);
					out.println(tweet.text);
				}
				out.close();
			}
			catch (FileNotFoundException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/

		return true;
	}
}
