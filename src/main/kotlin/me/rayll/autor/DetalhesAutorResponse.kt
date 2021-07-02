package me.rayll.autor

import io.micronaut.core.annotation.Introspected
import javax.persistence.Embedded

class DetalhesAutorResponse(val nome: String, val email: String, val descricao: String){

}