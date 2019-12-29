package com.scarlett.vectormath.core;

import java.util.ArrayList;
import java.util.Locale;

import android.util.Log;

import com.scarlett.vectormath.evaluator.CalcSUB;
import com.scarlett.vectormath.exception.CalcSyntaxException;
import com.scarlett.vectormath.struct.CalcDouble;
import com.scarlett.vectormath.struct.CalcFraction;
import com.scarlett.vectormath.struct.CalcFunction;
import com.scarlett.vectormath.struct.CalcInteger;
import com.scarlett.vectormath.struct.CalcMatrix;
import com.scarlett.vectormath.struct.CalcObject;
import com.scarlett.vectormath.struct.CalcSymbol;
import com.scarlett.vectormath.struct.CalcVector;

/**
 * Parses a mathematical expression string into a CalcObject
 * hiearchical data structure which can either be transformed back
 * into a string, evaluated, or passed as an argument to another hierarchy. 
 * @author Duyun Chen <A HREF="mailto:duchen@seas.upenn.edu">[duchen@seas.upenn.edu]</A>,
 * Seth Shannin <A HREF="mailto:sshannin@seas.upenn.edu">[sshannin@seas.upenn.edu]</A>
 *  
 *
 */
public final class CalcParser { //TODO add support for two-param vector parsing //TODO throw exception for invalid || placement
	/**
	 * These static constants define what values the token can take
	 * based on what currentChar is. This keeps track of the TYPE of
	 * currentChar.
	 */
	private final static int
	CALC_NULL = 0,				//end of line/file or unsupported token
	CALC_POWER = 1,				//exponentiation
	CALC_MULTIPLY = 2,			//multiply
	CALC_DIVIDE = 3,			//divide
	CALC_SUBTRACT = 4,			//subtract
	CALC_ADD = 5,				//add
	CALC_PARENTHESISOPEN = 6,	//open parenthesis
	CALC_PARENTHESISCLOSE = 7,	//close parenthesis
	CALC_MATRIXOPEN = 8,		//open matrix declaration
	CALC_MATRIXCLOSE = 9,		//close matrix declaration
	CALC_IDENTIFIER = 10,		//variable names. function names, symbolic anything
	CALC_DIGIT = 11,			//numbers
	CALC_COMMA = 12,			//commas (mostly used in function argument list)
	CALC_DEFINE = 13,			//variable assignment (i.e. x=10, f(x)=x+4, etc)
	CALC_FACTORIAL = 14,		//factorial (x!)
	CALC_ABS = 15,				//absolute value (|x|)
	CALC_CROSS = 16,			//cross product (×)
	CALC_SQRT = 17,             //square root (√)
	CALC_DECIMAL = 18;          //decimal point
	
	private String inputString;
	private char currentChar;
	private int currentCharIndex;
	private int token;
	//TODO why can't the calculator evaluate .1?
	/**
	 * Constructor
	 * @param StringIn The string that needs to be converted into a CalcObject hierarchy
	 */
	public CalcParser(final String StringIn) {
		this.inputString = StringIn;
	}
	/**
	 * Empty constructor
	 */
	public CalcParser() {
		this(null);
	}
	
	public CalcObject parse(final String input) throws CalcSyntaxException { //called by CalculusEngine class initially
		this.inputString = input;
		this.currentChar = ' ';
		this.currentCharIndex = 0;
		this.token = CALC_NULL;
		return parse();
	}
	
