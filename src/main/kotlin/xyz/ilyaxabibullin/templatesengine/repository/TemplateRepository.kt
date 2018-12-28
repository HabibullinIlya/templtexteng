package xyz.ilyaxabibullin.templatesengine.repository

import org.springframework.data.repository.CrudRepository
import xyz.ilyaxabibullin.templatesengine.entitys.TextTemplate

interface TemplateRepository:CrudRepository<TextTemplate, Int>