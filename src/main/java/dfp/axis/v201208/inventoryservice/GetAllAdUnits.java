// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dfp.axis.v201208.inventoryservice;

import com.google.api.ads.common.lib.auth.ClientLoginTokens;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.utils.v201208.StatementBuilder;
import com.google.api.ads.dfp.axis.v201208.AdUnit;
import com.google.api.ads.dfp.axis.v201208.AdUnitPage;
import com.google.api.ads.dfp.axis.v201208.InventoryServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * This example gets all ad units. To create ad units, run
 * CreateAdUnits.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: InventoryService.getAdUnitsByStatement
 *
 * @author Adam Rogal
 */
public class GetAllAdUnits {

  public static List<AdUnit> runExample(DfpServices dfpServices, DfpSession session)
      throws Exception {
    List<AdUnit> adUnits = Lists.newArrayList();

    // Get the InventoryService.
    InventoryServiceInterface inventoryService =
        dfpServices.get(session, InventoryServiceInterface.class);

    // Create a statement to select all ad units.
    StatementBuilder statementBuilder = new StatementBuilder()
        .limit(StatementBuilder.SUGGESTED_PAGE_LIMIT);

    // Default for total result set size.
    int totalResultSetSize = 0;

    do {
      // Get ad units by statement.
      AdUnitPage page = inventoryService.getAdUnitsByStatement(statementBuilder.toStatement());

      if (page.getResults() != null) {
        totalResultSetSize = page.getTotalResultSetSize();
        int i = page.getStartIndex();
        for (AdUnit adUnit : page.getResults()) {
          System.out.printf(
              "%s) Ad unit with ID \"%s\" and name \"%s\" was found.\n", i,
              adUnit.getId(), adUnit.getName());
          i++;
        }
        adUnits.addAll(Lists.<AdUnit>newArrayList(page.getResults()));
      }

      statementBuilder.increaseOffsetBy(StatementBuilder.SUGGESTED_PAGE_LIMIT);
    } while (statementBuilder.getOffset() < totalResultSetSize);

    System.out.printf("Number of results found: %s\n", totalResultSetSize);
    return adUnits;
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
