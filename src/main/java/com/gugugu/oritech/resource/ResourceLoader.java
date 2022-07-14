package com.gugugu.oritech.resource;

import com.gugugu.oritech.renderer.Shader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * @author theflysong
 * @author squid233
 * @since 1.0
 */
public class ResourceLoader {
    public static InputStream loadFile(String name) {
        return ResourceLoader.class.getClassLoader().getResourceAsStream(name);
    }

    public static InputStream loadFile(ResLocation location) {
        return loadFile(location.toPath());
    }

    public static String loadText(ResLocation location) {
        StringBuilder sb = new StringBuilder();
        InputStream file = loadFile(location);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file, StandardCharsets.UTF_8))) {
            String line = br.readLine();
            if (line != null) {
                sb.append(line);
                while ((line = br.readLine()) != null) {
                    sb.append("\n").append(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Shader loadShader(ResLocation vertexLocation, ResLocation fragmentLocation) {
        String vertexShader = loadText(vertexLocation);
        String fragmentShader = loadText(fragmentLocation);

        return new Shader(vertexShader, fragmentShader);
    }
}
