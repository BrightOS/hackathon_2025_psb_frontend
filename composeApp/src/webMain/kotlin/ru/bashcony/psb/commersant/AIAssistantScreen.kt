package ru.bashcony.psb.commersant

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Article
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.outlined.Article
import androidx.compose.material.icons.outlined.ThumbDown
import androidx.compose.material.icons.outlined.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import assistantbutton.style.Typography
import org.jetbrains.compose.resources.vectorResource
import psbcommersant.composeapp.generated.resources.Res
import psbcommersant.composeapp.generated.resources.compose_multiplatform
import psbcommersant.composeapp.generated.resources.ic_logo

@Composable
fun AIAssistantScreen(
    initialEmail: String = "",
    initialMail: String = ""
) {
    val viewModel = remember {
        AIAssistantViewModel(
            initialEmail = initialEmail,
            initialMail = initialMail
        )
    }
    val state by viewModel.state.collectAsState()

    LaunchedEffect(initialEmail, initialMail) {
        if (initialEmail.isNotEmpty() && initialMail.isNotEmpty()) {
            console.log("Auto-loading email data for: $initialEmail")
            viewModel.handleAction(
                AIAssistantAction.LoadEmailData(initialEmail, initialMail)
            )
        } else {
            console.log("No initial data provided, waiting for user input")
        }
    }

    AIAssistantScreenContent(
        state = state,
        onAction = { action -> viewModel.handleAction(action) }
    )
}

@Composable
fun AIAssistantScreenContent(
    state: AIAssistantState,
    onAction: (AIAssistantAction) -> Unit
) {
    val backgroundColor = Brush.verticalGradient(listOf(Color(0xFF49487E), Color(0xFF7676A9)))
    val cardColor = Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
    ) {
        Column {
            Header()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "ИИ-ассистент для работы с коммерцией",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(40.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 100.dp),
                    shape = RoundedCornerShape(44.dp),
                    color = cardColor,
                ) {
                    Column(
                        modifier = Modifier.padding(30.dp)
                    ) {
                        EmailAndResponseSection(
                            emailInfo = state.emailInfo,
                            originalEmail = state.originalEmail,
                            userFeedback = state.userResponseFeedback,
                            onEmailChange = { email ->
                                onAction(AIAssistantAction.UpdateSenderEmail(email))
                            },
                            onSubjectChange = { subject ->
                                onAction(AIAssistantAction.UpdateOriginalEmailSubject(subject))
                            },
                            onBodyChange = { body ->
                                onAction(AIAssistantAction.UpdateOriginalEmailBody(body))
                            },
                            onCopyResponse = {
                                onAction(AIAssistantAction.CopyGeneratedResponse)
                            },
                            onRateResponse = { feedback ->
                                onAction(AIAssistantAction.RateResponse(feedback))
                            },
                            isLoading = state.isLoading,
                            onResponseChange = { response ->
                                onAction(AIAssistantAction.UpdateGeneratedResponse(response))
                            },
                            onAnalyze = {
                                onAction(
                                    AIAssistantAction.LoadEmailData(
                                        sender = state.emailInfo.senderEmail,
                                        mail = state.originalEmail.body
                                    )
                                )
                            },
                        )

                        if (state.hasLoadedData) {

                            Spacer(modifier = Modifier.height(32.dp))

                            AssistantOutputSection(
                                assistantOutput = state.assistantOutput,
                            )

                            Spacer(modifier = Modifier.height(32.dp))

                            RecipientsSection(
                                recipients = state.recipients,
                                onOpenTicket = { recipientId ->
                                    onAction(AIAssistantAction.OpenRecipientTicket(recipientId))
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                ReadyTemplatesCard(
                    templates = state.templates,
                    onCopyTemplate = { templateId ->
                        onAction(AIAssistantAction.CopyTemplate(templateId))
                    }
                )

                Spacer(modifier = Modifier.height(40.dp))
            }
        }

        if (state.isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator(color = Color.White)
            }
        }

        state.error?.let { error ->
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                action = {
                    TextButton(onClick = { onAction(AIAssistantAction.ClearError) }) {
                        Text("Закрыть", color = Color.White)
                    }
                }
            ) {
                Text(error)
            }
        }
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 24.dp, horizontal = 130.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            vectorResource(Res.drawable.ic_logo),
            contentDescription = "logo",
            modifier = Modifier.height(24.dp)
        )
    }
}

