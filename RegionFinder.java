import java.awt.*;
import java.awt.image.*;
import java.util.*;

/**
 * Region growing algorithm: finds and holds regions in an image.
 * Each region is a list of contiguous points with colors similar to a target color.
 * Scaffold for PS-1, Dartmouth CS 10, Fall 2016
 *
 * @author Chris Bailey-Kellogg, Winter 2014 (based on a very different structure from Fall 2012)
 * @author Travis W. Peters, Dartmouth CS 10, Updated Winter 2015
 * @author CBK, Spring 2015, updated for CamPaint
 * @author Chipo Chibbamulilo ,Winter 2023
 */
public class RegionFinder {
    private static final int maxColorDiff = 20;				// how similar a pixel color must be to the target color, to belong to a region
    private static final int minRegion = 50; 				// how many points in a region to be worth considering

    private BufferedImage image;                            // the image in which to find regions
    private BufferedImage recoloredImage;                   // the image with identified regions recolored

    private ArrayList<ArrayList<Point>> regions;			// a region is a list of points
    // so the identified regions are in a list of lists of points

    public RegionFinder() {
        this.image = null;
        regions=new ArrayList<ArrayList<Point>>();//list of lists
    }

    public RegionFinder(BufferedImage image) {
        this.image = image;
        regions=new ArrayList<ArrayList<Point>>();
    }

    public void setImage(BufferedImage image) {
        this.image = image;
    }

    public BufferedImage getImage() {
        return image;
    }

    public BufferedImage getRecoloredImage() {
        return recoloredImage;
    }

    /**
     * Sets regions to the flood-fill regions in the image, similar enough to the trackColor.
     */
    public void findRegions(Color targetColor) {
        //setting visited to black
        BufferedImage visited = new BufferedImage(image.getWidth(),image.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // TODO: YOUR CODE HERE

        ArrayList<Point> toVisit = new ArrayList<>();//neighbors list

        //Looping over every pixel
        ArrayList<Point> region;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {

                //checking if (x,y) is close to target color
                Color c = new Color(image.getRGB(x, y));
                if (visited.getRGB(x, y) == 0 && colorMatch(c, targetColor)) {
                    //intiliazing region
                    region = new ArrayList<Point>();

                    toVisit.add(new Point(x,y));

                    while (toVisit.size() > 0) {
                        //pop it
                        Point i = toVisit.remove(0);

                        region.add(i);

                        //check if it has been visited
                        if (visited.getRGB(i.x, i.y) == 0) {

                            //marking it as visited
                            visited.setRGB(i.x, i.y, 1);

                            //loop over its neighbors
                            for (int ny = Math.max(0, i.y - 1); ny <= Math.min(image.getHeight() - 1, i.y + 1); ny++) {
                                for (int nx = Math.max(0, i.x - 1); nx <= Math.min(image.getWidth() - 1, i.x + 1); nx++) {

                                    if (visited.getRGB(nx, ny) == 0 ) {

                                        //checking if neighbors are close to target color
                                        Color b = new Color(image.getRGB(nx, ny));
                                        if (colorMatch(targetColor, b)) {
                                            toVisit.add(new Point(nx, ny));
                                        }
                                    }
                                }
                            }
                        }
                    }
                            if (region.size() >= minRegion) {
                                //adding region to regions
                                regions.add(region);

                    }
                }
            }
        }
    }





    /**
     * Tests whether the two colors are "similar enough" (your definition, subject to the maxColorDiff threshold, which you can vary).
     */
    private static boolean colorMatch(Color c1, Color c2) {
        // TODO: YOUR CODE HERE
        int r=Math.abs(c1.getRed()- c2.getRed()) ;
               int b= Math.abs (c1.getBlue()- c2.getBlue()) ;
               int g= Math.abs(c1.getGreen()- c2.getGreen());
        if (r<= maxColorDiff && b<=maxColorDiff && g<=maxColorDiff){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Returns the largest region detected (if any region has been detected)
     */
    public ArrayList<Point> largestRegion() {
        // TODO: YOUR CODE HERE
        int max=0;
        ArrayList<Point> largest_region = new ArrayList<>();

        for(ArrayList<Point> region: regions){

                if(max <region.size()){
                max =region.size();
                largest_region=region;
                }
            }

        return largest_region;
    }

    /**
     * Sets recoloredImage to be a copy of image,
     * but with each region a uniform random color,
     * so we can see where they are
     */
    public void recolorImage() {
        // First copy the original
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);

        // Now recolor the regions in it
        // TODO: YOUR CODE HERE

        for (ArrayList<Point> region: regions){
            Color color=new Color((int) (Math.random()* 255), (int) (Math.random()* 255), (int) (Math.random()* 255));
            for (Point point : region){
                recoloredImage.setRGB(point.x,point.y,color.getRGB());
            }
        }

    }
}