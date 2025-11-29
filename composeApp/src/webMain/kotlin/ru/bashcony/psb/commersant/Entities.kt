package ru.bashcony.psb.commersant

import kotlin.random.Random

data class EmailInfo(
    val senderEmail: String = "",
    val emailNumber: String = "",
    val dateRange: String = "",
    val generatedResponse: String = "",
    val approvalRate: Int = Random.nextInt(70, 81)
)

data class OriginalEmail(
    val body: String = ""
)

data class AssistantOutput(
    val emailClass: String = "",
    val responseRequired: Boolean = true,
    val responseNotRequiredReason: String = "",
    val attachedContacts: String = "",
    val generatedResponse: String = "",
    val approvalRate: Int = Random.nextInt(70, 81),
    val documents: List<DocumentInfo> = emptyList(),
    val summary: String = ""
)

data class DocumentInfo(
    val type: String,
    val source: String,
    val content: String
)

data class Recipient(
    val id: String,
    val name: String,
    val department: String
)

data class Template(
    val id: String,
    val title: String,
    val body: String
)

data class AIAssistantState(
    val emailInfo: EmailInfo = EmailInfo(),
    val originalEmail: OriginalEmail = OriginalEmail(),
    val assistantOutput: AssistantOutput = AssistantOutput(),
    val recipients: List<Recipient> = emptyList(),
    val templates: List<Template> = listOf(
        Template(
            "1",
            "Уважаемый клиент,",
            """
            Благодарим Вас за обращение в наш банк.
            Мы внимательно изучим указанную ситуацию и примем все необходимые меры для её разрешения.
            Наши специалисты свяжутся с Вами в течение 2 рабочих дней.
            
            С уважением,
            Служба поддержки клиентов
            """.trimIndent()
        ),
        Template(
            "2",
            "Уважаемые коллеги,",
            """
            Благодарим за проявленный интерес к сотрудничеству и Ваше предложение.
            Наши специалисты рассмотрят его в ближайшее время и свяжутся с Вами для обсуждения деталей.
            
            С уважением,
            Отдел развития бизнеса
            """.trimIndent()
        ),
        Template(
            "3",
            "Уважаемый клиент,",
            """
            Информируем Вас о следующем:
            [ИЗМЕНИТЬ ТЕКСТ]
            По постановлению,
            
            Благодарим за внимание.
            
            С уважением,
            Администрация банка
            """.trimIndent()
        )
    ),
    val userResponseFeedback: ResponseFeedback? = null,
    val copiedText: String? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val hasLoadedData: Boolean = false,
)

enum class ResponseFeedback {
    LIKED,
    DISLIKED
}
