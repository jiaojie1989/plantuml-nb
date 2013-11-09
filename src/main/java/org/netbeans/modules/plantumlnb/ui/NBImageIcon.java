/* 
 * The MIT License
 *
 * Copyright 2013 Venkat Ram Akkineni.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.netbeans.modules.plantumlnb.ui;


import java.awt.Image;
import java.io.IOException;
import java.io.Serializable;
import javax.swing.ImageIcon;
import org.netbeans.modules.plantumlnb.pumlDataObject;


/**
 * ImageIcon with serialization.
 *
 * @author Petr Hamernik, Michael Wever
 * @author  Marian Petras
 */
public class NBImageIcon extends ImageIcon implements Serializable {

    /** generated Serialized Version UID */
    static final long serialVersionUID = -1730253055388017036L;

    /** Appropriate image data object */
    pumlDataObject obj;

    /**
     * Loads an image from an <code>ImageDataObject</code>.
     * If an error occures during reading the image, an exception is thrown.
     * If the image format is not supported, <code>null</code> is returned.
     *
     * @param  obj  <code>ImageDataObject</code> to load the image from
     * @return  loaded image if loaded successfully,
     *          or <code>null</code> if no registered <code>ImageReader</code>
     *          claims to be able to read the image
     * @exception  java.io.IOException
     *             if an error occurs during reading the image
     * @see  javax.imageio.ImageIO#read(java.io.InputStream)
     */
    public static NBImageIcon load(pumlDataObject obj) throws IOException {
        Image image = obj.getImage();
        return (image != null) ? new NBImageIcon(obj, image) : null;
    }
    
    /** Construct a new icon.
     * @param obj the data object to represent the image in
     */
    private NBImageIcon(pumlDataObject obj, Image image) {
        //super(obj.getImageURL()); // PENDING for the time URL is incorrectly cached (in Toolkit)
        super(image);  //mw
        this.obj = obj;
    }
    
    
    /** Get an object to be written to the stream instead of this object. */
    public Object writeReplace() {
        return new ResolvableHelper(obj);
    }

    
    /** Helper class for serialization. */
    static class ResolvableHelper implements Serializable {
        
        /** generated Serialized Version UID. */
        static final long serialVersionUID = -1120520132882774882L;
        
        /** serializable data object. */
        pumlDataObject obj;
        
        /** Constructs ResolvableHelper object for given ImageDataObject. */
        ResolvableHelper(pumlDataObject obj) {
            this.obj = obj;
        }

        /** Restore with the same data object. */
        public Object readResolve() {
            Image image;
            try {
                image = obj.getImage();
            } catch (IOException ex) {
                image = null;
            }
            return new NBImageIcon(obj, (image != null) ? image : new ImageIcon().getImage());
        }
    } // End of nested class ResolvableHelper.
}
