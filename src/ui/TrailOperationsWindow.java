package ui;

import delegates.TrailOperationsDelegate;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * This class is responsible for the main trail operations  GUI and handing
 * user queries
 */
public class TrailOperationsWindow extends JFrame implements ActionListener {
    private static final String EXCEPTION_TAG = "[EXCEPTION]";
    private static final String WARNING_TAG = "[WARNING]";
    private static final int INVALID_INPUT = Integer.MIN_VALUE;
    private static final int EMPTY_INPUT = 0;

    private BufferedReader bufferedReader = null;
    private TrailOperationsDelegate delegate = null;

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

        JButton showContentsButton = new JButton("Show current trails");
        JPanel contentPane = new JPanel();
        this.setContentPane(contentPane);

        // layout components using the GridBag layout manager
        GridBagLayout gb = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();

        contentPane.setLayout(gb);
        contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // place the show contents button
        c.gridwidth = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(5, 10, 10, 10);
//        c.anchor = GridBagConstraints.CENTER;
        gb.setConstraints(showContentsButton, c);
        contentPane.add(showContentsButton);

        showContentsButton.addActionListener(this);

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

    public void showContent() {
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

    /**
     * ActionListener Methods
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        showContent();
    }



}
