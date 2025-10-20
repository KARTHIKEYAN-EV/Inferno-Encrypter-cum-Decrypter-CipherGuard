package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import algorithms.*;
import exceptions.InvalidKeyException;
import io.FileHandler;
import util.Logger;

public class MainAppUI extends JFrame {
    private JComboBox<String> cipherComboBox;
    private JTextField keyField;
    private JTextField mappingField;
    private JTextArea inputTextArea;
    private JTextArea outputTextArea;
    private JTextField inputFileField;
    private JTextField outputFileField;
    private JRadioButton fileInputRadio;
    private JRadioButton textInputRadio;
    private JButton encryptButton;
    private JButton decryptButton;
    private JButton browseInputButton;
    private JButton browseOutputButton;

    private CardLayout cardLayout;
    private JPanel keyPanel;

    // iPhone-inspired color scheme
    private final Color PRIMARY_COLOR = new Color(0, 122, 255); // iOS Blue
    private final Color SECONDARY_COLOR = new Color(88, 86, 214); // iOS Purple
    private final Color ACCENT_COLOR = new Color(52, 199, 89); // iOS Green
    private final Color DANGER_COLOR = new Color(255, 59, 48); // iOS Red
    private final Color WARNING_COLOR = new Color(255, 149, 0); // iOS Orange
    private final Color DARK_BG = new Color(28, 28, 30); // iOS Dark Gray
    private final Color CARD_BG = new Color(44, 44, 46); // iOS Card Gray
    private final Color TEXT_AREA_BG = new Color(255, 255, 255); // Pure White
    private final Color TEXT_COLOR = new Color(255, 255, 255); // White Text
    private final Color BUTTON_TEXT_COLOR = new Color(0, 0, 0); // Black Text for Buttons
    private final Color BORDER_COLOR = new Color(60, 60, 65); // iPhone border color

    public MainAppUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("CryptoGuard - Secure Encryption");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Better default window size so layout isn't cramped
        setSize(800, 700);
        setLocationRelativeTo(null);

        // Set application icon
        setIconImage(createDefaultIcon());

        // Create main panel with iOS-style background
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Solid dark background like iOS
                g2d.setColor(DARK_BG);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        // Add header with beautiful title
        mainPanel.add(createAestheticHeader(), BorderLayout.NORTH);

        // Add center content with optimized text areas
        mainPanel.add(createAestheticCenterPanel(), BorderLayout.CENTER);

        // Add button panel
        mainPanel.add(createAestheticButtonPanel(), BorderLayout.SOUTH);

        // Wrap in scroll pane for overall application
        JScrollPane mainScrollPane = new JScrollPane(mainPanel);
        mainScrollPane.setBorder(null);
        mainScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        mainScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        mainScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        styleMainScrollPane(mainScrollPane);

        add(mainScrollPane);

