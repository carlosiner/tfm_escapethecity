package uoc.tfm.escapethecity.data

data class Escape(
    var name: String = ""
    ,var info: String = ""
    ,var image: String = ""
    ,var enabled: Boolean = false
    ,var achievements: List<String>
    ,var escapeName: String = ""
)    {

}