package xyz.ilyaxabibullin.templatesengine

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*
import xyz.ilyaxabibullin.templatesengine.entitys.TextTemplate
import xyz.ilyaxabibullin.templatesengine.repository.TemplateRepository



@RestController
@RequestMapping("/template")
class TextEngineController{

    @Autowired
    private lateinit var repository: TemplateRepository


    @GetMapping("/{id}")
    fun template (@PathVariable("id") id: Int): TextTemplate {
        return repository.findById(id).get()
    }

}