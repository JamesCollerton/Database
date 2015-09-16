import java.util.*;
import java.io.*;
import java.nio.*;

// This class holds all of the information for a table, its filename, its
// parents database, the information to generate row keys, its column names
// and the records that create the table.

// Responsibilites: Storing the above information.
// 					Giving access to it and altering it.

public class Table {

	private String filename, table_name, par_database;
	private int curr_row_key, OLD_AND_NEW = 2, ERROR = -1, ORIG = 0, NEW = 1;
	private List<String> column_names;
	private List<Record> records = new ArrayList<Record>();
	private Map<String, String> cols_types = new HashMap<String, String>();

	private Table_UI table_UI = new Table_UI(this);

	// Each table needs a name, the name of its columns, and the database
	// it came from in order to make its filename.
	Table(String name, List<String> col_names, String par_database_arg,
		  String given_filename, boolean own_filename, List<String> col_ts)
	{
		table_name = name;
		curr_row_key = 0;
		column_names = new ArrayList<String>(col_names);
		par_database = par_database_arg;

		if(!own_filename){ filename = create_filename(); }
		else{ filename = given_filename; }
		
		if(col_ts != null){ init_col_types(column_names, col_ts); }
	}

	// Adds a record to the table.
	public void add_record(boolean testing, String test_value)
    {
    	Record new_rec;

    	if(!testing){
    		new_rec = 
    		new Record(curr_row_key++, column_names, true, 
    				   table_name, false, null, cols_types);
    	}
    	else{
    		new_rec = 
    		new Record(curr_row_key++, column_names, true, 
    				   table_name, true, test_value, cols_types);
    	}

    	add_to_record_list(new_rec, testing);
    }

    // Adds the record to the list if it has been successful.
    public void add_to_record_list(Record new_rec, boolean testing)
    {
		if(new_rec.create_success()){
			records.add(new_rec);
    		table_UI.print_table();
    	}
    }

    // Alters column names in the table. Asks for user input and then changes
    // the column name dependent.
	public void alter_col(boolean testing, String old_col, String new_col)
    {
        List<String> col_alter_new = new ArrayList<String>();
        String name_of_col_change, new_name;
        int original_col_ind;

        table_UI.print_table_cols();
        
        if(!testing){ col_alter_new = table_UI.find_col_to_alter(); }
        else{ col_alter_new.add(old_col); col_alter_new.add(new_col); }

        if(col_alter_new.size() == OLD_AND_NEW){

            original_col_ind = table_UI.find_orig_name(col_alter_new.get(ORIG));

            if(original_col_ind != ERROR){
                table_UI.alt_col_name( original_col_ind, col_alter_new.get(NEW));
                table_UI.print_table();
            }
        }
        
        else{ table_UI.invalid_arguments();}
    }

    // Deletes a record depending on what the user chooses to delete.
    // TESTED IN TESTING FUNCTIONS.
	public void delete_record(boolean testing, int test_rec_key)
	{
		int rec_key, record_ind;

		if(!testing){ rec_key = table_UI.get_del_rec_key(); }
		else{ rec_key = test_rec_key; }

		if(rec_key != ERROR){ remove_record(rec_key); table_UI.print_table(); }
		
	}

	// Initialises the column types according to them given by the user.
	private void init_col_types(List<String> col_names, List<String> column_types)
	{
		for(int i = 0; i < col_names.size(); ++i){
			cols_types.put(col_names.get(i), column_types.get(i));
		}
	}

	// Replace the existing types of the table with those written by the file.
	public void replace_existing_types(List<String> col_types)
	{
		cols_types = new HashMap<String, String>();

		for(int i = 0; i < column_names.size(); ++i){
			cols_types.put(column_names.get(i), col_types.get(i));
		}
	}

	// Removes a record according to its key.
	public void remove_record(int rec_key)
	{
		for(int i = 0; i < records.size(); ++i){
			if(rec_key == records.get(i).key()){records.remove(i);}
		}
	}

	// Changes the column name.
	public void change_column(int col_ind, String new_name)
	{
		boolean exists;
		exists = check_col_exists(new_name);
		if(!exists){ 
			String type = cols_types.remove(column_names.get(col_ind));
			cols_types.put(new_name, type);
			column_names.set(col_ind, new_name);
		}
	}

	// Checks a column exists when changing the column name.
	private boolean check_col_exists(String col_name)
	{
		for(String exist_col_name : column_names){
			if(col_name.equals(exist_col_name)){ return(true); }
		}
		return(false);
	}

	public List<Integer> record_keys()
	{
		List<Integer> rec_keys = new ArrayList<Integer>();
		for(Record rec : records){rec_keys.add(rec.key());}
		return(rec_keys);
	}

	public void replace_records(List<Record> new_records)
	{
		records = new ArrayList<Record>(new_records);
	}

	public int row_key()
	{
		return(curr_row_key++);
	}

	public String filename()
	{
		return(filename);
	}

	// ONLY USED IN TESTING.
	public int get_key()
	{
		return(curr_row_key);
	}

	public void reset_row_key()
	{
		curr_row_key = 0;
	}

	public Map<String, String> cols_types()
	{
		return(cols_types);
	}

	public void replace_existing_headers(List<String> split_input)
	{
		column_names = new ArrayList<String>(split_input);
	}

	public String create_filename()
	{
		return(par_database + "/" + table_name + ".txt");
	}

	public String name()
	{
		return(table_name);
	}

	public List<Record> records()
	{
		return(records);
	}

	public String col(int i)
	{
		return(column_names.get(i));
	}

	public String col_type(String col_name)
	{
		return(cols_types.get(col_name));
	}

	public List<String> column_names()
	{
		return(column_names);
	}

	public int size()
	{
		return(column_names.size());
	}

	public int num_rows()
	{
		return( records.size() );
	}

	public String par_database()
	{
		return(par_database);
	}

}