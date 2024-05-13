package app.services;

public class CarportSvg {

    private int width;
    private int length;

    private Svg carportSvg;




    public CarportSvg(int width, int length) {
        this.width = width;
        this.length = length;
        carportSvg = new Svg(0, 0, "0 0 855 690", "80%");
        carportSvg.addRectangle(0,0,600,780, "stroke-width:1px stroke:black; fill:white;");
        addStraps();
        addRafters();
    }

    public void addStraps(){
        carportSvg.addRectangle(0,35,4.5,780, "stroke-width:1px stroke:black; fill:white;");
        carportSvg.addRectangle(0,565,4.5,780, "stroke-width:1px stroke:black; fill:white;");
    }

    public void addRafters(){

        for(int i = 0; i < 780; i += 55){
            carportSvg.addRectangle(i, 0, 600, 4.5, "stroke:black; fill:white;");
        }
    }

    @Override
    public String toString() {
        return carportSvg.toString();
    }
}
