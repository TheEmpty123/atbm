package com.enctool.app;

import javax.swing.*;
import java.awt.*;

public class EncToolApp {
    private JFrame frame;
    private JTabbedPane tabbedPane;

    public EncToolApp() {
        initializeUI();
    }

    private void initializeUI() {
        // Set up the main frame
        frame = new JFrame("Encryption Tool");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);

        // Create a tabbed pane
        tabbedPane = new JTabbedPane();
        
        // Create and add panels for each encryption type
        tabbedPane.addTab("Traditional", new TraditionalEncryptionPanel());
        tabbedPane.addTab("Symmetric", new SymmetricEncryptionPanel());
        tabbedPane.addTab("Asymmetric", new AsymmetricEncryptionPanel());

        // Add tabbed pane to frame
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Set the look and feel to system default
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Launch the application on the EDT
        SwingUtilities.invokeLater(() -> {
            EncToolApp app = new EncToolApp();
            app.show();
        });
    }
}
