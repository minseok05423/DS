/**
 * Test cases for BigInteger assignment
 *
 * This file contains comprehensive test cases for the BigInteger implementation
 * covering addition, subtraction, multiplication with various edge cases.
 */

import java.io.*;
import java.util.Arrays;

public class BigIntegerTest {

    private static int totalTests = 0;
    private static int passedTests = 0;

    public static void main(String[] args) {
        System.out.println("BigInteger Test Suite");
        System.out.println("====================");

        // Test basic operations
        testAddition();
        testSubtraction();
        testMultiplication();

        // Test edge cases
        testZeroOperations();
        testNegativeNumbers();
        testLargeNumbers();
        testWhitespaceHandling();

        // Test expression parsing
        testExpressionParsing();

        // Print results
        System.out.println("\n====================");
        System.out.printf("Test Results: %d/%d passed (%.1f%%)\n",
                         passedTests, totalTests,
                         totalTests > 0 ? (100.0 * passedTests / totalTests) : 0.0);

        if (passedTests == totalTests) {
            System.out.println("All tests passed! ✓");
        } else {
            System.out.printf("%d test(s) failed! ✗\n", totalTests - passedTests);
        }
    }

    private static void testAddition() {
        System.out.println("\n--- Testing Addition ---");

        // Basic addition
        testExpression("123+456", "579");
        testExpression("999+1", "1000");
        testExpression("1+999", "1000");

        // Large number addition
        testExpression("10000000000000000+200000000000000000", "210000000000000000");
        testExpression("123456789012345678901234567890+987654321098765432109876543210",
                      "1111111110111111111011111111100");

        // Addition with carry
        testExpression("999999999+1", "1000000000");
        testExpression("999+999", "1998");

        // Single digit addition
        testExpression("5+3", "8");
        testExpression("9+9", "18");
    }

    private static void testSubtraction() {
        System.out.println("\n--- Testing Subtraction ---");

        // Basic subtraction
        testExpression("456-123", "333");
        testExpression("1000-1", "999");
        testExpression("1000-999", "1");

        // Large number subtraction
        testExpression("20000000000000000-100000000000000000", "-80000000000000000");
        testExpression("30000000000000000-200000000000000000", "-170000000000000000");

        // Subtraction resulting in negative
        testExpression("123-456", "-333");
        testExpression("1-1000", "-999");

        // Subtraction with borrowing
        testExpression("1000000000-1", "999999999");
        testExpression("1000-123", "877");
    }

    private static void testMultiplication() {
        System.out.println("\n--- Testing Multiplication ---");

        // Basic multiplication
        testExpression("123*456", "56088");
        testExpression("999*999", "998001");
        testExpression("12*34", "408");

        // Large number multiplication
        testExpression("50000000*1000", "50000000000");
        testExpression("123456789*987654321", "121932631112635269");

        // Single digit multiplication
        testExpression("7*8", "56");
        testExpression("9*9", "81");

        // Multiplication by powers of 10
        testExpression("123*10", "1230");
        testExpression("123*100", "12300");
    }

    private static void testZeroOperations() {
        System.out.println("\n--- Testing Zero Operations ---");

        // Addition with zero
        testExpression("0+123", "123");
        testExpression("123+0", "123");
        testExpression("0+0", "0");
        testExpression("-1000000+0", "-1000000");

        // Subtraction with zero
        testExpression("123-0", "123");
        testExpression("0-123", "-123");
        testExpression("0-0", "0");

        // Multiplication with zero
        testExpression("123*0", "0");
        testExpression("0*123", "0");
        testExpression("0*0", "0");
    }

    private static void testNegativeNumbers() {
        System.out.println("\n--- Testing Negative Numbers ---");

        // Negative + Positive
        testExpression("-123+456", "333");
        testExpression("-456+123", "-333");
        testExpression("-100+200", "100");

        // Positive + Negative (equivalent to subtraction)
        testExpression("123+-456", "-333");
        testExpression("456+-123", "333");

        // Negative + Negative
        testExpression("-123+-456", "-579");
        testExpression("-999+-1", "-1000");

        // Negative - Positive
        testExpression("-123-456", "-579");
        testExpression("-100-200", "-300");

        // Positive - Negative (equivalent to addition)
        testExpression("123--456", "579");
        testExpression("100--200", "300");

        // Negative - Negative
        testExpression("-456--123", "-333");
        testExpression("-123--456", "333");

        // Negative multiplication
        testExpression("-123*456", "-56088");
        testExpression("123*-456", "-56088");
        testExpression("-123*-456", "56088");
    }

