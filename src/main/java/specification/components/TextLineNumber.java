package specification.components;

import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Utilities;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class TextLineNumber extends JPanel
        implements CaretListener, DocumentListener, PropertyChangeListener {

    public final static float LEFT = 0.0f;

    private final JTextArea component;

    public TextLineNumber(JTextArea component) {
        this.component = component;

        setBackground(Color.GRAY);
        setForeground(Color.WHITE);
        setPreferredSize(new Dimension(30, 300));

        component.getDocument().addDocumentListener(this);
        component.addCaretListener(this);
        component.addPropertyChangeListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = g.getFontMetrics(getFont());
        Insets insets = getInsets();
        int availableWidth = getWidth() - insets.left - insets.right;

        Rectangle clip = g.getClipBounds();
        int rowStartOffset = component.viewToModel(new Point(0, clip.y));
        int endOffset = component.viewToModel(new Point(0, clip.y + clip.height));

        while (rowStartOffset <= endOffset) {
            try {
                int lineNumber = getLineNumber(rowStartOffset);
                if (lineNumber != -1) {
                    String line = String.valueOf(lineNumber);
                    int stringWidth = fontMetrics.stringWidth(line);
                    int x = getOffsetX(availableWidth, stringWidth) + insets.left;
                    int y = getOffsetY(rowStartOffset, fontMetrics);
                    g.drawString(line, x, y);
                }

                rowStartOffset = Utilities.getRowEnd(component, rowStartOffset) + 1;
            } catch (Exception e) { break; }
        }
    }

    private int getLineNumber(int rowStartOffset) {
        Document doc = component.getDocument();

        if (doc != null && doc.getLength() > 0) {
            Element root = doc.getDefaultRootElement();
            return root.getElementIndex(rowStartOffset) + 1;
        }

        return -1;
    }

    private int getOffsetX(int availableWidth, int stringWidth) {
        return (int)((availableWidth - stringWidth) * LEFT);
    }

    private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
            throws BadLocationException {
        Rectangle r = component.modelToView(rowStartOffset);
        return r.y + r.height - fontMetrics.getDescent();
    }

    @Override public void caretUpdate(CaretEvent e) { repaint(); }
    @Override public void changedUpdate(DocumentEvent e) { documentChanged(); }
    @Override public void insertUpdate(DocumentEvent e) { documentChanged(); }
    @Override public void removeUpdate(DocumentEvent e) { documentChanged(); }
    private void documentChanged() { repaint(); }
    @Override public void propertyChange(PropertyChangeEvent evt) { repaint(); }
}
