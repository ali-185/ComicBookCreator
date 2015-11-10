 /*
  * Comic Book Creator - A program for creating a comic book photo album.
  * Copyright (C) 2013  Alastair Crowe
  *
  * This code is free software; you can redistribute it and/or modify it
  * under the terms of the GNU General Public License version 2 only, as
  * published by the Free Software Foundation.
  *
  * This code is distributed in the hope that it will be useful, but WITHOUT
  * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
  * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
  * version 2 for more details.
  
  * You should have received a copy of the GNU General Public License version
  * 2 along with this work; if not, write to the Free Software Foundation,
  * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
  *
  * Please contact comicbookhelp@gmail.com if you need additional information
  * or have any questions.
  */

Comic Book Creator Readme

To compile and run the app you need javaws.jar. It can be collected from your Java installation, jre\lib\javaws.jar.

To compile the code on Windows, run the following a the command prompt:

> mkdir bin
> javac -d bin -classpath "src;javaws.jar" src\comicBookGUI\ComicBookApp.java
> copy resource\ bin\
> jar cvf ComicBookCreator.jar -C bin .

Note that you may get a Java security warning when running the application. To bypass this add the jnlp location to Java's security exception list:

Start > Control Panel > Programs > Java > "Security" tab > "Edit Site List..." button

To launch the application from a hosted website place the following files in the website directory:

index.html
ComicBookCreator.jnlp
ComicBookCreator.jar
javaws.jar

Alternatively, if you wish to launch the application locally then run:

> java -classpath ComicBookCreator.jar;javaws.jar comicBookGUI.ComicBookApp

Note that the load and save functions will not work as they require the Java Web Service to be running.