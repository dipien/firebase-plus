package com.dipien.template

import com.dipien.template.task.TemplateTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class TemplateGradlePlugin : Plugin<Project> {

    companion object {
        const val EXTENSION_NAME = "templateExtension"
    }

    override fun apply(project: Project) {
        val extension = project.extensions.create(EXTENSION_NAME, TemplateExtension::class.java)

        val task = project.tasks.create(TemplateTask.TASK_NAME, TemplateTask::class.java)
    }
}
