package net.ferraSolution.food.di

import net.ferraSolution.food.data.repository.*
import net.ferraSolution.food.data.repository.roomRepository.*
import net.ferraSolution.food.ui.bottomTabs.home.HomeFragmentViewModel
import net.ferraSolution.food.ui.bottomTabs.menu.MenuFragmentViewModel
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.foodDetails.RatingDialogViewModel
import net.ferraSolution.food.ui.bottomTabs.menu.foodsList.FoodsListFragmentViewModel
import net.ferraSolution.food.ui.bottomTabs.cart.CartFragmentViewModel
import net.ferraSolution.food.ui.bottomTabs.tools.ToolsFragmentViewModel
import net.ferraSolution.food.ui.map.AddressMapViewModel
import net.ferraSolution.food.ui.user.login.LoginFragmentViewModel
import net.ferraSolution.food.ui.user.signUp.SignUpFragmentViewModel
import net.ferraSolution.food.ui.user.verification.VerificationViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModules = module {

    viewModel { SignUpFragmentViewModel(get(), get(), get()) }
    viewModel { LoginFragmentViewModel(get()) }
    viewModel { HomeFragmentViewModel(get(), get(), get()) }
    viewModel { MenuFragmentViewModel(get(), get(), get(), get(), get()) }
    viewModel { FoodsListFragmentViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
    viewModel { RatingDialogViewModel(get(), get(), get(), get(), get()) }
    viewModel { CartFragmentViewModel(get()) }
    viewModel { ToolsFragmentViewModel() }
    viewModel { VerificationViewModel() }
    viewModel { AddressMapViewModel(get()) }

    factory { AuthRepository(get(), get()) }
    factory { HomeRepository(get(), get()) }
    factory { MenuRepository(get(), get()) }

    factory<CartRoomRepository> { CartRoomRepositoryImp(get()) }
    factory<MenuRoomRepository> { MenuRoomRepositoryImp(get()) }
    factory<UserRoomRepository> { UserRoomRepositoryImp(get()) }
    factory<AddressRepository> { AddressRepositoryImp(get()) }

}