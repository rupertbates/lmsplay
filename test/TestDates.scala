import helpers.CompetitionWeekHelper
import org.joda.time.DateTime
import org.specs2.mutable._
import services.MatchService


class TestDates extends Specification {
  "This game round" should {
    "start 2 days ago" in {
      val today = new DateTime(2012, 06, 16, 0, 0)
      val roundStart = MatchService.getRoundStart(today)
      roundStart.getDayOfMonth mustEqual 14
    }
    "start the previous Thursday when it is Wednesday" in {
      val today = new DateTime(2012, 06, 13, 0, 0)
      val roundStart = MatchService.getRoundStart(today)
      roundStart.getDayOfMonth mustEqual 7
    }
    "start the previous Thursday when it is Monday" in {
      val today = new DateTime(2012, 06, 18, 0, 0)
      val roundStart = MatchService.getRoundStart(today)
      roundStart.getDayOfMonth mustEqual 14
    }
  }
  "We" should{
    "be able to work out what game round it is from a date" in{
      CompetitionWeekHelper.getCompetitionWeek(new DateTime(2011, 8, 14, 0, 0)) mustEqual 1
      CompetitionWeekHelper.getCompetitionWeek(new DateTime(2011, 9, 4, 0, 0)) mustEqual 4
    }
  }

}
