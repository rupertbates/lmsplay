package admin

import com.mongodb.casbah.commons.MongoDBObject
import helpers.MatchWeekHelper
import models.{MatchDay, MatchWeek, Team, Match}
import org.joda.time.{Interval, DateTime}
import repositories.{MatchWeekRepository, MatchRepository}
import java.io.File
import org.codehaus.jackson.map.ObjectMapper
import services.MatchService
import collection.parallel.mutable
import collection.mutable.ListBuffer

object ImportMatches {

  def createMatchWeeks(matches: List[Match]) = {
    //val weirdMatch = MatchRepository.findOne(MongoDBObject("kickOffTime" -> "ISODate(2012-01-11T19:45:00.000Z)"))

    val matchWeeks = matches
      .groupBy(m => m.matchWeek)
      .map(args => new MatchWeek(args._1, args._2(0).kickOffTime, args._2(0).kickOffTime, MatchService.groupMatchesByDay(args._2)))
      .toList
      .sortBy(_.number)

    var last = matchWeeks.head

    matchWeeks.tail.foreach(mw => {
        last.endDate = mw.startDate.minusMinutes(5)
        last = mw
      })
    matchWeeks
  }

//  def getMatchWeeks = {
//    MatchRepository.find(null)
//      .toList
//      .groupBy(m => m.matchWeek)
//      .map(args => new MatchWeek(args._1, args._2(0).kickOffTime, args._2(0).kickOffTime, MatchService.groupMatchesByDay(args._2)))
//      .toList
//      .sortBy(_.number)
//  }

  def importMatches() {
    loadGames().foreach(MatchWeekRepository.save(_))
  }

  def saveMongo(game: Match) {
    println("saving game " + game)

    //val stuff = MongoConnection()("test")("match")
    MatchRepository.insert(game)

  }

  def loadGames() = {
    val om = new ObjectMapper
    val filename = System.getProperty("user.dir") + "\\conf\\matches.json"
    val file = new File(filename)

    val node = om.readTree(file)
    val months = node.get("months")
    var matchBuffer = new ListBuffer[Match]
    var i = 0
    while (i < months.size()) {
      //Console.out.print(months.get(i).get("startDate"))
      val days = months.get(i).get("days")
      var j = 0

      while (j < days.size()) {
        val matches = days.get(j).get("matches")
        var k = 0
        while (k < matches.size()) {
          val gameNode = matches.get(k)
          val home = new Team(gameNode.get("homeTeam").findValue("name").asText(), gameNode.get("homeTeam").get("score").asInt(0))
          val away = new Team(gameNode.get("awayTeam").findValue("name").asText(), gameNode.get("awayTeam").get("score").asInt(0))
          val date = new DateTime(gameNode.get("dateTime").asText())
          matchBuffer += new Match(MatchWeekHelper.getMatchWeek(date), date, home, away)
          k = k + 1
        }
        j = j + 1
      }
      i = i + 1
    }
    createMatchWeeks(matchBuffer.toList)
  }


  //    def saveCouch(game: Match) = {
  //      var httpClient = new DefaultHttpClient();
  //      val postRequest = new HttpPut("http://localhost:5984/lms/game_" + UUID.randomUUID().toString);
  //
  //      var om = new ObjectMapper();
  //
  //      val module = new OptionModule with CaseClassModule
  //      om.registerModule(module)
  //
  //      var g = om.writeValueAsString(game);
  //      val input = new StringEntity(g);
  //
  //      //  val cp = new ContentProducer() {
  //      //    def writeTo(outStream : OutputStream): Unit = {
  //      //      var om = new ObjectMapper();
  //      //      //om.toString
  //      //      om.writeValue(outStream, game)
  //      //    }
  //      //  };
  //
  //
  //      //  var entity = new EntityTemplate(cp);
  //      input.setContentType("application/json");
  //      postRequest.setEntity(input);
  //
  //      val response = httpClient.execute(postRequest);
  //
  //      if (response.getStatusLine().getStatusCode() != 201) {
  //        throw new RuntimeException("Failed : HTTP error code : "
  //          + response.getStatusLine().getStatusCode());
  //      }
  //
  //      //  val br = new BufferedReader(
  //      //    new InputStreamReader((response.getEntity().getContent())));
  //
  //      //  var output: String = ""
  //      //  System.out.println("Output from Server .... \n");
  //      //  while ((output = br.readLine()) != null) {
  //      //    System.out.println(output);
  //      //  }
  //
  //      httpClient.getConnectionManager().shutdown();
  //
  //    }

}


