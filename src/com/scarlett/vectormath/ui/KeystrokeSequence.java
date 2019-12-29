
package com.scarlett.vectormath.ui;

import java.util.Locale;

import com.scarlett.vectormath.core.CalculusEngine;

public final class KeystrokeSequence { //TODO fix bug caused when cursor is not at end when user clicks solve
    //can I keep a pointer node at the end of KeyList? or can I modify existing functions to work for any node?
    private KeyList<Keystroke> sequence;
    private final CalculusEngine engine;
    private static String ans = "1";

    public KeystrokeSequence() { //Idea: show a faded symbol if the user has not entered it directly
        this.sequence = new KeyList<Keystroke>();
        this.engine = new CalculusEngine();
    }
    
    public final void add(final Keystroke key) { //TODO if commas are being added or removed, then parse entire sequence to replace brackets/parenthesis
        if (key == null) {
            return;
        }
        if (key.hasProperty(Keystroke.PROP_FAST_INPUT)) {
            this.sequence.add(key);
            return;
        } else if (key.hasProperty(Keystroke.PROP_RIGHT_PAREN)) {
            Boolean addParen = inputRightParen(this.sequence.getCurrent(), 1, false);
            if (addParen == null) {
                return;
            } else if (addParen) {
                this.sequence.add(new Keystroke(Keystroke.RIGHTBRACKET, Keystroke.PROP_FAST_INPUT));
                return;
            } else {
                this.sequence.add(new Keystroke(Keystroke.RIGHTPAREN, Keystroke.PROP_FAST_INPUT));
                return;
            }
        } else if (key.hasProperty(Keystroke.PROP_LEFT_PAREN)
                && inputLeftParen(this.sequence.getCurrent(), 1, false)) {
            this.sequence.add(new Keystroke(Keystroke.LEFTBRACKET, Keystroke.PROP_FAST_INPUT));
            return;
        } /*else if (key.hasProperty(Keystroke.PROP_COMMA)) {
        this.sequence.add(new Keystroke(Keystroke.COMMA, Keystroke.PROP_FAST_INPUT));}
        //if there are parenthesis on outside then replace with brackets
        */ //TODO when a comma is removed, parse input again
        this.sequence.add(key);
    }

    public final synchronized String solve() {
        Node<Keystroke> last = this.sequence.getLast();
        if (last != null) {
            final Runnable runnable = new Runnable() {
            	@Override
        	    public void run() {
        	    	ans = KeystrokeSequence.this.engine.execute(parseFunction(KeystrokeSequence.this.sequence));
        	    }
        	};
    		runnable.run();
            return ans.toLowerCase(Locale.US).replaceAll("pi", "π");
        }
        return ans = "0";
    }

    private static String addAsterisks(final KeyList<Keystroke> list) { //TODO fix function errors
        String returnVal = "";
        Node<Keystroke> temp = list.getLast();
        while (temp.data != null) {
            Keystroke token = temp.data;
            Node<Keystroke> previous = temp.prev;
            Keystroke key = previous.data;
            returnVal = token + returnVal;
            if (key == null) {
                return returnVal;
            }
            if (token.hasProperty(Keystroke.PROP_VARIABLE) || token.hasName(Keystroke.LEFTPAREN)
                    || token.hasName(Keystroke.LEFTBRACKET) || token.hasProperty(Keystroke.PROP_ONE_PARAM)
                    || token.hasProperty(Keystroke.PROP_TWO_PARAM)) {
                if (key.hasName(Keystroke.RIGHTPAREN) || key.hasName(Keystroke.RIGHTBRACKET)
                        || key.hasProperty(Keystroke.PROP_NUMBER) || key.hasProperty(Keystroke.PROP_VARIABLE)) {
                    returnVal = Keystroke.MULTIPLY + returnVal;
                }
            } else if (token.hasProperty(Keystroke.PROP_NUMBER)) {
                if (key.hasName(Keystroke.RIGHTPAREN) || key.hasName(Keystroke.RIGHTBRACKET)
                        || key.hasProperty(Keystroke.PROP_VARIABLE)) {
                    returnVal = Keystroke.MULTIPLY + returnVal;
                }
            }
            temp = previous;
        }
        return returnVal;
    }

    private static String parseFunction(final KeyList<Keystroke> input) {
        return addAsterisks(input).replaceAll(" ", "").replaceAll("ans", ans);
    }

    public final int putCursor(final int characterOffset) {
        return this.sequence.putCursor(characterOffset);
    }

    public final void clear() {
        this.sequence.clear();
    }

    public final void backspace() {
        this.sequence.remove();
    }
    
    public final int getIndex() {
        return this.sequence.getIndex();
    }
    
    @Override
    public String toString() {
        return this.sequence.toString();
    }
    
