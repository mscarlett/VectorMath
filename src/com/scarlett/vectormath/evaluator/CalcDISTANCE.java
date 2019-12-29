package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcVector;

public class CalcDISTANCE implements CalcFunctionEvaluator {

	@Override
	public CalcObject evaluate(CalcFunction input) {
		if (input.size() == 1) {
			return CALC.ABS.createFunction(input.get(0));
		}
		if (input.get(0).isNumber()) {
			CalcVector v = new CalcVector(input.size());
			for (int i = 0; i < input.size(); i++) {
				v.set(i,(input.get(i)));
			}
			return CALC.ABS.createFunction(v);
		}
		if (input.get(0) instanceof CalcVector) {
			CalcFunction returnVal = new CalcFunction(CALC.ADD);
			for (int i = 0; i < input.size() - 1; i++) {
				returnVal.add(distance(input.get(i), input.get(i + 1)));
			}
			return returnVal;
		}
		return null;
	}

	private CalcObject distance(CalcObject obj1, CalcObject obj2) {
		return CALC.ABS.createFunction(CALC.ADD.createFunction(obj2, CALC.MULTIPLY.createFunction(obj1, CALC.NEG_ONE)));
	}

}
