import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML

plugins {
  alias(libs.plugins.androidApplication) apply false
  alias(libs.plugins.androidLibrary) apply false
  alias(libs.plugins.composeMultiplatform) apply false
  alias(libs.plugins.composeCompiler) apply false
  alias(libs.plugins.kotlinMultiplatform) apply false
  alias(libs.plugins.androidKotlinMultiplatformLibrary) apply false
  alias(libs.plugins.ktlint) apply false
  id("com.google.gms.google-services") version "4.4.4" apply false
  idea
}

idea {
  module {
    isDownloadSources = true
    isDownloadJavadoc = true
  }
}

tasks.withType<Wrapper>().configureEach {
  distributionType = Wrapper.DistributionType.ALL
}

allprojects {
  afterEvaluate {
    plugins.withId("org.jlleitschuh.gradle.ktlint") {
      extensions.configure<KtlintExtension> {
        reporters {
          reporter(HTML)
        }
        filter {
          exclude {
            it.file.toString().contains("build/generated")
          }
        }
      }
    }
  }
}
