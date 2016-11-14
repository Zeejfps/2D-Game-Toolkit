package gametoolkit.utils;

import org.lwjgl.BufferUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by Zeejfps on 11/13/2016.
 */
public class IOUtils {

    private IOUtils() {
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        ByteBuffer newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer resourceToByteBuffer(String resource, int bufferSize) throws IOException {
        ByteBuffer data;

        Path path = Paths.get(resource);
        if ( Files.isReadable(path) ) {
            try (SeekableByteChannel fc = Files.newByteChannel(path)) {
                data = BufferUtils.createByteBuffer((int)fc.size() + 1);
                while ( fc.read(data) != -1 ) ;
            }
        } else {
            try (
                    InputStream source = IOUtils.class.getClassLoader().getResourceAsStream(resource);
                    ReadableByteChannel rbc = Channels.newChannel(source)
            ) {
                data = BufferUtils.createByteBuffer(bufferSize);

                while ( true ) {
                    int bytes = rbc.read(data);
                    if ( bytes == -1 )
                        break;
                    if ( data.remaining() == 0 )
                        data = resizeBuffer(data, data.capacity() * 2);
                }
            }
        }

        data.flip();
        return data;
    }

}
