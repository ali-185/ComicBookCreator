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
package comicBookModel;

import java.util.LinkedList;

/** 
 * A ComicBook is an ordered list of one or more ComicPages. The ComicPages
 * are all the same width and height.
 * 
 * @author Alastair Crowe 
 */
public class ComicBook {
    private final int width;
    private final int height;
    private LinkedList<ComicPage> pages = new LinkedList<ComicPage>();
    /**
     * Creates a ComicBook with all pages the specified width and height. The 
     * initial ComicBook has only one ComicPage, at index 0.
     * @param width
     * @param height
     */
    public ComicBook(int width, int height) {
        this.width = width;
        this.height = height;
        addPage();
    }
    /**
     * Adds a new blank ComicPage to the end of the comic book. The first ComicPage
     * is at index 0.
     */
    void addPage() {
        ComicPage page = new ComicPage(width, height);
        pages.add(page);
    }
    /**
     * Removes the ComicPage at the specified index. 
     * @param index
     */
    void removePage(int index) {
        pages.remove(index);
        if(noOfPages() == 0){
            addPage();
        }
    }
    /**
     * Gets the ComicPage at the specified index.
     * @param index
     * @return The specified ComicPage
     */
    public ComicPage getPage(int index) {
        return pages.get(index);
    }
    /**
     * The number of ComicPages in this ComicBook
     * @return The number of ComicPages.
     */
    int noOfPages() {
        return pages.size();
    }
}
