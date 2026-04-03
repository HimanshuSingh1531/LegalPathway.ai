package com.legalpathways.ai.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.legalpathways.ai.model.Layer0Request
import com.legalpathways.ai.ui.components.*
import com.legalpathways.ai.ui.theme.*
import com.legalpathways.ai.viewmodel.MainViewModel
import com.legalpathways.ai.viewmodel.UiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Layer0Screen(onBack: () -> Unit, vm: MainViewModel = viewModel()) {
    val state      by vm.layer0State.collectAsState()
    var step       by remember { mutableStateOf(1) }
    var relStatus  by remember { mutableStateOf("") }
    var religion   by remember { mutableStateOf("") }
    var marriageAct by remember { mutableStateOf("") }
    var children   by remember { mutableStateOf(false) }
    var childrenSelected by remember { mutableStateOf(false) }
    var income     by remember { mutableStateOf("") }
    var risk       by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    // MCQ Data
    val mcqSteps = listOf(
        MCQQuestion(
            id = "relStatus",
            question = "What is your current relationship status?",
            options = listOf(
                "dating" to "Dating",
                "engaged" to "Engaged",
                "married" to "Married",
                "separated" to "Separated"
            )
        ),
        MCQQuestion(
            id = "religion",
            question = "Which religion / marriage act applies?",
            options = listOf(
                "Hindu" to "Hindu (HMA)",
                "Muslim" to "Muslim (MLA)",
                "Christian" to "Christian (IDA)",
                "Special Marriage" to "Special Marriage Act (SMA)",
                "Parsi" to "Parsi (PMDA)"
            )
        ),
        MCQQuestion(
            id = "children",
            question = "Do you have children together?",
            options = listOf(
                "yes" to "Yes",
                "no" to "No"
            )
        ),
        MCQQuestion(
            id = "income",
            question = "What is your income bracket?",
            options = listOf(
                "low" to "Low Income",
                "medium" to "Medium Income",
                "high" to "High Income"
            )
        ),
        MCQQuestion(
            id = "risk",
            question = "Any risk indicators in your situation?",
            options = listOf(
                "none" to "No Risk",
                "financial_dependency" to "Financial Dependency",
                "abuse" to "Abuse or Harm",
                "violence" to "Violence",
                "abandonment" to "Abandonment"
            )
        )
    )

    LaunchedEffect(state) {
        if (state is UiState.Idle) step = 1
    }

    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            NavyDeep.copy(alpha = 0.15f),
            ParchmentLight
        ),
        startY = 0f,
        endY = 1200f
    )

    Scaffold(topBar = { LegalTopBar("Phase 0 – Legal Positioning", onBack) }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(backgroundGradient)
                .padding(padding)
        ) {
            when (val s = state) {
                is UiState.Success -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp)
                    ) {
                        Layer0Result(data = s.data) { vm.resetLayer0(); step = 1 }
                    }
                }
                is UiState.Loading -> LoadingContent()
                is UiState.Error   -> ErrorContent(s.message) { vm.resetLayer0() }
                else -> {
                    // MCQ Flow - FIXED LAYOUT
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        // Main content - scrollable
                        Column(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp)
                                .padding(top = 16.dp),
                            verticalArrangement = Arrangement.Top
                        ) {
                            when (step) {
                                1 -> MCQStepDisplayFixed(
                                    mcqSteps[0],
                                    relStatus,
                                    onSelect = {
                                        relStatus = it
                                        coroutineScope.launch {
                                            delay(400)
                                            step++
                                        }
                                    }
                                )
                                2 -> MCQStepDisplayFixed(
                                    mcqSteps[1],
                                    religion,
                                    onSelect = {
                                        religion = it
                                        marriageAct = mapReligionToAct(it)
                                        coroutineScope.launch {
                                            delay(400)
                                            step++
                                        }
                                    }
                                )
                                3 -> {
                                    val childMcq = MCQQuestion(
                                        id = "children",
                                        question = mcqSteps[2].question,
                                        options = mcqSteps[2].options
                                    )
                                    MCQStepDisplayFixed(
                                        childMcq,
                                        if (childrenSelected) (if (children) "yes" else "no") else "",
                                        onSelect = {
                                            children = it == "yes"
                                            childrenSelected = true
                                            coroutineScope.launch {
                                                delay(400)
                                                step++
                                            }
                                        }
                                    )
                                }
                                4 -> MCQStepDisplayFixed(
                                    mcqSteps[3],
                                    income,
                                    onSelect = {
                                        income = it
                                        coroutineScope.launch {
                                            delay(400)
                                            step++
                                        }
                                    }
                                )
                                5 -> MCQStepDisplayFixed(
                                    mcqSteps[4],
                                    risk,
                                    onSelect = {
                                        risk = it
                                        coroutineScope.launch {
                                            delay(400)
                                            step++
                                        }
                                    }
                                )
                                6 -> ReviewStepModern(
                                    rel = relStatus,
                                    religion = religion,
                                    children = children,
                                    income = income,
                                    risk = risk
                                )
                                else -> {}
                            }
                        }

                        // Bottom button bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Back button (always shown except on step 1)
                            if (step > 1) {
                                OutlinedButton(
                                    onClick = { step-- },
                                    modifier = Modifier
                                        .height(52.dp)
                                        .width(56.dp),
                                    border = BorderStroke(1.5.dp, NavyMid),
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyMid),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.ChevronLeft, contentDescription = "Previous", modifier = Modifier.size(22.dp))
                                }
                            }

                            // Progress dots
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .weight(1f)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            ) {
                                repeat(6) { i ->
                                    Box(
                                        modifier = Modifier
                                            .size(if (i == step - 1) 10.dp else 8.dp)
                                            .background(
                                                color = if (i < step) NavyMid else NavyMid.copy(alpha = 0.25f),
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }

                            // Next button on MCQ steps, Submit on review step
                            if (step < 6) {
                                Button(
                                    onClick = { step++ },
                                    modifier = Modifier
                                        .height(52.dp)
                                        .width(56.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyMid),
                                    shape = RoundedCornerShape(26.dp),
                                    contentPadding = PaddingValues(0.dp)
                                ) {
                                    Icon(Icons.Default.ChevronRight, contentDescription = "Next", tint = Color.White, modifier = Modifier.size(22.dp))
                                }
                            } else {
                                Button(
                                    onClick = {
                                        val request = Layer0Request(
                                            relationshipStatus = relStatus,
                                            religion = religion,
                                            marriageAct = marriageAct,
                                            childrenFlag = children,
                                            incomeRange = income,
                                            riskIndicator = risk
                                        )
                                        vm.submitLayer0(request)
                                    },
                                    modifier = Modifier
                                        .height(52.dp)
                                        .weight(1f),
                                    colors = ButtonDefaults.buttonColors(containerColor = NavyMid),
                                    shape = RoundedCornerShape(26.dp)
                                ) {
                                    Text(
                                        " Submit",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * MCQ Step Display with Modern Styling - FIXED text visibility
 */
@Composable
fun MCQStepDisplayFixed(
    question: MCQQuestion,
    selectedValue: String,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp),
        verticalArrangement = Arrangement.spacedBy(32.dp)
    ) {
        // Question header
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(20.dp),
            color = NavyMid,
            shadowElevation = 6.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    question.question,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    fontSize = 18.sp,
                    lineHeight = 24.sp
                )
            }
        }

        // Options
        Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
            question.options.forEachIndexed { index, (value, label) ->
                MCQOptionButtonFixed(
                    label = label,
                    letter = ('A' + index).toString(),
                    isSelected = selectedValue == value,
                    onClick = { onSelect(value) }
                )
            }
        }
    }
}

