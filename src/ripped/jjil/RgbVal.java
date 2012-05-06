/*
 * RgbVal.java
 *
 * Created on September 9, 2006, 10:42 AM
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
 * Helper class for manipulating RGB values. All functions are static.
 * @author webb
 */
public class RgbVal {
    /**
     * Converts byte R, G, and B values to an ARGB word.
     * byte is a signed data type but the ARGB word has unsigned bit fields.
     * In other words the minimum byte value is Byte.MIN_VALUE but the color black in
     * the ARGB word is represented as 0x00. So we must subtract Byte.MIN_VALUE to get an
     * unsigned byte value before shifting and combining the bit fields.
     *
     * @param R input signed red byte
     * @param G input signed green byte
     * @param B input signed blue byte
     * @return the color ARGB word.
     */
    public static int toRgb(byte R, byte G, byte B) {
        return 0xFF000000 | 
                (toUnsignedInt(R)<<16) |
                (toUnsignedInt(G)<<8)  |
                toUnsignedInt(B);
    }
    
    /**
     * Compare two RgbVals in absolute value.
     * @return sum of absolute differences between pixel values
     */
    public static int getAbsDiff(int rgb1, int rgb2) {
        return Math.abs(RgbVal.getR(rgb1) - RgbVal.getR(rgb2)) +
                Math.abs(RgbVal.getG(rgb1) - RgbVal.getG(rgb2)) +
                Math.abs(RgbVal.getB(rgb1) - RgbVal.getB(rgb2));
    }
    
    /**
     * Compare two RgbVals in maximum difference in any band.
     * @return maximum difference between pixel values in any band
     */
    /**
     * Computes maximum difference (largest difference in
     * color, R, G, or B) of two color values.
     * @param ARGB1 first color
     * @param ARGB2 second color
     * @return largest difference. Will always be >= 0,
     * <= 256.
     */
    public static int getMaxDiff(int ARGB1, int ARGB2) {
        int nR1 = RgbVal.getR(ARGB1);
        int nG1 = RgbVal.getG(ARGB1);
        int nB1 = RgbVal.getB(ARGB1);
        int nR2 = RgbVal.getR(ARGB2);
        int nG2 = RgbVal.getG(ARGB2);
        int nB2 = RgbVal.getB(ARGB2);
        return Math.max(Math.abs(nR1-nR2), 
                Math.max(Math.abs(nG1-nG2), Math.abs(nB1-nB2)));
    }
    
    public static int getProportionateDiff(int ARGB1, int ARGB2) {
        int nR1 = RgbVal.getR(ARGB1) - Byte.MIN_VALUE;
        int nG1 = RgbVal.getG(ARGB1) - Byte.MIN_VALUE;
        int nB1 = RgbVal.getB(ARGB1) - Byte.MIN_VALUE;
        int nR2 = RgbVal.getR(ARGB2) - Byte.MIN_VALUE;
        int nG2 = RgbVal.getG(ARGB2) - Byte.MIN_VALUE;
        int nB2 = RgbVal.getB(ARGB2) - Byte.MIN_VALUE;
        // We're solving the equation
        // min/r ((r*nR1 - nR2) + (r*nG1 - nG2) + (r*nB1 - nB2))**2
        // which gives 2*((r*nR1 - nR2)*nR1 + (r*nG1 - nG2)*nG1 + (r*nB1 - nB2)*nB1) = 0
        // or r = (nR1*nR2 + nG1*nG2 + nB1*nB2) / (nR1*nR1 + nG1*nG1 + nB1*nB1)
        // we divide r into nNum / nDenom to avoid floating point
        int nNum = (nR1*nR2 + nG1*nG2 + nB1*nB2);
        int nDenom = (nR1*nR1 + nG1*nG1 + nB1*nB1);
        if (nDenom == 0) {
            return 3*Byte.MAX_VALUE;
        }
        // the error is then ((r*nR1 - nR2) + (r*nG1 - nG2) + (r*nB1 - nB2))**2
        // or (r*(nR1 + nG1 + nB1) - (nR2 + nB2 + nG2))**2
        // or ((nNum*(nR1 + nG1 + nB1) - nDenom*(nR1 + nG2 + nB2)) / nDenom)**2
        // or ((nNum*(nR1 + nG1 + nB1) - nDenom*(nR1 + nG2 + nB2))**2 / nDenom**2
        return MathPlus.square(8*(nNum*(nR1 + nG1 + nB1) - nDenom*(nR1 + nG2 + nB2)))
                / MathPlus.square(nDenom);
    }

