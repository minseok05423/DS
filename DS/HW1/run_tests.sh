#!/bin/bash

echo "Compiling BigInteger..."
javac BigInteger.java
if [ $? -ne 0 ]; then
    echo "Compilation failed!"
    exit 1
fi

echo "Compiling test file..."
javac BigIntegerTest.java
if [ $? -ne 0 ]; then
    echo "Test compilation failed!"
    exit 1
fi

echo
echo "Running unit tests..."
echo "====================="
java BigIntegerTest

echo
echo "Running input/output test..."
echo "============================"
echo "Testing with predefined input file..."
java BigInteger < test_input.txt > actual_output.txt

echo
echo "Comparing with expected output..."
if diff expected_output.txt actual_output.txt > /dev/null; then
    echo "Output test PASSED - all outputs match!"
else
    echo "Output test FAILED - differences found!"
    echo "Differences:"
    diff expected_output.txt actual_output.txt
fi

echo
echo "Testing performance (timeout after 5 seconds)..."
echo "================================================="
timeout 5 java BigInteger
echo
echo "Tests completed."