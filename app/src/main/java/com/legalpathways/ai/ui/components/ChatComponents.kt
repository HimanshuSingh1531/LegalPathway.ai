package com.legalpathways.ai.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import com.legalpathways.ai.model.ChatMessage
import com.legalpathways.ai.ui.theme.*
import androidx.compose.animation.core.*
import androidx.compose.material.icons.filled.Forum

// ── Chat Bubble ───────────────────────────────────────────────────────────────
@Composable
fun ChatBubble(message: ChatMessage, onSuggestionClick: (String) -> Unit = {}) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (message.isUser) Alignment.End else Alignment.Start
    ) {
        Surface(
            shape = RoundedCornerShape(
                topStart  = if (message.isUser) 16.dp else 4.dp,
                topEnd    = if (message.isUser) 4.dp else 16.dp,
                bottomStart = 16.dp,
                bottomEnd   = 16.dp
            ),
            color = if (message.isUser) NavyMid else MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 2.dp,
            modifier = Modifier.widthIn(max = 300.dp)
        ) {
            Column(modifier = Modifier.padding(12.dp, 10.dp)) {
                Text(
                    text  = message.text,
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Suggestion chips
        if (!message.isUser && message.suggestions.isNotEmpty()) {
            Spacer(Modifier.height(6.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 4.dp)
            ) {
                message.suggestions.take(3).forEach { s ->
                    AssistChip(
                        onClick = { onSuggestionClick(s) },
                        label   = { Text(s, style = MaterialTheme.typography.labelSmall, maxLines = 1) },
                        colors  = AssistChipDefaults.assistChipColors(
                            containerColor = NavyMid.copy(alpha = 0.08f),
                            labelColor     = NavyMid
                        ),
                        border = AssistChipDefaults.assistChipBorder(
                            borderColor = NavyMid.copy(alpha = 0.3f),
                            enabled = true
                        )
                    )
                }
            }
        }
    }
}

// ── Typing indicator ──────────────────────────────────────────────────────────
@Composable
fun TypingIndicator() {
    val dotAlpha = rememberInfiniteTransition(label = "dots")
    val alpha by dotAlpha.animateFloat(
        initialValue = 0.2f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(600), RepeatMode.Reverse),
        label = "alpha"
    )
    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = MaterialTheme.colorScheme.surfaceVariant,
            shadowElevation = 1.dp
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(3) { i ->
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(GoldPrimary.copy(alpha = if (i == 0) alpha else if (i == 1) alpha * 0.7f else alpha * 0.4f))
                    )
                }
            }
        }
    }
}

// ── Chat Input Bar ────────────────────────────────────────────────────────────
@Composable
fun ChatInputBar(
    value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    placeholder: String = "Type your message…",
    enabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value         = value,
            onValueChange = onValueChange,
            placeholder   = { Text(placeholder, style = MaterialTheme.typography.bodyMedium) },
            modifier      = Modifier.weight(1f),
            enabled       = enabled,
            shape         = RoundedCornerShape(24.dp),
            colors        = OutlinedTextFieldDefaults.colors(
                focusedBorderColor   = GoldPrimary,
                unfocusedBorderColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor    = GoldPrimary
            ),
            maxLines = 3
        )
        Spacer(Modifier.width(8.dp))
        FloatingActionButton(
            onClick            = { if (value.isNotBlank() && enabled) onSend() },
            containerColor     = if (value.isNotBlank() && enabled) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant,
            modifier           = Modifier.size(48.dp),
            shape              = CircleShape
        ) {
            Icon(
                Icons.Default.Send, "Send",
                tint     = if (value.isNotBlank() && enabled) NavyDeep else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

// ── Starter chips ─────────────────────────────────────────────────────────────
@Composable
fun StarterChips(questions: List<String>, onSelect: (String) -> Unit) {
    Column(
        modifier            = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            androidx.compose.material.icons.Icons.Default.Forum,
            null,
            tint     = GoldPrimary.copy(alpha = 0.6f),
            modifier = Modifier.size(40.dp)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Ask anything or try a question below",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(12.dp))
        questions.forEach { q ->
            OutlinedButton(
                onClick  = { onSelect(q) },
                modifier = Modifier.fillMaxWidth(),
                colors   = ButtonDefaults.outlinedButtonColors(contentColor = NavyMid),
                border   = BorderStroke(1.dp, NavyMid.copy(alpha = 0.3f)),
                shape    = RoundedCornerShape(20.dp)
            ) {
                Text(q, style = MaterialTheme.typography.bodySmall)
            }
            Spacer(Modifier.height(6.dp))
        }
    }
}