/*
 * Copyright 2007 (C) Tom Parker <thpr@users.sourceforge.net>
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
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import pcgen.base.formula.Formula;
import pcgen.cdom.base.CDOMObject;
import pcgen.cdom.base.ConcretePrereqObject;
import pcgen.cdom.enumeration.FormulaKey;
import pcgen.cdom.enumeration.IntegerKey;
import pcgen.cdom.enumeration.ListKey;
import pcgen.cdom.enumeration.MapKey;
import pcgen.cdom.enumeration.ObjectKey;
import pcgen.cdom.enumeration.StringKey;
import pcgen.cdom.enumeration.VariableKey;
import pcgen.core.prereq.Prerequisite;

public class ConsolidatedObjectCommitStrategy implements ObjectCommitStrategy
{
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

	public String getString(CDOMObject cdo, StringKey sk)
	{
		return cdo.get(sk);
	}

	public Integer getInteger(CDOMObject cdo, IntegerKey ik)
	{
		return cdo.get(ik);
	}

	public Formula getFormula(CDOMObject cdo, FormulaKey fk)
	{
		return cdo.get(fk);
	}

	public Formula getVariable(CDOMObject obj, VariableKey key)
	{
		return obj.get(key);
	}

	public Set<VariableKey> getVariableKeys(CDOMObject obj)
	{
		return obj.getVariableKeys();
	}

	public <T> T getObject(CDOMObject cdo, ObjectKey<T> ik)
	{
		return cdo.get(ik);
	}

	public <T> Changes<T> getListChanges(CDOMObject cdo, ListKey<T> lk)
	{
		return new CollectionChanges<T>(cdo.getListFor(lk), null, false);
	}

	public void put(CDOMObject cdo, StringKey sk, String s)
	{
		cdo.put(sk, s);
	}

	public <T> void put(CDOMObject cdo, ObjectKey<T> sk, T s)
	{
		cdo.put(sk, s);
	}

	public void remove(CDOMObject cdo, ObjectKey<?> sk)
	{
		cdo.remove(sk);
	}

	public void put(CDOMObject cdo, IntegerKey ik, Integer i)
	{
		cdo.put(ik, i);
	}

	public void put(CDOMObject cdo, FormulaKey fk, Formula f)
	{
		cdo.put(fk, f);
	}

	public void put(CDOMObject obj, VariableKey vk, Formula f)
	{
		obj.put(vk, f);
	}

	public boolean containsListFor(CDOMObject cdo, ListKey<?> key)
	{
		return cdo.containsListFor(key);
	}

	public <T> void addToList(CDOMObject cdo, ListKey<T> key, T value)
	{
		cdo.addToListFor(key, value);
	}

	public void removeList(CDOMObject cdo, ListKey<?> lk)
	{
		cdo.removeListFor(lk);
	}

	public <T> void removeFromList(CDOMObject cdo, ListKey<T> lk, T val)
	{
		cdo.removeFromListFor(lk, val);
	}

	public <K, V> void put(CDOMObject cdo, MapKey<K, V> mk, K key, V value)
	{
		cdo.addToMapFor(mk, key, value);
	}

	public <K, V> void remove(CDOMObject cdo, MapKey<K, V> mk, K key)
	{
		cdo.removeFromMapFor(mk, key);
	}

	public void put(ConcretePrereqObject cpo, Prerequisite p)
	{
		cpo.addPrerequisite(p);
	}

	public <K, V> MapChanges<K, V> getMapChanges(CDOMObject cdo, MapKey<K, V> mk)
	{
		return new MapChanges<K, V>(cdo.getMapFor(mk), null, false);
	}

	
	public Changes<Prerequisite> getPrerequisiteChanges(ConcretePrereqObject obj)
	{
		return new CollectionChanges<Prerequisite>(obj.getPrerequisiteList(),
				null, false);
	}

	public <T> void removePatternFromList(CDOMObject cdo, ListKey<T> lk,
			String pattern)
	{
		List<T> list = cdo.getListFor(lk);
		if (list == null || list.isEmpty())
		{
			return;
		}
		Pattern p = Pattern.compile(pattern);
		for (T obj : list)
		{
			if (p.matcher(obj.toString()).find())
			{
				cdo.removeFromListFor(lk, obj);
			}
		}
	}

	public void clearPrerequisiteList(ConcretePrereqObject cpo)
	{
		cpo.clearPrerequisiteList();
	}

	public <T> PatternChanges<T> getListPatternChanges(CDOMObject cdo,
			ListKey<T> lk)
	{
		return new PatternChanges<T>(cdo.getListFor(lk), null, false);
	}

	public boolean wasRemoved(CDOMObject cdo, ObjectKey<?> ok)
	{
		return false;
	}

	public void remove(CDOMObject cdo, StringKey sk)
	{
		cdo.remove(sk);
	}
	
	public boolean wasRemoved(CDOMObject cdo, StringKey sk)
	{
		return false;
	}

	public void remove(CDOMObject cdo, IntegerKey ik)
	{
		cdo.remove(ik);
	}

	public boolean wasRemoved(CDOMObject cdo, IntegerKey ik)
	{
		return false;
	}

}
