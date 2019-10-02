package edu.jmu.decaf;

import java.io.*;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit tests for parser
 */
public class TestParser extends TestCase
{
    /**
     * Initialization
     *
     * @param testName name of the test case
     */
    public TestParser(String testName)
    {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite(TestParser.class);
    }

    /**
     * Parse source code with invalid syntax
     * @param text Decaf source code
     * @return True if the parser correctly threw an
     * {@link InvalidSyntaxException}, false otherwise
     */
    public static boolean parseInvalidProgram(String text)
    {
        boolean passed = false;
        try {
            (new MyDecafParser()).parse((new MyDecafLexer()).lex(text));
        } catch (IOException ex) {
        } catch (InvalidTokenException ex) {
        } catch (InvalidSyntaxException ex) {
            passed = true;
        }
        return passed;
    }

    /**
     * Parse source code with invalid syntax
     * @param text Decaf source code
     */
    public static void assertInvalid(String text)
    {
        assertTrue(parseInvalidProgram(text));
    }

    /**
     * Parse source code with valid syntax (often a utility method for another,
     * more comprehensive test).
     * @param text Decaf source code
     * @return Parsed syntax tree
     */
    public static ASTProgram parseValidProgram(String text)
    {
        ASTProgram program = null;
        try {
            program = (new MyDecafParser()).parse(
                      (new MyDecafLexer()).lex(text));
        } catch (IOException ex) {
            assertTrue(false);
        } catch (InvalidTokenException ex) {
            assertTrue(false);
        } catch (InvalidSyntaxException ex) {
            assertTrue(false);
        }
        return program;
    }

    /**
     * Parse source code with valid syntax and make sure that it returns
     * a non-null {@link ASTNode} object
     * @param text Decaf source code
     */
    public static void assertValid(String text)
    {
        assertNotNull(parseValidProgram(text));
    }

    /**
     * Empty program
     */
    public void testEmpty()
    {
        ASTProgram program = parseValidProgram("");
        assertEquals(0, program.variables.size());
        assertEquals(0, program.functions.size());
    }

    /**
     * Test a variety of valid programs to make sure the parser doesn't throw
     * an exception
     */

    public void testSingleVar() { assertValid("int a;"); }
    public void testEmptyFunc() { assertValid("def void foo() { }"); }

    /**
     * Test a variety of invalid programs to make sure the parser throws an
     * exception.
     */

    public void testInvalidNoType() { assertInvalid("a"); }
    public void testInvalidVarNoSemicolon() { assertInvalid("int a"); }
}
