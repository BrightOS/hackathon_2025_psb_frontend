package ru.bashcony.psb.commersant

object MailClassMapper {

    private val classToRecipientMap = mapOf(
        "Уведомление или информирование" to Recipient(
            id = "1",
            name = "Отдел организации мероприятий",
            department = "Организационный отдел"
        ),
        "Запрос информации" to Recipient(
            id = "2",
            name = "Отдел по работе с клиентами",
            department = "Департамент обслуживания"
        ),
        "Жалоба или претензия" to Recipient(
            id = "3",
            name = "Служба контроля качества",
            department = "Департамент качества"
        ),
        "Юридический запрос" to Recipient(
            id = "4",
            name = "Юридический отдел",
            department = "Юридический департамент"
        ),
        "Технический вопрос" to Recipient(
            id = "5",
            name = "Техническая поддержка",
            department = "IT департамент"
        ),
        "Коммерческое предложение" to Recipient(
            id = "6",
            name = "Отдел развития бизнеса",
            department = "Коммерческий департамент"
        ),
        "Финансовый вопрос" to Recipient(
            id = "7",
            name = "Финансовый отдел",
            department = "Финансовый департамент"
        )
    )

    fun getRecipientsForMailClass(mailClass: String): List<Recipient> {
        classToRecipientMap[mailClass]?.let {
            return listOf(it)
        }

        val matchingRecipients = classToRecipientMap.entries
            .filter { mailClass.contains(it.key, ignoreCase = true) }
            .map { it.value }

        if (matchingRecipients.isNotEmpty()) {
            return matchingRecipients
        }

        return listOf(
            Recipient(
                id = "0",
                name = "Общий отдел",
                department = "Центральный офис"
            )
        )
    }

    fun getAllMailClasses(): List<String> {
        return classToRecipientMap.keys.toList()
    }
}
