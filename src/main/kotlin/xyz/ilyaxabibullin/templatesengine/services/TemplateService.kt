package xyz.ilyaxabibullin.templatesengine.services

import org.springframework.stereotype.Service
import xyz.ilyaxabibullin.templatesengine.entitys.TextTemplate


@Service
class TemplateService {

    fun getFormedText(params: Map<String, Any>, template: TextTemplate): String {

        var resultString = template.template
        for (p in params) {
            println("$" + p.key)
            println(p.value)
            resultString = resultString!!.replace("$" + p.key, p.value as String)
            println(resultString)
        }

        return resultString!!

    }
}