	public CalcObject parse() throws CalcSyntaxException { //called by parse(String input) initially
		parseNextToken(); //initialize the token for the parser
		CalcObject returnVal = parseDefine(); //step through the precedence levels, starting with highest -> define
		return returnVal;
	}
	/**
	 * Identifies the next sequence of characters by a unique 
	 * int stored in global variable <b>token</b>
	 * @throws CalcSyntaxException
	 */
	private void parseNextToken() throws CalcSyntaxException { //called by parse() initially
		while (this.inputString.length() > this.currentCharIndex) {
			this.currentChar = this.inputString.charAt(this.currentCharIndex++);
			this.token = CALC_NULL;	
			if (this.currentChar != '\n' && this.currentChar != '\t' 
				&& this.currentChar != '\r' && this.currentChar != ' ') { //make sure the char is not terminating or whitespace
				if ((this.currentChar >= 'a' && this.currentChar <= 'z') //is the char a letter (identifier)?
						|| (this.currentChar >= 'A' && this.currentChar <= 'Z')) {
					this.token = CALC_IDENTIFIER;
					return;
				}
				if (this.currentChar >= '0' && this.currentChar <= '9') { //is the char a number?
					this.token = CALC_DIGIT;
					return;
				} else if (this.currentChar == '.') {
				    this.token = CALC_DECIMAL;
                    return;
				}
				switch (this.currentChar) { //brute force identify the char and store the identification in token
					case '(':
						this.token = CALC_PARENTHESISOPEN;
						break;
					case ')':
						this.token = CALC_PARENTHESISCLOSE;
						break;
					case '{':
						this.token = CALC_MATRIXOPEN;
						break;
					case '}':
						this.token = CALC_MATRIXCLOSE;
						break;
					case ',':
						this.token = CALC_COMMA;
						break;
					case '^':
						this.token = CALC_POWER;
						break;
					case '+':
						this.token = CALC_ADD;
						break;
					case '-':
						this.token = CALC_SUBTRACT;
						break;
					case '*':
					case '•':
						this.token = CALC_MULTIPLY;
						break;
					case '/':
						this.token = CALC_DIVIDE;
						break;
					case '=':
						this.token = CALC_DEFINE;
						break;
					case '!':
						this.token = CALC_FACTORIAL;
						break;
					case '|':
						this.token = CALC_ABS;
						break;
					case '×':
						this.token = CALC_CROSS;
						break;
					case '√':
					    this.token = CALC_SQRT;
					    break;
					case 'π':
						this.token = CALC_IDENTIFIER;
						break;
					default:
						throw new CalcSyntaxException("Unidentified character: " + this.currentChar);
				}
				if (this.token == CALC_NULL) {
					throw new CalcSyntaxException("No token identified");
				}
				return;
			}
		}
		//end of loop variable reset
		this.currentCharIndex = this.inputString.length() + 1;
		this.currentChar = ' ';
		this.token = CALC_NULL;
	}
	
	private CalcObject parseDefine() throws CalcSyntaxException {
		CalcObject returnVal = parseExpression();
		int tempToken;	
		while (this.token == CALC_DEFINE) {
		    Log.i("TAG", "Current returnVal is: " + returnVal);
			tempToken = this.token;
			parseNextToken();
			if (tempToken == CALC_DEFINE) {
				returnVal = CALC.DEFINE.createFunction(returnVal, parseExpression());
				Log.i("TAG", "New returnVal is: " + returnVal);
			}
		}
		Log.i("TAG", "ReturnVal to be returned: " + returnVal);
		return returnVal;
	}
	
	private CalcObject parseExpression() throws CalcSyntaxException { //called by parseDefine() initially
		int tempToken;
		CalcObject returnVal;
		if (this.token == CALC_ADD || this.token == CALC_SUBTRACT) {
			tempToken = this.token;
			parseNextToken();
			if (tempToken == CALC_SUBTRACT) {
				returnVal = parseMultiplication(true);
			}
			else {
				returnVal = parseMultiplication(false);
			}
		}
		else {
			returnVal = parseMultiplication(false);
		}
		
		if (this.token == CALC_ADD || this.token == CALC_SUBTRACT) {
			CalcFunction returnFunction = CALC.ADD.createFunction(returnVal);
			while (this.token == CALC_ADD || this.token == CALC_SUBTRACT) {
				if (this.token == CALC_ADD) {
					parseNextToken();
					returnFunction.add(parseMultiplication(false));
				}
				else {
					parseNextToken();
					returnFunction.add(parseMultiplication(true));
				}
			}
			return returnFunction;
		}
        return returnVal;
	}
	
