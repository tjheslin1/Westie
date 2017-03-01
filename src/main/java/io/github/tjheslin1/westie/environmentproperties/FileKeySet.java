package io.github.tjheslin1.westie.environmentproperties;

import io.github.tjheslin1.westie.ValueType;

import java.nio.file.Path;
import java.util.Set;

/**
 * Represents a properties file and its set of property names (keys).
 */
public class FileKeySet extends ValueType {

    public final Path file;
    public final Set<Object> keySet;

    private FileKeySet(Path file, Set<Object> keySet) {
        this.file = file;
        this.keySet = keySet;
    }

    public static FileKeySet fileKeySet(Path file, Set<Object> keySet) {
        return new FileKeySet(file, keySet);
    }

    /**
     * @return the size of the list of property names (keys).
     */
    public int numKeys() {
        return keySet.size();
    }
}
