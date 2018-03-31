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
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import mainEditor.imageAction.RatioReceiver;

/**
 * Input Dialog panel to receive input by means of a slider. 
 * 
 * @see {@link https://github.com/ernieyu/Swing-range-slider}
 * @see {@link https://ernienotes.wordpress.com/2010/12/27/creating-a-java-swing-range-slider/}
 */
@SuppressWarnings("serial")
public class SliderBox extends JPanel implements ActionListener{
	
	// text label for user prompt
	private JLabel textPrompt = new JLabel();
	
	// first text label detailing position of first slider
	private JLabel sliderLabel1 = new JLabel();
    private JLabel sliderValue1 = new JLabel();
    
    private JSlider slider = new JSlider();
    
    private JButton submitBttn = new JButton("Submit");		// submit button
    
    private RatioReceiver parent;							// the parent to whom indices will be passed to
    private JDialog inputBox;								// the dialog box that will ask user for input
    
    // default prompt for an input
    static final String PROMPT = "Please move slider left to decrease the size of the image; right to increase it.";
    
    static final int DEFAULT_MIN = 50;					// default minimum of ratio
    static final int DEFAULT_MAX = 200;					// default maximum of ratio
    static final int DEFAULT_SCALE_RATIO = 100;			// default scale ratio (100 menas no scaling needed)

    /**
     * A slider, which would be used to get data from the user.
     * @param prompt The message to display to the user to tell them what to do.
     * @param parent The parent we have to pass the ratio to through some means.
     */
    public SliderBox(String prompt, RatioReceiver parent, float minRatio, float maxRatio) {
        setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        setLayout(new GridBagLayout());

        // sets prompt if it is not empty or null
        if(prompt != null && !prompt.equals("")) {
        	this.textPrompt.setText(prompt);
        }
        else {
        	this.textPrompt.setText(SliderBox.PROMPT);
        }
        
        // the maximum and minimum values for the slider
        int min = DEFAULT_MIN, max = DEFAULT_MAX;
        
        // tracks the parent we have to report back to
        this.parent = parent;
        
        if (parent != null) {
        	min = (int)(minRatio*100);
        	max = (int)(maxRatio*100);
        }
        
        sliderLabel1.setText("> Image:");
        sliderValue1.setHorizontalAlignment(JLabel.LEFT);
        
        // sets the size of the slider and its max and min values
        slider.setPreferredSize(new Dimension(500, slider.getPreferredSize().height));
        slider.setMinimum(min);
        slider.setMaximum(max);
        
        // puts some aesthetics on the slider
        slider.setPaintTicks(true);
        slider.setSnapToTicks(true);
        slider.setMajorTickSpacing(20);
        slider.setMinorTickSpacing(5);
        
        // Adds listener to button to let us know when the user has an input
        submitBttn.addActionListener(this);
        
        // Add listener to update display.
        slider.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                JSlider slider = (JSlider) e.getSource();
                sliderValue1.setText(String.valueOf(slider.getValue()));
            }
        });

        // adds components to the pane
        add(textPrompt	, new GridBagConstraints(0, 0, 5, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(3, 0, 3, 0), 0, 0));
        add(sliderLabel1, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 3), 0, 0));
        add(sliderValue1, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 3, 0), 0, 0));
        add(slider      , new GridBagConstraints(0, 2, 5, 1, 0.0, 0.0,
            GridBagConstraints.NORTHWEST, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
        add(submitBttn	, new GridBagConstraints(0, 3, 5, 1, 1, 0.0,
            GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(10, 0, 0, 0), 0, 0));
    }
    
    /**
     * A display method for a dialog box.  By default, blocks all input to 
     * initial parent window until this dialog is closed.
     * @param title The title of the input box.
     * @param win The window we want to center our dialog box on.
     */
    public void display(String title, Window win) {
        // Initialize values to middle of slider.
        slider.setValue(DEFAULT_SCALE_RATIO);
        
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
    			parent.setRatio(slider.getValue()/100f);
    		}
    	}
    	inputBox.dispose();
    	
	}
}
