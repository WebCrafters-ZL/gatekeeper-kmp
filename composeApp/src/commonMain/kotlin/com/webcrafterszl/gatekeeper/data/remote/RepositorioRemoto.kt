package com.webcrafterszl.gatekeeper.data.remote

import com.webcrafterszl.gatekeeper.data.model.Convite
import com.webcrafterszl.gatekeeper.data.model.ConviteVisitante
import com.webcrafterszl.gatekeeper.data.model.CredencialRFID
import com.webcrafterszl.gatekeeper.data.model.Identificavel
import com.webcrafterszl.gatekeeper.data.model.Portador
import com.webcrafterszl.gatekeeper.data.model.Reserva
import com.webcrafterszl.gatekeeper.data.model.Visitante
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlin.random.Random

class RepositorioRemoto(
	private val baseUrl: String = "https://gatekeeper-kmp-default-rtdb.firebaseio.com",
	private val client: HttpClient = KtorClient.client,
	private val json: Json = Json {
		ignoreUnknownKeys = true
		encodeDefaults = true
		explicitNulls = false
	},
) {
	private fun collectionUrl(collection: String): String = "${baseUrl.trimEnd('/')}/$collection.json"

	private fun itemUrl(collection: String, id: Int): String = "${baseUrl.trimEnd('/')}/$collection/$id.json"

	private suspend inline fun <reified T> putBody(collection: String, id: Int, item: T) {
		try {
			client.put(itemUrl(collection, id)) {
				contentType(ContentType.Application.Json)
				setBody(item)
			}
		} catch (e: Exception) {
			throw Exception("Erro ao atualizar item $id em $collection: ${e.message}", e)
		}
	}

	private suspend inline fun <reified T : Identificavel> listBody(collection: String): List<T> {
		return try {
			val responseText = client.get(collectionUrl(collection)).bodyAsText()
			if (responseText.isBlank() || responseText == "null") return emptyList()

			val parsed = json.parseToJsonElement(responseText)
			val nodes: List<JsonElement> = when (parsed) {
				is JsonObject -> {
					parsed["error"]?.let {
						throw Exception("Firebase retornou erro para $collection: $it")
					}
					parsed.values.toList()
				}
				is JsonArray -> parsed.filterNot { it.toString() == "null" }
				else -> emptyList()
			}

			nodes
				.mapNotNull { node -> runCatching { json.decodeFromJsonElement<T>(node) }.getOrNull() }
				.sortedBy { it.id }
		} catch (e: Exception) {
			throw Exception("Erro ao listar $collection: ${e.message}", e)
		}
	}

	private suspend fun deleteItem(collection: String, id: Int) {
		try {
			client.delete(itemUrl(collection, id))
		} catch (e: Exception) {
			throw Exception("Erro ao excluir item $id em $collection: ${e.message}", e)
		}
	}

	private suspend inline fun <reified T : Identificavel> nextId(collection: String): Int {
		val atual = listBody<T>(collection)
		return (atual.maxOfOrNull { it.id } ?: 0) + 1
	}

	suspend fun listarPortadores(): List<Portador> = listBody("portadores")

	suspend fun salvarPortador(portador: Portador) {
		val item = if (portador.id > 0) portador else portador.copy(id = nextId<Portador>("portadores"))
		putBody("portadores", item.id, item)
	}

	suspend fun excluirPortador(id: Int) = deleteItem("portadores", id)

	suspend fun listarCredenciais(): List<CredencialRFID> = listBody("credenciais")

	suspend fun salvarCredencial(credencial: CredencialRFID) {
		val item = if (credencial.id > 0) credencial else credencial.copy(id = nextId<CredencialRFID>("credenciais"))
		putBody("credenciais", item.id, item)
	}

	suspend fun excluirCredencial(id: Int) = deleteItem("credenciais", id)

	suspend fun listarVisitantes(): List<Visitante> = listBody("visitantes")

	suspend fun salvarVisitante(visitante: Visitante) {
		val item = if (visitante.id > 0) visitante else visitante.copy(id = nextId<Visitante>("visitantes"))
		putBody("visitantes", item.id, item)
	}

	suspend fun excluirVisitante(id: Int) = deleteItem("visitantes", id)


	suspend fun listarReservas(): List<Reserva> = listBody("reservas")

	suspend fun salvarReserva(reserva: Reserva) {
		val item = if (reserva.id > 0) reserva else reserva.copy(id = nextId<Reserva>("reservas"))
		putBody("reservas", item.id, item)
	}

	suspend fun excluirReserva(id: Int) = deleteItem("reservas", id)

    // Funções para Convites com Link
    suspend fun criarConvite(userId: Int, dataVisita: String): String {
        val allowedChars = ('A'..'Z') + ('a'..'z') + ('0'..'9')
        val token = (1..10).map { allowedChars.random() }.joinToString("")
        val novoId = nextId<Convite>("convites")
        val convite = Convite(id = novoId, token = token, dataVisita = dataVisita, criadoPorId = userId)
        putBody("convites", novoId, convite)
        return "https://gatekeeper.com/invite/$token"
    }

    suspend fun listarConvites(userId: Int): List<Convite> {
        val todosConvites = listBody<Convite>("convites")
        return todosConvites.filter { it.criadoPorId == userId }
    }

    // Função para Convite de Visitante por E-mail
    suspend fun enviarConvitePorEmail(convite: ConviteVisitante) {
        val item = if (convite.id > 0) convite else convite.copy(id = nextId<ConviteVisitante>("convites_visitantes"))
        putBody("convites_visitantes", item.id, item)
        // Em um backend real, esta chamada ao putBody acionaria uma Cloud Function/Trigger
        // que se encarregaria de formatar e enviar o e-mail para o visitante.
        // Para o frontend, apenas salvar o registro é suficiente.
    }
}