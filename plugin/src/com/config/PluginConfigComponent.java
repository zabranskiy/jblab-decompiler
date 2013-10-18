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
    private PluginConfigurationPane myConfigPane;
    private boolean myShowPrettyEnabled;
    private Language myChosenLanguage;
    private Integer myTextWidth, myTabSize;

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
        if (myConfigPane == null) {
            myConfigPane = new PluginConfigurationPane();
        }
        return myConfigPane.getRootPane();
    }

    @Override
    public boolean isModified() {
        return myConfigPane != null && myConfigPane.isModified(this);
    }

    @Override
    public void apply() throws ConfigurationException {
        if (myConfigPane != null) {
            myConfigPane.storeDataTo(this);
        }
    }

    @Override
    public void reset() {
        if (myConfigPane != null) {
            myConfigPane.readDataFrom(this);
        }
    }

    @Override
    public void disposeUIResources() {
        myConfigPane = null;
    }

    public Language getChosenLanguage() {
        return myChosenLanguage;
    }

    public void setChosenLanguage(final Language lang) {
        this.myChosenLanguage = lang;
    }

    public Integer getTextWidth() {
        return myTextWidth;
    }

    public void setTextWidth(final Integer textWidth) {
        this.myTextWidth = textWidth;
    }

    public Integer getTabSize() {
        return myTabSize;
    }

    public void setTabSize(final Integer tabSize) {
        this.myTabSize = tabSize;
    }

    public boolean isShowPrettyEnabled() {
        return myShowPrettyEnabled;
    }

    public void setShowPrettyEnabled(final boolean showMetadataEnabled) {
        this.myShowPrettyEnabled = showMetadataEnabled;
    }

    @Override
    public Element getState() {
        Element configuration = new Element(CONFIGURATION_CONFIG_ELEMENT);
        configuration.setAttribute(CHOOSE_LANGUAGE_ATTRIBUTE, String.valueOf(myChosenLanguage == null ? "null" : myChosenLanguage.getName()));
        configuration.setAttribute(SHOW_PRETTY_ATTRIBUTE, String.valueOf(myShowPrettyEnabled));
        configuration.setAttribute(SHOW_TAB_SIZE, String.valueOf(myTabSize));
        configuration.setAttribute(SHOW_TEXT_WIDTH, String.valueOf(myTextWidth));
        return configuration;
    }

    @Override
    public void loadState(final Element configuration) {
        String temp = configuration.getAttributeValue(CHOOSE_LANGUAGE_ATTRIBUTE);
        myChosenLanguage = new Language(temp.equals("null") ? DEFAULT_LANGUAGE : temp);
        temp = configuration.getAttributeValue(SHOW_TAB_SIZE);
        myTabSize = temp.equals("null") ? 4 : Integer.valueOf(temp);
        temp = configuration.getAttributeValue(SHOW_TEXT_WIDTH);
        myTextWidth = temp.equals("null") ? 100 : Integer.valueOf(temp);

        String showPrettyStr = configuration.getAttributeValue(SHOW_PRETTY_ATTRIBUTE);
        if (StringUtils.isNotBlank(showPrettyStr)) {
            myShowPrettyEnabled = Boolean.valueOf(showPrettyStr);
        }
    }

}
