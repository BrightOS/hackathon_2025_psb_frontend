package ru.bashcony.psb.commersant

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HelperResponse(
    @SerialName("mail_class")
    val mailClass: String,

    @SerialName("summary")
    val summary: String,

    @SerialName("contacts")
    val contacts: String,

    @SerialName("new_mail")
    val newMail: String,

    @SerialName("docs")
    val docs: List<DocumentPart>,

    @SerialName("count_mails")
    val countMails: Int
)

@Serializable
data class DocumentPart(
    @SerialName("document_type")
    val documentType: String,

    @SerialName("source")
    val source: String,

    @SerialName("content")
    val content: String
)

@Serializable
data class AnalysisResponse(
    @SerialName("summary")
    val summary: String,

    @SerialName("contacts")
    val contacts: String
)

@Serializable
data class ClassifierResponse(
    @SerialName("mail_class")
    val mailClass: String
)

@Serializable
data class GeneratingResponse(
    @SerialName("mail")
    val mail: String
)

@Serializable
data class DocumentsResponse(
    @SerialName("docs")
    val docs: List<DocumentPart>
)
