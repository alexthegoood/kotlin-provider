plugins {
    kotlin("jvm") version libs.versions.kotlin.get()
    alias(libs.plugins.paperweight.userdev)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.resource.factory.paper)
}

repositories {
    mavenCentral()
}

dependencies {
    paperweight.paperDevBundle(libs.versions.paper.api.get())
    compileOnly(libs.kotlin.stdlib)
}

paperPluginYaml {
    main = "${project.group}.kotlinProvider.KotlinProvider"
    loader = "${project.group}.kotlinProvider.KotlinProviderLoader"
    apiVersion = libs.versions.minecraft.get()

    author = "alexthegoood"
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
