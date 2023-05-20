import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.example"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(17)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation("com.darkrockstudios:mpfilepicker:1.0.0")
                implementation(compose.desktop.currentOs)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.material3)
                implementation("commons-io:commons-io:2.6")
            }
        }
        val jvmTest by getting
    }
    jvmToolchain(17)
}

compose.desktop {
    application {
        mainClass = "ui.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "EpisodeRenamer"
            packageVersion = "1.0.0"
            vendor = "SilentBit Studios"
            description = "Rename episodes of tv shows or anime"

            windows {
                iconFile.set(project.file("src/jvmMain/resources/EpisodeRenamer.ico"))
                menuGroup = "SilentBit"
                perUserInstall = true
                dirChooser = true
                upgradeUuid = "UUID"
            }
            linux {
                iconFile.set(project.file("src/jvmMain/resources/EpisodeRenamer.png"))
                //debMaintainer = "maintainer@example.com"
                appCategory = "Utility"
                appRelease = "1"
            }
            macOS {
                iconFile.set(project.file("icon.icns"))
                dockName = "Episode Renamer"
            }
        }
    }
}