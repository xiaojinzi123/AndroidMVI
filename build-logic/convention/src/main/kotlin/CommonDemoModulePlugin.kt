import org.gradle.api.Project

class CommonDemoModulePlugin: CommonModulePlugin() {

    override fun apply(project: Project) {
        super.apply(project)
        with(project) {
            dependencies.apply {
                add("implementation", project(":template"))
            }
        }
    }

}