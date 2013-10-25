package com.config;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;


public class DocFilter extends DocumentFilter {
    @Override
    public void insertString(final @NotNull FilterBypass fb,
                             final int offset,
                             final @NotNull String string,
                             final @Nullable AttributeSet attr) throws BadLocationException {

        final Document doc = fb.getDocument();
        final StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString()) || (sb.toString().equals(""))) {
            super.insertString(fb, offset, string, attr);
        }
    }

    private boolean test(final @NotNull String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void replace(final @NotNull FilterBypass fb,
                        final int offset,
                        final int length,
                        final @NotNull String text,
                        final @Nullable AttributeSet attrs) throws BadLocationException {
        final Document doc = fb.getDocument();
        final StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString()) || (sb.toString().equals(""))) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(final @NotNull FilterBypass fb, final int offset, final int length) throws BadLocationException {
        final Document doc = fb.getDocument();
        final StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString()) || (sb.toString().equals(""))) {
            super.remove(fb, offset, length);
        }
    }
}