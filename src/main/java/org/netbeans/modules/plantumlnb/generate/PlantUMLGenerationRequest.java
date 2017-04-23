/* 
 * The MIT License
 *
 * Copyright 2017 Venkat Ram Akkineni.
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
package org.netbeans.modules.plantumlnb.generate;

import java.io.File;
import java.util.StringJoiner;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.netbeans.modules.plantumlnb.StringUtils;

/**
 *
 * @author venkat
 */
@Accessors
public class PlantUMLGenerationRequest {
    
    @Setter @Getter
    private String destinationDirectory;
    
    @Setter @Getter
    private String outputFileName;

    @Setter @Getter
    private String sourcesDirectory;
    
    @Setter @Getter
    private String includePatterns;
    
    @Setter @Getter
    private String excludePatterns;
    
    // Display type options
    @Setter @Getter
    private boolean abstractClasses = true;
    
    @Setter @Getter
    private boolean annotations = true;
    
    @Setter @Getter
    private boolean classes = true;
    
    @Setter @Getter
    private boolean enums = true;
    
    @Setter @Getter
    private boolean extensions = true;
    
    @Setter @Getter
    private boolean implementations = true;
    
    @Setter @Getter
    private boolean imports = true;
    
    @Setter @Getter
    private boolean interfaces = true;
    
    @Setter @Getter
    private boolean nativeMethods = true;
    
    @Setter @Getter
    private boolean staticImports = true;
    // End Display type options
    
    @Setter @Getter
    private String displayNameRegex = "";
    
    @Setter @Getter
    private String displayPackageNameRegex = "";

    public PlantUMLGenerationRequest() {
    }

    public String getDisplayTypeOptionsString() {
        StringJoiner displayTypeOptionsString = new StringJoiner(",");
        
        if(abstractClasses) {
            displayTypeOptionsString.add(DisplayTypeOptions.abstract_classes.name());
        }
        
        if(annotations) {
            displayTypeOptionsString.add(DisplayTypeOptions.annotations.name());
        }
        
        if(classes) {
            displayTypeOptionsString.add(DisplayTypeOptions.classes.name());
        }
        
        if(enums) {
            displayTypeOptionsString.add(DisplayTypeOptions.enums.name());
        }
        
        if(extensions) {
            displayTypeOptionsString.add(DisplayTypeOptions.extensions.name());
        }
        
        if(implementations) {
            displayTypeOptionsString.add(DisplayTypeOptions.implementations.name());
        }
        
        if(imports) {
            displayTypeOptionsString.add(DisplayTypeOptions.imports.name());
        }
        
        if(interfaces) {
            displayTypeOptionsString.add(DisplayTypeOptions.interfaces.name());
        }
        
        if(nativeMethods) {
            displayTypeOptionsString.add(DisplayTypeOptions.native_methods.name());
        }
        
        if(staticImports) {
            displayTypeOptionsString.add(DisplayTypeOptions.static_imports.name());
        }
        
        return displayTypeOptionsString.toString();
    }
    
    public File getOutputFile() {
        if (StringUtils.isEmpty(destinationDirectory) || StringUtils.isEmpty(outputFileName)) {
            return null;
        }
        
        return new File(destinationDirectory + "/" + outputFileName + ".puml");
    }

    public File getSourcesDirectoryFile() {
        if (StringUtils.isEmpty(sourcesDirectory)) {
            return null;
        }
        
        return new File(sourcesDirectory);
    }
}
