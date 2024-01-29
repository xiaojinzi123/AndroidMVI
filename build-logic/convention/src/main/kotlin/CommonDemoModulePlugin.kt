import org.gradle.api.Project

class CommonDemoModulePlugin: CommonModulePlugin() {

    override fun apply(project: Project) {
        super.apply(project)
        with(project) {
            plugins.apply {
                apply("com.google.devtools.ksp")
            }
        }
        with(project) {
            dependencies.apply {
                add("api", project(":demo:module-base"))
                add("ksp", libs.findLibrary("kcomponent-compiler"))
            }
        }
    }

}