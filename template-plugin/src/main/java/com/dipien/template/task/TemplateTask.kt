package com.dipien.template.task

import com.dipien.template.common.AbstractTask

open class TemplateTask : AbstractTask() {

    companion object {
        const val TASK_NAME = "template"
    }

    init {
        group = "Template Task Group"
        description = "Template Task Description"
    }

    override fun onExecute() {
    }
}
