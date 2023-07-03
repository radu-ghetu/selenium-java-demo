import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {"src/test/java/com/brivo/features/"}
        , glue = {"com.web.steps"}
        , plugin = {"junit:build/reports/cucumber/cucumber-junit.xml",
        "json:build/reports/cucumber/cucumber.json",
        "rerun:target/rerun.txt"}
        , monochrome = true
)
public class CucumberRunner extends AbstractTestNGCucumberTests {
        @Override
        @DataProvider(parallel = true)
        public Object[][] scenarios() {
                return super.scenarios();
        }
}