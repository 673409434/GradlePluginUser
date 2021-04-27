package com.dev.hh.gradle_plugin

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * Package: com.dev.hh.gradle_plugin
 * User: hehao3
 * Email: hehao3@jd.com
 * Date: 2021/4/26
 * Time: 下午6:51
 * Description: 必须要继承DefautTask,并使用@TaskAction来定义Task的入口函数
 */
public class CleanTask extends DefaultTask {
    static final String TASK_GROUP_NAME = 'LintCleaner'
    // 任务名
    static final String TASK_NAME = "cleanUnusedRes"


    final String UNUSED_RESOURCES_ID = "UnusedResources"
    final String ISSUE_XML_TAG = "issue"
    HashSet<String> mFilePaths = new HashSet<>()
    StringBuilder mDelLogSb = new StringBuilder()
    StringBuilder mKeepLogSb = new StringBuilder()

    public CleanTask() {
        group = TASK_GROUP_NAME
        description = "Removes unused resources reported by Android lint task"
    }

    @TaskAction
    def start() {
        println 'CleanTask start! '

        def ext = project.extensions.findByName(MyPlugin.EXTENSION_NAME) as PluginExtension
        println ext.toString()

        def file = new File(ext.lintXmlPath)
        if (!file.exists()) {
            println '找不到lint的xml文件，请检查路径是否正确! '
            return
        }

        // 解析xml，添加无用文件的路径到容器中
        new XmlSlurper().parse(file).'**'.findAll { node ->
            if (node.name() == ISSUE_XML_TAG && node.@id == UNUSED_RESOURCES_ID) {
                mFilePaths.add(node.location.@file)
            }
        }

        def num = mFilePaths.size()
        if (num > 0) {
            mDelLogSb.append("num:${num}\n")
            mDelLogSb.append("\n=====删除的文件=====\n")
            mKeepLogSb.append("\n=====保留的文件=====\n")
            for (String path : mFilePaths) {
                println path
                deleteFileByPath(path)
            }
            writeToOutput(ext.outputPath)
        } else {
            println '不存在无用资源！'
        }
    }

    def deleteFileByPath(String path) {
        if (isDelFile(path)) {
            if (new File(path).delete()) {
                mDelLogSb.append('\n\t' + path)

            } else {
                mKeepLogSb.append('\n\t删除失败：' + path)

            }
        } else {
            mKeepLogSb.append('\n\t' + path)

        }
    }

/**
 * 只选定drawable,mipmap,menu下的文件,(无用引用暂不处理)
 * @param path
 */
    def isDelFile(String path) {
        String dir = path
        (dir.contains('layout') || dir.contains('drawable') || dir.contains('mipmap') || dir.contains('menu')) && (dir.endsWith('.png') || dir.endsWith('.jpg') || dir.endsWith('.jpeg'))
    }

    def writeToOutput(def path) {
        def f = new File(path)
        if (f.exists()) {
            f.delete()
        }
        new File(path).withPrintWriter { pw ->
            pw.write(mDelLogSb.toString())
            pw.write(mKeepLogSb.toString())
        }
    }
}