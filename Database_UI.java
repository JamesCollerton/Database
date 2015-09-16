import java.util.*;
import java.io.File;

// This class communicates with the outside world on behalf of the
// Database class. It takes commands from the database class and gets
// information from the user, feeding the results back in.

// Responsibilities: Interacting with the outside world for the database class.
//                   Creating new tables to be added to the database.
//                   Choosing tables for the database class to make do things
//                   corresponding to user input.

public class Database_UI {

    // All of the necessary fields for the database class.
	private String database_name;
    private String poss_types[] = {"string", "int", "float", "foreign"};
	private Map<String, Table> tabs_in_db = new HashMap<String, Table>();

    // All of the necessary classes so the UI can scan in input from the user,
    // parse commands and send messages in and out.
    private ScanFunctions scan_func = new ScanFunctions();
    private Parser parser = new Parser();
    private Output_UI out = new Output_UI();
    private Files files = new Files();
    
    // Need to know the existing tables in the database to check for replicas
    // and the name of the database for creating the table.
    Database_UI(Database database)
	{
		tabs_in_db = new HashMap<String, Table>(database.tabs_in_db());
		database_name = database.name();
	}

    // This is the create table sequence. It takes in the commands from the
    // user and then creates a new table, feeding it back to be added onto
    // the list of tables in the database. It checks if the name is taken or
    // empty before doing anything first, then the column names, then
    // returns the table.
	public Table create_table(boolean testing, String test_tab_name, 
                              List<String> test_column_types,
                              List<String> test_col_names)
	{
		String new_tab_name = "";
        boolean taken, empty, from_file = false;
        List<String> col_names = new ArrayList<String>();
        Table new_table;

        if(!testing){ from_file = ask_from_file(); new_tab_name = get_table_name(); }
        else{ new_tab_name = test_tab_name; }

        taken = check_if_taken(new_tab_name);
        empty = scan_func.check_if_empty(new_tab_name);

        if(!taken && !empty && !from_file){

            if(!testing){ col_names = get_col_names(false, null); }
            else if(test_col_names != null){
                col_names = new ArrayList<String>(test_col_names);}

            if(col_names != null){
                new_table = 
                make_table_seq(new_tab_name, database_name, col_names,
                               test_column_types, testing);
                return(new_table);
            }

        }
        else if(!testing && !taken && !empty){
            new_table = 
            read_table_seq(new_tab_name, database_name, col_names);
            return(new_table);
        }

        return(null);
	}

    // Gets the table name from the user.
	private String get_table_name()
    {
    	String tab_name;
    	out.output("\nPlease enter your table name:\n\n> ");
        tab_name = scan_func.scan_in_line();
    	return(tab_name);
    }

    // Function used to check if the table name is taken already.
    private boolean check_if_taken(String new_tab_name)
    {
        if( tabs_in_db.get(new_tab_name) != null ){
            out.output("\nTable name taken!\n\n");
            return(true);
        }
        return(false);
    }

    // Used to ask the person if they would like to read from a file or not.
    private boolean ask_from_file()
    {
        String response;
        do{
            out.output("\nRead the table from a file [Y/N]?\n\n> ");
            response = scan_func.scan_in_line();
        }while(!response.toLowerCase().equals("y") &&
               !response.toLowerCase().equals("n"));

        if(response.toLowerCase().equals("y")){return true;}
        else{return false;}
    }

    // Given someone is interested in reading from a file, finds the filename.
    private String find_filename()
    {
        String filename;
        out.output("\nEnter the filename you would like to read" +
                   " and write to please\n\n> ");
        filename = scan_func.scan_in_line();
        return(filename);
    }

    // When we want to make a table from scratch we go through this sequence.
    private Table make_table_seq(String new_tab_name, String database_name,
                                 List<String> col_names, List<String> test_col_types,
                                 boolean testing)
    {
        List<String> col_types = null;

        if(!testing){ col_types = get_col_types(col_names); }
        else if(testing && test_col_types != null){ 
            col_types = new ArrayList<String>(test_col_types);
        }

        find_max_col_name(col_names, new_tab_name);

        Table new_table = new Table(new_tab_name, col_names, database_name, 
                                    null, false, col_types);

        Table_UI table_UI = new Table_UI(new_table);
        table_UI.print_table_details();
        return(new_table);
    }

    // When we want to read a table from a file we go through this sequence.
    private Table read_table_seq(String new_tab_name, String database_name,
                                 List<String> col_names)
    {
        String filename;

        filename = find_filename();
        Table new_table = new Table(new_tab_name, col_names, database_name, 
                                    filename, true, null);
        files.read_table_file(new_table);
        Table_UI table_UI = new Table_UI(new_table);
        table_UI.print_table_details();
        return(new_table);
    }

    // Gets column names from the user and then puts them into the table.
    // (ONLY PUBLIC FOR TESTING).
    public List<String> get_col_names(boolean testing, String unsp_col_names)
    {
        String unsplit_col_names;
        boolean distinct, empty;
        List<String> col_names = new ArrayList<String>();

        out.output("\nPlease enter your column names" + 
                   " seperated by spaces:\n\n> ");

        if(!testing){ unsplit_col_names = scan_func.scan_in_line(); }
        else{ unsplit_col_names = unsp_col_names; }

        empty = scan_func.check_if_empty(unsplit_col_names);
        col_names = scan_func.split_input(unsplit_col_names, " ");
        distinct = check_distinct_cols(col_names);

        if(!distinct || empty){ return(null); }
        return(col_names);    
    }

    // Used to make sure that two column names are not the same.
    // This can't use object structured loop as it needs to check
    // that the indices of the column name are not the same.

