package inputBox;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainEditor.imageAction.RangeReceiver;

/**
 * A range-input mechanism implemented as a slider with 2 "thumbs/pointers".
 * 
 * @see {@link https://github.com/ernieyu/Swing-range-slider}
 * @see {@link https://ernienotes.wordpress.com/2010/12/27/creating-a-java-swing-range-slider/}
 */
@SuppressWarnings("serial")
public class RangeSliderBox extends JPanel implements ActionListener{
	
	// text label for user prompt
	private JLabel textPrompt = new JLabel();
	
	// first text label detailing position of first slider
	private JLabel rangeSliderLabel1 = new JLabel();
    private JLabel rangeSliderValue1 = new JLabel();
    
	// second text label detailing position of second slider
    private JLabel rangeSliderLabel2 = new JLabel();
    private JLabel rangeSliderValue2 = new JLabel();
    private RangeSlider rangeSlider = new RangeSlider();
    
    private JButton submitBttn = new JButton("Submit");		// submit button
    private JButton allBttn = new JButton("All Frames");	// submit button, but for all frames
    
    private RangeReceiver parent;							// the parent to whom indices will be passed to
    private JDialog inputBox;								// the dialog box that will ask user for input
    
    // default prompt for an input
    static final String PROMPT = "Please select the frames which you would like to change with this action.";
    
    static final int DEFAULT_MIN = 0;					// default minimum of range
    static final int DEFAULT_MAX = 11;					// default maximum of range plus 1

    /**
     * A slider, which would be used to get data from the user.
     * @param prompt The message to display to the user to tell them what to do.
     * @param parent The parent we have to pass the indices to through some means.
     */
    public RangeSliderBox(String prompt, RangeReceiver parent) {
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new GridBagLayout());

        // sets prompt if it is not empty or null
        if(prompt != null && !prompt.equals("")) {
        	this.textPrompt.setText(prompt);
        }
        else {
        	this.textPrompt.setText(RangeSliderBox.PROMPT);
        }
        
        // the maximum and minimum values for the slider
        int min = DEFAULT_MIN, max = DEFAULT_MAX;
        
        // tracks the parent we have to report back to
        this.parent = parent;
        
        if (parent != null) {
        	min = parent.getRangeMin();
        	max = parent.getRangeMax();
        }
        
        rangeSliderLabel1.setText("> First Frame:");
        rangeSliderLabel2.setText("> Last Frame:");
        rangeSliderValue1.setHorizontalAlignment(JLabel.LEFT);
        rangeSliderValue2.setHorizontalAlignment(JLabel.LEFT);
        
        // sets the size of the slider and its max and min values
        rangeSlider.setPreferredSize(new Dimension(400, rangeSlider.getPreferredSize().height));
        rangeSlider.setMinimum(min);
        rangeSlider.setMaximum(max);
        
        // Adds listener to button to let us know when the user has an input
        submitBttn.addActionListener(this);
        allBttn.addActionListener(this);  
        
        // Add listener to update display.
        rangeSlider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                RangeSlider slider = (RangeSlider) e.getSource();
                rangeSliderValue1.setText(String.valueOf(slider.getValue()));
                rangeSliderValue2.setText(String.valueOf(slider.getUpperValue()));
            }
        });

        // adds components to the pane
        add(textPrompt, new GridBagConstraints(0, 0, 4, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));
        add(rangeSliderLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(rangeSliderValue1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
        add(rangeSliderLabel2, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(rangeSliderValue2, new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 6, 0), 0, 0));
        add(rangeSlider      , new GridBagConstraints(0, 3, 4, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(submitBttn	     , new GridBagConstraints(1, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 40, 0, 0), 0, 0));
        add(allBttn		     , new GridBagConstraints(2, 4, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 20, 0, 0), 0, 0));
    }
    
    /**
     * A display method using a dialog box. By default, blocks all input to 
     * initial parent window until this dialog is closed.
     * @param title The title of the input box.
     * @param win The window we want to center our dialog box on.
     */
    public void display(String title, Window win) {
        // Initialize values.
        rangeSlider.setValue(rangeSlider.getMinimum());
        rangeSlider.setUpperValue(rangeSlider.getMinimum());
        
        // Initialize value display.
        rangeSliderValue1.setText(String.valueOf(rangeSlider.getValue()));
        rangeSliderValue2.setText(String.valueOf(rangeSlider.getUpperValue()));
        
        // displays the input box to the screen
        this.inputBox = DisplayInputBox.display(title, win, this);
        
        // THIS IS NEEDED SO WE GET A REFERENCE TO THE INPUT BOX
        inputBox.setVisible(true);
    }
    
    /**
     * Action listener for submit buttons. Does nothing if the parent is null.
     */
    public void actionPerformed(ActionEvent arg) {
    	if(this.parent !=null) { // if there is a parent to inform
    		if(arg.getSource() == submitBttn) {
    			parent.setIndices(rangeSlider.getValue(), rangeSlider.getUpperValue());
    		}
    		else if (arg.getSource() == allBttn) {
    			parent.setIndices(0,parent.getRangeMax());
    		}
    	}
    	inputBox.dispose();
    	
	}
}
