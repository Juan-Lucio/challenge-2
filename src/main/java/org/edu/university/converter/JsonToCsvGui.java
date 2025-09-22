package org.edu.university.converter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * JsonToCsvGui - Simple and practical Swing UI for the JSON -> CSV converter.
 *
 * Features:
 * - Browse input JSON
 * - Browse/select output CSV
 * - Delimiter input
 * - Overwrite output (deletes output before conversion if checked)
 * - Run conversion in background using SwingWorker
 * - Log area and progress indicator
 */
public class JsonToCsvGui extends JFrame {

    private final JTextField inputField = new JTextField(40);
    private final JButton browseInputBtn = new JButton("Browse...");
    private final JTextField outputField = new JTextField(40);
    private final JButton browseOutputBtn = new JButton("Browse...");
    private final JTextField delimiterField = new JTextField(1);
    private final JCheckBox overwriteCheck = new JCheckBox("Overwrite if exists");
    private final JButton convertBtn = new JButton("Convert");
    private final JTextArea logArea = new JTextArea(10, 60);
    private final JProgressBar progressBar = new JProgressBar();

    public JsonToCsvGui() {
        super("JSON → CSV Converter (GUI)");
        initComponents();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null); // center
    }

    private void initComponents() {
        // Layout
        JPanel main = new JPanel(new BorderLayout(8, 8));
        JPanel form = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.insets = new Insets(6, 6, 6, 6);
        c.anchor = GridBagConstraints.WEST;

        // Input row
        c.gridx = 0; c.gridy = 0;
        form.add(new JLabel("Input JSON:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        form.add(inputField, c);
        c.gridx = 2; c.fill = GridBagConstraints.NONE;
        form.add(browseInputBtn, c);

        // Output row
        c.gridx = 0; c.gridy = 1;
        form.add(new JLabel("Output CSV:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.HORIZONTAL;
        form.add(outputField, c);
        c.gridx = 2; c.fill = GridBagConstraints.NONE;
        form.add(browseOutputBtn, c);

        // Delimiter and overwrite
        c.gridx = 0; c.gridy = 2;
        form.add(new JLabel("Delimiter:"), c);
        c.gridx = 1; c.fill = GridBagConstraints.NONE;
        JPanel small = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0));
        delimiterField.setText(","); // default
        delimiterField.setColumns(1);
        small.add(delimiterField);
        small.add(new JLabel("(e.g. , ; \\t)"));
        small.add(overwriteCheck);
        form.add(small, c);

        // Convert button and progress bar
        c.gridx = 0; c.gridy = 3; c.gridwidth = 3;
        c.fill = GridBagConstraints.HORIZONTAL;
        JPanel bottom = new JPanel(new BorderLayout(6, 6));
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnPanel.add(convertBtn);
        bottom.add(btnPanel, BorderLayout.EAST);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        bottom.add(progressBar, BorderLayout.CENTER);
        form.add(bottom, c);

        // Log area
        logArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(logArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        main.add(form, BorderLayout.NORTH);
        main.add(scroll, BorderLayout.CENTER);

        getContentPane().add(main);

        // Listeners
        browseInputBtn.addActionListener(this::onBrowseInput);
        browseOutputBtn.addActionListener(this::onBrowseOutput);
        convertBtn.addActionListener(this::onConvert);
    }

    private void onBrowseInput(ActionEvent ev) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select input JSON file");
        chooser.setFileFilter(new FileNameExtensionFilter("JSON files", "json"));
        int res = chooser.showOpenDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            inputField.setText(f.getAbsolutePath());
            appendLog("Selected input: " + f.getAbsolutePath());
            // Suggest an output file name if none provided
            if (outputField.getText().isBlank()) {
                String defaultOut = "exports" + File.separator + f.getName().replaceAll("\\.json$", "") + ".csv";
                outputField.setText(defaultOut);
                appendLog("Suggested output: " + defaultOut);
            }
        }
    }

    private void onBrowseOutput(ActionEvent ev) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select output CSV file");
        chooser.setSelectedFile(new File("output.csv"));
        int res = chooser.showSaveDialog(this);
        if (res == JFileChooser.APPROVE_OPTION) {
            File f = chooser.getSelectedFile();
            outputField.setText(f.getAbsolutePath());
            appendLog("Selected output: " + f.getAbsolutePath());
        }
    }

    private void onConvert(ActionEvent ev) {
        String inputPath = inputField.getText().trim();
        String outputPath = outputField.getText().trim();
        String delimText = delimiterField.getText();

        // Validate basic fields
        if (inputPath.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please provide an input JSON file.", "Input required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (outputPath.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please provide an output CSV path.", "Output required",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        char delimiter = ',';
        if (delimText != null && !delimText.isBlank()) {
            delimiter = delimText.charAt(0);
            if (delimiter == '\\') { // if user types \t
                if (delimText.length() >= 2 && delimText.charAt(1) == 't') delimiter = '\t';
            }
        }

        // Validate JSON file (use FileValidator for a quick pre-check)
        if (!FileValidator.isValidJsonFile(inputPath)) {
            appendLog("Validation failed: input file does not appear to be a valid .json -> " + inputPath);
            // Offer a more complete validation with messages
            boolean ok = FileValidator.validateJsonFile(inputPath);
            if (!ok) {
                JOptionPane.showMessageDialog(this,
                        "Input JSON validation failed. Check console/logs for details.",
                        "Validation error", JOptionPane.ERROR_MESSAGE);
                return;
            }
        }

        // Check output existence and overwrite behavior
        Path outPath = Path.of(outputPath);
        boolean overwrite = overwriteCheck.isSelected();
        if (Files.exists(outPath)) {
            if (!overwrite) {
                int ans = JOptionPane.showConfirmDialog(this,
                        "Output file already exists. Do you want to overwrite it?",
                        "File exists", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
                if (ans != JOptionPane.YES_OPTION) {
                    appendLog("Conversion canceled by user (output exists).");
                    return;
                }
            }
        }

        // Disable UI while running
        setUiEnabled(false);
        progressBar.setIndeterminate(true);
        progressBar.setVisible(true);
        progressBar.setString("Converting...");

        // Run conversion in background
        char finalDelimiter = delimiter;
        SwingWorker<Integer, String> worker = new SwingWorker<>() {
            @Override
            protected Integer doInBackground() {
                try {
                    // If overwrite selected and file exists -> delete it so CsvWriter2 can create new (it uses CREATE_NEW)
                    if (overwrite && Files.exists(outPath)) {
                        publish("Overwriting enabled: deleting existing file: " + outPath.toAbsolutePath());
                        Files.delete(outPath);
                    }

                    publish("Starting conversion...");
                    JsonToCsvConverter converter = new JsonToCsvConverter();
                    int rows = converter.convert(inputPath, outputPath, finalDelimiter);
                    publish("Conversion completed. Rows processed: " + rows);
                    return rows;
                } catch (Exception ex) {
                    publish("ERROR: " + ex.getMessage());
                    ex.printStackTrace();
                    cancel(true);
                    return null;
                }
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                for (String s : chunks) appendLog(s);
            }

            @Override
            protected void done() {
                progressBar.setIndeterminate(false);
                progressBar.setVisible(false);
                setUiEnabled(true);
                try {
                    if (!isCancelled()) {
                        Integer rows = get();
                        appendLog("✅ Finished successfully. Rows: " + rows);
                        JOptionPane.showMessageDialog(JsonToCsvGui.this,
                                "Conversion succeeded. Rows processed: " + rows,
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        appendLog("Conversion failed or canceled. See logs above.");
                        JOptionPane.showMessageDialog(JsonToCsvGui.this,
                                "Conversion failed. Check logs for details.",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (Exception e) {
                    appendLog("Unexpected error finishing conversion: " + e.getMessage());
                    JOptionPane.showMessageDialog(JsonToCsvGui.this,
                            "Unexpected error: " + e.getMessage(),
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        };

        worker.execute();
    }

    private void setUiEnabled(boolean enabled) {
        inputField.setEnabled(enabled);
        browseInputBtn.setEnabled(enabled);
        outputField.setEnabled(enabled);
        browseOutputBtn.setEnabled(enabled);
        delimiterField.setEnabled(enabled);
        overwriteCheck.setEnabled(enabled);
        convertBtn.setEnabled(enabled);
    }

    private void appendLog(String text) {
        SwingUtilities.invokeLater(() -> {
            logArea.append(text + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        });
    }

    public static void main(String[] args) {
        // Optional: Use system look & feel
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ignored) { }

        SwingUtilities.invokeLater(() -> {
            JsonToCsvGui gui = new JsonToCsvGui();

            // If args provided, prefill fields (simple support)
            if (args.length >= 1) gui.inputField.setText(args[0]);
            if (args.length >= 2) gui.outputField.setText(args[1]);

            gui.setVisible(true);
        });
    }
}
