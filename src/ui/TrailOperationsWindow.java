package ui;

import delegates.TrailOperationsDelegate;

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

    JButton showLakesButton;
    JButton showContentButton;
    JButton showConnectsToButton;
    JButton showTrailsButton;
    JButton deleteTrailButton;
    JButton selectButton;
    JButton joinButton;
    JButton aggregationButton;
    JButton NestedaggregationButton;

    JLabel deleteTrailLabel;
    JTextField deleteTrailIdField;
    private JComboBox<String> fieldsDropDownSelect;
    private JComboBox<String> fieldsDropDownWhere;
    private JComboBox<String> comparatorDropDown;
    private JTextField conditionInput;
    private JTextField selectEntryField;


    public TrailOperationsWindow() {
        super("Trailblazers");
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

        showTrailsButton = new JButton("Show current trails");
        showLakesButton = new JButton("Show current lakes");
        showConnectsToButton = new JButton("Show connections");

        deleteTrailButton = new JButton("Delete trail");
        deleteTrailLabel = new JLabel("Enter id of trail to delete: ");
        deleteTrailIdField = new JTextField(10);

        selectButton = new JButton("Perform a projection or selection");

        aggregationButton = new JButton("Perform a simple aggregation query");
        NestedaggregationButton = new JButton("Perform a nested aggregation query");

        joinButton = new JButton("Perform a query using join");


        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the show trail button
        c.gridwidth = GridBagConstraints.PAGE_START;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(showTrailsButton, c);
        contentPane.add(showTrailsButton);

        // place the show lake button
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(showLakesButton, c);
        contentPane.add(showLakesButton);

        // place the show connections button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(showConnectsToButton, c);
        contentPane.add(showConnectsToButton);

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
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(5, 10, 10, 10);
        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(deleteTrailButton, c);
        contentPane.add(deleteTrailButton);

        // place select button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(selectButton, c);
        contentPane.add(selectButton);

        // place join button
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(joinButton, c);
        contentPane.add(joinButton);


        // place aggregation button
        c.gridwidth = GridBagConstraints.REMAINDER;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(aggregationButton, c);
        contentPane.add(aggregationButton);

        // place Nested aggregation button
        c.gridwidth = GridBagConstraints.RELATIVE;
        c.insets = new Insets(5, 10, 10, 10);
        gb.setConstraints(NestedaggregationButton, c);
        contentPane.add(NestedaggregationButton);

        showLakesButton.addActionListener(this);
        showConnectsToButton.addActionListener(this);
        showTrailsButton.addActionListener(this);

        deleteTrailButton.addActionListener(this);
        selectButton.addActionListener(this);
        joinButton.addActionListener(this);
        aggregationButton.addActionListener(this);
        NestedaggregationButton.addActionListener(this);

        // anonymous inner class for closing the window
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                delegate.trailOperationsFinished();
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

    public void showTrails() {
        ArrayList<String> contentList = delegate.showTrailInfo();
        String printString = "";
        for (String string : contentList ) {
            printString += string + "\n\n";
        }

        JFrame dialogueFrame = new JFrame("Trails");
        JOptionPane.showMessageDialog(dialogueFrame, printString);
    }

    public void showLakes() {
        ArrayList<String> contentList = delegate.showLakeInfo();
        String printString = "";
        for (String string : contentList ) {
            printString += string + "\n\n";
        }

        JFrame dialogueFrame = new JFrame("Lakes");
        JOptionPane.showMessageDialog(dialogueFrame, printString);
    }

    public void showConnections() {
        ArrayList<String> contentList = delegate.showConnectionInfo();
        String printString = "";
        for (String string : contentList ) {
            printString += string + "\n\n";
        }

        JFrame dialogueFrame = new JFrame("Connects_to");
        JOptionPane.showMessageDialog(dialogueFrame, printString);
    }

    public void handleDelete() {
        String input = deleteTrailIdField.getText();
        deleteTrailIdField.setText("");
        if (!input.equals("")) {
            int trailId = Integer.parseInt(input);
            delegate.deleteTrail(trailId);
        }
    }

    public void handleAggregationquery() {
        delegate.performAggregation();
    }

    public void showSelectionWindow() {
        JLabel selectLabel = new JLabel("SELECT ");
        JLabel fromLabel = new JLabel("FROM              Trail");
        JLabel whereLabel = new JLabel("WHERE ");
        JButton searchButton = new JButton("Run query");
        searchButton.addActionListener(this);


        String[] selectFields = {"*", "difficulty", "distance", "elevation gain"};
        String[] whereFields = {"difficulty", "distance", "elevation gain"};

        String[] comparators = {">", ">=", "==", "!=", "<", "<="};
        fieldsDropDownSelect = new JComboBox<String>(selectFields);
        fieldsDropDownWhere = new JComboBox<String>(whereFields);
        comparatorDropDown = new JComboBox<String>(comparators);
        conditionInput = new JTextField();


        JFrame selectionFrame = new JFrame("Selection");
        Dimension screenSize = new Dimension(500, 350);


        JPanel selectionPanel = new JPanel();

        selectionFrame.add(selectionPanel);

        selectionPanel.setLayout(null);

        selectionPanel.add(searchButton);

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
                40 + size.width, size.height);

        size = searchButton.getPreferredSize();
        searchButton.setBounds(40 + insets.left, 150 + insets.top,
                size.width, size.height);

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

    public void handleSelectionSearch() {
        String selectAttribute =  (String) fieldsDropDownSelect.getSelectedItem();
        String whereAttribute =  (String) fieldsDropDownWhere.getSelectedItem();
        String comparator = (String) comparatorDropDown.getSelectedItem();
        String value = conditionInput.getText();

        delegate.performSelection(selectAttribute, whereAttribute, comparator, value);
    }


    public void handleNestedAggregationQuery() {
        delegate.peformNestedAggregation();
    }
    public void showJoinWindow() {
        JLabel selectLabel = new JLabel("SELECT: ");
        JLabel remainderLabel = new JLabel("<html>FROM              Trail t, Lake l, Connects_to ct" +
                "<br/><br/>WHERE             swimmable = 1</html>");
        JLabel header = new JLabel("Perform a query over the trails that are connected to swimmable lakes:");
        JLabel selectOptions = new JLabel("<html>Enter one or more of: t.trail_id, ct.trail_id " +
                "t.trail_name, t.trail_difficulty,<br/> t.trail_distance, t.trail_elevation_gain, ct.lake_name, l.lake_name,<br/> l.swimmable</html>");

        selectEntryField = new JTextField(20);

        JButton searchButton = new JButton("Search");

        JFrame frame = new JFrame("Join Query");
        JPanel panel = new JPanel();

        Dimension screenSize = new Dimension(500, 350);

        frame.add(panel);

        panel.setLayout(null);
        panel.add(selectLabel);
        panel.add(remainderLabel);
        panel.add(header);
        panel.add(selectOptions);
        panel.add(selectEntryField);
        panel.add(searchButton);

        Insets insets = frame.getInsets();
        Dimension size = header.getPreferredSize();
        header.setBounds(5 + insets.left, 5 + insets.top,
                size.width, size.height);

        size = selectOptions.getPreferredSize();
        selectOptions.setBounds(5 + insets.left, 45 + insets.top,
                size.width, size.height);

        size = selectLabel.getPreferredSize();
        selectLabel.setBounds(5 + insets.left, 100 + insets.top,
                size.width, size.height);

        size = selectEntryField.getPreferredSize();
        selectEntryField.setBounds(85 + insets.left, 95 + insets.top,
                size.width, size.height);

        size = remainderLabel.getPreferredSize();
        remainderLabel.setBounds(5 + insets.left, 120 + insets.top,
                size.width, size.height);

        size = searchButton.getPreferredSize();
        searchButton.setBounds(5 + insets.left, 180 + insets.top,
                size.width, size.height);

        searchButton.addActionListener(this);

        // center the frame
        Dimension d = frame.getToolkit().getScreenSize();
        Rectangle r = frame.getBounds();
        frame.setLocation( (d.width - r.width)/2, (d.height - r.height)/2 );

        frame.pack();
        frame.setVisible(true);
        frame.setResizable(true);
        frame.setPreferredSize(screenSize);
        frame.setMinimumSize(screenSize);
        frame.setMaximumSize(screenSize);

    }

    public void handleJoinSearch() {
        String selection = (String) selectEntryField.getText();

        delegate.performJoinSearch(selection);
    }

    public void displaySearchResults(ArrayList<String> results) {
        String printString = "";

        for (String string : results ) {
            printString +=  string + "\n\n";
        }

        if (printString.equals("")) {
            printString = "No results found.";
        }

        JFrame dialogueFrame = new JFrame("Search Results");
        JOptionPane.showMessageDialog(dialogueFrame, printString);
    }


    /**
     * ActionListener Methods
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        String actionCommand = e.getActionCommand();
        switch (actionCommand) {
            case "Show current trails":
                showTrails();
                break;
            case "Delete trail":
                handleDelete();
                break;
            case "Perform a projection or selection":
                showSelectionWindow();
                break;
            case "Run query":
                handleSelectionSearch();
                break;
            case "Show current lakes":
                showLakes();
                break;
            case "Show connections":
                showConnections();
                break;
            case "Perform a query using join":
                showJoinWindow();
                break;
            case "Search":
                handleJoinSearch();
                break;
            case "Perform a simple aggregation query":

                handleAggregationquery();
                break;
            case "Perform a nested aggregation query":
                handleNestedAggregationQuery();
                break;
            default:
                break;
        }
    }

    public void displayAggregateResults(String result) {
        JFrame dialogueFrame = new JFrame("Search Results");
        JOptionPane.showMessageDialog(dialogueFrame, "Query: " +
                "\n SELECT MAX(trail_distance) " +
                "\n FROM trail" +
                "\n\n Result: " +
                "\nThe longest distance of any trail is: "+ result);
    }
    public void displayNestedAggregateResults(String result) {
        JFrame dialogueFrame = new JFrame("Search Results");
        JOptionPane.showMessageDialog(dialogueFrame, "Query: " +
                "\n" +
                 "SELECT MAX(t0.trail_difficulty)" +
                        "\n FROM trail t0 " +
                        "\n GROUP BY t0.trail_difficulty" +
                        "\n HAVING MAX(t0.trail_elevation_gain) <= all (SELECT MAX(t1.trail_elevation_gain) " +
                        "\n\t\tFROM trail t1 " +
                        "\n\t\tGROUP BY t1.trail_difficulty) " +
                "\n\n Result: \n The highest difficulty rating(s) where the highest elevation gain" +
                " is the minimum over all difficulties is/are: "+ result);
    }
}
