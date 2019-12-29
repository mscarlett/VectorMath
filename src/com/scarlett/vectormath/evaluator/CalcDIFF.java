/**
 * 
 */
package com.scarlett.vectormath.evaluator;

import com.scarlett.vectormath.core.CALC;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.exception.CalcWrongParametersException;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

/**
 * This function evaluator applies the derivative operator to a function with respect to
 * a given variable. An optional "order" parameter is built in to take multiple derivatives.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public class CalcDIFF implements CalcFunctionEvaluator { //TODO create method to differentiate vectors //TODO differentiate symbols correctly

	/**
	 * 
	 */
	public CalcDIFF() {
		// TODO Auto-generated constructor stub
	}
	

	public CalcObject evaluate(final CalcFunction function) {
		if (function.size() == 2) {	//case DIFF(function, variable)
			if (function.get(1) instanceof CalcSymbol) {
				if (function.get(0) instanceof CalcVector) {
					CalcVector vector = (CalcVector)function.get(0);
					CalcVector returnVal = new CalcVector(vector.size());
					for (int i = 0; i < vector.size(); i++) {
						returnVal.set(i, differentiate(vector.get(i), (CalcSymbol)function.get(1)));
					}
					return returnVal;
				}
				return differentiate(function.get(0), (CalcSymbol)function.get(1));
			}
			throw new CalcWrongParametersException("DIFF -> 2nd parameter syntax");
		}
		else if (function.size() == 3) { //case DIFF(function, variable, order)
			if (function.get(1) instanceof CalcSymbol) {
				if (function.get(2) instanceof CalcInteger) {
					int order = ((CalcInteger)function.get(2)).intValue();
					if (order == 0) return function.get(0);
					CalcObject returnVal = differentiate(function.get(0), (CalcSymbol)function.get(1));
					order--; //We already took one derivative. Take one away.
					for (int ii = 0; ii < order; ii++) {
						returnVal = differentiate(returnVal, (CalcSymbol)function.get(1));
					}
					return returnVal;
				}
				throw new CalcWrongParametersException("DIFF -> 3rd parameter syntax");
			}
			throw new CalcWrongParametersException("DIFF -> 2nd parameter syntax");
		}
		throw new CalcWrongParametersException("DIFF -> wrong number of parameters");
	}
	
	public static CalcObject differentiate(CalcObject obj, CalcSymbol var) {
		if (obj instanceof CalcFunction) { //input f(x..xn) //TODO do not substitute variable; why does this work differently?
			obj = CALC.SYM_EVAL(obj); //evaluate the function before attempting differentiation
		}
		if (obj.isNumber()) {	//	(d/dx) c = 0
			return CALC.ZERO; 
		}
		if (obj instanceof CalcSymbol) {
			CalcSymbol symbol = (CalcSymbol)obj;
			if (symbol.equals(var)) { //	(d/dx) x = 1
				return CALC.ONE;
			}
			CalcObject defined = CALC.getDefinedVariable(symbol);
			if (defined != null) {
				return CALC.DIFF.createFunction(defined, var);
			}
			return CALC.ZERO;
		}
		if (obj.getHeader().equals(CALC.ADD) && ((CalcFunction)obj).size() > 1) { //	(d/dx)(y1+y2+...) = (d/dx)y1 + (d/dx)y2 + ...
			CalcFunction function = (CalcFunction)obj;
			CalcFunction functionB = new CalcFunction(CALC.ADD, function, 1, function.size());
			return CALC.ADD.createFunction(
					differentiate(function.get(0), var),
					differentiate(functionB, var));
		}
		if (obj.getHeader().equals(CALC.MULTIPLY)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			if (firstObj.isNumber()) { //	(d/dx) c*y = c * (d/dx) y
				return CALC.MULTIPLY.createFunction(function.get(0), 
						differentiate(new CalcFunction(CALC.MULTIPLY, function, 1, function.size()), var));
			}
			//	(d/dx) y1*y2 = y1*(d/dx)y2 + y2*(d/dx)y1
			CalcFunction functionB = new CalcFunction(CALC.MULTIPLY, function, 1, function.size());
			CalcObject secondObj = CALC.SYM_EVAL(functionB);
			return CALC.ADD.createFunction(
					CALC.MULTIPLY.createFunction(firstObj, differentiate(secondObj, var)),
					CALC.MULTIPLY.createFunction(secondObj, differentiate(firstObj, var)));
		}
		if (obj.getHeader().equals(CALC.POWER)) { //this part is probably trickiest (form u^v)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			CalcObject secondObj = function.get(1);
			if (firstObj instanceof CalcFunction || firstObj instanceof CalcSymbol) {
				if (secondObj.isNumber() || secondObj instanceof CalcSymbol && !(secondObj.equals(var))) { //	(d/dx) f^n = n*f^(n-1)*(d/dx)f
					return CALC.MULTIPLY.createFunction(
							secondObj,
							CALC.POWER.createFunction(firstObj, CALC.ADD.createFunction(secondObj, CALC.NEG_ONE)),
							differentiate(firstObj, var));
				}
				if (secondObj instanceof CalcFunction || secondObj instanceof CalcSymbol) {
					return CALC.MULTIPLY.createFunction(
							CALC.POWER.createFunction(firstObj, secondObj),
							CALC.ADD.createFunction(	//(d/dx)(u^v) = (u^v)*(v*u'/u + v'*LN(u)) kind of obscure ....
									CALC.MULTIPLY.createFunction(secondObj, 
											differentiate(firstObj, var),
											CALC.POWER.createFunction(firstObj, CALC.NEG_ONE)),
									CALC.MULTIPLY.createFunction(
											CALC.LN.createFunction(firstObj),
											differentiate(secondObj, var))
									)
							);
				}
			}
			else if (firstObj.isNumber()) {	// (d/dx) c^u = c^u * ln(c) * (d/dx)u
				return CALC.MULTIPLY.createFunction(
						obj, CALC.LN.createFunction(firstObj), differentiate(secondObj, var));
			}
		}
		if (obj.getHeader().equals(CALC.LN)) {	//	(d/dx) LN(f(x)) = 1/f(x) * (d/dx)f(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(firstObj, CALC.NEG_ONE), differentiate(firstObj, var));
		}
		if (obj.getHeader().equals(CALC.SIN)) {	//	(d/dx) SIN(f(x)) = COS(f(x)) * (d/dx)f(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.COS.createFunction(firstObj), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.COS)) {	//	(d/dx) COS(f(x)) = -SIN(f(x)) * (d/dx)f(x)
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.NEG_ONE, CALC.SIN.createFunction(firstObj), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.ABS)) {	//	(d/dx) |u| = u/|u|*u';
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(firstObj, differentiate(firstObj, var), CALC.POWER.createFunction(obj, CALC.NEG_ONE)); 
		}
		if (obj.getHeader().equals(CALC.TAN)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.COS.createFunction(firstObj), CALC.NEG_TWO), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.ASIN)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.ADD.createFunction(CALC.ONE, CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(firstObj, CALC.TWO), CALC.NEG_ONE)), CALC.NEG_HALF), differentiate(firstObj, var));
		}
		if (obj.getHeader().equals(CALC.ACOS)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.ADD.createFunction(CALC.ONE, CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(firstObj, CALC.TWO), CALC.NEG_ONE)), CALC.NEG_HALF), differentiate(firstObj, var), CALC.NEG_ONE);
		}
		if (obj.getHeader().equals(CALC.ATAN)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.ADD.createFunction(CALC.ONE, CALC.POWER.createFunction(firstObj, CALC.TWO)), CALC.NEG_ONE), differentiate(firstObj, var));
		}
		if (obj.getHeader().equals(CALC.SINH)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.COSH.createFunction(firstObj), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.COSH)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.SINH.createFunction(firstObj), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.TANH)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.COSH.createFunction(firstObj), CALC.NEG_TWO), differentiate(firstObj, var)); 
		}
		if (obj.getHeader().equals(CALC.ASINH)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.ADD.createFunction(CALC.ONE, CALC.POWER.createFunction(firstObj, CALC.TWO)), CALC.NEG_HALF), differentiate(firstObj, var));
		}
		if (obj.getHeader().equals(CALC.ACOSH)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.ADD.createFunction(CALC.NEG_ONE, CALC.POWER.createFunction(firstObj, CALC.TWO)), CALC.NEG_HALF), differentiate(firstObj, var));
		}
		if (obj.getHeader().equals(CALC.ATANH)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(CALC.ADD.createFunction(CALC.ONE, CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(firstObj, CALC.TWO), CALC.NEG_ONE)), CALC.NEG_ONE), differentiate(firstObj, var));
		}
		if (obj.getHeader().equals(CALC.LOG)) {
			CalcFunction function = (CalcFunction)obj;
			CalcObject firstObj = function.get(0);
			return CALC.MULTIPLY.createFunction(CALC.POWER.createFunction(firstObj, CALC.NEG_ONE), differentiate(firstObj, var), CalcLOG.LN10INVERSE);
		}
		return CALC.DIFF.createFunction(obj, var); //don't know how to differentiate (yet). Return original expression.
	}
}
