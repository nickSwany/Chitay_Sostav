package com.example.chitaysostav.presentation.profile

import android.content.Intent
import android.content.res.Configuration
import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chitaysostav.R
import com.example.chitaysostav.presentation.profile.components.StatusChipCard
import com.example.chitaysostav.presentation.profile.components.SupportSection
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.AccentGreenDark
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.Border
import com.example.chitaysostav.presentation.theme.CardSurface
import com.example.chitaysostav.presentation.theme.ChitaySostavTheme
import com.example.chitaysostav.presentation.theme.SurfaceVariant
import com.example.chitaysostav.presentation.theme.TextMuted
import com.example.chitaysostav.presentation.theme.TextPrimary
import androidx.core.net.toUri

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = hiltViewModel(), onPrivacyPolicyClick: () -> Unit) {
    val stats by viewModel.status.collectAsStateWithLifecycle()
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
        ) {
            Text(
                text = "Профиль",
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = (-0.5).sp,
                modifier = Modifier.align(Alignment.BottomStart)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Box(
                modifier = Modifier
                    .size(72.dp)
                    .border(width = 1.5.dp, color = Border, shape = CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.Person,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Вы не вошли",
                fontSize = 16.sp,
                color = TextMuted,
                fontWeight = FontWeight.Bold,
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Создайте аккаунт или войдите, чтобы синхронизировать историю между устройствами",
                fontSize = 12.sp,
                lineHeight = 16.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = AccentGreen,
                    contentColor = AccentGreenDark
                )
            ) {
                Text(
                    text = "Зарегистрироваться",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Background,
                    contentColor = TextPrimary
                ),
                border = BorderStroke(1.dp, Border)
            ) {
                Text(
                    "Уже есть аккаунт? Войти",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                HorizontalDivider(modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "или", color = TextMuted, fontSize = 12.sp)
                Spacer(modifier = Modifier.width(8.dp))
                HorizontalDivider(modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = { },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = SurfaceVariant,
                    contentColor = TextPrimary
                ),
                border = BorderStroke(1.dp, Border)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_google),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Войти через Google",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                StatusChipCard(
                    name = "Отсканировано",
                    count = stats.scanned.toString(),
                    Modifier.weight(1f)
                )
                StatusChipCard(
                    name = "Добавлено",
                    count = stats.added.toString(),
                    Modifier.weight(1f)
                )
                StatusChipCard(
                    name = "Изменено",
                    count = stats.edited.toString(),
                    Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "О ПРИЛОЖЕНИИ",
                    color = TextMuted,
                    fontSize = 11.sp,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardSurface, RoundedCornerShape(14.dp))
                    .border(1.dp, Border, RoundedCornerShape(14.dp))
            ) {
                SupportSection(
                    name = "Написать нам",
                    label = "Сообщить об ошибке или предложении",
                    leadingIcon = Icons.Outlined.Email,
                    iconBgColor = Color(0x1A4ADE80),
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            data = "mailto:nkt.lebedev@yandex.ru".toUri()
                            putExtra(Intent.EXTRA_EMAIL, "Читай Состав - обратная ссвязь")
                        }
                        context.startActivity(Intent.createChooser(intent, "Выберите приложение"))
                    }
                )
                SupportSection(
                    name = "Поделиться приложением",
                    label = "",
                    leadingIcon = Icons.Outlined.Share,
                    iconBgColor = Color(0x1A4ADE80),
                    showTopDivider = true,
                    onClick = {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "text/plain"
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Попробуй приложение «Читай Состав» — сканируй штрихкоды и узнавай состав продуктов!"
                            )
                        }
                        context.startActivity(Intent.createChooser(intent, "Поделиться"))

                    }
                )
                SupportSection(
                    name = "Политика конфиденциальности",
                    label = "",
                    leadingIcon = Icons.Outlined.Policy,
                    iconBgColor = Color(0xFF1C1C1E),
                    showTopDivider = true,
                    onClick = onPrivacyPolicyClick
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun ProfileScreenPreview() {
    ChitaySostavTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ProfileScreen(onPrivacyPolicyClick = {})
        }
    }
}