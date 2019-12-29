/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcNumber;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

/**
 *  * Trignometric Sine function evaluator. Supports fast evaluation of special values (various multiples of PI and PI fractions).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcASINH extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if (input instanceof CalcVector) {
			throw new CalcWrongParametersException("ASINH is not defined for vectors.");
		}
		if (CALC.useDegrees && input.isNumber()) {
			return evaluateDegree((CalcNumber)input);
		}
		return null;
	}
	
	private CalcObject evaluateDegree(CalcNumber input) {
		return CALC.MULTIPLY.createFunction(new CalcDouble(asinh(input.doubleValue())), new CalcDouble(180/Math.PI));
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(asinh(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.ASINH.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(asinh(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.ASINH.createFunction(input);
	}
	
	protected static double asinh(double x) {
		return Math.log(x + Math.sqrt(x*x + 1.0));
	}

}
