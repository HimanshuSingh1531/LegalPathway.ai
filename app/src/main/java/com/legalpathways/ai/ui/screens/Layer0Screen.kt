package com.legalpathways.ai.ui.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.legalpathways.ai.model.Layer0Request
import com.legalpathways.ai.ui.components.*
import com.legalpathways.ai.ui.theme.*
import com.legalpathways.ai.viewmodel.MainViewModel
import com.legalpathways.ai.viewmodel.UiState

@Composable
fun Layer0Screen(onBack: () -> Unit, vm: MainViewModel = viewModel()) {
    val state      by vm.layer0State.collectAsState()
    var step       by remember { mutableStateOf(1) }
    var relStatus  by remember { mutableStateOf("dating") }
    var religion   by remember { mutableStateOf("Hindu") }
    var marriageAct by remember { mutableStateOf("HMA") }
    var children   by remember { mutableStateOf(false) }
    var income     by remember { mutableStateOf("low") }
    var risk       by remember { mutableStateOf("none") }

    LaunchedEffect(state) { if (state is UiState.Idle) step = 1 }

    Scaffold(topBar = { LegalTopBar("Phase 0 – Legal Positioning", onBack) }) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(16.dp)
        ) {
            when (val s = state) {
                is UiState.Success -> {
                    Layer0Result(data = s.data) { vm.resetLayer0(); step = 1 }
                }
                is UiState.Loading -> LoadingContent()
                is UiState.Error   -> ErrorContent(s.message) { vm.resetLayer0() }
                else -> {
                    // Step progress
                    StepProgress(current = step, total = 6)
                    Spacer(Modifier.height(20.dp))

                    when (step) {
                        1 -> OptionStep("What is your current relationship status?",
                            listOf("dating" to "💕 Dating", "engaged" to "💍 Engaged", "married" to "💒 Married", "separated" to "🚪 Separated"),
                            relStatus) { relStatus = it }

                        2 -> ReligionStep(religion) { rel, act -> religion = rel; marriageAct = act }

                        3 -> BoolStep("Do you have children together?", children) { children = it }

                        4 -> OptionStep("What is your income bracket?",
                            listOf("low" to "💰 Low", "medium" to "💳 Medium", "high" to "🏦 High"),
                            income) { income = it }

                        5 -> OptionStep("Any risk indicators?",
                            listOf(
                                "none"                  to "✅ No Risk",
                                "financial_dependency"  to "💸 Financial Dependency",
                                "abuse"                 to "🚨 Abuse",
                                "violence"              to "⚠️ Violence",
                                "abandonment"           to "🚪 Abandonment"
                            ), risk) { risk = it }

                        6 -> ReviewStep(relStatus, religion, children, income, risk) {
                            vm.submitLayer0(Layer0Request(relStatus, religion, marriageAct, children, income, risk))
                        }
                    }

                    Spacer(Modifier.height(24.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        if (step > 1) {
                            OutlinedButton(onClick = { step-- }, modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyMid),
                                border = BorderStroke(1.dp, NavyMid)) { Text("← Back") }
                        }
                        if (step < 6) {
                            GoldButton("Next →", onClick = { step++ }, modifier = Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepProgress(current: Int, total: Int) {
    Column {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            repeat(total) { i ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .background(if (i < current) GoldPrimary else MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(2.dp))
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text("Step $current of $total", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
fun OptionStep(question: String, options: List<Pair<String, String>>, selected: String, onSelect: (String) -> Unit) {
    Text(question, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
    Spacer(Modifier.height(16.dp))
    options.forEach { (value, label) ->
        val isSelected = selected == value
        Card(
            onClick   = { onSelect(value) },
            modifier  = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape     = RoundedCornerShape(10.dp),
            colors    = CardDefaults.cardColors(
                containerColor = if (isSelected) NavyMid.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface
            ),
            border = if (isSelected) BorderStroke(2.dp, NavyMid) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1f))
                if (isSelected) Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun ReligionStep(selected: String, onSelect: (String, String) -> Unit) {
    val options = listOf(
        Triple("Hindu",            "🛕 Hindu",            "HMA"),
        Triple("Muslim",           "🕌 Muslim",           "MLA"),
        Triple("Christian",        "⛪ Christian",         "IDA"),
        Triple("Special Marriage", "⚖️ Special Marriage", "SMA"),
        Triple("Parsi",            "🔥 Parsi",             "PMDA")
    )
    Text("Which religion / marriage act applies?", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
    Spacer(Modifier.height(16.dp))
    options.forEach { (value, label, act) ->
        val isSelected = selected == value
        Card(
            onClick   = { onSelect(value, act) },
            modifier  = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape     = RoundedCornerShape(10.dp),
            colors    = CardDefaults.cardColors(containerColor = if (isSelected) NavyMid.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface),
            border    = if (isSelected) BorderStroke(2.dp, NavyMid) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal, modifier = Modifier.weight(1f))
                if (isSelected) Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun BoolStep(question: String, value: Boolean, onSelect: (Boolean) -> Unit) {
    Text(question, style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
    Spacer(Modifier.height(16.dp))
    listOf(true to "✅ Yes", false to "❌ No").forEach { (v, label) ->
        Card(
            onClick   = { onSelect(v) },
            modifier  = Modifier.fillMaxWidth().padding(vertical = 4.dp),
            shape     = RoundedCornerShape(10.dp),
            colors    = CardDefaults.cardColors(containerColor = if (value == v) NavyMid.copy(alpha = 0.08f) else MaterialTheme.colorScheme.surface),
            border    = if (value == v) BorderStroke(2.dp, NavyMid) else BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
        ) {
            Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                if (value == v) Icon(Icons.Default.CheckCircle, null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun ReviewStep(rel: String, religion: String, children: Boolean, income: String, risk: String, onSubmit: () -> Unit) {
    Text("Review & Submit", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onBackground)
    Spacer(Modifier.height(16.dp))
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors   = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            InfoRow("Status:", rel.replaceFirstChar { it.uppercase() })
            InfoRow("Religion:", religion)
            InfoRow("Children:", if (children) "Yes" else "No")
            InfoRow("Income:", income.replaceFirstChar { it.uppercase() })
            InfoRow("Risk:", risk.replace("_", " ").replaceFirstChar { it.uppercase() })
        }
    }
    Spacer(Modifier.height(16.dp))
    GoldButton("🚀 Get Legal Position", onClick = onSubmit)
}

@Composable
fun Layer0Result(data: com.legalpathways.ai.model.Layer0Data, onReset: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text("📍 Your Legal Position", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)

        data.applicableLaw?.let {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = NavyMid.copy(alpha = 0.06f))) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SectionHeader("Applicable Law", Icons.Default.MenuBook)
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        if (!data.allowedActions.isNullOrEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = EmeraldAccent.copy(alpha = 0.06f))) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SectionHeader("Allowed Actions", Icons.Default.CheckCircle)
                    BulletList(data.allowedActions, EmeraldAccent)
                }
            }
        }

        if (!data.blockedActions.isNullOrEmpty()) {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = CrimsonAccent.copy(alpha = 0.06f))) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SectionHeader("Blocked Actions", Icons.Default.Block)
                    BulletList(data.blockedActions, CrimsonAccent)
                }
            }
        }

        data.recommendedNextStep?.let {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = GoldPrimary.copy(alpha = 0.06f))) {
                Column(modifier = Modifier.padding(14.dp)) {
                    SectionHeader("Recommended Next Step", Icons.Default.ArrowForward)
                    Text(it, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }

        OutlinedButton(onClick = onReset, modifier = Modifier.fillMaxWidth(),
            border = BorderStroke(1.dp, NavyMid), colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyMid)) {
            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(6.dp))
            Text("New Assessment")
        }
    }
}