    private static void testLargeNumbers() {
        System.out.println("\n--- Testing Large Numbers ---");

        // Very large addition (near 100 digits)
        testExpression("1234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890+" +
                      "9876543210987654321098765432109876543210987654321098765432109876543210987654321098765432109876543210",
                      "11111111101111111110111111111011111111101111111110111111111011111111101111111110111111111011111111100");

        // Large subtraction
        testExpression("9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999-" +
                      "1111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111",
                      "8888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888888");

        // Large multiplication
        testExpression("12345678901234567890*98765432109876543210",
                      "1219326311370217952237463801111263526900");
    }

    private static void testWhitespaceHandling() {
        System.out.println("\n--- Testing Whitespace Handling ---");

        // Spaces around numbers and operators
        testExpression(" 123 + 456 ", "579");
        testExpression("  123  +  456  ", "579");
        testExpression("123+ 456", "579");
        testExpression("123 +456", "579");

        // Spaces with negative numbers
        testExpression(" -123 + 456 ", "333");
        testExpression("123 + -456", "-333");
        testExpression(" -123 * -456 ", "56088");

        // Multiple spaces
        testExpression("   123   +   456   ", "579");
        testExpression("123     *     456", "56088");
    }

    private static void testExpressionParsing() {
        System.out.println("\n--- Testing Expression Parsing ---");

        // Valid expressions from assignment examples
        testExpression("10000000000000000+200000000000000000", "210000000000000000");
        testExpression("20000000000000000-100000000000000000", "-80000000000000000");
        testExpression("30000000000000000 - 200000000000000000", "-170000000000000000");
        testExpression("50000000 *+1000", "50000000000");
        testExpression("-1000000 + 0", "-1000000");

        // Edge cases for parsing
        testExpression("1+-2", "-1");
        testExpression("1*-2", "-2");
        testExpression("-1+-2", "-3");
        testExpression("-1*-2", "2");
    }

    private static void testExpression(String input, String expected) {
        totalTests++;
        try {
            BigInteger result = BigInteger.evaluate(input);
            String actual = result.toString();

            if (expected.equals(actual)) {
                System.out.printf("✓ %s = %s\n", input, actual);
                passedTests++;
            } else {
                System.out.printf("✗ %s = %s (expected: %s)\n", input, actual, expected);
            }
        } catch (Exception e) {
            System.out.printf("✗ %s threw exception: %s\n", input, e.getMessage());
        }
    }

    /**
     * Manual test method that mimics interactive input/output
     * Run this to test the main method behavior
     */
    public static void testInteractiveMode() {
        System.out.println("\n--- Interactive Mode Test Cases ---");
        System.out.println("Copy and paste these inputs to test manually:");
        System.out.println();

        String[] testInputs = {
            "10000000000000000+200000000000000000",
            "20000000000000000-100000000000000000",
            "30000000000000000 - 200000000000000000",
            "50000000 *+1000",
            "-1000000 + 0",
            "123+456",
            "999*999",
            "0*12345",
            "-123*-456",
            "quit"
        };

        for (String input : testInputs) {
            System.out.println(input);
        }
    }

    /**
     * Performance test for large operations
     */
    public static void testPerformance() {
        System.out.println("\n--- Performance Test ---");

        // Generate large numbers
        StringBuilder largeNum1 = new StringBuilder();
        StringBuilder largeNum2 = new StringBuilder();

        for (int i = 0; i < 50; i++) {
            largeNum1.append("123456789");
            largeNum2.append("987654321");
        }

        String testExpr = largeNum1.toString() + "+" + largeNum2.toString();

        long startTime = System.currentTimeMillis();
        try {
            BigInteger result = BigInteger.evaluate(testExpr);
            long endTime = System.currentTimeMillis();
            System.out.printf("Large addition (450 digits) completed in %dms\n", endTime - startTime);
        } catch (Exception e) {
            System.out.printf("Performance test failed: %s\n", e.getMessage());
        }
    }
}