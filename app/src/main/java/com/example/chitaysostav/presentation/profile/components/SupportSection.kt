package com.example.chitaysostav.presentation.profile.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Policy
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitaysostav.presentation.theme.ChitaySostavTheme
import com.example.chitaysostav.presentation.theme.TextDisabled
import com.example.chitaysostav.presentation.theme.TextMuted

@Composable
fun SupportSection(
    name: String,
    label: String,
    leadingIcon: ImageVector,
    iconBgColor: Color,
    showTopDivider: Boolean = false,
    onClick: () -> Unit
) {
    if (showTopDivider) {
        HorizontalDivider(color = Color(0xFF1C1C1E), thickness = 1.dp)
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(iconBgColor, RoundedCornerShape(9.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = leadingIcon,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(16.dp)
            )
        }
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = name,
                fontSize = 14.sp,
                color = Color(0xFFE4E4E7),
                fontWeight = FontWeight.Medium,
            )
            if (label.isNotEmpty()) {
                Text(
                    text = label,
                    fontSize = 12.sp,
                    color = TextDisabled,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Text(
            text = "›",
            fontSize = 18.sp,
            color = TextDisabled,
            fontWeight = FontWeight.Light,
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun SupportSectionPreview() {
    ChitaySostavTheme {
        Column(
            modifier = Modifier
                .background(Color(0xFF141414), RoundedCornerShape(14.dp))
                .padding(vertical = 4.dp),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {
            SupportSection(
                name = "Написать нам",
                label = "Сообщить об ошибке или предложении",
                leadingIcon = Icons.Outlined.Email,
                iconBgColor = Color(0x1A4ADE80),
                onClick = {}
            )
            SupportSection(
                name = "Поделиться приложением",
                label = "",
                leadingIcon = Icons.Outlined.Share,
                iconBgColor = Color(0x1A4ADE80),
                showTopDivider = true,
                onClick = {}
            )
            SupportSection(
                name = "Политика конфиденциальности",
                label = "",
                leadingIcon = Icons.Outlined.Policy,
                iconBgColor = Color(0xFF1C1C1E),
                showTopDivider = true,
                onClick = {}
            )
        }
    }
}
