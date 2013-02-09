/**
 * FractalFrameEvaluator.java
 * Author: Mateusz Szpakowski
 * License: LGPL v2.0
 */

package jfractus.app;

import java.util.Iterator;

public class FractalFrameEvaluator
{
	private float time;
	private Iterator<FractalKeyFrame> keyFrameIter;
	private FractalKeyFrame currentKeyFrame;
	private FractalKeyFrame nextKeyFrame;
	
	public FractalFrameEvaluator(Iterator<FractalKeyFrame> iter)
	{
		time = 0.0f;
		keyFrameIter = iter;
		currentKeyFrame = null;
	}
	
	public FractalFrame getFrame(float duration)
	{
		if (currentKeyFrame == null)
		{
			currentKeyFrame = keyFrameIter.next();
			nextKeyFrame = keyFrameIter.next();
			time = 0.0f;
			return new FractalFrame(currentKeyFrame.getTransform()); 
		}
		else
		{
			time += duration;
			while (time >= currentKeyFrame.getInterval())
			{
				time -= currentKeyFrame.getInterval();
				currentKeyFrame = nextKeyFrame;
				nextKeyFrame = keyFrameIter.next();
			}
			if (time != 0.0f)
			{
				if (nextKeyFrame == null)
					return null;
				return currentKeyFrame.evaluateFrame(nextKeyFrame, time);
			}
			else
			{
				if (currentKeyFrame == null)
					return null;
				return new FractalFrame(currentKeyFrame.getTransform());
			}
		}
	}
}
