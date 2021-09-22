package com.dipien.template.common

import com.dipien.template.TemplateExtension
import com.dipien.template.TemplateGradlePlugin
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class AbstractTask : DefaultTask() {

    @get:Input
    var verbose = false

    @TaskAction
    fun doExecute() {

        LoggerHelper.logger = logger
        LoggerHelper.verbose = verbose

        onExecute()
    }

    @Internal
    protected fun getExtension(): TemplateExtension {
        return project.extensions.getByName(TemplateGradlePlugin.EXTENSION_NAME) as TemplateExtension
    }

    protected abstract fun onExecute()
}
