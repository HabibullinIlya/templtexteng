package xyz.ilyaxabibullin.templatesengine

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TextTemplateEngineApplication

fun main(args: Array<String>) {
    runApplication<TextTemplateEngineApplication>(*args)
}
