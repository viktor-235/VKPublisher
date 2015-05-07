package com.vkpublisher;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class AccessForm
{
	private String url = null;
	
	public void openForm(String url)
	{
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		shell.setSize(650, 500);
		
		Browser browser = null;
		try
		{
			browser = new Browser(shell, SWT.NONE);
		}
		catch (SWTError e)
		{
			/*
			 * The Browser widget throws an SWTError if it fails to instantiate
			 * properly. Application code should catch this SWTError and disable
			 * any feature requiring the Browser widget. Platform requirements
			 * for the SWT Browser widget are available from the SWT FAQ
			 * website.
			 */
		}
		if (browser != null)
		{
			/* The Browser widget can be used */
			this.url = url;
			browser.setUrl(url);
		}
		
		shell.open();
		while (!shell.isDisposed())
		{
			if (!display.readAndDispatch())
				display.sleep();
			else
				if (!browser.isDisposed())
				{
					this.url = browser.getUrl();
					//System.out.println(browser.getCookie("TestCockie", "http://vk.com"));
				}
		}
		display.dispose();
	}
	
	public String getUrl()
	{
		return url;
	}
}
