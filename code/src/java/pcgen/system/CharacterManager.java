/*
 * CharacterManager.java
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
 * Created on May 8, 2010, 5:13:06 PM
 */
package pcgen.system;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;

import pcgen.cdom.base.Constants;
import pcgen.core.Globals;
import pcgen.core.PlayerCharacter;
import pcgen.core.facade.CampaignFacade;
import pcgen.core.facade.CharacterFacade;
import pcgen.core.facade.DataSetFacade;
import pcgen.core.facade.GameModeFacade;
import pcgen.core.facade.PartyFacade;
import pcgen.core.facade.SourceSelectionFacade;
import pcgen.core.facade.UIDelegate;
import pcgen.core.facade.util.AbstractListFacade;
import pcgen.core.facade.util.ListFacade;
import pcgen.core.facade.util.ListFacades;
import pcgen.gui2.facade.CharacterFacadeImpl;
import pcgen.gui2.facade.PartyFacadeImpl;
import pcgen.io.PCGFile;
import pcgen.io.PCGIOHandler;
import pcgen.util.Logging;

/**
 * This class stores the characters that are currently opened by
 * PCGen. It also handles creating new characters and opening
 * characters from files. The getCharacters method returns
 * a listenable list that allows users of this class to not
 * only see what characters are open but to easily track any
 * changes to the list of available characters.
 *
 * <br/>
 * Last Editor: $Author$
 * Last Edited: $Date$
 * 
 * @author Connor Petty <cpmeister@users.sourceforge.net>
 * @version $Revision$
 */
public class CharacterManager
{

	private static final PartyFacadeImpl characters;
	private static final RecentFileList recentCharacters;
	private static final RecentFileList recentParties;

	static
	{
		characters = new PartyFacadeImpl();
		recentCharacters = new RecentFileList(PCGenSettings.RECENT_CHARACTERS);
		recentParties = new RecentFileList(PCGenSettings.RECENT_PARTIES);
	}

	private CharacterManager()
	{
	}

	public static CharacterFacade createNewCharacter(UIDelegate delegate, DataSetFacade dataset)
	{
		CharacterFacade character = new CharacterFacadeImpl(delegate, dataset);
		String name = createNewCharacterName();
		character.setName(name);
		characters.addElement(character);
		return character;
	}

	public static ListFacade<File> getRecentCharacters()
	{
		return recentCharacters;
	}

	public static ListFacade<File> getRecentParties()
	{
		return recentParties;
	}

	/**
	 * This opens an existing character from a file and adds it to the
	 * list of open characters. If there is a character already open
	 * that uses this file, then this method does nothing.
	 * @param file the file to load this character from
	 * @param delegate the UIDelegate that this character will use
	 * @param dataset the dataset that this will be loaded with
	 * @return The character that was opened.
	 */
	public static CharacterFacade openCharacter(File file, UIDelegate delegate, DataSetFacade dataset)
	{
		@SuppressWarnings("rawtypes")
		List campaigns = ListFacades.wrap(dataset.getCampaigns());		
		@SuppressWarnings("unchecked")
		final PlayerCharacter newPC = new PlayerCharacter(false, campaigns);
		final PCGIOHandler ioHandler = new PCGIOHandler();
		ioHandler.read(newPC, file.getAbsolutePath());
		newPC.insertBonusLanguageAbility();

		for (String error : ioHandler.getErrors())
		{
			delegate.showErrorMessage(Constants.APPLICATION_NAME, "Error: " + error);
		}
		for (String warning : ioHandler.getWarnings())
		{
			delegate.showWarningMessage(Constants.APPLICATION_NAME, "Warning: " + warning);
		}

		// if we've had errors, then abort trying to add the new PC, it's most likely "broken"
		if (!ioHandler.getErrors().isEmpty())
		{
			return null;
		}
		// if it's not broken, then only warnings should have been generated, and we won't count those
		// Set the filename so that future checks to see if file already loaded will work
		newPC.setFileName(file.getAbsolutePath());
		Globals.getPCList().add(newPC);

		CharacterFacade character = new CharacterFacadeImpl(newPC, delegate, dataset);
		characters.addElement(character);
		return character;
	}

