package ui;

import delegates.TrailOperationsDelegate;
import jdk.nashorn.internal.scripts.JO;
import sun.tools.jconsole.inspector.XObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.util.ArrayList;

/**
 * This class is responsible for the main trail operations  GUI and handing
 * user queries
 */
@SuppressWarnings("DuplicatedCode")
public class TrailOperationsWindow extends JFrame implements ActionListener {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private static final int INVALID_INPUT = Integer.MIN_VALUE;
    private static final int EMPTY_INPUT = 0;

    private BufferedReader bufferedReader = null;
    private TrailOperationsDelegate delegate = null;

    JButton showContentsButton;
    JButton deleteTrailButton;
    JButton selectButton;
    JLabel deleteTrailLabel;
    JTextField deleteTrailIdField;

    public TrailOperationsWindow() {
        super("Damn you cute and sessy");
    }

    /**
     * Sets up the database to have a branch table with two tuples so we can insert/update/delete from it.
     * Refer to the databaseSetup.sql file to determine what tuples are going to be in the table.
     */
    public void setupDatabase(TrailOperationsDelegate delegate) {
        this.delegate = delegate;

        delegate.databaseSetup();
    }

    public void showFrame(TrailOperationsDelegate delegate) {
        this.delegate = delegate;

        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        showContentsButton = new JButton("Show current trails");

        deleteTrailButton = new JButton("Delete trail");
        deleteTrailLabel = new JLabel("Enter id of trail to delete: ");
        deleteTrailIdField = new JTextField(10);

        selectButton = new JButton("Perform a selection");


        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the show contents button
        c.gridwidth = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(showContentsButton, c);
        contentPane.add(showContentsButton);

        // place delete trail label
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(10, 10, 5, 0);
        gb.setConstraints(deleteTrailLabel, c);
        contentPane.add(deleteTrailLabel);

        // place delete label field
        // place the text field for the username
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(10, 0, 5, 10);
        gb.setConstraints(deleteTrailIdField, c);
        contentPane.add(deleteTrailIdField);

        // place the delete button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(deleteTrailButton, c);
        contentPane.add(deleteTrailButton);

        // place select button
        c.gridwidth = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(selectButton, c);
        contentPane.add(selectButton);

        showContentsButton.addActionListener(this);
        deleteTrailButton.addActionListener(this);
        selectButton.addActionListener(this);

        // anonymous inner class for closing the window
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // size the window to obtain a best fit for the components
        this.pack();

        // center the frame
        Dimension d = this.getToolkit().getScreenSize();
        Rectangle r = this.getBounds();
        this.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        // make the window visible
        this.setVisible(true);
    }

    public void showDatabaseContent() {
        System.out.println("made it here!");
        ArrayList<String> contentList = delegate.showTrailInfo();
        System.out.println(contentList);
        String printString = "";
        for (String string : contentList ) {
            printString += string + "\n";
        }

        JFrame dialogueFrame = new JFrame("Database Contents");
        JOptionPane.showMessageDialog(dialogueFrame, printString);
    }

    public void handleDelete() {
        String input = deleteTrailIdField.getText();
        if (!input.equals("")) {
            int trailId = Integer.parseInt(input);
            delegate.deleteTrail(trailId);
        }
    }

    public void handleSelection() {
        JLabel selectLabel = new JLabel("SELECT ");
        JLabel fromLabel = new JLabel("FROM              Trail");
        JLabel whereLabel = new JLabel("WHERE ");


        String[] fields = {"difficulty", "distance", "elevation gain"};
        String[] comparators = {">", ">=", "==", "!=", "<", "<="};
        JComboBox<String> fieldsDropDownSelect = new JComboBox<String>(fields);
        JComboBox<String> fieldsDropDownWhere = new JComboBox<String>(fields);
        JComboBox<String> comparatorDropDown = new JComboBox<String>(comparators);
        JTextField conditionInput = new JTextField();


        JFrame selectionFrame = new JFrame("Selection");
        Dimension screenSize = new Dimension(500, 350);


        JPanel selectionPanel = new JPanel();

        selectionFrame.add(selectionPanel);

        selectionPanel.setLayout(null);

        selectionPanel.add(selectLabel);
        selectionPanel.add(fieldsDropDownSelect);
        selectionPanel.add(fromLabel);
        selectionPanel.add(whereLabel);
        selectionPanel.add(fieldsDropDownWhere);
        selectionPanel.add(comparatorDropDown);
        selectionPanel.add(conditionInput);

        Insets insets = conditionInput.getInsets();
        Dimension size = selectLabel.getPreferredSize();
        selectLabel.setBounds(5 + insets.left, 5 + insets.top,
                size.width, size.height);

        size = fieldsDropDownSelect.getPreferredSize();
        fieldsDropDownSelect.setBounds(85 + insets.left, 2 + insets.top,
                size.width, size.height);

        size = fromLabel.getPreferredSize();
        fromLabel.setBounds(5 + insets.left, 40 + insets.top,
                size.width, size.height);

        size = whereLabel.getPreferredSize();
        whereLabel.setBounds(5 + insets.left, 75 + insets.top,
                size.width, size.height);

        size = fieldsDropDownWhere.getPreferredSize();
        fieldsDropDownWhere.setBounds(85 + insets.left, 72 + insets.top,
                size.width, size.height);

        size = comparatorDropDown.getPreferredSize();
        comparatorDropDown.setBounds(85 + insets.left, 95 + insets.top,
                size.width, size.height);

        size = conditionInput.getPreferredSize();
        conditionInput.setBounds(85 + insets.left, 117 + insets.top,
                40+ size.width, size.height);

        // center the frame
        Dimension d = selectionFrame.getToolkit().getScreenSize();
        Rectangle r = selectionFrame.getBounds();
        selectionFrame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        selectionFrame.pack();
        selectionFrame.setVisible(true);
        selectionFrame.setResizable(true);
        selectionFrame.setPreferredSize(screenSize);
        selectionFrame.setMinimumSize(screenSize);
        selectionFrame.setMaximumSize(screenSize);

    }

    /**
     * ActionListener Methods
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Show current trails":
                showDatabaseContent();
            case "Delete trail":
                handleDelete();
            case "Perform a selection":
                handleSelection();
            default:
                break;
        }
    }



}