        // Initialize the input panel state
        updateKeyPanel(); // make sure key panel shows right card initially
        updateInputPanel();
    }

    private JPanel createAestheticHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout(0, 15));
        headerPanel.setOpaque(false);

        // Beautiful title with gradient effect
        JLabel titleLabel = new JLabel("CryptoGuard", JLabel.CENTER) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Beautiful gradient for title
                GradientPaint gradient = new GradientPaint(
                        0, 0, new Color(100, 200, 255),
                        getWidth(), getHeight(), new Color(175, 82, 222));
                g2d.setPaint(gradient);
                g2d.setFont(getFont());
                FontMetrics fm = g2d.getFontMetrics();
                g2d.drawString(getText(),
                        (getWidth() - fm.stringWidth(getText())) / 2,
                        fm.getAscent() + (getHeight() - fm.getHeight()) / 2);
            }
        };
        titleLabel.setFont(new Font("SF Pro Display", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 10, 0));

        // Beautiful subtitle
        JLabel subtitleLabel = new JLabel("Advanced Encryption Toolkit", JLabel.CENTER);
        subtitleLabel.setFont(new Font("SF Pro Text", Font.PLAIN, 14));
        subtitleLabel.setForeground(new Color(200, 200, 220));
        subtitleLabel.setBorder(new EmptyBorder(0, 0, 5, 0));

        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.add(titleLabel, BorderLayout.CENTER);
        titlePanel.add(subtitleLabel, BorderLayout.SOUTH);

        headerPanel.add(titlePanel, BorderLayout.NORTH);
        headerPanel.add(createAestheticSettingsPanel(), BorderLayout.CENTER);

        return headerPanel;
    }

    private JPanel createAestheticSettingsPanel() {
        JPanel settingsPanel = new JPanel();
        settingsPanel.setLayout(new BoxLayout(settingsPanel, BoxLayout.Y_AXIS));
        settingsPanel.setOpaque(false);
        settingsPanel.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Cipher selection panel
        JPanel cipherPanel = createAestheticCardPanel("CIPHER SELECTION");
        cipherPanel.setLayout(new BorderLayout(8, 8));

        JLabel cipherLabel = new JLabel("Encryption Algorithm:");
        cipherLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        cipherLabel.setForeground(TEXT_COLOR);

        String[] ciphers = { "Caesar Cipher", "XOR Cipher", "Substitution Cipher" };
        cipherComboBox = new JComboBox<>(ciphers);
        styleAestheticComboBox(cipherComboBox);
        cipherComboBox.addActionListener(e -> updateKeyPanel());

        cipherPanel.add(cipherLabel, BorderLayout.NORTH);
        cipherPanel.add(cipherComboBox, BorderLayout.CENTER);

        // Key panel with card layout
        cardLayout = new CardLayout();
        keyPanel = new JPanel(cardLayout);
        keyPanel.setOpaque(false);
        keyPanel.setBorder(new EmptyBorder(8, 0, 0, 0));

        // Standard key panel
        JPanel standardKeyPanel = createAestheticCardPanel("ENCRYPTION KEY");
        standardKeyPanel.setLayout(new BorderLayout(8, 8));

        JLabel keyLabel = new JLabel("Numeric Key:");
        keyLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        keyLabel.setForeground(TEXT_COLOR);

        keyField = new JTextField();
        keyField.setColumns(10);
        styleAestheticTextField(keyField);

        standardKeyPanel.add(keyLabel, BorderLayout.NORTH);
        standardKeyPanel.add(keyField, BorderLayout.CENTER);

        // Substitution key panel
        JPanel substitutionKeyPanel = createAestheticCardPanel("CHARACTER MAPPING");
        substitutionKeyPanel.setLayout(new BorderLayout(8, 8));

        JLabel mappingLabel = new JLabel("26-Letter Mapping:");
        mappingLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        mappingLabel.setForeground(TEXT_COLOR);

        mappingField = new JTextField();
        mappingField.setColumns(15);
        styleAestheticTextField(mappingField);
        mappingField.setToolTipText("Enter 26 unique uppercase letters A-Z mapping");

        substitutionKeyPanel.add(mappingLabel, BorderLayout.NORTH);
        substitutionKeyPanel.add(mappingField, BorderLayout.CENTER);

        keyPanel.add(standardKeyPanel, "standard");
        keyPanel.add(substitutionKeyPanel, "substitution");

        // Input type selection panel
        JPanel inputTypePanel = createAestheticCardPanel("INPUT METHOD");
        inputTypePanel.setLayout(new BorderLayout(8, 8));

        JLabel inputTypeLabel = new JLabel("Input Source:");
        inputTypeLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        inputTypeLabel.setForeground(TEXT_COLOR);

        JPanel radioPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 8));
        radioPanel.setOpaque(false);

        ButtonGroup inputGroup = new ButtonGroup();
        textInputRadio = new JRadioButton("Text Input", true);
        fileInputRadio = new JRadioButton("File Input");
        styleAestheticRadioButton(textInputRadio);
        styleAestheticRadioButton(fileInputRadio);
        inputGroup.add(textInputRadio);
        inputGroup.add(fileInputRadio);

        textInputRadio.addActionListener(e -> updateInputPanel());
        fileInputRadio.addActionListener(e -> updateInputPanel());

        radioPanel.add(textInputRadio);
        radioPanel.add(fileInputRadio);

        inputTypePanel.add(inputTypeLabel, BorderLayout.NORTH);
        inputTypePanel.add(radioPanel, BorderLayout.CENTER);

        // File operations panel - WHITE HEADER, CENTERED
        JPanel filePanel = createAestheticCardPanel("FILE OPERATIONS");
        filePanel.setLayout(new BoxLayout(filePanel, BoxLayout.Y_AXIS));

        // Input File Section
        JPanel inputFileSection = new JPanel(new BorderLayout(8, 8));
        inputFileSection.setOpaque(false);

        JLabel inputFileLabel = new JLabel("Input File:");
        inputFileLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        inputFileLabel.setForeground(TEXT_COLOR);

        JPanel inputFileRow = new JPanel(new BorderLayout(8, 0));
        inputFileRow.setOpaque(false);

        inputFileField = new JTextField();
        styleAestheticTextField(inputFileField);

        browseInputButton = new JButton("Browse");
        styleAestheticButton(browseInputButton, new Color(100, 100, 105));
        browseInputButton.addActionListener(e -> browseInputFile());

        inputFileRow.add(inputFileField, BorderLayout.CENTER);
        inputFileRow.add(browseInputButton, BorderLayout.EAST);

        inputFileSection.add(inputFileLabel, BorderLayout.NORTH);
        inputFileSection.add(inputFileRow, BorderLayout.CENTER);

        // Output File Section
        JPanel outputFileSection = new JPanel(new BorderLayout(8, 8));
        outputFileSection.setOpaque(false);
        outputFileSection.setBorder(new EmptyBorder(12, 0, 0, 0));

        JLabel outputFileLabel = new JLabel("Output File:");
        outputFileLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        outputFileLabel.setForeground(TEXT_COLOR);

        JPanel outputFileRow = new JPanel(new BorderLayout(8, 0));
        outputFileRow.setOpaque(false);

        outputFileField = new JTextField();
        styleAestheticTextField(outputFileField);

        browseOutputButton = new JButton("Browse");
        styleAestheticButton(browseOutputButton, new Color(100, 100, 105));
        browseOutputButton.addActionListener(e -> browseOutputFile());

        outputFileRow.add(outputFileField, BorderLayout.CENTER);
        outputFileRow.add(browseOutputButton, BorderLayout.EAST);

        outputFileSection.add(outputFileLabel, BorderLayout.NORTH);
        outputFileSection.add(outputFileRow, BorderLayout.CENTER);

        // Add sections to file panel
        filePanel.add(inputFileSection);
        filePanel.add(outputFileSection);

        // Add all panels to settings with beautiful spacing
        settingsPanel.add(cipherPanel);
        settingsPanel.add(Box.createVerticalStrut(12));
        settingsPanel.add(keyPanel);
        settingsPanel.add(Box.createVerticalStrut(12));
        settingsPanel.add(inputTypePanel);
        settingsPanel.add(Box.createVerticalStrut(12));
        settingsPanel.add(filePanel);

        return settingsPanel;
    }

    private JPanel createAestheticCenterPanel() {
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Input panel - OPTIMIZED SIZE
        JPanel inputPanel = createAestheticTextAreaPanel("INPUT TEXT");
        inputTextArea = new JTextArea(8, 35);
        styleAestheticTextArea(inputTextArea);
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        styleAestheticScrollPane(inputScroll);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Output panel - OPTIMIZED SIZE
        JPanel outputPanel = createAestheticTextAreaPanel("OUTPUT TEXT");
        outputPanel.setBorder(new EmptyBorder(15, 0, 0, 0));
        outputTextArea = new JTextArea(8, 35);
        styleAestheticTextArea(outputTextArea);
        outputTextArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        styleAestheticScrollPane(outputScroll);

        // Copy button
        JPanel outputButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 8));
        outputButtonPanel.setOpaque(false);

        JButton copyButton = new JButton("Copy to Clipboard");
        styleAestheticButton(copyButton, new Color(100, 100, 105));
        copyButton.addActionListener(e -> copyOutputToClipboard());

        outputButtonPanel.add(copyButton);

        outputPanel.add(outputScroll, BorderLayout.CENTER);
        outputPanel.add(outputButtonPanel, BorderLayout.SOUTH);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);

        return centerPanel;
    }

    private JPanel createAestheticButtonPanel() {
        JPanel buttonPanel = new JPanel(new GridLayout(2, 2, 10, 10));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(15, 0, 0, 0));

        encryptButton = new JButton("ENCRYPT");
        styleAestheticButton(encryptButton, ACCENT_COLOR);
        encryptButton.addActionListener(e -> performEncryption());

        decryptButton = new JButton("DECRYPT");
        styleAestheticButton(decryptButton, PRIMARY_COLOR);
        decryptButton.addActionListener(e -> performDecryption());

        JButton clearButton = new JButton("CLEAR ALL");
        styleAestheticButton(clearButton, WARNING_COLOR);
        clearButton.addActionListener(e -> clearAll());

        JButton exitButton = new JButton("EXIT");
        styleAestheticButton(exitButton, DANGER_COLOR);
        exitButton.addActionListener(e -> System.exit(0));

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    // Aesthetic styling helper methods
    private JPanel createAestheticCardPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(8, 8));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true), // Black/white border
                new EmptyBorder(12, 15, 12, 15)));

        if (title != null) {
            JLabel titleLabel = new JLabel(title, JLabel.CENTER); // Centered header
            titleLabel.setFont(new Font("SF Pro Text", Font.BOLD, 13));
            titleLabel.setForeground(TEXT_COLOR); // White color
            titleLabel.setBorder(new EmptyBorder(0, 0, 5, 0));
            panel.add(titleLabel, BorderLayout.NORTH);
        }

        return panel;
    }

    private JPanel createAestheticTextAreaPanel(String title) {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(CARD_BG);
        panel.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true), // Black/white border
                new EmptyBorder(0, 0, 0, 0)));

        // Simple header
        JLabel headerLabel = new JLabel(title, JLabel.CENTER);
        headerLabel.setFont(new Font("SF Pro Text", Font.BOLD, 14));
        headerLabel.setForeground(TEXT_COLOR);
        headerLabel.setBackground(new Color(60, 60, 65));
        headerLabel.setOpaque(true);
        headerLabel.setBorder(new EmptyBorder(10, 0, 10, 0));

        panel.add(headerLabel, BorderLayout.NORTH);
        return panel;
    }

    private void styleAestheticComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        comboBox.setBackground(new Color(50, 50, 55));
        comboBox.setForeground(Color.BLACK); // Black text for contrast
        comboBox.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true), // Black/white border
                new EmptyBorder(8, 12, 8, 12)));

        // Custom renderer for black text in dropdown
        comboBox.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected,
                        cellHasFocus);
                label.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
                label.setForeground(Color.BLACK); // Black text in dropdown
                label.setBackground(isSelected ? PRIMARY_COLOR : Color.WHITE);
                return label;
            }
        });
    }

    private void styleAestheticTextField(JTextField textField) {
        textField.setFont(new Font("SF Pro Text", Font.PLAIN, 13));
        textField.setBackground(new Color(50, 50, 55));
        textField.setForeground(Color.WHITE);
        textField.setCaretColor(Color.WHITE);
        textField.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true), // Black/white border
                new EmptyBorder(8, 12, 8, 12)));
    }

    private void styleAestheticTextArea(JTextArea textArea) {
        textArea.setFont(new Font("SF Mono", Font.PLAIN, 13));
        textArea.setBackground(TEXT_AREA_BG);
        textArea.setForeground(Color.BLACK);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(12, 12, 12, 12));
    }

    private void styleAestheticScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER_COLOR, 1)); // Black/white border
        scrollPane.getViewport().setBackground(TEXT_AREA_BG);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new AestheticScrollBarUI());
        JScrollBar horizontalScrollBar = scrollPane.getHorizontalScrollBar();
        horizontalScrollBar.setUI(new AestheticScrollBarUI());
    }

    private void styleMainScrollPane(JScrollPane scrollPane) {
        scrollPane.getVerticalScrollBar().setUI(new AestheticScrollBarUI());
        scrollPane.getHorizontalScrollBar().setUI(new AestheticScrollBarUI());
    }

    private void styleAestheticRadioButton(JRadioButton radioButton) {
        radioButton.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        radioButton.setForeground(TEXT_COLOR);
        radioButton.setOpaque(false);
        radioButton.setFocusPainted(false);
    }

    private void styleAestheticButton(JButton button, Color backgroundColor) {
        button.setFont(new Font("SF Pro Text", Font.BOLD, 13));
        button.setForeground(BUTTON_TEXT_COLOR); // BLACK TEXT
        button.setBackground(backgroundColor);
        button.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_COLOR, 1, true), // Black/white border
                new EmptyBorder(10, 15, 10, 15)));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Subtle hover effect
        addAestheticHoverEffect(button, backgroundColor.brighter());
    }

    private void addAestheticHoverEffect(JButton button, Color hoverColor) {
        Color originalColor = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
            }
        });
    }

    private Image createDefaultIcon() {
        BufferedImage image = new BufferedImage(48, 48, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw a clean lock icon
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillRoundRect(12, 18, 24, 18, 5, 5);
        g2d.fillRect(18, 12, 12, 12);

        g2d.setColor(Color.WHITE);
        g2d.fillOval(21, 24, 6, 6);

        g2d.dispose();
        return image;
    }

    // Aesthetic ScrollBar UI
    static class AestheticScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(100, 100, 105);
            this.thumbDarkShadowColor = new Color(80, 80, 85);
            this.thumbLightShadowColor = new Color(120, 120, 125);
            this.trackColor = new Color(45, 45, 48);
        }

        @Override
        protected JButton createDecreaseButton(int orientation) {
            return createInvisibleButton();
        }

        @Override
        protected JButton createIncreaseButton(int orientation) {
            return createInvisibleButton();
        }

        private JButton createInvisibleButton() {
            JButton button = new JButton();
            button.setPreferredSize(new Dimension(0, 0));
            button.setMinimumSize(new Dimension(0, 0));
            button.setMaximumSize(new Dimension(0, 0));
            return button;
        }

        @Override
        protected void paintThumb(Graphics g, JComponent c, Rectangle thumbBounds) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(thumbColor);
            g2.fillRoundRect(thumbBounds.x + 1, thumbBounds.y + 1,
                    thumbBounds.width - 2, thumbBounds.height - 2, 4, 4);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }

    // Functional methods (same logic, aesthetic presentation)
    private void updateKeyPanel() {
        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        if ("Substitution Cipher".equals(selectedCipher)) {
            cardLayout.show(keyPanel, "substitution");
        } else {
            cardLayout.show(keyPanel, "standard");
        }
        updateInputPanel();
    }

    private void updateInputPanel() {
        boolean isFileInput = fileInputRadio.isSelected();

        // Enable/disable text area and file fields
        inputTextArea.setEnabled(!isFileInput);
        inputFileField.setEnabled(isFileInput);
        outputFileField.setEnabled(isFileInput);
        browseInputButton.setEnabled(isFileInput);
        browseOutputButton.setEnabled(isFileInput);

        // Visual feedback
        Color bgColor = isFileInput ? new Color(240, 240, 240) : TEXT_AREA_BG;
        inputTextArea.setBackground(bgColor);

        // Clear input area when switching to file input
        if (isFileInput) {
            inputTextArea.setText("[File input mode selected. Use file browser above.]");
            inputTextArea.setForeground(Color.GRAY);
        } else {
            inputTextArea.setForeground(Color.BLACK);
            if (inputTextArea.getText().equals("[File input mode selected. Use file browser above.]")) {
                inputTextArea.setText("");
            }
        }
    }

    private void browseInputFile() {
        JFileChooser fileChooser = createStyledFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String inputPath = fileChooser.getSelectedFile().getAbsolutePath();
            inputFileField.setText(inputPath);

            // Always default output file to "output.txt" in the same folder
            String parentDir = new File(inputPath).getParent();
            outputFileField.setText(parentDir + File.separator + "output.txt");
        }
    }

    private void browseOutputFile() {
        JFileChooser fileChooser = createStyledFileChooser();
        int result = fileChooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            outputFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
        }
    }

    private JFileChooser createStyledFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            // Keep default look and feel
        }
        return fileChooser;
    }

    private void performEncryption() {
        try {
            // If file mode and no output path provided, default to output.txt
            if (fileInputRadio.isSelected()) {
                if (inputFileField.getText().trim().isEmpty()) {
                    throw new IOException("Please choose an input file first.");
                }
                if (outputFileField.getText().trim().isEmpty()) {
                    String parentDir = new File(inputFileField.getText()).getParent();
                    outputFileField.setText(parentDir + File.separator + "output.txt");
                }
            }

            String result = processCipher(true);
            outputTextArea.setText(result);

            if (fileInputRadio.isSelected()) {
                showSuccessMessage("File encrypted successfully!\nSaved at: " + outputFileField.getText(),
                        "Encryption Complete");
            }
        } catch (Exception ex) {
            showErrorMessage("Encryption Error: " + ex.getMessage(), "Encryption Failed");
        }
    }

    private void performDecryption() {
        try {
            // If file mode and no output path provided, default to output.txt
            if (fileInputRadio.isSelected()) {
                if (inputFileField.getText().trim().isEmpty()) {
                    throw new IOException("Please choose an input file first.");
                }
                if (outputFileField.getText().trim().isEmpty()) {
                    String parentDir = new File(inputFileField.getText()).getParent();
                    outputFileField.setText(parentDir + File.separator + "output.txt");
                }
            }

            String result = processCipher(false);
            outputTextArea.setText(result);

            if (fileInputRadio.isSelected()) {
                showSuccessMessage("File decrypted successfully!\nSaved at: " + outputFileField.getText(),
                        "Decryption Complete");
            }
        } catch (Exception ex) {
            showErrorMessage("Decryption Error: " + ex.getMessage(), "Decryption Failed");
        }
    }

    private void showSuccessMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showErrorMessage(String message, String title) {
        JOptionPane.showMessageDialog(this, message, title, JOptionPane.ERROR_MESSAGE);
    }

    private String processCipher(boolean encrypt) throws InvalidKeyException, IOException {
        Cipher cipher = createCipher();
        String input = getInput();
        int key = getKey();

        String result;
        if (encrypt) {
            result = cipher.encrypt(input, key);
        } else {
            result = cipher.decrypt(input, key);
        }

        // Save to file ONLY if file input mode is selected AND output path is set
        if (fileInputRadio.isSelected() && !outputFileField.getText().trim().isEmpty()) {
            FileHandler.writeFile(outputFileField.getText(), result);
            Logger.log(cipher.getName() + (encrypt ? " encrypted" : " decrypted") + " file to "
                    + outputFileField.getText());
            showSuccessMessage((encrypt ? "File encrypted" : "File decrypted") + " successfully!",
                    encrypt ? "Encryption Complete" : "Decryption Complete");
        }

        return result;
    }

    private Cipher createCipher() throws InvalidKeyException {
        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        switch (selectedCipher) {
            case "Caesar Cipher":
                return new CaesarCipher();
            case "XOR Cipher":
                return new XORCipher();
            case "Substitution Cipher":
                String mappingStr = mappingField.getText().toUpperCase().trim();
                if (mappingStr.length() != 26) {
                    throw new InvalidKeyException("Mapping must be exactly 26 letters");
                }
                Map<Character, Character> mapping = new HashMap<>();
                char ch = 'A';
                for (int i = 0; i < 26; i++, ch++) {
                    mapping.put(ch, mappingStr.charAt(i));
                }
                return new SubstitutionCipher(mapping);
            default:
                throw new InvalidKeyException("Unknown cipher selected");
        }
    }

    private String getInput() throws IOException {
        if (fileInputRadio.isSelected()) {
            return FileHandler.readFile(inputFileField.getText());
        } else {
            return inputTextArea.getText();
        }
    }

    private int getKey() throws InvalidKeyException {
        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        if ("Substitution Cipher".equals(selectedCipher)) {
            return 0; // Not used for substitution cipher
        }
        String txt = keyField.getText().trim();
        if (txt.isEmpty()) {
            throw new InvalidKeyException("Key must not be empty");
        }
        try {
            return Integer.parseInt(txt);
        } catch (NumberFormatException e) {
            throw new InvalidKeyException("Key must be a valid integer");
        }
    }

    private void copyOutputToClipboard() {
        String output = outputTextArea.getText();
        if (!output.isEmpty()) {
            java.awt.datatransfer.StringSelection selection = new java.awt.datatransfer.StringSelection(output);
            java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(selection, null);
            showSuccessMessage("Output copied to clipboard!", "Copy Successful");
        }
    }

    private void clearAll() {
        inputTextArea.setText("");
        outputTextArea.setText("");
        keyField.setText("");
        mappingField.setText("");
        inputFileField.setText("");
        outputFileField.setText("");
        textInputRadio.setSelected(true);
        updateInputPanel();
    }

    /**
     * Generate an output file name in the same folder as inputPath using the given
     * suffix.
     * Ensures the generated file doesn't overwrite an existing file by adding _1,
     * _2, etc.
     *
     * Examples:
     * inputPath = /home/me/secret.txt, suffix = _encrypted
     * -> /home/me/secret_encrypted.txt
     */
    private String generateOutputFileName(String inputPath, String suffix) {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            // fallback
            return "output" + suffix + ".txt";
        }
        File inputFile = new File(inputPath);
        String parent = inputFile.getParent();
        if (parent == null)
            parent = "."; // current dir fallback
        String name = inputFile.getName();

        int dot = name.lastIndexOf('.');
        String base = (dot > 0) ? name.substring(0, dot) : name;
        String ext = (dot > 0) ? name.substring(dot) : "";

        String candidate = base + suffix + ext;
        File outFile = new File(parent, candidate);

        int count = 1;
        while (outFile.exists()) {
            candidate = base + suffix + "_" + count + ext;
            outFile = new File(parent, candidate);
            count++;
        }

        return outFile.getAbsolutePath();
    }

    public static void main(String[] args) {
        // Set modern look and feel with iOS-inspired settings
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // iOS-style UI defaults
            UIManager.put("OptionPane.background", new Color(28, 28, 30));
            UIManager.put("Panel.background", new Color(28, 28, 30));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("OptionPane.messageFont", new Font("SF Pro Text", Font.PLAIN, 13));
        } catch (Exception e) {
            try {
                UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

        SwingUtilities.invokeLater(() -> {
            MainAppUI app = new MainAppUI();
            app.setVisible(true);
        });
    }
}
