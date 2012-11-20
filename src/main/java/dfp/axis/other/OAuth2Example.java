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

package dfp.axis.other;

import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201208.Network;
import com.google.api.ads.dfp.axis.v201208.NetworkServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.auth.oauth2.CredentialRefreshListener;
import com.google.api.client.auth.oauth2.TokenErrorResponse;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * This example demonstrates how to authenticate using OAuth2.  This example
 * is meant to be run from the command line and requires user input.
 *
 * @author Adam Rogal
 */
public class OAuth2Example {

  private static final String SCOPE = "https://www.google.com/apis/ads/publisher";

  // This callback URL will allow you to copy the token from the success screen.
  private static final String CALLBACK_URL = "urn:ietf:wg:oauth:2.0:oob";

  // If you do not have a client ID or secret, please create one in the
  // API console: https://code.google.com/apis/console#access
  private static final String CLIENT_ID = "INSERT_CLIENT_ID_HERE";
  private static final String CLIENT_SECRET = "INSERT_CLIENT_SECRET_HERE";

  private static Credential getOAuth2Credential() throws Exception {
    GoogleAuthorizationCodeFlow authorizationFlow = new GoogleAuthorizationCodeFlow.Builder(
        new NetHttpTransport(),
        new JacksonFactory(),
        CLIENT_ID,
        CLIENT_SECRET,
        Lists.newArrayList(SCOPE))
        .setApprovalPrompt("force")
        // Set the access type to offline so that the token can be refreshed.
        // By default, the library will automatically refresh tokens when it
        // can, but this can be turned off by setting
        // dfp.api.refreshOAuth2Token=false in your ads.properties file.
        .setAccessType("offline").build();

    String authorizeUrl =
        authorizationFlow.newAuthorizationUrl().setRedirectUri(CALLBACK_URL).build();
    System.out.println("Paste this url in your browser: \n" + authorizeUrl + '\n');

    // Wait for the authorization code.
    System.out.println("Type the code you received here: ");
    String authorizationCode = new BufferedReader(new InputStreamReader(System.in)).readLine();

    // Authorize the OAuth2 token.
    GoogleAuthorizationCodeTokenRequest tokenRequest =
        authorizationFlow.newTokenRequest(authorizationCode);
    tokenRequest.setRedirectUri(CALLBACK_URL);
    GoogleTokenResponse tokenResponse = tokenRequest.execute();

    // Create the OAuth2 credential with custom refresh listener.
    GoogleCredential credential = new GoogleCredential.Builder()
        .setTransport(new NetHttpTransport())
        .setJsonFactory(new JacksonFactory())
        .setClientSecrets(CLIENT_ID, CLIENT_SECRET)
        .addRefreshListener(new CredentialRefreshListener() {
          public void onTokenResponse(Credential credential, TokenResponse tokenResponse) {
            // Handle success.
            System.out.println("Credential was refreshed successfully.");
          }

          public void onTokenErrorResponse(Credential credential,
              TokenErrorResponse tokenErrorResponse) {
            // Handle error.
            System.err.println("Credential was not refreshed successfully. "
                + "Redirect to error page or login screen.");
          }
        })

        // You can also add a credential store listener to have credentials
        // stored automatically.
        //.addRefreshListener(new CredentialStoreRefreshListener(userId, credentialStore))
        .build();

    // Set authorized credentials.
    credential.setFromTokenResponse(tokenResponse);

    // Though not necessary when first created, you can manually refresh the
    // token, which is needed after 60 minutes.
    credential.refreshToken();

    return credential;
  }

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
    if (CLIENT_ID.equals("INSERT_CLIENT_ID_HERE")
        || CLIENT_SECRET.equals("INSERT_CLIENT_SECRET_HERE")) {
      throw new IllegalArgumentException("Please input your client IDs or secret. "
          + "See https://code.google.com/apis/console#access");
    }

    // Get the OAuth2 credential.
    Credential credential = getOAuth2Credential();

    // Construct a DfpSession.
    DfpSession session = new DfpSession.Builder()
        .fromFile()
        .withOAuth2Credential(credential)
        .build();

    // Remove any network code set from the ads.properties file.
    session.setNetworkCode(null);

    DfpServices dfpServices = new DfpServices();

    runExample(dfpServices, session);
  }
}
