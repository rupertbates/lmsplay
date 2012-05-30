package models

import org.codehaus.jackson.annotate.JsonIgnore

case class User(id: String, name : String, username : String, gender : String){
  var password : String = ""

}
