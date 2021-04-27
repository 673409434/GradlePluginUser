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
 * Description:
 */
public class PluginExtension {
    String lintXmlPath
    String outputPath

    public PluginExtension(Project project) {
        // 默认路径
        lintXmlPath = "$project.buildDir/reports/lint-results.xml"
        outputPath = "$project.buildDir/reports/lintCleanerLog.txt"
    }

    @Override
    String toString() {
        return "配置项:\n\tlintXmlPath:" + lintXmlPath + "\n" +
                "outputPath:" + outputPath + "\n"
    }
}