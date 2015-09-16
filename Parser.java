import java.util.*;

// This class is for parsing user commands. It takes in input from the user
// and then returns an enumerated type depending on what they have input.

// Responsibilites: Parsing commands from the user.

public class Parser {

    private Action action;
	private String current_command, menu_command;
	private List<String> split_command = new ArrayList<String>();

    private ScanFunctions scan_func = new ScanFunctions();
    private Output_UI out = new Output_UI();

    // Runs the main part of the parser, returning the action to wherever the
    // parser has been called from.
	public Action RunParser() 
    {
    	scan_command_line();
    	split_command = scan_func.split_input(current_command, " ");
    	convert_lwr_case();
    	parse_cmmds();
    	return(action);
    }

    // Gets command from the user.
    private void scan_command_line()
    {
    	out.output("> ");
    	current_command = scan_func.scan_in_line();
    }

    // Converts is to lower case for maximum flexibility.
    private void convert_lwr_case()
    {
    	for(int i = 0; i < split_command.size(); ++i){
    		split_command.set(i, split_command.get(i).toLowerCase());
    	}
    }

    // Checks for valid user commands being entered and then selects an
    // action based on them.
    private void parse_cmmds()
    {

        if(split_command.size() == 2){

            if( check_for("create", "table") ){action = Action.CREATE_TABLE; return; }
            else if( check_for("exit", "db") ){action = Action.EXIT_DB; return; }
            else if( check_for("select", "table") ){action = Action.SELECT_TABLE; return; }
            else if( check_for("add", "record") ){action = Action.ADD_RECORD; return; }
            else if( check_for("print", "table") ){action = Action.PRINT_TABLE; return; }
            else if( check_for("alter", "columns") ){action = Action.ALTER_COLS; return; }
            else if( check_for("get", "columns") ){action = Action.GET_COLS; return; }
            else if( check_for("number", "columns") ){action = Action.GET_NUM_COLS; return; }
            else if( check_for("number", "rows") ){action = Action.GET_NUM_ROWS; return; }
            else if( check_for("get", "record") ){action = Action.GET_RECORD; return; }
            else if( check_for("create", "database") ){action = Action.CREATE_DATABASE; return; }
            else if( check_for("select", "database") ){action = Action.SELECT_DATABASE; return; }
            else if( check_for("delete", "record") ){action = Action.DELETE_RECORD; return; }
            else if( check_for("write", "file") ){action = Action.WRITE_FILE; return; }
            else if( check_for("read", "file") ){action = Action.READ_FILE; return; }
            else if( check_for("leave", "table") ){action = Action.LEAVE_TABLE; return; }
            else if( check_for("leave", "database") ){action = Action.LEAVE_DATABASE; return; }
            else if( check_for("start", "gui") ){action = Action.START_GUI; return; }
            else if( check_for("list", "databases") ){action = Action.LIST_DATABASES; return; }
            else if( check_for("list", "tables") ){action = Action.LIST_TABLES; return; }
        }

        action = Action.NO_COMMAND;
    }

    // Checks against two strings and returns true if the input is the same.
    private boolean check_for(String first_word, String sec_word)
    {
        if( first_word.equals(split_command.get(0)) && 
            sec_word.equals(split_command.get(1))){ return(true); }   
        return(false);
    }

}