@Composable
fun EmailAndResponseSection(
    emailInfo: EmailInfo,
    originalEmail: OriginalEmail,
    isLoading: Boolean,
    onAnalyze: () -> Unit,
    onResponseChange: (String) -> Unit,
    userFeedback: ResponseFeedback?,
    onEmailChange: (String) -> Unit,
    onSubjectChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    onCopyResponse: () -> Unit,
    onRateResponse: (ResponseFeedback) -> Unit,
) {
    val textFontSize = 14.sp

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(modifier = Modifier.weight(1f).fillMaxSize().heightIn(max = 400.dp)) {
                Text(
                    text = "E-mail отправителя письма",
                    fontSize = 18.sp,
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                OutlinedTextField(
                    value = emailInfo.senderEmail,
                    onValueChange = onEmailChange,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 8.dp),
                    placeholder = {
                        Text(
                            "Введите email отправителя",
                            fontSize = textFontSize,
                            style = Typography.bodyMedium.copy(fontSize = textFontSize),
                            color = Color(0x885C5C73)
                        )
                    },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF8F8FC),
                        focusedContainerColor = Color(0xFFF8F8FC),
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedIndicatorColor = Color(0xFF5B5D7D),
                        focusedTextColor = Color(0xFF5C5C73),
                        unfocusedTextColor = Color(0x885C5C73),
                    ),
                    shape = RoundedCornerShape(24.dp),
                    textStyle = Typography.bodyMedium.copy(fontSize = textFontSize),
                )

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (emailInfo.emailNumber.isNotEmpty()) {
                        Chip(text = emailInfo.emailNumber, color = Color(0xFF2E9327))
                    }
                    if (emailInfo.dateRange.isNotEmpty()) {
                        Chip(text = emailInfo.dateRange, color = Color(0xFF274F93))
                    }
                    if (emailInfo.approvalRate > 0 && emailInfo.emailNumber.isNotEmpty()) {
                        Chip(
                            text = "Одобрение: ${emailInfo.approvalRate}%",
                            color = Color(0xFFDFA017)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                OriginalEmailSection(
                    originalEmail = originalEmail,
                    onSubjectChange = onSubjectChange,
                    onBodyChange = onBodyChange,
                    textFontSize = textFontSize,
                    isLoading = isLoading,
                    emailInfo = emailInfo,
                    onAnalyze = onAnalyze,
                )
            }

            Column(modifier = Modifier.weight(1f).fillMaxSize().heightIn(max = 400.dp)) {
                Text(
                    text = "Текст сгенерированного ответа",
                    fontSize = 18.sp,
                    style = Typography.titleLarge,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Column(
                    modifier = Modifier
                        .background(Color(0xFFF8F8FC), RoundedCornerShape(24.dp))
                        .padding(20.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = emailInfo.generatedResponse.ifEmpty { "Нажмите 'Анализировать письмо' для генерации ответа" },
                            fontSize = textFontSize,
                            color = if (emailInfo.generatedResponse.isEmpty()) Color(0x885C5C73) else Color(
                                0xFF5C5C73
                            )
                        )
                    }

//                    if (emailInfo.generatedResponse.isNotEmpty())
                    Row(
                        verticalAlignment = Alignment.Bottom,
                    ) {
                        Button(
                            onClick = onCopyResponse,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF5B5D7D)
                            ),
                            shape = CircleShape,
                            enabled = emailInfo.generatedResponse.isNotEmpty()
                        ) {
                            Text(
                                "Скопировать ответ",
                                color = Color.White,
                                style = Typography.bodyMedium.copy(fontSize = 14.sp),
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Icon(
                                imageVector = Icons.Default.ContentCopy,
                                modifier = Modifier.height(20.dp),
                                tint = Color.White,
                                contentDescription = "copy",
                            )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        Surface(
                            shape = CircleShape,
                            tonalElevation = 0.dp,
                            shadowElevation = 0.dp,
                            color = Color.White,
                        ) {
                            Row(
                                modifier = Modifier.padding(
                                    horizontal = 20.dp,
                                    vertical = 10.dp
                                ),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {

                                Text(
                                    text = "Как вам ответ?",
                                    fontSize = 14.sp,
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Icon(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .selectable(
                                            selected = userFeedback == ResponseFeedback.LIKED,
                                            interactionSource = MutableInteractionSource(),
                                            enabled = emailInfo.generatedResponse.isNotEmpty(),
                                            onClick = { onRateResponse(ResponseFeedback.LIKED) },
                                        )
                                        .alpha(if (emailInfo.generatedResponse.isEmpty()) 0.5f else 1f),
                                    imageVector = Icons.Outlined.ThumbUp,
                                    tint = if (userFeedback == ResponseFeedback.LIKED)
                                        Color(0xFF7676A9)
                                    else
                                        Color.Black,
                                    contentDescription = "thumb up",
                                )

                                Spacer(modifier = Modifier.width(10.dp))

                                Icon(
                                    modifier = Modifier
                                        .height(20.dp)
                                        .selectable(
                                            selected = userFeedback == ResponseFeedback.DISLIKED,
                                            interactionSource = MutableInteractionSource(),
                                            enabled = emailInfo.generatedResponse.isNotEmpty(),
                                            onClick = { onRateResponse(ResponseFeedback.DISLIKED) },
                                        )
                                        .alpha(if (emailInfo.generatedResponse.isEmpty()) 0.5f else 1f),
                                    tint = if (userFeedback == ResponseFeedback.DISLIKED)
                                        Color(0xFF7676A9)
                                    else
                                        Color.Black,
                                    imageVector = Icons.Outlined.ThumbDown,
                                    contentDescription = "thumb down",
                                )
                            }
                        }
                    }
                }
            }
        }

    }
}

