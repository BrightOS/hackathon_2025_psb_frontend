package assistantbutton

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import assistantbutton.style.Typography
import org.jetbrains.compose.resources.vectorResource
import psbcommersant.assistantbutton.generated.resources.Res
import psbcommersant.assistantbutton.generated.resources.ic_psb_kommersant_white

expect fun assistantNavTo(email: String, mail: String, targetUrl: String)

@Composable
fun AssistantButton(
    email: String,
    mail: String,
    targetUrl: String = "http://localhost:8080"
) {
    val textColor = Color.White
    val shape: Shape = CircleShape
    val shadowColor = Color(0xFF4E4D83)
    val gradient = Brush.horizontalGradient(colors = listOf(Color(0xFF7676A9), Color(0xFF4E4E6F)))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)
    ) {
        Button(
            onClick = {
                assistantNavTo(email, mail, targetUrl)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 32.dp, bottom = 32.dp)
                .shadow(elevation = 4.dp, shape = shape, ambientColor = shadowColor),
            shape = shape,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
            contentPadding = PaddingValues(0.dp),
            elevation = null,
        ) {
            Row(
                modifier = Modifier
                    .background(gradient)
                    .padding(horizontal = 27.dp, vertical = 20.dp)
                    .align(Alignment.CenterVertically),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    vectorResource(Res.drawable.ic_psb_kommersant_white),
                    modifier = Modifier.height(24.dp),
                    contentDescription = "icon",
                )
                Spacer(modifier = Modifier.width(15.dp))
                Text(
                    modifier = Modifier.padding(vertical = 3.dp),
                    text = "Ассистент",
                    color = textColor,
                    style = Typography.bodyLarge.copy(
                        fontSize = 18.sp,
                        shadow = Shadow(
                            color = shadowColor,
                            offset = Offset(0f, 2f),
                            blurRadius = 4f,
                        ),
                    ),
                )
            }
        }
    }
}
