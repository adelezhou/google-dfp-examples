package dfp.axis.v201208;

import com.google.api.ads.common.lib.auth.ClientLoginTokens;
import com.google.api.ads.dfp.axis.factory.DfpServices;
import com.google.api.ads.dfp.axis.v201208.Team;
import com.google.api.ads.dfp.axis.v201208.TeamServiceInterface;
import com.google.api.ads.dfp.lib.client.DfpSession;


public class CreateTeam {

	public static void createTeam() {
		try {
			// Get a ClientLogin AuthToken.
			String clientLoginToken = new ClientLoginTokens.Builder()
					.forApi(ClientLoginTokens.Api.DFP).fromFile().build()
					.requestToken();

			// Construct a DfpSession.
			DfpSession session = new DfpSession.Builder().fromFile()
					.withClientLoginToken(clientLoginToken).build();

			DfpServices dfpServices = new DfpServices();

			// Get the TeamService.
			TeamServiceInterface teamService = dfpServices.get(session, TeamServiceInterface.class);

//			// Get DfpUser from "~/dfp.properties".
//			DfpUser user = new DfpUser();

			// Get the TeamService.
//			TeamServiceInterface teamService = user
//					.getService(DfpService.V201204.TEAM_SERVICE);

			// Create an array to store local team objects.
			Team[] teams = new Team[5];

			for (int i = 0; i < 5; i++) {
				Team team = new Team();
				team.setName("Team #" + i);
				team.setHasAllCompanies(false);
				team.setHasAllInventory(false);
				teams[i] = team;
			}

			// Create the teams on the server.
			// ERROR: Permission_Denied, should work after enable Teams feature in Network settings
			teams = teamService.createTeams(teams);

			if (teams != null) {
				for (Team team : teams) {
					System.out.println("A team with ID \"" + team.getId()
							+ "\", and name \"" + team.getName()
							+ "\" was created.");
				}
			} else {
				System.out.println("No teams created.");
			}

		} catch (Exception e) {
			System.out.printf("Exception is occurred in newAdUnitTest: ",
					e.getMessage());
		}
	}
	
	  public static void main(String[] args) throws Exception {

		  createTeam();
		  
	  }

}
