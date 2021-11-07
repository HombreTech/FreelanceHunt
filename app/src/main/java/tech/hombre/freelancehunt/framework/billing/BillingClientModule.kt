package tech.hombre.freelancehunt.framework.billing

import android.app.Activity
import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.android.billingclient.api.*
import com.android.billingclient.api.Purchase.PurchasesResult
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tech.hombre.data.local.LocalProperties
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.SKU_PREMIUM
import tech.hombre.freelancehunt.common.extensions.toast
import tech.hombre.freelancehunt.framework.tasks.TasksManger

class BillingClientModule(appContext: Application) : KoinComponent {

    private val appPreferences: LocalProperties by inject()

    private val tasksManger: TasksManger by inject()

    private val _isPremium = MutableLiveData<Boolean>()
    val isPremium: LiveData<Boolean>
        get() = _isPremium

    private val skuList = arrayListOf<String>(SKU_PREMIUM)

    private val skuDetailsList = arrayListOf<SkuDetails>()

    private var purchasesList: List<Purchase?>? = listOf<Purchase>()

    private val purchaseUpdateListener =
        PurchasesUpdatedListener { billingResult, purchases ->
            if (billingResult.responseCode == BillingClient.BillingResponseCode.OK && purchases != null) {
                purchasesList = purchases
                for (purchase in purchases) {
                    acknowledgePurchase(purchase)
                }
                setPremiumActions()
            }
        }

    private fun setPremiumActions() {
        val purchased = purchasesList?.any { it?.sku == SKU_PREMIUM }
        if (purchased == true) {
            _isPremium.value = true
        } else {
            _isPremium.value = false
            if (appPreferences.getWorkerInterval() < 120) {
                appPreferences.resetWorkerInterval()
                tasksManger.recreateTasks(
                    appPreferences.getWorkerFeedEnabled(),
                    appPreferences.getWorkerMessagesEnabled(),
                    appPreferences.getWorkerProjectsEnabled()
                )
            }
        }
    }

    fun isPremium() = _isPremium.value ?: false

    private var billingClient = BillingClient.newBuilder(appContext)
        .setListener(purchaseUpdateListener)
        .enablePendingPurchases()
        .build()

    fun init() {
        billingClient.startConnection(object : BillingClientStateListener {
            override fun onBillingSetupFinished(billingResult: BillingResult) {
                if (billingResult.responseCode == BillingClient.BillingResponseCode.OK) {
                    getSku()
                    purchasesList = getPurchases()
                    setPremiumActions()
                }
            }

            override fun onBillingServiceDisconnected() {
            }
        })
    }

    fun getSku() {
        val params = SkuDetailsParams.newBuilder()
        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP)
        billingClient.querySkuDetailsAsync(
            params.build()
        ) { responseCode, skuDetails ->
            if (responseCode.responseCode == 0) {
                skuDetails?.forEach {
                    skuDetailsList.add(it)
                }
            }
        }
    }

    private fun getPurchases(): List<Purchase?>? {
        val purchasesResult: PurchasesResult =
            billingClient.queryPurchases(BillingClient.SkuType.INAPP)
        return purchasesResult.purchasesList
    }

    private fun acknowledgePurchase(purchase: Purchase) {
        val acknowledgePurchaseResponseListener =
            AcknowledgePurchaseResponseListener { }
        if (purchase.purchaseState == Purchase.PurchaseState.PURCHASED) {
            if (!purchase.isAcknowledged) {
                val acknowledgePurchaseParams =
                    AcknowledgePurchaseParams.newBuilder()
                        .setPurchaseToken(purchase.purchaseToken)
                        .build()
                billingClient.acknowledgePurchase(
                    acknowledgePurchaseParams,
                    acknowledgePurchaseResponseListener
                )
            }
        }
    }

    fun launchBilling(activity: Activity, sku: String) {
        try {
            if (!billingClient.isReady) {
                toast(activity.getString(R.string.billing_not_ready))
                return
            }
            val flowParams = BillingFlowParams.newBuilder()
                .setSkuDetails(skuDetailsList.first { it.sku == sku })
                .build()
            billingClient.launchBillingFlow(activity, flowParams)
        } catch (e: Exception) {
            toast(e.message ?: "Launch billing error")
        }
    }
}