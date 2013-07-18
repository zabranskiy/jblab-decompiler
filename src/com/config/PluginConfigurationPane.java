package com.config;

import com.intellij.ui.HyperlinkLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginConfigurationPane {
    protected static final String COMPONENT_NAME = "PluginConfiguration";
    private JCheckBox showPrettyCheckBox;
    private JPanel contentPane;
    private HyperlinkLabel hyperlinkLabel;
    private JComboBox comboBox1;
    private JTextField textField1;
    private JTextField textField2;
    private String selectedLanguage;


    public PluginConfigurationPane() {
        String[] languages = {"Java", "JavaScript", "C"};
        comboBox1.insertItemAt(languages[0], 0);
        comboBox1.insertItemAt(languages[1], 1);
//        comboBox1.insertItemAt(languages[2], 2);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(@NotNull ActionEvent e) {
                if (e.getSource() instanceof JComboBox) {
                    selectedLanguage = (String) ((JComboBox) e.getSource()).getSelectedItem();
                }
                if (showPrettyCheckBox.isSelected()) {
                    textField1.setEditable(true);
                    textField2.setEditable(true);
                } else {
                    textField1.setEditable(false);
                    textField2.setEditable(false);
                }
            }
        };
        showPrettyCheckBox.addActionListener(actionListener);
        comboBox1.addActionListener(actionListener);
        PlainDocument doc1 = (PlainDocument) textField1.getDocument();
        doc1.setDocumentFilter(new DocFilter());
        PlainDocument doc2 = (PlainDocument) textField2.getDocument();
        doc2.setDocumentFilter(new DocFilter());
    }

    public void storeDataTo(PluginComponent pluginComponent) {
        pluginComponent.setChosenLanguage(selectedLanguage);
        pluginComponent.setShowPrettyEnabled(showPrettyCheckBox.isSelected());
        pluginComponent.setTabSize(Integer.valueOf(textField1.getText()));
        pluginComponent.setTextWidth(Integer.valueOf(textField2.getText()));
    }

    public void readDataFrom(PluginComponent pluginComponent) {
        comboBox1.setSelectedItem(pluginComponent.getChosenLanguage());
        showPrettyCheckBox.setSelected(pluginComponent.isShowPrettyEnabled());
        textField1.setText(pluginComponent.getTabSize().toString());
        textField2.setText(pluginComponent.getTextWidth().toString());

        if (pluginComponent.isShowPrettyEnabled()) {
            textField1.setEditable(true);
            textField2.setEditable(true);
        } else {
            textField1.setEditable(false);
            textField2.setEditable(false);
        }
    }

    public boolean isModified(PluginComponent pluginComponent) {
        return !comboBox1.getSelectedItem().equals(pluginComponent.getChosenLanguage())
                || showPrettyCheckBox.isSelected() != pluginComponent.isShowPrettyEnabled()
                || !textField1.getText().equals(pluginComponent.getTabSize().toString())
                || !textField2.getText().equals(pluginComponent.getTextWidth().toString());
    }

    public JPanel getRootPane() {
        return contentPane;
    }

    private void createUIComponents() {
        final String link = "https://github.com/zabranskiy/djlab/";
        hyperlinkLabel = new HyperlinkLabel();
        hyperlinkLabel.setHyperlinkText(link);
        hyperlinkLabel.setHyperlinkTarget(link);
    }
}
