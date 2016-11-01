package io.github.tjheslin1.westie;

import org.mockito.Mockito;
import org.mockito.stubbing.OngoingStubbing;

public interface WithMockito {

    default <T> T mock(Class<T> classToMock) {
        return Mockito.mock(classToMock);
    }

    default <T> OngoingStubbing<T> when(T methodCall) {
        return Mockito.when(methodCall);
    }
}
