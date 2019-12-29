package com.scarlett.vectormath.core;

import java.math.MathContext;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.scarlett.vectormath.evaluator.CalcABS;
import com.scarlett.vectormath.evaluator.CalcACOS;
import com.scarlett.vectormath.evaluator.CalcACOSH;
import com.scarlett.vectormath.evaluator.CalcADD;
import com.scarlett.vectormath.evaluator.CalcANGLE;
import com.scarlett.vectormath.evaluator.CalcASIN;
import com.scarlett.vectormath.evaluator.CalcASINH;
import com.scarlett.vectormath.evaluator.CalcATAN;
import com.scarlett.vectormath.evaluator.CalcATANH;
import com.scarlett.vectormath.evaluator.CalcBASIS;
import com.scarlett.vectormath.evaluator.CalcCOS;
import com.scarlett.vectormath.evaluator.CalcCOSH;
import com.scarlett.vectormath.evaluator.CalcCROSS;
import com.scarlett.vectormath.evaluator.CalcCURL;
import com.scarlett.vectormath.evaluator.CalcDEFINE;
import com.scarlett.vectormath.evaluator.CalcDET;
import com.scarlett.vectormath.evaluator.CalcDIFF;
import com.scarlett.vectormath.evaluator.CalcDISTANCE;
import com.scarlett.vectormath.evaluator.CalcDIV;
import com.scarlett.vectormath.evaluator.CalcFACTORIAL;
import com.scarlett.vectormath.evaluator.CalcGAMMA;
import com.scarlett.vectormath.evaluator.CalcGRAD;
import com.scarlett.vectormath.evaluator.CalcINT;
import com.scarlett.vectormath.evaluator.CalcLN;
import com.scarlett.vectormath.evaluator.CalcLOG;
import com.scarlett.vectormath.evaluator.CalcMULTIPLY;
import com.scarlett.vectormath.evaluator.CalcPOWER;
import com.scarlett.vectormath.evaluator.CalcPROJECTION;
import com.scarlett.vectormath.evaluator.CalcSIN;
import com.scarlett.vectormath.evaluator.CalcSINH;
import com.scarlett.vectormath.evaluator.CalcSLOPE;
import com.scarlett.vectormath.evaluator.CalcSUB;
import com.scarlett.vectormath.evaluator.CalcTAN;
import com.scarlett.vectormath.evaluator.CalcTANH;
import com.scarlett.vectormath.evaluator.CalcTAYLOR;
import com.scarlett.vectormath.evaluator.CalcUNIT;
import com.scarlett.vectormath.evaluator.extend.CalcConstantEvaluator;
import com.scarlett.vectormath.evaluator.extend.CalcFunctionEvaluator;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;

