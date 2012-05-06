/*
 * Image.java
 *
 * Created on August 27, 2006, 1:40 PM
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
 * Image is the fundamental abstract class for holding images.
 * It is used for passing images between pipeline stages.
 * The image dimensions are stored here; the image format
 * and the actual image data are defined in the derived classes.
 * @author webb
 */
public abstract class Image {
    /**
     * The image height.
     */
    private final int cHeight;
    /**
     * The image width.
     */
    private final int cWidth;
    
    /** Creates a new instance of Image 
     *
     * @param cWidth   the image width
     * @param cHeight  the image height
     */
    public Image(int cWidth, int cHeight) {
        this.cWidth = cWidth;
        this.cHeight = cHeight;
    }
    
    /**
     * Makes a copy of the image
     * @return the image copy
     */
    public abstract Object clone();
    
    /** Returns the image height 
     *
     * @return the image height (rows)
     */
    public int getHeight()
    {
        return this.cHeight;
    }
    
    /** Returns the image width
     *
     * @return the image width (columns)
     */
    public int getWidth()
    {
        return this.cWidth;
    }
}
