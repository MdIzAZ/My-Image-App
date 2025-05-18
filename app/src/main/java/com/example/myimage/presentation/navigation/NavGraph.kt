package com.example.myimage.presentation.navigation

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.myimage.domain.repo.NetworkConnectivityObserver
import com.example.myimage.presentation.fovorite_screen.FavoriteScreen
import com.example.myimage.presentation.fovorite_screen.FavouriteViewModel
import com.example.myimage.presentation.fullimage_screen.FullImageScreen
import com.example.myimage.presentation.fullimage_screen.FullImageViewModel
import com.example.myimage.presentation.homes_screen.HomeScreen
import com.example.myimage.presentation.homes_screen.HomeVideModel
import com.example.myimage.presentation.profile_screen.ProfileScreen
import com.example.myimage.presentation.search_screen.SearchScreen
import com.example.myimage.presentation.search_screen.SearchVideModel
import kotlinx.serialization.Serializable

@Serializable
data object Home

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavGraph(
    navController: NavHostController,
    scrollBehavior: TopAppBarScrollBehavior,
    snackBarHostState: SnackbarHostState,
    connectivityObserver: NetworkConnectivityObserver,
) {
    NavHost(
        navController = navController,
        startDestination = Routes.HomeScreen
    ) {
        composable<Routes.HomeScreen> {
            val viewModel = hiltViewModel<HomeVideModel>()
            val editorialFieldImages = viewModel.homeImages.collectAsLazyPagingItems()
            val favouriteImageIds by viewModel.favouriteImageIds.collectAsStateWithLifecycle()

            HomeScreen(
                onRefresh = {  },
                scrollBehavior = scrollBehavior,
                images = editorialFieldImages,
                favouriteImageIds = favouriteImageIds,
                snackBarEvent = viewModel.snackBarEvent,
                snackBarHostState = snackBarHostState,
                onCardItemClick = {
                    navController.navigate(Routes.FullImageScreen(it))
                },
                onSearchIconClick = {
                    navController.navigate(Routes.SearchScreen)
                },
                onSaveIconClick = {
                    navController.navigate(Routes.FavoritesScreen)
                },
                connectivityObserver = connectivityObserver,
                onToggle = viewModel::toggleFavourite
            )
        }

        composable<Routes.SearchScreen> {
            val viewModel = hiltViewModel<SearchVideModel>()
            val searchedImages = viewModel.searchImages.collectAsLazyPagingItems()
            val favouriteImageIds by viewModel.favouriteImageIds.collectAsStateWithLifecycle()
            SearchScreen(
                searchedImages = searchedImages,
                snackBarEvent = viewModel.snackBarEvent,
                snackBarHostState = snackBarHostState,
                favouriteImageIds = favouriteImageIds,
                onSearch = viewModel::searchImages,
                onBackClick = { navController.navigateUp() },
                onImageCardClick = {
                    navController.navigate(Routes.FullImageScreen(it))
                },
                onToggle = viewModel::toggleFavourite
            )

        }

        composable<Routes.FavoritesScreen> {
            val viewModel = hiltViewModel<FavouriteViewModel>()
            val favouriteImages = viewModel.favouriteImages.collectAsLazyPagingItems()
            val favouriteImageIds by viewModel.favouriteImageIds.collectAsStateWithLifecycle()

            FavoriteScreen(
                scrollBehavior = scrollBehavior,
                favouriteImages = favouriteImages,
                snackBarEvent = viewModel.snackBarEvent,
                snackBarHostState = snackBarHostState,
                favouriteImageIds = favouriteImageIds,
                onImageCardClick = {
                    navController.navigate(Routes.FullImageScreen(it))
                },
                onBackClick = { navController.navigateUp() },
                onSearchIconClick = { navController.navigate(Routes.SearchScreen) },
                onToggle = viewModel::toggleFavourite
            )
        }

        composable<Routes.FullImageScreen> {
            val viewModel = hiltViewModel<FullImageViewModel>()
            val image = viewModel.image
            FullImageScreen(
                image,
                snackBarEvent = viewModel.snackBarEvent,
                snackBarHostState = snackBarHostState,
                onProfileImageClick = {
                    navController.navigate(Routes.ProfileScreen(it))
                },
                onBackButtonClick = { navController.navigateUp() },
                onDownloadOptionClick = { url, title ->
                    viewModel.downloadImage(url, title)
                }

            )
        }

        composable<Routes.ProfileScreen> {
            val args = it.toRoute<Routes.ProfileScreen>()
            ProfileScreen(
                profileLink = args.profileLink,
                onBackClick = { navController.navigateUp() }
            )
        }

    }
}