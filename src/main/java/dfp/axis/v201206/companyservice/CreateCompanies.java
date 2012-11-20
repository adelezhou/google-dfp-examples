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

package dfp.axis.v201206.companyservice;

import com.google.api.ads.common.lib.auth.ClientLoginTokens;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201206.Company;
import com.google.api.ads.dfp.axis.v201206.CompanyServiceInterface;
import com.google.api.ads.dfp.axis.v201206.CompanyType;
import com.google.api.ads.dfp.lib.client.DfpSession;

import java.util.Random;

/**
 * This example creates new companies. To determine which companies exist, run
 * GetAllCompanies.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: CompanyService.createCompanies
 *
 * @author Adam Rogal
 */
public class CreateCompanies {

  public static void runExample(DfpServices dfpServices, DfpSession session) throws Exception {
    // Get the CompanyService.
    CompanyServiceInterface companyService =
        dfpServices.get(session, CompanyServiceInterface.class);

    // Create an advertiser.
    Company company1 = new Company();
    company1.setName("Advertiser #" + new Random().nextLong());
    company1.setType(CompanyType.ADVERTISER);

    // Create an agency.
    Company company2 = new Company();
    company2.setName("Agency #" + new Random().nextLong());
    company2.setType(CompanyType.AGENCY);

    // Create the companies on the server.
    Company[] companies = companyService.createCompanies(new Company[] {company1, company2});

    for (Company company : companies) {
      System.out.printf("A company with ID \"%d\", name \"%s\", and type \"%s\" was created.\n",
          company.getId(), company.getName(), company.getType());
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