    /**
     * Compare two RgbVals in sum of squares difference.
     * @return sum of squares differences between pixel values
     */
    public static int getSqrDiff(int rgb1, int rgb2) {
        return MathPlus.square(RgbVal.getR(rgb1) - RgbVal.getR(rgb2)) +
                MathPlus.square(RgbVal.getG(rgb1) - RgbVal.getG(rgb2)) +
                MathPlus.square(RgbVal.getB(rgb1) - RgbVal.getB(rgb2));
    }

    /**
     * Extracts blue byte from input ARGB word.
     * The bit fields in ARGB word are unsigned, ranging from 0x00 to 0xff.
     * To convert these to the returned signed byte value we must add
     * Byte.MIN_VALUE.
     * @return the blue byte value, converted to a signed byte
     * @param ARGB the input color ARGB word.
     */
    public static byte getB(int ARGB) {
        return toSignedByte((byte)(ARGB & 0xff));
    }
    
    /**
     * Extracts green byte from input ARGB word.
     * The bit fields in ARGB word are unsigned, ranging from 0x00 to 0xff.
     * To convert these to the returned signed byte value we must add
     * Byte.MIN_VALUE.
     * @param ARGB the input color ARGB word.
     * @return the green byte value, converted to a signed byte
     */
    public static byte getG(int ARGB) {
        return toSignedByte((byte) ((ARGB>>8) & 0xff));
    }
    
    /**
     * Extracts red byte from input ARGB word.
     * The bit fields in ARGB word are unsigned, ranging from 0x00 to 0xff.
     * To convert these to the returned signed byte value we must add
     * Byte.MIN_VALUE.
     * @param ARGB the input color ARGB word.
     * @return the red byte value, converted to a signed byte
     */
    public static byte getR(int ARGB) {
        return toSignedByte((byte) ((ARGB>>16) & 0xff));
    }
    
    /**
     * Return "vector" difference of Rgb values. Treating each Rgb value
     * as a 3-element vector form the value (ARGB-ARGBTarg) . ARGBVec where
     * . is dot product. Useful for determining whether an Rgb value is
     * near another weighted the different channels differently.
     * @param ARGB tested Rgb value
     * @param ARGBTarg target Rgb value
     * @param ARGBVec weighting
     * @return (ARGB-ARGBTarg) . ARGBVec where . is dot product and the
     * Rgb values are treated as 3-vectors.
     */
    public static int getVecDiff(int ARGB, int ARGBTarg, int ARGBVec) {
        int nR1 = RgbVal.getR(ARGB);
        int nG1 = RgbVal.getG(ARGB);
        int nB1 = RgbVal.getB(ARGB);
        int nR2 = RgbVal.getR(ARGBTarg);
        int nG2 = RgbVal.getG(ARGBTarg);
        int nB2 = RgbVal.getB(ARGBTarg);
        int nR3 = RgbVal.getR(ARGBVec);
        int nG3 = RgbVal.getG(ARGBVec);
        int nB3 = RgbVal.getB(ARGBVec);
        return  (nR1 - nR2) * nR3 +
                (nG1 - nG2) * nG3 +
                (nB1 - nB2) * nB3;
    }
    
    /**
     * Converts from an unsigned bit field (as stored in an ARGB word
     * to a signed byte value (that we can do computation on).
     * @return the signed byte value
     * @param b the unsigned byte value.
     */
    public static byte toSignedByte(byte b) {
        return (byte) (b + Byte.MIN_VALUE);
    }

    /**
     * Converts from a signed byte value (which we do computation on)
     * to an unsigned bit field (as stored in an ARGB word). The result
     * is returned as an int because the unsigned 8 bit value cannot be
     * represented as a byte.
     * @return the unsigned bit field
     * @param b the signed byte value.
     */
    public static int toUnsignedInt(byte b) {
        return (b - Byte.MIN_VALUE);
    }
    
    /**
     * Provide a way to turn color values into strings
     * @param ARGB the input color value
     * @return a string describing the color
     */
    public static String toString(int ARGB) {
        return "[" + new Integer(RgbVal.getR(ARGB)).toString() + "," +
                new Integer(RgbVal.getR(ARGB)).toString() + "," +
                new Integer(RgbVal.getB(ARGB)).toString() + "]";
    }
}
