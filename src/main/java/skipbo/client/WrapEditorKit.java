package skipbo.client;

import javax.swing.text.*;

/**
 * An EditorKit that wraps the lines if there are no spaces in the line and the line is too long.
 * Source: https://stackoverflow.com/questions/30590031/jtextpane-line-wrap-behavior
 */
public class WrapEditorKit extends StyledEditorKit {

    private final ViewFactory defaultFactory = new WrapColumnFactory();

    @Override
    public ViewFactory getViewFactory() {
        return defaultFactory;
    }

    private static class WrapColumnFactory implements ViewFactory {
        @Override
        public View create(final Element element) {
            final String kind = element.getName();
            if (kind != null) {
                switch (kind) {
                    case AbstractDocument.ContentElementName:
                        return new WrapLabelView(element);
                    case AbstractDocument.ParagraphElementName:
                        return new ParagraphView(element);
                    case AbstractDocument.SectionElementName:
                        return new BoxView(element, View.Y_AXIS);
                    case StyleConstants.ComponentElementName:
                        return new ComponentView(element);
                    case StyleConstants.IconElementName:
                        return new IconView(element);
                }
            }

            // Default to text display.
            return new LabelView(element);
        }
    }

    private static class WrapLabelView extends LabelView {
        public WrapLabelView(final Element element) {
            super(element);
        }

        @Override
        public float getMinimumSpan(final int axis) {
            switch (axis) {
                case View.X_AXIS:
                    return 0;
                case View.Y_AXIS:
                    return super.getMinimumSpan(axis);
                default:
                    throw new IllegalArgumentException("Invalid axis: " + axis);
            }
        }
    }
}
