package assistantbutton.style

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import org.jetbrains.compose.resources.Font
import psbcommersant.assistantbutton.generated.resources.Res
import psbcommersant.assistantbutton.generated.resources.gilroy_bold
import psbcommersant.assistantbutton.generated.resources.gilroy_medium
import psbcommersant.assistantbutton.generated.resources.gilroy_regular
import psbcommersant.assistantbutton.generated.resources.gilroy_semibold


val Typography
    @Composable
    get() = Typography().let {
        val gilroyRegular = FontFamily(Font(Res.font.gilroy_regular))
        val gilroyMedium = FontFamily(Font(Res.font.gilroy_medium))
        val gilroySemibold = FontFamily(Font(Res.font.gilroy_semibold))
        val gilroyBold = FontFamily(Font(Res.font.gilroy_bold))
        it.copy(
            displayLarge = it.displayLarge.copy(fontFamily = gilroyBold),
            displayMedium = it.displayMedium.copy(fontFamily = gilroyBold),
            displaySmall = it.displaySmall.copy(fontFamily = gilroyBold),
            headlineLarge = it.headlineLarge.copy(fontFamily = gilroyBold),
            headlineMedium = it.headlineMedium.copy(fontFamily = gilroyBold),
            headlineSmall = it.headlineSmall.copy(fontFamily = gilroyBold),
            titleLarge = it.titleLarge.copy(fontFamily = gilroySemibold),
            titleMedium = it.titleMedium.copy(fontFamily = gilroySemibold),
            titleSmall = it.titleSmall.copy(fontFamily = gilroySemibold),
            bodyLarge = it.bodyLarge.copy(fontFamily = gilroyMedium),
            bodyMedium = it.bodyMedium.copy(fontFamily = gilroyMedium),
            bodySmall = it.bodySmall.copy(fontFamily = gilroyMedium),
            labelLarge = it.labelLarge.copy(fontFamily = gilroyRegular),
            labelMedium = it.labelMedium.copy(fontFamily = gilroyRegular),
            labelSmall = it.labelSmall.copy(fontFamily = gilroyRegular)
        )
    }