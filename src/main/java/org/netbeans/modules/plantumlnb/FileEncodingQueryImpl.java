/*
 * The MIT License
 *
 * Copyright 2021 Matthias Bläsing.
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
package org.netbeans.modules.plantumlnb;

import java.nio.charset.Charset;
import org.netbeans.api.editor.mimelookup.MimeRegistration;
import org.netbeans.modules.plantumlnb.ui.options.PlantUMLPanel;
import org.netbeans.spi.queries.FileEncodingQueryImplementation;
import org.openide.filesystems.FileObject;
import org.openide.util.NbPreferences;

import static org.netbeans.modules.plantumlnb.ui.options.PlantUMLPanel.DEFAULT_UTF8_ENCODING;
import static org.netbeans.modules.plantumlnb.ui.options.PlantUMLPanel.PLANTUML_ENCODING;


@MimeRegistration(mimeType = "text/x-puml", service = FileEncodingQueryImplementation.class)
public class FileEncodingQueryImpl extends FileEncodingQueryImplementation {

    @Override
    public Charset getEncoding(FileObject file) {
        return Charset.forName(NbPreferences.forModule(PlantUMLPanel.class).get(PLANTUML_ENCODING, DEFAULT_UTF8_ENCODING));
    }

}
