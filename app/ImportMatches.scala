import helpers.CompetitionWeekHelper
import models.{Team, Match}
import org.joda.time.DateTime
import repositories.MatchRepository

import com.fasterxml.jackson.module.scala.{CaseClassModule, OptionModule}
import java.io.File
import java.util.UUID
import org.apache.http.client.methods.HttpPut
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient
import org.codehaus.jackson.map.ObjectMapper




  object ImportMatches {
    def importMatches() {
      saveGames(saveMongo)
    }

    def saveMongo(game: Match) {
      println("saving game " + game)

      //val stuff = MongoConnection()("test")("match")
      MatchRepository.insert(game)

    }

    def saveGames(saveGame: Match => Unit) {
      val om = new ObjectMapper
      val filename = System.getProperty("user.dir") + "\\conf\\matches.json"
      val file = new File(filename)

      val node = om.readTree(file)
      val months = node.get("months")

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
            val game = new Match(CompetitionWeekHelper.getCompetitionWeek(date), date, home, away)
            saveGame(game)
            k = k + 1
          }
          j = j + 1
        }
        i = i + 1
      }
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


