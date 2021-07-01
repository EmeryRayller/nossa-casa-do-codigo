package me.rayll.autor

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.QueryValue

@Controller("/autor")
class BuscarAutoresController(val autorRepository: AutorRepository) {

    @Get
    fun listarAutores(@QueryValue(defaultValue = "") email: String): HttpResponse<Any>{

        if (email.isEmpty()) {

            val autores = autorRepository.findAll()
            val response = autores.map { autor -> DetalhesAutorResponse(autor) }
            return HttpResponse.ok(response)
        }

        autorRepository.findByEmail(email).run {
            return if (isPresent) HttpResponse.ok(DetalhesAutorResponse(get())) else HttpResponse.notFound()
        }
    }
}