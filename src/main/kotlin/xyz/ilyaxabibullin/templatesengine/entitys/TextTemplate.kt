package xyz.ilyaxabibullin.templatesengine.entitys

import javax.persistence.*

@Entity
@Table(name="texttemplate")
data class TextTemplate(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name="id_template")
        var id:Int,

        @Column(name="mtemplate")
        var content:String,

        @Column(name="params")
        var params:String
)