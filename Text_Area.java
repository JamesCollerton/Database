import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.swing.text.*;
import javax.swing.border.*;

// This class is responsible for controlling how the text areas look and the
// text that is held within them. The text can be updated according to different
// classes.

// Responsibilities: Formatting the text areas.
//                   Controlling the data that is held within them.

class Text_Area extends JTextPane {

    private Border border = BorderFactory.createEmptyBorder(10, 10, 10, 10);

    // Sets the background colour as white, text as centered and adds a border.
    Text_Area() 
    {
        SimpleAttributeSet attribs = new SimpleAttributeSet();  
        StyleConstants.setAlignment(attribs , StyleConstants.ALIGN_CENTER);  
        this.setParagraphAttributes(attribs,true);

        this.setBorder(border);
        this.setBackground(Color.white);

        setText("\nWelcome");
        setEditable(false);
    }

    // Updates whatever text is being displayed in the area.
    public void update_text(String text) 
    {
        setText(text);
    }

}