package com.config;

import com.intellij.openapi.components.ApplicationComponent;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.options.ConfigurationException;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Configuration component for decompiler.
 */
@State(
        name = PluginConfigurationPane.COMPONENT_NAME,
        storages = {@Storage(id = "other", file = "$APP_CONFIG$/jblab.decompiler.xml")}
)
public class PluginComponent implements ApplicationComponent, Configurable, PersistentStateComponent<Element> {
    public static final String SHOW_PRETTY_ATTRIBUTE = "displayPrettyEnabled";
    public static final String CHOOSE_LANGUAGE_ATTRIBUTE = "chooseLanguage";
    public static final String SHOW_TAB_SIZE = "displayTabSize";
    public static final String SHOW_TEXT_WIDTH = "displayTextWidth";
    public static final String CONFIGURATION_CONFIG_ELEMENT = "configuration";
    public static final String DEFAULT_LANGUAGE = "Java";

    private PluginConfigurationPane configPane;
    private boolean showPrettyEnabled;

    private String chosenLanguage;
    private Integer textWidth, tabSize;

    public void initComponent() {
        // nop
    }

    public void disposeComponent() {
        // nop
    }

    @NotNull
    public String getComponentName() {
        return "JBLab Decompiler Plugin";
    }

    @Nls
    @Override
    public String getDisplayName() {
        return "JBLab Decompiler";
    }

    @Nullable
    @Override
    public String getHelpTopic() {
        return null;
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        if (configPane == null) {
            configPane = new PluginConfigurationPane();
        }
        return configPane.getRootPane();
    }

    @Override
    public boolean isModified() {
        return configPane != null && configPane.isModified(this);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (configPane != null) {
            configPane.storeDataTo(this);
        }
    }

    @Override
    public void reset() {
        if (configPane != null) {
            configPane.readDataFrom(this);
        }
    }

    @Override
    public void disposeUIResources() {
        configPane = null;
    }

    public String getChosenLanguage() {
        return chosenLanguage;
    }

    public Integer getTextWidth() {
        return textWidth;
    }

    public Integer getTabSize() {
        return tabSize;
    }

    public void setTextWidth(Integer textWidth) {
        this.textWidth = textWidth;
    }

    public void setTabSize(Integer tabSize) {
        this.tabSize = tabSize;
    }

    public void setChosenLanguage(String language) {
        this.chosenLanguage = language;
    }

    public boolean isShowPrettyEnabled() {
        return showPrettyEnabled;
    }

    public void setShowPrettyEnabled(boolean showMetadataEnabled) {
        this.showPrettyEnabled = showMetadataEnabled;
    }

    @Override
    public Element getState() {
        Element configuration = new Element(CONFIGURATION_CONFIG_ELEMENT);
        configuration.setAttribute(CHOOSE_LANGUAGE_ATTRIBUTE, String.valueOf(chosenLanguage));
        configuration.setAttribute(SHOW_PRETTY_ATTRIBUTE, String.valueOf(showPrettyEnabled));
        configuration.setAttribute(SHOW_TAB_SIZE, String.valueOf(tabSize));
        configuration.setAttribute(SHOW_TEXT_WIDTH, String.valueOf(textWidth));
        return configuration;
    }

    @Override
    public void loadState(Element configuration) {
        String temp = configuration.getAttributeValue(CHOOSE_LANGUAGE_ATTRIBUTE);
        chosenLanguage = temp.equals("null") ? DEFAULT_LANGUAGE : temp;
        temp = configuration.getAttributeValue(SHOW_TAB_SIZE);
        tabSize = temp.equals("null") ? 4 : Integer.valueOf(temp);
        temp = configuration.getAttributeValue(SHOW_TEXT_WIDTH);
        textWidth = temp.equals("null") ? 100 : Integer.valueOf(temp);

        String showMetadataStr = configuration.getAttributeValue(SHOW_PRETTY_ATTRIBUTE);
        if (StringUtils.isNotBlank(showMetadataStr)) {
            showPrettyEnabled = Boolean.valueOf(showMetadataStr);
        }
    }

}
