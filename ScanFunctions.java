import java.util.*;

// This class handles taking in and handling all of the input from the user.

// Responsibilites: Scanning in user input from the console.
//                  Splitting it, parsing it, making it manageable for the 
//                  rest of the program.

public class ScanFunctions {

    private int ERROR = -1, TYPE = 0, TABLE = 1, DATABASE = 2;

	private Scanner scanner = new Scanner(System.in);
    private Output_UI out = new Output_UI();

    // Takes in a line from the console. If returns a blank string then there
    // has been nothing scanned.
	public String scan_in_line()
    {
        String next_line = "";
        boolean valid_input = true;
        try{ next_line = scanner.nextLine(); }
        catch(Exception e){ out.error("Line not scanned."); }
        valid_input = check_valid_input(next_line);
        if(valid_input){return(next_line);}
        return("");
    }

    // These are all special characters used in creating files so that they
    // can be read into the program. We don't allow them to be typed in at all
    // so that they can't mess up the reading and writing process.
    private boolean check_valid_input(String next_line)
    {
        if(next_line.toLowerCase().contains("|") ||
           next_line.toLowerCase().contains("<type>") ||
           next_line.toLowerCase().contains("<foreign>") ){ 
            out.output("\nSorry, invalid characters contained in input.\n\n");
            return(false); 
        }
        return(true);
    }

    // Splits a string based on some characters. Is used in reading files.
    public List<String> split_input(String unsplit_input, String split_char)
    {
        List<String> split_input = new ArrayList<String>();

        try{ split_input = Arrays.asList(unsplit_input.split(split_char)); }
        catch (Exception e) { out.error("Error splitting inputs."); }
        return(split_input);
    }

    // For parsing integers from the rest of the program when we need to certify
    // one.
    public int parse_integer(String argument_string)
    {
        int int_parsed_result = ERROR;

        try{ int_parsed_result = Integer.parseInt(argument_string); }
        catch(NumberFormatException e){ out.output("\nInteger not parsed.\n"); }
        return(int_parsed_result);
    }

    // When we try and enter a float type we parse it and see if it succeeds. If
    // so then we allow it to be added to the table.
    public boolean parse_float(String argument_string)
    {
        float float_result = 0;
        boolean success = true;

        try{ float_result = Float.parseFloat(argument_string); }
        catch(NumberFormatException e){ 
            out.output("\nFloat not parsed.\n");
            success = false;
        }
        return(success);
    }

    // Checks if an input string is empty.
    public boolean check_if_empty(String argument_string)
    {
        if (argument_string.trim().length() > 0){ return(false); }
        out.output("\nInput parsed as empty.\n\n");
        return(true);
    }

    // Trims a split input so that we don't end up with trailing whitespace.
    public void trim_split_input(List<String> split_input)
    {
        for(int i = 0 ; i < split_input.size(); ++i){
            split_input.set(i, split_input.get(i).trim());
        }
    }

    // Whenever we try to enter a field of a certain type we test against this
    // to see if it conforms or not.
    public boolean check_field(String new_field, String type)
    {
        List<String> split_types = new ArrayList<String>();

        if(type != null){
            split_types = split_input(type, "<foreign>");
            trim_split_input(split_types);
        }

        switch(split_types.get(TYPE)) {

            case "int":
                int int_result;
                int_result = parse_integer(new_field);
                if(int_result == ERROR){ return(false); }
                break;

            case "float":
                boolean float_result;
                float_result = parse_float(new_field);
                return(float_result);

            case "foreign":
                boolean valid_foreign;
                valid_foreign = check_foreign(split_types, new_field);
                return(valid_foreign);
            default:
                break;
        }

        return(true);
    }

    // When we have defined a foreign key we use this function to scan through
    // all of the existing keys in the referenced table and see if one matches.
    private boolean check_foreign(List<String> split_types, String new_field)
    {
        Menu menu = new Menu();
        List<Table> tab_mtch = menu.find_list_of_tables(split_types.get(TABLE));

        for(Table tab : tab_mtch){
            if(split_types.get(DATABASE).equals(tab.par_database())){

                List<Integer> rec_keys = tab.record_keys();
                int field_int = parse_integer(new_field);
                for(int key : rec_keys){ if(key == field_int){ return(true); } }
                    
            }
        }

        out.output("\nThis did not match the required foreign key.\n\n");
        return(false);
    }

}