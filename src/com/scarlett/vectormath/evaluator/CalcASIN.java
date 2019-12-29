package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcNumber;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;

public class CalcASIN extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if (input.isNumber()) {
			if (CALC.useDegrees) {
				return evaluateDegree((CalcNumber)input);
			}
			if (input.equals(CALC.ZERO)) { // ASIN(0) = 0
				return CALC.ZERO;
			}
			CalcDouble PI = new CalcDouble(Math.PI);
			if(input.equals(CALC.ONE)) { // ASIN(1) = PI/2
				return CALC.MULTIPLY.createFunction(PI, CALC.D_HALF);
			}
			if(input.equals(CALC.NEG_ONE)) { // ASIN(-1) = -PI/2	
				return CALC.MULTIPLY.createFunction(PI, CALC.NEG_HALF);
			}
		}
		return null;
	}
	
	private CalcObject evaluateDegree(CalcNumber input) {
		return CALC.MULTIPLY.createFunction(new CalcDouble(Math.asin(input.doubleValue())), new CalcDouble(180/Math.PI));
	}

	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.asin(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.ASIN.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.asin(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.ASIN.createFunction(input);
	}

}
