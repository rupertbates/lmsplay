package models

import com.novus.salat.annotations.raw.Key

case class Team(@Key("_id") name : String, score : Int) {
}
