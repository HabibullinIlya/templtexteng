package xyz.ilyaxabibullin.templatesengine.controllers

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import xyz.ilyaxabibullin.templatesengine.entitys.Parameter
import xyz.ilyaxabibullin.templatesengine.entitys.TemplateRequest

import xyz.ilyaxabibullin.templatesengine.entitys.TextTemplate
import xyz.ilyaxabibullin.templatesengine.repository.TemplateRepository
import xyz.ilyaxabibullin.templatesengine.utill.Logger


@RestController
@RequestMapping("/template")
class TextEngineController {

    @Autowired
    private lateinit var repository: TemplateRepository
    val logger = Logger()



    @GetMapping("info/{id}")
    fun template(@PathVariable("id") id: Int): ResponseEntity<Map<String,Any>> {
        val template = repository.findById(id)
        logger.i(template.get().toString())
        if (template.isPresent) {
            return ResponseEntity(mapOf("error" to false, "template" to template.get()),HttpStatus.OK)
        }
        return ResponseEntity(mapOf("error" to false, "template" to template.get()),HttpStatus.OK)
        //return ResponseEntity(mapOf("error" to true),HttpStatus.NOT_FOUND)
    }

    @PostMapping("/")
    fun postTemplate(@RequestBody template: TemplateRequest):ResponseEntity<Map<String,Any>> {

        println(template.toString())

        var templ = TextTemplate(0, template.textTemplate)
        for(p in template.params){
            logger.i(p.description)
            (templ.params as HashSet<Parameter>).add(p)

        }
            val result = repository.save(templ)


        if(result.id!=0){
            return ResponseEntity(mapOf("error" to false, "template" to result),HttpStatus.OK)
        }

        return ResponseEntity(mapOf("error" to true,"message" to "i don't know why yet"),HttpStatus.FORBIDDEN)

    }

    @DeleteMapping("/{id}")
    fun deleteTemplate(@PathVariable("id") id: Int): ResponseEntity<Map<String, Any>> {
        var template = repository.findById(id)
        if(template.isPresent){
            repository.deleteById(template.get().id!!)
            return ResponseEntity(
                    mapOf("error" to false), HttpStatus.OK
            )
        }
        return ResponseEntity(
                mapOf("error" to true, "message" to "no such template"), HttpStatus.NOT_FOUND
        )

    }

    @GetMapping("/{id}")
    fun getTemplate(@PathVariable("id") id: Int,
                    @RequestParam input:  Map<String, Any> ): Map<String, String> {




        return mapOf("formed_text" to "poka tak")


    }

}