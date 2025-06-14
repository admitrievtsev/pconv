package core.convoulter;

import java.util.concurrent.Callable;

public class Cbyte implements Callable<Byte> {
    public byte value;

    public Cbyte(byte v) {
        this.value = v;
    }

    @Override
    public Byte call() throws Exception {
        return value;
    }
}
