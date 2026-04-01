package com.legalpathways.ai.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.legalpathways.ai.ui.screens.*
import com.legalpathways.ai.viewmodel.MainViewModel

@Composable
fun LegalNavHost(navController: NavHostController) {
    // Shared ViewModel across most screens
    val sharedVm: MainViewModel = viewModel()

    NavHost(
        navController    = navController,
        startDestination = "home",
        enterTransition  = { slideInHorizontally(tween(300)) { it / 2 } + fadeIn(tween(300)) },
        exitTransition   = { slideOutHorizontally(tween(300)) { -it / 4 } + fadeOut(tween(200)) },
        popEnterTransition  = { slideInHorizontally(tween(300)) { -it / 2 } + fadeIn(tween(300)) },
        popExitTransition   = { slideOutHorizontally(tween(300)) { it / 4 } + fadeOut(tween(200)) }
    ) {
        composable("home") {
            HomeScreen(onNavigate = { route -> navController.navigate(route) })
        }
        composable("roadmap") {
            RoadmapScreen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("counselor") {
            CounselorScreen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer0") {
            Layer0Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer1") {
            Layer1Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer2") {
            Layer2Screen(onBack = { navController.popBackStack() })
        }
        composable("layer3") {
            Layer3Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer4") {
            Layer4Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer5") {
            Layer5Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer6") {
            Layer6Screen(onBack = { navController.popBackStack() })
        }
        composable("layer7") {
            Layer7Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer8") {
            Layer8Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer9") {
            Layer9Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
        composable("layer10") {
            Layer10Screen(onBack = { navController.popBackStack() }, vm = sharedVm)
        }
    }
}