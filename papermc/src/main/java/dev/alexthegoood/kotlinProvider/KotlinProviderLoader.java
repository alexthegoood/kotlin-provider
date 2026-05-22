package dev.alexthegoood.kotlinProvider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

    private final String fileName = "loadData";

    @Override
    public void classloader(final PluginClasspathBuilder builder) {
        URL resource = this.getClass().getResource("/" + fileName + ".json");
        if (resource == null) throw new RuntimeException("File " + fileName + ".json not found");

        MavenLibraryResolver resolver = new MavenLibraryResolver();
        JsonObject loadData;

        try (InputStreamReader reader = new InputStreamReader(resource.openStream(), StandardCharsets.UTF_8)) {
            loadData = JsonParser.parseReader(reader).getAsJsonObject();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        JsonArray repositories = loadData.getAsJsonArray("repositories");
        for (JsonElement repository : repositories) {
            JsonArray arr = repository.getAsJsonArray();
            String id = arr.get(0).getAsString();
            String url = arr.get(1).getAsString();
            resolver.addRepository(new RemoteRepository.Builder(id, "default", url).build());
        }

        JsonArray libraries = loadData.getAsJsonArray("libraries");
        for (JsonElement library : libraries) {
            JsonArray libraryArray = library.getAsJsonArray();
            String url = libraryArray.get(0).getAsString();
            resolver.addDependency(new Dependency(new DefaultArtifact(url), null));
        }

        builder.addLibrary(resolver);
    }

}
