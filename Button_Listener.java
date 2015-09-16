import java.awt.event.*;

// This button listener class is used to listen for presses on the search button
// within the GUI. If they are registered it takes the appropriate action.

// Responsibilities: Dealing with actions coming from button presses on the GUI.

class Button_Listener implements ActionListener
{
    // Menu is included here as it contains a list of all databases and can be
    // scanned for them and their properties.
    private Layout original_win;
    private Menu menu = new Menu();

    // Need reference to the original window in order to carry out actions
    // there depending on the button click
    Button_Listener(Layout orig_win)
    {
        original_win = orig_win;
    }

    // This registers the action being performed and passes to the find
    // sequence function. 
    public void actionPerformed(ActionEvent e)
    {
        find_sequence();
    }

    // Takes the value from the text field and either looks for it in 
    // databases or tables depending on the checkbox selection.
    private void find_sequence()
    {
        String textFieldValue = original_win.current_text_area();
        String db_tabs = "", tables_to_print = "";

        if( original_win.db_check() ){
            db_tabs = menu.find_text_in_db_names(textFieldValue);
            original_win.text_area().update_text(db_tabs);
        }

        if ( original_win.tab_check() ){
            db_tabs = db_tabs + menu.find_text_in_tab_names(textFieldValue);
            tables_to_print = menu.find_tab(textFieldValue);
            original_win.table_printing_area().update_text(tables_to_print);
            original_win.text_area().update_text(db_tabs);
        }
        else{ original_win.table_printing_area().update_text(""); }

    }
}