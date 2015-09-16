import java.util.*;

// This class will store a row (record) in a table. Each row has fields (the
// actual values in the table) and a key. The key is actually stored seperately
// to the table and is not a field.

// Responsibilites: Storing and giving access to a list of the fields in the 
//                  record.
//                  Storing and giving access to the record key.

public class Record {

	private int rec_key;
	private String parent_table;
    private boolean create_success;
	private List<String> column_headers = new ArrayList<String>();
	private List<Field> column_fields = new ArrayList<Field>();
    private Map<String, String> cols_types = new HashMap<String, String>();
	
    private ScanFunctions scan_func = new ScanFunctions();
    private Record_UI rec_UI = new Record_UI(parent_table, null);

    // Constructor for the record. Needs the record key from the table,
    // the column headers so we know what which field represents, and the parent
    // table so that we can assign the correct max string size to the correct
    // table. If we are using the terminal then it will read in the file from
    // there, otherwise uses a file.
	Record(int rec_key_arg, List<String> col_headers, boolean terminal_input,
           String par_tab, boolean testing, String new_field_arg, 
           Map<String, String> col_ts)
	{
		rec_key = rec_key_arg;
		column_headers = new ArrayList<String>(col_headers);
        parent_table = par_tab;

        if(col_ts != null){ cols_types = new HashMap<String, String>(col_ts);}
		if(terminal_input == true){ add_fields(testing, new_field_arg); }

        rec_UI = new Record_UI(parent_table, cols_types);
	}

    // Initialises some entries and passes them to find col entries to read
    // in user input.
	public void add_fields(boolean testing, String new_field_arg)
    {
        List<String> column_entries = new ArrayList<String>();
        find_col_entries(column_entries, testing, new_field_arg);
    }

    // Reads in the fields from user inputs and creates them as fields. Then
    // adds them to the current list of fields. Also scans them to see how big
    // they are in order to adjust the table to an appropriate size.
    public boolean read_in_fields(List<String> new_field_list)
    {
    	boolean valid_type = true;
        String header_for_type, type;
        Output_UI out = new Output_UI();

        for(int i = 0; i < new_field_list.size(); ++i){

            header_for_type = column_headers.get(i);
            type = cols_types.get(header_for_type);

            if(!scan_func.check_field(new_field_list.get(i), type)){ 
                valid_type = false; 
            }

            Field new_field = new Field( new_field_list.get(i), parent_table );
            column_fields.add(new_field);
            out.check_max_str_len(new_field_list.get(i), parent_table);

        }

        return(valid_type);
    }

    // This finds the fields from the user. If it is the last entry then
    // the UI prints some whitespace for aesthetics.
    private void find_col_entries(List<String> column_entries, boolean testing,
                                  String new_field_arg)
    {
        int i;
        Field new_field;
        List<Field> new_record = new ArrayList<Field>();
        String new_field_value;
        boolean success = true;
        rec_UI = new Record_UI(parent_table, cols_types);

        for(i = 0; i < column_headers.size(); ++i){

            if(i != column_headers.size() - 1){
                new_field_value = rec_UI.find_field(column_headers, i, false, 
                                                    testing, new_field_arg);
            }
            else{
                new_field_value = rec_UI.find_field(column_headers, i, true, 
                                                    testing, new_field_arg);
            }

            if(new_field_value != null){
                new_field = new Field( new_field_value, parent_table );
                new_record.add(new_field);
            }
            else{ success = false;}

        }

        if(success){
            for(Field field : new_record){ column_fields.add(field); }
            create_success = true;
        }
        else{ create_success = false;}

    }

	public int key()
	{
		return(rec_key);
	}

    // Creates a copy of the fields to be returned to other functions.
	public List<String> get()
	{
		List<String> row_entries = new ArrayList<String>();

		for(Field field: column_fields){
			row_entries.add( field.entry() );
		}

		return(row_entries);
	}

	public List<Field> fields()
	{
		return(column_fields);
	}

    public boolean create_success()
    {
        return(create_success);
    }

}