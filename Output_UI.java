import java.util.*;
import java.lang.*;
import java.io.File;

// This class determines how the messages sent to the outside world by the 
// program looks. All printing to the console is done through here. What this
// means is that other classes tell this class what they want to communicate
// (eg: "Help" as an error) and then this one class can control how all of these
// sentiments visually manifest themselves.

// Responsibilites: Deciding how output looks to the outside world.

public class Output_UI {

	// This hashmap stores the greatest size of a string for each table. Then
	// the table can be printed to the appropriate size.
	private static Map<String, Integer> max_str_len = new HashMap<String, Integer>();

	// If not already initialised, then this will set the size of the table
	// to ten as this makes a sensible looking table.
	public void init_max_str_len(String table_name)
	{
		if( max_str_len.get(table_name) == null ){
			max_str_len.put(table_name, 10);
		}
	}

	// Chooses how regular output appears.
	public void output(String output)
	{
		System.out.printf("%s", output);
	}

	// Chooses how errors appear.
	public void error(String error_mess)
	{
		System.err.printf("\n\n" + error_mess + "\n\n");
        System.exit(1);
	}

	// This prints columns of a table depending on how large the maximum
	// size of that column is.
	public void col_value(String value, String tab_name)
	{
        List<String> lines = new ArrayList<String>();
        ScanFunctions scan_func = new ScanFunctions();

        lines = scan_func.split_input(value, "\r\n|\r|\n");

        for(int i = 0; i < lines.size(); ++i){

            String temp_string = 
            fixedLengthString(lines.get(i), max_str_len.get(tab_name) + 1);

			System.out.printf("%s", temp_string );

			if( i != lines.size() - 1){ ws(1); }
        }
	}

	// Used for printing the record key dependent on the maximum size of that
	// table.
	public void record_key(int rec_key, String tab_name)
	{
		String temp_string = 
		fixedLengthString("" + rec_key, max_str_len.get(tab_name)  + 1);

		System.out.printf("%s", temp_string );
	}

	// Given an integer will print a number of whitespace lines.
	public void ws(int num_lines)
	{
		for(int i = 0; i < num_lines; ++i){ System.out.printf("\n"); }
	}

	// Used to create a fixed length string of a given size so that all
	// elements of the table are equally spaced when printed.
	private String fixedLengthString(String string, int length) {
        return (String.format("%1$" + length + "s", string));
    }

    // Used to give the maximum string length for a certain table to different
    // classes to compare to and see if it needs updating.
    public int max_string_length(String tab_name)
    {
    	return(max_str_len.get(tab_name));
    }

    // This replaces the maximum known size of a string in a given table.
    public void max_str_len_rep(int new_length, String tab_name)
    {
    	max_str_len.put(tab_name, new_length);
    }

    // Used to test the maximum length of a string in a table for better
    // printing.
    public void check_max_str_len(String value, String parent_table)
    {
    	ScanFunctions scan_func = new ScanFunctions();
        List<String> lines = new ArrayList<String>();
        lines = scan_func.split_input(value, "\r\n|\r|\n");

        for(String ind_line: lines){
            if(ind_line.length() > max_string_length(parent_table)){
                max_str_len_rep(ind_line.length(), parent_table);
            }
        }
    }

    // Converts a list of values into a single string.
    public String convert_to_sing_string(List<String> databases)
    {
    	String fin_string = new String();
    	for(String db_name : databases){fin_string = fin_string + "\n" + db_name;}
    	return(fin_string);
    }

    // Converts a table into a single string for printing in the GUI.
    public String convert_tabs_to_string(List<Table> tables)
    {
    	String fin_string = "\nExample of Found Table " +
    						"(if more than one found, only first printed):\n";
    	if(tables.size() > 0){
    		fin_string = fin_string + "\nName: " + tables.get(0).name() + "\n\n";
    		fin_string = fin_string + convert_cols_to_string(tables.get(0)) + "\n";
    		fin_string = fin_string + convert_recs_to_string(tables.get(0));
    	}
    	return(fin_string);
    }

    // Used in above to convers the column headers to strings.
    public String convert_cols_to_string(Table tab)
    {
    	String columns = "";
    	for(String col_name: tab.column_names()){
    		columns = columns + 
                      fixedLengthString(col_name, max_str_len.get(tab.name()) + 1);
    	}
    	return(columns);
    }

    // Used in the above to convert all records to strings.
    public String convert_recs_to_string(Table tab)
    {
        ScanFunctions scan_func = new ScanFunctions();
    	String records = "";
    	for(Record rec : tab.records()){
    		for( String field: rec.get() ){
                records = space_multi_lines(tab, field, records);
    		}
    		records = records + "\n";
    	}
    	return(records);
    }

    // This function is so that when tables are converted to a single string they
    // are formatted with the correct width.
    private String space_multi_lines(Table tab, String field, String records)
    {
        ScanFunctions scan_func = new ScanFunctions();
        List<String> lines = new ArrayList<String>();
        lines = scan_func.split_input(field, "\r\n|\r|\n");

        for(int i = 0; i < lines.size(); ++i){

            records = 
            records + 
            fixedLengthString(lines.get(i), max_str_len.get(tab.name()) + 1);

            if( i != lines.size() - 1 && lines.size() > 1 ){
                for(int j = 0; j < tab.size() - 1; ++j){

                    records = 
                    records + fixedLengthString("", max_str_len.get(tab.name()) + 1);

                }
            records = records + "\n";
            }
        }

        return(records);

    }

}