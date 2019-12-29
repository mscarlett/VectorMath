/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;

/**
 * Natural Logarithm evaluator. Supports stable treatment of border and nondomain input.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcLN extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		CalcDouble E = null;
		try {
			E = (CalcDouble)CALC.E.evaluate();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		if (input.equals(E)) {
			return CALC.ONE;
		}
		if (input.equals(CALC.ONE)) {
			return CALC.ZERO;
		}
		if (input.equals(CALC.ZERO)) {
			return CALC.NEG_INFINITY;
		}
		return null;
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.log(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		//TODO decide whether ln(x/y) should evaluate to ln(x)-ln(y)
		return CALC.LN.createFunction(input);
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		//TODO make this more flexible?
		return CALC.LN.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.log(input.bigIntegerValue().intValue()));
	}

	
	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.LN.createFunction(input);
	}

}
