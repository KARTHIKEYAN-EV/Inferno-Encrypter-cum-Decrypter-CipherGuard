package main;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage; // Added this import
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
    
    // Color scheme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);
    private final Color ACCENT_COLOR = new Color(46, 204, 113);
    private final Color DANGER_COLOR = new Color(231, 76, 60);
    private final Color WARNING_COLOR = new Color(241, 196, 15);
    private final Color DARK_BG = new Color(44, 62, 80);
    private final Color LIGHT_BG = new Color(236, 240, 241);
    private final Color TEXT_AREA_BG = new Color(253, 254, 254);
    
    // Gradients
    private GradientPaint primaryGradient;
    private GradientPaint secondaryGradient;

    public MainAppUI() {
        initializeUI();
    }

    private void initializeUI() {
        setTitle("🔐 Advanced Encryption Tool");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);
        
        // Set application icon
        setIconImage(createDefaultIcon());

        // Create main panel with gradient background
        JPanel mainPanel = new JPanel(new BorderLayout(15, 15)) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gradient = new GradientPaint(0, 0, new Color(52, 73, 94), getWidth(), getHeight(), new Color(44, 62, 80));
                g2d.setPaint(gradient);
                g2d.fillRect(0, 0, getWidth(), getHeight());
            }
        };
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        // Add header with animated title
        mainPanel.add(createAnimatedHeader(), BorderLayout.NORTH);
        
        // Add center content
        mainPanel.add(createCenterPanel(), BorderLayout.CENTER);
        
        // Add button panel
        mainPanel.add(createStyledButtonPanel(), BorderLayout.SOUTH);

        add(mainPanel);
    }

    private JPanel createAnimatedHeader() {
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);
        
        // Animated title
        JLabel titleLabel = new JLabel("🔐 Advanced Encryption Tool", JLabel.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(new EmptyBorder(0, 0, 15, 0));
        
        // Add subtle animation
        Timer titleTimer = new Timer(1000, e -> {
            String text = titleLabel.getText();
            if (text.contains("🔐")) {
                titleLabel.setText("🔒 Advanced Encryption Tool");
            } else {
                titleLabel.setText("🔐 Advanced Encryption Tool");
            }
        });
        titleTimer.start();
        
        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(createSettingsPanel(), BorderLayout.CENTER);
        
        return headerPanel;
    }

    private JPanel createSettingsPanel() {
        JPanel settingsPanel = new JPanel(new GridLayout(2, 2, 15, 15));
        settingsPanel.setOpaque(false);
        settingsPanel.setBorder(BorderFactory.createCompoundBorder(
            new TitledBorder(createStyledBorder(PRIMARY_COLOR), "⚙️ Cipher Settings", 
                TitledBorder.CENTER, TitledBorder.TOP, 
                new Font("Segoe UI", Font.BOLD, 14), Color.WHITE),
            new EmptyBorder(10, 10, 10, 10)
        ));

        // Cipher selection with styled combo box
        JPanel cipherPanel = createStyledPanel("Select Cipher:");
        String[] ciphers = {"🔒 Caesar Cipher", "🔑 XOR Cipher", "🔠 Substitution Cipher"};
        cipherComboBox = new JComboBox<>(ciphers);
        styleComboBox(cipherComboBox);
        cipherComboBox.addActionListener(e -> updateKeyPanel());
        cipherPanel.add(cipherComboBox);

        // Key panel with card layout
        cardLayout = new CardLayout();
        keyPanel = new JPanel(cardLayout);
        keyPanel.setOpaque(false);
        
        // Standard key panel
        JPanel standardKeyPanel = createStyledPanel("Key (integer):");
        keyField = new JTextField(10);
        styleTextField(keyField);
        standardKeyPanel.add(keyField);
        
        // Substitution key panel
        JPanel substitutionKeyPanel = createStyledPanel("Mapping (26 letters A-Z):");
        mappingField = new JTextField(26);
        styleTextField(mappingField);
        mappingField.setToolTipText("Enter 26 unique letters representing A-Z mapping");
        substitutionKeyPanel.add(mappingField);
        
        keyPanel.add(standardKeyPanel, "standard");
        keyPanel.add(substitutionKeyPanel, "substitution");

        settingsPanel.add(cipherPanel);
        settingsPanel.add(keyPanel);
        
        // Input type selection
        JPanel inputTypePanel = createStyledPanel("Input Type:");
        ButtonGroup inputGroup = new ButtonGroup();
        textInputRadio = new JRadioButton("📝 Text Input", true);
        fileInputRadio = new JRadioButton("📁 File Input");
        styleRadioButton(textInputRadio);
        styleRadioButton(fileInputRadio);
        inputGroup.add(textInputRadio);
        inputGroup.add(fileInputRadio);
        
        textInputRadio.addActionListener(e -> updateInputPanel());
        fileInputRadio.addActionListener(e -> updateInputPanel());
        
        inputTypePanel.add(textInputRadio);
        inputTypePanel.add(fileInputRadio);

        // File panel
        JPanel filePanel = createStyledPanel("File Operations");
        filePanel.setLayout(new GridLayout(2, 2, 8, 8));
        
        inputFileField = new JTextField();
        styleTextField(inputFileField);
        browseInputButton = new JButton("📂 Browse");
        styleButton(browseInputButton, SECONDARY_COLOR);
        browseInputButton.addActionListener(e -> browseInputFile());
        
        outputFileField = new JTextField();
        styleTextField(outputFileField);
        browseOutputButton = new JButton("💾 Save As");
        styleButton(browseOutputButton, SECONDARY_COLOR);
        browseOutputButton.addActionListener(e -> browseOutputFile());
        
        filePanel.add(createLabel("Input File:"));
        filePanel.add(inputFileField);
        filePanel.add(browseInputButton);
        
        filePanel.add(createLabel("Output File:"));
        filePanel.add(outputFileField);
        filePanel.add(browseOutputButton);

        settingsPanel.add(inputTypePanel);
        settingsPanel.add(filePanel);

        return settingsPanel;
    }

    private JPanel createCenterPanel() {
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(new EmptyBorder(15, 0, 15, 0));

        // Input panel with gradient header
        JPanel inputPanel = createTextAreaPanel("📥 Input", true);
        inputTextArea = new JTextArea(15, 35);
        styleTextArea(inputTextArea);
        JScrollPane inputScroll = new JScrollPane(inputTextArea);
        styleScrollPane(inputScroll);
        inputPanel.add(inputScroll, BorderLayout.CENTER);

        // Output panel with gradient header
        JPanel outputPanel = createTextAreaPanel("📤 Output", false);
        outputTextArea = new JTextArea(15, 35);
        styleTextArea(outputTextArea);
        outputTextArea.setEditable(false);
        JScrollPane outputScroll = new JScrollPane(outputTextArea);
        styleScrollPane(outputScroll);
        
        // Add copy button for output
        JButton copyButton = new JButton("📋 Copy Output");
        styleButton(copyButton, WARNING_COLOR);
        copyButton.addActionListener(e -> copyOutputToClipboard());
        
        JPanel outputButtonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        outputButtonPanel.setOpaque(false);
        outputButtonPanel.add(copyButton);
        
        outputPanel.add(outputScroll, BorderLayout.CENTER);
        outputPanel.add(outputButtonPanel, BorderLayout.SOUTH);

        centerPanel.add(inputPanel);
        centerPanel.add(outputPanel);

        return centerPanel;
    }

    private JPanel createStyledButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 25, 15));
        buttonPanel.setOpaque(false);
        buttonPanel.setBorder(new EmptyBorder(10, 0, 0, 0));

        encryptButton = new JButton("🚀 Encrypt");
        styleButton(encryptButton, ACCENT_COLOR);
        encryptButton.addActionListener(e -> performEncryption());

        decryptButton = new JButton("🔓 Decrypt");
        styleButton(decryptButton, PRIMARY_COLOR);
        decryptButton.addActionListener(e -> performDecryption());

        JButton clearButton = new JButton("✨ Clear All");
        styleButton(clearButton, WARNING_COLOR);
        clearButton.addActionListener(e -> clearAll());

        JButton exitButton = new JButton("❌ Exit");
        styleButton(exitButton, DANGER_COLOR);
        exitButton.addActionListener(e -> System.exit(0));

        // Add hover effects
        addHoverEffect(encryptButton, ACCENT_COLOR.brighter());
        addHoverEffect(decryptButton, SECONDARY_COLOR);
        addHoverEffect(clearButton, WARNING_COLOR.brighter());
        addHoverEffect(exitButton, DANGER_COLOR.brighter());

        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);
        buttonPanel.add(clearButton);
        buttonPanel.add(exitButton);

        return buttonPanel;
    }

    // Styling helper methods
    private JPanel createStyledPanel(String title) {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        panel.setOpaque(false);
        if (title != null) {
            panel.add(createLabel(title));
        }
        return panel;
    }

    private JLabel createLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return label;
    }

    private JPanel createTextAreaPanel(String title, boolean isInput) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        // Create gradient header
        JLabel headerLabel = new JLabel(title, JLabel.CENTER);
        headerLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        headerLabel.setForeground(Color.WHITE);
        headerLabel.setOpaque(true);
        headerLabel.setBackground(isInput ? PRIMARY_COLOR : ACCENT_COLOR);
        headerLabel.setBorder(new EmptyBorder(8, 0, 8, 0));
        
        panel.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(isInput ? PRIMARY_COLOR : ACCENT_COLOR, 2, true),
            new EmptyBorder(5, 5, 5, 5)
        ));
        
        panel.add(headerLabel, BorderLayout.NORTH);
        return panel;
    }

    private void styleComboBox(JComboBox<String> comboBox) {
        comboBox.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(PRIMARY_COLOR, 1, true),
            new EmptyBorder(5, 8, 5, 8)
        ));
    }

    private void styleTextField(JTextField textField) {
        textField.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        textField.setBackground(TEXT_AREA_BG);
        textField.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(SECONDARY_COLOR, 1, true),
            new EmptyBorder(6, 8, 6, 8)
        ));
    }

    private void styleTextArea(JTextArea textArea) {
        textArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        textArea.setBackground(TEXT_AREA_BG);
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setBorder(new EmptyBorder(10, 10, 10, 10));
    }

    private void styleScrollPane(JScrollPane scrollPane) {
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
        scrollPane.getViewport().setBackground(TEXT_AREA_BG);
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new CustomScrollBarUI());
    }

    private void styleRadioButton(JRadioButton radioButton) {
        radioButton.setFont(new Font("Segoe UI", Font.BOLD, 12));
        radioButton.setForeground(Color.WHITE);
        radioButton.setOpaque(false);
        radioButton.setFocusPainted(false);
    }

    private void styleButton(JButton button, Color color) {
        button.setFont(new Font("Segoe UI", Font.BOLD, 14));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(color.darker(), 2, true),
            new EmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    private void addHoverEffect(JButton button, Color hoverColor) {
        Color originalColor = button.getBackground();
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(hoverColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(hoverColor.darker(), 2, true),
                    new EmptyBorder(10, 20, 10, 20)
                ));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(originalColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(originalColor.darker(), 2, true),
                    new EmptyBorder(10, 20, 10, 20)
                ));
            }
        });
    }

    private Border createStyledBorder(Color color) {
        return BorderFactory.createLineBorder(color, 2, true);
    }

    private Image createDefaultIcon() {
        // Create a simple icon programmatically
        BufferedImage image = new BufferedImage(32, 32, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Draw a lock icon
        g2d.setColor(PRIMARY_COLOR);
        g2d.fillRoundRect(8, 12, 16, 12, 4, 4);
        g2d.fillRect(12, 8, 8, 8);
        g2d.setColor(Color.WHITE);
        g2d.fillOval(14, 16, 4, 4);
        
        g2d.dispose();
        return image;
    }

    // Custom ScrollBar UI for better appearance
    static class CustomScrollBarUI extends javax.swing.plaf.basic.BasicScrollBarUI {
        @Override
        protected void configureScrollBarColors() {
            this.thumbColor = new Color(41, 128, 185);
            this.trackColor = new Color(240, 240, 240);
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
            g2.fillRoundRect(thumbBounds.x + 2, thumbBounds.y + 2, 
                           thumbBounds.width - 4, thumbBounds.height - 4, 6, 6);
        }

        @Override
        protected void paintTrack(Graphics g, JComponent c, Rectangle trackBounds) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(trackColor);
            g2.fillRect(trackBounds.x, trackBounds.y, trackBounds.width, trackBounds.height);
        }
    }

    // Original functional methods (unchanged but now using styled components)
    private void updateKeyPanel() {
        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        if ("🔠 Substitution Cipher".equals(selectedCipher)) {
            cardLayout.show(keyPanel, "substitution");
        } else {
            cardLayout.show(keyPanel, "standard");
        }
        updateInputPanel();
    }

    private void updateInputPanel() {
        boolean isFileInput = fileInputRadio.isSelected();
        inputTextArea.setEnabled(!isFileInput);
        inputFileField.setEnabled(isFileInput);
        outputFileField.setEnabled(isFileInput);
        browseInputButton.setEnabled(isFileInput);
        browseOutputButton.setEnabled(isFileInput);
    }

    private void browseInputFile() {
        JFileChooser fileChooser = createStyledFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            inputFileField.setText(fileChooser.getSelectedFile().getAbsolutePath());
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
            String result = processCipher(true);
            outputTextArea.setText(result);
            if (fileInputRadio.isSelected()) {
                showSuccessMessage("🎉 File encrypted successfully!", "Encryption Complete");
            }
        } catch (Exception ex) {
            showErrorMessage("❌ Error: " + ex.getMessage(), "Encryption Error");
        }
    }

    private void performDecryption() {
        try {
            String result = processCipher(false);
            outputTextArea.setText(result);
            if (fileInputRadio.isSelected()) {
                showSuccessMessage("🎉 File decrypted successfully!", "Decryption Complete");
            }
        } catch (Exception ex) {
            showErrorMessage("❌ Error: " + ex.getMessage(), "Decryption Error");
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
            if (fileInputRadio.isSelected()) {
                FileHandler.writeFile(outputFileField.getText(), result);
                Logger.log(cipher.getName() + " encrypted file to " + outputFileField.getText());
            }
        } else {
            result = cipher.decrypt(input, key);
            if (fileInputRadio.isSelected()) {
                FileHandler.writeFile(outputFileField.getText(), result);
                Logger.log(cipher.getName() + " decrypted file to " + outputFileField.getText());
            }
        }
        
        return result;
    }

    private Cipher createCipher() throws InvalidKeyException {
        String selectedCipher = (String) cipherComboBox.getSelectedItem();
        switch (selectedCipher) {
            case "🔒 Caesar Cipher":
                return new CaesarCipher();
            case "🔑 XOR Cipher":
                return new XORCipher();
            case "🔠 Substitution Cipher":
                String mappingStr = mappingField.getText().toUpperCase();
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
        if ("🔠 Substitution Cipher".equals(selectedCipher)) {
            return 0; // Not used for substitution cipher
        }
        try {
            return Integer.parseInt(keyField.getText());
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
            showSuccessMessage("📋 Output copied to clipboard!", "Copy Successful");
        }
    }

    private void clearAll() {
        inputTextArea.setText("");
        outputTextArea.setText("");
        keyField.setText("");
        mappingField.setText("");
        inputFileField.setText("");
        outputFileField.setText("");
    }

    public static void main(String[] args) {
        // Set modern look and feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // Customize UI defaults for better appearance
            UIManager.put("OptionPane.background", new Color(44, 62, 80));
            UIManager.put("Panel.background", new Color(44, 62, 80));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
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