package tech.hombre.freelancehunt.common

const val EMPTY_STRING = ""

const val EXTRA_1 = "EXTRA_1"
const val EXTRA_2 = "EXTRA_2"
const val EXTRA_3 = "EXTRA_3"
const val EXTRA_4 = "EXTRA_4"

const val SKU_PREMIUM = "premium"

const val CHANNEL_ID = "freelancehunt_channel_1"
const val NOTIFY_MESSAGE_ID = 9042
const val NOTIFY_FEED_ID = 9043
const val NOTIFY_PROJECTS_ID = 9044
const val MAX_LINES = 3

const val SIMPLE_DIALOG_REQUEST_ARBITRAGE = 1
const val SIMPLE_DIALOG_CLOSE_WORKSPACE = 2
const val SIMPLE_DIALOG_EXTEND_WORKSPACE = 3

const val REQUEST_CODE_FILEPICKER = 5326

const val ATTACH_MAX_FILESIZE: Long = 1024 * 1000

val ATTACH_FILE_EXTENSIONS: Array<String> = arrayOf("gif","jpeg","jpg","png","pdf","psd","gz","7z","docx","doc","zip","rar","rtf","odt","ott","ods","sxw","ai","gzip","cdr","mp3","xlsx","xls","txt","pptx","ppt","css","c","h","js","ico","htm","html","csv","yml","json","avi","eps","sketch")

enum class UserType(val type: String) {
    EMPLOYER("employer"),
    FREELANCER("freelancer")
}

enum class FeedType(val type: Int) {
    UNKNOWN(0),
    PROJECT(1),
    WORK(2),
    LIKE(3),
    FORUM_MESSAGE(4),
    PERSONAL_PROJECT(5),
    MESSAGE(6),
    APPRECIATED(7),
    VACANCY(8)
}

enum class FreelancerStatus(val status: Int) {
    FREE(10),
    BIT_BUSY(20),
    VERY_BUSY(30),
    TEMP_NOT_WORK(40),
    ON_VOCATION(50)
}

enum class ProjectStatus(val id: Int) {
    OPEN_FOR_PROPOSALS(11),
    PENDING_PAYMENT_RESERVATION(12),
    CONTRACTOR_CHOSEN(13),
    PROJECT_ONGOING(14),
    PROJECT_UNDER_ARBITRAGE(15),
    PROJECT_COMPLETE(21),
    CLOSED_WITHOUT_COMPLETION(22),
    PROJECT_EXPIRED(23),
    RULES_VIOLATED(24),
    PROJECT_NOT_COMPLETED(25),
    PROJECT_UNPAYED(26),
    CLOSED_WITHOUT_REVIEW(27),
    BLOCKED_BY_USERS(32),
    CLOSED_BY_MODERATOR(33),
    CLOSED_BECAUSE_OF_BUDGET(34)
}

enum class ContestStatus(val id: Int) {
    MODERATION(100),
    CLARIFICATION_REQUIRED(105),
    PENDING_PAYMENT_RESERVATION(110),
    OPEN_FOR_APPLICATIONS(130),
    FINAL(140),
    WINNER_CHOSEN(150),
    CONTEST_UNDER_ARBITRAGE(155),
    CONTEST_COMPLETE(210),
    WINNER_CHOOSING_EXPIRED(220),
    WINNER_NOT_CHOSEN(230),
    CLOSED_BY_MODERATOR(310),
    CONTEST_EXPIRED(320)
}

enum class SafeType(val type: String?) {
    EMPLOYER("employer"),
    DEVELOPER("developer")
}

enum class CurrencyType(val currency: String) {
    UAH("UAH"),
    RUB("RUB")
}

enum class BidStatus(val status: String?) {
    ACTIVE("active"),
    REVOKED("revoked"),
    REJECTED("rejected"),
}

enum class ThemeType(val theme: String) {
    LIGHT("light"),
    DARK("dark")
}

