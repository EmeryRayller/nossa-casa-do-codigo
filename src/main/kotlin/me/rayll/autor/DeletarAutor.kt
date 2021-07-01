package me.rayll.autor

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.PathVariable

@Controller("/autor/{id}")
class DeletarAutor(val autorRepository: AutorRepository) {

    fun deletarAutor(@PathVariable id: Long): HttpResponse<Any> {

        autorRepository.findById(id).run {
            if (isPresent)
                autorRepository.delete(get())
            else
                return HttpResponse.notFound()
        }

        return HttpResponse.noContent()
    }
}