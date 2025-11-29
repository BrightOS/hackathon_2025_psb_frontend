package ru.bashcony.psb.commersant

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

class AIAssistantViewModel(
    initialEmail: String = "",
    initialMail: String = ""
) {
    private val _state = MutableStateFlow(
        AIAssistantState(
            emailInfo = EmailInfo(senderEmail = initialEmail),
            originalEmail = OriginalEmail(body = initialMail),
        ),
    )
    val state: StateFlow<AIAssistantState> = _state.asStateFlow()

    private val apiClient = ApiClient()
    private val scope = CoroutineScope(Dispatchers.Default)

    fun handleAction(action: AIAssistantAction) {
        when (action) {
            is AIAssistantAction.UpdateSenderEmail -> {
                updateSenderEmail(action.email)
            }

            is AIAssistantAction.UpdateGeneratedResponse -> {
                updateGeneratedResponse(action.response)
            }

            is AIAssistantAction.UpdateOriginalEmailSubject -> {
                updateOriginalEmailSubject(action.subject)
            }

            is AIAssistantAction.UpdateOriginalEmailBody -> {
                updateOriginalEmailBody(action.body)
            }

            is AIAssistantAction.CopyGeneratedResponse -> {
                copyGeneratedResponse()
            }

            is AIAssistantAction.RateResponse -> {
                rateResponse(action.feedback)
            }

            is AIAssistantAction.SelectRecipient -> {
                selectRecipient(action.recipientId)
            }

            is AIAssistantAction.OpenRecipientTicket -> {
                openRecipientTicket(action.recipientId)
            }

            is AIAssistantAction.CopyTemplate -> {
                copyTemplate(action.templateId)
            }

            is AIAssistantAction.EditTemplate -> {
                editTemplate(action.templateId, action.newBody)
            }

            is AIAssistantAction.RegenerateResponse -> {
                regenerateResponse()
            }

            is AIAssistantAction.ClearError -> {
                clearError()
            }

            is AIAssistantAction.RefreshData -> {
                refreshData()
            }

            is AIAssistantAction.LoadEmailData -> {
                loadEmailData(action.sender, action.mail)
            }

            is AIAssistantAction.SendFeedbackLike -> {
                sendFeedbackLike()
            }

            is AIAssistantAction.SendFeedbackDislike -> {
                sendFeedbackDislike()
            }

            is AIAssistantAction.SendEmail -> {
                sendEmail(action.mail, action.id)
            }
        }
    }

    private fun updateSenderEmail(email: String) {
        _state.value = _state.value.copy(
            emailInfo = _state.value.emailInfo.copy(senderEmail = email)
        )
    }

    private fun updateGeneratedResponse(response: String) {
        _state.value = _state.value.copy(
            emailInfo = _state.value.emailInfo.copy(generatedResponse = response)
        )
    }

    private fun updateOriginalEmailSubject(subject: String) {
        _state.value = _state.value.copy(
            emailInfo = _state.value.emailInfo.copy(senderEmail = subject)
        )
    }

    private fun updateOriginalEmailBody(body: String) {
        _state.value = _state.value.copy(
            originalEmail = _state.value.originalEmail.copy(body = body)
        )
    }

    private fun copyGeneratedResponse() {
        val response = _state.value.emailInfo.generatedResponse
        _state.value = _state.value.copy(copiedText = response)

        println("Copied to clipboard: $response")
        copyToClipboard(response)
    }

    private fun rateResponse(feedback: ResponseFeedback) {
        _state.value = _state.value.copy(userResponseFeedback = feedback)

        scope.launch {
            when (feedback) {
                ResponseFeedback.LIKED -> sendFeedbackLike()
                ResponseFeedback.DISLIKED -> sendFeedbackDislike()
            }
        }
    }

    private fun selectRecipient(recipientId: String) {
        val recipient = _state.value.recipients.find { it.id == recipientId }
        println("Selected recipient: ${recipient?.name}")
    }

    private fun openRecipientTicket(recipientId: String) {
        val recipient = _state.value.recipients.find { it.id == recipientId }
        println("Opening ticket for: ${recipient?.name}")
    }

    private fun copyTemplate(templateId: String) {
        val template = _state.value.templates.find { it.id == templateId }
        template?.let {
            _state.value = _state.value.copy(copiedText = it.body)
            copyToClipboard(it.body)
            println("Copied template: ${it.title}")
        }
    }

    private fun editTemplate(templateId: String, newBody: String) {
        val updatedTemplates = _state.value.templates.map { template ->
            if (template.id == templateId) {
                template.copy(body = newBody)
            } else {
                template
            }
        }

        _state.value = _state.value.copy(templates = updatedTemplates)
    }

    private fun regenerateResponse() {
        val sender = _state.value.emailInfo.senderEmail
        val mail = _state.value.originalEmail.body
        val mailClass = _state.value.assistantOutput.emailClass

        _state.value = _state.value.copy(isLoading = true)

        scope.launch {
            apiClient.generateResponse(sender, mail, mailClass)
                .onSuccess { response ->
                    _state.value = _state.value.copy(
                        emailInfo = _state.value.emailInfo.copy(
                            generatedResponse = response.mail,
                            approvalRate = Random.nextInt(70, 81)
                        ),
                        isLoading = false
                    )
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка генерации ответа: ${error.message}"
                    )
                }
        }
    }

    private fun clearError() {
        _state.value = _state.value.copy(error = null)
    }

    private fun refreshData() {
        val sender = _state.value.emailInfo.senderEmail
        val mail = _state.value.originalEmail.body

        if (sender.isNotEmpty() && mail.isNotEmpty()) {
            loadEmailData(sender, mail)
        }
    }

    private fun loadEmailData(sender: String, mail: String) {
        _state.value = _state.value.copy(isLoading = true, error = null)

        scope.launch {
            apiClient.getHelper(sender, mail)
                .onSuccess { response ->
                    val recipients = MailClassMapper.getRecipientsForMailClass(response.mailClass)
                    val currentDate = "29.11 - 29.11"

                    _state.value = _state.value.copy(
                        emailInfo = _state.value.emailInfo.copy(
                            senderEmail = sender,
                            generatedResponse = response.newMail,
                            emailNumber = "${response.countMails + 1}-е письмо",
                            dateRange = currentDate,
                            approvalRate = Random.nextInt(70, 81)
                        ),
                        originalEmail = _state.value.originalEmail.copy(
                            body = mail
                        ),
                        assistantOutput = AssistantOutput(
                            emailClass = response.mailClass,
                            attachedContacts = response.contacts,
                            responseRequired = response.newMail.isNotEmpty(),
                            responseNotRequiredReason = if (response.newMail.isEmpty()) "Ответ не требуется" else "",
                            documents = response.docs.map {
                                DocumentInfo(it.documentType, it.source, it.content.replace("\n\n", "\n"))
                            },
                            summary = response.summary
                        ),
                        recipients = recipients,
                        isLoading = false,
                        hasLoadedData = true
                    )

                    println("Email data loaded successfully")
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка загрузки данных: ${error.message}"
                    )
                }
        }
    }

    private fun sendFeedbackLike() {
        scope.launch {
            apiClient.sendLike()
                .onSuccess {
                    println("Like sent successfully")
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        userResponseFeedback = null,
                        error = "Ошибка отправки лайка: ${error.message}"
                    )
                }
        }
    }

    private fun sendFeedbackDislike() {
        scope.launch {
            apiClient.sendDislike()
                .onSuccess {
                    println("Dislike sent successfully")
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        userResponseFeedback = null,
                        error = "Ошибка отправки дизлайка: ${error.message}"
                    )
                }
        }
    }

    private fun sendEmail(mail: String, id: String) {
        _state.value = _state.value.copy(isLoading = true)

        scope.launch {
            apiClient.sendMail(mail, id)
                .onSuccess {
                    _state.value = _state.value.copy(
                        isLoading = false
                    )
                    println("Email sent successfully")
                }
                .onFailure { error ->
                    _state.value = _state.value.copy(
                        isLoading = false,
                        error = "Ошибка отправки письма: ${error.message}"
                    )
                }
        }
    }

    fun onCleared() {
        apiClient.close()
    }
}
