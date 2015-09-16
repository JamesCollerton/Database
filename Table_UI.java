import java.util.*;
import java.io.*;
import java.nio.*;

// This class holds all of the information for a table, its filename, its
// parents database, the information to generate row keys, its column names
// and the records that create the table.

// Responsibilites: Storing the above information.
// 					Giving access to it and altering it.

public class Table_UI {

	private Table table;
	private int ERROR = -1;

	private ScanFunctions scan_func = new ScanFunctions();
	private Output_UI out = new Output_UI();

	// Each table UI needs a copy of the table it is representing in order 
	// to print details of it and make comparisons. Actually altering the 
	// information of the table is done within the table class itself.
	Table_UI(Table tab)
	{
		table = tab;
	}

	// Prints an overview of the table, its names and its columns.
	public void print_table_details()
	{
        print_table_name();
        print_table_cols();
        out.ws(2);
	}

	public void print_table_name()
	{
		out.output("\nTable Name: " + table.name() + "\n" );
	}

	// Cycles through the columns of the table and prints the column name.
	// Key is represented by a column and so is also printed.
	public void print_table_cols()
	{
		int i;

		out.output("\nTable Columns: ");
		for(i = 0; i < table.size(); ++i){ out.output(table.col(i) + " (" + 
										   table.col_type(table.col(i)) + ") ");}
        out.output("Key");
	}

	// Used to actually print the table to the screen. Gives its name and then
	// prints the headings of the table. Cycles through all of its records and
	// for each record prints out the values within it. 
	public void print_table()
	{
		int i, rec_key;
		List<Field> column_fields;
		Record_UI rec_UI;

		out.output("\n\nTable " + table.name() + ":\n\n");
		print_table_headers();

		for( Record rec: table.records() ){
			column_fields = new ArrayList<Field>(rec.fields());
			rec_key = rec.key();
			rec_UI = new Record_UI(table.name(), null);
			out.ws(1);
			rec_UI.print_record(column_fields, rec_key);
		}

		out.ws(2);
	}

	// Cycles through the column names and prints all of the headers for cols.
	// This is different to previous as this prints them spaced for a table.
	public void print_table_headers()
	{
		for(int i = 0; i < table.size(); ++i){
			out.col_value( table.col(i), table.name() );
		}

		out.col_value("Key", table.name() );
	}

	public void invalid_arguments()
	{
		out.output("\nSorry, invalid arguments.\n");
	}

	// Takes input from the user and translates that into two columns to alter.
	public List<String> find_col_to_alter()
    {
        String user_input;
        List<String> col_alter_new = new ArrayList<String>();

        out.output("\n\nEnter name of column to change "+
                   "followed by a space and the ammendment\n\n> ");

        user_input = scan_func.scan_in_line();
        col_alter_new = scan_func.split_input(user_input, " ");

        return(col_alter_new);
    }

    // Finds the index of the original name of the column to be swapped and
    // returns it to the Table class to do the swapping.
    public int find_orig_name(String orig_name)
    {
        for(int i = 0; i < table.size(); ++i){
            if(orig_name.equals( table.col(i) ) ){ return(i); }
        }

        out.output("\nSorry, not a valid column name.\n");
        return(ERROR);
    }

    // This passes back to the table and tries to swap the two columns specified
    // by the user.
	public void alt_col_name(int col_ind, String new_name)
	{
		try{ table.change_column(col_ind, new_name); }
		catch(Exception e){ out.error("Column name not changed."); }
	}
	
	// This gets the record number interested in from the user and then
	// looks for it in the table. If not found prints message, otherwise
	// prints record.
	// (ONLY RETURNS INT FOR TESTING.)
	public int get_record(boolean testing, int test_key)
    {
        String integer_input;
        int key, found_key;

        print_table();

        out.output("\nPlease enter the key of the record" +
                   " you are interested in selecting.\n\n> ");

        if(!testing){ 
        	integer_input = scan_func.scan_in_line(); 
        	key = scan_func.parse_integer(integer_input);
        }
        else{ key = test_key;}

        found_key = find_key(key);

        if(found_key != ERROR){ print_rec_from_key(found_key); }

        return(found_key);
    }

	// Passes to the Record UI in order to print the information and then
	// adds whitespace etc. so it is formatted nicely.
	private void print_rec_from_key(int rec)
	{
		List<Field> column_fields = 
			new ArrayList<Field>(table.records().get(rec).fields());

		Record_UI rec_UI = new Record_UI( table.name(), null );

		out.ws(1);
		print_table_headers();
		out.ws(1);
		rec_UI.print_record(column_fields, rec);
		out.ws(2);
	}

	// Gets a key from the user and then searches for it to delete.
	public int get_del_rec_key()
	{
		String rec_key_str;
		int rec_key_int;

		out.output("\n\nPlease enter the key of the record you" +
				   " would like to delete.\n\n> ");

		rec_key_str = scan_func.scan_in_line();
		rec_key_int = scan_func.parse_integer(rec_key_str);

		return(rec_key_int);
	}

	// Used for finding a key within the table.
	public int find_key(int rec_key)
	{
		for(Record rec : table.records() ){
			if(rec.key() == rec_key){ return( rec.key() ); }
		}

		out.output( "\nCould not find record.\n" );
		return(ERROR);
	}

}