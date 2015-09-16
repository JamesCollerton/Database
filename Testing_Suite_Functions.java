import java.util.*;

// This class handles running the specific tests. It has its own main and
// can be run using java Testing_Suite

// Responsibilites: Running a list of interactive unit tests.
//                  Passing results to test class to be printed.

// NOTE: The run class seems pretty self explanatory. Instead I will only comment
// the functions as they go. Again, this is not perfectly written as it is only
// temporary testing.

public class Testing_Suite_Functions
{
    private int counter;

    private int MENU = 0, DATABASE = 1, SCANNING = 2, FILE = 3, 
                TABLE = 4, RECORD = 5;

	private Test test = new Test();
	private Menu menu = new Menu();
	private Database test_database = new Database("Test Database");
    private Files test_file = new Files();
    private ScanFunctions scan_func = new ScanFunctions();
	private Database_UI db_UI;

	public void run()
	{
        // INITIALISING ///////////////////////////////////////////////////////

		test.initialise_testing();

    	System.out.printf("\n\nInteractive Testing Suite.\n\n");

        ///////////////////////////////////////////////////////////////////////

        ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECK CAN CREATE BASIC DATABASE.\n");
    	create_db_tests("James");
        test.add_one();

        System.out.printf("\nCHECK DEALS WITH REPLICATE NAME.\n");
    	create_db_tests("James");

        System.out.printf("\nCHECK FOR SPACES IN NAME.\n");
    	create_db_tests("James Space Test");

        System.out.printf("\nCHECK FOR BLANK DATABASE NAME.\n");
    	error_create_db_tests("");

        System.out.printf("\nCHECK FOR ONLY WHITESPACE NAME.\n");
    	error_create_db_tests(" ");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECK FOR SELECTING EXISTING DATABASES.\n");
    	select_db_tests("James");

        System.out.printf("\nCHECK FOR SELECTING NON-EXISTENT DATABASES.\n");
    	error_select_db_tests("Steven");

        System.out.printf("\nCHECK FOR SELECTING EMPTY DATABASES.\n");
    	error_select_db_tests("");

        System.out.printf("\nCHECK FOR SELECTING WHITESPACE DATABASES.\n");
    	error_select_db_tests(" ");

     //    ///////////////////////////////////////////////////////////////////////

    	System.out.printf("\nTESTING CREATE DATABASE, ENTER ANYTHING.\n");
    	menu_intrpt_cmmd(Action.CREATE_DATABASE);

    	System.out.printf("\nTESTING SELECT DATABASE, ENTER ANYTHING.\n");
    	menu_intrpt_cmmd(Action.SELECT_DATABASE);

    	System.out.printf("\nTESTING CREATE TABLE, ENTER BLANK.\n");
    	db_intrpt_cmmd(Action.CREATE_TABLE);

    	System.out.printf("\nTESTING SELECT TABLE, ENTER BLANK.\n");
    	db_intrpt_cmmd(Action.SELECT_TABLE);

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nTESTING CAN CREATE TABLE.\n");
    	db_add_table("James");

        System.out.printf("\nTESTING DETECTS DUPLICATE TABLE.\n");
    	db_add_table("James");

        System.out.printf("\nTESTING DOES NOT CREATE TABLE WITH BLANK NAME.\n");
    	error_db_add_table("");

        System.out.printf("\nTESTING DOES NOT CREATE WHITESPACE TABLE.\n");
    	error_db_add_table(" ");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nTESTING CAN SELECT EXISTING TABLE.\n");
    	db_sel_table("James");

        System.out.printf("\nTESTING DOES NOT SELECT NON-EXISTENT TABLE.\n");
    	error_db_sel_table("Steven");

        System.out.printf("\nTESTING DOES NOT SELECT BLANK TABLE NAME.\n");
    	error_db_sel_table("");

        System.out.printf("\nTESTING DOES NOT SELECT WS TABLE NAME.\n");
    	error_db_sel_table(" ");

     //    ///////////////////////////////////////////////////////////////////////

    	db_UI = new Database_UI(test_database);

        System.out.printf("\nCHECK IN ADD RECORD SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.ADD_RECORD, db_UI);

        System.out.printf("\nCHECK IN PRINT TABLE SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.PRINT_TABLE, db_UI);

        System.out.printf("\nCHECK IN ALTER COLUMNS SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.ALTER_COLS, db_UI);

        System.out.printf("\nCHECK IN GET COLUMNS SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.GET_COLS, db_UI);

        System.out.printf("\nCHECK IN GET NUMBER COLUMNS SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.GET_NUM_COLS, db_UI);

        System.out.printf("\nCHECK IN GET NUMBER ROWS SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.GET_NUM_ROWS, db_UI);

        System.out.printf("\nCHECK IN GET RECORD SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.GET_RECORD, db_UI);

        System.out.printf("\nCHECK IN DELETE RECORD SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.DELETE_RECORD, db_UI);

        System.out.printf("\nCHECK IN WRITE FILE SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.WRITE_FILE, db_UI);

        System.out.printf("\nCHECK IN READ FILE SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.READ_FILE, db_UI);

        System.out.printf("\nCHECK IN NO SEQUENCE.\n");
    	db_intrpt_cmmd(test_database.table("James"), Action.NO_COMMAND, db_UI);

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECK GETTING COLUMNS WORKS.\n");
        test_get_col_names(db_UI, "age weight height", "age", "weight", "height");

        System.out.printf("\nCHECK GETTING COLUMNS WORKS AND IGNORES WS.\n");
        test_get_col_names(db_UI, "age weight height      ", "age", "weight", "height");

        System.out.printf("\nCHECK GETTING COLUMNS WORKS AND IGNORES WS.\n");
        test_get_col_names_fail(db_UI, "age weight       nationality");

        System.out.printf("\nCHECK RECOGNISES REPLICATE COLUMNS.\n");
        test_get_col_names_fail(db_UI, "age weight weight");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECK READS AND WRITES BASIC TABLE TO FILE.\n");
        test_read_write("test_1");

        System.out.printf("\nCHECK READS AND WRITES TABLE WITH SPACES IN NAME.\n");
        test_read_write("test_2");

        System.out.printf("\nCHECK READS AND WRITES JUST NAMES TO FILE.\n");
        test_read_write("test_3");

        System.out.printf("\nCHECK READS AND WRITES VALUES WITH NEW LINE TO FILE.\n");
        test_read_write("test_4");

        System.out.printf("\nCHECK READS AND WRITES VALUES WITH NEW LINE TO FILE.\n");
        test_read_write("test_5");

        System.out.printf("\nCHECK READS AND WRITES VALUES WITH NEW LINE TO FILE.\n");
        test_read_write("test_6");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nENTER LONG NAMES.\n");
        test_add_fields("AAAAAAAAAAAAAAAAAAAAAAAAA");

        System.out.printf("\nENTER SHORT NAMES.\n");
        test_add_fields("A");

        System.out.printf("\nENTER WS AND SPECIAL CHARACTERS.\n");
        test_add_fields("                                   ");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECK BASIC SCAN: TYPE James.\n");
        test_scan("James");

        System.out.printf("\nCHECK BASIC WS SCAN: TYPE ONE SPACE.\n");
        test_scan(" ");

        System.out.printf("\nCHECK BLANK SCAN: TYPE NOTHING.\n");
        test_scan("");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING SPACE SPLITTING.\n");
        test_split("doe", "la", "me", "so", "doe la me so", " ");

        System.out.printf("\nCHECKING SPACE SPLITTING.\n");
        test_split("doe", "la", "me", "so", "doe || la || me || so", "\\|\\|");

        System.out.printf("\nCHECKING SPACE SPLITTING.\n");
        test_split("doe", "la", "me", "so", "doe | la | me | so", "\\|");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING INTEGER PARSING.\n");
        test_integer_parse(1, "1");
        test_integer_parse(1, "01");
        test_integer_parse(-1, "1.0");
        test_integer_parse(-1, "1.");
        test_integer_parse(-1, ".10");
        test_integer_parse(-1, "abc");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING EMPTY STRING FINDER.\n");
        test_empty_string(" ", true);
        test_empty_string("                 ", true);
        test_empty_string("         ", true);
        test_empty_string("a b c s ", false);
        test_empty_string("         aaaaaa", false);

     //   ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING ADD RECORD SEQUENCE.\n");
        counter = 0;
        check_add_record("It's");
        check_add_record("a");
        check_add_record("1");

     //   ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING ALTER_COL SEQUENCE.\n");
        counter = 0;
        check_alter_col("New", "Two", "Three", "One", "New");
        check_alter_col("One", "New", "Three", "Two", "New");
        check_alter_col("One", "Two", "New", "Three", "New");
        check_alter_col("One", "Two", "Three", "One", "One");
        check_alter_col("One", "Two", "Three", "One", "Two");
        check_alter_col("One", "Two", "Three", "Four", "One");

     //    ///////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING GET RECORD.\n");

        System.out.printf("\nGET FIRST RECORD.\n");
        check_get_record("test_1", 0, 0);

        System.out.printf("\nGET SECOND RECORD.\n");
        check_get_record("test_1", 1, 1);

        System.out.printf("\nGET THIRD RECORD.\n");
        check_get_record("test_1", 2, 2);

        System.out.printf("\nGET NON-EXISTENT RECORD.\n");
        check_get_record("test_1", 8, -1);

     //    // //////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING DELETE RECORD.\n");

        System.out.printf("\nDELETE FIRST RECORD.\n");
        check_del_record("test_5", true, 0);

        System.out.printf("\nDELETE SECOND RECORD.\n");
        check_del_record("test_5", true, 1);

        System.out.printf("\nDELETE THIRD RECORD.\n");
        check_del_record("test_5", true, 3);

        System.out.printf("\nDELETE NON-EXISTENT RECORD.\n");
        check_del_record("test_5", false, 8);

        // //////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECK READS AND WRITES BASIC TABLE TO FILE.\n");
        check_print_tab_details("test_1");

        System.out.printf("\nCHECK READS AND WRITES TABLE WITH SPACES IN NAME.\n");
        check_print_tab_details("test_2");

        System.out.printf("\nCHECK READS AND WRITES JUST NAMES TO FILE.\n");
        check_print_tab_details("test_3");

        System.out.printf("\nCHECK READS AND WRITES VALUES WITH NEW LINE TO FILE.\n");
        check_print_tab_details("test_4");

        System.out.printf("\nCHECK READS AND WRITES VALUES WITH NEW LINE TO FILE.\n");
        check_print_tab_details("test_5");

        System.out.printf("\nCHECK READS AND WRITES VALUES WITH NEW LINE TO FILE.\n");
        check_print_tab_details("test_6");

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING THAT COLUMN TYPES ARE SET.\n");
        check_col_types("int", "int", "int", "age", "weight", "height");

        System.out.printf("\nCHECKING THAT COLUMN TYPES ARE SET.\n");
        check_col_types("int", "string", "float", "age", "weight", "height");

        System.out.printf("\nCHECKING THAT COLUMN TYPES ARE SET.\n");
        check_col_types("float", "float", "float", "age", "weight", "height");

        System.out.printf("\nCHECKING THAT COLUMN TYPES ARE SET.\n");
        check_col_types("int", "string", "int", "age", "weight", "height");

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING THAT VALID/ INVALID TYPES ARE RECOGNISED.\n");
        check_valid_invalid_cols("int", "string", "int", "age", "weight", "height", "a", 0);

        System.out.printf("\nCHECKING THAT VALID/ INVALID TYPES ARE RECOGNISED.\n");
        check_valid_invalid_cols("int", "int", "int", "age", "weight", "height", "1", 1);

        System.out.printf("\nCHECKING THAT VALID/ INVALID TYPES ARE RECOGNISED.\n");
        check_valid_invalid_cols("int", "string", "float", "age", "weight", "height", "1", 1);

        System.out.printf("\nCHECKING THAT VALID/ INVALID TYPES ARE RECOGNISED.\n");
        check_valid_invalid_cols("int", "float", "int", "age", "weight", "height", "1", 1);

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nCHECKING THAT VALID/ INVALID READING IN TABLES ARE RECOGNISED.\n");
        check_invalid_cols_unread("test_7");

        System.out.printf("\nCHECKING THAT VALID/ INVALID READING IN TABLES ARE RECOGNISED.\n");
        check_invalid_cols_unread("test_8");

        System.out.printf("\nCHECKING THAT VALID/ INVALID READING IN TABLES ARE RECOGNISED.\n");
        check_invalid_cols_unread("test_9");

        System.out.printf("\nCHECKING THAT VALID/ INVALID READING IN TABLES ARE RECOGNISED.\n");
        check_invalid_cols_unread("test_10");

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nTESTING THE FINDING OF NAMES IN TABLES.\n");
        find_table_name("Jam", "James");

        System.out.printf("\nTESTING THE FINDING OF NAMES IN TABLES.\n");
        find_table_name("s", "James");

        System.out.printf("\nTESTING THE FINDING OF NAMES IN TABLES.\n");
        find_table_name("Space", "James");

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nTESTING THE FINDING OF NAMES IN DATABASES.\n");
        find_database_name("Space", "James");

        System.out.printf("\nTESTING THE FINDING OF NAMES IN DATABASES.\n");
        find_database_name("Test", "James");

        //////////////////////////////////////////////////////////////////////

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nTESTING FOREIGN KEYS WORK.\n");
        test_for_keys("for_key_test_1", "for_key_test_2", "Test Database", "1");

        System.out.printf("\nTESTING FOREIGN KEYS WORK.\n");
        test_for_keys("for_key_test_1", "for_key_test_2", "Test Database", "0");

        System.out.printf("\nTESTING FOREIGN KEYS WORK.\n");
        test_for_keys("for_key_test_1", "for_key_test_2", "Test Database", "2");

        //////////////////////////////////////////////////////////////////////

        System.out.printf("\nTESTING FOREIGN KEYS WORK.\n");
        test_for_keys_error("for_key_test_1", "for_key_test_2", "Test Database", "-1");

        System.out.printf("\nTESTING FOREIGN KEYS WORK.\n");
        test_for_keys_error("for_key_test_1", "for_key_test_2", "Test Database", "20");

        System.out.printf("\nTESTING FOREIGN KEYS WORK.\n");
        test_for_keys_error("for_key_test_1", "for_key_test_2", "Test Database", "32");

        //////////////////////////////////////////////////////////////////////

    	test.results();
	}

    // Creates a database of a given name, checks if it exists.
    // DONE
	private void create_db_tests(String db_name)
	{
    	menu.create_database_seq(true, db_name);
    	test.is_not(menu.database(db_name), null, db_name + 
                    " create database test passed.", MENU); 
	}

    // Tries to create a database of an invalid name, checks it doesn't exist.
    // DONE
	private void error_create_db_tests(String db_name)
	{
    	menu.create_database_seq(true, db_name);
    	test.is(menu.database(db_name), null, db_name + 
                " error create database test passed.", MENU); 
	}

    // Tries to select a database of a given name. Checks that it is selected
    // and that it exists.
    // DONE
	private void select_db_tests(String db_name)
	{
    	menu.select_database_seq(true, db_name);
    	test.is_not(menu.database(db_name), null, db_name + 
                    " database select test passed.", MENU); 
	}

    // Tries to select an invalid database. Checks it doesn't exist.
    // DONE
	private void error_select_db_tests(String db_name)
	{
    	menu.select_database_seq(true, db_name);
    	test.is(menu.database(db_name), null, db_name + 
                " error database select test passed.", MENU); 
	}

    // Tries adding a table, then checks it exists.
    // DONE.
	private void db_add_table(String tab_name)
	{
    	test_database.add_table(true, tab_name);
    	test.is_not(test_database.table(tab_name), null, tab_name + 
                    " add table test passed.", MENU); 
	}

    // Tries adding an invalid table, then checks it doesn't exist.
    // DONE
	private void error_db_add_table(String tab_name)
	{
    	test_database.add_table(true, tab_name);
    	test.is(test_database.table(tab_name), null, tab_name + 
                " error add table test passed.", MENU); 
	}

    // Tries selecting a table that exists, should find it and checks it
    // exists.
    // DONE
	private void db_sel_table(String tab_name)
	{
    	test_database.select_table(true, tab_name);
    	test.is_not(test_database.table(tab_name), null, tab_name + 
                    " database select table test passed.", DATABASE); 
	}

    // Tries selecting a DB that shouldn't exist. Checks errors and table does
    // not exist.
    // DONE.
	private void error_db_sel_table(String tab_name)
	{
    	test_database.select_table(true, tab_name);
    	test.is(test_database.table(tab_name), null, tab_name + 
                " error database select table test passed.", DATABASE); 
	}

    // Interprets each of the different commands in turn and checks that the
    // table is not detected as NULL and the correct reaction happens.
    // DONE
	private void db_intrpt_cmmd(Table sel_tab, Action select_cmmd, 
								Database_UI db_UI)
	{
    	Action result = test_database.interpret_select_cmmd(sel_tab, select_cmmd, 
                                                            db_UI, true);
    	test.is(result, select_cmmd, sel_tab + 
                " database command interpret test passed.", DATABASE); 
	}

    // Checks all of the commands from the menu have been correctly interpreted.
    // DONE
	private void menu_intrpt_cmmd(Action action)
	{
		menu.change_cmmd_ind(action);
		Action result = menu.interpret_ind(true);
        test.is(result, action, action + 
                " menu interpreter test passed.", MENU); 
	}

    // Checks all of the commands from the database have been correctly 
    // interpreted.
    // DONE
	private void db_intrpt_cmmd(Action action)
	{
		Action result = menu.intrpt_sel_db_cmmd(test_database, action, true);
        test.is(result, action, action + 
                " database interpreter (menu) test passed.", DATABASE); 
	}

    // Tests that the column names function is correctly working 
    // DONE.
    private void test_get_col_names(Database_UI db_UI, String arg, String out_1,
                                    String out_2, String out_3)
    {
        String[] out_vec = {out_1, out_2, out_3};
        List<String> col_names = db_UI.get_col_names(true, arg);
        for(int i = 0; i < col_names.size(); ++i){
            test.is(col_names.get(i), out_vec[i], out_vec[i] + 
                    " get column names test passed.", DATABASE); 
        }
    }

    // Same as above but makes sure necessary ones fail.
    // DONE.
    private void test_get_col_names_fail(Database_UI db_UI, String arg)
    {
        List<String> col_names = db_UI.get_col_names(true, arg);
        test.is(col_names, null, arg + 
                " error get column names test passed.", DATABASE); 
    }

    // For reading and writing files. Reads test files in, then writes them back,
    // then reads them in again to make sure that both ways work.
    // DONE.
    private void test_read_write(String table)
    {
        List<String> column_names = new ArrayList<String>();
        Table test_table = new Table(table, column_names, "Test", null, false, null);
        Table_UI tab_UI = new Table_UI(test_table);
        test_file.read_table_file(test_table);
        tab_UI.print_table();
        test_file.write_tab_to_file(test_table);
        test_file.read_table_file(test_table);
        tab_UI.print_table();
        test.is(test_table.name(), table, table + 
                " reading and writing file test passed.", FILE); 
    }

    // Test for adding fields to a table.
    // DONE
    private void test_add_fields(String new_field_arg)
    {
        db_add_table("James");
        List<String> col_headers = new ArrayList<String>();
        col_headers.add("One");
        col_headers.add("Two");
        col_headers.add("Three");
        Record test_rec = 
        new Record(0, col_headers, true, "James", true, new_field_arg, null);
        List<String> row_entries = test_rec.get();
        for(String row_entry : row_entries){
            test.is(row_entry, new_field_arg, new_field_arg + 
                    " adding fields test passed.", RECORD); 
        }
    }

    // Tests that scanning works as it should.
    // DONE
    private void test_scan(String arg)
    {
        test.is(scan_func.scan_in_line(), arg, arg + 
                " scanning test passed.", SCANNING); 
    }

    // The splitting function here is tested to make sure that given
    // one of the splitters used in the program it does the correct thing.
    // DONE
    private void test_split(String arg_1, String arg_2, String arg_3,
                            String arg_4, String unsplit, String splitter)
    {
        int i;
        List<String> split_input = scan_func.split_input(unsplit, splitter);
        String args[] = {arg_1, arg_2, arg_3, arg_4};
        for(i = 0; i < 4; ++i){
            test.is(args[i], split_input.get(i).trim(), args[i] + 
                    " splitting at " + splitter + "test passed.", 2);
        } 
    }

    // Tests integer parsing.
    // DONE.
    private void test_integer_parse(int exp_result, String to_parse)
    {
        test.is(exp_result, scan_func.parse_integer(to_parse), 
                to_parse + " integer parsing test passed.", SCANNING);
    }

    // Tests to make sure that empty strings are registered as such.
    // DONE.
    private void test_empty_string(String arg, boolean bool_res)
    {
        test.is(bool_res, scan_func.check_if_empty(arg), arg + 
                " empty string test passed.", SCANNING);
    }

    // Creates a table and then attempts to add records to it, checking
    // they have been correctly added.
    // DONE.
    private void check_add_record(String arg)
    {
        int i; 
        db_add_table("James");
        List<String> col_headers = new ArrayList<String>();
        col_headers.add("One");
        col_headers.add("Two");
        col_headers.add("Three");
        Table test_table = new Table("James", col_headers, "James", null, false, null);
        test_table.add_record(true, arg);
        for(i = 0; i < test_table.records().get(0).fields().size(); ++i){
            test.is(test_table.records().get(0).fields().get(i).entry(), arg, 
                    "Table " + arg + " " + i + " test passed.", RECORD);
        }
        ++counter;
    }

    // Alters columns and then checks that they have been altered as needed.
    // DONE.
    private void check_alter_col(String arg_1, String arg_2, String arg_3,
                                 String old_col, String new_col)
    {
        int i;
        String args[] = {arg_1, arg_2, arg_3};
        db_add_table("James");
        List<String> col_headers = new ArrayList<String>();
        col_headers.add("One");
        col_headers.add("Two");
        col_headers.add("Three");
        Table test_table = new Table("James", col_headers, "James", null, false, null);
        test_table.alter_col(true, old_col, new_col);
        for(i = 0; i < args.length; ++i){
            test.is(test_table.col(i), args[i], 
                    "Table " + old_col + " " + new_col + " " + i + 
                    " test passed.", TABLE);
        }
        ++counter;
    }

    // Used to check that getting a record returns the correct record or not
    // record depending on if a valid entry was specified.
    private void check_get_record(String table, int key, int exp_result)
    {
        List<String> column_names = new ArrayList<String>();
        Table test_table = new Table(table, column_names, "Test", null, false, null);
        Table_UI tab_UI = new Table_UI(test_table);
        test_file.read_table_file(test_table);
        int result = tab_UI.get_record(true, key);
        test.is(result, exp_result, "Table " + table + 
                " get record test passed.", TABLE);
    }

    // Check we can delete records as needed.
    private void check_del_record(String table, boolean correct, int del_rec_key)
    {
        int temp_size;

        List<String> column_names = new ArrayList<String>();
        Table test_table = new Table(table, column_names, "Test", null, false, null);
        Table_UI tab_UI = new Table_UI(test_table);
        test_file.read_table_file(test_table);
        temp_size = test_table.records().size();
        test_table.delete_record(true, del_rec_key);
        if(correct == true){
            test.is(test_table.records().size(), temp_size - 1, "Table " 
                    + table + " delete record test passed.", TABLE);
        }
        else{
            test.is(test_table.records().size(), temp_size, "Table " 
                    + table + " delete record test passed.", TABLE);
        }
    }

    // Checks table details are printed as expected.
    private void check_print_tab_details(String table)
    {
        List<String> column_names = new ArrayList<String>();
        Table test_table = new Table(table, column_names, "Test", null, false, null);
        Table_UI tab_UI = new Table_UI(test_table);

        test_file.read_table_file(test_table);
        tab_UI.print_table_details();
    }

    private void check_col_types(String type_one, String type_two, 
                                 String type_three, String col_one,
                                 String col_two, String col_three)
    {
        List<String> column_types = new ArrayList<String>();
        List<String> column_names = new ArrayList<String>();
        column_names.add(col_one);
        column_names.add(col_two);
        column_names.add(col_three);
        column_types.add(type_one);
        column_types.add(type_two);
        column_types.add(type_three);
        Table test_tab = db_UI.create_table(true, "ColTest", column_types, column_names);
        for(int i = 0; i < column_names.size(); ++i){
            test.is(test_tab.cols_types().get(column_names.get(i)), column_types.get(i), 
                    "Col types " + type_one + type_two + type_three + " test passed.", TABLE);
        }
    }

    private void check_valid_invalid_cols(String type_one, String type_two, 
                                          String type_three, String col_one,
                                          String col_two, String col_three,
                                          String arg, int expected_size)
    {
        List<String> column_types = new ArrayList<String>();
        List<String> column_names = new ArrayList<String>();
        column_names.add(col_one);
        column_names.add(col_two);
        column_names.add(col_three);
        column_types.add(type_one);
        column_types.add(type_two);
        column_types.add(type_three);
        Table test_tab = new Table("James", column_names, "Dad", null, false, column_types);
        test_tab.add_record(true, arg);
        test.is(test_tab.num_rows(), expected_size, 
                "Col types " + col_one + " test passed.", TABLE);
    }

    private void check_invalid_cols_unread(String table)
    {
        List<String> column_names = new ArrayList<String>();
        Table test_table = new Table(table, column_names, "Test", null, false, null);
        Table_UI tab_UI = new Table_UI(test_table);
        test_file.read_table_file(test_table);
        tab_UI.print_table();
        test.is(test_table.num_rows(), 0, table + " col error .", FILE); 
    }

    private void find_table_name(String text, String table_name)
    {
        test_database.add_table(true, table_name);
        String result = menu.find_text_in_db_names(text);
        test.is(result.toLowerCase().contains(table_name.toLowerCase()), true,
                text + " " + table_name + " find test passed.", FILE);
    }

    private void find_database_name(String text, String database_name)
    {
        String result = menu.find_text_in_db_names(text);
        System.out.printf("\n%s\n", result);
        // test.is(result.toLowerCase().contains(database_name.toLowerCase()), true,
        //         text + " " + database_name + " find test passed.", 3);
    }

    private void test_for_keys(String table_one, String table_two, String db_one,
                               String arg)
    {
        Menu menu = new Menu();
        Database test_db = new Database(db_one);
        List<String> column_names = new ArrayList<String>();
        Table test_table_one = new Table(table_one, column_names, db_one, null, false, null);
        test_db.test_add_table(test_table_one);
        Table test_table_two = new Table(table_two, column_names, db_one, null, false, null);
        test_db.test_add_table(test_table_two);
        menu.test_add_db(test_db);
        Table_UI tab_UI_one = new Table_UI(test_table_one);
        Table_UI tab_UI_two = new Table_UI(test_table_two);
        test_file.read_table_file(test_table_one);
        tab_UI_one.print_table();
        test_file.read_table_file(test_table_two);
        tab_UI_two.print_table();
        test_table_one.add_record(true, arg);
        System.out.printf("\n\n%s\n\n", menu.find_text_in_tab_names("f"));
        for(int i = 0; i < test_table_one.records().get(0).fields().size(); ++i){
            test.is(test_table_one.records().get(0).fields().get(i).entry(), arg, 
                    "Table " + arg + " " + i + " for key test passed.", RECORD);
        }
        ++counter;
        tab_UI_one.print_table();
        test_file.read_table_file(test_table_one);
        test_file.write_tab_to_file(test_table_one);
        test_file.read_table_file(test_table_one);
        test.is(test_table_one.name(), table_one, table_one + 
                " reading and writing file test passed.", FILE); 
    }

    private void test_for_keys_error(String table_one, String table_two, String db_one,
                                     String arg)
    {
        Menu menu = new Menu();
        Database test_db = new Database(db_one);
        List<String> column_names = new ArrayList<String>();
        Table test_table_one = new Table(table_one, column_names, db_one, null, false, null);
        test_db.test_add_table(test_table_one);
        Table test_table_two = new Table(table_two, column_names, db_one, null, false, null);
        test_db.test_add_table(test_table_two);
        menu.test_add_db(test_db);
        Table_UI tab_UI_one = new Table_UI(test_table_one);
        Table_UI tab_UI_two = new Table_UI(test_table_two);
        test_file.read_table_file(test_table_one);
        tab_UI_one.print_table();
        test_file.read_table_file(test_table_two);
        tab_UI_two.print_table();
        test_table_one.add_record(true, arg);
        test.is(test_table_one.num_rows(), 0, arg + " error for_key test passed.", FILE);
        tab_UI_one.print_table();
        test_file.read_table_file(test_table_one);
        test_file.write_tab_to_file(test_table_one);
        test_file.read_table_file(test_table_one);
        test.is(test_table_one.name(), table_one, table_one + 
                " for key error reading and writing file test passed.", FILE); 
    }

}