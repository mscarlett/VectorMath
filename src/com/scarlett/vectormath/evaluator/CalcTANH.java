/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcArithmeticException;
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
 * Trigonometric Tangent function evaluator. Supports fast evaluation of special values (various multiples of PI and PI fractions).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcTANH extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if (input instanceof CalcVector) {
			throw new CalcWrongParametersException("TANH is not defined for vectors.");
		}
		if (CALC.useDegrees && input.isNumber()) {
			return evaluateDegree((CalcNumber)input);
		}
		return null;
	}
	
	private CalcObject evaluateDegree(CalcNumber input) {
		return new CalcDouble(Math.tanh(input.doubleValue()/180*Math.PI));
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.tanh(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.TANH.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.tanh(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.TANH.createFunction(input);
	}

}
