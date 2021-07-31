package com.toprest.navigation

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.tempo.reply.ui.ReviewReplyFragment
import com.toprest.R
import com.toprest.activity.main.MainActivity
import com.toprest.activity.start.StartActivity
import com.toprest.addrestaurant.ui.AddRestaurantFragment
import com.toprest.dashboard.ui.DashboardFragment
import com.toprest.home.ui.customer.CustomerHomeFragment
import com.toprest.landing.ui.LandingFragment
import com.toprest.leavereview.ui.LeaveReviewFragment
import com.toprest.login.ui.LoginActivity
import com.toprest.navigation.ext.*
import com.toprest.restaurantdetails.ui.RestaurantDetailsFragment
import com.toprest.ui.usertype.UserTypeSelectionFragment
import com.toprest.ui.registeremail.RegisterEmailFragment
import com.toprest.ui.registername.RegisterNameFragment
import com.toprest.ui.registerpassword.RegisterPasswordFragment
import com.toprest.ui.signup.SignUpActivity

private const val LAST_FRAGMENT = 0

private const val START_CONTAINER = R.id.start_container
private const val SIGN_UP_CONTAINER = R.id.signup_container
private const val MAIN_CONTAINER = R.id.main_container
private const val HOME_CONTAINER = R.id.home_container

class RouterImpl(
    private val activity: AppCompatActivity,
    fragmentManager: FragmentManager,
    closeableRouterContext: CloseableRouterContext
) : CloseableRouter(fragmentManager, closeableRouterContext), Router {

    override fun finishHostActivity() {
        activity.finish()
    }

    override fun clearAll() = fragmentManager.safeClearBackStack()

    override fun goBack() = dispatchOnMainThreadWithThrottle(this::goBackInternal)

    private fun goBackInternal() {
        if (!propagateBackToTopFragment(fragmentManager)) {
            if (fragmentManager.backStackEntryCount != LAST_FRAGMENT) {
                fragmentManager.popBackStackImmediate()
            } else {
                finishHostActivity()
            }
        }
    }

    override fun showStartActivity() {
        activity.startActivity(StartActivity.createIntent(activity))
        activity.finishAffinity()
    }

    override fun showMainActivity() {
        activity.startActivity(MainActivity.createIntent(activity))
        activity.finishAffinity()
    }

    override fun showLandingScreen() {
        fragmentManager.inTransaction {
            add(START_CONTAINER, LandingFragment.newInstance(), LandingFragment.TAG)
        }
    }

    override fun showSignUpFlow() {
        activity.startActivity(SignUpActivity.createIntent(activity))
    }

    override fun showRegisterName() {
        fragmentManager.inTransaction {
            add(SIGN_UP_CONTAINER, RegisterNameFragment.newInstance(), RegisterNameFragment.TAG)
        }
    }

    override fun showUserTypeSelection() {
        fragmentManager.inTransactionAndAddToBackStack(UserTypeSelectionFragment.TAG) {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(SIGN_UP_CONTAINER, UserTypeSelectionFragment.newInstance(), UserTypeSelectionFragment.TAG)
        }
    }

    override fun showRegisterEmail() {
        fragmentManager.inTransactionAndAddToBackStack(RegisterEmailFragment.TAG) {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(SIGN_UP_CONTAINER, RegisterEmailFragment.newInstance(), RegisterEmailFragment.TAG)
        }
    }

    override fun showRegisterPassword() {
        fragmentManager.inTransactionAndAddToBackStack(RegisterPasswordFragment.TAG) {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(SIGN_UP_CONTAINER, RegisterPasswordFragment.newInstance(), RegisterPasswordFragment.TAG)
        }
    }

    override fun showLogin() {
        activity.startActivity(LoginActivity.createIntent(activity))
    }

    override fun showDashboard() {
        fragmentManager.inTransaction {
            add(MAIN_CONTAINER, DashboardFragment.newInstance(), DashboardFragment.TAG)
        }
    }

    override fun showAddRestaurant() {
        fragmentManager.inTransactionAndAddToBackStack(AddRestaurantFragment.TAG) {
            add(MAIN_CONTAINER, AddRestaurantFragment.newInstance(), AddRestaurantFragment.TAG)
        }
    }

    override fun closeAddRestaurant() {
        markForClosing(AddRestaurantFragment.TAG)
    }

    override fun showLeaveReview(restaurantId: String) {
        fragmentManager.inTransactionAndAddToBackStack(LeaveReviewFragment.TAG) {
            add(MAIN_CONTAINER, LeaveReviewFragment.newInstance(restaurantId), LeaveReviewFragment.TAG)
        }
    }

    override fun closeLeaveReview() {
        markForClosing(LeaveReviewFragment.TAG)
    }

    override fun showReviewReply(restaurantId: String, reviewId: String) {
        fragmentManager.inTransactionAndAddToBackStack(ReviewReplyFragment.TAG) {
            add(MAIN_CONTAINER, ReviewReplyFragment.newInstance(restaurantId, reviewId), ReviewReplyFragment.TAG)
        }
    }

    override fun closeReviewReply() {
        markForClosing(ReviewReplyFragment.TAG)
    }

    override fun showCustomerHome() {
        fragmentManager.inTransaction {
            replace(HOME_CONTAINER, CustomerHomeFragment.newInstance(), CustomerHomeFragment.TAG)
        }
    }

    override fun showRestaurantDetails(restaurantId: String) {
        fragmentManager.inTransactionAndAddToBackStack(RestaurantDetailsFragment.TAG) {
            applyFadeInEnterAndFadeOutExitAnimation()
            add(MAIN_CONTAINER, RestaurantDetailsFragment.newInstance(restaurantId), RestaurantDetailsFragment.TAG)
        }
    }
}
