package app.services;

    public class Svg {
        private static final String SVG_TEMPLATE = "  <svg x=\"%d\" y=\"%d\" viewBox=\"%s\" width=\"%s\" preserveAspectRatio=\"xMinYMin\">";

        private static final String SVG_RECT_TEMPLATE = "<rect x=\"%f\" y=\"%f\" height=\"%f\" width=\"%f\" style=\"%s\"></rect>";
        private static final String SVG_ARROW_DEFS = "  <defs>\n" +
                "        <marker id=\"a\" markerWidth=\"12\" markerHeight=\"12\" refX=\"0\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,6 L12,0 L12,12 L0,6\" fill=\"#000\"/>\n" +
                "        </marker>\n" +
                "        <marker id=\"b\" markerWidth=\"12\" markerHeight=\"12\" refX=\"12\" refY=\"6\" orient=\"auto\">\n" +
                "            <path d=\"M0,0 L12,6 L0,12 L0,0 \" fill=\"#000\"/>\n" +
                "        </marker>\n" +
                "    </defs>";

        private static final String SVG_LINE_TEMPLATE = "  <line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"%s\"><\"line>";
        private static final String SVG_TEXT_TEMPLATE = "<text x=\"%d\" y=\"%d\" transform=\"%s\" >%s</text>";
        private static final String SVG_ARROW_TEMPLATE = "<line x1=\"%d\" y1=\"%d\" x2=\"%d\" y2=\"%d\" style=\"%s\" marker-start=\"url(#a)\" marker-end=\"url(#b)\"></line>";
        private static StringBuilder svg = new StringBuilder();
        public Svg(int x, int y, String viewBox, String width){
            svg.append(String.format(SVG_TEMPLATE, x, y, viewBox, width));
            svg.append(SVG_ARROW_DEFS);
        }
        public static void addRectangle(double x, double y, double height, double width, String style){
            svg.append(String.format(SVG_RECT_TEMPLATE, x, y, height, width, style));
        }
        public void addLine(int x1, int y1, int x2, int y2, String style){
            svg.append(String.format(SVG_LINE_TEMPLATE, x1, y1, x2, y2, style));
        }
        public void addArrow(int x1, int y1, int x2, int y2, String style) {
            svg.append(String.format(SVG_ARROW_TEMPLATE, x1, y1, x2, y2, style));
        }
        public void addText(int x, int y, String rotation, String text){
             svg.append(String.format(SVG_TEXT_TEMPLATE, x ,y, rotation, text));
        }
        public void addSvg(Svg innerSvg){
            svg.append(innerSvg.toString());
        }
        public String toString()
        {
            return svg.append("</svg>").toString();
        }
    }
