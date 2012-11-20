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
import com.google.api.ads.dfp.axis.v201208.AdUnitSize;
import com.google.api.ads.dfp.axis.v201208.InventoryServiceInterface;
import com.google.api.ads.dfp.axis.v201208.TargetPlatform;
import com.google.api.ads.dfp.lib.client.DfpSession;

/**
 * This example gets all mobile target platform ad unit sizes.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: InventoryService.getAdUnitSizesByStatement
 *
 * @author Adam Rogal
 */
public class GetMobileAdUnitSizes {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the InventoryService.
    InventoryServiceInterface inventoryService =
        dfpServices.get(session, InventoryServiceInterface.class);

    // Create a statement to select ad unit sizes available for the mobile
    // platform.
    StatementBuilder statementBuilder = new StatementBuilder()
        .where("targetPlatform = :targetPlatform")
        .withBindVariableValue("targetPlatform", TargetPlatform.MOBILE.toString());

    // Get all ad unit sizes.
    AdUnitSize[] adUnitSizes =
        inventoryService.getAdUnitSizesByStatement(statementBuilder.toStatement());

    if (adUnitSizes != null) {
      for (int i = 0; i < adUnitSizes.length; i++) {
        AdUnitSize adUnitSize = adUnitSizes[i];
        System.out.printf("%s) Web ad unit size of dimensions %s was found.\n", i,
            adUnitSize.getFullDisplayString());
      }
    } else {
      System.out.println("No ad unit sizes found.");
    }
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
