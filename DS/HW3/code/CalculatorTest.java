import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CalculatorTest
{	
	private static Stack<Character> s;        // operator and parenthesis stack
	private static Queue<String> q;           // postfix output queue

	// Parser state variables
	private static int lastType = 1;          // 0: numbers/')' | 1: operators/'('/','
	private static boolean isLastNum = false; // tracks consecutive numbers

	// Function handling
	private static int avgCount = 0;          // parameter count for avg function


	// function to check if character is operator
	private static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
	}

	// function to check if token is avg function during evalutate()
	private static boolean isAvg(String token) { 
		return token.matches("\\d+ avg");
	}

	// function to return precedence of operators
	private static int pres(char c) {
		int result;
		switch (c) {
			case ',': 
				result = 1;
				break;
			case '+': 
				result = 2;
				break;
			case '-': 
				result = 2;
				break;
			case '*': 
				result = 3;
				break;
			case '%': 
				result = 3;
				break;
			case '/': 
				result = 3;
				break;
			case '^': 
				result = 4;
				break;
			case '~': 
				result = 4;
				break;
			default: 
				result = 0;
				break;
		}

		return result;
	}

	// handle left associative operators
	private static void handleLeft(char c) {
		while (!s.isEmpty() && pres(c) <= pres(s.peek()) && s.peek() != '(') {
			// pop while top of stack has greater than or equal precedence
			// pop until left parenthesis is found or stack is empty
				q.offer(Character.toString(s.pop()));
			}
			s.push(c);
	}

	// handle right associative operators
	// similar to handleLeft but with strict less than
	// as right associative operators should not pop same precedence
	private static void handleRight(char c) {
		while (!s.isEmpty() && pres(c) < pres(s.peek()) && s.peek() != '(') {
				q.offer(Character.toString(s.pop()));
			}
			s.push(c);
	}

	// rules for handling operators, parentheses and commas
	// basically anything other than numbers
	public static void operatorRules(char c) {
		if (isOperator(c)) {
			if (c == '-' && lastType == 1) {
				c = '~';
			}
			if (c == '^' || c == '~') {
				handleRight(c);
				lastType = 1;
			} else {
				handleLeft(c);
				lastType = 1;
			}
			// Handle operator
		} else if (c == '(') {
			s.push(c);
			lastType = 1;
			// Handle left parenthesis
		} else if (c == ')') {
			// pop until left parenthesis is found
			while (s.peek() != '(') {
				if (s.peek() != ',') {
					q.offer(Character.toString(s.pop()));
				} else {
					s.pop();
					// just pop comma without adding to output
				}
			}
			
			if (!s.isEmpty() && s.peek() == '(') {
				if (avgCount != 0) {
					// adding avg and avg count to the output queue
					String str = (avgCount + 1) + " avg";
					q.add(str);
					avgCount = 0;
				}
				s.pop(); 
			}
			lastType = 0;
			// Handle right parenthesis
		} else if (c == ',') {
			s.push(c);
			avgCount++;
			lastType = 1;
			// Handle comma
		} else if (c == ' ' || c == '	') {
			// Skip whitespace
		} else {
			throw new IllegalArgumentException("Invalid character: '" + c + "'");
		}
		
	}

	// main method for parsing infix to postfix
	private static void postfixParsing(String input) {
		s = new Stack<>();
		q = new LinkedList<>();

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			// Handle numbers
			if (Character.isDigit(c)) {
				if (isLastNum) {
					// two consecutive numbers without operator
					// reason why isLastNum is needed
					throw new IllegalArgumentException("cannot have to consecutive numbers without operators");
				}
				String str = "" + c;
				// handle multi-digit numbers
				while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
					str += input.charAt(i + 1);
					i++;
				}
				q.offer(str);
				lastType = 0;
				isLastNum = true;
			} else {
				if (c != ' ' && c != '(' && c != ')' && c != '	') {
					isLastNum = false;
				}
				operatorRules(c);
			}
		}

		// pop remaining operators from stack
		while (!s.isEmpty()) {
			if (s.peek() == '(') {
				throw new IllegalArgumentException("unresolved parenthesis: '('");
			}
			q.offer(Character.toString(s.pop()));
		}
	}

	// convert queue to string for output
	private static String queueToString() {
		StringBuilder sb = new StringBuilder();
		for (String s : q) {
			sb.append(s).append(" ");
		}
		return sb.toString().trim();  // Remove trailing space
	}

	// evaluate postfix expression
	private static long evaluate() {
		long A, B;
		Stack<Long> stk = new Stack<>();

		while (!q.isEmpty()) {
			String item = q.poll();
			Character lastChar = item.charAt(item.length() - 1);
			if (Character.isDigit(lastChar)) {
				// it's a number
				long tmp = Long.parseLong(item);
				stk.push(tmp);
			} else if (isOperator(lastChar) && lastChar != '~') {
				// it's a binary operator
				B = stk.pop();  // Second operand
				A = stk.pop();  // First operand
				long val = operation(A, B, lastChar);
				stk.push(val);
			} else if (lastChar == '~') {
				// it's a unary operator
				A = stk.pop();
				B = 0; // dummy value
				long val = operation(A, B, lastChar);
				stk.push(val);
			} else if (isAvg(item)) {
				// it's an avg function
				String str = "";
				int num;
				for (int i = 0; i < item.length(); i++) {
					if (Character.isDigit(item.charAt(i))) {
						str += item.charAt(i);
					} else break;
				}
				num = Integer.parseInt(str);
				A = stk.pop();
				for (int i = 1; i < num; i++) {
					// keep popping and adding to A
					B = stk.pop();
					A = A + B;
				}
				Long val = A / num; // avg calculation
				stk.push(val);
			}
		}

		return stk.pop();
	}

	// perform arithmetic operations inside evaluate()
	private static long operation(long a, long b, char ch) {
		long val = 0;
		switch (ch) {
			case '*':
				val = a * b;
				break;
			case '/':
				if (b == 0) throw new ArithmeticException("division by zero");
				val = a / b;
				break;
			case '+':
				val = a + b;
				break;
			case '-':
				val = a - b;
				break;
			case '%':
				if (b == 0) throw new ArithmeticException("modulo by zero");
				val = a % b;
				break;
			case '^':
				if (a == 0 && b < 0) throw new ArithmeticException("0 to the power of a negative value");
				val = (long) Math.pow(a, b);
				break;
			case '~':
				// Unary minus - only uses 'a', ignores 'b'
				val = -a;
				break;
		}
		return val;
	}

	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
				resetVariables();
			}
		}
	}

	private static void resetVariables() {
		lastType = 1;
		avgCount = 0;
		isLastNum = false;
	}

	private static void command(String input) {
		postfixParsing(input);
		
		String result = queueToString();
		Long calculation = evaluate();

		System.out.println(result);
		System.out.println(calculation);

		resetVariables();
	}
}
