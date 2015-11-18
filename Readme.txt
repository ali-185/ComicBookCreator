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

To compile and run the app you need javaws.jar. It can be collected from your
Java installation, jre\lib\javaws.jar.

+++++++++++++++++++++++
+  Compiling the Jar  +
+++++++++++++++++++++++
To compile the code on Windows, run the following a the command prompt:

> mkdir bin
> javac -d bin -classpath "src;javaws.jar" src\comicBookGUI\ComicBookApp.java
> copy resource\ bin\
> mkdir bin\JNLP-INF
> copy APPLICATION.JNLP bin\JNLP-INF\
> jar cvfm ComicBookCreator.jar MANIFEST.MF -C bin .

+++++++++++++++++++++++
+   Signing the Jar   +
+++++++++++++++++++++++

You can then either sign the jar or add the jar to Java's security exception
list. To sign the Jar:

Create your certificate:
1. Go on the "Certum Certification" website in the "OpenSource Code Signing"
   section. Since 2015 it's not free anymore but costs only 15€.
2. Follow the certificate creation procedure. You'll receive a mail asking
   for some official documents (ID card, rent bill, etc.) for activating the
   certificate. 
3. Proceed the activation of the certificate. You'll receive another mail
   asking for installing the certificate.
4. Install online your certificate using Google Chrome web browser (make sure
   you launched Chrome with the admin rights or you'll not be able to export
   the private key).
Obtain a PFX file:
5. Open Chrome settings >> Manage Certificates
6. Export your certificate as "certificate.pfx" file using the following
   parameters: 
   •Yes, export private key
   •PKCS#12 type
   •Check include certificate chain
Sign your jar file:
7. Find the alias used in your PFX file using the following command:
   > keytool -list -v -storetype pkcs12 -keystore certificate.pfx | find ^
   "Alias name:"
8. Then sign your jar file using the following command:
   > jarsigner -storetype pkcs12 -keystore certificate.pfx ^
   ComicBookCreator.jar "[AliasName]"


Alternatively, yo bypass this add the jnlp location to Java's security
exception list:

Start > Control Panel > Programs > Java > "Security" tab >
"Edit Site List..." button

+++++++++++++++++++++++
+   Running the Jar   +
+++++++++++++++++++++++

To launch the application from a hosted website place the following files in
the website directory:

index.html
APPLICATION.JNLP

If you wish to host the ComicBookCreator.jar file as well then the
APPLICATION.JNLP will need to have its codebase attribute updated and then
the jar recreated.

Alternatively, if you wish to launch the application locally then run:

> java -classpath ComicBookCreator.jar;javaws.jar comicBookGUI.ComicBookApp

Note that the load and save functions will not work as they require the Java
Web Service to be running.