package com.config;

import com.decompiler.Language;
import com.intellij.ui.HyperlinkLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.text.PlainDocument;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PluginConfigurationPane {
    protected static final String COMPONENT_NAME = "PluginConfiguration";
    private JCheckBox myShowPrettyCheckBox;
    private JPanel myContentPane;
    private HyperlinkLabel myHyperlinkLabel;
    private JComboBox myLanguageComboBox;
    private JTextField myTabSizeField;
    private JTextField myTextWidthField;
    private Language mySelectedLanguage;


    public PluginConfigurationPane() {
        final String[] languages = {"Java", "JavaScript", "Kotlin"};
        myLanguageComboBox.insertItemAt(languages[0], 0);
        myLanguageComboBox.insertItemAt(languages[1], 1);
        myLanguageComboBox.insertItemAt(languages[2], 2);
        ActionListener actionListener = new ActionListener() {
            @Override
            public void actionPerformed(@NotNull ActionEvent e) {
                if (e.getSource() instanceof JComboBox) {
                    mySelectedLanguage = new Language((String) ((JComboBox) e.getSource()).getSelectedItem());
                    if (mySelectedLanguage.getName().equals("JavaScript")) {
                        myShowPrettyCheckBox.setEnabled(false);
                        myShowPrettyCheckBox.setSelected(true);
                    } else {
                        myShowPrettyCheckBox.setEnabled(true);
                    }
                }
                if (myShowPrettyCheckBox.isSelected()) {
                    myTabSizeField.setEditable(true);
                    myTextWidthField.setEditable(true);
                } else {
                    myTabSizeField.setEditable(false);
                    myTextWidthField.setEditable(false);
                }
            }
        };
        myShowPrettyCheckBox.addActionListener(actionListener);
        myLanguageComboBox.addActionListener(actionListener);
        PlainDocument doc1 = (PlainDocument) myTabSizeField.getDocument();
        doc1.setDocumentFilter(new DocFilter());
        PlainDocument doc2 = (PlainDocument) myTextWidthField.getDocument();
        doc2.setDocumentFilter(new DocFilter());
    }

    public void storeDataTo(final PluginConfigComponent pluginComponent) {
        pluginComponent.setChosenLanguage(mySelectedLanguage);
        pluginComponent.setShowPrettyEnabled(myShowPrettyCheckBox.isSelected());
        pluginComponent.setTabSize(Integer.valueOf(myTabSizeField.getText()));
        pluginComponent.setTextWidth(Integer.valueOf(myTextWidthField.getText()));
    }

    public void readDataFrom(final PluginConfigComponent pluginComponent) {
        myLanguageComboBox.setSelectedItem(pluginComponent.getChosenLanguage().getName());
        myShowPrettyCheckBox.setSelected(pluginComponent.isShowPrettyEnabled());
        myTabSizeField.setText(pluginComponent.getTabSize().toString());
        myTextWidthField.setText(pluginComponent.getTextWidth().toString());

        if (pluginComponent.isShowPrettyEnabled()) {
            myTabSizeField.setEditable(true);
            myTextWidthField.setEditable(true);
        } else {
            myTabSizeField.setEditable(false);
            myTextWidthField.setEditable(false);
        }
    }

    public boolean isModified(final PluginConfigComponent pluginComponent) {
        return !myLanguageComboBox.getSelectedItem().equals(pluginComponent.getChosenLanguage().getName())
                || myShowPrettyCheckBox.isSelected() != pluginComponent.isShowPrettyEnabled()
                || !myTabSizeField.getText().equals(pluginComponent.getTabSize().toString())
                || !myTextWidthField.getText().equals(pluginComponent.getTextWidth().toString());
    }

    public JPanel getRootPane() {
        return myContentPane;
    }

    private void createUIComponents() {
        final String link = "https://github.com/zabranskiy/djlab/";
        myHyperlinkLabel = new HyperlinkLabel();
        myHyperlinkLabel.setHyperlinkText(link);
        myHyperlinkLabel.setHyperlinkTarget(link);
    }
}
