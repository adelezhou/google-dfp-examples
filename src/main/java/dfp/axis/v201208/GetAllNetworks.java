// Copyright 2012 Google Inc. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package dfp.axis.v201208;

import com.google.api.ads.common.lib.auth.ClientLoginTokens;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201208.Network;
import com.google.api.ads.dfp.axis.v201208.NetworkServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;

/**
 * This example gets all networks that you have access to with the current login
 * credentials. A networkCode should be left out for this request.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: NetworkService.getAllNetworks
 *
 * @author Adam Rogal
 */
public class GetAllNetworks {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the NetworkService.
    NetworkServiceInterface networkService =
        dfpServices.get(session, NetworkServiceInterface.class);

    // Get all networks that you have access to with the current login
    // credentials.
    Network[] networks = networkService.getAllNetworks();

    int i = 0;
    for (Network network : networks) {
      System.out.printf(
          "%s) Network with network code \"%s\" and display name \"%s\" was found.\n",
          i, network.getNetworkCode(), network.getDisplayName());
      i++;
    }

    System.out.printf("Number of networks found: %s\n", networks.length);
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

    // Remove any network code set from the ads.properties file.
    session.setNetworkCode(null);

    DfpServices dfpServices = new DfpServices();

    runExample(dfpServices, session);
  }
}
