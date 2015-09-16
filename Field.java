import java.util.*;

// These classes ultimately store the data relating to the databases.
// Each class can store, return or print their data. Tables, databases and 
// records had their own UI equivalents, but I didn't think such a small class
// merited one.

// Responsibilites: Storing all values in the table.
// 					Returning the values when needed.

public class Field {

	private String parent_table, value;
	private Output_UI out = new Output_UI();

	Field(String new_val, String par_tab)
	{
		value = new_val;
		parent_table = par_tab;
		out.check_max_str_len(value, parent_table);
	}

	public void print()
	{
		out.col_value(value, parent_table);
	}

	public String entry()
	{
		return(value);
	}

}