@Composable
fun OriginalEmailSection(
    originalEmail: OriginalEmail,
    isLoading: Boolean,
    emailInfo: EmailInfo,
    onAnalyze: () -> Unit,
    onSubjectChange: (String) -> Unit,
    onBodyChange: (String) -> Unit,
    textFontSize: TextUnit = 14.sp,
) {
    Column {
        Text(
            text = "Текст исходного письма",
            fontSize = 18.sp,
            style = Typography.titleLarge,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = originalEmail.body,
            onValueChange = onBodyChange,
            modifier = Modifier.fillMaxWidth().weight(1f),
            placeholder = {
                Text(
                    text = "Введите текст письма для анализа...",
                    color = Color(0x885C5C73),
                    style = Typography.bodyMedium.copy(fontSize = textFontSize),
                )
            },
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color(0xFFF8F8FC),
                focusedContainerColor = Color(0xFFF8F8FC),
                unfocusedIndicatorColor = Color.Transparent,
                focusedIndicatorColor = Color(0xFF5B5D7D),
                focusedTextColor = Color(0xFF5C5C73),
                unfocusedTextColor = Color(0x885C5C73),
            ),
            shape = RoundedCornerShape(24.dp),
            maxLines = Int.MAX_VALUE,
            textStyle = Typography.bodyMedium.copy(fontSize = textFontSize)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Button(
            onClick = onAnalyze,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF5B5D7D)
            ),
            shape = CircleShape,
            enabled = !isLoading &&
                    emailInfo.senderEmail.isNotEmpty() &&
                    originalEmail.body.isNotEmpty()
        ) {
            Text(
                text = if (isLoading) "Обработка..." else "Анализировать письмо",
                color = Color.White,
                style = Typography.bodyMedium.copy(fontSize = 14.sp),
            )
        }
    }
}

@Composable
fun AssistantOutputSection(
    assistantOutput: AssistantOutput,
) {

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Выжимка от ассистента",
                style = Typography.titleLarge.copy(fontSize = 22.sp),
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        val hasClassInfo = assistantOutput.emailClass.isNotEmpty()
        val hasContactsInfo = assistantOutput.attachedContacts.isNotEmpty()

        if (hasClassInfo || hasContactsInfo) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                if (assistantOutput.summary.isNotEmpty()) {
                    item {
                        InfoCard(
                            title = "Краткое содержание",
                            content = assistantOutput.summary,
                        )
                    }
                }

                if (hasClassInfo) {
                    item {
                        InfoCard(
                            title = "Класс письма",
                            content = assistantOutput.emailClass,
                        )
                    }
                }

                if (hasContactsInfo) {
                    item {
                        InfoCard(
                            title = "Прикрепленные контакты",
                            content = assistantOutput.attachedContacts,
                        )
                    }
                }
            }
        }

        if (assistantOutput.documents.isNotEmpty()) {
            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Найденные документы",
                style = Typography.titleLarge.copy(fontSize = 22.sp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            DocumentsGrid(documents = assistantOutput.documents)
        }
    }
}

