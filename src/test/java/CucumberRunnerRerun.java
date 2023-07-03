import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(
        features = {"@target/rerun.txt"}
        , glue = {"com.web.steps"}
        , plugin = {"junit:build/reports/cucumber/cucumber-junit-rerun.xml",
        "json:build/reports/cucumber/cucumber-rerun.json"}
        , monochrome = true
)
public class CucumberRunnerRerun extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}