package config;

import org.jetbrains.annotations.NotNull;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.DocumentFilter;

public class DocFilter extends DocumentFilter {
    @Override
    public void insertString(@NotNull FilterBypass fb, int offset, @NotNull String string, AttributeSet attr) throws BadLocationException {

        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.insert(offset, string);

        if (test(sb.toString()) || (sb.toString().equals(""))) {
            super.insertString(fb, offset, string, attr);
        }
    }

    private boolean test(String text) {
        try {
            Integer.parseInt(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    public void replace(@NotNull FilterBypass fb, int offset, int length, @NotNull String text, AttributeSet attrs) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.replace(offset, offset + length, text);

        if (test(sb.toString()) || (sb.toString().equals(""))) {
            super.replace(fb, offset, length, text, attrs);
        }
    }

    @Override
    public void remove(@NotNull FilterBypass fb, int offset, int length) throws BadLocationException {
        Document doc = fb.getDocument();
        StringBuilder sb = new StringBuilder();
        sb.append(doc.getText(0, doc.getLength()));
        sb.delete(offset, offset + length);

        if (test(sb.toString()) || (sb.toString().equals(""))) {
            super.remove(fb, offset, length);
        }
    }
}