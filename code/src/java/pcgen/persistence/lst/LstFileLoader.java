/*
 * LstFileLoader.java
 * Copyright 2003 (C) David Hibbs <sage_sam@users.sourceforge.net>
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Created on September 22, 2003, 11:29 AM
 *
 * Current Ver: $Revision$ <br>
 * Last Editor: $Author$ <br>
 * Last Edited: $Date$
 */
package pcgen.persistence.lst;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

import pcgen.cdom.base.Constants;
import pcgen.core.SettingsHandler;
import pcgen.core.utils.CoreUtility;
import pcgen.core.utils.MessageType;
import pcgen.core.utils.ShowMessageDelegate;
import pcgen.persistence.PersistenceLayerException;
import pcgen.util.Logging;

/**
 * This class is a base class for LST file loaders.
 * 
 * <p/>
 * This class lays out a skeleton for LST file loading, setting
 * up shared features and functions for loading and parsing of files.
 * 
 * <p/>
 * This class extends the <tt>Observable</tt> class so interested observers 
 * will be notified of the progress of loading files.
 * 
 * <p />
 * Instances of LstFileLoader or its subclasses are not thread-safe,
 * so any thread should only acccess a single loader (or group of loaders)
 * at a time.
 */
public final class LstFileLoader
{
	private LstFileLoader()
	{
		//Utility class
	}
	
	/** The String that represents the start of a line comment. */
	public static final char LINE_COMMENT_CHAR = '#'; //$NON-NLS-1$

	/** The String that separates individual objects */
	public static final String LINE_SEPARATOR_REGEXP = "(\r|\n)"; //$NON-NLS-1$

	/**
	 * This method reads the given URL and stores its contents in the provided
	 * data buffer, returning a URL to the specified file for use in log/error
	 * messages by its caller.
	 *
	 * @param url        String path of the URL to read -- MUST be a URL path,
	 *                   not a file!
	 * @param dataBuffer StringBuffer to buffer the file content into
	 * @return URL pointing to the actual file read, for use in debug/log
	 *         messages
	 * @throws PersistenceLayerException 
	 */
	public static StringBuilder readFromURI(URI uri) throws PersistenceLayerException
	{
		if (uri == null)
		{
			// We have a problem!
			throw new PersistenceLayerException(
				"LstFileLoader.readFromURI() received a null uri parameter!");
		}
		
		StringBuilder dataBuffer = new StringBuilder();
		URL url;
		try {
			url = uri.toURL();
		} catch (MalformedURLException e) {
			throw new PersistenceLayerException(
					"LstFileLoader.readFromURI() could not convert parameter to a URL: "
							+ e.getLocalizedMessage());
		}
		InputStream inputStream = null;

		try
		{
			//only load local urls, unless loading of URLs is allowed
			if (!CoreUtility.isNetURL(url) || SettingsHandler.isLoadURLs())
			{
				// Get the URL and open the stream
				inputStream = url.openStream();

				// Read from the stream
				final InputStreamReader ir =
						new InputStreamReader(inputStream, "UTF-8"); //$NON-NLS-1$

				// Buffer the stream content
				final char[] b = new char[512];
				int n;

				n = ir.read(b, 0, 1);
				/*
				 * Take out the optional BOM: This is a pre-Java 1.6 workaround
				 * for Sun B-U-G 4508058, see:
				 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4508058
				 */
				if (n == 1 && b[0] != '\uFEFF')
				{
					dataBuffer.append(b, 0, 1);
				}
				while ((n = ir.read(b)) > 0)
				{
					dataBuffer.append(b, 0, n);
				}
			}
			else
			{
				// Just to protect people from using web
				// sources without their knowledge,
				// we added a preference.
				ShowMessageDelegate
					.showMessageDialog(
						"Preferences are currently set to NOT allow\nloading of "
							+ "sources from web links. \n" + url
							+ " is a web link", Constants.APPLICATION_NAME,
						MessageType.ERROR);
				// aURL = null; //currently unnecessary reassignment 
			}
		}
		catch (IOException ioe)
		{
			// Don't throw an exception here because a simple
			// file not found will prevent ANY other files from
			// being loaded/processed -- NOT what we want
			Logging.errorPrint("ERROR:" + url + "\n" + "Exception type:"
				+ ioe.getClass().getName() + "\n" + "Message:"
				+ ioe.getMessage());
		}
		finally
		{
			if (inputStream != null)
			{
				try
				{
					inputStream.close();
				}
				catch (IOException e2)
				{
					Logging.errorPrint(
						"Can't close inputStream in LstSystemLoader.initFile",
						e2);
				}
			}
		}
		return dataBuffer;
	}
}