/**
 * This class contains a lot of global static 
 * constant or mutable values, functions, and instances.
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public final class CALC {
	
	/**
	 * The math context for the engine. Controls rounding and truncations
	 * to 32 bit to IEEE 754R Decimal32 standards (7 digits). The precision
	 * can be changed by calling <b>CALC.setMathContext(int precision)</b>
	 */
	public static MathContext mathcontext = MathContext.DECIMAL32;
	
	public static boolean operator_notation = false;
	
	/**
	 * Standard unaryChar operator symbols
	 */
	public static final CalcSymbol ADD = new CalcSymbol("ADD", new CalcADD(),
			CalcSymbol.OPERATOR | CalcSymbol.COMMUTATIVE | CalcSymbol.ASSOCIATIVE | CalcSymbol.UNIPARAM_IDENTITY);
	public static final CalcSymbol SUBTRACT = new CalcSymbol("SUBTRACT",
			CalcSymbol.OPERATOR | CalcSymbol.UNIPARAM_IDENTITY);
	public static final CalcSymbol MULTIPLY = new CalcSymbol("MULTIPLY", new CalcMULTIPLY(), 
			CalcSymbol.OPERATOR | CalcSymbol.COMMUTATIVE | CalcSymbol.ASSOCIATIVE | CalcSymbol.UNIPARAM_IDENTITY);
	public static final CalcSymbol DIVIDE = new CalcSymbol("DIVIDE", 
			CalcSymbol.OPERATOR | CalcSymbol.UNIPARAM_IDENTITY);
	public static final CalcSymbol POWER = new CalcSymbol("POWER", new CalcPOWER(),
			CalcSymbol.OPERATOR | CalcSymbol.UNIPARAM_IDENTITY);
	public static final CalcSymbol FACTORIAL = new CalcSymbol("FACTORIAL", new CalcFACTORIAL(),
			CalcSymbol.OPERATOR);
	public static final CalcSymbol ABS = new CalcSymbol("ABS", new CalcABS(), 
			CalcSymbol.OPERATOR);
	
	/**
	 * Special function symbols
	 */
	public static final CalcSymbol SIN = new CalcSymbol("SIN", new CalcSIN(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol COS = new CalcSymbol("COS", new CalcCOS(),
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol TAN = new CalcSymbol("TAN", new CalcTAN(),
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ASIN = new CalcSymbol("ASIN", new CalcASIN(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ACOS = new CalcSymbol("ACOS", new CalcACOS(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ATAN = new CalcSymbol("ATAN", new CalcATAN(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol SINH = new CalcSymbol("SINH", new CalcSINH(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol COSH = new CalcSymbol("COSH", new CalcCOSH(),
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol TANH = new CalcSymbol("TANH", new CalcTANH(),
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ASINH = new CalcSymbol("ASINH", new CalcASINH(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ACOSH = new CalcSymbol("ACOSH", new CalcACOSH(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ATANH = new CalcSymbol("ATANH", new CalcATANH(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol LN = new CalcSymbol("LN", new CalcLN(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol LOG = new CalcSymbol("LOG", new CalcLOG(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol DIFF = new CalcSymbol("DIFF", new CalcDIFF(),
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol INT = new CalcSymbol("INT", new CalcINT(),
			CalcSymbol.NO_PROPERTY);
	//TODO implement INT (integration). This is gonna a hell of a lot harder.
	public static final CalcSymbol DEFINE = new CalcSymbol("DEFINE", new CalcDEFINE(),
			CalcSymbol.OPERATOR | CalcSymbol.FAST_EVAL);
	public static final CalcSymbol SUB = new CalcSymbol("SUB", new CalcSUB(),
			CalcSymbol.FAST_EVAL);
	public static final CalcSymbol TAYLOR = new CalcSymbol("TAYLOR", new CalcTAYLOR(),
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol GAMMA = new CalcSymbol("GAMMA", new CalcGAMMA(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol DET = new CalcSymbol("DET", new CalcDET(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol LIMIT = new CalcSymbol("DET", new CalcDET(), 
			CalcSymbol.NO_PROPERTY);
	
	/**
	 * Vector operation symbols
	 */
	public static final CalcSymbol CROSS = new CalcSymbol("CROSS", new CalcCROSS(), 
	        CalcSymbol.OPERATOR | CalcSymbol.UNIPARAM_IDENTITY);
	public static final CalcSymbol DIV = new CalcSymbol("DIV", new CalcDIV(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol CURL = new CalcSymbol("CURL", new CalcCURL(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol GRAD = new CalcSymbol("GRAD", new CalcGRAD(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol PROJ = new CalcSymbol("PROJ", new CalcPROJECTION(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol ANGLE = new CalcSymbol("ANGLE", new CalcANGLE(), 
			CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol UNIT = new CalcSymbol("UNIT", new CalcUNIT(), 
            CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol BASIS = new CalcSymbol("BASIS", new CalcBASIS(), 
            CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol DISTANCE = new CalcSymbol("DISTANCE", new CalcDISTANCE(), 
            CalcSymbol.NO_PROPERTY);
	public static final CalcSymbol SLOPE = new CalcSymbol("SLOPE", new CalcSLOPE(), 
            CalcSymbol.NO_PROPERTY);
	
	/**
	 * Useful numerical constants
	 */
	private static final byte[] IntegerZero = {0}; 
	public static final CalcInteger ZERO = new CalcInteger(IntegerZero);
	public static final CalcDouble D_ZERO = new CalcDouble(ZERO);
	private static final byte[] IntegerOne = {1}; 
	public static final CalcInteger ONE = new CalcInteger(IntegerOne);
	public static final CalcDouble D_ONE = new CalcDouble(ONE);
	private static final byte[] IntegerNegOne = {-1};
	public static final CalcInteger NEG_ONE = new CalcInteger(IntegerNegOne);
	public static final CalcDouble D_NEG_ONE = new CalcDouble(NEG_ONE); 
	private static final byte[] IntegerTwo = {2}; 
	public static final CalcInteger TWO = new CalcInteger(IntegerTwo);
	private static final byte[] IntegerNegTwo = {-2}; 
	public static final CalcInteger NEG_TWO = new CalcInteger(IntegerNegTwo);
	public static final CalcDouble D_TWO = new CalcDouble(TWO);
	private static final byte[] IntegerFour = {4}; 
	public static final CalcInteger FOUR = new CalcInteger(IntegerFour);
	public static final CalcDouble D_FOUR = new CalcDouble(FOUR);
	public static final CalcFraction HALF = new CalcFraction(ONE, TWO);
	public static final CalcFraction NEG_HALF = new CalcFraction(NEG_ONE, TWO);
	public static final CalcDouble D_NEG_QUARTER = new CalcDouble("-0.25");
	public static final CalcDouble D_QUARTER = new CalcDouble("0.25");
	public static final CalcDouble D_HALF = new CalcDouble("0.5");
	public static final CalcDouble D_THREE_HALF = new CalcDouble("1.5");
	public static final CalcDouble INFINITY = new CalcDouble(Double.POSITIVE_INFINITY);
	public static final CalcDouble NEG_INFINITY = new CalcDouble(Double.NEGATIVE_INFINITY);
	
	/**
	 * Header definitions for certain structs
	 */
	public static final CalcSymbol INTEGER = new CalcSymbol("Integer");
	public static final CalcSymbol DOUBLE = new CalcSymbol("Double");
	public static final CalcSymbol FRACTION = new CalcSymbol("Fraction");
	public static final CalcSymbol SYMBOL = new CalcSymbol("Symbol");
	public static final CalcSymbol MATRIX = new CalcSymbol("Matrix");
	public static final CalcSymbol VECTOR = new CalcSymbol("Vector");
	public static final CalcSymbol SET = new CalcSymbol("");
	
	/**
	 * Symbols for built-in constants
	 */
	public static final CalcSymbol PI = new CalcSymbol("PI",
			new CalcConstantEvaluator(new CalcDouble(Math.PI)), CalcSymbol.CONSTANT);
	public static final CalcSymbol E = new CalcSymbol("E", 
			new CalcConstantEvaluator(new CalcDouble(Math.E)), CalcSymbol.CONSTANT);

	/**
	 * HashMaps that store user defined local variables, vectors, and functions using the header symbol as key
	 */
	public static HashMap<CalcSymbol, CalcObject> defined = new HashMap<CalcSymbol, CalcObject>();
	public static HashMap<CalcSymbol, CalcObject> operators = new HashMap<CalcSymbol, CalcObject>();
	/**
	 * 
	 * @param variable
	 * @return true if the HashMap <b>defined</b> contains key <b>variable</b>. False otherwise.
	 */
	public static boolean hasDefinedVariable(final CalcSymbol variable) {
		if (defined.isEmpty()) {
		    return false;
		}
		Set<CalcSymbol> keySet = defined.keySet();
		Iterator<CalcSymbol> iter = keySet.iterator();
		while (iter.hasNext()) {
		    if (variable.equals(iter.next())) {
		       return true;
		    }
		}
		return false;
	}
	
	/**
	 * 
	 * @param variable
	 * @return The value in HashMap <b>defined</b> corresponding to the key <b>variable</b>
	 */
	public static CalcObject getDefinedVariable(final CalcSymbol variable) {
		if (!hasDefinedVariable(variable)) {
		    return null;
		}
		Set<CalcSymbol> keySet = defined.keySet();
		Iterator<CalcSymbol> iter = keySet.iterator();
		while (iter.hasNext()) {
			CalcSymbol next = iter.next();
			if (variable.equals(next)) {
			    return defined.get(next);
			}
		}
		return null;
	}
	
	public static CalcSymbol getDefinedVariableKey(final CalcObject value) {
		if (!defined.containsValue(value)) {
		    return null;
		}
		Set<CalcSymbol> keySet = defined.keySet();
		Iterator<CalcSymbol> iter = keySet.iterator();
		while (iter.hasNext()) {
			CalcSymbol next = iter.next();
			if (value.equals(defined.get(next))) {
			    return next;
			}
		}
		
		return null;
	}
	
	/**
	 * Puts entry (<b>key</b>, <b>value</b>) in HashMap <b>defined</b>
	 * @param key
	 * @param value
	 */
	public static void setDefinedVariable(final CalcSymbol key, final CalcObject value) {
		if (hasDefinedVariable(key)) {
			if (!getDefinedVariable(key).equals(value)) {
			    removeDefinedVariable(key);
			}
		}
		defined.put(key, value);
	}
	
	public static void removeDefinedVariable(final CalcSymbol variable) {
	    Set<CalcSymbol> keySet = defined.keySet();
        Iterator<CalcSymbol> iter = keySet.iterator();
        while (iter.hasNext()) {
            CalcSymbol next = iter.next();
            if (variable.equals(next)) {
                iter.remove();
                return;
            }
        }
	}
	
	/**
	 * 
	 * @param input
	 * @return true if every char in input is upper case. False otherwise.
	 */
	public static final boolean isUpperCase(final String input) {
		for (int ii = 0; ii < input.length(); ii++) {
			if (Character.isLowerCase(input.charAt(ii))) return false;
		}
		return true;
	}
	
	/**
	 * Symbolically and recursively evaluate a CalcObject using its own evaluate method
	 */
	public static CalcObject SYM_EVAL(final CalcObject input) { //TODO definition of x,y,z
		CalcObject returnVal = null;
		
		try {
			returnVal = input.evaluate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		if (returnVal == null) return null;
		
		CalcObject temp = null;
		
		try {
			temp = returnVal.evaluate();
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		
		//if 2nd evaluation still produced different result, do it again
		//until the result cannot be further evaluated into a different form
		while (temp != null && !returnVal.equals(temp)) {
			returnVal = temp;
			try {
				temp = returnVal.evaluate();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return returnVal == null ? input : returnVal;
	}
	
	/**
	 * 
	 * @param identifier
	 * @return a function evaluator capable of evaluating the properties of symbol
	 */
    public static CalcSymbol getSymbol(final String identifier) { //why aren't these in a hash table?
		if (identifier.equals("SIN")) return (CalcSymbol)SIN.clone();
		else if (identifier.equals("COS")) return (CalcSymbol)COS.clone();
		else if (identifier.equals("TAN")) return (CalcSymbol)TAN.clone();
		else if (identifier.equals("ASIN")) return (CalcSymbol)ASIN.clone();
		else if (identifier.equals("ACOS")) return (CalcSymbol)ACOS.clone();
		else if (identifier.equals("ATAN")) return (CalcSymbol)ATAN.clone();
		else if (identifier.equals("LN")) return (CalcSymbol)LN.clone();	
		else if (identifier.equals("ABS")) return (CalcSymbol)ABS.clone();
		else if (identifier.equals("DIFF")) return (CalcSymbol)DIFF.clone();
		else if (identifier.equals("INT")) return (CalcSymbol)INT.clone();
		else if (identifier.equals("DEFINE")) return (CalcSymbol)DEFINE.clone();
		else if (identifier.equals("TAYLOR")) return (CalcSymbol)TAYLOR.clone();
		else if (identifier.equals("GAMMA")) return (CalcSymbol)GAMMA.clone();
		else if (identifier.equals("DET")) return (CalcSymbol)DET.clone();
		else if (identifier.equals("CROSS")) return (CalcSymbol)CROSS.clone();
		else if (identifier.equals("DIV")) return (CalcSymbol)DIV.clone();
		else if (identifier.equals("CURL")) return (CalcSymbol)CURL.clone();
		else if (identifier.equals("GRAD")) return (CalcSymbol)GRAD.clone();
		else if (identifier.equals("PROJ")) return (CalcSymbol)PROJ.clone();
		else if (identifier.equals("ANGLE")) return (CalcSymbol)ANGLE.clone();
        else if (identifier.equals("UNIT")) return (CalcSymbol)UNIT.clone();
        else if (identifier.equals("BASIS")) return (CalcSymbol)BASIS.clone();
        else if (identifier.equals("DISTANCE")) return (CalcSymbol)DISTANCE.clone();
        else if (identifier.equals("LIMIT")) return (CalcSymbol)LIMIT.clone();
        else if (identifier.equals("LOG")) return (CalcSymbol)LOG.clone();
        else if (identifier.equals("SINH")) return (CalcSymbol)SINH.clone();
		else if (identifier.equals("COSH")) return (CalcSymbol)COSH.clone();
		else if (identifier.equals("TANH")) return (CalcSymbol)TANH.clone();
		else if (identifier.equals("ASINH")) return (CalcSymbol)ASINH.clone();
		else if (identifier.equals("ACOSH")) return (CalcSymbol)ACOSH.clone();
		else if (identifier.equals("ATANH")) return (CalcSymbol)ATANH.clone();
		else if (identifier.equals("SLOPE")) return (CalcSymbol)SLOPE.clone();
		else if (identifier.equals("PI") || identifier.equals("Π")) return PI; //HACK little pi becomes big pi
		else if (identifier.equals("E")) return E;
		else {
			String name = "com.scarlett.vectormath.evaluator.Calc" + identifier;
			Class<?> cls = null;
			CalcFunctionEvaluator evaluator;
			CalcSymbol symbol = new CalcSymbol(identifier);
			try {
				cls = Class.forName(name);
			}
			catch (ClassNotFoundException e) {}
			try {
				evaluator = (CalcFunctionEvaluator) cls.newInstance();
				symbol.setEvaluator(evaluator);
				return symbol;
			}
			catch (Exception e) {}
		}
		return null;
	}
    
	public static void setMathContext(final int precision) {
		mathcontext = new MathContext(precision);
	}
	
	public static void toggleOperatorNotation() {
		operator_notation = !operator_notation;
	}
	
	public static boolean defineMode = false;
	public static boolean useDegrees = false;

    public static CalcObject EVALUATE(final CalcObject input) { //TODO errors should not be catched during evaluation
        CalcObject returnVal = SYM_EVAL(input);
        if (defineMode) {
			CalcSymbol.setDefinedVariableMode(true);
			try {
				returnVal = SYM_EVAL(returnVal);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				CalcSymbol.setDefinedVariableMode(false);
			}
        }
        return returnVal == null ? input: returnVal;
    }

	public static void clearDefinitions() {
		defined.clear();
	}
}
