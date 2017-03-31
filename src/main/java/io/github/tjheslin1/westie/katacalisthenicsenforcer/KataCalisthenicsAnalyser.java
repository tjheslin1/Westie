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
package io.github.tjheslin1.westie.katacalisthenicsenforcer;

import io.github.tjheslin1.westie.WestieAnalyser;
import io.github.tjheslin1.westie.infrastructure.WestieFileReader;

/**
 * Not yet in use.
 */
public class KataCalisthenicsAnalyser {

    private final WestieAnalyser westieAnalyser;

    public KataCalisthenicsAnalyser() {
        this.westieAnalyser = new WestieAnalyser();
    }

    /**
     * Not yet in use.
     */
    public KataCalisthenicsAnalyser(WestieFileReader fileReader) {
        this.westieAnalyser = new WestieAnalyser(fileReader);
    }
}
