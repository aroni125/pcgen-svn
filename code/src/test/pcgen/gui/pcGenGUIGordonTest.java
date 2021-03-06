package pcgen.gui;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests a Unit Test Case designed to hit many features of PCGen
 * See the PCG file for details
 */
@SuppressWarnings("nls")
public class pcGenGUIGordonTest extends pcGenGUITestCase
{

	/**
	 * 
	 */
	public pcGenGUIGordonTest()
	{
		// Empty Constructor
	}

	/**
	 * standard JUnit style constructor
	 * 
	 * @param name
	 */
	public pcGenGUIGordonTest(String name)
	{
		super(name);
	}

	/**
	 * @return A <tt>TestSuite</tt>
	 */
	public static Test suite()
	{
		return new TestSuite(pcGenGUIGordonTest.class);
	}

	/**
	 * Loads and outputs the character.
	 * 
	 * @throws Exception If an error occurs.
	 */
	public void testCode() throws Exception
	{
		runTest("Gordon", "35e");
	}
}
