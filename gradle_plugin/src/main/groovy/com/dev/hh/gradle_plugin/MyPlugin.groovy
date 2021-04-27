package com.dev.hh.gradle_plugin

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task

/**
 * Package: com.dev.hh.gradle_plugin
 * User: hehao3
 * Email: hehao3@jd.com
 * Date: 2021/4/26
 * Time: 下午6:51
 * Description: 插件入口
 */
class MyPlugin implements Plugin<Project> {
    static final String EXTENSION_NAME = 'lintCleanerConfig'

    @Override
    void apply(Project project) {
        // 获取外部参数
        project.extensions.create(EXTENSION_NAME, PluginExtension, project)

        // 创建清理任务
        Task cleanTask = project.tasks.create(CleanTask.TASK_NAME, CleanTask)

        // 执行完lint后，再执行
        cleanTask.dependsOn project.tasks.getByName('lint')
    }
}