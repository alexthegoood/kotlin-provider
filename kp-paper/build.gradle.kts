plugins {
    alias(libs.plugins.kotlin)
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.resource.factory.paper)
    id("bundle-to-json")
}

base {
    archivesName = "kotlin-provider"
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api.get())
    api(libs.bundles.kotlin.libraries)
}

paperPluginYaml {
    name = rootProject.name
    main = "${project.group}.kotlinProvider.KotlinProvider"
    loader = "${project.group}.kotlinProvider.KotlinProviderLoader"
    apiVersion = libs.versions.minecraft.get()

    author = "alexthegoood"
}

bundleToJson {
    fileName = "loadData"
    bundleName = "kotlin-libraries"
}

kotlin {
    jvmToolchain(25)
}

tasks {
    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        jvmArgs("-Xms2G", "-Xmx2G", "-Dcom.mojang.eula.agree=true")
    }
}
