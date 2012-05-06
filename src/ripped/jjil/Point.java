/*
 * Point.java
 *
 * Created on September 9, 2006, 1:46 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 *
 * Copyright 2006 by Jon A. Webb
 *     This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU Lesser General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU Lesser General Public License for more details.
 *
 *    You should have received a copy of the Lesser GNU General Public License
 *    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 */

package ripped.jjil;


/**
 * Point: an object holding a 2-dimensional point coordinate
 * @author webb
 */
public class Point {
    int wX;
    int wY;
    
    /** Creates a new instance of Point 
     *
     * @param wX the Point's x position
     * @param wY the Point's y position
     */
    public Point(int wX, int wY) {
        this.wX = wX;
        this.wY = wY;
    }
    
    /**
     * Return the point's x-coordinate.
     * @return the horizontal position of the point.
     */
    public int getX() {
        return this.wX;
    } 
    
    /**
     * Return the point's y-coordinate.
     * @return the vertical position of the point.
     */
    public int getY() {
        return this.wY;
    }
    
    /**
     * Offset a point by a certain x,y
     * @param x x offset
     * @param y y offset
     * @return modified Point
     */
    public Point offset(int x, int y) {
        this.wX += x;
        this.wY += y;
        return this;
    }
    
    /**
     * Implement toString
     * @return Object address + (x,y)
     */
    public String toString() {
        return super.toString() + "(" +
                new Integer(this.wX).toString() + "," +
                new Integer(this.wY).toString() + ")";
    }
}
