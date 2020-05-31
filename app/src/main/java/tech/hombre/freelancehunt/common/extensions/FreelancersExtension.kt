package tech.hombre.freelancehunt.common.extensions

import androidx.annotation.ColorRes
import tech.hombre.freelancehunt.R
import tech.hombre.freelancehunt.common.FreelancerStatus


fun getFreelancerStatus(status: Int): FreelancerStatus {
    return FreelancerStatus.values().find { it.status == status } ?: FreelancerStatus.FREE
}

@ColorRes
fun getColorByFreelancerStatus(status: FreelancerStatus): Int {
    return when (status) {
        FreelancerStatus.FREE -> R.color.status_free
        FreelancerStatus.BIT_BUSY -> R.color.status_bit_busy
        FreelancerStatus.VERY_BUSY -> R.color.status_very_busy
        FreelancerStatus.TEMP_NOT_WORK -> R.color.status_not_work
        FreelancerStatus.ON_VOCATION -> R.color.status_on_vocation
    }
}