    private static Boolean inputRightParen(final Node<Keystroke> node, final int count, final boolean hasComma) {
        if (node.data == null) {
            return null;
        }
        Keystroke token = node.data;
        if (token.hasName(Keystroke.LEFTPAREN) || token.hasName(Keystroke.LEFTBRACKET)
                || token.hasProperty(Keystroke.PROP_ONE_PARAM) || token.hasProperty(Keystroke.PROP_TWO_PARAM)) {
            if (count == 1) {
                if (!token.hasName(Keystroke.LEFTPAREN) && !token.hasName(Keystroke.LEFTBRACKET)) {
                    return false;
                }
                if (hasComma) {
                    node.data = new Keystroke(Keystroke.LEFTBRACKET, Keystroke.PROP_FAST_INPUT);
                    return true;
                }
                node.data = new Keystroke(Keystroke.LEFTPAREN, Keystroke.PROP_FAST_INPUT);
                return false;
            }
            return inputRightParen(node.prev, count-1, hasComma);
        } else if (token.hasName(Keystroke.RIGHTPAREN)) {
            return inputRightParen(node.prev, count+1, hasComma);
        } else if (token.hasName(Keystroke.COMMA) && count == 1) {
            return inputRightParen(node.prev, 1, true);
        } else if (token.hasName(Keystroke.RIGHTBRACKET)) { //can't be vector
            return false;
        }
        return inputRightParen(node.prev, count, hasComma);
    }
    


    private boolean inputLeftParen(final Node<Keystroke> node, final int count, final boolean hasComma) {
        if (node == null || node.data == null) {
            return false;
        }
        Keystroke token = node.data;
        if (token.hasName(Keystroke.RIGHTPAREN) || token.hasName(Keystroke.RIGHTBRACKET)) {
            if (count == 1) {
                if (!token.hasName(Keystroke.RIGHTPAREN) && !token.hasName(Keystroke.RIGHTBRACKET)) {
                    return false;
                }
                if (hasComma) {
                    node.data = new Keystroke(Keystroke.RIGHTBRACKET, Keystroke.PROP_FAST_INPUT);
                    return true;
                }
                node.data = new Keystroke(Keystroke.RIGHTPAREN, Keystroke.PROP_FAST_INPUT);
                return false;
            }
            return inputLeftParen(node.next, count-1, hasComma);
        } else if (token.hasName(Keystroke.LEFTPAREN)) {
            return inputLeftParen(node.next, count+1, hasComma);
        } else if (token.hasName(Keystroke.COMMA) && count == 1) {
            return inputLeftParen(node.next, 1, true);
        } else if (token.hasName(Keystroke.LEFTBRACKET)) { //can't be vector
            return false;
        }
        return inputLeftParen(node.next, count, hasComma);
    }
    
    private static class KeyList<T> {
        
        private Node<T> curr;
        private int index;
        
        protected KeyList() {
            this.curr = new Node<T>(null, null, null);
            this.index = 0;
        }

        protected KeyList(Node<T> current, int position) {
            this.curr = current;
            this.index = position;
        }

        protected int getIndex() {
            return this.index;
        }

        protected int putCursor(final int characterOffset) { //TODO the character offset will not always correspond to a possible index
            if (characterOffset < this.index) { //why does this sometimes cause a nullpointer exception?
                do {
                    this.index -= this.curr.length(); //TODO why would this cause a null pointer exception?
                    this.curr = this.curr.prev; //input grad(x^2y^2z^2) then remove square roots then try solving
                } while (characterOffset < this.index);
            }
            if (characterOffset > this.index) {
                do {
                    if (this.curr.next == null) {
                        break;
                    }
                    this.curr = this.curr.next;
                    this.index += this.curr.length();
                } while (characterOffset > this.index);
            }
            return this.index;
        }

        protected void add(final T elem) {
            Node<T> temp = new Node<T>(elem, this.curr, this.curr.next);
            if (this.curr.next != null) {
                this.curr.next.prev = temp;
            }
            this.curr.next = temp;
            this.curr = temp;
            this.index += this.curr.length();
        }
        
        protected void remove() {
            if (this.curr.data != null) {
                if (this.curr.prev != null) {
                    this.curr.prev.next = this.curr.next;
                }
                if (this.curr.next != null) {
                    this.curr.next.prev = this.curr.prev;
                }
                this.index -= this.curr.length();
                this.curr = this.curr.prev;
            }
        }

        protected void clear() {
            this.curr = new Node<T>(null, null, null);
            this.index = 0;
        }
        
        protected Node<T> getCurrent() {
            return this.curr;
        }
        
        protected Node<T> getLast() {
            if (this.curr.data == null) {
                return null;
            }
            return getLast(this.curr);
        }
        
        private Node<T> getLast(final Node<T> node) {
            if (node.next == null) {
                return node;
            }
            //this.index += node.length(); //only change index when cursor is moved
            return getLast(node.next);
        }
        
        @Override
        public final KeyList<T> clone() {
            return new KeyList<T>(this.curr.clone(), this.index);
        }

        @Override
        public final String toString() {
            String returnVal = "";
            Node<T> temp;
            for (temp = this.curr; temp.data != null; temp = temp.prev) {
                returnVal = temp + returnVal;
            }
            for (temp = this.curr.next; temp != null; temp = temp.next) {
                returnVal += temp;
            }
            return returnVal;
        }
    }
    
    private static class Node<T> {
        
        private T data;
        private Node<T> prev;
        private Node<T> next;
        
        protected Node(final T elem, final Node<T> before, final Node<T> after) {
            this.data = elem;
            this.prev = before;
            this.next = after;
        }
        
        @Override
        public final Node<T> clone() {
            return new Node<T>(this.data, this.prev, this.next);
        }
        
        @Override
        public final String toString() {
            return this.data + "";
        }
        
        public final int length() {
            return this.toString().length();
        }
    }
}
