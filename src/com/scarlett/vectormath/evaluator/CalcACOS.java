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

public class CalcACOS extends Calc1ParamFunctionEvaluator {

	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if(input.isNumber()) {
			if (CALC.useDegrees) {
				return evaluateDegree((CalcNumber)input);
			}
			if(input.equals(CALC.ONE)) { // ACOS(1) = 0
				return CALC.ZERO;
			}
			CalcDouble PI = new CalcDouble(Math.PI);
			if(input.equals(CALC.ZERO)) { // ACOS(0) = PI/2
				return CALC.MULTIPLY.createFunction(PI, CALC.D_HALF);
			}
			if(input.equals(CALC.NEG_ONE)) { // ACOS(-1) = PI
				return PI;
			}
		}
		return null;
	}
	
	private CalcObject evaluateDegree(CalcNumber input) {
		return CALC.MULTIPLY.createFunction(new CalcDouble(Math.acos(input.doubleValue())), new CalcDouble(180/Math.PI));
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.acos(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.ACOS.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.acos(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.ACOS.createFunction(input);
	}
}
