// composeApp/src/webMain/kotlin/ru/bashcony/psb/commersant/EmailViewScreen.kt
package ru.bashcony.psb.commersant

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import assistantbutton.AssistantButton

@Composable
fun EmailViewScreen(
    emailData: EmailData = EmailData.sample()
) {
    val backgroundColor = Color(0xFFF5F5F7)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // Header
            EmailViewHeader()

            Spacer(modifier = Modifier.height(24.dp))

            // Email content
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 100.dp),
                shape = RoundedCornerShape(24.dp),
                color = Color.White,
                shadowElevation = 2.dp
            ) {
                Column(
                    modifier = Modifier.padding(40.dp)
                ) {
                    // Email header info
                    EmailHeaderSection(emailData)

                    Spacer(modifier = Modifier.height(32.dp))

                    Divider(color = Color(0xFFE0E0E0))

                    Spacer(modifier = Modifier.height(32.dp))

                    // Email body
                    EmailBodySection(emailData)
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }

        // AssistantButton в правом нижнем углу
        Box(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(32.dp)
        ) {
            AssistantButton(
                email = emailData.senderEmail,
                mail = emailData.body + emailData.signature,
                targetUrl = "http://localhost:8080" // или ваш URL
            )
        }
    }
}

@Composable
fun EmailViewHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Почтовый клиент ПСБ",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF5B5D7D)
        )

        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            TextButton(onClick = { /* Reply */ }) {
                Text("Ответить", color = Color(0xFF5B5D7D))
            }
            TextButton(onClick = { /* Forward */ }) {
                Text("Переслать", color = Color(0xFF5B5D7D))
            }
            TextButton(onClick = { /* Archive */ }) {
                Text("В архив", color = Color(0xFF5B5D7D))
            }
        }
    }
}

@Composable
fun EmailHeaderSection(emailData: EmailData) {
    Column {
        // Subject
        Text(
            text = emailData.subject,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1A1A1A)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // From
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "От:",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = emailData.senderName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF1A1A1A)
                )
                Text(
                    text = emailData.senderEmail,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            // Date and time
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = emailData.date,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
                Text(
                    text = emailData.time,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // To
        Row {
            Text(
                text = "Кому: ",
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = emailData.recipientEmail,
                fontSize = 14.sp,
                color = Color(0xFF1A1A1A)
            )
        }

        // CC if exists
        if (emailData.cc.isNotEmpty()) {
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Text(
                    text = "Копия: ",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = emailData.cc,
                    fontSize = 14.sp,
                    color = Color(0xFF1A1A1A)
                )
            }
        }
    }
}

@Composable
fun EmailBodySection(emailData: EmailData) {
    Column {
        Text(
            text = emailData.body,
            fontSize = 15.sp,
            lineHeight = 24.sp,
            color = Color(0xFF1A1A1A)
        )

        // Signature if exists
        if (emailData.signature.isNotEmpty()) {
            Spacer(modifier = Modifier.height(32.dp))

            Divider(
                color = Color(0xFFE0E0E0),
                modifier = Modifier.width(100.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = emailData.signature,
                fontSize = 14.sp,
                lineHeight = 20.sp,
                color = Color.Gray
            )
        }
    }
}

@Composable
fun EmailAttachmentsSection(attachments: List<Attachment>) {
    Column {
        Text(
            text = "Вложения (${attachments.size})",
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF1A1A1A)
        )
    }
}

// Data classes
data class EmailData(
    val subject: String,
    val senderName: String,
    val senderEmail: String,
    val recipientEmail: String,
    val cc: String = "",
    val date: String,
    val time: String,
    val body: String,
    val signature: String = "",
    val attachments: List<Attachment> = emptyList()
) {
    companion object {
        fun sample() = EmailData(
            subject = "Заявка на открытие расчетного счета",
            senderName = "Иванов Иван Иванович",
            senderEmail = "ivanov@company.ru",
            recipientEmail = "support@psbank.ru",
            cc = "manager@company.ru",
            date = "15 декабря 2024",
            time = "14:30",
            body = """
                Добрый день!
                
                Прошу рассмотреть возможность открытия расчетного счета для нашей компании ООО "Рога и Копыта".
                
                Основные данные компании:
                - ИНН: 7743013902
                - ОГРН: 1027700132195
                - Юридический адрес: г. Москва, ул. Ленина, д. 1
                
                Планируемый объем операций: до 5 млн рублей в месяц.
                Основной вид деятельности: оптовая торговля.
                
                Прошу выслать список необходимых документов и условия обслуживания.
                
                Готов предоставить дополнительную информацию по запросу.
            """.trimIndent(),
            signature = """
                С уважением,
                Иванов Иван Иванович
                Генеральный директор
                ООО "Рога и Копыта"
                Тел.: +7 (495) 123-45-67
                Email: ivanov@company.ru
            """.trimIndent(),
            attachments = listOf(
                Attachment("Устав компании.pdf", "pdf", "2.3 MB"),
                Attachment("Выписка ЕГРЮЛ.pdf", "pdf", "1.1 MB"),
                Attachment("Паспорт директора.pdf", "pdf", "856 KB")
            )
        )
    }
}

data class Attachment(
    val name: String,
    val extension: String,
    val size: String
)
