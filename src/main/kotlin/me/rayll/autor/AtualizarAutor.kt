package me.rayll.autor

import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.validation.Validated
import javax.transaction.Transactional
import javax.validation.Valid

@Validated
@Controller("/autor/{id}")
class AtualizarAutor(val autorRepository: AutorRepository) {

    @Put @Transactional
    fun atualizar(@PathVariable id: Long, @Body @Valid autor: AutorRequest): HttpResponse<Any>{
        var autorPesquisa = autorRepository.findById(id).run {
            if(isPresent) get() else return HttpResponse.notFound()
        }

        autorPesquisa.run {
            this.nome = autor.nome
            this.email = autor.email
            this.descricao = autor.descricao
            return HttpResponse.ok(DetalhesAutorResponse(this))
        }
    }
}