package com.dibtr.util;

import static java.util.stream.Collectors.toList;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class CustomizedReport implements IReporter {
  private static final Logger LOGGER = LoggerFactory.getLogger(CustomizedReport.class);
  private Properties testParams = EnvironmentSettings.getInstance().getProperties();
  private static final String NA = "NA";
  private static final String REASON = "reason";
  private static final String REPORT_LINK = "reportLink";
  private static final String VISUAL_REPORT_LINK = "visualReportLink";
  private static final String TEST_STATUS = "testStatus";
  private static final String ROW_TEMPLATE =
      "<tr class=\"%s\"><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td><td>%s</td></tr>";

  /**
   * This function is implementation of generateReport function from IReporter interface. It will be
   * invoked by the CustomizedReport at the time of report generation.
   */
  public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites,
      String outputDirectory) {

    String reportTemplate = initReportTemplate();

    String appVersionNumber = "<header><center><b>" + "Build Type ::"
        + testParams.getProperty("application.build.type") + " Application Build Version :: "
        + testParams.getProperty("environment.application.build") + " Execution Environment "
        + testParams.getProperty("environment.code") + "</b></center></header>";
    reportTemplate = appVersionNumber.concat(reportTemplate);
    final String body = suites.stream().flatMap(suiteToResults()).collect(Collectors.joining());
    reportTemplate = reportTemplate.replaceFirst("</tbody>", String.format("%s</tbody>", body));
    try {
      saveReportTemplate(reportTemplate);
    } catch (IOException e) {
      LOGGER.error("ERROR : An exception was thrown while writing custom report to filesystem.", e);
    }
  }

  private Function<ISuite, Stream<? extends String>> suiteToResults() {
    return suite -> suite.getResults().entrySet().stream().flatMap(resultsToRows(suite));
  }

  private Function<Map.Entry<String, ISuiteResult>, Stream<? extends String>> resultsToRows(
      ISuite suite) {
    return e -> {
      ITestContext testContext = e.getValue().getTestContext();
      Set<ITestResult> failedTests = testContext.getFailedTests().getAllResults();
      Set<ITestResult> passedTests = testContext.getPassedTests().getAllResults();
      Set<ITestResult> skippedTests = testContext.getSkippedTests().getAllResults();
      String suiteName = suite.getName();
      return Stream.of(failedTests, passedTests, skippedTests)
          .flatMap(results -> generateReportRows(e.getKey(), suiteName, results).stream());
    };
  }

  private List<String> generateReportRows(String testName, String suiteName,
      Set<ITestResult> allTestResults) {
    return allTestResults.stream().map(testResultToResultRow(testName, suiteName))
        .collect(toList());
  }

  private Function<ITestResult, String> testResultToResultRow(String testName, String suiteName) {
    return testResult -> {
      switch (testResult.getStatus()) {
        case ITestResult.FAILURE:
          return String.format(ROW_TEMPLATE, "danger", this.getDeviceName(testResult),
              this.getTestCaseID(testResult), this.getTestCaseName(testResult),
              this.getTestResultStatus(testResult), this.getFailureReason(testResult),
              this.getPerfectoReportLink(testResult), this.getAppliToolsReportLink(testResult));

        case ITestResult.SUCCESS:
          return String.format(ROW_TEMPLATE, "success", this.getDeviceName(testResult),
              this.getTestCaseID(testResult), this.getTestCaseName(testResult),
              this.getTestResultStatus(testResult), this.getFailureReason(testResult),
              this.getPerfectoReportLink(testResult), this.getAppliToolsReportLink(testResult));

        case ITestResult.SKIP:
          return String.format(ROW_TEMPLATE, "warning", this.getDeviceName(testResult),
              this.getTestCaseID(testResult), this.getTestCaseName(testResult),
              this.getTestResultStatus(testResult), this.getFailureReason(testResult),
              this.getPerfectoReportLink(testResult), this.getAppliToolsReportLink(testResult));

        default:
          return "";
      }
    };
  }

  private String initReportTemplate() {
    String template = null;
    byte[] reportTemplate;
    try {
      reportTemplate = Files.readAllBytes(Paths.get("src/test/resources/reportTemplate.html"));
      template = new String(reportTemplate, StandardCharsets.UTF_8);
    } catch (IOException e) {
      LOGGER.error("Problem initializing template", e);
    }
    return template;
  }

  /**
   * This method will write the report to the file system in the ./target/surefire-reports/
   * directory, relative to the current directory. The report filename is my-report.html.
   * 
   * @param reportTemplate - the HTML markup containing the report content
   * @throws IOException
   */
  private void saveReportTemplate(String reportTemplate) throws IOException {
    String directory = Paths.get("") + System.getProperty("user.dir") + File.separator + "report"
        + File.separator + "custom-reports";
    new File(directory).mkdirs();
    try (PrintWriter reportWriter = new PrintWriter(
        new BufferedWriter(new FileWriter(new File(directory, "my-report.html"))));) {
      reportWriter.println(reportTemplate);
    } catch (SecurityException | IOException e) {
      LOGGER.error("ERROR : Exception thrown while saving custom report.", e);
    }
  }

  /**
   * This utility function is used to get Perfecto Report Link.
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Perfecto execution video URL.
   */
  private String getPerfectoReportLink(ITestResult iTestResult) {
    if (iTestResult.getAttribute(REPORT_LINK) != null) {
      return iTestResult.getAttribute(REPORT_LINK).toString();
    }
    return NA;
  }

  /**
   * This utility function is used to get AppliTools Report Link.
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Applitools execution video URL.
   */
  private String getAppliToolsReportLink(ITestResult iTestResult) {
    if (iTestResult.getAttribute(VISUAL_REPORT_LINK) != null) {
      return iTestResult.getAttribute(VISUAL_REPORT_LINK).toString();
    }
    return NA;
  }

  /**
   * This utility function is used to get Test failure reason..
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Test case failure reason.
   */
  private String getFailureReason(ITestResult iTestResult) {
    if (iTestResult.getAttribute(REASON) != null) {
      return iTestResult.getAttribute(REASON).toString();
    }
    return NA;
  }

  /**
   * This utility function is used to get Test result.
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Test case pass/fail/skip result.
   */
  private String getTestResultStatus(ITestResult iTestResult) {
    switch (iTestResult.getStatus()) {

      case ITestResult.FAILURE:
        if (iTestResult.getAttribute(TEST_STATUS) != null) {
          return iTestResult.getAttribute(TEST_STATUS).toString();
        }
        return "Fail";

      case ITestResult.SUCCESS:
        if (iTestResult.getAttribute(TEST_STATUS) != null) {
          return iTestResult.getAttribute(TEST_STATUS).toString();
        }
        return "Pass";
      case ITestResult.SKIP:
        if (iTestResult.getAttribute(TEST_STATUS) != null) {
          return iTestResult.getAttribute(TEST_STATUS).toString();
        }
        return "Skip";
      default:
        return "";
    }
  }

  /**
   * This utility function is used to get Test case ID.
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Rally Test case ID which is mapped to test.
   */
  private String getTestCaseID(ITestResult iTestResult) {
    if (iTestResult.getMethod().getDescription() != null) {
      return iTestResult.getMethod().getDescription();
    }
    return NA;
  }

  /**
   * This utility function is used to get Test case name.
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Test function name which is given to test.
   */
  private String getTestCaseName(ITestResult iTestResult) {
    return iTestResult.getMethod().getMethodName();
  }

  /**
   * This function is used to get Device Name.
   * 
   * @param iTestResult - ITestResult - Executed test instance.
   * @return - String - Device Name
   */
  private String getDeviceName(ITestResult iTestResult) {
    if (iTestResult.getAttribute("platformVersion") != null) {
      return iTestResult.getAttribute("platformVersion").toString();
    }
    return "Failed to Obtain device";
  }
}