	private CalcObject parseMultiplication(final boolean isNegative) throws CalcSyntaxException {
		CalcObject returnVal = parseDivision();
		if (isNegative) { //handle negated case
			if (returnVal instanceof CalcInteger) {
				returnVal = ((CalcInteger)returnVal).multiply(CALC.NEG_ONE);
			}
			else if (returnVal instanceof CalcFraction) {
				returnVal = ((CalcFraction)returnVal).multiply(CALC.NEG_ONE);
			}
			else returnVal = CALC.MULTIPLY.createFunction(CALC.NEG_ONE, returnVal);
		}
		
		if (this.token != CALC_MULTIPLY) {
			return returnVal;
		}
		CalcFunction returnFunction = new CalcFunction(CALC.MULTIPLY, returnVal);
		while (this.token == CALC_MULTIPLY) {
			parseNextToken();
			returnFunction.add(parseDivision());
		}
		return returnFunction;
	}
	
	/**
	 * Parses division precedence expression. The reason why I did
	 * not create or use a separate "Divide" evaluation class, instead using 
	 * multiply to negative one power, is because of the properties
	 * of the multiply function over the divide function (same with
	 * add over subtract) -> commutativity and associativity. Makes life
	 * a lot easier in evaluation but will probably come back to haunt me.
	 * Delete this comment when this issue is resolved.
	 * @return the parsed hierarchy tree at division level
	 * @throws CalcSyntaxException
	 */
	private CalcObject parseDivision() throws CalcSyntaxException {
		CalcObject numerator = parseCross();
		CalcObject denominator;
		if (this.token != CALC_DIVIDE) {
			return numerator;
		}
		parseNextToken();
		denominator = parseCross();
		if (this.token != CALC_DIVIDE) {
			if (numerator instanceof CalcInteger && denominator instanceof CalcInteger) {
				if (denominator == CALC.ZERO) {
					throw new CalcSyntaxException("Division by zero.");
				}
				return new CalcFraction((CalcInteger)numerator, (CalcInteger)denominator);
			}
            CalcFunction reciprocal = CALC.POWER.createFunction(denominator,CALC.NEG_ONE);
            return new CalcFunction(CALC.MULTIPLY, numerator, reciprocal);
		}
		CalcFunction reciprocal = CALC.POWER.createFunction(denominator,CALC.NEG_ONE);
		CalcFunction function = new CalcFunction(CALC.MULTIPLY, numerator, reciprocal);
		while (this.token == CALC_DIVIDE) { //handle continued fraction expressions
			parseNextToken();
			function.add(CALC.POWER.createFunction(parsePower(),CALC.NEG_ONE));
		}
		return function;
	}
	
	private CalcObject parseCross() throws CalcSyntaxException {
	    CalcObject returnVal = parsePower();
	    if (this.token != CALC_CROSS) {
	        return returnVal;
	    }
	    CalcFunction returnFunction = new CalcFunction(CALC.CROSS, returnVal);
	    while(this.token == CALC_CROSS) {
	        parseNextToken();
	        returnFunction.add(parsePower());
	    }
        return returnFunction;
	}
	
	private CalcObject parsePower() throws CalcSyntaxException {
		CalcObject returnVal = parseSquareRoot();
		if (this.token != CALC_POWER) {
			return returnVal;
		}
		while (this.token == CALC_POWER) {
			parseNextToken();
			CalcFunction function = new CalcFunction(CALC.POWER, returnVal);
			function.add(parseSquareRoot());
			returnVal = function;
		}
		return returnVal;
	}
	
