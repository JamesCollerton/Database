import java.util.*;

// This class contains everything in the menu that communicates with the 
// outside world. The menu class controls the data and sending messages to
// the rest of the program, but this class controls taking information from
// the user and displaying information to them.

// Responsibilities: Communicating with the outside world from the menu.
//                   Creating new databases to be added to the list of databases.
//                   Choosing tables for the database class to make do things.

public class Menu_UI {

    // List of existing databases so we know not to add replicas and 
    // know which ones exist in it.
    private Map<String, Database> existing_databases;

    // Classes required for scanning, parsing and sending messages to the
    // outside world.
    private ScanFunctions scan_func = new ScanFunctions();
    private Output_UI out = new Output_UI();
    private Parser parser = new Parser();
    private Test test = new Test();

    Menu_UI(Map<String, Database> exist_db)
    {
        existing_databases  = new HashMap<String, Database>(exist_db);
    }

    public void print_welcome()
    {
        out.output("\n\nHello and welcome to JamesDB.\n\n" +
                   "Please refer to the report for the language and" +
                   " guidelines.\n\n");
    }

    // This is the create database sequence. It gets a name from the user,
    // checks if it is taken, checks it is not an empty string and then creates
    // a new database and returns it to the Menu to be added to the list of
    // existing databases.
    public Database create_database(boolean testing, String test_db)
    {
        String new_db_name;
        boolean taken, empty;

        if(!testing){ new_db_name = get_db_name(); }
        else{ new_db_name = test_db; }

        taken = check_not_taken(new_db_name);
        empty = scan_func.check_if_empty(new_db_name);

        if(!taken && !empty){
            Database new_db = new Database(new_db_name);
            print_db_confirm(new_db);
            return(new_db);
        }

        return(null);
    }

    // Gets the new database name from the user.
    private String get_db_name()
    {
        String db_name;

        out.output("\nPlease enter your database name:\n\n> ");
        db_name = scan_func.scan_in_line();

        return(db_name);
    }

    // Checks that the name is not taken already.
    private boolean check_not_taken(String new_db_name)
    {
        if( existing_databases.get(new_db_name) != null ){
            out.output("\nSorry! That database name is taken!\n");
            return(true);
        }

        return(false);
    }

    // Confirms the database has been added.
    private void print_db_confirm(Database new_db)
    {
        out.output("\nSuccessfully added database: " + new_db.name() + "\n\n");
    }

    // Prints a list of databases for the user to look through, then scans in
    // a name and tries to look it up. Finally returns the database to the menu
    // so it can be used later.
    public Database select_database(boolean testing, String test_db)
    {
        String db_name; 
        Database sel_db;

        print_databases(false);

        if(!testing){ db_name = scan_func.scan_in_line(); }
        else{ db_name = test_db; }

        sel_db = lookup_db(db_name);
        return(sel_db);
    }

    // This is the link from the Menu to the Menu UI for choosing what exactly
    // the user wants to do with the selected database.
    public Action database_actions(Database sel_db)
    {
        Action select_db_cmmd;
        out.output("\nCurrently in database " + sel_db.name() + ".\n\n");
        select_db_cmmd = parser.RunParser();
        return(select_db_cmmd);
    }

    // Prints the list of databases in the Menu so that the user can choose one.
    public void print_databases(boolean in_menu)
    {
        out.output("\nAvailable Databases: ");
        for (Database value : existing_databases.values() ) {
            out.output( value.name() + " " );
        }
        if(in_menu){out.output("\n\n");}
        else{ out.output("\n\n> ");}
    }

    // Looks up the database from the existing ones to be returned to the
    // menu. 
    private Database lookup_db(String db_name)
    {
        Database db;

        if( (db = existing_databases.get(db_name)) == null ){
            out.output("\nError, database " + db_name + " does not exist.\n\n");
        }
        else{ out.output("\nSelected database " + db_name + "\n\n"); }

        return(db);
    }

}