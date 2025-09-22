import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
  
  
public class BigInteger
{
    public static final String QUIT_COMMAND = "quit";
    public static final String MSG_INVALID_INPUT = "Wrong Input";
  
    // implement this
    public static final Pattern EXPRESSION_PATTERN = Pattern.compile("(\\d+)\\s*([\\+\\-\\*])\\s*(\\d+)");
    
    private int[] digits;
    private boolean isNegative;
  
    public BigInteger(int i)
    {
    }
  
    public BigInteger(int[] num1)
    {
        digits = num1;
        isNegative = false;
    }
  
    public BigInteger(String s)
    {   
        if (s.charAt(0) == '-') {
            isNegative = true;
            String numberPart = s.substring(1);  
            digits = new int[numberPart.length()];
            for (int i = 0; i < numberPart.length(); i++) {
            digits[i] = numberPart.charAt(numberPart.length() - 1 - i) - '0';
          }
        } else {
            isNegative = false;
            digits = new int[s.length()];
            for (int i=0; i<s.length(); i++) {
                digits[i] = s.charAt(s.length()-1-i) - '0';
            }
        }
    }
  
    public BigInteger add(BigInteger big) {
        // assume both are positive
        int resultLength = Math.max(big.digits.length, this.digits.length);
        int[] result = new int[resultLength + 1]; 
        int carry = 0;

        for (int i = 0; i < resultLength; i++) {
            int thisDigit = (i < this.digits.length) ? this.digits[i] : 0;
            int bigDigit = (i < big.digits.length) ? big.digits[i] : 0;
            int sum = thisDigit + bigDigit + carry; 
            result[i] = sum % 10;             
            carry = sum / 10;                    
        }   

        if (carry > 0) {                        
            result[resultLength] = carry;
        }

        return new BigInteger(result);
    }
    
    public BigInteger subtract(BigInteger big) {
    // assume this >= big and both are positive
        int[] result = new int[this.digits.length];
        int borrow = 0;

        for (int i=0; i<this.digits.length; i++) {
            int thisDigit = (i < this.digits.length) ? this.digits[i] : 0;
            int bigDigit = (i < big.digits.length) ? big.digits[i] : 0;

            int calculation = thisDigit - bigDigit - borrow;
            System.out.println(this);
            System.out.println(big);
            System.out.println(thisDigit);
            System.out.println(bigDigit);
            System.out.println(borrow);
            System.out.println(calculation);

            if (calculation >= 0) {
                result[i] = calculation;
                borrow = 0;
            } else {
                result[i] = calculation + 10;
                borrow = 1;
            }
        }
        return new BigInteger(result);   
    }
  public static void main(String[] args) {
      // Test basic construction and toString
      System.out.println("=== Testing Construction and toString ===");
      BigInteger num1 = new BigInteger("429");
      BigInteger num2 = new BigInteger("123");
      System.out.println("num1: " + num1.toString());
      System.out.println("num2: " + num2.toString());

      // Test addition
      System.out.println("\n=== Testing Addition ===");
      BigInteger sum = num1.add(num2);
      System.out.println("123 + 456 = " + sum.toString());

      // Test subtraction (assuming first >= second)
      System.out.println("\n=== Testing Subtraction ===");
      BigInteger diff = num2.subtract(num1);
      System.out.println("123 - 429 = " + diff.toString());

      // Test multiplication
      System.out.println("\n=== Testing Multiplication ===");
      BigInteger prod = num1.multiply(num2);
      System.out.println("123 * 456 = " + prod.toString());

      // Test negative numbers
      System.out.println("\n=== Testing Negative Numbers ===");
      BigInteger neg = new BigInteger("-123");
      System.out.println("Negative: " + neg.toString());

      // Test small numbers
      System.out.println("\n=== Testing Small Numbers ===");
      BigInteger small1 = new BigInteger("5");
      BigInteger small2 = new BigInteger("3");
      System.out.println("5 + 3 = " + small1.add(small2).toString());
      System.out.println("5 - 3 = " + small1.subtract(small2).toString());
      System.out.println("5 * 3 = " + small1.multiply(small2).toString());
  }
  
    public BigInteger multiply(BigInteger big) {
        // assume this >= big
        int[] result = new int[this.digits.length + big.digits.length];
        int carry = 0;

        for (int i=0; i<big.digits.length; i++) {
            for (int k=0; k<this.digits.length; k++) {
                int product = big.digits[i] * this.digits[k] + carry;
                result[i + k] = product % 10;
                carry = product / 10;
            }
        }

        if (carry >= 0) {
            result[this.digits.length + big.digits.length - 1] = carry;
        }

        BigInteger finalResult = new BigInteger(result);
        finalResult.isNegative = (this.isNegative != big.isNegative);
        return finalResult;
    }
  
    @Override
    public String toString()
    {
        String result = "";
        if (this.isNegative) {
            result = "-";
        }
        for (int i=this.digits.length-1 ; i>=0; i--) {
            result = result + this.digits[i];
        }
        return result;
    }
  
    static BigInteger evaluate(String input) throws IllegalArgumentException
    {
        Matcher matcher = EXPRESSION_PATTERN.matcher(input);
        if (matcher.matches()) {
            String group1 = matcher.group(1);
            String operator = matcher.group(2); 
            String group3 = matcher.group(3);

            BigInteger num1 = new BigInteger(group1);
            BigInteger num2 = new BigInteger(group3);

            switch (operator) {
                case "+":
                    if (num1.isNegative == num2.isNegative) {
                        BigInteger result = num1.add(num2);
                        result.isNegative = num1.isNegative;
                        return result;
                    } else {
                        if (num1.isNegative) {

                            BigInteger result = num2.add(num1);
                            result.isNegative = num1.isNegative;
                            return result;
                        } else {

                        }
                    }
                    break;
                case "-":
                    
                    break;
                case "*":
                    
                    break;
                default:
                    throw new AssertionError();
            }


        }
  
    public static void main1(String[] args) throws Exception
    {
        try (InputStreamReader isr = new InputStreamReader(System.in))
        {
            try (BufferedReader reader = new BufferedReader(isr))
            {
                boolean done = false;
                while (!done)
                {
                    String input = reader.readLine();
  
                    try
                    {
                        done = processInput(input);
                    }
                    catch (IllegalArgumentException e)
                    {
                        System.err.println(MSG_INVALID_INPUT);
                    }
                }
            }
        }
    }
  
    static boolean processInput(String input) throws IllegalArgumentException
    {
        boolean quit = isQuitCmd(input);
  
        if (quit)
        {
            return true;
        }
        else
        {
            BigInteger result = evaluate(input);
            System.out.println(result.toString());
  
            return false;
        }
    }
  
    static boolean isQuitCmd(String input)
    {
        return input.equalsIgnoreCase(QUIT_COMMAND);
    }
}
