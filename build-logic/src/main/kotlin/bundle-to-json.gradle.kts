import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.gradle.api.artifacts.repositories.MavenArtifactRepository
import org.gradle.api.artifacts.VersionCatalogsExtension

plugins { java }

interface BundleToJsonExtension {
    @get:Input val fileName: Property<String>
    @get:Input val bundleName: Property<String>
    @get:Input val catalogName: Property<String>
}

val config = project.extensions.create<BundleToJsonExtension>("bundleToJson").apply {
    catalogName.convention("libs")
}

val outputFileProvider = config.fileName.flatMap { fileName ->
    project.layout.buildDirectory.file("generated/internal/main/$fileName.json")
}

project.tasks.register("bundleToJson") {
    val outputFileProvider = outputFileProvider
    val bundleNameProvider = config.bundleName
    val catalogNameProvider = config.catalogName

    val repositoriesProvider = project.provider {
        project.repositories
            .filterIsInstance<MavenArtifactRepository>()
            .map { listOf(it.name, it.url.toString()) }
    }

    val librariesProvider = project.provider {
        val catalog = project.extensions.getByType<VersionCatalogsExtension>().named(catalogNameProvider.get())
        val bundle = catalog.findBundle(bundleNameProvider.get())

        if (!bundle.isPresent) throw GradleException(
            "Bundle '${bundleNameProvider.get()}' not found in catalog '${catalogNameProvider.get()}'"
        )

        bundle.get().get().map { listOf("${it.module}:${it.version}") }
    }

    inputs.property("repositories", repositoriesProvider)
    inputs.property("libraries", librariesProvider)
    outputs.file(outputFileProvider)

    doLast {
        val jsonString = Json { prettyPrint = true }.encodeToString(mapOf(
            "repositories" to repositoriesProvider.get(),
            "libraries" to librariesProvider.get()
        ))

        val file = outputFileProvider.get().asFile
        file.parentFile.mkdirs()
        file.writeText(jsonString)
    }
}

project.tasks.processResources {
    dependsOn(project.tasks.named("bundleToJson"))
}

project.the<SourceSetContainer>().named("main") {
    resources.srcDir(outputFileProvider.map { it.asFile.parentFile })
}