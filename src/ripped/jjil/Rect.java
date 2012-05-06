/*
 * Rect.java
 *
 * Created on December 10, 2007, 1:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package ripped.jjil;

/**
 * Rect represents a rectangular region. The rectangle is specified using its
 * upper left coordinate and size or the upper left and lower right coordinates.
 * Methods allow the addition of a new point to the rectangle, merging rectangles,
 * computing rectangle size, etc.
 * @author webb
 */
public class Rect {
    int nTlx = 0, nTly = 0, nWidth = 0, nHeight = 0;
    
    /** Creates a new instance of Rect */
    public Rect() {
    }
    
    /**
     * Create a new Rect specifying the upper left coordinate and size.
     * @param nTlx the upper left x (horizontal) coordinate
     * @param nTly the upper left y (vertical) coordinate
     * @param nWidth the width
     * @param nHeight the height
     */
    public Rect(int nTlx, int nTly, int nWidth, int nHeight) {
        this.nTlx = nTlx;
        this.nTly = nTly;
        this.nWidth = nWidth;
        this.nHeight = nHeight;
    }
    
    /**
     * Create a new Rect specifying two corners.
     * @param p1 the first corner.
     * @param p2 the second corner.
     */
    public Rect(Point p1, Point p2) {
        this.nTlx = Math.min(p1.wX, p2.wX);
        this.nTly = Math.min(p1.wY, p2.wY);
        this.nWidth = Math.max(p1.wX, p2.wX) - this.nTlx;
        this.nHeight = Math.max(p1.wY, p2.wY) - this.nTly;
    }
    
    /**
     * Create a new Rect (0 width and height) from a single point.
     * @param p the point.
     */
    public Rect(Point p) {
        this.nTlx = p.wX;
        this.nTly = p.wY;
        this.nWidth = 0;
        this.nHeight = 0;
    }
    
    /**
     * Add a new point to the Rect, extending it if necessary.
     * @param p the new Point
     */
    public Rect add(Point p) {
    	if (p.wX < this.nTlx) {
    		this.nTlx = p.wX;
    	}
    	if (p.wY < this.nTly) {
    		this.nTly = p.wY;
    	}
    	this.nWidth = Math.max(this.nWidth, p.wX - this.nTlx);
    	this.nHeight = Math.max(this.nHeight, p.wY - this.nTly);
        return this;
    }
    
    /**
     * Test a point for inclusion in a rectangle, including
     * boundaries.
     * @param p the point to test
     * @return true iff the point is in the rectangle
     */
    public boolean contains(Point p) {
    	return p.wX >= this.nTlx && p.wX <= this.nTlx + this.nWidth &&
    		p.wY >= this.nTly && p.wY <= this.nTly + this.nHeight;
    }
    
    /**
     * Return area of the rectangle.
     * @return the Rect's area.
     */
    public int getArea() {
        return this.nWidth * this.nHeight;
    }
    
    public int getBottom() {
        return this.nTly + this.nHeight;
    }
    
    /**
     * Return the left (horizontal) position of the rectangle.
     * @return returns the left edge of the rectangle.
     */
    public int getLeft() {
    	return this.nTlx;
    }
    
    /**
     * Return the height of the rectangle.
     * @return the rectangle's height.
     */
    public int getHeight() {
        return this.nHeight;
    }
    
    /**
     * Returns rectangle perimeter
     * @return rectangle perimeter
     */
    public int getPerimeter() {
        return 2*(this.getWidth() + this.getHeight());
    }
    
    public int getRight() {
        return this.nTlx + this.nWidth;
    }
    
    /**
     * Return the top (vertical) position of the rectangle.
     * @return the top (vertical) edge of the rectangle.
     */
    public int getTop() {
    	return this.nTly;
    }
    
    /**
     * Return the width of the rectangle.
     * @return the width of the rectangle.
     */
    public int getWidth() {
        return this.nWidth;
    }
    
    public Rect offset(int nX, int nY) {
        this.nTlx += nX;
        this.nTly += nY;
        return this;
    }
    
    public boolean overlaps(Rect r) {
        if (this.contains(new Point(r.getLeft(), r.getTop()))) {
            return true;
        }
        if (this.contains(new Point(r.getRight(), r.getTop()))) {
            return true;
        }
        if (this.contains(new Point(r.getLeft(), r.getBottom()))) {
            return true;
        }
        if (this.contains(new Point(r.getRight(), r.getBottom()))) {
            return true;
        }
        if (r.contains(new Point(this.getLeft(), this.getTop()))) {
            return true;
        }
        if (r.contains(new Point(this.getRight(), this.getTop()))) {
            return true;
        }
        if (r.contains(new Point(this.getLeft(), this.getBottom()))) {
            return true;
        }
        if (r.contains(new Point(this.getRight(), this.getBottom()))) {
            return true;
        }
        return false;
    }
    
    public Rect scale(int nX, int nY) {
        this.nTlx *= nX;
        this.nTly *= nY;
        this.nHeight *= nX;
        this.nWidth *= nY;
        return this;
    }
    
    public String toString() {
        return super.toString() + "(" + 
                new Integer(this.nTlx).toString() + "," +
                new Integer(this.nTly).toString() + ";" +
                new Integer(this.nWidth).toString() + "x" +
                new Integer(this.nHeight).toString() + ")";
                
    }
}
