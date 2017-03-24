/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.netbeans.modules.plantumlnb.generate;

import java.io.File;
import java.net.MalformedURLException;
import java.text.ParseException;
import net.sourceforge.plantumldependency.cli.main.program.PlantUMLDependencyProgram;
//import net.sourceforge.mazix.cli.command.CommandLine;
//import net.sourceforge.mazix.cli.command.impl.CommandLineImpl;
//import net.sourceforge.mazix.cli.exception.CommandLineException;
//import net.sourceforge.mazix.cli.program.JavaProgram;
//import net.sourceforge.mazix.cli.program.execution.JavaProgramExecution;
//import net.sourceforge.plantuml.dependency.main.program.PlantUMLDependencyProgram;
import net.sourceforge.plantumldependency.commoncli.command.CommandLine;
import net.sourceforge.plantumldependency.commoncli.command.impl.CommandLineImpl;
import net.sourceforge.plantumldependency.commoncli.exception.CommandLineException;
import net.sourceforge.plantumldependency.commoncli.program.JavaProgram;
import net.sourceforge.plantumldependency.commoncli.program.execution.JavaProgramExecution;

/**
 *
 * @author Venkat Ram Akkineni
 */
public class PlantUMLDependencyService {
    

    public static void generate(File directory, File outputFile) throws MalformedURLException, CommandLineException, ParseException {

        // Creates the PlantUML Dependency arguments as they would be written in the command line
//        final CommandLine commandLineArguments = new CommandLineImpl(new String[] {"-o", "plantuml-jdk-1.7.0.45.txt", "-b", "C:\\jdk-1.7.0.45-src", "-e", "**/package-info.java"});
        final CommandLine commandLineArguments = new CommandLineImpl(new String[] {
            "-o", outputFile.getAbsolutePath(), 
            "-b", directory.getAbsolutePath(), 
            "-e", "**/package-info.java"
        });

        // Creates the PlantUML Dependency program instance
        final JavaProgram plantumlDependencyProgram = new PlantUMLDependencyProgram();

        // Get the PlantUML Dependency program execution instance following the command line arguments, ready to be executed
        final JavaProgramExecution plantumlDependencyProgramExecution = plantumlDependencyProgram.parseCommandLine(commandLineArguments);

        // Executes the PlantUML Dependency program
        plantumlDependencyProgramExecution.execute();
    }
    
    
}