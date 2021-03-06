/*
 * jcurl curling simulation framework 
 * Copyright (C) 2005 M. Rohrmoser
 * 
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation; either version 2 of the License, or (at your
 * option) any later version.
 * 
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 */
package org.jcurl.core.swing;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class PngFileChooser extends JFileChooser {

    private static final long serialVersionUID = 400354997568206163L;

    public PngFileChooser(File currentFile) {
        super(currentFile == null ? new File(".") : currentFile
                .isDirectory() ? currentFile : currentFile.getParentFile());
        this.setMultiSelectionEnabled(false);
        this.setAcceptAllFileFilterUsed(true);
        this.setFileFilter(new FileFilter() {
            public boolean accept(final File f) {
                if (f == null)
                    return false;
                return f.isDirectory() || f.getName().endsWith(".png");
            }

            public String getDescription() {
                return "Portable Network Graphics (.png)";
            }
        });
    }
}