	/**
	 * This opens an existing party from a file and adds all characters to the
	 * list of open characters.
	 *  
	 * @param file the file to load this party from
	 * @param delegate the UIDelegate that these characters will use
	 * @param dataset the dataset that this will be loaded with
	 * @return The party that was opened.
	 */
	public static PartyFacade openParty(File file, final UIDelegate delegate, final DataSetFacade dataset)
	{
		PCGIOHandler ioHandler = new PCGIOHandler();
		for (File charFile : ioHandler.readCharacterFileList(file))
		{
			openCharacter(charFile, delegate, dataset);
		}
		characters.setFile(file);
		return characters;
	}

	public static SourceSelectionFacade getRequiredSourcesForParty(File pcpFile, UIDelegate delegate)
	{
		PCGIOHandler ioHandler = new PCGIOHandler();
		List<File> files = ioHandler.readCharacterFileList(pcpFile);
		if (files == null || files.isEmpty())
		{
			return null;
		}
		GameModeFacade gameMode = null;
		HashSet<CampaignFacade> campaignSet = new HashSet<CampaignFacade>();
		for (File file : files)
		{
			SourceSelectionFacade selection = getRequiredSourcesForCharacter(file, delegate);
			if (selection == null)
			{
				Logging.errorPrint("Failed to find sources in: " + file.getAbsolutePath());
				continue;
			}
			GameModeFacade game = selection.getGameMode().getReference();
			if (gameMode == null)
			{
				gameMode = game;
			}
			else if (gameMode != game)
			{
				Logging.errorPrint("Characters in " + pcpFile.getAbsolutePath()
						+ " do not share the same game mode");
				return null;
			}

			for (CampaignFacade campaign : selection.getCampaigns())
			{
				campaignSet.add(campaign);
			}
		}
		//TODO: check to make sure that the campaigns are compatable

		return FacadeFactory.createSourceSelection(gameMode, new ArrayList<CampaignFacade>(campaignSet));
	}

	/**
	 * 
	 * @param pcgFile a character file
	 * @param delegate  The UIDelegate used to display message to the user
	 * @return a SourceSelectionFacade or null if no sources could be found
	 */
	public static SourceSelectionFacade getRequiredSourcesForCharacter(File pcgFile, UIDelegate delegate)
	{
		if (!PCGFile.isPCGenCharacterFile(pcgFile))
		{
			throw new IllegalArgumentException();
		}

		final PCGIOHandler ioHandler = new PCGIOHandler();
		SourceSelectionFacade selection = ioHandler.readSources(pcgFile);
		if (!ioHandler.getErrors().isEmpty())
		{
			for (String msg : ioHandler.getErrors())
			{
				delegate.showErrorMessage(Constants.APPLICATION_NAME, msg);
				Logging.errorPrint(msg);
			}
			return null;
		}
		return selection;
	}

	/**
	 * Saves this character to the character's file specified
	 * by character.getFileRef().getReference()
	 * This is expected to be called before a character is to
	 * be removed from the list of open characters.
	 * @param character the character to be saved
	 * @return true if the save succeeded, false if not 
	 */
	public static boolean saveCharacter(CharacterFacade character)
	{
		File file = character.getFileRef().getReference();
		if (StringUtils.isBlank(file.getName()))
		{
			return false;
		}

		if (character instanceof CharacterFacadeImpl)
		{
			UIDelegate delegate = character.getUIDelegate();
			try
			{
				((CharacterFacadeImpl) character).save();
			}
			catch (NullPointerException e)
			{
				Logging.errorPrint("Could not save " + character.getNameRef().getReference(), e);
				delegate.showErrorMessage(Constants.APPLICATION_NAME,
										  "Could not save " + character.getNameRef().getReference());
				return false;
			}
			catch (IOException e)
			{
				Logging.errorPrint("Could not save " + character.getNameRef().getReference(), e);
				delegate.showErrorMessage(Constants.APPLICATION_NAME,
										  "Could not save " + character.getNameRef().getReference()
						+ " due to the error:\n" + e.getMessage());
				return false;
			}
		}
		else
		{
			Logging.errorPrint("Could not save " + character.getNameRef().getReference()
					+ " due to unexpected class of character: "
					+ character.getClass().getCanonicalName());
			return false;
		}

		recentCharacters.addRecentFile(file);
		return true;
	}