@Composable
fun DocumentsGrid(documents: List<DocumentInfo>) {
    var selectedDocument by remember { mutableStateOf<DocumentInfo?>(null) }

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        documents.chunked(3).forEach { rowDocs ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                rowDocs.forEach { doc ->
                    DocumentCompactCard(
                        document = doc,
                        onClick = { selectedDocument = doc },
                        modifier = Modifier.weight(1f)
                    )
                }
                repeat(3 - rowDocs.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    selectedDocument?.let { doc ->
        DocumentDialog(
            document = doc,
            onDismiss = { selectedDocument = null }
        )
    }
}

@Composable
fun DocumentCompactCard(
    document: DocumentInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxHeight()
            .clickable(
                onClick = onClick,
                indication = LocalIndication.current,
                interactionSource = remember { MutableInteractionSource() }
            ),
        color = Color(0xFFF8F8FC),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                tint = Color(0xFF5B5D7D),
                imageVector = Icons.AutoMirrored.Outlined.Article,
                contentDescription = "document",
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = document.content,
                color = Color(0xFF5B5D7D),
                style = Typography.bodyMedium.copy(fontSize = 14.sp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = document.source,
                color = Color(0xFF5B5D7D),
                style = Typography.titleMedium.copy(fontSize = 14.sp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun DocumentDialog(
    document: DocumentInfo,
    onDismiss: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black.copy(alpha = 0.6f))
            .clickable(
                onClick = onDismiss,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            color = Color.White,
            shape = RoundedCornerShape(24.dp),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(32.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = document.type,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF5B5D7D)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Источник: ${document.source}",
                            fontSize = 14.sp,
                            color = Color(0xFF5B5D7D)
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Text("✕", fontSize = 28.sp, color = Color.Gray)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = document.content,
                            fontSize = 15.sp,
                            color = Color(0xFF5B5D7D),
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Chip(text: String, color: Color) {
    Box(
        modifier = Modifier
            .background(color.copy(alpha = 0.15f), CircleShape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            color = color.copy(alpha = 1f)
        )
    }
}

@Composable
fun InfoCard(title: String, content: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.widthIn(max = 500.dp),
        color = Color(0xFFF8F8FC),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = title,
                style = Typography.titleMedium.copy(fontSize = 16.sp),
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = content,
                fontSize = 14.sp,
                color = Color(0xFF5B5D7D),
            )
        }
    }
}

@Composable
fun RecipientsSection(
    recipients: List<Recipient>,
    onOpenTicket: (String) -> Unit
) {

    Column {
        Text(
            text = "Кому можно отнести это письмо?",
            style = Typography.titleLarge.copy(fontSize = 22.sp),
            modifier = Modifier.padding(bottom = 20.dp)
        )

        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            recipients.forEach { recipient ->
                RecipientCard(
                    recipient = recipient,
                    onOpenTicket = { onOpenTicket(recipient.id) }
                )
            }
        }
    }
}

@Composable
fun RecipientCard(
    recipient: Recipient,
    onOpenTicket: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color(0xFFF8F8FC),
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recipient.name,
                    style = Typography.titleMedium.copy(fontSize = 16.sp),
                    color = Color(0xFF5B5D7D)
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = recipient.department,
                    fontSize = 14.sp,
                    color = Color.Gray
                )
            }

            Button(
                onClick = onOpenTicket,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF5B5D7D)
                ),
                shape = CircleShape,
            ) {
                Text(
                    text = "Завести тикет в таск-трекере",
                    color = Color.White,
                    style = Typography.bodyMedium.copy(fontSize = 14.sp),
                )
            }
        }
    }
}

@Composable
fun ReadyTemplatesCard(
    templates: List<Template>,
    onCopyTemplate: (String) -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 100.dp),
        shape = RoundedCornerShape(44.dp),
        color = Color.White,
    ) {
        Column(modifier = Modifier.padding(40.dp)) {
            Text(
                text = "Готовые шаблоны с ответами",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                templates.forEach { template ->
                    TemplateCard(
                        template = template,
                        onCopy = { onCopyTemplate(template.id) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
fun TemplateCard(
    template: Template,
    onCopy: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        color = Color(0xFFF8F8FC),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = template.title,
                style = Typography.bodyMedium.copy(fontSize = 16.sp),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = template.body,
                style = Typography.bodyMedium.copy(fontSize = 14.sp),
                color = Color(0xFF5B5D7D),
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onCopy,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF5B5D7D)),
                shape = CircleShape,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    "Скопировать шаблон",
                    color = Color.White,
                    style = Typography.bodyMedium.copy(fontSize = 14.sp),
                )
            }
        }
    }
}
