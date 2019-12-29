/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcUnsupportedException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;

/**
 * Logarithm base 10 evaluator. Supports stable treatment of border and nondomain input.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcLOG extends Calc1ParamFunctionEvaluator {
	
	protected static CalcDouble LN10INVERSE = new CalcDouble(0.434294481903251827651128918917);
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		return CALC.MULTIPLY.createFunction(CALC.LN.createFunction(input), LN10INVERSE);
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		throw new CalcUnsupportedException("Not implemented.");
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		throw new CalcUnsupportedException("Not implemented.");
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		throw new CalcUnsupportedException("Not implemented.");
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		throw new CalcUnsupportedException("Not implemented.");
	}

	
	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		throw new CalcUnsupportedException("Not implemented.");
	}

}
