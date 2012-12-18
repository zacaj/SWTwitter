import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Label;


public abstract class SWTweetButtonMouseListener implements MouseListener, MouseTrackListener
{
	Label label;
	Tweet tweet;
	SWTwitter twitter;
	String prefix;
	String def="";
	public  SWTweetButtonMouseListener(Label _label,String _prefix,Tweet _tweet,SWTwitter _twitter)
	{
		label=_label;
		tweet=_tweet;
		twitter=_twitter;
		prefix=_prefix;
	}
	@Override public void mouseEnter(MouseEvent e)
	{
		label.setImage(new Image(twitter.display,"resources/"+prefix+"_hover.png"));
	}

	@Override public void mouseExit(MouseEvent e)
	{
		label.setImage(new Image(twitter.display,"resources/"+prefix+def+".png"));
		
	}

	@Override public void mouseHover(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public void mouseDoubleClick(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

	@Override public void mouseDown(MouseEvent e)
	{
		// TODO Auto-generated method stub
		
	}

}
