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
    private boolean isNegative = false;
  
    public BigInteger(int i) 
    {
        if (i < 0) {
            isNegative = true;
            i = -i;
        } else {
            isNegative = false;
        }

        if (i == 0) {
            digits = new int[1];
            digits[0] = 0;
            return;
        }

        int temp = i;
        int count = 0;
        while (temp > 0) {
            temp /= 10;
            count++;
        }

        digits = new int[count];
        for (int idx = count-1; idx >= 0; idx--) {
            digits[idx] = i % 10;
            i /= 10;
        }
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

        int[] refinedResult = refine(result);
        BigInteger finalResult = new BigInteger(refinedResult);
        finalResult.isNegative = (this.isNegative == true);

        return finalResult;
    }
    
    public BigInteger subtract(BigInteger big) {
        int[] num1;
        int[] num2;

        int[] bigger = absCompare(this, big);

        if ((this.isNegative == true) == (bigger == this.digits)) {
            num1 = this.digits;
            num2 = big.digits;
        }
        else {
            num1 = big.digits;
            num2 = this.digits;
        }    

    // assume this >= big and both are positive
        int maxLength = Math.max(num1.length, num2.length);
        int[] result = new int[maxLength];
        int borrow = 0;

        for (int i=0; i<maxLength; i++) {
            int thisDigit = (i < this.digits.length) ? this.digits[i] : 0;
            int bigDigit = (i < big.digits.length) ? big.digits[i] : 0;

            int calculation = thisDigit - bigDigit - borrow;

            if (calculation >= 0) {
                result[i] = calculation;
                borrow = 0;
            } else {
                result[i] = calculation + 10;
                borrow = 1;
            }
        }
        int[] refinedResult = refine(result);
        BigInteger finalResult = new BigInteger(refinedResult);

        if (bigger != num1) {
            finalResult.isNegative = true;
        }

        return finalResult;   
    }

    public BigInteger multiply(BigInteger big) {
        int maxLength = Math.max(this.digits.length, big.digits.length);
        // assume this >= big
        int[] result = new int[this.digits.length + big.digits.length];
        int carry = 0;

        for (int i=0; i<maxLength; i++) {
            for (int k=0; k<this.digits.length; k++) {
                int product = big.digits[i] * this.digits[k] + carry;
                result[i + k] = product % 10;
                carry = product / 10;
            }
        }

        if (carry >= 0) {
            result[this.digits.length + big.digits.length - 1] = carry;
        }

        int[] refinedResult = refine(result);
        BigInteger finalResult = new BigInteger(refinedResult);
        finalResult.isNegative = (this.isNegative != big.isNegative);

        return finalResult;
    }

    public int[] absCompare(BigInteger num1, BigInteger num2) {
        int[] digit1 = num1.digits;
        int[] digit2 = num2.digits;
        if (digit1.length > digit2.length) {
            return digit1;
        }
        if (digit1.length < digit2.length) {
            return digit2;
        }
        else {
            for (int i = 0; i < digit1.length; i++) {
                if (digit1[i] > digit2[i]) {
                    return digit1;
                }
                if (digit2[i] > digit1[i]) {
                    return digit2;
                }
            }
            return digit1;
        }
    }

    public int[] refine(int[] array) {
        int[] result = new int[array.length];
        for (int i=0; i<array.length; i++) {
            if (array[i] > 0) {
                result[i] = array[i];
            }
        }
        return result;
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
                        return num1.add(num2);
                    } else {
                        return num1.subtract(num2);
                    }
                case "-":
                    if (num1.isNegative == num2.isNegative) {
                        return num1.subtract(num2);
                    } else {
                        return num1.add(num2);
                    }
                case "*":
                    return num1.multiply(num2);
                default:
                    throw new AssertionError();
                }
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
