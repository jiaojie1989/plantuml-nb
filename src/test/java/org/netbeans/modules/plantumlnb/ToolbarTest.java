/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.netbeans.modules.plantumlnb;

import org.netbeans.modules.plantumlnb.ui.Toolbar;
import javax.swing.JToolBar;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author venkat
 */
public class ToolbarTest {
    
    JToolBar jtoolbar;
    Toolbar toolbar;
    
    public ToolbarTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        toolbar = Toolbar.instance();
    }
    
    @After
    public void tearDown() {
    }
    
    @Test
    public void testGetExportButton() {
        toolbar.getExportButton();
    }
}