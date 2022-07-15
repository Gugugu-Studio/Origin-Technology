package com.gugugu.oritech.resource;

import com.gugugu.oritech.renderer.Shader;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.BufferUtils;

import java.io.*;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public class ResourceLoader {
    @Nullable
    public static InputStream loadFile(String name) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(name);
    }

    @Nullable
    public static InputStream loadFile(ResLocation location) {
        return loadFile(location.toPath());
    }

    public static String loadText(ResLocation location) {
        StringBuilder sb = new StringBuilder();
        InputStream file = Objects.requireNonNull(loadFile(location), "Couldn't load file from " + location);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                sb.append(line);
                while ((line = br.readLine()) != null) {
                    sb.append("\n").append(line);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load text from " + location, e);
        }
        return sb.toString();
    }

    private static ByteBuffer resizeBuffer(ByteBuffer buffer, int newCapacity) {
        var newBuffer = BufferUtils.createByteBuffer(newCapacity);
        buffer.flip();
        newBuffer.put(buffer);
        return newBuffer;
    }

    public static ByteBuffer loadToByteBuffer(ResLocation location, int bufferSize)
        throws IOException {
        URL url = Thread.currentThread().getContextClassLoader().getResource(location.toPath());
        if (url == null) {
            throw new IOException("Classpath resource not found: " + location);
        }
        File file = new File(url.getFile());
        if (file.isFile()) {
            try (FileInputStream fis = new FileInputStream(file);
                 FileChannel fc = fis.getChannel()) {
                return fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            }
        }
        ByteBuffer buffer = BufferUtils.createByteBuffer(bufferSize);
        InputStream source = url.openStream();
        if (source == null) {
            throw new FileNotFoundException(location.toPath());
        }
        try (source) {
            byte[] buf = new byte[8192];
            while (true) {
                int bytes = source.read(buf);
                if (bytes == -1)
                    break;
                if (buffer.remaining() < bytes)
                    buffer = resizeBuffer(buffer, Math.max(buffer.capacity() * 2, buffer.capacity() - buffer.remaining() + bytes));
                buffer.put(buf, 0, bytes);
            }
            buffer.flip();
        }
        return buffer;
    }

    public static Shader loadShader(ResLocation vertexLocation, ResLocation fragmentLocation) {
        String vertexShader = loadText(vertexLocation);
        String fragmentShader = loadText(fragmentLocation);

        return new Shader(vertexShader, fragmentShader);
    }
}
