/*
 * Icons.java
 * Copyright 2010 Connor Petty <cpmeister@users.sourceforge.net>
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
 * Created on Apr 4, 2010, 5:24:43 PM
 */
package pcgen.gui2.tools;

import java.net.URL;
import java.util.EnumMap;
import java.util.Map;
import javax.swing.ImageIcon;

/**
 *
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 */
public enum Icons
{

	About16(".gif"),
	Add16(".gif"),
	AlignBottom16(".gif"),
	AlignCenter16(".gif"),
	AlignJustifyHorizontal16(".gif"),
	AlignJustifyVertical16(".gif"),
	AlignLeft16(".gif"),
	AlignRight16(".gif"),
	AlignTop16(".gif"),
	BBack16(".gif"),
	BBack24(".gif"),
	Back16(".gif"),
	Bookmarks16(".gif"),
	button_arrow_up(".png"),
	button_arrow_down(".png"),
	Checklist16(".gif"),
	Close16(".gif"),
	CloseAll16(".gif"),
	CloseX9(".gif"),
	ComposeMail16(".gif"),
	ContextualHelp16(".gif"),
	Copy16(".gif"),
	CustomZoom16(".gif"),
	Cut16(".gif"),
	DDown16(".gif"),
	DDown24(".gif"),
	DefaultPortrait(".gif"),
	Delete16(".gif"),
	Down16(".gif"),
	Edit16(".gif"),
	EditZoom16(".gif"),
	Export16(".gif"),
	FForward16(".gif"),
	FForward24(".gif"),
	Find16(".gif"),
	FindAgain16(".gif"),
	Forward16(".gif"),
	Help16(".gif"),
	History16(".gif"),
	Import16(".gif"),
	Information16(".gif"),
	MediaStop16(".gif"),
	New16(".gif"),
	NewEnvelope(".gif"),
	NewNPC16(".gif"),
	Open16(".gif"),
	PageSetup16(".gif"),
	Paste16(".gif"),
	PcgenIcon(".gif"),
	Preferences16(".gif"),
	PreferencesHighlightBlue16(".gif"),
	Print16(".gif"),
	PrintPreview16(".gif"),
	Properties16(".gif"),
	Redo16(".gif"),
	Refresh16(".gif"),
	Remove16(".gif"),
	RemovePreferences16(".gif"),
	RemoveZoom16(".gif"),
	Replace16(".gif"),
	Save16(".gif"),
	SaveAll16(".gif"),
	SaveAs16(".gif"),
	Search16(".gif"),
	SendMail16(".gif"),
	SplashPcgen(".gif"),
	Stop16(".gif"),
	TipOfTheDay16(".gif"),
	TipOfTheDay24(".gif"),
	UUp16(".gif"),
	UUp24(".gif"),
	Undo16(".gif"),
	Up16(".gif"),
	XButton_Click(".gif"),
	XButton_Roll(".gif"),
	XButton_Stat(".gif"),
	Zoom16(".gif"),
	ZoomHighlightBlue16(".gif"),
	ZoomIn16(".gif"),
	ZoomOut16(".gif"),
	gmgen_icon(".png"),
	stock_broken_image("-16.png"),
	stock_color("-16.png"),
	stock_copy("-16.png"),
	stock_cut("-16.png"),
	stock_export("-16.png"),
	stock_insert_graphic("-16.png"),
	stock_insert_table("-16.png"),
	stock_list_bulet("-16.png"),
	stock_list_enum("-16.png"),
	stock_new("-16.png"),
	stock_paste("-16.png"),
	stock_revert("-16.png"),
	stock_save("-16.png"),
	stock_spellcheck("-16.png"),
	stock_text_align_center("-16.png"),
	stock_text_align_left("-16.png"),
	stock_text_align_right("-16.png"),
	stock_text_bold("-16.png"),
	stock_text_italic("-16.png"),
	stock_text_underline("-16.png");
	
	
	private static final String RESOURCE_URL = "/pcgen/resources/images/";
	private static final Map<Icons, ImageIcon> iconMap = new EnumMap<Icons, ImageIcon>(Icons.class);
	public static final String RESOURCE_APP_ICON = "PCGenApp.png";
	private final String extension;

	private Icons(String ex)
	{
		this.extension = ex;
	}

	/**
	 * Fetch an <code>ImageIcon</code> relative to the calling
	 * location.
	 *
	 * @param fileName <code>String</code>, the path to the
	 * <code>IconImage> source
	 *
	 * @return <code>ImageIcon</code>, the icon or <code>null</code>
	 * on failure
	 */
	public static ImageIcon createImageIcon(String fileName)
	{
		fileName = RESOURCE_URL + fileName;
		final URL iconURL = Icons.class.getResource(fileName);
		if (iconURL == null)
		{
			return null;
		}
		return new ImageIcon(iconURL);
	}

	public ImageIcon getImageIcon()
	{
		ImageIcon image = iconMap.get(this);
		if (image == null)
		{
			image = createImageIcon(toString() + extension);
			iconMap.put(this, image);
		}
		return image;
	}

}
