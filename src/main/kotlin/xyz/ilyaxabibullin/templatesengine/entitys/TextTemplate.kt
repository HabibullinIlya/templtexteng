package xyz.ilyaxabibullin.templatesengine.entitys

import javax.persistence.*

@Entity
@Table(name="texttemplate")
class TextTemplate(_id: Int, _template: String) {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name="id_template")
        var id:Int? = _id

        @Column(name="mtemplate")
        var template:String? = _template

        @ManyToMany(cascade = [CascadeType.ALL])
        @JoinTable(
                name = "template_params",
                joinColumns = [(JoinColumn(name = "id_template"))],
                inverseJoinColumns = [(JoinColumn(name = "id_param"))]
        )
        var params:Set<Parameter> = HashSet()
}