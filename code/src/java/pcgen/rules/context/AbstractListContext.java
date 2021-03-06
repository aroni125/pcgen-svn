/*
 * Copyright 2008 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.rules.context;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pcgen.base.util.DoubleKeyMap;
import pcgen.base.util.DoubleKeyMapToList;
import pcgen.base.util.HashMapToList;
import pcgen.base.util.ListSet;
import pcgen.base.util.MapToList;
import pcgen.base.util.TreeMapToList;
import pcgen.base.util.TripleKeyMap;
import pcgen.cdom.base.AssociatedPrereqObject;
import pcgen.cdom.base.CDOMList;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.CDOMObjectUtilities;
import pcgen.cdom.base.CDOMReference;
import pcgen.cdom.base.MasterListInterface;
import pcgen.cdom.base.PrereqObject;
import pcgen.cdom.base.SimpleAssociatedObject;
import pcgen.cdom.enumeration.AssociationKey;
import pcgen.cdom.reference.ReferenceUtilities;
import pcgen.core.Globals;

public abstract class AbstractListContext
{

//	private static final CDOMReference<? extends CDOMList<CDOMObject>> GRANTED = new CDOMDirectSingleRef<CDOMList<CDOMObject>>(
//			new GrantedList());

	private final TrackingListCommitStrategy edits = new TrackingListCommitStrategy();

	public URI getSourceURI()
	{
		return edits.getSourceURI();
	}

	public void setSourceURI(URI sourceURI)
	{
		edits.setSourceURI(sourceURI);
		getCommitStrategy().setSourceURI(sourceURI);
	}

	public URI getExtractURI()
	{
		return edits.getExtractURI();
	}

	public void setExtractURI(URI extractURI)
	{
		edits.setExtractURI(extractURI);
		getCommitStrategy().setExtractURI(extractURI);
	}

	public <T extends CDOMObject> AssociatedPrereqObject addToMasterList(
			String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<T>> list, T allowed)
	{
		return edits.addToMasterList(tokenName, owner, list, allowed);
	}

	public <T extends CDOMObject> void removeFromMasterList(
			String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<T>> list, T allowed)
	{
		edits.removeFromMasterList(tokenName, owner, list, allowed);
	}

	public void clearAllMasterLists(String tokenName, CDOMObject owner)
	{
		edits.clearAllMasterLists(tokenName, owner);
	}

	public <T extends CDOMObject> void clearMasterList(String tokenName,
			CDOMObject owner, CDOMReference<? extends CDOMList<T>> list)
	{
		edits.clearMasterList(tokenName, owner, list);
	}

	public <T extends CDOMObject> AssociatedPrereqObject addToList(
			String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<? super T>> list,
			CDOMReference<T> allowed)
	{
		return edits.addToList(tokenName, owner, list, allowed);
	}

	public <T extends CDOMObject> AssociatedPrereqObject removeFromList(
			String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<? super T>> list,
			CDOMReference<T> ref)
	{
		return edits.removeFromList(tokenName, owner, list, ref);
	}

	public void removeAllFromList(String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<?>> swl)
	{
		edits.removeAllFromList(tokenName, owner, swl);
	}

//	public <T extends CDOMObject> AssociatedPrereqObject grant(
//			String sourceToken, CDOMObject obj, CDOMReference<T> pro)
//	{
//		return addToList(sourceToken, obj, GRANTED, pro);
//	}
//
//	public <T extends CDOMObject> void remove(String sourceToken,
//			CDOMObject obj, CDOMReference<T> pro)
//	{
//		removeFromList(sourceToken, obj, GRANTED, pro);
//	}
//
//	public void removeAll(String tokenName, CDOMObject obj)
//	{
//		removeAllFromList(tokenName, obj, GRANTED);
//	}

//	public <T extends PrereqObject> AssociatedChanges<CDOMReference<T>> getChangesFromToken(
//			String tokenName, CDOMObject source, Class<T> cl)
//	{
//		AssociatedChanges<CDOMReference<CDOMObject>> assoc = getChangesInList(
//				tokenName, source, GRANTED);
//		boolean globalClear = assoc.includesGlobalClear();
//		MapToList<CDOMReference<CDOMObject>, AssociatedPrereqObject> added = assoc
//				.getAddedAssociations();
//		MapToList<CDOMReference<T>, AssociatedPrereqObject> add = new TreeMapToList<CDOMReference<T>, AssociatedPrereqObject>(
//				TokenUtilities.REFERENCE_SORTER);
//		if (added != null)
//		{
//			for (CDOMReference<CDOMObject> key : added.getKeySet())
//			{
//				if (cl.equals(key.getReferenceClass()))
//				{
//					add.addAllToListFor((CDOMReference<T>) key, added.getListFor(key));
//				}
//			}
//		}
//
//		MapToList<CDOMReference<T>, AssociatedPrereqObject> remove = new TreeMapToList<CDOMReference<T>, AssociatedPrereqObject>(
//				TokenUtilities.REFERENCE_SORTER);
//		MapToList<CDOMReference<CDOMObject>, AssociatedPrereqObject> removed = assoc
//				.getRemovedAssociations();
//		if (removed != null)
//		{
//			for (CDOMReference<CDOMObject> key : removed.getKeySet())
//			{
//				if (cl.equals(key.getReferenceClass()))
//				{
//					remove.addAllToListFor((CDOMReference<T>) key, removed.getListFor(key));
//				}
//			}
//		}
//
//		return new AssociatedCollectionChanges<CDOMReference<T>>(add, remove, globalClear);
//	}

	public void commit()
	{
		ListCommitStrategy commit = getCommitStrategy();
		for (CDOMReference list : edits.positiveMasterMap.getKeySet())
		{
			commitDirect(list);
		}
		for (CDOMReference list : edits.negativeMasterMap.getKeySet())
		{
			removeDirect(list);
		}
		for (URI uri : edits.globalClearSet.getKeySet())
		{
			for (CDOMObject owner : edits.globalClearSet
					.getSecondaryKeySet(uri))
			{
				for (CDOMReference<? extends CDOMList<?>> list : edits.globalClearSet
						.getListFor(uri, owner))
				{
					commit.removeAllFromList("FOO", owner, list);
				}
			}
		}
		for (URI uri : edits.negativeMap.getKeySet())
		{
			for (CDOMObject owner : edits.negativeMap.getSecondaryKeySet(uri))
			{
				CDOMObject neg = edits.negativeMap.get(uri, owner);
				Collection<CDOMReference<? extends CDOMList<? extends PrereqObject>>> modifiedLists = neg
						.getModifiedLists();
				for (CDOMReference list : modifiedLists)
				{
					remove(owner, neg, list);
				}
			}
		}
		for (URI uri : edits.positiveMap.getKeySet())
		{
			for (CDOMObject owner : edits.positiveMap.getSecondaryKeySet(uri))
			{
				CDOMObject neg = edits.positiveMap.get(uri, owner);
				Collection<CDOMReference<? extends CDOMList<? extends PrereqObject>>> modifiedLists = neg
						.getModifiedLists();
				for (CDOMReference list : modifiedLists)
				{
					add(owner, neg, list);
				}
			}
		}
		for (String token : edits.masterAllClear.getKeySet())
		{
			for (OwnerURI ou : edits.masterAllClear.getListFor(token))
			{
				commit.clearAllMasterLists(token, ou.owner);
			}
		}
		rollback();
	}

	private <T extends CDOMObject> void commitDirect(
			CDOMReference<? extends CDOMList<T>> list)
	{
		ListCommitStrategy commit = getCommitStrategy();
		for (OwnerURI ou : edits.positiveMasterMap.getSecondaryKeySet(list))
		{
			for (CDOMObject child : edits.positiveMasterMap.getTertiaryKeySet(
					list, ou))
			{
				AssociatedPrereqObject assoc = edits.positiveMasterMap.get(
						list, ou, child);
				AssociatedPrereqObject edge = commit.addToMasterList(assoc
						.getAssociation(AssociationKey.TOKEN), ou.owner, list,
						(T) child);
				Collection<AssociationKey<?>> associationKeys = assoc
						.getAssociationKeys();
				for (AssociationKey<?> ak : associationKeys)
				{
					setAssoc(assoc, edge, ak);
				}
				edge.addAllPrerequisites(assoc.getPrerequisiteList());
			}
		}
	}

	private <T extends CDOMObject> void removeDirect(
			CDOMReference<? extends CDOMList<T>> list)
	{
		ListCommitStrategy commit = getCommitStrategy();
		for (OwnerURI ou : edits.negativeMasterMap.getSecondaryKeySet(list))
		{
			for (CDOMObject child : edits.negativeMasterMap.getTertiaryKeySet(
					list, ou))
			{
				AssociatedPrereqObject assoc = edits.negativeMasterMap.get(
						list, ou, child);
				commit.removeFromMasterList(assoc
						.getAssociation(AssociationKey.TOKEN), ou.owner, list,
						(T) child);
			}
		}
	}

	public void rollback()
	{
		edits.decommit();
	}

	public Collection<CDOMReference<? extends CDOMList<? extends PrereqObject>>> getChangedLists(
			CDOMObject owner, Class<? extends CDOMList<?>> cl)
	{
		return getCommitStrategy().getChangedLists(owner, cl);
	}

	public <T extends CDOMObject> AssociatedChanges<CDOMReference<T>> getChangesInList(
			String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<T>> swl)
	{
		return getCommitStrategy().getChangesInList(tokenName, owner, swl);
	}

	public <T extends CDOMObject> AssociatedChanges<T> getChangesInMasterList(
			String tokenName, CDOMObject owner,
			CDOMReference<? extends CDOMList<T>> swl)
	{
		return getCommitStrategy().getChangesInMasterList(tokenName, owner, swl);
	}

	public Changes<CDOMReference> getMasterListChanges(String tokenName,
			CDOMObject owner, Class<? extends CDOMList<?>> cl)
	{
		return getCommitStrategy().getMasterListChanges(tokenName, owner, cl);
	}

	public boolean hasMasterLists()
	{
		return getCommitStrategy().hasMasterLists();
	}

	private <BT extends CDOMObject> void remove(CDOMObject owner,
			CDOMObject neg, CDOMReference<CDOMList<BT>> list)
	{
		ListCommitStrategy commit = getCommitStrategy();
		Collection<CDOMReference<BT>> mods = neg.getListMods(list);
		for (CDOMReference<BT> ref : mods)
		{
			for (AssociatedPrereqObject assoc : neg.getListAssociations(list,
					ref))
			{
				String token = assoc.getAssociation(AssociationKey.TOKEN);
				AssociatedPrereqObject edge = commit.removeFromList(token, owner,
						list, ref);
				Collection<AssociationKey<?>> associationKeys = assoc
						.getAssociationKeys();
				for (AssociationKey<?> ak : associationKeys)
				{
					setAssoc(assoc, edge, ak);
				}
				edge.addAllPrerequisites(assoc.getPrerequisiteList());
			}
		}
	}

	private <BT extends CDOMObject> void add(CDOMObject owner, CDOMObject neg,
			CDOMReference<CDOMList<BT>> list)
	{
		ListCommitStrategy commit = getCommitStrategy();
		Collection<CDOMReference<BT>> mods = neg.getListMods(list);
		for (CDOMReference<BT> ref : mods)
		{
			for (AssociatedPrereqObject assoc : neg.getListAssociations(list,
					ref))
			{
				String token = assoc.getAssociation(AssociationKey.TOKEN);
				AssociatedPrereqObject edge = commit.addToList(token, owner,
						list, ref);
				Collection<AssociationKey<?>> associationKeys = assoc
						.getAssociationKeys();
				for (AssociationKey<?> ak : associationKeys)
				{
					setAssoc(assoc, edge, ak);
				}
				edge.addAllPrerequisites(assoc.getPrerequisiteList());
			}
		}
	}

	private <T> void setAssoc(AssociatedPrereqObject assoc,
			AssociatedPrereqObject edge, AssociationKey<T> ak)
	{
		edge.setAssociation(ak, assoc.getAssociation(ak));
	}

	public static class TrackingListCommitStrategy implements ListCommitStrategy
	{

		protected static class CDOMShell extends CDOMObject
		{
			@Override
			public CDOMObject clone() throws CloneNotSupportedException
			{
				throw new CloneNotSupportedException();
			}

			@Override
			public boolean isType(String str)
			{
				return false;
			}
		}

		private URI sourceURI;

		private URI extractURI;

		public URI getExtractURI()
		{
			return extractURI;
		}

		public void setExtractURI(URI extractURI)
		{
			this.extractURI = extractURI;
		}

		public URI getSourceURI()
		{
			return sourceURI;
		}

		public void setSourceURI(URI sourceURI)
		{
			this.sourceURI = sourceURI;
		}

		/*
		 * TODO These maps (throughout this entire class) are probably problems
		 * because they are not using Identity characteristics
		 */
		private final TripleKeyMap<CDOMReference<? extends CDOMList<?>>, OwnerURI, CDOMObject, AssociatedPrereqObject> positiveMasterMap = new TripleKeyMap<CDOMReference<? extends CDOMList<?>>, OwnerURI, CDOMObject, AssociatedPrereqObject>();//HashMap.class, HashMap.class, IdentityHashMap.class);

		private final TripleKeyMap<CDOMReference<? extends CDOMList<?>>, OwnerURI, CDOMObject, AssociatedPrereqObject> negativeMasterMap = new TripleKeyMap<CDOMReference<? extends CDOMList<?>>, OwnerURI, CDOMObject, AssociatedPrereqObject>();//HashMap.class, HashMap.class, IdentityHashMap.class);
		
		private final HashMapToList<CDOMReference<? extends CDOMList<?>>, OwnerURI> masterClearSet = new HashMapToList<CDOMReference<? extends CDOMList<?>>, OwnerURI>();

		private final HashMapToList<String, OwnerURI> masterAllClear = new HashMapToList<String, OwnerURI>();

		public <T extends CDOMObject> AssociatedPrereqObject addToMasterList(
				String tokenName, CDOMObject owner,
				CDOMReference<? extends CDOMList<T>> list, T allowed)
		{
			SimpleAssociatedObject a = new SimpleAssociatedObject();
			a.setAssociation(AssociationKey.OWNER, owner);
			a.setAssociation(AssociationKey.TOKEN, tokenName);
			positiveMasterMap.put(list, new OwnerURI(sourceURI, owner),
					allowed, a);
			return a;
		}

		public <T extends CDOMObject> void removeFromMasterList(
				String tokenName, CDOMObject owner,
				CDOMReference<? extends CDOMList<T>> list, T allowed)
		{
			SimpleAssociatedObject a = new SimpleAssociatedObject();
			a.setAssociation(AssociationKey.OWNER, owner);
			a.setAssociation(AssociationKey.TOKEN, tokenName);
			negativeMasterMap.put(list, new OwnerURI(sourceURI, owner),
					allowed, a);
		}

		public Changes<CDOMReference> getMasterListChanges(String tokenName,
				CDOMObject owner, Class<? extends CDOMList<?>> cl)
		{
			OwnerURI lo = new OwnerURI(extractURI, owner);
			TreeSet<CDOMReference> list = new TreeSet(
					ReferenceUtilities.REFERENCE_SORTER);
			Set<CDOMReference<? extends CDOMList<?>>> set = positiveMasterMap
					.getKeySet();
			if (set != null)
			{
				LIST: for (CDOMReference<? extends CDOMList<?>> ref : set)
				{
					if (!cl.equals(ref.getReferenceClass()))
					{
						continue;
					}
					for (CDOMObject allowed : positiveMasterMap
							.getTertiaryKeySet(ref, lo))
					{
						AssociatedPrereqObject assoc = positiveMasterMap.get(
								ref, lo, allowed);
						if (owner.equals(assoc
								.getAssociation(AssociationKey.OWNER))
								&& tokenName.equals(assoc
										.getAssociation(AssociationKey.TOKEN)))
						{
							list.add(ref);
							continue LIST;
						}
					}
				}
			}
			set = negativeMasterMap.getKeySet();
			ArrayList<CDOMReference> removelist = new ArrayList<CDOMReference>();
			if (set != null)
			{
				LIST: for (CDOMReference<? extends CDOMList<?>> ref : set)
				{
					if (!cl.equals(ref.getReferenceClass()))
					{
						continue;
					}
					for (CDOMObject allowed : negativeMasterMap
							.getTertiaryKeySet(ref, lo))
					{
						AssociatedPrereqObject assoc = negativeMasterMap.get(
								ref, lo, allowed);
						if (owner.equals(assoc
								.getAssociation(AssociationKey.OWNER))
								&& tokenName.equals(assoc
										.getAssociation(AssociationKey.TOKEN)))
						{
							removelist.add(ref);
							continue LIST;
						}
					}
				}
			}
			return new CollectionChanges<CDOMReference>(list, removelist,
					masterAllClear.containsInList(tokenName, lo));
		}

		public <T extends CDOMObject> AssociatedChanges<T> getChangesInMasterList(
				String tokenName, CDOMObject owner,
				CDOMReference<? extends CDOMList<T>> swl)
		{
			MapToList<T, AssociatedPrereqObject> map = new TreeMapToList<T, AssociatedPrereqObject>(
					CDOMObjectUtilities.CDOM_SORTER);
			OwnerURI lo = new OwnerURI(extractURI, owner);
			Set<CDOMObject> added = positiveMasterMap
					.getTertiaryKeySet(swl, lo);
			for (CDOMObject lw : added)
			{
				AssociatedPrereqObject apo = positiveMasterMap.get(swl, lo, lw);
				if (tokenName.equals(apo.getAssociation(AssociationKey.TOKEN)))
				{
					map.addToListFor((T) lw, apo);
				}
			}
			MapToList<T, AssociatedPrereqObject> rmap = new TreeMapToList<T, AssociatedPrereqObject>(
					CDOMObjectUtilities.CDOM_SORTER);
			Set<CDOMObject> removed = negativeMasterMap
					.getTertiaryKeySet(swl, lo);
			for (CDOMObject lw : removed)
			{
				AssociatedPrereqObject apo = negativeMasterMap.get(swl, lo, lw);
				if (tokenName.equals(apo.getAssociation(AssociationKey.TOKEN)))
				{
					rmap.addToListFor((T) lw, apo);
				}
			}
			return new AssociatedCollectionChanges<T>(map, rmap, masterClearSet
					.containsInList(swl, lo));
		}

		public <T extends CDOMObject> void clearMasterList(String tokenName,
				CDOMObject owner, CDOMReference<? extends CDOMList<T>> list)
		{
			masterClearSet.addToListFor(list, new OwnerURI(sourceURI, owner));
		}

		public void clearAllMasterLists(String tokenName, CDOMObject owner)
		{
			masterAllClear.addToListFor(tokenName, new OwnerURI(sourceURI,
					owner));
		}

		private final DoubleKeyMap<URI, CDOMObject, CDOMObject> positiveMap = new DoubleKeyMap<URI, CDOMObject, CDOMObject>(HashMap.class, IdentityHashMap.class);

		private final DoubleKeyMap<URI, CDOMObject, CDOMObject> negativeMap = new DoubleKeyMap<URI, CDOMObject, CDOMObject>(HashMap.class, IdentityHashMap.class);

		private final DoubleKeyMapToList<URI, CDOMObject, CDOMReference<? extends CDOMList<?>>> globalClearSet = new DoubleKeyMapToList<URI, CDOMObject, CDOMReference<? extends CDOMList<?>>>(HashMap.class, IdentityHashMap.class);

		private CDOMObject getPositive(URI source, CDOMObject cdo)
		{
			CDOMObject positive = positiveMap.get(source, cdo);
			if (positive == null)
			{
				positive = new CDOMShell();
				positiveMap.put(source, cdo, positive);
			}
			return positive;
		}

		private CDOMObject getNegative(URI source, CDOMObject cdo)
		{
			CDOMObject negative = negativeMap.get(source, cdo);
			if (negative == null)
			{
				negative = new CDOMShell();
				negativeMap.put(source, cdo, negative);
			}
			return negative;
		}

		public <T extends PrereqObject> AssociatedPrereqObject addToList(
				String tokenName, CDOMObject owner,
				CDOMReference<? extends CDOMList<? super T>> list,
				CDOMReference<T> allowed)
		{
			SimpleAssociatedObject a = new SimpleAssociatedObject();
			a.setAssociation(AssociationKey.TOKEN, tokenName);
			getPositive(sourceURI, owner).putToList(list, allowed, a);
			return a;
		}

		public <T extends PrereqObject> AssociatedPrereqObject removeFromList(String tokenName,
				CDOMObject owner,
				CDOMReference<? extends CDOMList<? super T>> list,
				CDOMReference<T> ref)
		{
			SimpleAssociatedObject a = new SimpleAssociatedObject();
			a.setAssociation(AssociationKey.TOKEN, tokenName);
			getNegative(sourceURI, owner).putToList(list, ref, a);
			return a;
		}

		public Collection<CDOMReference<? extends CDOMList<? extends PrereqObject>>> getChangedLists(
				CDOMObject owner, Class<? extends CDOMList<?>> cl)
		{
			Set<CDOMReference<? extends CDOMList<? extends PrereqObject>>> list = new ListSet<CDOMReference<? extends CDOMList<? extends PrereqObject>>>();
			for (CDOMReference<? extends CDOMList<? extends PrereqObject>> ref : getPositive(
					extractURI, owner).getModifiedLists())
			{
				if (cl.equals(ref.getReferenceClass()))
				{
					list.add(ref);
				}
			}
			for (CDOMReference<? extends CDOMList<? extends PrereqObject>> ref : getNegative(
					extractURI, owner).getModifiedLists())
			{
				if (cl.equals(ref.getReferenceClass()))
				{
					list.add(ref);
				}
			}
			List<CDOMReference<? extends CDOMList<?>>> globalClearList = globalClearSet
					.getListFor(extractURI, owner);
			if (globalClearList != null)
			{
				for (CDOMReference<? extends CDOMList<? extends PrereqObject>> ref : globalClearList)
				{
					if (cl.equals(ref.getReferenceClass()))
					{
						list.add(ref);
					}
				}
			}
			return list;
		}

		public void removeAllFromList(String tokenName, CDOMObject owner,
				CDOMReference<? extends CDOMList<?>> swl)
		{
			globalClearSet.addToListFor(sourceURI, owner, swl);
		}

		public <T extends PrereqObject> AssociatedChanges<CDOMReference<T>> getChangesInList(
				String tokenName, CDOMObject owner,
				CDOMReference<? extends CDOMList<T>> swl)
		{
			return new ListChanges<T>(tokenName,
					getPositive(extractURI, owner), getNegative(extractURI,
							owner), swl, globalClearSet.containsInList(
							extractURI, owner, swl));
		}

		public boolean hasMasterLists()
		{
			return !positiveMasterMap.isEmpty() && !masterClearSet.isEmpty()
					&& !masterAllClear.isEmpty();
		}

		public void decommit()
		{
			masterAllClear.clear();
			masterClearSet.clear();
			positiveMasterMap.clear();
			negativeMasterMap.clear();
			positiveMap.clear();
			negativeMap.clear();
			globalClearSet.clear();
		}

		public boolean equalsTracking(ListCommitStrategy obj)
		{
			if (obj instanceof TrackingListCommitStrategy)
			{
				TrackingListCommitStrategy other = (TrackingListCommitStrategy) obj;
				return other.masterAllClear.equals(this.masterAllClear)
						&& other.masterClearSet.equals(this.masterClearSet)
						&& other.positiveMasterMap.equals(this.positiveMasterMap)
						&& other.negativeMasterMap.equals(this.negativeMasterMap);
			}
			return false;
		}

		public void purge(CDOMObject cdo)
		{
			positiveMap.remove(sourceURI, cdo);
			negativeMap.remove(sourceURI, cdo);
			globalClearSet.removeListFor(sourceURI, cdo);
		}
	}

	private static class OwnerURI
	{
		public final CDOMObject owner;
		public final URI source;

		public OwnerURI(URI sourceURI, CDOMObject cdo)
		{
			source = sourceURI;
			owner = cdo;
		}

		@Override
		public int hashCode()
		{
			return owner.hashCode();
		}

		@Override
		public boolean equals(Object o)
		{
			if (o instanceof OwnerURI)
			{
				OwnerURI other = (OwnerURI) o;
				if (source == null)
				{
					if (other.source != null)
					{
						return false;
					}
				}
				else
				{
					if (!source.equals(other.source))
					{
						return false;
					}
				}
				return owner.equals(other.owner);
			}
			return false;
		}
	}
	
	public boolean masterListsEqual(AbstractListContext lc)
	{
		return getCommitStrategy().equalsTracking(lc.getCommitStrategy());
	}
	
	protected abstract ListCommitStrategy getCommitStrategy();
	
	/**
	 * Create a copy of any associations to the original object and link them 
	 * to the new object. This will scan lists such as ClassSpellLists and 
	 * DomainSpellLists which may link to the original object. For each 
	 * association found, a new association will be created linking to the new object 
	 * and the association will be added to the list.
	 * 
	 * @param <T>    The type of CDOMObject being copied (e.g. Spell, Domain etc)
	 * @param cdoOld The original object being copied. 
	 * @param cdoNew The new object to be linked in.
	 */
	@SuppressWarnings("unchecked")
	public <T extends CDOMObject> void cloneInMasterLists(T cdoOld, T cdoNew)
	{
		MasterListInterface masterLists = Globals.getMasterLists();
		for (CDOMReference ref : masterLists.getActiveLists())
		{
			Collection<AssociatedPrereqObject> assocs = masterLists
					.getAssociations(ref, cdoOld);
			if (assocs != null)
			{
				for (AssociatedPrereqObject apo : assocs)
				{
//					Logging.debugPrint("Found assoc from " + ref + " to "
//							+ apo.getAssociationKeys() + " / "
//							+ apo.getAssociation(AssociationKey.OWNER));
					AssociatedPrereqObject newapo = getCommitStrategy()
							.addToMasterList(
									apo.getAssociation(AssociationKey.TOKEN),
									cdoNew, ref, cdoNew);
					newapo.addAllPrerequisites(apo.getPrerequisiteList());
					for (AssociationKey assocKey : apo.getAssociationKeys())
					{
						if (assocKey != AssociationKey.TOKEN
								&& assocKey != AssociationKey.OWNER)
						{
							newapo.setAssociation(assocKey, apo
									.getAssociation(assocKey));
						}
					}
				}
			}
		}
	}
}
