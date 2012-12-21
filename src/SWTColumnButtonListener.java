import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;


public class SWTColumnButtonListener implements SelectionListener
{
	SWTColumnObserver observer;
	SWTwitter twitter;
	public SWTColumnButtonListener(SWTColumnObserver _observer,SWTwitter _twitter)
	{
		observer=_observer;
		twitter=_twitter;
	}
	@Override public void widgetSelected(SelectionEvent e)
	{
		twitter.columnStack.topControl=observer.scroll;
		twitter.scrollHolder.layout();
		observer.scroll.setFocus();
		twitter.currentColumn=observer;
	}

	@Override public void widgetDefaultSelected(SelectionEvent e)
	{
		// TODO Auto-generated method stub
		
	}
}