	public static boolean saveCurrentParty()
	{
		File file = characters.getFileRef().getReference();
		if (file == null)
		{
			return false;
		}
		characters.save();
		return true;
	}

	/**
	 * removes a character from the list of open characters.
	 * This is called at the end of a close character operation.
	 * Note: this operation does not save the character!
	 * @param character the character to be closed
	 */
	public static void removeCharacter(CharacterFacade character)
	{
		characters.removeElement(character);
		File charFile = character.getFileRef().getReference();
		recentCharacters.addRecentFile(charFile);
		if (characters.isEmpty())
		{
			recentParties.addRecentFile(characters.getFileRef().getReference());
			characters.setFile(null);
		}
	}

	public static void removeAllCharacters()
	{
		for (CharacterFacade characterFacade : characters)
		{
			recentCharacters.addRecentFile(characterFacade.getFileRef().getReference());
		}
		characters.clearContents();
		recentParties.addRecentFile(characters.getFileRef().getReference());
		characters.setFile(null);
	}

	public static PartyFacade getCharacters()
	{
		return characters;
	}

	private static String createNewCharacterName()
	{
		String name = "Unnamed ";
		int i = 1;
		while (isNameUsed(name + i))
		{
			i++;
		}
		return name + i;
	}

	private static boolean isNameUsed(String name)
	{
		for (CharacterFacade character : characters)
		{
			if (character.getNameRef().getReference().equals(name))
			{
				return true;
			}
		}
		return false;
	}

}

final class RecentFileList extends AbstractListFacade<File>
{

	private static final int MAX_RECENT_FILES = 5;
	private final LinkedList<File> fileList = new LinkedList<File>();
	private final String contextProp;

	public RecentFileList(String contextProp)
	{
		this.contextProp = contextProp;
		String[] recentFiles = PCGenSettings.getInstance().getStringArray(contextProp);
		if (!ArrayUtils.isEmpty(recentFiles))
		{
			URI userdir = new File(ConfigurationSettings.getUserDir()).toURI();
			for (int i = recentFiles.length-1; i >= 0 ; i--)
			{
				addRecentFile(new File(userdir.resolve(recentFiles[i])));
			}
		}
	}

	private void updateRecentFileProp()
	{
		URI userdir = new File(ConfigurationSettings.getUserDir()).toURI();

		List<String> uris = new ArrayList<String>(fileList.size());
		for (File file : fileList)
		{
			URI uri = userdir.relativize(file.toURI());
			uris.add(uri.toString());
		}
		PCGenSettings.getInstance().setStringArray(contextProp, uris);
	}

	public void addRecentFile(File file)
	{
		if (file == null || !file.isFile())
		{
			return;
		}
		//Remove the file if it already exists, that way it gets moved to the top
		int index = indexOf(file);
		if (index != -1)
		{
			File oldFile = fileList.remove(index);
			fireElementRemoved(this, oldFile, index);
		}
		//add it to the front
		fileList.addFirst(file);
		fireElementAdded(this, file, 0);
		//then remove any overflowing files
		if (fileList.size() > MAX_RECENT_FILES)
		{
			File oldFile = fileList.removeLast();
			fireElementRemoved(this, oldFile, MAX_RECENT_FILES);
		}
		updateRecentFileProp();
	}

	public File getElementAt(int index)
	{
		return fileList.get(index);
	}

	public int getSize()
	{
		return fileList.size();
	}

	@Override
	public boolean containsElement(File element)
	{
		return indexOf(element) != -1;
	}

	private int indexOf(File element)
	{
		if (element != null)
		{
			for (int i = 0; i < fileList.size(); i++)
			{
				if (fileList.get(i).getAbsolutePath().equals(element.getAbsolutePath()))
				{
					return i;
				}
			}
		}
		return -1;
	}

}
