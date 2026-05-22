package dev.alexthegoood.kotlinProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.papermc.paper.plugin.loader.PluginClasspathBuilder;
import io.papermc.paper.plugin.loader.PluginLoader;
import io.papermc.paper.plugin.loader.library.impl.MavenLibraryResolver;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.graph.Dependency;
import org.eclipse.aether.repository.RemoteRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;

class KotlinProviderLoader implements PluginLoader {

    private final String DATA_FILE = "loadData.json";

    @Override
    public void classloader(final PluginClasspathBuilder builder) {
        URL resource = this.getClass().getResource("/" + DATA_FILE);
        if (resource == null) throw new RuntimeException("Required resource file '" + DATA_FILE + "' could not be found in the plugin jar");

        MavenLibraryResolver resolver = new MavenLibraryResolver();

        // repositories
        resolver.addRepository(new RemoteRepository.Builder("PaperMC", "default", "https://repo.papermc.io/repository/maven-public/").build());

        // libraries
        try (InputStreamReader reader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
            JsonArray loadData = JsonParser.parseReader(reader).getAsJsonArray();
            if (loadData != null) for (JsonElement element : loadData) resolver.addDependency(new Dependency(new DefaultArtifact(element.getAsString()), null));
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + DATA_FILE + " due to an I/O error", e);
        } catch (IllegalStateException | IndexOutOfBoundsException | NullPointerException e) {
            throw new IllegalStateException("Failed to parse " + DATA_FILE + ". Ensure the JSON structure is valid.", e);
        }

        builder.addLibrary(resolver);
    }

}
