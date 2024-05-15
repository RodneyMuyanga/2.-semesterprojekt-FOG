package app.services;

import app.controllers.CarportController;

public class CarportSvg {

    private int width;
    private int length;

    private Svg carportSvg;
    private Svg containerSvg;

    private int containerWidth;
    private int containerLength;

    public CarportSvg(int width, int length) {
        this.width = width;
        this.length = length;

        containerWidth = width + 210;
        containerLength = length + 210;

        int viewBoxWidth = width + 50;
        int viewBoxLength = length + 50;

        String widthText = String.valueOf(width);
        String lengthText = String.valueOf(length);

        String viewBox = "0 0 " + viewBoxWidth + " " + viewBoxLength;
        String containerViewBox = "0 0 " + containerWidth + " " + containerLength;

        containerSvg = new Svg(0, 0, containerViewBox, "100%");
        carportSvg = new Svg(50, 50, viewBox, "100%");

        carportSvg.addRectangle(0,0,length,width, "fill=\"white\" stroke=\"white\" stroke-width=\"2\"");
        addStraps();
        addRafters();
        addPosts();

        containerSvg.addSvg(carportSvg);

        containerSvg.addArrow(30,50,30,length, "stroke:black; ");
        containerSvg.addArrow(30, length + 50, width+50,length+50 , "stroke:black; ");



        containerSvg.addText(10, length/2, "rotate(-90 30 300)" , lengthText);
        containerSvg.addText(viewBoxWidth/2, length + 210, "", widthText );
    }


        public void addStraps(){
        carportSvg.addRectangle(0,35,4.5,width, "stroke-width:2px; stroke:black; fill:white;");
        carportSvg.addRectangle(0,length-35,4.5,width, "stroke-width:2px; stroke:black; fill:white;");
    }



    public void addRafters(){
        int numberOfRafters = CarportController.getRafterWoodQuantity();
        int gapBetweenRafters = width / (numberOfRafters + 1); // Calculate the gap between rafters

        // Start from the gap to ensure the first rafter is not at the edge
        for(int i = gapBetweenRafters; i < width; i += gapBetweenRafters) {
            carportSvg.addRectangle(i, 0, length, 4.5, "stroke:black; fill:white;");
        }
    }

    public void addPosts(){
        int numberOfPosts = CarportController.getPostQuantity();
        int gapBetweenPosts = (width - 100) / (numberOfPosts - 1); // Calculate the gap between posts


        for(int i = 50; i <= width - 50; i += gapBetweenPosts) {
            carportSvg.addRectangle(i-5, 30, 15, 15, "stroke:black; fill:white;");
        }

        for(int i = 50; i <= width - 50; i += gapBetweenPosts) {
            carportSvg.addRectangle(i-5, length-40, 15, 15, "stroke:black; fill:white;");
        }
    }


    @Override
    public String toString() {
        return containerSvg.toString();
    }
}
