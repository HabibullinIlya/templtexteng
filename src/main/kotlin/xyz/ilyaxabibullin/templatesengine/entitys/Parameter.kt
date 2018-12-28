package xyz.ilyaxabibullin.templatesengine.entitys

import javax.persistence.*

@Entity
@Table(name = "parameter")
class Parameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_param")
    var id = 0

    @Column(name = "key")
    var key = ""

    @Column(name = "description")
    var description = ""

    /*@ManyToMany(mappedBy = "params")
    var templates:Set<TextTemplate> = HashSet()*/
}