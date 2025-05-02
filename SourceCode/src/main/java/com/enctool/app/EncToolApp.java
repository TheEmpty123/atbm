package com.enctool.app;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import javax.swing.*;
import java.awt.*;
import java.security.Security;

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
        frame.setSize(800, 800);
        frame.setLocationRelativeTo(null);

        // Create a tabbed pane
        tabbedPane = new JTabbedPane();

        // Create and add panels for each encryption type
        tabbedPane.addTab("Traditional", new TraditionalEncryptionPanel());
        tabbedPane.addTab("Symmetric", new SymmetricEncryptionPanel());
        tabbedPane.addTab("Asymmetric", new AsymmetricEncryptionPanel());
        tabbedPane.addTab("Hash", new HashPanel());

        // Add tabbed pane to frame
        frame.getContentPane().add(tabbedPane, BorderLayout.CENTER);
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        // Initialize Bouncy Castle provider
        try {
            // Remove any existing BC provider to avoid duplicates
            Security.removeProvider("BC");
            // Add the BC provider with a specific position
            Security.insertProviderAt(new BouncyCastleProvider(), 1);
            // Verify the provider is properly installed
            if (Security.getProvider("BC") == null) {
                System.err.println("Bouncy Castle provider not installed");
            }
        } catch (Exception e) {
            System.err.println("Error initializing Bouncy Castle provider: " + e.getMessage());
            e.printStackTrace();
        }

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
