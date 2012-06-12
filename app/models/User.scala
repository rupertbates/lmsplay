package models

import org.codehaus.jackson.annotate.JsonIgnore
import com.novus.salat.annotations.raw.Key

case class User(@Key("_id") id: Int,
                name : String,
                username : String,
                gender : String,
                games : Option[List[Game]]){
}
