
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.*;

// This class represents the GUI and how it is layed out. It stores information
// about the state of the GUI (checkboxes ticked etc.) and communicates it to
// the outside world.

// Responsibilities: Setting up and running the GUI.
//                   Communicating interactions with it to the other classes.

class Layout implements Runnable
{
    private Text_Area text_area, table_printing_area;
    private JTextField field;

    private String databases = new String("Databases");
    private String table = new String("Tables");

    private JCheckBox db_check_box, tab_check_box;
    private boolean db_check, tab_check;

    private Border border = BorderFactory.createEmptyBorder(100, 100, 100, 100);

    // When called invokes the GUI.
    Layout()
    {
        db_check = false;
        tab_check = false;
        SwingUtilities.invokeLater(this);
    }

    // Creates the main window for running the program.
    public void run()
    {
        JFrame w = new JFrame();
        w.setDefaultCloseOperation(w.EXIT_ON_CLOSE);
        w.setTitle("Database GUI");
        w.add(display());
        w.pack();
        w.setLocationByPlatform(true);
        w.setVisible(true);
    }

    // Create a display from a set of checkboxes, two text areas and a button.
    Box display()
    {
        JLabel label = new JLabel("Find:");
        label.setBorder(border);

        Box checkboxes = create_checkboxes();
        Box text_field = create_text_field();
        Box left_layer = create_left_layer(label, checkboxes);
        JPanel go_button_panel = new_button_panel("Go");

        text_area = new Text_Area();
        table_printing_area = new Text_Area();

        Box box = Box.createHorizontalBox();
        box.add(left_layer);
        box.add(text_field);
        box.add(go_button_panel);
        box.add(text_area);
        box.setBorder(border);

        Box final_box = Box.createVerticalBox();
        final_box.add(box);
        final_box.add(table_printing_area);
        final_box.setBorder(border);
        final_box.setPreferredSize( new Dimension(600, 400) );

        return(final_box);
    }

    // Create the Go button as seen. Sets the size of the button so it
    // isn't stretched when you move it around and adds a listener so the
    // program can react.
    JPanel new_button(String button_name)
    {
        GridLayout grid = new GridLayout(2, 1, 10, 10);
        JPanel box = new JPanel();
        box.setSize(new Dimension(100, 100));
        JButton new_button = new JButton(button_name);
        Button_Listener new_listen = new Button_Listener(this);
        new_button.addActionListener(new_listen);
        box.setLayout(grid);
        box.setBorder(border);
        box.add(new_button);
        return(box);
    }

    // This creates the panel with the button in and adds the borders and 
    // alignment.
    JPanel new_button_panel(String button_name)
    {
        JPanel new_button = new_button(button_name);
        new_button.setBorder(border);
        new_button.setAlignmentY(JComponent.TOP_ALIGNMENT);
        new_button.setBorder(border);
        Dimension btn_dim = new Dimension(new_button.getPreferredSize().width, 
                                          new_button.getPreferredSize().height); 
        new_button.setMaximumSize(btn_dim);
        return(new_button);
    }

    // This is used to create and align the text field for the form.
    Box create_text_field()
    {
        Box text_field = form();
        text_field.setBorder(border);
        text_field.setAlignmentY(JComponent.TOP_ALIGNMENT);
        return(text_field);
    }

    // This creates the two checkboxes and adds the listener to them to detect
    // the behaviours in the GUI.
    Box create_checkboxes()
    {
        db_check_box = new JCheckBox("Databases");
        tab_check_box = new JCheckBox("Tables");
        Checkbox_Listener myListener = new Checkbox_Listener(this);
        db_check_box.addItemListener(myListener);
        tab_check_box.addItemListener(myListener);
        Box box = Box.createVerticalBox();
        box.add(db_check_box);
        box.add(tab_check_box);
        return(box);
    }

    // Creates the left pane that holds the label and the checkboxes for
    // searching.
    Box create_left_layer(JLabel label, Box checkboxes)
    {
        Box left_layer = Box.createVerticalBox();
        left_layer.add(label);
        left_layer.add(checkboxes);
        left_layer.setAlignmentY(JComponent.TOP_ALIGNMENT);
        return(left_layer);
    }

    // Create a form for the display
    Box form()
    {
        field = new JTextField("");
        Dimension form_max_dim = new Dimension(Integer.MAX_VALUE, 
                                               field.getPreferredSize().height);
        field.setMaximumSize(form_max_dim);
        Dimension form_pref_dim = new Dimension(100, 
                                                field.getPreferredSize().height);
        field.setPreferredSize(form_pref_dim);
        Box box = Box.createVerticalBox();
        box.setBorder(border);
        box.add(field);
        return(box);
    }

    // Getters to be accessed by various other classes to get information on
    // the state of the GUI.

    public String current_text_area()
    {
        return(field.getText());
    }

    public boolean db_check()
    {
        return(db_check);
    }

    public boolean tab_check()
    {
        return(tab_check);
    }

    public Text_Area text_area()
    {
        return(text_area);
    }

    public Text_Area table_printing_area()
    {
        return(table_printing_area);
    }

    public void update_db_check(boolean new_db_check)
    {
        db_check = new_db_check;
    }

    public void update_tab_check(boolean new_tab_check)
    {
        tab_check = new_tab_check;
    }

    public JCheckBox db_check_box()
    {
        return(db_check_box);
    }

    public JCheckBox tab_check_box()
    {
        return(tab_check_box);
    }

}

