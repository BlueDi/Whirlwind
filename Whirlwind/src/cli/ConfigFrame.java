package cli;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

class ConfigFrame extends JFrame {

    private static final long serialVersionUID = 1L;
    private int GAMEMODE = 0;

    /**
     * Create the frame.
     */
    ConfigFrame() {
        setTitle("Whirlwind");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setBounds(100, 100, 292, 84);
        JPanel contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);

        JLabel lblNewLabel = new JLabel("Choose mode");
        lblNewLabel.setBounds(5, 5, 702, 14);
        contentPane.add(lblNewLabel);

        Button button = new Button("PVP");
        button.setBounds(42, 103, 162, 203);
        button.addActionListener(e -> GAMEMODE = 1);
        contentPane.add(button);

        Button button_1 = new Button("PVE");
        button_1.setBounds(263, 103, 162, 203);
        button_1.addActionListener(e -> GAMEMODE = 2);
        contentPane.add(button_1);

        Button button_2 = new Button("EVE");
        button_2.setBounds(490, 103, 162, 203);
        button_2.addActionListener(e -> GAMEMODE = 3);
        contentPane.add(button_2);
    }

    int getGAMEMODE() {
        return GAMEMODE;
    }
}
