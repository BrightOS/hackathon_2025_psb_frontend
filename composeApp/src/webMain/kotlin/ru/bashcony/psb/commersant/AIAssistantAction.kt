package ru.bashcony.psb.commersant

sealed class AIAssistantAction {
    data class UpdateSenderEmail(val email: String) : AIAssistantAction()
    data class UpdateGeneratedResponse(val response: String) : AIAssistantAction()

    data class UpdateOriginalEmailSubject(val subject: String) : AIAssistantAction()
    data class UpdateOriginalEmailBody(val body: String) : AIAssistantAction()

    object CopyGeneratedResponse : AIAssistantAction()
    data class RateResponse(val feedback: ResponseFeedback) : AIAssistantAction()

    data class SelectRecipient(val recipientId: String) : AIAssistantAction()
    data class OpenRecipientTicket(val recipientId: String) : AIAssistantAction()

    data class CopyTemplate(val templateId: String) : AIAssistantAction()
    data class EditTemplate(val templateId: String, val newBody: String) : AIAssistantAction()

    object RegenerateResponse : AIAssistantAction()
    object ClearError : AIAssistantAction()
    object RefreshData : AIAssistantAction()

    data class LoadEmailData(val sender: String, val mail: String) : AIAssistantAction()
    object SendFeedbackLike : AIAssistantAction()
    object SendFeedbackDislike : AIAssistantAction()
    data class SendEmail(val mail: String, val id: String) : AIAssistantAction()
}
