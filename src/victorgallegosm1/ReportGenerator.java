/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package victorgallegosm1;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author 12546
 */
public class ReportGenerator {
    public static File saveCustomerReport(String customerName, List<Order> orders, String startDate, String endDate) {
        String html = generateCustomerHTML(customerName, orders, startDate, endDate);
        return saveHTMLToFile("CustomerReport_" + customerName + ".html", html);
    }

    public static File saveEmployeeReport(List<Customer> customers, String startDate, String endDate) {
        String html = generateEmployeeHTML(customers, startDate, endDate);
        return saveHTMLToFile("EmployeeReport.html", html);
    }

    private static String generateCustomerHTML(String customerName, List<Order> orders, String startDate, String endDate) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Customer Report</title><style>")
            .append("body { font-family: Arial; background-color: #f9f9f9; }")
            .append("table { width: 100%; border-collapse: collapse; }")
            .append("th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }")
            .append("th { background-color: #4CAF50; color: white; }")
            .append("tr:nth-child(even) { background-color: #f2f2f2; }")
            .append("</style></head><body>");
        html.append("<h2>Order History for ").append(customerName).append("</h2>");
        html.append("<p>Date range: ").append(startDate).append(" to ").append(endDate).append("</p>");
        html.append("<table><tr><th>Start Date</th><th>End Date</th><th>Lodge</th><th>Total</th></tr>");

        for (Order o : orders) {
            html.append("<tr><td>").append(o.getFormattedStartDate()).append("</td>")
                .append("<td>").append(o.getFormattedEndDate()).append("</td>")
                .append("<td>").append(o.getLodge().getName()).append("</td>")
                .append("<td>").append(String.format("$%.2f", o.getTotalCost())).append("</td></tr>");
        }

        html.append("</table></body></html>");
        return html.toString();
    }

    private static String generateEmployeeHTML(List<Customer> customers, String startDate, String endDate) {
        StringBuilder html = new StringBuilder();
        html.append("<html><head><title>Employee Report</title><style>")
            .append("body { font-family: Arial; background-color: #f9f9f9; }")
            .append("table { width: 100%; border-collapse: collapse; }")
            .append("th, td { border: 1px solid #ccc; padding: 8px; text-align: left; }")
            .append("th { background-color: #3333cc; color: white; }")
            .append("tr:nth-child(even) { background-color: #f2f2f2; }")
            .append("</style></head><body>");
        html.append("<h2>Customer Spending Report</h2>");
        html.append("<p>Date range: ").append(startDate).append(" to ").append(endDate).append("</p>");
        html.append("<table><tr><th>Customer Name</th><th>Total Spending</th></tr>");

        for (Customer c : customers) {
            double total = c.getOrdersWithin(startDate, endDate)
                           .stream().mapToDouble(Order::getTotalCost).sum();
            html.append("<tr><td>").append(c.getName()).append("</td>")
                .append("<td>").append(String.format("$%.2f", total)).append("</td></tr>");
        }

        html.append("</table></body></html>");
        return html.toString();
    }

    private static File saveHTMLToFile(String fileName, String htmlContent) {
        String docsPath = FileSystemView.getFileSystemView().getDefaultDirectory().getPath();
        File reportsDir = new File(docsPath, "Reports");

        if (!reportsDir.exists()) reportsDir.mkdirs();

        File htmlFile = new File(reportsDir, fileName);
        try (PrintWriter out = new PrintWriter(htmlFile)) {
            out.println(htmlContent);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return htmlFile;
    }
}
