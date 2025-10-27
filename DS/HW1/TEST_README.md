# BigInteger Test Suite

This directory contains comprehensive test cases for the BigInteger assignment.

## Files Created

- **`BigIntegerTest.java`** - Comprehensive unit test suite
- **`test_input.txt`** - Input file for automated testing
- **`expected_output.txt`** - Expected output for the input file
- **`run_tests.bat`** - Windows batch script to run all tests
- **`run_tests.sh`** - Linux/Mac shell script to run all tests

## Test Categories

### 1. Unit Tests (BigIntegerTest.java)

#### Addition Tests
- Basic addition (123+456 = 579)
- Large number addition (100+ digit numbers)
- Addition with carry (999+1 = 1000)
- Single digit addition

#### Subtraction Tests
- Basic subtraction (456-123 = 333)
- Large number subtraction
- Subtraction resulting in negative numbers
- Subtraction with borrowing

#### Multiplication Tests
- Basic multiplication (123*456 = 56088)
- Large number multiplication
- Single digit multiplication
- Multiplication by powers of 10

#### Edge Cases
- **Zero Operations**: Addition, subtraction, multiplication with zero
- **Negative Numbers**: All combinations of positive/negative operands
- **Large Numbers**: Operations with numbers approaching 100 digits
- **Whitespace Handling**: Various spacing patterns around operators and numbers

#### Expression Parsing Tests
- Tests based on assignment examples
- Edge cases like `1+-2`, `1*-2`, etc.

### 2. Input/Output Tests

The `test_input.txt` and `expected_output.txt` files contain test cases that match the assignment's input/output format:

```
Input: 10000000000000000+200000000000000000
Output: 210000000000000000

Input: 20000000000000000-100000000000000000
Output: -80000000000000000
```

## Running Tests

### Windows
```cmd
run_tests.bat
```

### Linux/Mac
```bash
./run_tests.sh
```

### Manual Testing

1. **Compile the code:**
   ```bash
   javac BigInteger.java
   ```

2. **Run unit tests:**
   ```bash
   javac BigIntegerTest.java
   java BigIntegerTest
   ```

3. **Test input/output:**
   ```bash
   java BigInteger < test_input.txt
   ```

4. **Interactive testing:**
   ```bash
   java BigInteger
   # Then enter expressions like: 123+456
   # Type 'quit' to exit
   ```

## Test Coverage

The test suite covers:

- ✅ All three operations: +, -, *
- ✅ Positive and negative numbers
- ✅ Zero operations
- ✅ Large numbers (up to 100 digits)
- ✅ Whitespace handling
- ✅ Expression parsing
- ✅ Assignment example cases
- ✅ Edge cases and corner cases
- ✅ Performance testing

## Expected Test Results

When running `BigIntegerTest.java`, you should see output like:

```
BigInteger Test Suite
====================

--- Testing Addition ---
✓ 123+456 = 579
✓ 999+1 = 1000
...

--- Testing Subtraction ---
✓ 456-123 = 333
...

====================
Test Results: XX/XX passed (100.0%)
All tests passed! ✓
```

## Assignment Requirements Verification

These tests verify:

1. **Input Format**: Various spacing and sign combinations
2. **Output Format**: No extra characters, correct sign handling
3. **Large Numbers**: Up to 100 digits input, up to 200 digits output
4. **Operations**: +, -, * with correct precedence for signs
5. **Edge Cases**: Zero, negative numbers, large numbers

## Performance Testing

The test suite includes basic performance testing for large number operations. The assignment requires completion within 40 seconds for the test set.

## Debugging Tips

If tests fail:

1. Check the `toString()` method for correct digit order
2. Verify sign handling in arithmetic operations
3. Check array bounds in operations
4. Ensure proper handling of leading zeros
5. Verify regex pattern matches all valid inputs

## Notes

- Tests assume the BigInteger class follows the provided skeleton code structure
- All test cases use only the specified operations (+, -, *)
- No external libraries are used, following assignment constraints
- Test inputs follow the specified format requirements