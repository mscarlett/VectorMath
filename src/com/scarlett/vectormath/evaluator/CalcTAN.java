/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.Calc1ParamFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcArithmeticException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcNumber;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;

/**
 * Trigonometric Tangent function evaluator. Supports fast evaluation of special values (various multiples of PI and PI fractions).
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcTAN extends Calc1ParamFunctionEvaluator {
	
	@Override
	protected CalcObject evaluateObject(CalcObject input) {
		if (CALC.useDegrees && input.isNumber()) {
			return evaluateDegree((CalcNumber)input);
		}
		CalcDouble PI = new CalcDouble(Math.PI);
		if (input.equals(PI)) {	//TAN(PI) = 0
			return CALC.ZERO;
		}
		if (input instanceof CalcDouble) {
			CalcDouble param = (CalcDouble)input;
			param = param.divide(PI); //retrieve coefficient of pi
			if (param.isInteger()) { //TAN(c*PI) = 0
				return CALC.ZERO;
			}
			if (param.isDivisibleBy(CALC.D_QUARTER)) { //TAN(c*PI/4) = 0
				if (param.isDivisibleBy(CALC.D_HALF)) {
					throw new CalcArithmeticException("TAN(k*PI/2) not defined");
				}
				else {
					param = param.divide(CALC.D_QUARTER);
					param = param.add(CALC.D_NEG_ONE);
					if (param.isDivisibleBy(CALC.D_FOUR) || param.equals(CALC.D_ZERO)) { //TAN(k*PI/4)=1 //TAN(5k*PI/4)=1
						return CALC.ONE;
					}
					else {	//TAN(3k*PI/4)= -1, //TAN(7k*PI/4) = -1
						return CALC.NEG_ONE;
					}
				}
			}
		}
		return null;
	}
	
	private CalcObject evaluateDegree(CalcNumber input) {
		return new CalcDouble(Math.tan(input.doubleValue()/180*Math.PI));
	}
	
	@Override
	protected CalcObject evaluateDouble(CalcDouble input) {
		return new CalcDouble(Math.tan(input.doubleValue()));
	}

	@Override
	protected CalcObject evaluateFraction(CalcFraction input) {
		return null;
	}

	@Override
	protected CalcObject evaluateFunction(CalcFunction input) {
		return CALC.TAN.createFunction(input);
	}

	@Override
	protected CalcObject evaluateInteger(CalcInteger input) {
		return new CalcDouble(Math.tan(input.bigIntegerValue().intValue()));
	}

	@Override
	protected CalcObject evaluateSymbol(CalcSymbol input) {
		//cannot evaluate symbols, so just return the original function
		return CALC.TAN.createFunction(input);
	}

}
