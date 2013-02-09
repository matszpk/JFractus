/**
 * AbstractRenderThread.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.locks.*;
import java.util.concurrent.atomic.*;

public abstract class AbstractRenderThread extends Thread
{
	protected ReentrantLock finishLock;
	protected Condition finishCond;
	protected AtomicBoolean doCancel;
	protected AtomicInteger globalFragmentIndex;
	protected BlockingQueue<Integer> fragmentQueue;
	protected int fragmentsNum;
	
	public static class SharedData
	{
		protected ReentrantLock finishLock;
    	protected Condition finishCond;
    	protected AtomicBoolean doCancel;
    	protected AtomicInteger globalFragmentIndex;
    	protected BlockingQueue<Integer> fragmentQueue;
    	protected int fragmentsNum;
    	
    	public SharedData()
    	{
    		this.finishLock = new ReentrantLock();
    		this.finishCond = finishLock.newCondition();
    		this.doCancel = new AtomicBoolean();
    		this.globalFragmentIndex = new AtomicInteger();
    		this.fragmentQueue = new LinkedBlockingDeque<Integer>();
    	}
    	
    	public SharedData(int num)
    	{
    		this();
    		this.fragmentsNum = num;
    	}
    	
    	public ReentrantLock getFinishLock()
    	{
    		return finishLock;
    	}
    	public Condition getFinishCond()
    	{
    		return finishCond;
    	}
    	public AtomicBoolean getCancelIndicator()
    	{
    		return this.doCancel;
    	}
    	public AtomicInteger getGlobalFragmentIndex()
    	{
    		return this.globalFragmentIndex;
    	}
    	public int getFragmentsNumber()
    	{
    		return this.fragmentsNum;
    	}
    	public void setFragmentsNumber(int num)
    	{
    		this.fragmentsNum = num;
    	}
    	public BlockingQueue<Integer> getFragmentQueue()
    	{
    		return fragmentQueue;
    	}
    	
    	public void reset()
    	{
    		doCancel.set(false);
    		globalFragmentIndex.set(0);
    	}
	}
	
	public void setSharedData(SharedData data)
	{
		finishLock = data.getFinishLock();
		finishCond = data.getFinishCond();
		doCancel = data.getCancelIndicator();
		globalFragmentIndex = data.getGlobalFragmentIndex();
		fragmentsNum = data.getFragmentsNumber();
		fragmentQueue = data.getFragmentQueue();
	}
	
	public void signalizeFinish()
	{
		finishLock.lock();
		finishCond.signal();
		finishLock.unlock();
	}
	
	public BlockingQueue<Integer> getFragmentQueue()
	{
		return fragmentQueue;
	}
	
	public abstract void renderFragment(int index);
	
	public void run()
	{
		while (true)
		{
			int x = globalFragmentIndex.getAndIncrement();
			//System.out.printf("X:%d\n", x);
			/*try
			{ Thread.sleep(10); }
			catch(InterruptedException e)
			{ }*/
			renderFragment(x);
			if (doCancel.get())
				break;
			fragmentQueue.offer(new Integer(x));
			if (x+1 >= fragmentsNum)
			{
				if (x+1 == fragmentsNum)
					signalizeFinish();
				break;
			}
		}
	}
	
}
