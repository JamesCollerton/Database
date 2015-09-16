import java.util.*;
import javax.swing.*;
import java.util.concurrent.*;

// Class that runs the menu before selecting databases or databases.
// Stores the list of databases and controls their behaviour and acts
// as an access point to the program for the GUI.

// Responsibilities: Running the main menu.
//                   Controlling the list of databases.
//                   Interacting with the GUI.

public class Menu {

    // The necessary fields for use in the menu. cmmd_ind is to see which
    // command has been parsed, databases holds the list of databases.
	private Action cmmd_ind;
    private static Map<String, Database> databases = new HashMap<String, Database>();

    // The classes the menu needs access to in order to parse functions and
    // output to the console.
    private Parser parser = new Parser();
    private Output_UI out = new Output_UI();
    private Menu_UI menu_UI;

    // Runs the main menu while we have not chosen to quit the database.
	public void RunMenu() 
    {
        menu_UI = new Menu_UI(databases);
    	menu_UI.print_welcome();

    	do{

            out.output("\nCurrently in main menu.\n\n");
        	init_cmmd_ind();
        	cmmd_ind = parser.RunParser();
        	interpret_ind(false);

    	}while( cmmd_ind != Action.EXIT_DB );

        // So we close the GUI if open.
        System.exit(0);
    }

    // Used to reset the parsed command every time so we can scan something
    // new.
    private void init_cmmd_ind()
    {
    	cmmd_ind = Action.NO_COMMAND;
    }

    // Interprets the command index and chooses what to do next based on that.
    // (ONLY PUBLIC AND BOOLEAN (WAS VOID) FOR TESTING.)
    public Action interpret_ind(boolean testing)
    {
        if(cmmd_ind != null){
            switch(cmmd_ind) {

                case CREATE_DATABASE:

                    if(!testing){ create_database_seq(false, null); }
                    else{ return(Action.CREATE_DATABASE); }
                    break;

                case SELECT_DATABASE:

                    if(!testing){ select_database_seq(false, null); }
                    else{ return(Action.SELECT_DATABASE); }
                    break;

                case LIST_DATABASES:

                    if(!testing){ menu_UI.print_databases(true); }
                    else{ return(Action.LIST_DATABASES); }
                    break;

                case START_GUI:

                    Layout program = new Layout();
                    break;

                default:
                    break;    
            }
        }

        return(null);
    }

    // Initialises the Menu_UI with the list of currently available databases.
    // Looks to create a new one in the UI, if one is create then adds it to
    // the current list.
    // (ONLY PUBLIC FOR TESTING.)
    public void create_database_seq(boolean testing, String test_db)
    {
        Database new_db;

        menu_UI = new Menu_UI(databases);

        if( (new_db = menu_UI.create_database(testing, test_db)) != null){
            databases.put( new_db.name(), new_db );
        }

    }

    // If we decide to select a database we run this sequence to see what we
    // want to do to it and then pass that to the database so it can run 
    // the commands on its data.
    // ONLY PUBLIC FOR TESTING.
    public void select_database_seq(boolean testing, String test_db)
    {
        Database sel_db;
        Action select_db_cmmd;

        menu_UI = new Menu_UI(databases);

        if( (sel_db = menu_UI.select_database(testing, test_db) ) != null
            && !testing){

            do{

                select_db_cmmd = menu_UI.database_actions(sel_db);
                intrpt_sel_db_cmmd(sel_db, select_db_cmmd, false);

            }while( select_db_cmmd != Action.LEAVE_DATABASE );

        }
    }

    // Interprets the command and sees if we want to select a database in the
    // table or create a table. This is then passed to the database to make
    // the necessary changes. 
    // ONLY PUBLIC FOR TESTING.
    public Action intrpt_sel_db_cmmd(Database sel_db, Action select_cmmd,
                                   boolean testing)
    {
        switch(select_cmmd) {

            case CREATE_TABLE:

                if(!testing){ sel_db.add_table(false, null); }
                else{ return(Action.CREATE_TABLE); }
                break;

            case SELECT_TABLE:

                if(!testing){ sel_db.select_table(false, null); }
                else{ return(Action.SELECT_TABLE); }
                break;

            case LIST_TABLES:

                if(!testing){ sel_db.list_tables(); }
                else{ return(Action.LIST_TABLES); }
                break;
                
            default:
                break;
        }

        return(Action.NO_COMMAND);

    }

    // This communicates with the GUI and finds the text from the search
    // bar within the names of the databases. It returns a single string to be
    // printed to screen in the text area.
    public String find_text_in_db_names(String text)
    {
        List<String> db_names = new ArrayList<String>();
        for(String name: databases.keySet() ){
            if(name.toLowerCase().contains(text.toLowerCase())){
                db_names.add("Database: " + name);
            }
        }
        String list_of_dbs = out.convert_to_sing_string(db_names);
        return(list_of_dbs);
    }

    // This does the same as the above but with table names.
    public String find_text_in_tab_names(String text)
    {
        List<String> tab_names = new ArrayList<String>();
        tab_names = find_list_of_table_names(text);
        String list_of_tabs = out.convert_to_sing_string(tab_names);
        return(list_of_tabs);
    }

    // This is used in the GUI to find tables with names matching the typed
    // input and then converts them to a single string to be printed to screen.
    public String find_tab(String text)
    {   
        List<Table> tables = new ArrayList<Table>();
        String printable_string = "";
        tables = find_list_of_tables(text);
        printable_string = out.convert_tabs_to_string(tables);
        return(printable_string);
    }

    // This finds the list of table names from the existing ones that match
    // the text within the string.
    public List<String> find_list_of_table_names(String text)
    {
        List<String> tab_names = new ArrayList<String>();
        for(Database ind_database: databases.values() ){
            for(String tab_name : ind_database.tabs_in_db().keySet() ){
                if(tab_name.toLowerCase().contains(text.toLowerCase())){
                    tab_names.add("Table: " + tab_name + 
                                  " (database: " + ind_database.name() + ")");
                }
            }
        }
        return(tab_names);
    }

    // This finds a list of actual tables to return.
    public List<Table> find_list_of_tables(String text)
    {
        List<Table> tables = new ArrayList<Table>();
        for(Database ind_database: databases.values() ){
            for(String tab_name : ind_database.tabs_in_db().keySet() ){
                if(tab_name.toLowerCase().contains(text.toLowerCase())){
                    tables.add(ind_database.tabs_in_db().get(tab_name));
                }
            }
        }
        return(tables);
    }

    // Getters used in testing.

    public Database database(String name)
    {
        return(databases.get(name));
    }

    public void change_cmmd_ind(Action new_ind)
    {
        cmmd_ind = new_ind;
    }

    public void test_add_db(Database to_add)
    {
        databases.put(to_add.name(), to_add);
    }

}