    // Note: I would much rather have used a Hashmap for the column
    // names, but it would be very hard to have gone back and changed everything
    // now. This is covered in the report.
    private boolean check_distinct_cols(List<String> col_names)
    {
        for(int i = 0; i < col_names.size(); ++i){
            for(int j = 0; j < col_names.size(); ++j){

                if(col_names.get(i).equals(col_names.get(j)) && i != j){

                    if(col_names.get(i).equals("") || 
                       col_names.get(i).equals(" ")){
                            out.output("\nPlease use only single spaces.\n\n");
                    }
                    else{ out.output("\nReplicate column name included.\n\n"); }
                    
                    return(false);
                }
            }
        }

        return(true);
    }

    // This finds the maximum column name so that the table can be printed
    // to that length.
    private void find_max_col_name(List<String> col_names, String tab_name)
    {
        out.init_max_str_len(tab_name);

        for(String column_name: col_names){
            if( column_name.length() > out.max_string_length(tab_name) ){
                out.max_str_len_rep(column_name.length(), tab_name);
            }
        }
    }

    // This function gets the types of the columns to be fed into the Fields.
    private List<String> get_col_types(List<String> col_names)
    {
        String type;
        List<String> col_types = new ArrayList<String>();

        for(String column_name: col_names){
            col_types.add(get_type_for_col( column_name ));
        }

        out.ws(1);
        return(col_types);
    }

    // Controls the actual scanning of the type from the user.
    private String get_type_for_col(String col_name)
    {
        String col_type;
        boolean valid_type = false;

        do{

            out.output("\nPlease enter type (string/ int/ float/ foreign)" +
                       " for column " + col_name + ": ");

            col_type = scan_func.scan_in_line();
            valid_type = check_val_type(col_type);

        }while(valid_type != true);

        if(col_type.equals("foreign")){col_type = find_ref_table();}

        return(col_type);
    }

    // Checks that whatever has been scanned in the previous function matches
    // one of the predefined valid types.
    private boolean check_val_type(String col_type)
    {
        for(int i = 0; i < poss_types.length; ++i){
            if(poss_types[i].equals(col_type)){ return(true);}
        }

        return(false);
    }

    // Used when defining a foreign key to make sure that the foreign key
    // references a valid table.
    private String find_ref_table()
    {
        boolean valid_ref = false, valid_type = false;
        String ref_table_type;

        do{
            out.output("\nPlease enter the name of the reference table" + 
                       " or an alternate type (string/ float/ int):\n\n> ");
            ref_table_type = scan_func.scan_in_line();
            valid_ref = check_valid_ref(ref_table_type);
            valid_type = check_val_type(ref_table_type);
        }while(!valid_ref && !valid_type);

        if(valid_ref){return("foreign <foreign> " + ref_table_type + 
                             " <foreign> " + database_name);}
        return(ref_table_type);
    }

    private boolean check_valid_ref(String ref_table_type)
    {
        for(String table : tabs_in_db.keySet()){
            if(table.equals(ref_table_type)){ return(true); }
        }
        return(false);
    }

    // ////////////////////////////////////////////////////////////////////////

    // This is used to select a table from the existing ones to have actions
    // taken on it. The result is then fed back into the database class to have
    // actions taken on it.
    public Table select_table(boolean testing, String table_name)
    {
        String tab_name;
        Table selected_table; 

        print_table_sel_info();

        if(!testing){ tab_name = scan_func.scan_in_line(); }
        else{ tab_name = table_name; }

        selected_table = lookup_table(tab_name);

        return(selected_table);
    }

    // Run whenever the user has got to a stage where they have selected a 
    // table and need to input their command. The result is then passed back
    // to the database class to have actions taken on it.
    public Action table_actions(String tab_name)
    {
        Action select_table_cmmd;
        out.output("\nCurrently in table " + tab_name + "\n\n");
        select_table_cmmd = parser.RunParser();
        return(select_table_cmmd);
    }

    // Prints tables to choose from followed by greeting message.
    private void print_table_sel_info()
    {
        print_tables();
        out.output("\nPlease enter the name of the " +
                   "table you would like to select. \n\n> ");
    }

    // Looks up tables by their name in the hashmap and then returns their
    // name if they appear.
    private Table lookup_table(String tab_name)
    {
        Table tab;

        tab = tabs_in_db.get(tab_name);

        if( tab == null){
            out.output("\nError, table " + tab_name + " does not exist.\n\n");
        }
        else{ out.output("\nSelected table " + tab_name + "\n\n");}        

        return(tab);
    }

    // ////////////////////////////////////////////////////////////////////////

    // Prints all of the columns for a table
    public void print_table_cols(Table sel_tab)
    {
        Table_UI table_UI = new Table_UI(sel_tab);
        table_UI.print_table_cols();
        out.ws(2);
    }

    public void print_num_tab_cols(Table sel_tab)
    {
        out.output("\nNumber of columns: " + sel_tab.size() + "\n\n");
    }

    public void print_num_tab_rows(Table sel_tab)
    {
        out.output("\nNumber of rows: " + sel_tab.num_rows() + "\n\n");
    }

    // Loops through all keys in the hashmap containing the tables and
    // prints their corresponding tables.
    public void print_tables()
    {
        out.output("\nTables in database: \n");

        for( String key: tabs_in_db.keySet() ){
            Table_UI table_UI = new Table_UI( tabs_in_db.get(key) );
            out.ws(1);
            table_UI.print_table_details();
        }
    }

    // ////////////////////////////////////////////////////////////////////////

}