	private CalcObject parseSquareRoot() throws CalcSyntaxException {
	    CalcObject returnVal;
        if (this.token == CALC_SQRT) {
            parseNextToken();
            returnVal = CALC.POWER.createFunction(parseExpression(), CALC.D_HALF);
            return returnVal;
        }
        CalcObject next = parseFactorial();
        return next;
	}
	
	private CalcObject parseFactorial() throws CalcSyntaxException {
		CalcObject returnVal = parseAbs();
		while (this.token == CALC_FACTORIAL) {
			parseNextToken();
			returnVal = CALC.FACTORIAL.createFunction(returnVal);
		}
		return returnVal;
	}
	
	private CalcObject parseAbs() throws CalcSyntaxException {
		CalcObject returnVal;
		if (this.token == CALC_ABS) {
			parseNextToken();
			returnVal = CALC.ABS.createFunction(parseExpression());
			if (this.token != CALC_ABS) {
				throw new CalcSyntaxException("Missing close vertical bar");
			}
			parseNextToken();
			return returnVal;
		}
        CalcObject next = parseMatrix();
        return next;
	}
	
	private CalcObject parseMatrix() throws CalcSyntaxException {
		ArrayList<CalcVector> elements = new ArrayList<CalcVector>();
		if (this.token == CALC_MATRIXOPEN) {
			parseNextToken();
			while (true) {
				elements.add(parseVector());
				if (this.token != CALC_COMMA) {
					break;
				}
				parseNextToken();
			}
			if (this.token != CALC_MATRIXCLOSE) {
				throw new CalcSyntaxException("Missing close matrix bracket");
			}
			parseNextToken();
			CalcVector[] vectorArray = new CalcVector[elements.size()];
			elements.toArray(vectorArray);
			if (elements.size() == 1) {
				return elements.get(0);	//case simply a vector
			}
            return new CalcMatrix(vectorArray);	// case an actual matrix
		}
        return parseTerm();
	}
	
	private CalcVector parseVector() throws CalcSyntaxException {
		ArrayList<CalcObject> elements = new ArrayList<CalcObject>();
		if (this.token == CALC_MATRIXOPEN) {
			parseNextToken();
			while (true) {
				elements.add(parseExpression());
				if (this.token != CALC_COMMA) break;
				parseNextToken();
			}
			//parseNextToken();
			if (this.token != CALC_MATRIXCLOSE) {
				throw new CalcSyntaxException("Missing close vector bracket");
			}
			parseNextToken();
			CalcObject[] elementArray = new CalcObject[elements.size()];
			elements.toArray(elementArray);
			return new CalcVector(elementArray);
		}
		//case -> just list of elements with no open or close identifiers
        while (true) {
        	elements.add(parseExpression());
        	if (this.token != CALC_COMMA) break;
        	parseNextToken();
        }
        CalcObject[] elementArray = new CalcObject[elements.size()];
        elements.toArray(elementArray);
        return new CalcVector(elementArray);
	}
	
	private CalcObject parseTerm() throws CalcSyntaxException {
		CalcObject returnVal;
		if (this.token == CALC_SUBTRACT) {
			parseNextToken();
			return CALC.MULTIPLY.createFunction(CALC.NEG_ONE, parseTerm());
		}
		if (this.token == CALC_IDENTIFIER) {
			CalcSymbol id = parseIdentifier();
			if (this.token == CALC_PARENTHESISOPEN) {
				return parseFunction(id);
			}
			/*else if (this.parsed && CALC.hasDefinedVariable(id)) {
			    CalcObject temp = CALC.getDefinedVariable(id); //TODO this is where variable gets reassigned
			    Log.i("TAG", "Get defined variable: " + temp);
				return temp;
			}*/
			return id;
		}
		if (this.token == CALC_DIGIT || this.token == CALC_DECIMAL) {
			return parseNumber();
		}
		if (this.token == CALC_PARENTHESISOPEN) {
			parseNextToken();
			returnVal = parseExpression(); //return all the way to root recursion
			if (this.token != CALC_PARENTHESISCLOSE) {
				throw new CalcSyntaxException("Missing close parenthesis");
			}
			parseNextToken();
			return returnVal;
		}
		switch (this.token) {
			case CALC_PARENTHESISCLOSE:
				throw new CalcSyntaxException("Extra closing parenthesis");
		}
		throw new CalcSyntaxException("Unable to parse term: " + this.currentChar);
	}
	
