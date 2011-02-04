package pcgen.gui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests a Unit Test Case designed to hit many features of PCGen
 * See the PCG file for details
 */
@SuppressWarnings("nls")
public class pcGenGUIFranTest extends pcGenGUITestCase
{

	/**
	 * 
	 */
	public pcGenGUIFranTest()
	{
		// Empty Constructor
	}

	/**
	 * standard JUnit style constructor
	 * 
	 * @param name
	 */
	public pcGenGUIFranTest(String name)
	{
		super(name);
	}

	/**
	 * @return A <tt>TestSuite</tt>
	 */
	public static Test suite()
	{
		return new TestSuite(pcGenGUIFranTest.class);
	}

	/**
	 * Loads and outputs the character.
	 * 
	 * @throws Exception If an error occurs.
	 */
	public void testCode() throws Exception
	{
		runTest("Fran", "35e");
	}
}
