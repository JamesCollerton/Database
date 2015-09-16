import java.util.*;

// Thes record class is used for storing the record data and giving access
// to it for other classes. This handles all interaction with the outside world
// for the record class.

// Responsibilites: Outputting to the outside world for the Record class.
//                  Taking in input from the outside world for the Record class.

public class Record_UI {

    private String parent_table;
    private int ERROR = -1;
    private Map<String, String> cols_types = new HashMap<String, String>();

    private ScanFunctions scan_func = new ScanFunctions();
	private Output_UI out = new Output_UI();

    Record_UI(String par_tab, Map<String, String> col_ts)
    {
        parent_table = par_tab;
        if(col_ts != null){cols_types = new HashMap<String, String>(col_ts);}
    }

    // Prints the full record to the screen.
	public void print_record(List<Field> column_fields, int rec_key)
	{
		for(Field field: column_fields){
			field.print();
		}

		out.record_key(rec_key, parent_table);
	}

    // For user input to define values in fields.
	public String find_field(List<String> column_headers, int header, boolean end,
                             boolean testing, String new_field_arg)
    {
        String new_field;
        boolean field_valid = true;

        output_entry_message(column_headers, header);

        if(!testing){ new_field = scan_func.scan_in_line(); }
        else{ new_field = new_field_arg; }

        try{ 
            field_valid = 
            scan_func.check_field(new_field, cols_types.get(column_headers.get(header))); 
        }catch(Exception err){out.output("\nColumn type exception caught.\n"); }

        if(end){ out.ws(2); }
        
        if(field_valid){return(new_field);}
        return(null);
    }

    // Message for telling users what to enter in add record.
    private void output_entry_message(List<String> column_headers, int header)
    {
        out.output("\nEnter " + cols_types.get(column_headers.get(header)) +
                   " for " + column_headers.get(header) + ": ");
    }

}