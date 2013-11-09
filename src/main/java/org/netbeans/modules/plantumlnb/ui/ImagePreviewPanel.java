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

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import org.openide.util.NbBundle;

/**
 * JPanel used for image preview in Navigator window
 *
 * @author jpeska
 */
public class ImagePreviewPanel extends JPanel {

    BufferedImage image;
    private final int stringGapSize = 10;

    public void setImage(BufferedImage image) {
        this.image = image;
        this.revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.setColor(Color.BLACK);

            int width = image.getWidth();
            int height = image.getHeight();
            String sizes = "Dimensions: " + width + " x " + height;

//            g.drawString(sizes, (int) (this.getWidth() * 0.05), this.getHeight() - stringGapSize);
            // adapt image width and height to the size of Navigator window
//            double widthRatio = ((double) image.getWidth()) / (((double) this.getWidth()) * 0.9);
//            double heightRatio = ((double) image.getHeight()) / (((double) this.getHeight()) * 0.9 - stringGapSize - 20);
//            if (widthRatio > 1 || heightRatio > 1) {
//                double ratio = widthRatio > heightRatio ? widthRatio : heightRatio;
//                width = (int) (((double) image.getWidth()) / ratio);
//                height = (int) (((double) image.getHeight()) / ratio);
//            }
            this.setPreferredSize(new Dimension(width + 100, height + 100));
            g.drawImage(image, (this.getWidth() - width) / 2, (this.getHeight() - height) / 2, width, height, this);
            
//            setBounds(this.getX(), this.getY(), width, height);
        } else {
            g.setColor(Color.RED);
            FontMetrics fm = this.getFontMetrics(g.getFont()) ;
            String errMessage = NbBundle.getMessage(ImagePreviewPanel.class, "ERR_Thumbnail");
            int stringWidth = fm.stringWidth(errMessage);
            BufferedImage defaultIcon = null;
            try {
                defaultIcon = ImageIO.read(getClass().getResourceAsStream("default-icon.png"));
            } catch(IOException ioe){
                Logger.getLogger(ImagePreviewPanel.class.getName()).info(ioe.toString());             
            }
            int width = defaultIcon.getWidth();
            int height = defaultIcon.getHeight();
                        
            g.drawImage(defaultIcon, (this.getWidth() - stringWidth) / 2, this.getHeight() / 2, width, height, this);
        }
    }
}
