/**
 * 
 */
package com.scarlett.vectormath.evaluator.extend;

import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcObject;

/**
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcConstantEvaluator implements CalcFunctionEvaluator {
	
	CalcObject constant;
	/**
	 * 
	 */
	public CalcConstantEvaluator(CalcObject obj) {
		constant = obj;
	}

	/* (non-Javadoc)
	 * @see javacalculus.evaluator.CalcFunctionEvaluator#evaluate(javacalculus.struct.CalcFunction)
	 */

	public CalcObject evaluate(CalcFunction input) {
		return null;
	}
	
	public CalcObject getValue() {
		return constant;
	}

}
