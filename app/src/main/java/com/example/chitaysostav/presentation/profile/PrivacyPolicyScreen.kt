package com.example.chitaysostav.presentation.profile

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.R
import com.example.chitaysostav.presentation.theme.AccentGreen
import com.example.chitaysostav.presentation.theme.Background
import com.example.chitaysostav.presentation.theme.ChitaySostavTheme
import com.example.chitaysostav.presentation.theme.TextDisabled
import com.example.chitaysostav.presentation.theme.TextMuted
import com.example.chitaysostav.presentation.theme.TextPolicy

@Composable
fun PrivacyPolicyScreen(onBack: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
//                .padding(top = 24.dp, start = 20.dp, end = 20.dp, bottom = 8.dp)
            ,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_arrow_back),
                    contentDescription = "Назад",
                    tint = Color.White
                )
            }
            Text(
                text = "Политика конфиденциальности",
                color = Color.White,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Spacer(modifier = Modifier.height(8.dp))

        HorizontalDivider()

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Text(
                text = "Последнее обновление: 7 июля 2026 г.",
                color = TextDisabled,
                fontSize = 12.sp,
                fontWeight = FontWeight.Normal,
                textAlign = TextAlign.Start
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "1. Общие положения",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Настоящая политика конфиденциальности описывает, как приложение «Читай Состав» собирает, использует и защищает данные пользователей. Используя приложение, вы соглашаетесь с условиями настоящей политики.",
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "2. Собираемые данные",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            val section2 = buildAnnotatedString {
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                    append("Камера")
                }
                append(" — используется исключительно для сканирования штрихкодов в реальном времени. Изображения не сохраняются и не передаются на серверы.\n")
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                    append("История сканирования")
                }
                append(" — список отсканированных товаров хранится локально на вашем устройстве в закрытом хранилище приложения.\n")
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                    append("Данные о товарах")
                }
                append(" — если вы добавляете или редактируете товар, введённые данные (название, состав, КБЖУ) сохраняются в общей базе данных и становятся доступны другим пользователям при сканировании того же штрихкода.\n")
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                    append("Данные аккаунту")
                }
                append(" — при регистрации или входе через Google мы получаем от Firebase Authentication: адрес электронной почты и, при входе через Google, имя и фотографию профиля. Пароль в открытом виде нами не хранится и недоступен — он управляется сервисом Firebase Authentication (Google).")
            }
            Text(
                text = section2,
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "3. Использование данных",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Данные аккаунта используются исключительно для идентификации пользователя внутри приложения и отображения в профиле. Данные о товарах используются для работы основного функционала приложения. Мы не передаём ваши данные третьим лицам в коммерческих целях, не используем их в рекламных целях и не продаём.",
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "4. Хранение данных",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "История сканирований хранится локально на устройстве и может быть удалена в любой момент через раздел «История». Данные аккаунта и данные о товарах хранятся на серверах Google Firebase (регион: Западная Европа) в соответствии с политикой конфиденциальности Google.",
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "5. Разрешения приложения",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            val section5 = buildAnnotatedString {
                withStyle(SpanStyle(color = Color.White, fontWeight = FontWeight.Bold)) {
                    append("Камера")
                }
                append(" — обязательное разрешение для сканирования штрихкодов. Без него основной функционал приложения недоступен. Приложение запрашивает это разрешение только при первом обращении к функции сканирования.")
            }
            Text(
                text = section5,
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "6. Права пользователя",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Вы вправе в любой момент удалить историю сканирований через раздел «История». Вы вправе удалить свой аккаунт — все данные аккаунта будут удалены из системы. По запросу мы также удалим данные о добавленных вами товарах из общей базы данных.",
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "7. Контакты",
                color = AccentGreen,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "По вопросам, связанным с политикой конфиденциальности, а также для запроса на удаление данных, обращайтесь через раздел «Написать нам» в профиле приложения.",
                color = TextPolicy,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal
            )

        }


    }


}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun PrivacyPolicyScreenPreview() {
    ChitaySostavTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PrivacyPolicyScreen(onBack = {})
        }
    }
}