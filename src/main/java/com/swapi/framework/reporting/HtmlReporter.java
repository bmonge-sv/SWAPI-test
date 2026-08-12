package com.swapi.framework.reporting;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import org.testng.IReporter;
import org.testng.ISuite;
import org.testng.ISuiteResult;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.xml.XmlSuite;

public class HtmlReporter implements IReporter {

    private static final String REPORT_PATH = "target/swapi-report.html";

    @Override
    public void generateReport(List<XmlSuite> xmlSuites, List<ISuite> suites, String outputDirectory) {
        List<TestEntry> entries = new ArrayList<>();
        int passed = 0, failed = 0, skipped = 0;
        long totalDurationMs = 0;

        for (ISuite suite : suites) {
            for (ISuiteResult sr : suite.getResults().values()) {
                ITestContext ctx = sr.getTestContext();
                for (ITestResult r : sorted(ctx.getPassedTests().getAllResults())) {
                    entries.add(new TestEntry(r, "PASSED"));
                    passed++;
                    totalDurationMs += r.getEndMillis() - r.getStartMillis();
                }
                for (ITestResult r : sorted(ctx.getFailedTests().getAllResults())) {
                    entries.add(new TestEntry(r, "FAILED"));
                    failed++;
                    totalDurationMs += r.getEndMillis() - r.getStartMillis();
                }
                for (ITestResult r : sorted(ctx.getSkippedTests().getAllResults())) {
                    entries.add(new TestEntry(r, "SKIPPED"));
                    skipped++;
                    totalDurationMs += r.getEndMillis() - r.getStartMillis();
                }
            }
        }

        entries.sort(Comparator.comparing(e -> e.endpoint));

        int total = passed + failed + skipped;

        File report = new File(REPORT_PATH);
        report.getParentFile().mkdirs();
        try (FileWriter fw = new FileWriter(report)) {
            fw.write(buildHtml(passed, failed, skipped, total, totalDurationMs, entries));
        } catch (IOException e) {
            throw new RuntimeException("Cannot write HTML report to " + REPORT_PATH, e);
        }
        System.out.printf("[HtmlReporter] report written → %s%n", report.getAbsolutePath());
    }

    private List<ITestResult> sorted(Set<ITestResult> set) {
        return set.stream()
                .sorted(Comparator.comparing(r -> r.getTestClass().getName() + r.getMethod().getMethodName()))
                .collect(Collectors.toList());
    }

    // ─── Main HTML builder ────────────────────────────────────────────────────

    private String formatDuration(long ms) {
        if (ms < 60_000) return (ms / 1000) + "s";
        long m = ms / 60_000;
        long s = (ms % 60_000) / 1000;
        return s == 0 ? m + "m" : m + "m " + s + "s";
    }

