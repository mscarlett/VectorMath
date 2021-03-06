/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import java.util.ArrayList;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcUnsupportedException;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcNumber;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

/**
 * This function evaluator applies the integral operator to a function with respect to
 * a given variable. An optional "order" parameter is built in to take multiple integrals
 * with respect to the same variable.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcINT implements CalcFunctionEvaluator {

	/**
	 * 
	 */
	public CalcINT() {}
	

	public CalcObject evaluate(CalcFunction function) {		
		if (function.size() == 2) {	//case INT(function, variable)
			if (function.get(1) instanceof CalcSymbol) {	//evaluate, adding an arbitrary constant for good practice
				if (function.get(0) instanceof CalcVector) {
					CalcVector vector = (CalcVector)function.get(0);
					CalcVector returnVal = new CalcVector(vector.size());
					for (int i = 0; i < vector.size(); i++) {
						returnVal.set(i, CALC.INT.createFunction(vector.get(i), function.get(1)));
					}
					return returnVal;
				}
				return CALC.ADD.createFunction(integrate(function.get(0), (CalcSymbol)function.get(1)), new CalcSymbol("C"));
			}
			else throw new CalcWrongParametersException("INT -> 2nd parameter syntax");
		}
		if (function.size() == 4) {	//case INT(function, variable, left, right)
			if (function.get(1) instanceof CalcSymbol) {	//evaluate, adding an arbitrary constant for good practice
				if (function.get(2) instanceof CalcVector) {
					throw new CalcWrongParametersException("INT -> 3rd parameter syntax");
				}
				if (function.get(3) instanceof CalcVector) {
					throw new CalcWrongParametersException("INT -> 4th parameter syntax");
				}
				if (function.get(0) instanceof CalcVector) {
					CalcVector vector = (CalcVector)function.get(0);
					CalcVector returnVal = new CalcVector(vector.size());
					for (int i = 0; i < vector.size(); i++) {
						ArrayList<CalcObject> temp = new ArrayList<CalcObject>(4);
						temp.add(vector.get(i));
						temp.add(function.get(1));
						temp.add(function.get(2));
						temp.add(function.get(3));
						returnVal.set(i, new CalcFunction((CalcSymbol)CALC.INT.clone(), temp));
					}
					return returnVal;
				}
				if (function.get(2).isNumber() && function.get(3).isNumber()) {
					return numericIntegrate(function.get(0), (CalcSymbol)function.get(1), (CalcNumber)function.get(2), (CalcNumber)function.get(3));
				}
				return null;
			}
			else throw new CalcWrongParametersException("INT -> 2nd parameter syntax");
		}
		else throw new CalcWrongParametersException("INT -> wrong number of parameters");
	}
	
	public CalcObject integrate(CalcObject obj, CalcSymbol var) {
		if (obj instanceof CalcFunction) { //input f(x..xn)
			obj = CALC.SYM_EVAL(obj); //evaluate the function before attempting integration
		}
		if (obj.isNumber()) {	//	INT(c,x) = c*x
			return CALC.MULTIPLY.createFunction(obj, var); 
		}
		if ((obj instanceof CalcSymbol && !((CalcSymbol)obj).equals(var))) {
			CalcSymbol symbol = (CalcSymbol)obj;
			CalcObject defined = CALC.getDefinedVariable(symbol);
			if (defined != null) {
				return CALC.INT.createFunction(defined, var);
			}
			return CALC.MULTIPLY.createFunction(symbol, var); 
		}
		if (obj.equals(var)) { //	INT(x, x) = x^2/2
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(var, CALC.TWO), CALC.HALF);
		}
		if (obj.getHeader().equals(CALC.ADD) && ((CalcFunction)obj).size() > 1) { //	INT(y1+y2+...,x) = INT(y1,x) + INT(y2,x) + ...
			CalcFunction function = (CalcFunction)obj;
			CalcFunction functionB = new CalcFunction(CALC.ADD, function, 1, function.size());
			return CALC.ADD.createFunction(
					integrate(function.get(0), var),
					integrate(functionB, var));
		}
		if (obj.getHeader().equals(CALC.MULTIPLY)) {	//INT(c*f(x),x) = c*INT(f(x),x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.isNumber()) {
				return CALC.MULTIPLY.createFunction(function.get(0), 
						integrate(new CalcFunction(CALC.MULTIPLY, function, 1, function.size()), var));
			}
			else { //	INT(f(x)*g(x),x) = ?? (u-sub)
				CalcFunction functionB = new CalcFunction(CALC.MULTIPLY, function, 1, function.size());
				CalcObject secondObj = CALC.SYM_EVAL(functionB);
				return obj; //TODO implement reverse chain rule (u-sub)?? Very tricky.
				// create method to determine which part should be u and which part should be dv, then return new integral
			}
		}
		if (obj.getHeader().equals(CALC.POWER)) { //this part is probably trickiest (form f(x)^g(x)). A lot of integrals here does not evaluate into elementary functions
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			CalcObject secondObj = function.get(1);
			if (firstObj instanceof CalcSymbol) {
				if (secondObj.isNumber() || secondObj instanceof CalcSymbol && !(secondObj.equals(var))) { //	INT(x^n,x) = x^(n+1)/(n+1)
					return CALC.MULTIPLY.createFunction(
							CALC.POWER.createFunction(firstObj, CALC.ADD.createFunction(secondObj, CALC.ONE)),
							CALC.POWER.createFunction(CALC.ADD.createFunction(secondObj, CALC.ONE)), CALC.NEG_ONE);
				}
			}
			else if (firstObj.isNumber()) {	// INT(c^x,x) = c^x/ln(c)
				return CALC.MULTIPLY.createFunction(obj, CALC.POWER.createFunction(CALC.LN.createFunction(firstObj), CALC.NEG_ONE));
			}
		}
		if (obj.getHeader().equals(CALC.LN)) {	//	INT(LN(x),x) = x*LN(x) - x
			return CALC.ADD.createFunction(
					CALC.MULTIPLY.createFunction(var, obj), 
					CALC.MULTIPLY.createFunction(var, CALC.NEG_ONE)); 
		}
		if (obj.getHeader().equals(CALC.SIN)) {	//	INT(SIN(x),x) = -COS(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.equals(var)) return CALC.MULTIPLY.createFunction(CALC.NEG_ONE, CALC.COS.createFunction(firstObj)); 
		}
		if (obj.getHeader().equals(CALC.COS)) {	//	INT(COS(x),x) = SIN(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.equals(var)) return CALC.SIN.createFunction(firstObj);
		}
		if (obj.getHeader().equals(CALC.TAN)) {	//	INT(TAN(x),x) = -LN(|COS(x)|)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.equals(var)) return CALC.MULTIPLY.createFunction(CALC.NEG_ONE, 
					CALC.LN.createFunction(CALC.ABS.createFunction(CALC.COS.createFunction(var))));
		}
		if (obj.getHeader().equals(CALC.ABS)) {	//	INT(|x|,x) = x*|x|/2
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.equals(var)) return CALC.MULTIPLY.createFunction(var, CALC.HALF, 
					CALC.ABS.createFunction(var));
		}
		throw new CalcUnsupportedException("Error evaluating indefinite integral. You can also do definite integrals by using INT(function, variable, left, right).");
	}
	
	public CalcObject numericIntegrate(CalcObject obj, CalcSymbol var, CalcNumber left, CalcNumber right) {
		if (obj.isNumber()) {
			return CALC.MULTIPLY.createFunction(obj, CALC.ADD.createFunction(left, CALC.MULTIPLY.createFunction(right, CALC.NEG_ONE)));
		}
		if (obj instanceof CalcSymbol) {
			if (obj.equals(var)) {
				return riemannSum(obj, var, left, right);
			}
			return CALC.MULTIPLY.createFunction(obj, CALC.ADD.createFunction(left, CALC.MULTIPLY.createFunction(right, CALC.NEG_ONE)));
		}
		if (obj instanceof CalcFunction) {
			return riemannSum(obj, var, left, right);
		}
		throw new CalcUnsupportedException("This operation is not supported.");
	}


	private CalcObject riemannSum(CalcObject obj, CalcSymbol var, CalcNumber left, CalcNumber right) {
		double leftBound = left.doubleValue();
		double rightBound = right.doubleValue();
		boolean flipBounds = leftBound > rightBound;
		if (flipBounds) {
			double temp = leftBound;
			leftBound = rightBound;
			rightBound = temp;
		}
		CalcDouble currentValue = new CalcDouble(leftBound);
		CalcDouble delta = new CalcDouble((rightBound-leftBound)/100);
		CalcFunction sum = new CalcFunction(CALC.ADD, new ArrayList<CalcObject>(100));
		while (currentValue.doubleValue() < rightBound) {
			currentValue = currentValue.add(delta);
			sum.add(CALC.SUB.createFunction(obj, var, currentValue));
			if (sum.size() == 100) {
				try {
					sum.evaluate();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		if (flipBounds) {
			return CALC.MULTIPLY.createFunction(sum, delta, CALC.NEG_ONE);
		}
		return CALC.MULTIPLY.createFunction(sum, delta);
	}
}
