package com.legalpathways.ai.model

import com.google.gson.annotations.SerializedName

// ── Generic API wrapper ───────────────────────────────────────────────────────
data class ApiResponse<T>(
    val success: Boolean,
    val data: T?,
    val message: String? = null
)

// ── Ask / Chat ────────────────────────────────────────────────────────────────
data class AskRequest(
    val question: String,
    val religion: String = "hindu"
)

data class AskData(
    val answer: String,
    val explanation: String
)

data class CounselorRequest(val question: String)

data class CounselorData(
    val introduction: String,
    val understanding: String,
    val summary: String,
    val explanation: String,
    @SerializedName("key_points") val keyPoints: List<String>,
    val conclusion: String,
    val motivation: String
)

// ── Roadmap ───────────────────────────────────────────────────────────────────
data class RoadmapNode(
    val title: String?,
    val description: String?,
    val substeps: List<Any>?,
    val options: List<Any>?
)

data class RoadmapData(val steps: List<RoadmapNode>?)

// ── Layer 0 ───────────────────────────────────────────────────────────────────
data class Layer0Request(
    @SerializedName("relationship_status") val relationshipStatus: String,
    val religion: String,
    @SerializedName("marriage_act") val marriageAct: String,
    @SerializedName("children_flag") val childrenFlag: Boolean,
    @SerializedName("income_range") val incomeRange: String,
    @SerializedName("risk_indicator") val riskIndicator: String
)

data class Layer0Data(
    @SerializedName("applicable_law") val applicableLaw: String?,
    @SerializedName("key_points") val keyPoints: List<String>?,
    @SerializedName("allowed_actions") val allowedActions: List<String>?,
    @SerializedName("blocked_actions") val blockedActions: List<String>?,
    @SerializedName("recommended_next_step") val recommendedNextStep: String?
)

// ── Layer 1 (Phase 1 – Readiness) ────────────────────────────────────────────
data class EvidenceItem(
    val type: String,
    val description: String?,
    @SerializedName("legal_weight") val legalWeight: String,
    val requirements: List<String>?,
    @SerializedName("husband_strategy") val husbandStrategy: List<String>?,
    @SerializedName("wife_strategy") val wifeStrategy: List<String>?
)

data class EvidenceCategories(
    @SerializedName("high_relevance") val highRelevance: List<EvidenceItem>?,
    @SerializedName("medium_relevance") val mediumRelevance: List<EvidenceItem>?,
    @SerializedName("low_relevance") val lowRelevance: List<EvidenceItem>?
)

data class EligibilityData(
    val religion: String,
    @SerializedName("applicable_acts") val applicableActs: List<String>?,
    @SerializedName("min_age") val minAge: MinAge?,
    @SerializedName("consent_details") val consentDetails: String?,
    @SerializedName("witness_requirement") val witnessRequirement: WitnessReq?,
    @SerializedName("residency_requirement") val residencyRequirement: ResidencyReq?
)

data class MinAge(val male: String?, val female: String?)
data class WitnessReq(val details: String?)
data class ResidencyReq(val details: String?)

data class Phase1Data(
    val eligibility: EligibilityData?,
    @SerializedName("evidence_categories") val evidenceCategories: EvidenceCategories?
)

// ── Layer 3 ───────────────────────────────────────────────────────────────────
data class ScenarioItem(
    val type: String,
    val scenario: String,
    @SerializedName("court_relevance") val courtRelevance: String,
    @SerializedName("impact_on_maintenance") val impactOnMaintenance: String,
    @SerializedName("impact_on_custody") val impactOnCustody: String,
    @SerializedName("how_to_claim_maintenance") val howToClaimMaintenance: List<String>?
)

// ── Layer 4 ───────────────────────────────────────────────────────────────────
data class Layer4Request(@SerializedName("severity_level") val severityLevel: String)

