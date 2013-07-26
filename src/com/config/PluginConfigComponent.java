package com.config;

import com.decompiler.Language;
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
 * The component for the application-level configuration.
 */
@State(
        name = PluginConfigurationPane.COMPONENT_NAME,
        storages = {@Storage(id = "other", file = "$APP_CONFIG$/jblab.decompiler.xml")}
)
public class PluginConfigComponent implements ApplicationComponent, Configurable, PersistentStateComponent<Element> {
    private static final String COMPONENT_NAME = "JBLab Decompiler Plugin Config";
    private static final String DISPLAY_NAME = "JBLab Decompiler";
    private static final String CONFIGURATION_CONFIG_ELEMENT = "configuration";
    private static final String SHOW_PRETTY_ATTRIBUTE = "displayPrettyEnabled";
    private static final String CHOOSE_LANGUAGE_ATTRIBUTE = "selectLanguage";
    private static final String SHOW_TAB_SIZE = "displayTabSize";
    private static final String SHOW_TEXT_WIDTH = "displayTextWidth";
    private static final String DEFAULT_LANGUAGE = "Java";

    private PluginConfigurationPane configPane;
    private boolean showPrettyEnabled;

    private Language chosenLanguage;
    private Integer textWidth, tabSize;

    public void initComponent() {
        // nop
    }

    public void disposeComponent() {
        // nop
    }

    @NotNull
    public String getComponentName() {
        return COMPONENT_NAME;
    }

    @Nls
    @Override
    public String getDisplayName() {
        return DISPLAY_NAME;
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

    public Language getChosenLanguage() {
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

    public void setChosenLanguage(Language lang) {
        this.chosenLanguage = lang;
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
        configuration.setAttribute(CHOOSE_LANGUAGE_ATTRIBUTE, String.valueOf(chosenLanguage == null ? "null" : chosenLanguage.getName()));
        configuration.setAttribute(SHOW_PRETTY_ATTRIBUTE, String.valueOf(showPrettyEnabled));
        configuration.setAttribute(SHOW_TAB_SIZE, String.valueOf(tabSize));
        configuration.setAttribute(SHOW_TEXT_WIDTH, String.valueOf(textWidth));
        return configuration;
    }

    @Override
    public void loadState(Element configuration) {
        String temp = configuration.getAttributeValue(CHOOSE_LANGUAGE_ATTRIBUTE);
        chosenLanguage = new Language(temp.equals("null") ? DEFAULT_LANGUAGE : temp);
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