/**
 * Individual MCQ Option Button - FIXED for text visibility
 * - Dark text color (0xFF1A1A1A) on white background for maximum contrast
 * - Proper font sizes for readability
 * - White text on navy when selected
 */
@Composable
fun MCQOptionButtonFixed(
    label: String,
    letter: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val fillAnimation = remember { Animatable(0f) }

    LaunchedEffect(isSelected) {
        if (isSelected) {
            fillAnimation.animateTo(
                1f,
                animationSpec = tween(500, easing = EaseOutCubic)
            )
        } else {
            fillAnimation.snapTo(0f)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .clickable { onClick() }
    ) {
        // ─── BACKGROUND FILL DRAWN FIRST (behind content) ───
        if (fillAnimation.value > 0f) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(fillAnimation.value)
                    .fillMaxHeight()
                    .align(Alignment.CenterStart)
                    .background(
                        color = NavyMid,
                        shape = RoundedCornerShape(28.dp)
                    )
            )
        }

        // ─── BORDER + CONTENT DRAWN ON TOP OF FILL ───
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = Color.Transparent,
            border = BorderStroke(
                width = if (isSelected) 3.dp else 2.dp,
                color = if (isSelected) NavyMid else NavyMid.copy(alpha = 0.25f)
            ),
            shape = RoundedCornerShape(28.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 18.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // ─── LETTER CIRCLE ───
                Surface(
                    shape = CircleShape,
                    color = if (isSelected) Color.White.copy(alpha = 0.2f) else NavyMid.copy(alpha = 0.12f),
                    modifier = Modifier.size(36.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            letter,
                            style = MaterialTheme.typography.labelLarge,
                            color = if (isSelected) Color.White else NavyDeep,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                // ─── OPTION LABEL - text color matches background state ───
                Text(
                    label,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (isSelected) Color.White else Color(0xFF1A1A1A),
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                    fontSize = 16.sp,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                // ─── CHECKMARK (when selected) ───
                if (isSelected) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

/**
 * Review step with modern design
 */
@Composable
fun ReviewStepModern(rel: String, religion: String, children: Boolean, income: String, risk: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 40.dp)
    ) {
        Text(
            "Review Your Answers",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                ReviewRow("Relationship Status:", rel.replaceFirstChar { it.uppercase() })
                ReviewRow("Religion / Act:", religion)
                ReviewRow("Children:", if (children) "Yes" else "No")
                ReviewRow("Income Bracket:", income.replaceFirstChar { it.uppercase() })
                ReviewRow("Risk Indicators:", risk.replace("_", " ").replaceFirstChar { it.uppercase() })
            }
        }

        Text(
            "Click Submit to get your legal position analysis",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun ReviewRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(value, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
    }
}

/**
 * Map religion selection to marriage act code
 */
fun mapReligionToAct(religion: String): String = when (religion) {
    "Hindu" -> "HMA"
    "Muslim" -> "MLA"
    "Christian" -> "IDA"
    "Special Marriage" -> "SMA"
    "Parsi" -> "PMDA"
    else -> "HMA"
}

/**
 * Existing result component (kept from original)
 */
@Composable
fun Layer0Result(data: com.legalpathways.ai.model.Layer0Data, onReset: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Title
        Text(
            "Your Legal Position",
            style = MaterialTheme.typography.headlineMedium,
            color = Color(0xFF1A1A2E),
            fontWeight = FontWeight.Bold
        )

        data.applicableLaw?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    SectionHeader("Applicable Law", Icons.Default.MenuBook)
                    Spacer(Modifier.height(6.dp))
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2D2D2D)
                    )
                }
            }
        }

        if (!data.allowedActions.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldAccent, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Allowed Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EmeraldAccent)
                    }
                    Spacer(Modifier.height(10.dp))
                    BulletList(data.allowedActions, EmeraldAccent)
                }
            }
        }

        if (!data.blockedActions.isNullOrEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Block, contentDescription = null, tint = CrimsonAccent, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Blocked Actions", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = CrimsonAccent)
                    }
                    Spacer(Modifier.height(10.dp))
                    BulletList(data.blockedActions, CrimsonAccent)
                }
            }
        }

        data.recommendedNextStep?.let {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = GoldPrimary, modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Recommended Next Step", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = GoldPrimary)
                    }
                    Spacer(Modifier.height(6.dp))
                    Text(
                        it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF2D2D2D)
                    )
                }
            }
        }

        Spacer(Modifier.height(4.dp))

        OutlinedButton(
            onClick = onReset,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            border = BorderStroke(1.5.dp, NavyMid),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = NavyMid),
            shape = RoundedCornerShape(26.dp)
        ) {
            Icon(Icons.Default.Refresh, null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(8.dp))
            Text("New Assessment", fontWeight = FontWeight.SemiBold, fontSize = 15.sp)
        }
    }
}


// Data class - Keep from original
data class MCQQuestion(
    val id: String,
    val question: String,
    val options: List<Pair<String, String>>,
    val imageUrl: String? = null
)