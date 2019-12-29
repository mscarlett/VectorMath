package com.scarlett.vectormath.evaluator.extend;

import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcObject;

/**
 * Dummy symbolic evaluator (null evaluator).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcNullEvaluator implements CalcFunctionEvaluator {


	public CalcObject evaluate(CalcFunction input) {
		return null;
	}

}