data class Layer4Data(
    @SerializedName("severity_level") val severityLevel: String,
    @SerializedName("system_legal_direction") val systemLegalDirection: String,
    @SerializedName("urgency_level") val urgencyLevel: String,
    @SerializedName("police_intervention_required") val policeIntervention: Any?,
    @SerializedName("override_flag") val overrideFlag: String?,
    @SerializedName("immediate_actions") val immediateActions: List<String>?,
    @SerializedName("digital_vault_evidence") val digitalVaultEvidence: List<String>?,
    @SerializedName("primary_legal_basis") val primaryLegalBasis: List<String>?,
    @SerializedName("route_rationale") val routeRationale: String?
)

// ── Layer 5 ───────────────────────────────────────────────────────────────────
data class Layer5Request(
    @SerializedName("case_type") val caseType: String,
    @SerializedName("children_flag") val childrenFlag: Boolean,
    @SerializedName("income_ratio") val incomeRatio: String,
    @SerializedName("settlement_type") val settlementType: String
)

data class Layer5Data(
    @SerializedName("settlement_id") val settlementId: String?,
    val rationale: String?,
    @SerializedName("legal_basis") val legalBasis: List<String>?,
    @SerializedName("suggested_terms") val suggestedTerms: Map<String, Any>?,
    @SerializedName("mediation_notes") val mediationNotes: List<String>?,
    val disclaimer: String?
)

// ── Layer 7 ───────────────────────────────────────────────────────────────────
data class Layer7Module(
    val issue: String,
    @SerializedName("legal_provision") val legalProvision: List<String>,
    @SerializedName("what_law_says") val whatLawSays: String,
    @SerializedName("practical_reality") val practicalReality: String,
    @SerializedName("key_legal_limits") val keyLegalLimits: List<String>,
    @SerializedName("discretion_level") val discretionLevel: String,
    @SerializedName("time_limit_statutary") val timeLimitStatutary: String,
    @SerializedName("enforcement_required_separate_proceeding") val enforcementRequired: Boolean
)

data class Layer7Data(
    val description: String,
    val modules: List<Layer7Module>,
    @SerializedName("engine_flags") val engineFlags: Map<String, Boolean>
)

// ── Layer 8 ───────────────────────────────────────────────────────────────────
data class Layer8Stage(
    @SerializedName("case_stage") val caseStage: String,
    @SerializedName("expected_duration") val expectedDuration: Any?,
    @SerializedName("common_delays") val commonDelays: List<String>,
    @SerializedName("required_actions") val requiredActions: List<String>
)

data class Layer8Data(
    val phase: String,
    @SerializedName("dataset_name") val datasetName: String,
    val jurisdiction: String,
    @SerializedName("applicable_laws") val applicableLaws: List<String>,
    val stages: List<Layer8Stage>
)

// ── Layer 9 ───────────────────────────────────────────────────────────────────
data class CaseItem(
    @SerializedName("case_name") val caseName: String,
    val year: String,
    val court: String,
    val area: String,
    @SerializedName("ratio_decidendi") val ratioDecidendi: String?,
    @SerializedName("court_decision") val courtDecision: String?,
    @SerializedName("key_principles") val keyPrinciples: List<String>?,
    @SerializedName("husband_gain") val husbandGain: List<String>?,
    @SerializedName("wife_gain") val wifeGain: List<String>?,
    val enforceability: List<String>?
)

data class Layer9Data(
    val phase: String,
    val jurisdiction: String,
    @SerializedName("judgments_in_favour_of_husband") val husbandJudgments: List<CaseItem>?,
    @SerializedName("judgments_in_favour_of_wife") val wifeJudgments: List<CaseItem>?
)

// ── Layer 10 ──────────────────────────────────────────────────────────────────
data class Layer10Item(
    @SerializedName("what_to_update") val whatToUpdate: String,
    @SerializedName("authority_office") val authorityOffice: String,
    @SerializedName("documents_required") val documentsRequired: List<String>,
    @SerializedName("how_to_do_it") val howToDoIt: String,
    @SerializedName("risk_if_not_done") val riskIfNotDone: String
)

data class Layer10Data(val items: List<Layer10Item>)

// ── Chat message (local) ──────────────────────────────────────────────────────
data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val suggestions: List<String> = emptyList()
)