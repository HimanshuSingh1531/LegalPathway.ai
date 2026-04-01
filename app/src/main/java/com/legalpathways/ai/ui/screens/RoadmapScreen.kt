package com.legalpathways.ai.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.legalpathways.ai.ui.components.*
import com.legalpathways.ai.ui.theme.*
import com.legalpathways.ai.viewmodel.MainViewModel
import com.legalpathways.ai.viewmodel.UiState

val marriageOptions = listOf("hindu" to "Hindu (HMA)", "muslim" to "Muslim", "christian" to "Christian", "sma" to "Special Marriage Act")
val roleOptions     = listOf("husband" to "Husband", "wife" to "Wife")

val starterQs = mapOf(
    "hindu"     to listOf("What is mutual consent divorce?", "How long does it take?", "Who gets the house?"),
    "muslim"    to listOf("What is talaq procedure?", "What is iddat period?", "Can wife get khula?"),
    "christian" to listOf("What grounds exist for divorce?", "How long does it take?", "What about child custody?"),
    "sma"       to listOf("What is the SMA notice period?", "Can family object?", "How is alimony calculated?")
)

@Composable
fun RoadmapScreen(onBack: () -> Unit, vm: MainViewModel = viewModel()) {
    val marriageType by vm.selectedMarriageType.collectAsState()
    val role         by vm.selectedRole.collectAsState()
    val roadmapState by vm.roadmapState.collectAsState()
    val chatMessages by vm.chatMessages.collectAsState()
    val chatLoading  by vm.chatLoading.collectAsState()
    var chatInput    by remember { mutableStateOf("") }
    val listState    = rememberLazyListState()

    LaunchedEffect(marriageType, role) {
        vm.loadRoadmap(marriageType, if (marriageType == "sma") "husband" else role)
    }
    LaunchedEffect(chatMessages.size) {
        if (chatMessages.isNotEmpty()) listState.animateScrollToItem(chatMessages.size - 1)
    }

    Scaffold(
        topBar = {
            LegalTopBar(
                title  = "Divorce Roadmap + AI Chat",
                onBack = onBack,
                actions = {
                    if (chatMessages.isNotEmpty()) {
                        IconButton(onClick = { vm.clearLegalChat() }) {
                            Icon(Icons.Default.DeleteSweep, "Clear chat", tint = MaterialTheme.colorScheme.primary)
                        }
                    }
                }
            )
        },
        bottomBar = {
            Column {
                GoldDivider()
                ChatInputBar(
                    value         = chatInput,
                    onValueChange = { chatInput = it },
                    onSend        = { vm.sendLegalChat(chatInput, marriageType); chatInput = "" },
                    placeholder   = "Ask about $marriageType divorce law…",
                    enabled       = !chatLoading
                )
            }
        }
    ) { padding ->
        LazyColumn(
            state            = listState,
            contentPadding   = padding,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier         = Modifier.fillMaxSize().padding(horizontal = 16.dp)
        ) {
            item { Spacer(Modifier.height(4.dp)) }

            // ── Selectors ─────────────────────────────────────────────────────
            item {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Marriage Type", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(4.dp))
                        DropdownSelector(
                            options   = marriageOptions,
                            selected  = marriageType,
                            onSelect  = { vm.selectedMarriageType.value = it }
                        )
                    }
                    if (marriageType != "sma") {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Your Role", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            Spacer(Modifier.height(4.dp))
                            DropdownSelector(
                                options  = roleOptions,
                                selected = role,
                                onSelect = { vm.selectedRole.value = it }
                            )
                        }
                    }
                }
            }

            // ── Roadmap ───────────────────────────────────────────────────────
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        SectionHeader("Roadmap", Icons.Default.Map)
                        Spacer(Modifier.height(8.dp))
                        when (roadmapState) {
                            is UiState.Loading -> LinearProgressIndicator(modifier = Modifier.fillMaxWidth(), color = GoldPrimary)
                            is UiState.Success -> {
                                val data = (roadmapState as UiState.Success).data
                                if (data.steps.isNullOrEmpty()) {
                                    Text("No roadmap available for this selection.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                } else {
                                    data.steps.forEachIndexed { i, node ->
                                        RoadmapNode(node = node, depth = 0, onExplain = { text ->
                                            vm.sendLegalChat("Explain: $text", marriageType)
                                        })
                                        if (i < data.steps.size - 1) Spacer(Modifier.height(4.dp))
                                    }
                                }
                            }
                            is UiState.Error -> Text((roadmapState as UiState.Error).message, style = MaterialTheme.typography.bodySmall, color = CrimsonAccent)
                            else -> {}
                        }
                    }
                }
            }

            // ── Chat header ───────────────────────────────────────────────────
            item {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.SmartToy, null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    Text("Legal AI Assistant", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                }
            }

            // ── Starter chips or messages ─────────────────────────────────────
            if (chatMessages.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            StarterChips(
                                questions = starterQs[marriageType] ?: emptyList(),
                                onSelect  = { vm.sendLegalChat(it, marriageType) }
                            )
                        }
                    }
                }
            } else {
                items(chatMessages) { msg ->
                    ChatBubble(message = msg, onSuggestionClick = { vm.sendLegalChat(it, marriageType) })
                }
                if (chatLoading) {
                    item { TypingIndicator() }
                }
            }

            item { Spacer(Modifier.height(8.dp)) }
        }
    }
}

// ── Roadmap node (expandable) ─────────────────────────────────────────────────
@Composable
fun RoadmapNode(
    node: com.legalpathways.ai.model.RoadmapNode,
    depth: Int,
    onExplain: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val children = (node.substeps ?: emptyList()) + (node.options ?: emptyList())

    Column(modifier = Modifier.padding(start = (depth * 12).dp)) {
        if (node.title != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded }
                    .padding(vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    if (expanded) Icons.Default.ExpandMore else Icons.Default.ChevronRight,
                    null,
                    tint     = GoldPrimary,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(node.title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
            }
        }
        if (expanded) {
            node.description?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(start = 24.dp, bottom = 4.dp))
            }
        }
    }
}

// ── Dropdown selector ─────────────────────────────────────────────────────────
@Composable
fun DropdownSelector(
    options:  List<Pair<String, String>>,
    selected: String,
    onSelect: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val label    = options.find { it.first == selected }?.second ?: selected

    Box {
        OutlinedButton(
            onClick  = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
            colors   = ButtonDefaults.outlinedButtonColors(contentColor = NavyMid),
            border   = BorderStroke(1.dp, GoldPrimary.copy(alpha = 0.4f))
        ) {
            Text(label, style = MaterialTheme.typography.bodySmall, modifier = Modifier.weight(1f))
            Icon(Icons.Default.ArrowDropDown, null, modifier = Modifier.size(18.dp))
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            options.forEach { (value, text) ->
                DropdownMenuItem(
                    text    = { Text(text) },
                    onClick = { onSelect(value); expanded = false },
                    trailingIcon = if (value == selected) ({ Icon(Icons.Default.Check, null, tint = GoldPrimary) }) else null
                )
            }
        }
    }
}