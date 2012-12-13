import java.awt.Color;

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
	public static void main(String[] args)
	{
		final Display display=new Display();
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
		
		RowLayout lay=new RowLayout();
		final Composite tweetComposite=new Composite(scroll,SWT.NONE);
		scroll.setContent(tweetComposite);
		lay.type=SWT.HORIZONTAL;
		lay.wrap=true;
		lay.fill=true;
		tweetComposite.setLayout(lay);
		
		tweetComposite.setSize(tweetComposite.computeSize(300,SWT.DEFAULT));
		
		{
			Button button=new Button(shell,SWT.PUSH);
			button.setText("tedfd");
			data=new FormData();
			data.top=new FormAttachment(scroll,0);
			data.left=new FormAttachment(0,0);
			button.setLayoutData(data);
			button.addSelectionListener(new SelectionListener()
					{

						@Override public void widgetSelected(SelectionEvent e)
						{
							
							{
								Composite c=new Composite(tweetComposite,SWT.NONE);
								c.setLayout(new FormLayout());
								{
									Label name=new Label(c,SWT.NONE);
									name.setText("zacaj_");
									name.setFont(new Font(display,"Arial",18, SWT.NORMAL));
									FormData data=new FormData();
									data.left=new FormAttachment(0,0);
									data.top=new FormAttachment(0,0);
									name.setLayoutData(data);
									
									Label image=new Label(c,SWT.NONE);
									image.setImage(new Image(display,"h:\\avatar.png"));
									data=new FormData();
									data.left=new FormAttachment(0,0);
									data.top=new FormAttachment(name,0);
									image.setLayoutData(data);
									
									Label text=new Label(c,SWT.WRAP);
									text.setText("RT @ibogost: Breaking: <B>No, They Are Not Giants</B>, confirms They Might Be Giants Manager");
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
							}
							tweetComposite.setSize(tweetComposite.computeSize(300,SWT.DEFAULT));
						}

						@Override public void widgetDefaultSelected(
								SelectionEvent e)
						{
							// TODO Auto-generated method stub
							
						}
				
					});
		}
		shell.open();
		int i=0;
		while(!shell.isDisposed())
		{
			if(!display.readAndDispatch()) display.sleep();
		}
		display.dispose();
	}
}
