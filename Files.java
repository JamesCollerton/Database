import java.util.*;
import java.io.*;
import java.nio.*;
import java.io.File;

// All input and output of files is handled within this class. 

// Responsibilites: This class controls all of the reading and writing to files.

public class Files 
{

	private Map<String, String> cols_types = new HashMap<String, String>();
	private boolean valid_table;
	private int HEADER = 0, TYPE = 1;

	private ScanFunctions scan_func = new ScanFunctions();
	private Output_UI out = new Output_UI();

	// This is used to create folders in the CD when new databases are made.
	public void create_database_folder(String db_name)
    {
        File new_dir = new File(db_name);

        if (!new_dir.exists()) {
                boolean result = false;

            try{
                new_dir.mkdir();
                result = true;
            } 
            catch(SecurityException se){ out.error("Error creating directory.");}        
             
            if(result) {out.output("\nNew directory created for database.\n\n");}
        }
    }

    // This writes the selected table to a file for reading back in later.
    public void write_tab_to_file(Table table)
	{

		try{
			PrintWriter writer = new PrintWriter(table.filename(), "UTF-8");

			write_columns_titles(writer, table);
			write_records(writer, table);

			writer.close();

			out.output("\nWrite successful.\n\n");
		}
		catch(Exception e){

			out.output("\n\nError creating file.\n\n");

		}
	}

	// Each title is seperated by a | symbol, newlines are marked by ||.
	private void write_columns_titles(PrintWriter writer, Table table)
	{
		int i;
		String temp_type;

		for(i = 0; i < table.size(); ++i){
			temp_type = table.cols_types().get(table.col(i));
			writer.printf(" %s <type> %s |", table.col(i), temp_type);
		}

		writer.printf("| ");
	}

	// This function gets every row of the table and then writes it to a file
	// using the method explained above.
	private void write_records(PrintWriter writer, Table table)
	{
		int i, j;
		List<String> row_entries;

		for(i = 0; i < table.num_rows(); ++i){

			row_entries = new ArrayList<String>();
			row_entries = table.records().get(i).get();

			for(j = 0; j < row_entries.size(); ++j){
				writer.printf(" %s |", row_entries.get(j));
			}

			writer.printf("| ");

		}
	}

	// This is used to read the table in from a file. First of all it
	// initialises the part of the Output class that holds the max size of
	// the string. This is so that by default tables are printed out looking
	// nice.
	public void read_table_file(Table table)
	{
		String file_content;
		List<String> file_lines = new ArrayList<String>();

		out.init_max_str_len(table.name());
		file_content = read_data_in_prog(table);

		if(file_content != null){

			file_lines = scan_func.split_input(file_content, "\\|\\|");
			change_headers(file_lines, table);
			if(valid_table){change_row_contents(file_lines, table);
							out.output("\nFile read successful.\n\n");}

		}
	}

	// This reads all of the data in the file into one line in order to
	// be split up later.
	private String read_data_in_prog(Table table)
	{
		String file_content = null;

    	try {
      		file_content = 
      		new Scanner(new File(table.filename())).useDelimiter("\\Z").next();
    	} 
    	catch (Exception e){ out.output("\n\nError reading file.\n\n"); }

    	return(file_content);
	}

	// Here we change the headers of the existing table in order to accomodate
	// the newly scanned headers.
	private void change_headers(List<String> file_lines, Table table)
	{
		List<String> split_input = new ArrayList<String>();
		List<String> split_headers = new ArrayList<String>();
		List<String> split_types = new ArrayList<String>();
		valid_table = true;

		split_input = scan_func.split_input(file_lines.get(0), "\\|");
		scan_func.trim_split_input(split_input);

		split_headers = find_headers_types(split_input, true);
		scan_func.trim_split_input(split_headers);

		split_types = find_headers_types(split_input, false);
		scan_func.trim_split_input(split_types);

		find_max_header_size(split_headers, table.name());

		if(valid_table){ table.replace_existing_headers(split_headers);
						 table.replace_existing_types(split_types); }
	}

	// Finds the maximum size of the new headers and expands the maximum size
	// of the table depending on how big the header is.
	private void find_max_header_size(List<String> headers, String tab_name)
	{
		int i;

		for(String ind_header: headers){
			if(ind_header.length() > out.max_string_length( tab_name ) ){
				out.max_str_len_rep(ind_header.length(), tab_name);
			}
		}
	}

	// This splits the headers at the <type> special character, and then returns
	// the value before or after it depending on whether we are looking for
	// headers or types.
	private List<String> find_headers_types(List<String> split_input, boolean header)
	{
		List<String> result = new ArrayList<String>();
		List<String> split_header_type = new ArrayList<String>();
		boolean unique_headers;

		for(String header_type : split_input){
			split_header_type = scan_func.split_input(header_type, "<type>");
			try{
				if(header){
					check_header_unique(split_header_type.get(HEADER), result);
					result.add(split_header_type.get(HEADER));
				}
				else{result.add(split_header_type.get(TYPE));}
			}catch(Exception err){out.error("\nInvalid file.\n");}
		}

		return(result);
	}

	private void check_header_unique(String header, List<String> result)
	{
		for(String exist_header : result){ 
			if( header.toLowerCase().equals(exist_header.toLowerCase()) ){
				out.output("\nDuplicate column names, creating blank table.\n\n");
				valid_table = false;
			}
		}
	}

	// Here we scan in all of the rest of the file. We reset the key of the
	// table so we don't get duplicate keys. Then for every line in the file
	// we split it and trim it, creating a new record from the input.
	private void change_row_contents(List<String> file_lines, Table table)
	{
		int i;
		List<String> split_input = new ArrayList<String>();
		List<Record> new_records = new ArrayList<Record>();

		table.reset_row_key();
		valid_table = true;
		
		for(i = 1; i < file_lines.size() - 1; ++i){

			split_input = scan_func.split_input(file_lines.get(i), "\\|");
			scan_func.trim_split_input(split_input);

			Record new_rec = 
			new Record(table.row_key(), table.column_names(), false,
					   table.name(), false, null, table.cols_types());

			if(!new_rec.read_in_fields(split_input) ){ valid_table = false;}
			new_records.add(new_rec);

		}

		if(valid_table){ table.replace_records(new_records); }
		else{ out.output("\nFile had invalid entries, blank table created.\n\n"); }
	}

}