	private CalcObject parseFunction(final CalcSymbol symbol) throws CalcSyntaxException{
		CalcFunction function = new CalcFunction(symbol);
		parseNextToken();
		
		if (this.token == CALC_PARENTHESISCLOSE) {
			parseNextToken();
			return function;
		}
		parseParameters(function);
		if (this.token == CALC_PARENTHESISCLOSE) {
			parseNextToken();
			return function;
		}
		throw new CalcSyntaxException("Expecting '('");
	}

	private void parseParameters(final CalcFunction inputFunction) throws CalcSyntaxException {
		while (true) {	//this scares the SHIT out of me but I have to
			inputFunction.add(parseExpression());
			if (this.token != CALC_COMMA) break; //no more parameters
			parseNextToken();
		}
	}
	
	private CalcSymbol parseIdentifier() throws CalcSyntaxException {
		StringBuffer identifier = new StringBuffer();
		identifier.append(this.currentChar);
		if (this.currentChar == 'V') { //XXX dirty fix to parse V1, V2, etc.
			parseNextChar();
			identifier.append(this.currentChar);
			parseNextChar();
		} else {
			parseNextChar();
			while ((this.currentChar >= 'a' && this.currentChar <= 'z') 
					|| (this.currentChar >= 'A' && this.currentChar <= 'Z') 
					|| (this.currentChar >= '0' && this.currentChar <= '9')) {
				identifier.append(this.currentChar);
				parseNextChar();
			}
		}
		this.currentCharIndex--;
		parseNextToken();
		CalcSymbol symbol;
		CalcSymbol temp;
		if ((temp = CALC.getSymbol(identifier.toString().toUpperCase(Locale.US))) != null)
			symbol = temp;
		else {
			symbol = new CalcSymbol(identifier);
			if (CALC.hasDefinedVariable(symbol)) { //if the symbol is a defined variable, use a substitution evaluator
				symbol.setEvaluator(new CalcSUB());
				//return CALC.getDefinedVariable(symbol);
			}
		}
		return symbol;
	}

	private CalcObject parseNumber() throws CalcSyntaxException {
		StringBuffer numberString = new StringBuffer();
		boolean isFloating = false;
		do {
			if (this.currentChar == '.') {
				if (isFloating) {
				    throw new CalcSyntaxException("Number must have one decimal point");
				}
				isFloating = true;
				numberString.append(this.currentChar);
				parseNextChar();
			}
			else {
				numberString.append(this.currentChar);
				parseNextChar();
			}
		} while ((this.currentChar >= '0' && this.currentChar <= '9') || this.currentChar == '.');
		this.currentCharIndex--;
		parseNextToken();
		/*if (!(this.currentChar >= '0' && this.currentChar <= '9') && this.currentChar != ',' && this.currentChar != ']') {		
			if (IsFloating) {
				return CALC.MULTIPLY.createFunction(new CalcDouble(numberString.toString()), parseTerm());
			}
            return CALC.MULTIPLY.createFunction(new CalcInteger(numberString.toString()), parseTerm());
		}*/
		if (isFloating) {
			return new CalcDouble(numberString.toString());
		}
        return new CalcInteger(numberString.toString());
	}

	private void parseNextChar() {
		if (this.inputString.length() > this.currentCharIndex) {
			this.currentChar = this.inputString.charAt(this.currentCharIndex++);
			return;
		}
		this.currentCharIndex = this.inputString.length() + 1;
		this.currentChar = ' ';
		this.token = CALC_NULL;
	}
	
	@Override
	public String toString() {
		return this.inputString;
	}
}