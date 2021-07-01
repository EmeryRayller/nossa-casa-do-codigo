package me.rayll.autor

import java.time.LocalDateTime
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

@Entity
class Autor(
    var nome: String,
    var email: String,
    var descricao: String,
    @field:Embedded var endereco: Endereco
){
    @Id @GeneratedValue
    var id: Long? = null
    val criadoEm = LocalDateTime.now()
}
