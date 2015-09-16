import java.util.*;
import java.io.File;

// This class holds all of the information for the database (i.e. the list
// of tables that it holds) and is in charge of everything that happens to it.
// It passes messages to the tables in the list telling them to do things like
// add or remove records etc. It also has a corresponding Database_UI class
// which carries out all communication with the outside world.

// Responsibilites: Holding the data for a database (i.e. the list of tables).
//                  Making the tables inside itself do things.
//                  Letting other classes access the tables.

public class Database {

    // All of the neccessary fields for a database.
	private String database_name;
	private Map<String, Table> tabs_in_db = new HashMap<String, Table>();

    private Files files = new Files();
    // private Database_UI db_UI = new Database_UI(this);

	Database(String db_name)
	{
		database_name = db_name;
        files.create_database_folder(db_name);
	}

    // This adds a new table to the database after having it passed back
    // by the UI.
    // TESTED IN TESTING SUITE.
	public void add_table(boolean testing, String tab_name)
	{
        Table new_tab;
		Database_UI db_UI = new Database_UI(this);

        if((new_tab = db_UI.create_table(testing, tab_name, null, null)) != null){
            tabs_in_db.put( new_tab.name(), new_tab );
        }

	}

    // This selects a table from the database according to instructions from 
    // the UI and then looks for commands relating to it until we leave the 
    // table.
    // TESTED IN TESTING SUITE.
    public void select_table(boolean testing, String tab_name)
    {
        Database_UI db_UI = new Database_UI(this);
        Table selected_table;
        Action select_table_cmmd;

        selected_table = db_UI.select_table(testing, tab_name);

        if(selected_table != null && !testing){
            do{
                select_table_cmmd = db_UI.table_actions(selected_table.name());
                interpret_select_cmmd(selected_table, select_table_cmmd, db_UI, 
                                      false);
            }while( select_table_cmmd != Action.LEAVE_TABLE );
        }
    }

    // This interprets commands given to it by the user and then passes them
    // onto either the selected table for action or onto the Files class which
    // controls reading and writing to files.
    // ONLY PUBLIC FOR TESTING.
    // TESTED IN TESTING SUITE.
    public Action interpret_select_cmmd(Table sel_tab, Action select_cmmd,
                                       Database_UI db_UI, boolean testing)
    {
        Table_UI table_UI = new Table_UI(sel_tab);
        Files files = new Files();

        switch (select_cmmd) {

            case ADD_RECORD:

                if(!testing){ sel_tab.add_record(false, null); }
                else{ return(Action.ADD_RECORD); }
                break;

            case PRINT_TABLE:

                if(!testing){ table_UI.print_table(); }
                else{ return(Action.PRINT_TABLE); }
                break;

            case ALTER_COLS:

                if(!testing){ sel_tab.alter_col(false, null, null); }
                else{ return( Action.ALTER_COLS); }
                break;

            case GET_COLS:

                if(!testing){ db_UI.print_table_cols(sel_tab); }
                else{ return(Action.GET_COLS); }
                break;

            case GET_NUM_COLS:

                if(!testing){ db_UI.print_num_tab_cols(sel_tab); }
                else{ return(Action.GET_NUM_COLS); }
                break;

            case GET_NUM_ROWS:

                if(!testing){ db_UI.print_num_tab_rows(sel_tab); }
                else{ return(Action.GET_NUM_ROWS); }
                break;

            case GET_RECORD:

                if(!testing){ table_UI.get_record(false, 0); }
                else{ return(Action.GET_RECORD); }
                break;

            case DELETE_RECORD:

                if(!testing){ sel_tab.delete_record(false, 0); }
                else{ return(Action.DELETE_RECORD); }
                break;

            case WRITE_FILE:

                if(!testing){ files.write_tab_to_file(sel_tab); }
                else{ return(Action.WRITE_FILE); }
                break;

            case READ_FILE:

                if(!testing){ files.read_table_file(sel_tab); }
                else{ return(Action.READ_FILE); }
                break;
                
            default:
                break;
        }

        return(Action.NO_COMMAND);
    }

    // Used for listing all of the tables and their information.
    public void list_tables()
    {
        for(Table tab : tabs_in_db.values() ){
            Table_UI tab_UI = new Table_UI(tab);
            tab_UI.print_table_details();
        }
    }

    // Getters for the different information within the classes.
	public String name()
	{
		return(database_name);
	}

    public Map<String, Table> tabs_in_db() 
    {
        return(tabs_in_db);
    }

    // Note: These are only used in testing.
    public Table table(String tab_name)
    {
        return(tabs_in_db.get(tab_name));
    }

    public void test_add_table(Table to_add)
    {
        tabs_in_db.put(to_add.name(), to_add);
    }
}