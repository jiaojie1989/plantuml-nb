PlantUML Dependency - reverse engineering java source files to generate PlantUML description (http://plantuml-depend.sourceforge.net)
=====================================================================================================================================

?, Dec 2013 - 1.2.0
====================
- Introducing a new and powerful feature : the Display option which allows to filter output elements and relations, such as classes, interfaces, enums, abstract classes, annotations and so on... Ant task and help messages have been updated. (feature 3)
- Annotations java files are now fully supported (feature 22 and bug 22)
- Elements (classes, enums, interfaces abstract classes and annotations) are now better sorted in the generated output file
- Fixed a bug with dependencies which have a similar name to a java.lang class or interface (bug 5)
- Fixed a bug which causes to badly parse class when annotations are used (bug 3)
- Fixed a bug which causes to write import and native dependencies in the output file from string contents contained in the parsed source files. String contents are now removed from the source code before analysis
- Fixed a bug which causes the program to stop when a java source file couldn't be read (bug 2)
- Fixed a bug with native dependency regular expression which didn't work in some particular cases
- Updated junit dependency version to 4.11 (was 4.7)
- Updated cobertura-maven-plugin dependency version to 2.5.2 (was 2.4)
- Updated maven-compiler-plugin dependency version to 3.1 (was 2.3.2)
- Updated maven-jar-plugin dependency version to 2.4 (was 2.3.1)
- Updated maven-source-plugin dependency version to 2.2.1 (was 2.1.2)
- Updated maven-javadoc-plugin dependency version to 2.9.1 (was 2.7)
- Updated maven-site-plugin dependency version to 3.3 (was 3.0)
- Updated maven-surefire-report-plugin dependency version to 2.15 (was 2.7.1)
- Updated maven-jxr-plugin dependency version to 2.3 (was 2.2)
- Updated maven-pmd-plugin dependency version to 3.0.1 (was 2.5)
- Updated findbugs-maven-plugin dependency version to 2.5.2 (was 2.3.1)
- Updated maven-checkstyle-plugin dependency version to 2.10 (was 2.6)
- Updated org.apache.ant dependency version to 1.8.4 (was 1.8.2)

5, Jul 2011 - 1.1.0
====================
- Ant task available
- Fixed a bug with the verbose level option, which was reloading the "logging.properties" file configuration
- Improving the way to get the program version, by using a properties file and managing to read a Maven version pattern

28, May 2011 - 1.0.1
====================
- Fixed an error when generating description with classes which have static imports

2, Dec 2010 - 1.0.0
====================
- First public release
- Fixed and issue when generating description with class which has a comment on the same line as the class declaration
- Fixed and issue when generating description with class importing all classes of a package, using the "*" character
- Fixed and issue when generating description with inner class, bad class order declaration
- Fixed and issue when generating description with generic class

Command line Usage
====================

Usage:
	java -jar plantuml-dependency-1.2.0.jar [OPTIONS]


where optional options are:

	-about, --author, --authors
		To display information about PlantUML Dependency, its license and its authors.

	-b, --basedir DIR
		The base directory where to look for source files. If not specified, the default pattern is "." i.e. the directory where the program is launched from.
		DIR specifies a valid and existing directory path, not a single file. It can be absolute or relative.

	-d, --display DISPLAY_OPTIONS
		To specify class diagram objects to display. If not specified, the default is [abstract_classes,annotations,classes,enums,extensions,implementations,imports,interfaces,native_methods,static_imports]
		DISPLAY_OPTIONS specifies display options when generating the plantuml output file, it is a separated comma list with these possible values : [abstract_classes,annotations,classes,enums,extensions,implementations,imports,interfaces,native_methods,static_imports]. "abstract_classes" : displays parsed source files which are abstract classes and relations to abstract classes, "annotations" : displays parsed source files which are annotations, annotations (upon classes and methods) of all parsed source files and relations to annotations, "classes" : displays parsed source files which are classes (not abstract), dependencies which are considered as classes (because they are imported or extended but not parsed) and relations to classes, "enums" : displays parsed source files which are enums and relations to enums, "extensions" : displays relations between dependencies which are extended by parsed source files (i.e. classes or interfaces) if their type is displayed, "implementations" : displays relations between dependencies which are implemented by parsed source files (i.e. interfaces) if their type is displayed, "imports" : displays relations from parsed source files to import dependencies (not static) if their type is displayed, "interfaces" : displays parsed source files which are interfaces, dependencies which are considered as interfaces (because they are implemented but not parsed) and relations to interfaces, "native_methods" : displays relations from parsed source files to the native dependency if they use native methods, "static_imports" : displays relations from parsed source files to import dependencies (only static) if their type is displayed.

	-e, --exclude FILE_PATTERN
		To exclude files that match the provided pattern. If not specified, no file is excluded.
		FILE_PATTERN specifies a file pattern, with the same syntax as ANT patterns. It means that "**", "*" or "?" special characters can be used. For more information, please consult http://ant.apache.org/manual/dirtasks.html.

	-h, --help, -?
		To display this help message.

	-i, --include FILE_PATTERN
		To include files that match the provided pattern. If not specified, the default pattern is "**/*.java".
		FILE_PATTERN specifies a file pattern, with the same syntax as ANT patterns. It means that "**", "*" or "?" special characters can be used. For more information, please consult http://ant.apache.org/manual/dirtasks.html.

	-o, --output FILE
		To specify the output file path where to generate the PlantUML description.
		FILE specifies a valid file path, where the file can exist or not and is not a directory. It can be absolute or relative. If the file already exists, it overrides it.

	-v, --verbose [VERBOSE_LEVEL]
		To display log information.
		VERBOSE_LEVEL specifies the verbose level. The argument may consist of either a level name or an integer value. Classical values are : "SEVERE":1000, "WARNING":900, "INFO":800, "CONFIG":700, "FINE":500, "FINER":400, "FINEST":300. By default, if the verbose option is specified but the level is not set, the value "INFO":800 is taken. If not specified, the default value is "WARNING":900.

	-version
		To display versions information about PlantUML Dependency and Java.

Examples:

	java -jar plantuml-dependency-1.2.0.jar -h
	java -jar plantuml-dependency-1.2.0.jar -o /home/test/plantuml.txt -b . -i **/*.java -e **/*Test*.java -d implementations,interfaces,extensions,imports,static_imports -v
	java -jar plantuml-dependency-1.2.0.jar -o plantuml.txt -b "C:\Users\PlantUML test" -i **/*Test.java
	java -jar plantuml-dependency-1.2.0.jar -version -v

Known bugs or program limitations:

	- As PlantUML Dependency reads source files imports, it will generate object - imports relation even if the imports are not used within the object (usually, this raises a warning within the IDE)
	- Be careful, in order to correctly parse source files, it is better to have source code which compiles without any errors
	- Generated annotations (if used) are only supported by PlantUML 7972 and above
	- Import instructions "import package_name.*" are ignored because the dependencies are not explicitly defined, use precise imports instead
	- Links between dependencies are found out by parsing "import" instructions, so PlantUML Dependency won't display dependencies which are called using their full names in the source code
	- PlantUML Dependency can be run by JRE 1.6+
	- PlantUML Dependency is fully compliant up to Java 1.6 source files (and may work with Java 1.7 source files)
	- The generated output file is better when PlantUML Dependency is launched with a JRE matching the parsed source files
