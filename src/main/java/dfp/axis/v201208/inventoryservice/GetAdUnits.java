package dfp.axis.v201208.inventoryservice;

import com.google.api.ads.common.lib.auth.ClientLoginTokens;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201208.StatementBuilder;
import com.google.api.ads.dfp.axis.v201208.AdUnit;
import com.google.api.ads.dfp.axis.v201208.AdUnitPage;
import com.google.api.ads.dfp.axis.v201208.InventoryServiceInterface;
import com.google.api.ads.dfp.axis.v201208.InventoryStatus;
import com.google.api.ads.dfp.axis.v201208.Placement;
import com.google.api.ads.dfp.axis.v201208.PlacementPage;
import com.google.api.ads.dfp.axis.v201208.PlacementServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;

/**
 * This example gets all child ad units of the effective root ad unit. To create
 * ad units, run CreateAdUnits.java.
 * 
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 * 
 * Tags: InventoryService.getAdUnitsByStatement
 * 
 */
public class GetAdUnits {

	private static String adUnitId = "26880867"; // Web_ad_unit_-6510277081988074764

	private static void runExample(DfpServices dfpServices, DfpSession session)
			throws Exception {
		// Get the InventoryService.
		InventoryServiceInterface inventoryService = dfpServices.get(session,
				InventoryServiceInterface.class);

		// Get the PlacementService.
		PlacementServiceInterface placementService = dfpServices.get(session,
				PlacementServiceInterface.class);

		StatementBuilder statementBuilder = new StatementBuilder()
				.where("id = :id OR parentId = :id").limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
				.withBindVariableValue("id", adUnitId);  // select product level (1st) and page level (2st)

		// Default for total result set size.
		int totalResultSetSize = 0;

		do {
			// Get ad units by statement.
			AdUnitPage page = inventoryService
					.getAdUnitsByStatement(statementBuilder.toStatement());

			if (page.getResults() != null) {
				totalResultSetSize = page.getTotalResultSetSize();
		        int i = page.getStartIndex();
				for (AdUnit adUnit : page.getResults()) {
			          System.out.printf(
			                  "%s) Ad unit with ID \"%s\" and name \"%s\" was found.\n", i,
			                  adUnit.getId(), adUnit.getName());
					
					StatementBuilder statementBuilderForPlacement = new StatementBuilder()
							.where("status = :status")
							.limit(StatementBuilder.SUGGESTED_PAGE_LIMIT)
							.withBindVariableValue("status",
									InventoryStatus._ACTIVE);

					PlacementPage placementPage = placementService
							.getPlacementsByStatement(statementBuilderForPlacement
									.toStatement());
					Placement placement = placementPage.getResults(0);
					if (placement.getTargetedAdUnitIds(0).equalsIgnoreCase(
							adUnitId)) {
				          System.out.printf(
				                  "%s) Ad unit with Enabled for AdSense \"%s\" and Placements \"%s\" \n", i,
				                  adUnit.getInheritedAdSenseSettings()
									.getValue().getAdSenseEnabled()
									.toString(), placement.getId().toString());

					} else {

				          System.out.printf(
				                  "%s) Ad unit with Enabled for AdSense \"%s\" and Placements \"%s\" \n", i,
				                  adUnit.getInheritedAdSenseSettings()
									.getValue().getAdSenseEnabled()
									.toString(), "0");
					}
			          i++;


				}
			}

			statementBuilder
					.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
		} while (statementBuilder.getOffset() < totalResultSetSize);

	    System.out.printf("Number of results found: %s\n", totalResultSetSize);
	}

	  public static void main(String[] args) throws Exception {
		    // Get a ClientLogin AuthToken.
		    String clientLoginToken = new ClientLoginTokens.Builder()
		        .forApi(ClientLoginTokens.Api.DFP)
		        .fromFile()
		        .build()
		        .requestToken();

		    // Construct a DfpSession.
		    DfpSession session = new DfpSession.Builder()
		        .fromFile()
		        .withClientLoginToken(clientLoginToken)
		        .build();

		    DfpServices dfpServices = new DfpServices();

		    runExample(dfpServices, session);
		  }

}
