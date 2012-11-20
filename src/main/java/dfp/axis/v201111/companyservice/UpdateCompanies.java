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

package dfp.axis.v201111.companyservice;

import com.google.api.ads.common.lib.auth.ClientLoginTokens;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201111.Company;
import com.google.api.ads.dfp.axis.v201111.CompanyServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;

/**
 * This example updates company comments. To determine which companies exist,
 * run GetAllCompanies.java.
 *
 * Credentials and properties in {@code fromFile()} are pulled from the
 * "ads.properties" file. See README for more info.
 *
 * Tags: CompanyService.getCompany
 *
 * @author Adam Rogal
 */
public class UpdateCompanies {

  // Set the ID of the company to update.
  static final String COMPANY_ID = "INSERT_COMPANY_ID_HERE";

  public static void runExample(DfpServices dfpServices, DfpSession session, Long companyId)
      throws Exception {
    // Get the CompanyService.
    CompanyServiceInterface companyService =
        dfpServices.get(session, CompanyServiceInterface.class);

    // Get the company.
    Company company = companyService.getCompany(companyId);

    // Update the comment.
    company.setComment(company.getComment() + " Updated.");

    // Update the companies on the server.
    Company[] companies = companyService.updateCompanies(new Company[] {company});

    for (Company updatedCompany : companies) {
      System.out.printf(
          "Company with ID \"%d\", name \"%s\", and comment \"%s\" was updated.\n",
          updatedCompany.getId(), updatedCompany.getName(), updatedCompany.getComment());
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

    Long companyId = Long.parseLong(COMPANY_ID);

    runExample(dfpServices, session, companyId);
  }
}