    private String buildHtml(int passed, int failed, int skipped, int total, long totalDurationMs, List<TestEntry> entries) {
        String passedPct  = total == 0 ? "0.00" : String.format(Locale.ROOT, "%.2f", passed  * 100.0 / total);
        String failedPct  = total == 0 ? "0.00" : String.format(Locale.ROOT, "%.2f", failed  * 100.0 / total);
        String skippedPct = total == 0 ? "0.00" : String.format(Locale.ROOT, "%.2f", skipped * 100.0 / total);

        StringBuilder rows = new StringBuilder();
        boolean alt = false;
        for (TestEntry e : entries) {
            String trClass = alt ? " class=\"alt\"" : "";
            String color = switch (e.result) {
                case "PASSED"  -> "#69dd6ee0";
                case "FAILED"  -> "#e25754";
                default        -> "#eecf7a";
            };
            String statusCode = e.statusCode != null ? String.valueOf(e.statusCode) : "-";
            String perfColor  = PerformanceRating.badgeColor(e.performance);
            rows.append("<tr").append(trClass).append(">")
                .append("<td>").append(esc(e.endpoint)).append("</td>")
                .append("<td><span class=\"badge\" style=\"background:").append(color).append("\">")
                .append(e.result).append("</span></td>")
                .append("<td class=\"code-cell\">").append(statusCode).append("</td>")
                .append("<td>").append(e.durationMs).append(" ms</td>")
                .append("<td><span class=\"badge\" style=\"background:").append(perfColor).append("\">")
                .append(e.performance).append("</span></td>")
                .append("</tr>\n");
            alt = !alt;
        }

        String date = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("MMM d, yyyy", Locale.ENGLISH));
        String ts   = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        return htmlTemplate()
                .replace("{{TOTAL}}",       String.valueOf(total))
                .replace("{{PASSED}}",      String.valueOf(passed))
                .replace("{{FAILED}}",      String.valueOf(failed))
                .replace("{{SKIPPED}}",     String.valueOf(skipped))
                .replace("{{PASS_PCT}}",    passedPct)
                .replace("{{FAIL_PCT}}",    failedPct)
                .replace("{{SKIP_PCT}}",    skippedPct)
                .replace("{{EXEC_TIME}}",   formatDuration(totalDurationMs))
                .replace("{{ROWS}}",        rows.toString())
                .replace("{{DATE}}",        date)
                .replace("{{TIMESTAMP}}",   ts);
    }

    private String esc(String s) {
        return s == null ? "" : s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    // ─── HTML template ────────────────────────────────────────────────────────

    private String htmlTemplate() {
        return """
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8"/>
<title>SWAPI – Swapi Testing Report</title>
<style>
  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }
  body { font-family: 'Segoe UI', Arial, sans-serif; background: #f5f6fa; color: #333; font-size: 14px; }

  /* ── Header ─────────────────────────────────────────────────── */
  .header {
    background: #fff;
    padding: 15px 28px;
    display: flex; align-items: center; justify-content: space-between;
    border-bottom: 1px solid #e8e8e8;
    box-shadow: 0 1px 4px rgba(0,0,0,.06);
  }
  .header-title { font-size: 20px; font-weight: 700; color: #2c2c2c; }
  .header-meta  { text-align: right; font-size: 14px; color: #666; line-height: 2; }
  .api-badge    { display: flex; align-items: center; gap: 6px; justify-content: flex-end; }
  .api-dot      { width: 8px; height: 8px; border-radius: 50%; background: #1976d2; flex-shrink: 0; }

  /* ── Content ─────────────────────────────────────────────────── */
  .content { padding: 22px 28px; }

  /* ── Summary cards ───────────────────────────────────────────── */
  .cards { display: flex; gap: 16px; margin-bottom: 22px; }
  .card {
    background: #fff; border-radius: 8px; flex: 1;
    box-shadow: 0 1px 5px rgba(0,0,0,.08);
    padding: 18px 22px;
    border-left: 5px solid transparent;
  }
  .card-total   { border-left-color: #1976d2; }
  .card-passed  { border-left-color: #43a047; }
  .card-failed  { border-left-color: #e25754; }
  .card-skipped { border-left-color: #fb8c00; }
  .card-time    { border-left-color: #d566e9; }
  .card-label {
    font-size: 11px; font-weight: 700; color: #999;
    text-transform: uppercase; letter-spacing: .7px; margin-bottom: 10px;
  }
  .card-value { font-size: 42px; font-weight: 700; line-height: 1; }
  .card-total   .card-value { color: #1976d2; }
  .card-passed  .card-value { color: #43a047; }
  .card-failed  .card-value { color: #e25754; }
  .card-skipped .card-value { color: #fb8c00; }
  .card-time    .card-value { color: #d566e9; }
  .card-pct { font-size: 12px; color: #bbb; margin-top: 7px; }

  /* ── Endpoints table ─────────────────────────────────────────── */
  .section-hdr {
    font-size: 11px; font-weight: 700; color: #666;
    text-transform: uppercase; letter-spacing: .7px;
    display: flex; align-items: center; gap: 7px;
    padding-bottom: 9px; border-bottom: 1px solid #dde;
    margin-bottom: 14px;
  }
  table {
    width: 100%; border-collapse: collapse;
    background: #fff; border-radius: 8px;
    box-shadow: 0 1px 5px rgba(0,0,0,.07);
    overflow: hidden;
  }
  th {
    background: #f7f8fa; text-align: left; padding: 10px 18px;
    font-size: 11px; font-weight: 700; color: #555;
    text-transform: uppercase; letter-spacing: .6px;
    border-bottom: 2px solid #e4e6ea;
  }
  td { padding: 9px 18px; font-size: 13px; border-bottom: 1px solid #f2f2f2; }
  tr:last-child td { border-bottom: none; }
  tr.alt td { background: #fafbfc; }
  tr:hover td { background: #f0f4ff; transition: background .15s; }
  .badge {
    display: inline-block; padding: 3px 11px; border-radius: 12px;
    color: #fff; font-size: 11px; font-weight: 700; letter-spacing: .4px;
  }

  /* ── Footer ──────────────────────────────────────────────────── */
  .footer { text-align: right; font-size: 11px; color: #ccc; margin-top: 16px; }
</style>
</head>
<body>

<div class="header">
  <span class="header-title">Automation Test Report</span>
  <div class="header-meta">
    <div>&#128197; {{DATE}} &ndash; {{DATE}}</div>
    <div class="api-badge"><span class="api-dot"></span>API: SWAPI.dev</div>
  </div>
</div>

<div class="content">

  <!-- ─ Summary cards ──────────────────────────────────────────── -->
  <div class="cards">
    <div class="card card-total">
      <div class="card-label">Total Tests</div>
      <div class="card-value">{{TOTAL}}</div>
    </div>
    <div class="card card-passed">
      <div class="card-label">Passed</div>
      <div class="card-value">{{PASSED}}</div>
      <div class="card-pct">{{PASS_PCT}}%</div>
    </div>
    <div class="card card-failed">
      <div class="card-label">Failed</div>
      <div class="card-value">{{FAILED}}</div>
      <div class="card-pct">{{FAIL_PCT}}%</div>
    </div>
    <div class="card card-skipped">
      <div class="card-label">Skipped</div>
      <div class="card-value">{{SKIPPED}}</div>
      <div class="card-pct">{{SKIP_PCT}}%</div>
    </div>
    <div class="card card-time">
      <div class="card-label">Total Execution Time</div>
      <div class="card-value">{{EXEC_TIME}}</div>
    </div>
  </div>

  <!-- ─ Endpoints Summary ──────────────────────────────────────── -->
  <div>
    <div class="section-hdr">&#128196;&nbsp; Endpoints Summary</div>
    <table>
      <thead>
        <tr>
          <th>Endpoint</th>
          <th>Result</th>
          <th>Status Code</th>
          <th>Time</th>
          <th>Performance</th>
        </tr>
      </thead>
      <tbody>
        {{ROWS}}
      </tbody>
    </table>
  </div>

  <div class="footer">SWAPI Test Framework &mdash; {{TIMESTAMP}}</div>

</div>
</body>
</html>
""";
    }

    // ─── TestEntry ────────────────────────────────────────────────────────────

    private static class TestEntry {
        final String  endpoint;
        final String  result;
        final Integer statusCode;
        final long    durationMs;
        final String  performance;

        TestEntry(ITestResult r, String status) {
            Story classStory = r.getTestClass().getRealClass().getAnnotation(Story.class);
            String left;
            if (classStory != null) {
                left = classStory.value();
            } else {
                String cls = r.getTestClass().getName();
                left = cls.substring(cls.lastIndexOf('.') + 1).replace("Test", "");
            }

            Story methodStory = r.getMethod().getConstructorOrMethod()
                                  .getMethod().getAnnotation(Story.class);
            String right = methodStory != null
                    ? methodStory.value()
                    : r.getMethod().getMethodName();

            this.endpoint    = left + " :: " + right;
            this.result      = status;
            this.statusCode  = (Integer) r.getAttribute("statusCode");
            this.durationMs  = r.getEndMillis() - r.getStartMillis();
            this.performance = PerformanceRating.classify(this.durationMs);
        }
    }
}
