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

public class CalcATAN extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if (input.equals(CALC.ZERO)) { // ATAN(0) = 0
			return CALC.ZERO;
		}
		if (CALC.useDegrees && input.isNumber()) {
			return evaluateDegree((CalcNumber)input);
		}
		return null;
	}
	
	private CalcObject evaluateDegree(CalcNumber input) {
		return CALC.MULTIPLY.createFunction(new CalcDouble(Math.atan(input.doubleValue())), new CalcDouble(180/Math.PI));
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.atan(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.ATAN.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.atan(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.ATAN.createFunction(input);
	}

}
