@echo off
echo Compiling BigInteger...
javac BigInteger.java
if errorlevel 1 (
    echo Compilation failed!
    pause
    exit /b 1
)

echo Compiling test file...
javac BigIntegerTest.java
if errorlevel 1 (
    echo Test compilation failed!
    pause
    exit /b 1
)

echo.
echo Running unit tests...
echo =====================
java BigIntegerTest

echo.
echo Running input/output test...
echo ============================
echo Testing with predefined input file...
java BigInteger < test_input.txt > actual_output.txt

echo.
echo Comparing with expected output...
fc expected_output.txt actual_output.txt
if errorlevel 1 (
    echo Output test FAILED - differences found!
) else (
    echo Output test PASSED - all outputs match!
)

echo.
echo Testing performance (timeout after 5 seconds)...
echo =================================================
timeout 5 java BigInteger
echo.
echo Tests completed.
pause