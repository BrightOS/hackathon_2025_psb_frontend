package ru.bashcony.psb.commersant

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

class ApiClient(private val baseUrl: String = "http://158.160.201.2:8000") {

    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }

    /**
     * Получить всю информацию о письме через helper
     */
    suspend fun getHelper(sender: String, mail: String): Result<HelperResponse> {
        return try {
            val response = client.get("$baseUrl/helper") {
                parameter("sendler", sender)
                parameter("mail", mail)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            println("Error in getHelper: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Отправить лайк
     */
    suspend fun sendLike(): Result<Unit> {
        return try {
            client.get("$baseUrl/like")
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error in sendLike: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Отправить дизлайк
     */
    suspend fun sendDislike(): Result<Unit> {
        return try {
            client.get("$baseUrl/dislike")
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error in sendDislike: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Получить анализ письма
     */
    suspend fun getAnalysis(sender: String, mail: String): Result<AnalysisResponse> {
        return try {
            val response = client.get("$baseUrl/analysis") {
                parameter("sendler", sender)
                parameter("mail", mail)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            println("Error in getAnalysis: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Получить классификацию письма
     */
    suspend fun getClassifier(sender: String, mail: String): Result<ClassifierResponse> {
        return try {
            val response = client.get("$baseUrl/classifier") {
                parameter("sendler", sender)
                parameter("mail", mail)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            println("Error in getClassifier: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Получить документы
     */
    suspend fun getDocuments(sender: String, mail: String): Result<DocumentsResponse> {
        return try {
            val response = client.get("$baseUrl/documents") {
                parameter("sendler", sender)
                parameter("mail", mail)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            println("Error in getDocuments: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Сгенерировать ответ
     */
    suspend fun generateResponse(
        sender: String,
        mail: String,
        mailClass: String
    ): Result<GeneratingResponse> {
        return try {
            val response = client.get("$baseUrl/generate") {
                parameter("sendler", sender)
                parameter("mail", mail)
                parameter("mail_class", mailClass)
            }
            Result.success(response.body())
        } catch (e: Exception) {
            println("Error in generateResponse: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Отправить письмо
     */
    suspend fun sendMail(mail: String, id: String): Result<Unit> {
        return try {
            client.get("$baseUrl/send") {
                parameter("mail", mail)
                parameter("id", id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            println("Error in sendMail: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Проверить доступность API
     */
    suspend fun ping(): Result<Boolean> {
        return try {
            client.get("$baseUrl/ping")
            Result.success(true)
        } catch (e: Exception) {
            println("Error in ping: ${e.message}")
            Result.failure(e)
        }
    }

    fun close() {
        client.close()
    }
}
