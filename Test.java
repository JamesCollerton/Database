import java.util.*;

// This class handles all of the basic testing functions. It is not perfectly
// written as it is only testing.

// Responsibilites: Holding the list of tests run.
// 					Contains methods that can be used by all classes in order
// 					to run their tests.

public class Test {

	static int tests, num_test_classes = 7;
	private static int MENU = 0, DATABASE = 1, SCANNING = 2, FILE = 3, 
					   TABLE = 4, RECORD = 5;
	static List<List<String>> test_results = new ArrayList<List<String>>();

	// Creates an array of empty tests to be added to as they are carried
	// out.
	static void initialise_testing()
	{
		int i;

		for(i = 0; i < num_test_classes; ++i){
			test_results.add( new ArrayList<String>() );
		}
	}

	// Checks if something is the same as something else. If so it is added
	// to the test_results. If not errors out.
	static void is(Object x, Object y, String arg, int test_class) 
	{
 		add_one();
 		if (x == y) {add_to_tests(arg, test_class); return; }
 		if (x != null && x.equals(y)) {add_to_tests(arg, test_class); return; }
 		throw new Error("Test failed: " + x + ", " + y);
	}

	// Same as above but the 'is not' equivalent.
	static void is_not(Object x, Object y, String arg, int test_class) 
	{
 		add_one();
 		if (x != y) {add_to_tests(arg, test_class); return; }
 		if (x != null && !x.equals(y)) {add_to_tests(arg, test_class); return; }
 		throw new Error("Test failed: " + x + ", " + y);
	}

	// This prints the results. Each class of testing has a number assigned to 
	// it so goes through and prints them one by one.
	static void results()
	{
		print_tests("MENU", MENU);
		print_tests("DATABASE", DATABASE);
		print_tests("SCANNING", SCANNING);
		print_tests("FILE", FILE);
		print_tests("TABLE", TABLE);
		print_tests("RECORD", RECORD);

		System.out.printf("\n\nTOTAL TESTS PASSED: %d\n\n", tests);
	}

	// Prints the type of test, the test_results and then the total number of
	// tests run within that category.
	static void print_tests(String test_area, int array_ind)
	{
		int i;

		System.out.printf("\n\n ---------%s TESTS----------\n\n", test_area);

		for(i = 0; i < test_results.get(array_ind).size(); ++i){
			System.out.printf("\n%s\n", test_results.get(array_ind).get(i));
		}

		System.out.printf("\nTOTAL %s TESTS RUN: %d\n", test_area, 
						  test_results.get(array_ind).size());
	}

	// Increments the number of tests done.
	static void add_one()
	{
		tests++;
	}

	// Adds a test to the list of tests for the category i.
	static void add_to_tests(String arg, int i)
	{
		add_one();

		if(i < 0 || i > num_test_classes){
			System.err.printf("\n\nCan't assign tests here!\n\n");
			System.exit(1);
		}
		else{ test_results.get(i).add(arg);}
	} 

}
