package com.legalpathways.ai

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.navigation.compose.*
import com.legalpathways.ai.ui.screens.*
import com.legalpathways.ai.ui.theme.LegalPathwaysTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LegalPathwaysApp()
        }
    }
}

@Composable
fun LegalPathwaysApp() {
    LegalPathwaysTheme {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "home"
        ) {

            composable("home") {
                HomeScreen(
                    onNavigateToChat = {
                        navController.navigate("chat")
                    },
                    onNavigateToRoadmap = {
                        navController.navigate("roadmap")
                    }
                )
            }

            composable("chat") {
                CounselorScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("roadmap") {
                RoadmapScreen(
                    onBack = { navController.popBackStack() }
                )
            }

            composable("layer0") {
                Layer0Screen(navController)
            }

            composable("layer1") {
                Layer1Screen(navController)
            }
        }
    }
}