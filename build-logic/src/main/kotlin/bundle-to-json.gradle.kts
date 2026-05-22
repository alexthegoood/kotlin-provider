import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

plugins { java }

interface BundleToJsonExtension {
    @get:Input val fileName: Property<String>
    @get:Input val bundleName: Property<String>
    @get:Input val catalogName: Property<String>
}

abstract class BundleToJsonTask : DefaultTask() {
    @get:Input abstract val libraries: ListProperty<String>
    @get:OutputFile abstract val outputFile: RegularFileProperty

    @TaskAction
    fun execute() {
        val jsonString = Json { prettyPrint = true }.encodeToString(libraries.get())
        val file = outputFile.get().asFile
        file.parentFile.mkdirs()
        file.writeText(jsonString)
    }
}

val config = project.extensions.create<BundleToJsonExtension>("bundleToJson").apply {
    catalogName.convention("libs")
}

val bundleToJson = project.tasks.register<BundleToJsonTask>("bundleToJson") {
    val bundleNameProvider = config.bundleName
    val catalogNameProvider = config.catalogName

    outputFile = project.layout.buildDirectory.file("generated/internal/main/${config.fileName.get()}.json")

    libraries = project.provider {
        val catalog = project.extensions.getByType<VersionCatalogsExtension>().named(catalogNameProvider.get())
        val bundle = catalog.findBundle(bundleNameProvider.get())

        if (!bundle.isPresent) throw GradleException(
            "Bundle '${bundleNameProvider.get()}' not found in catalog '${catalogNameProvider.get()}'"
        )

        bundle.get().get().map { "${it.module}:${it.version}" }
    }
}

project.tasks.processResources {
    dependsOn(project.tasks.named("bundleToJson"))
}

project.the<SourceSetContainer>().named("main") {
    resources.srcDir(bundleToJson.map { it.outputFile.get().asFile.parentFile })
}