package xyz.ilyaxabibullin.templatesengine.entitys

class TemplateRequest {
    var textTemplate = ""
    var params = ArrayList<Parameter>()
    override fun toString(): String {
        return "TemplateRequest(textTemplate='$textTemplate', params=$params)"
    }

}