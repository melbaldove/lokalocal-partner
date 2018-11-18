package st.teamcataly.lokalocalpartner

/**
 * Created by aar on 19/12/2017.
 */

enum class ScaleType {
    FIT_CENTER,
    CENTER_CROP;


    companion object {
        @JvmStatic
        fun fromOrdinal(ordinal: Int): ScaleType? {
            return ScaleType.values().iterator().asSequence()
                    .filter { it.ordinal == ordinal }
                    .firstOrNull()
        }
    }
}