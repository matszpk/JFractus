/**
 * UndoManager.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app.ui;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.ListIterator;

public class UndoManager<E> extends LinkedList<E> implements Serializable
{
    private static final long serialVersionUID = 3676482978507830680L;

    private ListIterator<E> opIter;
    
	public UndoManager()
	{
		opIter = listIterator();
	}
	
	public void clear()
	{
		super.clear();
		opIter = listIterator();
	}
	
	public void doOp(E op)
	{
		while(opIter.hasNext())
		{
			opIter.next();
			opIter.remove();
		}
		opIter.add(op);
	}
	
	public boolean hasUndo()
	{
		return opIter.hasPrevious();
	}
	public E undoOp()
	{
		return opIter.previous();
	}
	public boolean hasRedo()
	{
		return opIter.hasNext();
	}
	public E redoOp()
	{
		return opIter.next();
	}
}
