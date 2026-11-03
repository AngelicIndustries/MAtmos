import net.darkhax.curseforgegradle.TaskPublishCurseForge
import org.gradle.api.publish.maven.MavenPublication

plugins {
    id("com.gtnewhorizons.gtnhconvention")
}

apply(from = "https://raw.githubusercontent.com/AngelicIndustries/67minecraft-gradle-publish/${property("publish67ScriptTag")}/67minecraft-publish.gradle.kts")

// Publish under Angelic coordinates while preserving the original Java packages.
publishing {
    publications.withType<MavenPublication>().configureEach {
        groupId = "industries.angelic.matmos"
    }
}

tasks.withType<TaskPublishCurseForge>().configureEach {
    uploadArtifacts.forEach { it.addEnvironment("Client") }
}
