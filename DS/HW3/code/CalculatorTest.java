import java.io.*;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class CalculatorTest
{	
	private static Stack<Character> s;
	private static Queue<String> q;
	private static int lastType = 1;
	// 0: operators as defined in isOperator
	// 1: other types
	private static int avgCount = 0;
	private static String result = "";
	private static Long calculation = 0L;
	private static boolean isLastNum = false;


	private static boolean isOperator(char c) {
		return c == '+' || c == '-' || c == '*' || c == '/' || c == '%' || c == '^';
	}

	private static boolean isAvg(String token) {
		return token.matches("\\d+ avg");
	}

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

	private static void handleLeft(char c) {
		while (!s.isEmpty() && pres(c) <= pres(s.peek()) && s.peek() != '(') {
				q.offer(Character.toString(s.pop()));
			}
			s.push(c);
			lastType = 1;
	}

	private static void handleRight(char c) {
		while (!s.isEmpty() && pres(c) < pres(s.peek()) && s.peek() != '(') {
				q.offer(Character.toString(s.pop()));
			}
			s.push(c);
			lastType = 1;
	}

	public static void rules(char c) {
		if (isOperator(c)) {
			if (c == '-' && lastType == 1) {
				c = '~';
			}
			if (c == '^' || c == '~') {
				handleRight(c);
			} else {
				handleLeft(c);
			}
			// Handle operator
		} else if (c == '(') {
			s.push(c);
			lastType = 1;
			// Handle left parenthesis
		} else if (c == ')') {
			while (s.peek() != '(') {
				if (s.peek() != ',') {
					q.offer(Character.toString(s.pop()));
				} else {
					Character.toString(s.pop());
				}
			}
			if (!s.isEmpty() && s.peek() == '(') {
				if (avgCount != 0) {
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
			// Invalid character - ERROR
		}
		
	}

	public static void postfixParsing(String input) {
		s = new Stack<>();
		q = new LinkedList<>();

		for (int i = 0; i < input.length(); i++) {
			char c = input.charAt(i);
			if (Character.isDigit(c)) {
				if (isLastNum) {
					throw new IllegalArgumentException("cannot have to consecutive numbers without operators");
				}
				String str = "" + c;
				while (i + 1 < input.length() && Character.isDigit(input.charAt(i + 1))) {
					str += input.charAt(i + 1);
					i++;
				}
				q.offer(str);
				lastType = 0;
				isLastNum = true;
				// Handle number
			} else {
				if (c != ' ' && c != '(' && c != ')' && c != '	') {
					isLastNum = false;
				}
				rules(c);
			}
		}

		while (!s.isEmpty()) {
			if (s.peek() == '(') {
				throw new IllegalArgumentException("unresolved parenthesis: '('");
			}
			q.offer(Character.toString(s.pop()));
		}

		result = queueToString();
		calculation = evaluate();
		lastType = 1;
	}

	private static String queueToString() {
		StringBuilder sb = new StringBuilder();
		for (String s : q) {
			sb.append(s).append(" ");
		}
		return sb.toString().trim();  // Remove trailing space
	}

	public static long evaluate() {
		long A, B;
		Stack<Long> stk = new Stack<>();

		while (!q.isEmpty()) {
			String item = q.poll();
			Character lastChar = item.charAt(item.length() - 1);
			if (Character.isDigit(lastChar)) {
				long tmp = Long.parseLong(item);
				stk.push(tmp);
			} else if (isOperator(lastChar) && lastChar != '~') {
				B = stk.pop();  // Second operand
				A = stk.pop();  // First operand
				long val = operation(A, B, lastChar);
				stk.push(val);
			} else if (lastChar == '~') {
				A = stk.pop();
				B = 0; // dummy value
				long val = operation(A, B, lastChar);
				stk.push(val);
			} else if (isAvg(item)) {
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
					B = stk.pop();
					A = A + B;
				}
				Long val = A / num;
				stk.push(val);
			}
		}

		return stk.pop();
	}

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
				lastType = 1;
				avgCount = 0;
				result = "";
				calculation = 0L;
				isLastNum = false;
			}
		}
	}

	private static void command(String input) {
		postfixParsing(input);
		
		System.out.println(result);
		System.out.println(calculation);

		lastType = 1;
		avgCount = 0;
		result = "";
		calculation = 0L;
		isLastNum = false;
	}
}
