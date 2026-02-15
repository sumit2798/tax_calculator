package io.helidon.examples.tax;

import jakarta.enterprise.context.RequestScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import io.helidon.security.annotations.Authenticated;

@Path("/tax")
@RequestScoped
@Authenticated
public class TaxResource {

    private static final double TAX_RATE_B2B = 0.10;
    private static final double TAX_RATE_B2C = 0.15;

    @POST
    @Path("/calculate")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response calculateTax(TaxRequest request) {
        if (request == null || request.getSource() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid request: Source is required"))
                    .build();
        }

        if ("CRM".equalsIgnoreCase(request.getSource())) {
            return calculateCrmTax(request);
        } else if ("BRM".equalsIgnoreCase(request.getSource())) {
            return calculateBrmTax(request);
        } else {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Unknown Source: " + request.getSource()))
                    .build();
        }
    }

    private double getTaxRate(String type) {
        if ("B2B".equalsIgnoreCase(type)) {
            return TAX_RATE_B2B;
        } else if ("B2C".equalsIgnoreCase(type)) {
            return TAX_RATE_B2C;
        }
        throw new IllegalArgumentException("Unknown tax type: " + type);
    }

    private Response calculateCrmTax(TaxRequest request) {
        if (request.getLineItems() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Line items required for CRM"))
                    .build();
        }

        double taxRate;
        try {
            taxRate = getTaxRate(request.getAccountType());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", e.getMessage()))
                    .build();
        }

        List<TaxResult> taxResults = new ArrayList<>();
        double totalTax = 0.0;

        for (LineItem item : request.getLineItems()) {
            double taxAmount = item.getAmount() * taxRate;
            // Round to 2 decimal places
            taxAmount = Math.round(taxAmount * 100.0) / 100.0;

            String taxRateStr = String.format("%.0f%%", taxRate * 100);
            TaxResult result = new TaxResult(item.getLineItemId(), taxAmount, taxRateStr);
            taxResults.add(result);
            totalTax += taxAmount;
        }

        // Round total tax to 2 decimal places
        totalTax = Math.round(totalTax * 100.0) / 100.0;

        CrmTaxResponse response = new CrmTaxResponse(taxResults, totalTax);
        return Response.ok(response).build();
    }

    private Response calculateBrmTax(TaxRequest request) {
        String amountStr = request.getAmountTaxed();
        if (amountStr == null || amountStr.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "AmountTaxed required for BRM"))
                    .build();
        }

        double taxRate;
        try {
            taxRate = getTaxRate(request.getTaxCode());
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", e.getMessage()))
                    .build();
        }

        try {
            double amountTaxed = Double.parseDouble(amountStr);
            double calculatedTax = amountTaxed * taxRate;

            double grossTaxAmount = amountTaxed + calculatedTax;

            BrmTaxResponse response = new BrmTaxResponse();
            response.setCalculatedTax(String.valueOf(calculatedTax));
            response.setTaxPercentage(String.format("%.0f%%", taxRate * 100)); // Convert 0.10 to 10%
            response.setGrossTaxAmount(String.valueOf(grossTaxAmount));
            response.setTaxCode(request.getTaxCode() != null ? request.getTaxCode() : "");
            response.setTransactionId(request.getTransactionId() != null ? request.getTransactionId() : "");

            return Response.ok(response).build();
        } catch (NumberFormatException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Collections.singletonMap("error", "Invalid AmountTaxed format"))
                    .build();
        }
    }
}
