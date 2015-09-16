import java.awt.event.*;

// This class is responsible for looking for events from the checkboxes and
// then setting information in the layout class accordingly.

// Responsibilities: Controlling input and output relating to the GUI checkboxes.

class Checkbox_Listener implements ItemListener {

    private Layout original_win;

    Checkbox_Listener(Layout orig_win)
    {
        original_win = orig_win;
    }

    // This is a switch that looks for different interactions with the GUI
    // and sets the logic in the program accordingly.
    public void itemStateChanged(ItemEvent e) 
    {

        if(e.getItem() == original_win.db_check_box()){
            if(e.getStateChange() == ItemEvent.SELECTED){
                original_win.update_db_check(true);
            }
            else{ original_win.update_db_check(false); }
        }
        else if(e.getItem() == original_win.tab_check_box()){
            if(e.getStateChange() == ItemEvent.SELECTED){
                original_win.update_tab_check(true);
            }
            else{ original_win.update_tab_check(false); }
        }

    }

}