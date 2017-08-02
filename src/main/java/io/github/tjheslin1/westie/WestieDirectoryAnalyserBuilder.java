/*
 * Copyright 2017 Thomas Heslin <tjheslin1@gmail.com>.
 *
 * This file is part of Westie.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.tjheslin1.westie;

import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

import java.nio.file.Path;

public class WestieDirectoryAnalyserBuilder {

    private final WestieFileReader fileReader;
    private final Path pathToCheck;

    private String filetype;

    public WestieDirectoryAnalyserBuilder(WestieFileReader fileReader, Path pathToCheck) {
        this.fileReader = fileReader;
        this.pathToCheck = pathToCheck;
    }

    /**
     * Sets type of files to be analysed to '.java'.
     *
     * @return A {@link WestieDirectoryAnalyser} with the 'fileType' set to '.java'.
     */
    public WestieDirectoryAnalyser forJavaFiles() {
        return forFileType(".java");
    }

    /**
     * Sets type of files to be analysed to '.properties'.
     *
     * @return A {@link WestieDirectoryAnalyser} with the 'fileType' set to '.properties'.
     */
    public WestieDirectoryAnalyser forPropertiesFiles() {
        return forFileType(".properties");
    }

    /**
     * Sets type of files to be analysed.
     *
     * @param filetype The suffix of files to be analysed.
     * @return A {@link WestieDirectoryAnalyser} with the 'fileType' set to the provided value.
     */
    public WestieDirectoryAnalyser forFileType(String filetype) {
        if (!filetype.startsWith(".")) {
            this.filetype = "." + filetype;
        } else {
            this.filetype = filetype;
        }

        return new WestieDirectoryAnalyser(pathToCheck, filetype, fileReader);
    }

    /**
     * Does not set a 'fileType', resulting in all regular files being analysed.
     *
     * @return A {@link WestieDirectoryAnalyser} with the 'fileType' set to '.properties'.
     */
    public WestieDirectoryAnalyser forAllFiles() {
        return new WestieDirectoryAnalyser(pathToCheck, filetype, fileReader);
    }
}
